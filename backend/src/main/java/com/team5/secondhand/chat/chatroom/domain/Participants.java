package com.team5.secondhand.chat.chatroom.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participants implements Serializable {
    private Map<String, ParticipantInfo> info = new ConcurrentHashMap<>();

    public Participants(Map<String, ParticipantInfo> info) {
        this.info = info;
    }

    public static Participants init(List<String> memberIds) {
        Map<String, ParticipantInfo> info = new ConcurrentHashMap<>();
        memberIds.forEach(e -> info.put(e, ParticipantInfo.init(e)));

        if (info.size() != memberIds.size()) {
            throw new DuplicateFormatFlagsException("채팅방 기록 생성에 오류가 있습니다. 회원 아이디를 확인해주세요.");
        }
        return new Participants(info);
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

    public boolean getMessage(Long receiver) {
        ParticipantInfo member;
        if((member=info.get(receiver))==null) {
            return false;
        }
        member.plusBubble();
        return true;
    }

    public boolean exit(String memberId) {
        ParticipantInfo participantInfo;
        if ((participantInfo = info.get(memberId))==null) {
            return false;
        }

        participantInfo.disconnect();
        info.put(memberId, participantInfo);
        return true;
    }

    public boolean hasMember(String id) {
        return this.info.get(id).getIsConnected();
    }
}
