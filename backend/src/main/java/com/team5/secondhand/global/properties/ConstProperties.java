package com.team5.secondhand.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties("const")
public class ConstProperties {

    private final Chat chat;

    @ConstructorBinding
    public ConstProperties(Chat chat) {
        this.chat = chat;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Chat {

        private final String bucket;
        private final int pageSize;
    }

}
