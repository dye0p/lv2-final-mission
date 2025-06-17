package finalmission.woowabowling.reservatoin;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.woowabowling.member.Member;
import finalmission.woowabowling.member.MemberRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("특정 사용자의 예약을 저장할 수 있다.")
    @Test
    void register() {
        //given
        Member member = Member.from("aaa", "aaa", "1234");
        Member savedMember = memberRepository.save(member);

        ReservationRequest request = new ReservationRequest(
                1L,
                3L,
                4L,
                LocalDate.of(2025, 12, 12),
                LocalTime.of(10, 0));

        //when
        ReservationResponse actual = reservationService.register(request, member.getId());

        //then
        assertThat(actual.id()).isEqualTo(1L);
    }
}
