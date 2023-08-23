package com.team5.secondhand.global.resource.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String imgUrl;

    public Category(String title, String imgUrl) {
        this.title = title;
        this.imgUrl = imgUrl;
    }
}
