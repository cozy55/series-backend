package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.dto.ContentDto;
import com.example.seriesbackend.exception.FailedParsingException;
import com.example.seriesbackend.service.PageParser;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SweetTvPageParser implements PageParser {
    private final static Pattern CONTENT_ID_PATTERN = Pattern.compile("\\/(\\d+)-");

    @Override
    public List<ContentDto> parsePage(String url) {
        try {
            System.out.println("page");
            var doc = Jsoup.connect(url).get();
            var contents = new ArrayList<>(
                    doc.getElementsByClass("movie__item-link")
                    .stream().map(element -> {
                        var title = element.getElementsByClass("img_wauto_hauto").get(0).attr("title");
                        title = title.substring(0, title.lastIndexOf(" - SWEET.TV"));
                        var contentUrl = element.attr("href");
                        var idMatcher = CONTENT_ID_PATTERN.matcher(contentUrl);
                        idMatcher.find();
                        var id = Integer.parseInt(idMatcher.group(1));
                        return new ContentDto(id, title, contentUrl);
                    }).toList()
            );

            var pagination = doc.getElementsByClass("pagination").get(0);
            var pageItems = pagination.children();
            var currentItem = pagination.getElementsByClass("active").get(0);
            var index = pageItems.indexOf(currentItem) + 1;
            if(index < pageItems.size())
            {
                contents.addAll(parsePage(pageItems.get(index).getElementsByTag("a").get(0).attr("href")));
            }

            return contents;
        } catch (IOException e) {
            throw new FailedParsingException(String.format("Failed to parse SweetTv page by %s url ", url));
        }
    }

}
