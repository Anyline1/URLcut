package ru.anyline.urlcut.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlCacheServiceImpl implements UrlCacheService{
    private final RedisTemplate<String, String> redisTemplate;

    public UrlCacheServiceImpl(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Cacheable(value = "cachedUrl", key = "#url")
    @Override
    public String getUrlFromCache(String url) {

        return redisTemplate.opsForValue().get(url);
    }

    @CachePut(value = "cachedUrl", key = "#url")
    @Override
    public void updateUrlInCache(String url, String shortenedUrl) {

        redisTemplate.opsForValue().set(url, shortenedUrl);
    }

    @Override
    public boolean hasKey(String url){

        return Boolean.TRUE.equals(redisTemplate.hasKey(url));
    }
}
