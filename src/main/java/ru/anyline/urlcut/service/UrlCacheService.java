package ru.anyline.urlcut.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.anyline.urlcut.model.ShortenedUrl;

@AllArgsConstructor
@Service
public class UrlCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    @Cacheable(value = "cachedUrl", key = "#url")
    public String getUrlFromCache(String url) {

        return redisTemplate.opsForValue().get(url);
    }

    @CachePut(value = "cachedUrl", key = "#url")
    public void updateUrlInCache(String url, String shortenedUrl) {

        redisTemplate.opsForValue().set(url, shortenedUrl);
    }
}
