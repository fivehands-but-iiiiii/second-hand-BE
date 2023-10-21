package com.team5.secondhand.api.item.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE item_counts SET is_deleted = true WHERE id = ?")
public class ItemCounts {

    private static final long INITIAL_COUNT = 0L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long hits;
    private Long likeCounts;
    private Long chatCounts;
    private Boolean isDeleted;

    @Builder
    protected ItemCounts(Long id, Long hits, Long likeCounts, Long chatCounts, Boolean isDeleted) {
        this.id = id;
        this.hits = hits;
        this.likeCounts = likeCounts;
        this.chatCounts = chatCounts;
        this.isDeleted = isDeleted;
    }

    public static ItemCounts initCounts() {
        return ItemCounts.builder()
                .hits(INITIAL_COUNT)
                .likeCounts(INITIAL_COUNT)
                .chatCounts(INITIAL_COUNT)
                .isDeleted(false)
                .build();
    }
}
