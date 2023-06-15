package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberLogin;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.repositoy.MemberBasedRegionRepository;
import com.team5.secondhand.api.member.repositoy.MemberRepository;
import com.team5.secondhand.api.model.Region;
import com.team5.secondhand.api.model.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinService {

    private final MemberRepository memberRepository;
    private final MemberBasedRegionRepository basedRegionRepository;
    private final RegionRepository regionRepository;

    @Transactional
    @Override
    public Long join(MemberJoin joinDto) throws ExistMemberIdException {
        boolean existsMemberId = memberRepository.existsByMemberId(joinDto.getMemberId());

        if (existsMemberId) {
            throw new ExistMemberIdException("이미 존재하는 회원 ID 입니다.");
        }

        List<Region> regions = regionRepository.findAllById(joinDto.getRegions());

        Member newMember = memberRepository.save(joinDto.toMember());
        List<BasedRegion> basedRegions = new ArrayList<>();

        basedRegions.add(BasedRegion.create(newMember, regions.get(0)));
        basedRegions.addAll(regions.stream().skip(1)
                .map(region -> BasedRegion.addSubRegion(newMember, region))
                .collect(Collectors.toList()));

        basedRegionRepository.saveAll(basedRegions);
        return newMember.getId();
    }

    //로그인
    public void login(MemberLogin loginDTO) {
        //todo : 레포에 해당 id가 있는지 확인 -> 있으면 성공, 없으면 권한 없음

    }

    public boolean existsByMemberId(String id) {
        return memberRepository.existsByMemberId(id);
    }
}
