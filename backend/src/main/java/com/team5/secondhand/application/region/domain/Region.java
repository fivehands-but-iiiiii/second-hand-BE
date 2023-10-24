package com.team5.secondhand.application.region.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String county;
    private String district;

    @Builder
    protected Region(Long id, String city, String county, String district) {
        this.id = id;
        this.city = city;
        this.county = county;
        this.district = district;
    }
}
