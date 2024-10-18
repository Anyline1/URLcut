package ru.anyline.urlcut.service;


public interface UrlShortenerService {

    String shortenUrl(String originalUrl);
    String customUrl(String originalUrl, String customShortUrl);
    String updateShortUrl(String originalUrl, String newShortUrl);
    String getOriginalUrl(String shortUrl);

}
