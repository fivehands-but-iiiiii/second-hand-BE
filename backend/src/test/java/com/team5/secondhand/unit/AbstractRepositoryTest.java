package com.team5.secondhand.unit;

import com.team5.secondhand.FixtureFactory;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRepositoryTest extends FixtureFactory {

    @Autowired
    protected ChatBubbleCache chatBubbleCache; // ChatBubble 저장 및 조회를 위한 Cache

}
