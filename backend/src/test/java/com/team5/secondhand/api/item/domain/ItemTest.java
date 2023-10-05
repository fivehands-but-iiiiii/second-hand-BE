package com.team5.secondhand.api.item.domain;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.javax.validation.plugin.JavaxValidationPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    Item item;

    public static FixtureMonkey fixtureMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .plugin(new JavaxValidationPlugin()) // or new JavaxValidationPlugin()
                .defaultNotNull(true)
                .build();
    }

    @BeforeEach
    void init() {
        item = fixtureMonkey().giveMeBuilder(Item.class)
                .set("title", "Origin Title")
                .set("contents", "Origin contents")
                .sample();
    }

    @Test
    @DisplayName("")
    void update() {
    }

    @Test
    void getFirstImageUrl() {
    }

    @Test
    void updateThumbnail() {
    }

    @Test
    void assignOwnership() {
    }

    @Test
    void isSeller() {
    }

    @Test
    void testIsSeller() {
    }
}
