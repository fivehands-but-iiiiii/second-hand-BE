package com.team5.secondhand.api.chatroom.domian;

import com.team5.secondhand.api.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.global.model.BasedTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BasedTimeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chatroomId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    private Long sellerId;

    @Enumerated(EnumType.STRING)
    private ChatroomStatus chatroomStatus;

    @Builder
    protected Chatroom(Long id, String chatroomId, Item item, Member buyer, Long sellerId, ChatroomStatus chatroomStatus) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.item = item;
        this.buyer = buyer;
        this.sellerId = sellerId;
        this.chatroomStatus = chatroomStatus;
    }

    public static Chatroom create(Item item, Member buyer) {
        return com.team5.secondhand.api.chatroom.domian.Chatroom.builder()
                .chatroomId(UUID.randomUUID().toString())
                .item(item)
                .buyer(buyer)
                .sellerId(item.getSeller().getId())
                .chatroomStatus(ChatroomStatus.FULL)
                .build();
    }

    public List<Long> getChatroomMemberIds() {
        Map<Long, Member> chatroomMembers = getChatroomMembers();
        return new ArrayList<>(chatroomMembers.keySet());
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
            return;
        } else if (this.chatroomStatus == ChatroomStatus.SELLER_ONLY && isBuyer) {
            return;
        } else if (this.chatroomStatus == ChatroomStatus.BUYER_ONLY && isSeller) {
            return;
        }
        this.chatroomStatus = ChatroomStatus.EMPTY;
    }

    public boolean isChatroomMember(Member member) throws NotChatroomMemberException {
        boolean isSeller = item.isSeller(member.getId());
        //TODO: 현재 member 객체가 프록시 객체라 객체 비교가 불가능하다.. 어떤 방식을 해야 좋을지 모르겠다. 임시로 pk 비교
        boolean isBuyer = buyer.getId().equals(member.getId());

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
                .filter(e -> !e.getId().equals(myself.getId()))
                .findAny().orElseThrow();
    }
}
