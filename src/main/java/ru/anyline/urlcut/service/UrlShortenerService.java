package ru.anyline.urlcut.service;

import ru.anyline.urlcut.model.ShortenedUrl;

import java.util.List;

public interface UrlShortenerService {

    public String shortenUrl(String str);
    public String customUrl(String original, String customShortUrl);
    public String updateShortUrl(String original, String newShortUrl);
    public String getOriginalUrl(String shortUrl);
    public List<ShortenedUrl> getAllRepos();

}
