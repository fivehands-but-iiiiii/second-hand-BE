package com.team5.secondhand.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team5.secondhand.chat.bubble.service.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class ChatConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Object> redisObjectTemplate() {
        objectMapper.registerModule(new JavaTimeModule());

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class)); // Use Jackson2JsonRedisSerializer
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class)); // Use Jackson2JsonRedisSerializer


        return template;
    }

    @Bean
    public RedisMessageSubscriber messageListener() {
        return new RedisMessageSubscriber(objectMapper, redisObjectTemplate(), messagingTemplate);
    }

    @Bean
    public ChannelTopic chatTopic() {
        return new ChannelTopic("chatRoom");
    }

}
