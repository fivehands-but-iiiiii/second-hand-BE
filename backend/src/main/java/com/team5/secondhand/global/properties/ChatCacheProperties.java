package com.team5.secondhand.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "chat.bubble")
public class ChatCacheProperties {
    private String key;
    private int pageSize;
}