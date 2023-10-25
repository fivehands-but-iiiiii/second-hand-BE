package com.team5.secondhand.application.item.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Status {
    ON_SALE(true),
    RESERVATION(true),
    SOLD(false);

    private final boolean isSales;

    Status(boolean isSales) {
        this.isSales = isSales;
    }

    @JsonValue
    public Integer getValue(){
        return ordinal();
    }

    public static List<Status> isSales(Boolean value) {
        if (value!=null) {
            return Arrays.stream(values()).filter(e -> e.isSales == value).collect(Collectors.toList());
        }
        return null;
    }
}
