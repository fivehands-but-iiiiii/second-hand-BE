package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.domain.BasedRegion;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.domain.Oauth;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberLogin;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.ExistMemberIdException;
import com.team5.secondhand.api.member.exception.UnauthorizedException;
import com.team5.secondhand.api.member.exception.UnauthorizedGithubMemberException;
import com.team5.secondhand.api.member.exception.NotValidMemberIdException;
import com.team5.secondhand.api.member.repository.MemberBasedRegionRepository;
import com.team5.secondhand.api.member.repository.MemberRepository;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.oauth.dto.UserProfile;
import com.team5.secondhand.api.member.exception.MemberDataCorruptedException;
import com.team5.secondhand.global.exception.EmptyBasedRegionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinService {

    private final String ID_PATTERN = "^[a-z0-9]+$";

    private final MemberRepository memberRepository;
    private final MemberBasedRegionRepository basedRegionRepository;

    @Override
    @Transactional
    public Long join(MemberJoin joinDto, Map<Region, Boolean> regions, Oauth joinPlatform) {

        Member newMember = joinDto.toMember();
        newMember.updatePlatform(joinPlatform);
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
        Member member = memberRepository.findByMemberIdAndOauth(loginDTO.getMemberId(), Oauth.NONE)
                .orElseThrow(() -> new UnauthorizedException("가입되지 않은 회원입니다"));

        return MemberDetails.fromMember(member);
    }

    public MemberDetails loginByOAuth(UserProfile loginDTO) throws UnauthorizedGithubMemberException, EmptyBasedRegionException {
        Member member = memberRepository.findByMemberIdAndOauth(loginDTO.getLogin(), Oauth.GITHUB)
                .orElseThrow(() -> new UnauthorizedGithubMemberException("가입되지 않은 GITHUB 회원입니다", loginDTO));

        return MemberDetails.fromMember(member);
    }

    public boolean isExistMemberId(String memberId, Oauth oauth) {
        return memberRepository.existsByMemberIdAndOauth(memberId, oauth);
    }

    @Transactional
    public int updateProfileImage(Long id, String uploadUrl) {
        return memberRepository.updateMemberProfileImage(id,uploadUrl);
    }

    public void isValidMemberId(String memberId) throws ExistMemberIdException, NotValidMemberIdException {
        if (isExistMemberId(memberId, Oauth.NONE)) {
            throw new ExistMemberIdException("이미 존재하는 회원 아이디입니다.");
        }
        if (!isFormatMemberId(memberId)) {
            throw new NotValidMemberIdException("아이디 형식이 올바르지 않습니다.");
        }
    }

    private boolean isFormatMemberId(String memberId) {
        return Pattern.matches(ID_PATTERN, memberId);
    }

    public void checkDataCorruption(MemberJoin request, UserProfile tempMember) throws MemberDataCorruptedException {
        if (!request.toMember().equals(tempMember.toMember())) {
            throw new MemberDataCorruptedException("유효하지 않은 회원가입 요청입니다.");
        }
    }
}
