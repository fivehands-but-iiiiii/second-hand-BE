package com.team5.secondhand.unit.api;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.javax.validation.plugin.JavaxValidationPlugin;
import com.team5.secondhand.TestContainer;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
public abstract class AbstractApplicationTest extends TestContainer {

    @Autowired
    protected ChatBubbleCache chatBubbleCache;

    public static FixtureMonkey fixtureMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .defaultNotNull(true)
                .build();
    }

}
