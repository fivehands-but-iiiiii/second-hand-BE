package com.team5.secondhand.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Getter
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Object> redisObjectTemplate() {
        objectMapper.registerModule(new JavaTimeModule());

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class)); // Use Jackson2JsonRedisSerializer

        return template;
    }
}
