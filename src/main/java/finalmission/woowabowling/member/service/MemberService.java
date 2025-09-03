package finalmission.woowabowling.member.service;

import finalmission.woowabowling.client.RandomNameRestClient;
import finalmission.woowabowling.member.domain.Member;
import finalmission.woowabowling.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final RandomNameRestClient randomNameRestClient;
    private final MemberRepository memberRepository;

    public Long register(final String email, final String password) {
        final String name = randomNameRestClient.request();
        final Member member = Member.from(name, email, password);
        final Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

}
