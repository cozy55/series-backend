package com.example.seriesbackend.service.impl.pageparser;

import com.example.seriesbackend.dto.ParsedContentDto;
import com.example.seriesbackend.entity.Source;
import com.example.seriesbackend.entity.SourceContent;
import com.example.seriesbackend.exception.FailedParsingException;
import com.example.seriesbackend.service.PageParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MegogoPageParser implements PageParser {
    int i = 0;

    @Override
    public List<ParsedContentDto> parsePage(String url){
        try {
            System.out.println("MEGOGO" + (++i));
            var doc = Jsoup.connect(url).get();
            var contents = new ArrayList<>(
                    doc.getElementsByClass("videoItem").stream().map(element -> {
                        var serviceId = Long.parseLong(element.attr("data-obj-id"));
                        var title = element.attr("data-title");
                        var contentUrl = element.getElementsByTag("a").attr("href");
                        var imageUrl = element.getElementsByTag("img").get(0).attr("data-original");
                        return new ParsedContentDto(serviceId, title, contentUrl, imageUrl, Source.SourceType.MEGOGO);
                    }).toList()
            );

            var pagination = doc.getElementsByClass("pagination-more");
            if(!pagination.isEmpty()){
                var moreUrl = pagination.get(0).getElementsByTag("a").attr("href");
                contents.addAll(parsePage(URI.create(url).resolve(moreUrl).toString()));
            }

            return contents;
        } catch (IOException e) {
            throw new FailedParsingException(String.format("Failed to parse Megogo page by %s url ", url));
        }
    }
}
