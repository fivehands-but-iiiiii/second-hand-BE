package com.team5.secondhand.unit;

import com.team5.secondhand.TestContainer;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@DataJpaTest
public abstract class AbstractRepositoryTest extends TestContainer {
    @Autowired
    protected ChatBubbleCache chatBubbleCache;
}
