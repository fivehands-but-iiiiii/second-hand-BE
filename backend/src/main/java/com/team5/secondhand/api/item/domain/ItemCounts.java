package com.team5.secondhand.api.item.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE item_counts SET is_deleted = true WHERE id = ?")
public class ItemCounts {

    private static final long INIT_COUNT_NUMBER = 0L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    private Long hits;

    @Min(0)
    private Long likeCounts;

    @Min(0)
    private Long chatCounts;

    private Boolean isDeleted;

    @Builder
    public ItemCounts(Long id, Long hits, Long likeCounts, Long chatCounts, Boolean isDeleted) {
        this.id = id;
        this.hits = hits;
        this.likeCounts = likeCounts;
        this.chatCounts = chatCounts;
        this.isDeleted = isDeleted;
    }

    public static ItemCounts createRelated() {
        return ItemCounts.builder()
                .hits(INIT_COUNT_NUMBER)
                .likeCounts(INIT_COUNT_NUMBER)
                .chatCounts(INIT_COUNT_NUMBER)
                .isDeleted(false)
                .build();
    }
}
