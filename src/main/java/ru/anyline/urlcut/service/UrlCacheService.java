package ru.anyline.urlcut.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.anyline.urlcut.model.ShortenedUrl;

@Service
public class UrlCacheService {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UrlCacheService(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Cacheable(value = "cachedUrl", key = "#url")
    public String getUrlFromCache(String url) {

        return redisTemplate.opsForValue().get(url);
    }

    @CachePut(value = "cachedUrl", key = "#url")
    public void updateUrlInCache(String url, String shortenedUrl) {

        redisTemplate.opsForValue().set(url, shortenedUrl);
    }

    public boolean hasKey(String url){

        return Boolean.TRUE.equals(redisTemplate.hasKey(url));
    }
}
