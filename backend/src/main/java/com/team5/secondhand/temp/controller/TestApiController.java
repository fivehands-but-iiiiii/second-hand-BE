package com.team5.secondhand.temp.controller;

import com.team5.secondhand.temp.dto.PostRequest;
import com.team5.secondhand.temp.dto.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestApiController {

    @Operation(
            summary = "테스트 API Home",
            tags = "Test",
            description = "테스트 API에 대한 간단한 가이드입니다."
    )
    @GetMapping("/")
    public String testGetRequest() {
        return "WELCOME!! SECONDHAND TEST : /get?input=?  /post{testText=?,testNumber=?}";
    }

    @Operation(
            summary = "테스트 GET API",
            tags = "Test",
            description = "문자열을 쿼리로 요청해주세요."
    )
    @GetMapping("/get")
    public String testGetRequest(String input) {
        return "RESULT = { " + input + " }";
    }

    @Operation(
            summary = "테스트 POST API",
            tags = "Test",
            description = "문자열과 숫자를 JSON 형식으로 요청해주세요."
    )
    @PostMapping("/post")
    public PostResponse testPostRequest(@RequestBody PostRequest postRequest) {
        return PostResponse.from(postRequest);
    }
}
