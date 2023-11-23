package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.entity.Content;
import com.example.seriesbackend.entity.Source;
import com.example.seriesbackend.entity.SourceContent;
import com.example.seriesbackend.entity.SourceContentKey;
import com.example.seriesbackend.repository.ContentRepository;
import com.example.seriesbackend.repository.SourceContentRepository;
import com.example.seriesbackend.repository.SourceRepository;
import com.example.seriesbackend.service.impl.pageparser.MegogoPageParser;
import com.example.seriesbackend.service.impl.pageparser.SweetTvPageParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ContentAggregator {
    private final MegogoPageParser megogoPageParser;
    private final SweetTvPageParser sweetTvPageParser;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ContentRepository contentRepository;
    private final SourceContentRepository sourceContentRepository;
    private final SourceRepository sourceRepository;
    private final MailService mailService;

    private final static String MEGOGO_CONTENT_URL = "https://megogo.net/ua/search-extended?category_id=films&main_tab=filters&sort=popular";
    private final static String SWEET_TV_CONTENT_URL = "https://sweet.tv/movies/usi-filmi";

    //@EventListener(ApplicationReadyEvent.class)
    public void start(){
        scheduledExecutorService.scheduleAtFixedRate(this::aggregate, 0, 1, TimeUnit.DAYS);
    }

    @Transactional
    public void aggregate(){
        var megogoFuture = CompletableFuture.supplyAsync(() -> megogoPageParser.parsePage(MEGOGO_CONTENT_URL));
        var sweetTvFuture = CompletableFuture.supplyAsync(() -> sweetTvPageParser.parsePage(SWEET_TV_CONTENT_URL));

        var sourceContentMap = new HashMap<String, SourceContent>();
        var contentMap = new HashMap<String, Content>();
        var sourceMap = new HashMap<String, Source>();
        sourceContentRepository.findAll().forEach(sourceContent -> {
            sourceContentMap.put(sourceContent.getContent().getTitle(), sourceContent);
        });
        contentRepository.findAll().forEach(content -> {
            contentMap.put(content.getTitle().toUpperCase(), content);
        });
        sourceRepository.findAll().forEach(source -> {
            sourceMap.put(source.getName().name().toUpperCase(), source);
        });

        var aggregatedContent = Stream.of(megogoFuture, sweetTvFuture).map(CompletableFuture::join).flatMap(Collection::stream).toList();
        System.out.println(aggregatedContent.size());
        var newContents = aggregatedContent.stream().filter(contentDto -> {
                    var sourceContent = sourceContentMap.get(contentDto.getTitle());
                    return !(sourceContent != null &&
                            (sourceContent.getSourceContentId().equals(contentDto.getServiceId())
                                    && sourceContent.getSource().getName().equals(contentDto.getSourceType())));
                })
                .toList();
        System.out.println(newContents.size());
        var newSourceContents = newContents.stream().map(contentDto ->{
            try {
                var content = Optional.ofNullable(contentMap.get(contentDto.getTitle().toUpperCase().replaceAll("\\s+", " ")))
                        .orElseGet(() -> contentRepository.findContentByTitle(contentDto.getTitle().replaceAll("\\s+", " "))
                                .orElseGet(() -> contentRepository.save(new Content(contentDto.getTitle().replaceAll("\\s+", " "), contentDto.getImageUrl()))));
                var source = Optional.ofNullable(sourceMap.get(contentDto.getSourceType().name().toUpperCase()))
                        .orElseGet(() -> sourceRepository.save(new Source(contentDto.getSourceType())));
                return new SourceContent(new SourceContentKey(content.getId(), source.getId()), content, source, contentDto.getServiceId(), contentDto.getUrl(), contentDto.getImageUrl());
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
            return new SourceContent();
        }).toList();

        System.out.println("Saving source content");
        sourceContentRepository.saveAll(newSourceContents);

        System.out.println("Notifying users");
        newSourceContents.forEach(newSourceContent ->{
            newSourceContent.getContent().getSubscribedUsers().forEach(user -> {
                mailService.send(
                        user.getEmail(),
                        String.format("%s має нове надходження!", newSourceContent.getContent().getTitle()),
                        String.format("%s додав %s до своєї колекції. Переглянути можна тут: %s", newSourceContent.getSource().getName(), newSourceContent.getContent().getTitle(), newSourceContent.getUrl()));
            });
        });
        System.out.println("Aggregation is completed");
    }
}
