package com.team5.secondhand.application.chatroom.controller;

import com.team5.secondhand.application.chatroom.domian.Chatroom;
import com.team5.secondhand.application.chatroom.dto.request.ChatItem;
import com.team5.secondhand.application.chatroom.dto.response.ChatroomDetails;
import com.team5.secondhand.application.chatroom.dto.response.ChatroomList;
import com.team5.secondhand.application.chatroom.exception.BuyerException;
import com.team5.secondhand.application.chatroom.exception.ExistChatRoomException;
import com.team5.secondhand.application.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.application.chatroom.service.ChatroomService;
import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.exception.ExistItemException;
import com.team5.secondhand.application.item.service.ItemReadService;
import com.team5.secondhand.application.member.domain.Member;
import com.team5.secondhand.application.member.exception.ExistMemberIdException;
import com.team5.secondhand.application.member.service.MemberService;
import com.team5.secondhand.chat.chatroom.service.ChatroomCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatroomFacade {
    private final ChatroomService chatRoomService;
    private final ItemReadService itemService;
    private final MemberService memberService;
    private final ChatroomCacheService chatroomCacheService;

    private final int FILTER_SIZE = 10;

    public ChatroomDetails findChatroomInfo(long itemId, long memberId) throws ExistMemberIdException, ExistItemException, NotChatroomMemberException {
        Item item = itemService.findById(itemId);
        Member member = memberService.findById(memberId);
        Optional<Chatroom> chatroom = chatRoomService.findChatroom(memberId, itemId);

        if (chatroom.isEmpty()) {
            return ChatroomDetails.from(item);
        }

        return ChatroomDetails.of(chatroom.get(), member);
    }

    public ChatroomDetails findChatroomInfo(String chatroomId, long memberId) throws ExistChatRoomException, NotChatroomMemberException, ExistMemberIdException {
        Chatroom chatroom = chatRoomService.findByChatroomId(chatroomId);
        Member member = memberService.findById(memberId);

        if (!chatroom.isChatroomMember(member)) {
            throw new NotChatroomMemberException("채팅방 멤버가 아닙니다.");
        }


        return ChatroomDetails.of(chatroom, member);
    }

    public ChatroomList findChatroomList(ChatItem chatItem, Long id) throws ExistMemberIdException, ExistItemException {
        PageRequest pageRequest = PageRequest.of(chatItem.getPage(), FILTER_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        ChatroomList chatroomList;
        Member member = memberService.findById(id);
        //채팅방 id가 존재하지 않고 멤버id가 존재할 경우
        if (chatItem.getItemId() == null) {
            chatroomList = chatRoomService.findChatroomListByMember(pageRequest, member);
        } else {
            Item item = itemService.findById(chatItem.getItemId());
            chatroomList = chatRoomService.findChatroomListByItem(pageRequest, item);
        }
        //마지막 채팅로그 추가
        chatroomList.addLastMessage(chatroomCacheService.addLastMessage(chatroomList.getChatRooms(), member.getId()));
        chatroomList.sort();

        return chatroomList;
    }

    @Transactional
    public String create(Long itemId, Long memberId) throws ExistMemberIdException, ExistItemException, ExistChatRoomException, BuyerException {
        Member buyer = memberService.findById(memberId);
        Item item = itemService.findById(itemId);
        Chatroom createdChatroom = chatRoomService.create(item, buyer);
        return createdChatroom.getChatroomId().toString();
    }

    public void exitChatroom(String chatId, Long memberId) throws ExistMemberIdException, ExistChatRoomException, NotChatroomMemberException {
        Member member = memberService.findById(memberId);
        Chatroom chatroom = chatRoomService.findByChatroomId(chatId);
        chatRoomService.exitChatroom(chatroom, member);
    }
}
