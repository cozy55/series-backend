package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.dto.ContentDto;
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

    @Override
    public List<ContentDto> parsePage(String url){
        try {
            var doc = Jsoup.connect(url).get();
            var contents = new ArrayList<>(
                    doc.getElementsByClass("videoItem").stream().map(element -> {
                        var serviceId = Integer.parseInt(element.attr("data-obj-id"));
                        var title = element.attr("data-title");
                        var contentUrl = element.getElementsByTag("a").attr("href");
                        return new ContentDto(serviceId, title, contentUrl);
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
