package com.team5.secondhand.application.chatroom.controller;

import com.team5.secondhand.application.chatroom.dto.request.ChatItem;
import com.team5.secondhand.application.chatroom.dto.response.ChatroomDetails;
import com.team5.secondhand.application.chatroom.dto.response.ChatroomList;
import com.team5.secondhand.application.chatroom.exception.BuyerException;
import com.team5.secondhand.application.chatroom.exception.ExistChatRoomException;
import com.team5.secondhand.application.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.application.chatroom.service.ChatroomFacade;
import com.team5.secondhand.application.item.exception.ExistItemException;
import com.team5.secondhand.application.member.dto.response.MemberDetails;
import com.team5.secondhand.application.member.exception.ExistMemberIdException;
import com.team5.secondhand.global.auth.Logined;
import com.team5.secondhand.global.model.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatroomController {
    private final ChatroomFacade chatRoomFacade;

    @Operation(
            summary = "채팅방 정보 조회",
            tags = "Chatroom",
            description = "사용자는 채팅방 정보를 조회할 수 있다."
    )
    @GetMapping("/{chatroomId}")
    public GenericResponse<ChatroomDetails> getChatroomInfo(@PathVariable String chatroomId,
                                                            @Logined MemberDetails loginMember) throws ExistMemberIdException, ExistItemException, ExistChatRoomException, NotChatroomMemberException {
        ChatroomDetails chatroomInfo = chatRoomFacade.findChatroomInfo(chatroomId, loginMember.getId());
        return GenericResponse.send("채팅방 정보가 조회되었습니다.", chatroomInfo);
    }

    @Operation(
            summary = "채팅방 정보 조회",
            tags = "Chatroom",
            description = "사용자는 채팅방 정보를 조회할 수 있다."
    )
    @GetMapping("/items/{itemId}")
    public GenericResponse<ChatroomDetails> getChatroomInfo(@PathVariable long itemId,
                                                            @Logined MemberDetails loginMember) throws ExistMemberIdException, ExistItemException, NotChatroomMemberException {
        ChatroomDetails chatroomInfo = chatRoomFacade.findChatroomInfo(itemId, loginMember.getId());
        return GenericResponse.send("채팅방 정보가 조회되었습니다.", chatroomInfo);
    }

    @Operation(
            summary = "채팅방 생성",
            tags = "Chatroom",
            description = "사용자는 채팅방을 생성할 수 있다."
    )
    @PostMapping
    public GenericResponse<String> createChatRoom(@RequestBody ChatItem chatItem,
                                                  @Logined MemberDetails loginMember) throws ExistMemberIdException, ExistItemException, ExistChatRoomException, BuyerException {
        String chatRoomId = chatRoomFacade.create(chatItem.getItemId(), loginMember.getId());

        return GenericResponse.send("채팅방이 생성되었습니다.", chatRoomId);
    }

    @Operation(
            summary = "채팅방 리스트 조회",
            tags = "Chatroom",
            description = "사용자는 채팅방 리스트를 조회할 수 있다."
    )
    @GetMapping
    public GenericResponse<ChatroomList> getChatroomList(ChatItem chatItem,
                                                         @Logined MemberDetails loginMember) throws ExistMemberIdException, ExistItemException {
        ChatroomList chatroomList = chatRoomFacade.findChatroomList(chatItem, loginMember.getId());
        return GenericResponse.send("채팅방 목록을 조회되었습니다.", chatroomList);
    }

    @Operation(
            summary = "채팅방 완전히 나가기",
            tags = "Chatroom",
            description = "사용자는 채팅방을 완전히 나갈 수 있다."
    )
    @DeleteMapping("/{chatId}")
    public GenericResponse<String> exitChatroom(@PathVariable String chatId,
                                                @Logined MemberDetails loginMember) throws ExistMemberIdException, ExistChatRoomException, NotChatroomMemberException {
        chatRoomFacade.exitChatroom(chatId, loginMember.getId());

        return GenericResponse.send("채탕방을 나갔습니다.", null);
    }
}
