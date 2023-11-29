package com.team5.secondhand.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    public CacheManager redisCacheManager() {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfiguration())
                .withInitialCacheConfigurations(CacheConfigurations())
                .build();
    }

    @Bean(name = "defaultCacheConfiguration")
    public RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofSeconds(3))
                .disableCachingNullValues();
    }

    @Bean
    public Map<String, RedisCacheConfiguration> CacheConfigurations() {
        return Map.of(
                CacheKey.MEMBER, defaultCacheConfiguration().entryTtl(Duration.ofSeconds(5)),
                CacheKey.ITEM, defaultCacheConfiguration().entryTtl(Duration.ofSeconds(3)),
                CacheKey.REGION, defaultCacheConfiguration().entryTtl(Duration.ofSeconds(100))
        );
    }
}
