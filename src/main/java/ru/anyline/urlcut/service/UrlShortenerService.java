package ru.anyline.urlcut.service;

import ru.anyline.urlcut.model.ShortenedUrl;
import ru.anyline.urlcut.repository.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    @Autowired
    private ShortenedUrlRepository repository;

    private static final String BASE_URL = "http://localhost:8080/";

    public String shortenUrl(String originalUrl) {
        Optional<ShortenedUrl> existing = repository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return BASE_URL + existing.get().getShortUrl();
        }

        String shortUrl = generateShortUrl();
        while (repository.findByShortUrl(shortUrl).isPresent()) {
            shortUrl = generateShortUrl();
        }

        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(originalUrl);
        shortenedUrl.setShortUrl(shortUrl);
        repository.save(shortenedUrl);

        return BASE_URL + shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        Optional<ShortenedUrl> shortenedUrl = repository.findByShortUrl(shortUrl);
        return shortenedUrl.map(ShortenedUrl::getOriginalUrl).orElse(null);
    }

    public List<ShortenedUrl> getAllRepos(){
        return repository.findAll();
    }




    private String generateShortUrl() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8).replaceAll("[^a-zA-Z0-9]", "");
    }
}

