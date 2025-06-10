package finalmission.woowabowling.member;

import finalmission.woowabowling.client.RandomNameRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final RandomNameRestClient randomNameRestClient;
    private final MemberRepository memberRepository;

    public Long register(final LoginRequest loginRequest) {
        final String name = randomNameRestClient.requestConfirm();
        final Member member = loginRequest.toMember(name);
        final Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

}
