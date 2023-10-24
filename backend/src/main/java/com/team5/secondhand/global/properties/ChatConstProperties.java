package com.team5.secondhand.global.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties("const.chat")
public class ChatConstProperties {

    private final String bucket;
    private final int pageSize;

    @ConstructorBinding
    public ChatConstProperties(String bucket, int pageSize) {
        this.bucket = bucket;
        this.pageSize = pageSize;
    }
}
