package finalmission.woowabowling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public String createToken(final LoginRequest request) {
        final Member member = findMemberByEmail(request.email());
        return jwtTokenProvider.createToken(member);
    }

    private Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일과 일치하는 회원을 찾을 수 없습니다."));
    }
}
