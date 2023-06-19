package com.team5.secondhand.api.item.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    ON_SALE,
    RESERVATION,
    SOLD;

    @JsonValue
    public Integer getValue(){
        return ordinal();
    }
}
