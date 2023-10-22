package com.team5.secondhand.application.member.dto.response;

import lombok.Getter;

@Getter
public class MemberDetailsWithToken extends MemberDetails {

    private String token;

    public MemberDetailsWithToken (MemberDetails memberDetails, String token) {
        super(memberDetails.getId(), memberDetails.getMemberId(), memberDetails.getProfileImgUrl(), memberDetails.getOauth(), memberDetails.getRegions());
        this.token = token;
    }

}
