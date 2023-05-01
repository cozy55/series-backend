package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.dto.ContentDto;
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

    public List<ContentDto> parsePage(String url){
        try {
            var doc = Jsoup.connect(url).get();
            //System.out.println(doc);
            var elements = doc.getElementsByClass("videoItem");
            var contents = elements.stream().map(element -> {
                var serviceId = Integer.parseInt(element.attr("data-obj-id"));
                var title = element.attr("data-title");
                var contentUrl = element.getElementsByTag("a").attr("href");
                return new ContentDto(serviceId, title, contentUrl);
            }).toList();

            var pagination = doc.getElementsByClass("pagination-more");

            var list = new ArrayList<>(contents);
            if(!pagination.isEmpty()){
                var moreUrl = pagination.get(0).getElementsByTag("a").attr("href");
                list.addAll(parsePage(URI.create(url).resolve(moreUrl).toString()));
            }

            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
