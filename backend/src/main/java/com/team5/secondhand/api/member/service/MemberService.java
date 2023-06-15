package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberLogin;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.repository.MemberBasedRegionRepository;
import com.team5.secondhand.api.member.repository.MemberRepository;
import com.team5.secondhand.api.model.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinService {

    private final MemberRepository memberRepository;
    private final MemberBasedRegionRepository basedRegionRepository;

    @Transactional
    @Override
    public Long join(MemberJoin joinDto, Map<Region, Boolean> regions) {

        Member newMember = joinDto.toMember();
        List<BasedRegion> basedRegions = new ArrayList<>();

        regions.keySet().forEach(region -> {
            if (regions.get(region)) {
                basedRegions.add(BasedRegion.create(newMember, region));
            }
            if (!regions.get(region)) {
                basedRegions.add(BasedRegion.addSubRegion(newMember, region));
            }
        });

        newMember.updateBasedRegions(basedRegions);
        memberRepository.save(newMember);

        return newMember.getId();
    }

    public void login(MemberLogin loginDTO) {
        //todo : 레포에 해당 id가 있는지 확인 -> 있으면 성공, 없으면 권한 없음

    }

    public Boolean isExistMemberId(String memberId) throws ExistMemberIdException {
        return memberRepository.existsByMemberId(memberId);
    }

    public boolean updateProfileImage(Long id, String uploadUrl) {
        return memberRepository.updateMemberProfileImage(id,uploadUrl)==1;
    }
}
