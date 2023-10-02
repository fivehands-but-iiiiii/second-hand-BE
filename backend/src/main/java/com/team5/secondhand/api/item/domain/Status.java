package com.team5.secondhand.api.item.domain;


import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Status {
    ON_SALE(true),
    RESERVATION(true),
    SOLD(false);

    private final boolean isOnSales;

    Status(boolean isOnSales) {
        this.isOnSales = isOnSales;
    }

    public static List<Status> isOnSales(Boolean value) {
        if (value!=null) {
            return Arrays.stream(values())
                    .filter(e -> e.isOnSales == value)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
