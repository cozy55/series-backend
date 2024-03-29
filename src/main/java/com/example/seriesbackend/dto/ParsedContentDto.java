package com.example.seriesbackend.dto;

import com.example.seriesbackend.entity.Source;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedContentDto {
    Long serviceId;
    String title;
    String url;
    String imageUrl;
    Source.SourceType sourceType;
}
