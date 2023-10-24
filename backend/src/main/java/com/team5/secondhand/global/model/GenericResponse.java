package com.team5.secondhand.global.model;

import lombok.Getter;

@Getter
public class GenericResponse<T> {

    private final String message;
    private final T data;

    private GenericResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> GenericResponse<T> send(String message, T data) {
        return new GenericResponse<T>(message, data);
    }
}
