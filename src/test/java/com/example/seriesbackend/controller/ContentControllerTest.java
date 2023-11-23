package com.example.seriesbackend.controller;

import com.example.seriesbackend.dto.ContentDto;
import com.example.seriesbackend.entity.Content;
import com.example.seriesbackend.security.JwtTokenProvider;
import com.example.seriesbackend.service.impl.ContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ContentControllerTest {

    @Mock
    private ContentService contentService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private ContentController contentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllContent_ReturnsListOfContentDto() {
        // Arrange
        int page = 0;
        int pageSize = 10;
        String search = "";
        String sortBy = "";
        String sortDirection = "";
        String token = "jwt-token";
        String username = "testuser";
        List<ContentDto> expectedContentList = new ArrayList<>();
        when(contentService.findAllLike(page, pageSize, search, sortBy, sortDirection, username)).thenReturn(expectedContentList);
        when(jwtTokenProvider.getTokenFromBearer(token)).thenReturn("jwt-token");
        when(jwtTokenProvider.getUsername("jwt-token")).thenReturn(username);

        // Act
        List<ContentDto> result = contentController.getAllContent(page, pageSize, search, sortBy, sortDirection, token);

        // Assert
        assertEquals(expectedContentList, result);
        verify(contentService, times(1)).findAllLike(page, pageSize, search, sortBy, sortDirection, username);
        verify(jwtTokenProvider, times(1)).getUsername("jwt-token");
        verify(jwtTokenProvider, times(1)).getTokenFromBearer(token);
    }

    @Test
    void getContentById_ReturnsContentById() {
        // Arrange
        Long contentId = 1L;
        Content expectedContent = new Content();
        when(contentService.findContentById(contentId)).thenReturn(Optional.of(expectedContent));

        // Act
        ResponseEntity<Content> result = contentController.getContentById(contentId);

        // Assert
        assertEquals(expectedContent, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(contentService, times(1)).findContentById(contentId);
    }

    @Test
    void getContentById_ReturnsNotFoundWhenContentNotFound() {
        // Arrange
        Long contentId = 1L;
        when(contentService.findContentById(contentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Content> result = contentController.getContentById(contentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(contentService, times(1)).findContentById(contentId);
    }

    // Add more test cases for the remaining methods in the ContentController class
}
