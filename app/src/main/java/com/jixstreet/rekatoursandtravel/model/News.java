package com.jixstreet.rekatoursandtravel.model;

/**
 * Created by fachrifebrian on 9/2/15.
 */
public class News {
    String idNews;
    String titleNews;
    String descNews;

    public News(String idNews, String titleNews, String descNews) {
        this.idNews = idNews;
        this.titleNews = titleNews;
        this.descNews = descNews;
    }

    public String getIdNews() {
        return idNews;
    }

    public String getTitleNews() {
        return titleNews;
    }

    public String getDescNews() {
        return descNews;
    }
}
