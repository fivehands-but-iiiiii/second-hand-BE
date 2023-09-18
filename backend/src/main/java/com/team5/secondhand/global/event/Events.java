package com.team5.secondhand.global.event;

import com.team5.secondhand.global.model.BaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Events {
    private static ApplicationEventPublisher publisher;

    static void setPublisher(ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    public static void raise(BaseEvent event) {
        if (publisher!=null) {
            publisher.publishEvent(event);
        }
    }
}
