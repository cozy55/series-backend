package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.dto.ContentDto;
import com.example.seriesbackend.entity.Content;
import com.example.seriesbackend.entity.User;
import com.example.seriesbackend.repository.ContentRepository;
import com.example.seriesbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContentServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContentService contentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsListOfContent() {
        // Arrange
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Content());
        contentList.add(new Content());
        when(contentRepository.findAll()).thenReturn(contentList);

        // Act
        List<Content> result = contentService.findAll();

        // Assert
        assertEquals(contentList, result);
        verify(contentRepository, times(1)).findAll();
    }

    @Test
    void findContentById_ReturnsContentById() {
        // Arrange
        Long contentId = 1L;
        Content content = new Content();
        when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));

        // Act
        Optional<Content> result = contentService.findContentById(contentId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(content, result.get());
        verify(contentRepository, times(1)).findById(contentId);
    }
}
