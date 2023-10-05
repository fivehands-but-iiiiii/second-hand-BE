package com.team5.secondhand.chat.chatroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Getter
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class ChatroomConfig {
    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Chatroom> redisChatroomTemplate() {
        final RedisTemplate<String, Chatroom> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        objectMapper.registerModule(new JavaTimeModule());

        Jackson2JsonRedisSerializer<Chatroom> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Chatroom.class);
        jsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jsonRedisSerializer);

        return template;
    }

    @Bean
    public HashOperations<String, String, Chatroom> chatroomHashOperations() {
        return redisChatroomTemplate().opsForHash();
    }

}
