package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.model.Region;

import java.util.Map;

public interface JoinService {
    Long join(MemberJoin joinDto, Map<Region, Boolean> regions) throws ExistMemberIdException;
}
