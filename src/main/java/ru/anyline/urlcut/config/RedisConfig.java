package ru.anyline.urlcut.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.anyline.urlcut.model.ShortenedUrl;

@Configuration
@EnableCaching
public class RedisConfig {

//    @Bean
//    public CacheManager cacheManager(
//            RedisConnectionFactory redisConnectionFactory
//    ) {
//        RedisCacheConfiguration configuration = RedisCacheConfiguration
//                .defaultCacheConfig()
//                .serializeKeysWith(
//                        RedisSerializationContext
//                                .SerializationPair
//                                .fromSerializer(new StringRedisSerializer())
//                )
//                .serializeValuesWith(
//                        RedisSerializationContext
//                                .SerializationPair
//                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
//                );
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(configuration)
//                .build();
//    }

    @Bean
    public RedisSerializer<Object> redisSerializer(ObjectMapper objectMapper) {
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer<Object> redisSerializer) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(redisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(redisSerializer);
        return template;
    }
}
