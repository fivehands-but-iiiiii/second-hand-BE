package com.team5.secondhand.api.member.service;

import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.api.member.domain.Oauth;
import com.team5.secondhand.api.member.dto.request.BasedRegionSummary;
import com.team5.secondhand.api.member.dto.request.MemberJoin;
import com.team5.secondhand.api.member.dto.request.MemberLogin;
import com.team5.secondhand.api.member.dto.request.MemberRegion;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.api.member.exception.*;
import com.team5.secondhand.api.member.repository.MemberRepository;
import com.team5.secondhand.api.oauth.dto.UserProfile;
import com.team5.secondhand.api.region.domain.Region;
import com.team5.secondhand.api.region.exception.EmptyBasedRegionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService implements JoinService {

    private final String ID_PATTERN = "^[a-z0-9]+$";

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long join(MemberJoin joinDto, Map<Region, Boolean> regions, Oauth joinPlatform) {

        Member newMember = joinDto.toMember();
        newMember.updatePlatform(joinPlatform);
        newMember.updateBasedRegions(regions);

        if (newMember.getProfileImgUrl().length()==0){
            newMember.setDefaultProfileImg();
        }

        memberRepository.save(newMember);

        return newMember.getId();
    }

    @Transactional(readOnly = true)
    public MemberDetails login(MemberLogin loginDTO) throws UnauthorizedException, EmptyBasedRegionException {
        Member member = memberRepository.findByMemberIdAndOauth(loginDTO.getMemberId(), Oauth.NONE)
                .orElseThrow(() -> new UnauthorizedException("가입되지 않은 회원입니다"));

        return MemberDetails.fromMember(member);
    }

    @Transactional(readOnly = true)
    public MemberDetails loginByOAuth(UserProfile loginDTO) throws UnauthorizedGithubMemberException, EmptyBasedRegionException {
        Member member = memberRepository.findByMemberIdAndOauth(loginDTO.getLogin(), Oauth.GITHUB)
                .orElseThrow(() -> new UnauthorizedGithubMemberException("가입되지 않은 GITHUB 회원입니다", loginDTO));

        return MemberDetails.fromMember(member);
    }

    @Transactional(readOnly = true)
    public boolean isExistMemberId(String memberId, Oauth oauth) {
        return memberRepository.existsByMemberIdAndOauth(memberId, oauth);
    }

    @Transactional
    public Member findById(Long id) throws ExistMemberIdException {
        return memberRepository.findById(id).orElseThrow(() -> new ExistMemberIdException("잘못된 회원입니다."));
    }

    @Transactional
    public int updateProfileImage(Long id, String uploadUrl) {
        return memberRepository.updateMemberProfileImage(id, uploadUrl);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public void checkDataCorruption(MemberJoin request, UserProfile tempMember) throws MemberDataCorruptedException {
        if (!request.toMember().equals(tempMember.toMember())) {
            throw new MemberDataCorruptedException("유효하지 않은 회원가입 요청입니다.");
        }
    }

    @Transactional
    public void updateRegions(Long id, Map<Region, Boolean> basedRegions) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 회원이 없습니다."));

        member.updateBasedRegions(basedRegions);
        memberRepository.save(member);
    }

    @Transactional
    public void switchRegions(Long id, MemberRegion memberRegion) {
        for (BasedRegionSummary region : memberRegion.getRegions()) {
            if (memberRepository.switchBasedRegion(region.isOnFocus(), id, region.getId()) != 1) {
                throw new IllegalArgumentException("대표지역을 변경할 수 없습니다.");
            }
        }
    }
}
