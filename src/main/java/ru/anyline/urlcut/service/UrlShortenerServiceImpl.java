package ru.anyline.urlcut.service;

import lombok.AllArgsConstructor;
import ru.anyline.urlcut.model.ShortenedUrl;
import ru.anyline.urlcut.repository.ShortenedUrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService{

    private final ShortenedUrlRepository repository;

    private final UrlCacheServiceImpl urlCacheServiceImpl;

    private static final String BASE_URL = "local/api/";

//    @Cacheable(key = "#originalUrl", value = "shortenUrl")
    @Override
    public String shortenUrl(String originalUrl) {

        Optional<ShortenedUrl> existingUrl = repository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return "Short URL already exists = " + BASE_URL + existingUrl.get().getShortUrl();
        }

        String generatedShortUrl = generateShortUrl();
        while (repository.findByShortUrl(generatedShortUrl).isPresent()) {
            generatedShortUrl = generateShortUrl();
        }

        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(originalUrl);
        shortenedUrl.setShortUrl(generatedShortUrl);
        System.out.println(originalUrl + " " + shortenedUrl);
        urlCacheServiceImpl.updateUrlInCache(originalUrl, generatedShortUrl);
        repository.save(shortenedUrl);
        return BASE_URL + generatedShortUrl;
    }

    @Override
    public String customUrl(String originalUrl, String customShortUrl) {

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
            urlCacheServiceImpl.updateUrlInCache(originalUrl, customShortUrl);
            repository.save(shortenedUrl);
            return BASE_URL + customShortUrl;
        } else {
            throw new IllegalArgumentException("Custom short URL cannot be null or empty.");
        }
    }


    @Override
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

    @Override
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

