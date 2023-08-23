package com.team5.secondhand.chat.bubble.controller;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.dto.response.ChatroomLog;
import com.team5.secondhand.chat.bubble.service.ChatBubbleService;
import com.team5.secondhand.global.dto.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chats")
public class ChatLogController {

    private final ChatBubbleService chatLogService;

    @Operation(
            summary = "채팅방 로그 조회",
            tags = "Chatroom",
            description = "사용자는 채팅방 메시지 내용을  조회할 수 있다."
    )
    @GetMapping("/{roomId}/logs")
    public GenericResponse<ChatroomLog> getChatroomLogs(int page, @PathVariable String roomId, @RequestAttribute MemberDetails loginMember) {
        Slice<ChatBubble> chatBubbles = chatLogService.getChatBubbles(page, roomId);
        ChatroomLog chatroomLog = ChatroomLog.from(chatBubbles, loginMember);
        return GenericResponse.send("채팅방 로그가 조회되었습니다.", chatroomLog);
    }
}
