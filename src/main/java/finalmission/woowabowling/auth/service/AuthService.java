package finalmission.woowabowling.auth.service;

import finalmission.woowabowling.auth.JwtTokenProvider;
import finalmission.woowabowling.auth.service.response.LoginMember;
import finalmission.woowabowling.member.domain.Member;
import finalmission.woowabowling.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public String createToken(final String email) {
        final Member member = findMemberByEmail(email);
        return jwtTokenProvider.createToken(member);
    }

    private Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일과 일치하는 회원을 찾을 수 없습니다."));
    }

    public LoginMember findLoginMemberByToken(final String token) {
        final Member member = findMemberByToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail());
    }

    private Member findMemberByToken(final String token) {
        final Long id = jwtTokenProvider.getSubjectFromPayloadBy(token);
        return findMemberById(id);
    }

    private Member findMemberById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. 회원가입 또는 로그인을 해주세요"));
    }


}
