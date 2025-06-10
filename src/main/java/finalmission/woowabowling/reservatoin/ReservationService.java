package finalmission.woowabowling.reservatoin;

import finalmission.woowabowling.member.LoginMember;
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

    public List<ReservationResponse> findAll() {
        final List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::of)
                .toList();
    }

    public List<ReservationResponse> finaAllByMember(final LoginMember loginMember) {
        final List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.id());
        return reservations.stream()
                .map(ReservationResponse::of)
                .toList();
    }

    @Transactional
    public Long cancel(final LoginMember loginMember, final Long id) {
        final Member member = findMember(loginMember.id());
        final List<Reservation> memberReservations = reservationRepository.findByMemberId(member.getId());

        final Reservation reservation = findReservationBy(id, memberReservations);
        reservationRepository.deleteById(reservation.getId());

        return reservation.getId();
    }

    @Transactional
    public ReservationResponse update(final LoginMember loginMember, final Long id,
                                      final UpdateReservationRequest request) {
        final Member member = findMember(loginMember.id());
        final List<Reservation> memberReservations = reservationRepository.findByMemberId(member.getId());

        Reservation reservation = findReservationBy(id, memberReservations);
        updateReservation(request, reservation);

        return ReservationResponse.of(reservation);
    }

    private Member findMember(final long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    private Reservation findReservationBy(final Long id, final List<Reservation> memberReservations) {
        return memberReservations.stream()
                .filter(memberReservation -> memberReservation.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 예약이거나, 해당 회원의 예약이 아닙니다."));
    }

    private void updateReservation(final UpdateReservationRequest request, final Reservation reservation) {
        reservation.update(
                request.laneId(),
                request.memberCount(),
                request.gameCount(),
                request.reservationDate(),
                request.reservationTime()
        );
    }
}
