package com.team5.secondhand.chat.chatroom.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Participants {
    private Map<String, ParticipantInfo> info = new ConcurrentHashMap<>();

    public boolean init(Map<String, String> memberInfo) {
        memberInfo.forEach((key, value) -> info.put(key, ParticipantInfo.init(key, value)));
        return memberInfo.size() == info.size();
    }

    public boolean enter(String memberId) {
        ParticipantInfo participantInfo;
        if ((participantInfo = info.get(memberId))==null) {
            return false;
        }

        participantInfo.connect();
        info.put(memberId, participantInfo);
        return true;
    }
}
