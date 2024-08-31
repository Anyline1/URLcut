package ru.anyline.urlcut.service;

import ru.anyline.urlcut.model.ShortenedUrl;
import ru.anyline.urlcut.repository.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    private final ShortenedUrlRepository repository;

    private static final String BASE_URL = "local/api/";

    @Autowired
    public UrlShortenerService(ShortenedUrlRepository repository) {
        this.repository = repository;
    }

    public String shortenUrl(String originalUrl, String customShortUrl) {

        Optional<ShortenedUrl> existingUrl = repository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return "Short URL already exists = " + BASE_URL + existingUrl.get().getShortUrl();
        }

        if (customShortUrl != null && !customShortUrl.isEmpty()) {

            Optional<ShortenedUrl> existingCustomUrl = repository.findByShortUrl(customShortUrl);
            if (existingCustomUrl.isPresent()) {
                throw new IllegalArgumentException("Custom short URL is already in use.");
            }

            ShortenedUrl shortenedUrl = new ShortenedUrl();
            shortenedUrl.setOriginalUrl(originalUrl);
            shortenedUrl.setShortUrl(customShortUrl);
            repository.save(shortenedUrl);
            return BASE_URL + customShortUrl;
        } else {

            String generatedShortUrl = generateShortUrl();
            while (repository.findByShortUrl(generatedShortUrl).isPresent()) {
                generatedShortUrl = generateShortUrl();
            }

            ShortenedUrl shortenedUrl = new ShortenedUrl();
            shortenedUrl.setOriginalUrl(originalUrl);
            shortenedUrl.setShortUrl(generatedShortUrl);
            repository.save(shortenedUrl);
            return BASE_URL + generatedShortUrl;
        }
    }

    public String updateShortUrl(String originalUrl, String newShortUrl) {
        Optional<ShortenedUrl> existingUrl = repository.findByOriginalUrl(originalUrl);

        if (existingUrl.isEmpty()) {
            throw new IllegalArgumentException("Original URL does not exist.");
        }

        if (newShortUrl != null && !newShortUrl.isEmpty()) {
            Optional<ShortenedUrl> existingShortUrl = repository.findByShortUrl(newShortUrl);
            if (existingShortUrl.isPresent()) {
                throw new IllegalArgumentException("New short URL is already in use.");
            }

            ShortenedUrl shortenedUrl = existingUrl.get();
            shortenedUrl.setShortUrl(newShortUrl);
            repository.save(shortenedUrl);
            return BASE_URL + newShortUrl;
        } else {
            throw new IllegalArgumentException("New short URL cannot be null or empty.");
        }
    }

    public String getOriginalUrl(String shortUrl) {
        Optional<ShortenedUrl> shortenedUrl = repository.findByShortUrl(shortUrl);
        return shortenedUrl.map(ShortenedUrl::getOriginalUrl).orElse(null);
    }

    public List<ShortenedUrl> getAllRepos(){
        return repository.findAll();
    }

    private String generateShortUrl() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrl = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }
}

