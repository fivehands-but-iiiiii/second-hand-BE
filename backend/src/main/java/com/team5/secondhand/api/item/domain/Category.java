package com.team5.secondhand.api.item.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String imgUrl;

    public Category(String title, String imgUrl) {
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public static Category createNew(String title, String imgUrl) {
        return new Category(title, imgUrl);
    }
}
