package com.team5.secondhand.api.item.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {


    @Test
    @DisplayName("Item가 판매중이면 가능한 Item Status는 ON_SALE과 RESERVATION 이다.")
    void isOnSales_param_true() {
        boolean onSale = true;

        List<Status> onSales = Status.isOnSales(onSale);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(onSales.contains(Status.ON_SALE)).isTrue();
            softAssertions.assertThat(onSales.contains(Status.RESERVATION)).isTrue();
            softAssertions.assertThat(onSales.contains(Status.SOLD)).isFalse();
            softAssertions.assertThat(onSales).hasSize(2);
        });
    }

    @Test
    @DisplayName("Item가 판매중이면 가능한 Item Status는 ON_SALE과 RESERVATION 이다.")
    void isOnSales_param_false() {
        boolean onSale = false;

        List<Status> onSales = Status.isOnSales(onSale);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(onSales.contains(Status.ON_SALE)).isFalse();
            softAssertions.assertThat(onSales.contains(Status.RESERVATION)).isFalse();
            softAssertions.assertThat(onSales.contains(Status.SOLD)).isTrue();
            softAssertions.assertThat(onSales).hasSize(1);
        });
    }
}