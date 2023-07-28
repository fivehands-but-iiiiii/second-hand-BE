package com.team5.secondhand.chat.noti.service;

import com.team5.secondhand.chat.noti.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;
    private final NotificationRepository notificationRepository;
    }
}
