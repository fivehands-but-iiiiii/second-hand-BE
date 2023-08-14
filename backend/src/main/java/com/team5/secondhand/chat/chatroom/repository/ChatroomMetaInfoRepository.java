package com.team5.secondhand.chat.chatroom.repository;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatroomMetaInfoRepository extends MongoRepository<Chatroom, String> {
}
