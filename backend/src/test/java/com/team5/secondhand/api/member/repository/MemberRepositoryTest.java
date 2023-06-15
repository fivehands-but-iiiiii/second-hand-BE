package com.team5.secondhand.api.member.repository;

import com.team5.secondhand.api.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    
    @Test
    @DisplayName("인덱스로 멤버를 찾을 수 있다.")
    public void findById() throws Exception{
        //given
        Member member = memberRepository.findById(1L).orElseThrow();
        //then
        assertThat(member.getMemberId()).isEqualTo("new-pow");
    }

    @Test
    @DisplayName("멤버 ID가 이미 있는지 확인할 수 있다.")
    public void exists() throws Exception{
        //given
        boolean b = memberRepository.existsByMemberId("NEW-pow");
    
        //then
        assertThat(b).isTrue();
    }
    
}
