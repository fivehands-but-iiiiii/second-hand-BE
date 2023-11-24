package com.team5.secondhand.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.secondhand.chat.topic.service.RedisMessageSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Getter
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class ChatConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisObjectTemplate;


    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListener(), chatTopic());
        return container;
    }

    @Bean
    public RedisMessageSubscriber messageListener() {
        return new RedisMessageSubscriber(objectMapper, redisObjectTemplate, messagingTemplate);
    }

    @Bean("chatTopic")
    public ChannelTopic chatTopic() {
        return new ChannelTopic("chatRoom");
    }

    @Bean("notificationTopic")
    public ChannelTopic notificationTopic() {
        return new ChannelTopic("notification");
    }
}
