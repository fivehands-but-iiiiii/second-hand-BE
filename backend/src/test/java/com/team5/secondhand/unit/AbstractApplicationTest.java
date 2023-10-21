package com.team5.secondhand.unit;

import com.team5.secondhand.FixtureFactory;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleCache;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import com.team5.secondhand.chat.bubble.service.RedisChatPublisher;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

public abstract class AbstractApplicationTest extends FixtureFactory {

    @Mock
    protected ChatBubbleCache chatBubbleCache; // ChatBubble 저장 및 조회를 위한 Cache
    @Mock
    protected ChatBubbleRepository chatBubbleRepository; // ChatBubble 저장 및 조회를 위한 Repository
    @Mock
    protected RedisChatPublisher redisChatPublisher; // Redis 메시지 발행을 위한 클래스
    @Mock
    protected ApplicationEventPublisher publisher; // event 발행을 위한 클래스
}
