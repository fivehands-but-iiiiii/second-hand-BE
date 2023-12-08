package com.team5.secondhand.integration.notification.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.team5.secondhand.FixtureFactory;
import com.team5.secondhand.chat.notification.domain.SseKey;
import com.team5.secondhand.chat.notification.repository.NotificationRepository;
import com.team5.secondhand.integration.IntegrationTest;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@IntegrationTest
@DisplayName("NotificationRepository 통합 테스트")
class NotificationRepositoryTest extends FixtureFactory {

    @Autowired
    NotificationRepository notificationRepository;

    SseKey sseKey;
    SseEmitter sseEmitter;

    @BeforeEach
    void setUp() {
        sseKey = SseKey.of(1L);
        sseEmitter = new SseEmitter();
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAllStartByWithId(1L);
    }

    @Nested
    @DisplayName("save 를 시행할 때, ")
    class save {

        @Test
        @DisplayName("SseEmitter를 저장할 수 있다.")
        void save() {
            // given
            Map<SseKey, SseEmitter> allStartById = notificationRepository.findAllStartById(
                    sseKey.getMemberId());
            int before = allStartById.size();
            // when
            SseEmitter savedSseEmitter = notificationRepository.save(sseKey, sseEmitter);
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(savedSseEmitter).isEqualTo(sseEmitter);
                softAssertions.assertThat(
                                notificationRepository.findAllStartById(sseKey.getMemberId()).size())
                        .isEqualTo(before + 1);
            });
        }
    }

    @Nested
    @DisplayName("deleteAllStartByWithId 를 시행할 때, ")
    class deleteAllStartByWithId {

        @Test
        @DisplayName("SseEmitter를 삭제할 수 있다.")
        void deleteAllStartByWithId() {
            // given
            notificationRepository.save(sseKey, sseEmitter);
            // when
            notificationRepository.deleteAllStartByWithId(sseKey.getMemberId());
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(
                                notificationRepository.findAllStartById(sseKey.getMemberId()).size())
                        .isEqualTo(0);
            });
        }

        @Test
        @DisplayName("특정 key로 시작하는 SseEmitter를 삭제할 수 있다.")
        void deleteAllStartByWithId2() {
            // given
            notificationRepository.save(sseKey, sseEmitter);
            notificationRepository.save(SseKey.of(2L), sseEmitter);
            // when
            notificationRepository.deleteAllStartByWithId(sseKey.getMemberId());
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(
                                notificationRepository.findAllStartById(sseKey.getMemberId()).size())
                        .isEqualTo(0);
            });
        }
    }

    @Nested
    @DisplayName("findAllStartById 를 시행할 때, ")
    class findAllStartById {
        @Test
        @DisplayName("특정 key로 시작하는 SseEmitter를 찾을 수 있다.")
        void findAllStartById() {
            // given
            notificationRepository.save(sseKey, sseEmitter);
            notificationRepository.save(SseKey.of(2L), sseEmitter);
            // when
            Map<SseKey, SseEmitter> allStartById = notificationRepository.findAllStartById(
                    sseKey.getMemberId());
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(allStartById.size()).isEqualTo(1);
                softAssertions.assertThat(allStartById.get(sseKey)).isEqualTo(sseEmitter);
            });
        }
    }

    @Nested
    @DisplayName("findStartById 를 시행할 때, ")
    class findStartById {
        @Test
        @DisplayName("특정 key로 시작하는 SseEmitter를 찾을 수 있다.")
        void findStartById() {
            // given
            notificationRepository.save(sseKey, sseEmitter);
            notificationRepository.save(SseKey.of(2L), sseEmitter);
            // when
            SseEmitter startById = notificationRepository.findStartById(sseKey.getMemberId()).get();
            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(startById).isEqualTo(sseEmitter);
            });
        }
    }
}
