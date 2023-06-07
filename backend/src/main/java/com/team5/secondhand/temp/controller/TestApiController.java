package com.team5.secondhand.temp.controller;

import com.team5.secondhand.temp.dto.PostRequest;
import com.team5.secondhand.temp.dto.PostResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/test")
@RestController
public class TestApiController {

    @GetMapping("")
    public String testGetRequest() {
        return "/get?input=?  /post{testText=?,testNumber=?}";
    }

    @GetMapping("/get")
    public String testGetRequest(String input) {
        return "API response = " + input;
    }

    @PostMapping("/post")
    public PostResponse testPostRequest(PostRequest postRequest) {
        return PostResponse.from(postRequest);
    }
}
