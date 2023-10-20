package com.team5.secondhand.chat.notification.controller;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.chat.notification.service.NotificationService;
import com.team5.secondhand.global.auth.Logined;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream") // Accept: text/event-stream
    public SseEmitter subscribe(@Logined MemberDetails loginMember, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId, HttpServletResponse response) {
        return notificationService.subscribe(loginMember.getId(), lastEventId, response);
    }
}
