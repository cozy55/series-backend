package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.dto.ParsedContentDto;
import com.example.seriesbackend.entity.Content;
import com.example.seriesbackend.entity.Source;
import com.example.seriesbackend.entity.SourceContent;
import com.example.seriesbackend.entity.SourceContentKey;
import com.example.seriesbackend.repository.ContentRepository;
import com.example.seriesbackend.repository.SourceContentRepository;
import com.example.seriesbackend.repository.SourceRepository;
import com.example.seriesbackend.service.impl.pageparser.MegogoPageParser;
import com.example.seriesbackend.service.impl.pageparser.SweetTvPageParser;
import com.example.seriesbackend.service.impl.MailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ContentAggregatorTest {

    @Mock
    private MegogoPageParser megogoPageParser;

    @Mock
    private SweetTvPageParser sweetTvPageParser;

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private SourceContentRepository sourceContentRepository;

    @Mock
    private SourceRepository sourceRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private ContentAggregator contentAggregator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    void aggregate_CallsParsersAndSavesSourceContents() {
        // Arrange
        String megogoContentUrl = "https://megogo.net/ua/search-extended?category_id=films&main_tab=filters&sort=popular";
        String sweetTvContentUrl = "https://sweet.tv/movies/usi-filmi";

        List<ParsedContentDto> megogoContent = new ArrayList<>();
        List<ParsedContentDto> sweetTvContent = new ArrayList<>();

        when(megogoPageParser.parsePage(megogoContentUrl)).thenReturn(megogoContent);
        when(sweetTvPageParser.parsePage(sweetTvContentUrl)).thenReturn(sweetTvContent);

        Content content = new Content("Title", "Image URL");
        Source source = new Source(Source.SourceType.MEGOGO);
        SourceContentKey sourceContentKey = new SourceContentKey(content.getId(), source.getId());
        SourceContent existingSourceContent = new SourceContent(sourceContentKey, content, source, 11L, "URL", "Image URL");

        Map<String, SourceContent> sourceContentMap = new HashMap<>();
        sourceContentMap.put("Title", existingSourceContent);

        Map<String, Content> contentMap = new HashMap<>();
        contentMap.put("TITLE", content);

        Map<String, Source> sourceMap = new HashMap<>();
        sourceMap.put("SOURCETYPE", source);

        when(sourceContentRepository.findAll()).thenReturn(new ArrayList<>(sourceContentMap.values()));
        when(contentRepository.findAll()).thenReturn(new ArrayList<>(contentMap.values()));
        when(sourceRepository.findAll()).thenReturn(new ArrayList<>(sourceMap.values()));


        ParsedContentDto newContentDto = new ParsedContentDto(11L, "New Title", "URL", "Image URL", Source.SourceType.MEGOGO);
        megogoContent.add(newContentDto);

        // Act
        contentAggregator.aggregate();

        // Assert
        verify(sourceContentRepository, times(1)).saveAll(anyList());
    }
}
