package com.team5.secondhand.application.member.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum Oauth {
    GITHUB, NONE;

    @JsonValue
    public String getValue(){
        return name().toLowerCase(Locale.ROOT);
    }
}
