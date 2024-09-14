package ru.anyline.urlcut.service;

public interface UrlCacheService {

    public String getUrlFromCache(String url);
    public void updateUrlInCache(String url, String shortenedUrl);
    public boolean hasKey(String url);
}
