package com.team5.secondhand.integration.chatroom.domian;

import com.team5.secondhand.application.chatroom.domian.Chatroom;
import com.team5.secondhand.application.chatroom.domian.ChatroomStatus;
import com.team5.secondhand.application.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.ItemContents;
import com.team5.secondhand.application.item.domain.ItemCounts;
import com.team5.secondhand.application.item.domain.Status;
import com.team5.secondhand.application.member.domain.BasedRegion;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.domain.Oauth;
import com.team5.secondhand.application.region.domain.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ChatRoomTest {

    @Mock
    private Region region;
    private Member iirin;
    private Member dewey;
    private Item keyboard;
    private Chatroom chatRoom;

    @BeforeEach
    void init() {
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

        keyboard = Item.builder().id(10L)
                .title("키보드 팝니다.")
                .category(3L)
                .price(100000)
                .seller(dewey)
                .thumbnailUrl("keyboard.png")
                .region(region)
                .contents(ItemContents.builder().build())
                .count(ItemCounts.builder().build())
                .status(Status.ON_SALE)
                .isDeleted(false)
                .build();

        chatRoom = Chatroom.builder().id(1L)
                .chatroomId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
                .buyer(iirin)
                .item(keyboard)
                .chatroomStatus(ChatroomStatus.FULL)
                .build();
    }
    
    @Test
    @DisplayName("채팅방에 참여한 사람들의 ID를 알 수 있다.")
    public void getMemberIds() throws Exception{
        List<Long> chatroomMemberIds = chatRoom.getChatroomMemberIds();
        assertSoftly(assertions -> {
            assertions.assertThat(chatroomMemberIds).hasSize(2);
            assertions.assertThat(chatroomMemberIds.contains(iirin.getId())).isTrue();
            assertions.assertThat(chatroomMemberIds.contains(dewey.getId())).isTrue();
        });
    }
    
    @Test
    @DisplayName("채팅방에 있는 상대방을 알 수 있다.")
    public void testFindOpponent() throws Exception{
        Member opponent = chatRoom.findOpponent(iirin);
        assertThat(opponent.getMemberId()).isEqualTo("듀이");
    }
    
    @Test
    public void testIsChatroomMember() throws Exception{
        chatRoom.exitMember(dewey);
    
        assertSoftly(softAssertions -> {
            try {
                softAssertions.assertThat(chatRoom.isChatroomMember(dewey)).isFalse();
                softAssertions.assertThat(chatRoom.isChatroomMember(iirin)).isTrue();
                softAssertions.assertThat(chatRoom.getChatroomStatus()).isEqualTo(ChatroomStatus.BUYER_ONLY);
            } catch (NotChatroomMemberException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
