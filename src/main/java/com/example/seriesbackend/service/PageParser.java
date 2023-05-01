package com.example.seriesbackend.service;

import com.example.seriesbackend.dto.ContentDto;

import java.util.List;

public interface PageParser {
    List<ContentDto> parsePage(String url);
}
