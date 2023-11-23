package com.example.seriesbackend.dto;

import com.example.seriesbackend.entity.Source;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContentDto {

    Long id;
    String title;
    String imageUrl;
    List<SourceContentDto> sourceContentDtoList;
    boolean isSubscribed;

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SourceContentDto{
        String url;
        String imageUrl;
        Source.SourceType sourceType;
    }

    public ContentDto(Long id, String title, String imageUrl, List<SourceContentDto> sourceContentDtoList) {
        this.id = id;
        this.title = title;
        this.imageUrl =imageUrl;
        this.sourceContentDtoList = sourceContentDtoList;
    }
}
