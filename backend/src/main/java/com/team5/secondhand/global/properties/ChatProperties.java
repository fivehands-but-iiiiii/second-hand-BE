package com.team5.secondhand.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "const.chat")
public class ChatProperties {
    private String bucket;
    private int pageSize;
}
