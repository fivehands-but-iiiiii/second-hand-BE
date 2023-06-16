package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.repository.MemberBasedRegionRepository;
import com.team5.secondhand.api.member.repository.MemberRepository;
import com.team5.secondhand.api.model.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinService {

    private final MemberRepository memberRepository;
    private final MemberBasedRegionRepository basedRegionRepository;

    @Override
    @Transactional
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

    public Boolean isExistMemberId(String memberId) throws ExistMemberIdException {
        return memberRepository.existsByMemberId(memberId);
    }

    @Transactional
    public int updateProfileImage(Long id, String uploadUrl) {
        return memberRepository.updateMemberProfileImage(id,uploadUrl);
    }
}
