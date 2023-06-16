package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberLogin;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.exception.UnauthorizedException;
import com.team5.secondhand.api.member.repository.MemberBasedRegionRepository;
import com.team5.secondhand.api.member.repository.MemberRepository;
import com.team5.secondhand.api.model.Region;
import com.team5.secondhand.api.oauth.dto.UserProfile;
import com.team5.secondhand.global.exception.EmptyBasedRegionException;
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

    public MemberDetails login(MemberLogin loginDTO) throws UnauthorizedException, EmptyBasedRegionException {
        //todo : 레포에 해당 id가 있는지 확인 -> 있으면 성공, 없으면 권한 없음
        Member member = memberRepository.findByMemberId(loginDTO.getMemberId())
                .orElseThrow(() -> new UnauthorizedException("가입되지 않은 회원입니다"));

        return MemberDetails.fromMember(member);
    }

    public MemberDetails loginByOAuth(UserProfile loginDTO) throws UnauthorizedException, EmptyBasedRegionException {
        Member member = memberRepository.findByMemberId(loginDTO.getLogin())
                .orElseThrow(() -> new UnauthorizedException("가입되지 않은 회원입니다", loginDTO));

        return MemberDetails.fromMember(member);
    }

    public Boolean isExistMemberId(String memberId) throws ExistMemberIdException {
        return memberRepository.existsByMemberId(memberId);
    }

    @Transactional
    public int updateProfileImage(Long id, String uploadUrl) {
        return memberRepository.updateMemberProfileImage(id,uploadUrl);
    }
}
