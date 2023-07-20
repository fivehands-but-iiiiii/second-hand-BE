package com.team5.secondhand.api.chatroom.controller;

import com.team5.secondhand.api.chatroom.dto.response.ChatItem;
import com.team5.secondhand.api.chatroom.dto.response.ChatroomDetails;
import com.team5.secondhand.api.chatroom.exception.ExistChatRoomException;
import com.team5.secondhand.api.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.api.chatroom.service.ChatroomFacade;
import com.team5.secondhand.api.chatroom.service.ChatroomService;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.exception.ExistItemException;
import com.team5.secondhand.api.item.service.ItemService;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.service.MemberService;
import com.team5.secondhand.global.dto.GenericResponse;
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
    private final ChatroomService chatRoomService;
    private final MemberService memberService;
    private final ItemService itemService;

    @Operation(
            summary = "채팅방 정보 조회",
            tags = "Chatroom",
            description = "사용자는 채팅방 정보를 조회할 수 있다."
    )
    @GetMapping("/{chatroomId}")
    public GenericResponse<ChatroomDetails> getChatroomInfo(@PathVariable String chatroomId, @RequestAttribute MemberDetails loginMember) throws ExistMemberIdException, ExistItemException, ExistChatRoomException, NotChatroomMemberException {
        ChatroomDetails chatroomInfo = chatRoomFacade.findChatroomInfo(chatroomId, loginMember.getId());
        return GenericResponse.send("채팅방 정보가 조회되었습니다.", chatroomInfo);
    }

    @Operation(
            summary = "채팅방 정보 조회",
            tags = "Chatroom",
            description = "사용자는 채팅방 정보를 조회할 수 있다."
    )
    @GetMapping("/items/{itemId}")
    public GenericResponse<ChatroomDetails> getChatroomInfo(@PathVariable long itemId, @RequestAttribute MemberDetails loginMember) throws ExistMemberIdException, ExistItemException, NotChatroomMemberException {
        ChatroomDetails chatroomInfo = chatRoomFacade.findChatroomInfo(itemId, loginMember.getId());
        return GenericResponse.send("채팅방 정보가 조회되었습니다.", chatroomInfo);
    }

    @Operation(
            summary = "채팅방 생성",
            tags = "Chatroom",
            description = "사용자는 채팅방을 생성할 수 있다."
    )
    @PostMapping
    public GenericResponse<String> createChatRoom(@ModelAttribute ChatItem chatItem, @RequestAttribute MemberDetails loginMember) throws ExistMemberIdException, ExistItemException, ExistChatRoomException {
        Member buyer = memberService.findById(loginMember.getId());
        Item item = itemService.findById(chatItem.getItemId());
        String chatRoomId = chatRoomService.create(item, buyer);
    
        return GenericResponse.send("채팅방이 생성되었습니다.", chatRoomId);
    }
}
