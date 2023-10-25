package com.team5.secondhand.application.member.service;

import com.team5.secondhand.application.member.domain.Oauth;
import com.team5.secondhand.application.member.dto.request.MemberJoin;
import com.team5.secondhand.application.member.exception.ExistMemberIdException;
import com.team5.secondhand.application.region.domain.Region;

import java.util.Map;

public interface JoinService {
    Long join(MemberJoin joinDto, Map<Region, Boolean> regions, Oauth platform) throws ExistMemberIdException;
}
