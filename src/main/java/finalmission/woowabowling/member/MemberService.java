package finalmission.woowabowling.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long register(final LoginRequest loginRequest) {
        final Member member = loginRequest.toMember();
        final Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

}
