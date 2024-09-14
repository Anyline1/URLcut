package ru.anyline.urlcut.service;

import ru.anyline.urlcut.model.ShortenedUrl;

import java.util.List;

public interface UrlShortenerService {

    String shortenUrl(String originalUrl);
    String customUrl(String originalUrl, String customShortUrl);
    String updateShortUrl(String originalUrl, String newShortUrl);
    String getOriginalUrl(String shortUrl);

}
