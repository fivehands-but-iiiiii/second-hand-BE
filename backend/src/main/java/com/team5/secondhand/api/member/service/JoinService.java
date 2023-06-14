package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;

public interface JoinService {
    Long join(MemberJoin joinDto) throws ExistMemberIdException;
}
