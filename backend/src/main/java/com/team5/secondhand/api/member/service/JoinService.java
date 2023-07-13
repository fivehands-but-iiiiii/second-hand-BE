package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.domain.Oauth;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.region.domain.Region;

import java.util.Map;

public interface JoinService {
    Long join(MemberJoin joinDto, Map<Region, Boolean> regions, Oauth platform) throws ExistMemberIdException;
}
