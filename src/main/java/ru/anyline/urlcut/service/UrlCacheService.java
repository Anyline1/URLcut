package ru.anyline.urlcut.service;

public interface UrlCacheService {

    String getUrlFromCache(String url);
    void updateUrlInCache(String url, String shortenedUrl);
    boolean hasKey(String url);

}
