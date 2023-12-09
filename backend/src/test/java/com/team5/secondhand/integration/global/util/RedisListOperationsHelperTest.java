package com.team5.secondhand.integration.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team5.secondhand.TestContainer;
import com.team5.secondhand.application.member.domain.BasedRegion;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.domain.Oauth;
import com.team5.secondhand.application.region.domain.Region;
import com.team5.secondhand.global.util.RedisListOperationsHelper;
import com.team5.secondhand.integration.IntegrationTest;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@DisplayName("Redis operations test")
class RedisListOperationsHelperTest extends TestContainer {

    @Autowired
    RedisListOperationsHelper redisOperationsHelper;
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

    @Test
    void putToList() {
        String key = "member::2";

        redisOperationsHelper.add(key, iirin);
        List<Member> list = redisOperationsHelper.getList(key, 0, 1, Member.class);
        Member savedMember = list.get(0);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(savedMember.getMemberId()).isEqualTo(iirin.getMemberId());
            softAssertions.assertThat(savedMember.getProfileImgUrl()).isEqualTo(iirin.getProfileImgUrl());
        });
    }

    @Nested
    @DisplayName("모든 데이터를 가져올 때 ")
    class getAll {

        @Test
        @DisplayName("1개 채팅방의 채팅 내역을 가져올 수 있다.")
        void getAll() throws JsonProcessingException {
            String key = "test::";
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, iirin);
            redisOperationsHelper.add(key, iirin);
            redisOperationsHelper.add(key, iirin);
            redisOperationsHelper.add(key, iirin);
            redisOperationsHelper.add(key, iirin);
            redisOperationsHelper.add(key, iirin);

            List<Member> members = redisOperationsHelper.popAll(key, Member.class);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(members).hasSize(12);
                softAssertions.assertThat(members.get(7).getMemberId()).isEqualTo(dewey.getMemberId());
            });
        }

        @Test
        @DisplayName("요소가 없으면 빈 리스트를 반환한다.")
        void getAll_empty() throws JsonProcessingException {
            String key = "test::";
            List<Member> members = redisOperationsHelper.popAll(key, Member.class);
            assertThat(members).isEmpty();
        }

        @Test
        @DisplayName("prefix를 통해 모든 리스트에서 데이터를 가져올 수 있다.")
        void getAll_multiple() throws JsonProcessingException {
            //given
            String key = "test::chatroom1";
            String key2 = "test::chatroom2";
            String prefix = "test::";
            // 채팅방 1 데이터 삽입
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);
            redisOperationsHelper.add(key, dewey);

            // 채팅방 2 데이터 삽입
            redisOperationsHelper.add(key2, iirin);
            redisOperationsHelper.add(key2, iirin);
            redisOperationsHelper.add(key2, iirin);
            redisOperationsHelper.add(key2, iirin);
            redisOperationsHelper.add(key2, iirin);
            redisOperationsHelper.add(key2, iirin);

            // getAll
            List<Member> members = redisOperationsHelper.popAll(prefix, Member.class);

            //then
    SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(members).hasSize(11);
                softAssertions.assertThat(members.get(7).getMemberId()).isEqualTo(dewey.getMemberId());
                softAssertions.assertThat(redisOperationsHelper.popAll(key, Member.class)).hasSize(0);
            });
        }

    }


    @Test
    void getList() {
        String key = "test::";
        redisOperationsHelper.add(key, dewey);
        redisOperationsHelper.add(key, dewey);
        redisOperationsHelper.add(key, iirin);
        redisOperationsHelper.add(key, iirin);

        List<Member> members = redisOperationsHelper.getList(key, 0, 10, Member.class);
        assertThat(members).hasSize(4);
    }

    @Test
    void size() {
        String key = "test::";
        redisOperationsHelper.add(key, dewey);
        redisOperationsHelper.add(key, dewey);
        redisOperationsHelper.add(key, iirin);
        redisOperationsHelper.add(key, iirin);
        redisOperationsHelper.add(key, iirin);

        List<Member> members = redisOperationsHelper.getList(key, 0, 3, Member.class);

        assertThat(members).hasSize(3);
    }
}
