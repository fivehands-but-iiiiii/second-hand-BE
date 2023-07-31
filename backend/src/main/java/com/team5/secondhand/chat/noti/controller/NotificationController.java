package com.team5.secondhand.chat.noti.controller;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.chat.noti.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;

@Slf4j
@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream") // Accept: text/event-stream
    public SseEmitter subscribe(@RequestAttribute MemberDetails loginMember, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId, HttpServletResponse response) {
        return notificationService.subscribe(loginMember.getMemberId(), lastEventId, response);
    }
}
