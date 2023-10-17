package com.team5.secondhand.application.api.repository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.team5.secondhand.TestContainer;
import com.team5.secondhand.application.api.ApplicationTest;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@ApplicationTest
class ChatBubbleRequestCacheTest extends TestContainer {

    @Autowired
    ChatBubbleCache cache;

    ChatBubble chatBubble;
    String key;

    public static FixtureMonkey fixtureMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
//                .plugin(new JavaxValidationPlugin()) // or new JavaxValidationPlugin()
                .defaultNotNull(true)
                .build();
    }

    @BeforeEach
    void init() {
        chatBubble = fixtureMonkey().giveMeBuilder(ChatBubble.class).sample();
        key = "test:chatBubble1";
    }


    @Test
    void save() {
        Slice<ChatBubble> all = cache.findAllByRoomId(key, PageRequest.of(0, 100));

        ChatBubble saved = cache.save(key, chatBubble);

        Slice<ChatBubble> newAll = cache.findAllByRoomId(key, PageRequest.of(0, 100));
        Assertions.assertThat(newAll.getContent()).hasSize(all.getContent().size()+1);
    }

    @Test
    void findAllByRoomId() {
    }
}
