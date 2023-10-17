package com.team5.secondhand.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.chat.topic.service.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
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
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, ChatBubble> redisChatBubbleTemplate() {
        final RedisTemplate<String, ChatBubble> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        objectMapper.registerModule(new JavaTimeModule());

        Jackson2JsonRedisSerializer<ChatBubble> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(ChatBubble.class);
        jsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jsonRedisSerializer);

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
