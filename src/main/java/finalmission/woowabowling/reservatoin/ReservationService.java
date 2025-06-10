package finalmission.woowabowling.reservatoin;

import finalmission.woowabowling.member.Member;
import finalmission.woowabowling.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse register(final ReservationRequest request) {
        final Member member = findMember(request.memberId());
        final Reservation reservation = request.toReservation(member);
        final Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.of(savedReservation);
    }

    private Member findMember(final long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public List<ReservationResponse> findAll() {
        final List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::of)
                .toList();
    }
}
