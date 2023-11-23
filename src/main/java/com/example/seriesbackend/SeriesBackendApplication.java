package com.example.seriesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SeriesBackendApplication {

    public static void main(String[] args) {
        //var l = new SweetTvPageParser().parsePage("https://sweet.tv/movies/usi-filmi");
        //System.out.println(l.size());
        //System.out.println(l);
        //l = new MegogoPageParser().parsePage("https://megogo.net/ua/search-extended?category_id=films&main_tab=filters&sort=popular");
        //System.out.println(l.size());
        //l = new MegogoPageParser().parsePage("https://megogo.net/ua/search-extended?category_id=mult&main_tab=filters&sort=popular");
        //System.out.println(l.size());
        SpringApplication.run(SeriesBackendApplication.class, args);
    }

}
