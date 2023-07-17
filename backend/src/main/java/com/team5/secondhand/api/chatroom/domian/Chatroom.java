package com.team5.secondhand.api.chatroom.domian;

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
    private Chatroom(Long id, String chatroomId, Item item, Member buyer, Instant createdAt) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.item = item;
        this.buyer = buyer;
        this.createdAt = createdAt;
    }

    public static Chatroom create(Item item, Member buyer) {
        return com.team5.secondhand.api.chatroom.domian.Chatroom.builder()
                .item(item)
                .buyer(buyer)
                .build();
    }

    public List<String> getChatroomMemberIds() {
        Member seller = item.getSeller();
        return List.of(buyer.getMemberId(), seller.getMemberId());
    }
}
