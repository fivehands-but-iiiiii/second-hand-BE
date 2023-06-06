package com.team5.backend.temp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String testTextResult;
    private String testNumberResult;

    public static PostResponse from(PostRequest postRequest) {
        return new PostResponse("result : "+postRequest.getTestText(), postRequest.getTestNumber());
    }
}
