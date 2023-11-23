package com.example.seriesbackend.service;

import com.example.seriesbackend.dto.ParsedContentDto;

import java.util.List;

public interface PageParser {
    List<ParsedContentDto> parsePage(String url);
}
