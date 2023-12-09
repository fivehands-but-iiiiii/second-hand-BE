package com.team5.secondhand.integration.notification.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.team5.secondhand.FixtureFactory;
import com.team5.secondhand.chat.notification.domain.SseKey;
import com.team5.secondhand.chat.notification.repository.NotificationRepository;
import com.team5.secondhand.chat.notification.service.NotificationService;
import com.team5.secondhand.global.properties.ChatNotificationProperties;
import com.team5.secondhand.integration.IntegrationTest;
import java.time.Duration;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService 통합 테스트")
class NotificationServiceTest extends FixtureFactory {

    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    ChatNotificationProperties chatNotificationProperties;
    @Spy
    HttpServletResponse httpServletResponse;

    String sseKey = "1_1239213";
    long id = 1L;

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAllStartByWithId(id);
    }

    @Nested
    @DisplayName("subscribe 를 시행할 때, ")
    class subscribe {
        @Test
        @DisplayName("SseEmitter를 저장할 수 있다.")
        void subscribe() {
            // when
            notificationService.subscribe(1L, null, httpServletResponse);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(
                                notificationRepository.findAllStartById(id).size())
                        .isEqualTo(1);
            });
        }

        @Test
        @DisplayName("SseEmitter가 Complete 되면 삭제한다.")
        void subscribeAndComplete() {
            // given
            Map<SseKey, SseEmitter> before = notificationRepository.findAllStartById(id);
            SseEmitter sseEmitter = notificationService.subscribe(1L, null, httpServletResponse);

            // when
            sseEmitter.complete();

        }

        @Test
        @DisplayName("SseEmitter가 Error 되면 삭제한다.")
        void subscribeAndError() {
            // given
            // when
            SseEmitter sseEmitter = notificationService.subscribe(1L, null, httpServletResponse);
            sseEmitter.completeWithError(new RuntimeException());

            await().atLeast(Duration.ofMillis(1000));

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(
                                notificationRepository.findAllStartById(id).size())
                        .isEqualTo(0);
            });
        }

        @Test
        @DisplayName("SseEmitter가 Timeout 되면 삭제한다.")
        void subscribeAndTimeout() {
            // given
            // when
            SseEmitter sseEmitter = notificationService.subscribe(1L, "", httpServletResponse);

            await().atMost(Duration.ofMillis(chatNotificationProperties.getTimeOut() + 1000))
                    .untilAsserted(() -> {
                        // then
                        SoftAssertions.assertSoftly(softAssertions -> {
                            softAssertions.assertThat(sseEmitter.getTimeout()).isLessThan(0L);
                            softAssertions.assertThat(
                                            notificationRepository.findAllStartById(id).size())
                                    .isEqualTo(0);
                        });
                    });
        }
    }

}
