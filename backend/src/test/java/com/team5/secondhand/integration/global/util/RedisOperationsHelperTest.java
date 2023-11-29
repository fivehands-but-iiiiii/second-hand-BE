package com.team5.secondhand.integration.global.util;

import com.team5.secondhand.TestContainer;
import com.team5.secondhand.application.member.domain.BasedRegion;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.domain.Oauth;
import com.team5.secondhand.application.region.domain.Region;
import com.team5.secondhand.global.util.RedisOperationsHelper;
import com.team5.secondhand.integration.IntegrationTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("Redis operations test")
class RedisOperationsHelperTest extends TestContainer {

    @Autowired
    RedisOperationsHelper redisOperationsHelper;
    Region region;
    Member iirin;
    Member dewey;

    @BeforeEach
    void init () {
        iirin = Member.builder().id(1L)
                .memberId("이린")
                .regions(List.of(BasedRegion.builder().id(10L)
                        .region(region)
                        .represented(true)
                        .build()))
                .oauth(Oauth.NONE)
                .profileImgUrl("image.png")
                .build();
        dewey = Member.builder().id(2L)
                .memberId("듀이")
                .regions(List.of(BasedRegion.builder().id(10L)
                        .region(region)
                        .represented(true)
                        .build()))
                .oauth(Oauth.NONE)
                .profileImgUrl("image2.png")
                .build();
    }

    @AfterEach
    void setup () {
        redisOperationsHelper.deleteAll();
    }

    @Nested
    @DisplayName("put 테스트 - ")
    class put {

        @Test
        @DisplayName("성공")
        void put_success () {

            String key = "member::1";

            redisOperationsHelper.put(key, iirin, null);
            Member savedMember = redisOperationsHelper.get(key, Member.class).get();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(savedMember.getMemberId()).isEqualTo(iirin.getMemberId());
                softAssertions.assertThat(savedMember.getProfileImgUrl()).isEqualTo(iirin.getProfileImgUrl());
            });
        }

            @Test
            @DisplayName("만료시간 지정 성공")
            void put_with_ttl_success() throws InterruptedException {
            String key = "member::1";

            redisOperationsHelper.put(key, iirin, 5l);
            Member savedMember = redisOperationsHelper.get(key, Member.class).get();

            Thread.sleep(5000);

            Optional<Member> afterMember = redisOperationsHelper.get(key, Member.class);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(savedMember.getMemberId()).isEqualTo(iirin.getMemberId());
                softAssertions.assertThat(savedMember.getProfileImgUrl()).isEqualTo(iirin.getProfileImgUrl());
                softAssertions.assertThat(afterMember).isEmpty();
            });
        }
    }

    @Test
    void putToList() {
        String key = "member::2";

        redisOperationsHelper.putToList(key, iirin);
        List<Member> list = redisOperationsHelper.getList(key, 0, 1, Member.class);
        Member savedMember = list.get(0);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(savedMember.getMemberId()).isEqualTo(iirin.getMemberId());
            softAssertions.assertThat(savedMember.getProfileImgUrl()).isEqualTo(iirin.getProfileImgUrl());
        });
    }

    @Test
    void delete() {
        String key = "delete::";
        redisOperationsHelper.put(key, iirin, null);
        redisOperationsHelper.delete(key);

        Optional<Member> savedMember = redisOperationsHelper.get(key, Member.class);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(savedMember.isEmpty()).isTrue();
            softAssertions.assertThatThrownBy(() -> savedMember.orElseThrow())
                    .isInstanceOf(NoSuchElementException.class);
        });
    }

    @Nested
    @DisplayName("만료시간 테스트 - ")
    class setExpireTime {

        @Test
        @DisplayName("만료 전 테스트")
        void setExpireTime_not_expired() {
            String key = "member::3";
            long expire = 10;

            redisOperationsHelper.put(key, dewey, null);
            redisOperationsHelper.setExpireTime(key, expire);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(redisOperationsHelper.getExpireTime(key)).isGreaterThan(0);
                softAssertions.assertThat(redisOperationsHelper.get(key, Member.class)).isNotEmpty();
            });
        }

        @Test
        @DisplayName("만료 후 테스트")
        void setExpireTime() throws InterruptedException {
            String key = "member::3";
            long expire = 1;

            redisOperationsHelper.put(key, dewey, null);
            redisOperationsHelper.setExpireTime(key, expire);

            Thread.sleep((expire + 1) * 1000);
            SoftAssertions.assertSoftly(softAssertions -> {
                assertThat(redisOperationsHelper.getExpireTime(key)).isLessThan(0);
                assertThat(redisOperationsHelper.get(key, Member.class)).isEmpty();
            });
        }
    }

    @Test
    void getAll() {
        String key = "test::";
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, iirin);
        redisOperationsHelper.putToList(key, iirin);
        redisOperationsHelper.putToList(key, iirin);
        redisOperationsHelper.putToList(key, iirin);
        redisOperationsHelper.putToList(key, iirin);
        redisOperationsHelper.putToList(key, iirin);

        List<Member> members = redisOperationsHelper.getAll(key, Member.class);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(members).hasSize(12);
            softAssertions.assertThat(members.get(7).getMemberId()).isEqualTo(iirin.getMemberId());
        });

    }

    @Test
    void getList() {
        String key = "test::";
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, iirin);
        redisOperationsHelper.putToList(key, iirin);

        List<Member> members = redisOperationsHelper.getList(key, 0, 10, Member.class);
        assertThat(members).hasSize(4);
    }

    @Test
    void size() {
        String key = "test::";
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, dewey);
        redisOperationsHelper.putToList(key, iirin);
        redisOperationsHelper.putToList(key, iirin);
        redisOperationsHelper.putToList(key, iirin);

        List<Member> members = redisOperationsHelper.getList(key, 0, 3, Member.class);

        assertThat(members).hasSize(3);
    }
}
