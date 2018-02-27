package xyz.castensson.server.model;

import java.io.Serializable;

/**
 * Created by tobcas on 2018-02-25.
 */
public class UrlData implements Serializable {

    int id;
    String longUrl;
    String shortUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
