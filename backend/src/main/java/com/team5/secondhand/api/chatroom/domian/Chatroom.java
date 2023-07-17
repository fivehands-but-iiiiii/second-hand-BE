package com.team5.secondhand.api.chatroom.domian;

import com.team5.secondhand.api.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "chatroom")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom {
    @Id
    private Long id;
    private String chatroomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @CreatedDate
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private ChatroomStatus chatroomStatus;

    @Builder
    public Chatroom(Long id, String chatroomId, Item item, Member buyer, Instant createdAt, ChatroomStatus chatroomStatus) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.item = item;
        this.buyer = buyer;
        this.createdAt = createdAt;
        this.chatroomStatus = chatroomStatus;
    }

    public static Chatroom create(Item item, Member buyer) {
        return com.team5.secondhand.api.chatroom.domian.Chatroom.builder()
                .item(item)
                .buyer(buyer)
                .chatroomStatus(ChatroomStatus.FULL)
                .build();
    }

    public List<String> getChatroomMemberIds() {
        Map<Long, Member> chatroomMembers = getChatroomMembers();
        return chatroomMembers.values().stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());
    }

    private Map<Long, Member> getChatroomMembers() {
        Member seller = this.item.getSeller();
        return Map.of(buyer.getId(), buyer, seller.getId(), seller);
    }

    public void exitMember(Member member) throws NotChatroomMemberException {
        boolean isSeller = item.isSeller(member.getId());
        boolean isBuyer = buyer.equals(member);

        if (!isBuyer && !isSeller) {
            throw new NotChatroomMemberException("채팅방에 있는 멤버가 아닙니다.");
        }

        if (this.chatroomStatus == ChatroomStatus.FULL) {
            if (isSeller) {
                this.chatroomStatus = ChatroomStatus.BUYER_ONLY;
                return;
            }
            this.chatroomStatus = ChatroomStatus.SELLER_ONLY;
        } else if (this.chatroomStatus == ChatroomStatus.SELLER_ONLY && isBuyer) {
            return;
        } else if (this.chatroomStatus == ChatroomStatus.BUYER_ONLY && isSeller) {
            return;
        }
        this.chatroomStatus = ChatroomStatus.EMPTY;
    }

    public boolean isChatroomMember(Member member) throws NotChatroomMemberException {
        boolean isSeller = item.isSeller(member.getId());
        boolean isBuyer = buyer.equals(member);

        if (!isBuyer && !isSeller) {
            throw new NotChatroomMemberException("채팅방에 있는 멤버가 아닙니다.");
        }

        switch (this.chatroomStatus) {
            case EMPTY:
                return false;
            case BUYER_ONLY:
                return isBuyer;
            case SELLER_ONLY:
                return isSeller;
            case FULL:
                return true;
            default:
                throw new IllegalStateException("알 수 없는 채팅방 상태입니다.");
        }
    }


    public Member findOpponent(Member myself) {
        Map<Long, Member> chatroomMembers = getChatroomMembers();
        return chatroomMembers.values().stream()
                .filter(e -> !e.equals(myself))
                .findAny().orElseThrow();
    }
}
