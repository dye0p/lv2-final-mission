package finalmission.woowabowling.reservatoin.service;

import finalmission.woowabowling.lane.domain.Lane;
import finalmission.woowabowling.lane.domain.LaneRepository;
import finalmission.woowabowling.member.LoginMember;
import finalmission.woowabowling.member.domain.Member;
import finalmission.woowabowling.member.domain.MemberRepository;
import finalmission.woowabowling.reservatoin.ReservationRequest;
import finalmission.woowabowling.reservatoin.ReservationResponse;
import finalmission.woowabowling.reservatoin.UpdateReservationRequest;
import finalmission.woowabowling.reservatoin.domain.Reservation;
import finalmission.woowabowling.reservatoin.domain.ReservationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final LaneRepository laneRepository;

    @Transactional
    public ReservationResponse register(final ReservationRequest request, final Long memberId) {
        final Member member = findMember(memberId);
        final Lane lane = findLane(request.laneId());
        final Reservation reservation = request.toReservation(member, lane);
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
        final Lane lane = findLane(request.laneId());
        final List<Reservation> memberReservations = reservationRepository.findByMemberId(member.getId());

        final Reservation reservation = findReservationBy(id, memberReservations);
        updateReservation(request, reservation, lane);

        return ReservationResponse.of(reservation);
    }

    private Member findMember(final long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    private Lane findLane(final Long id) {
        return laneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 레인은 존재하지 않습니다."));
    }

    private Reservation findReservationBy(final Long id, final List<Reservation> memberReservations) {
        return memberReservations.stream()
                .filter(memberReservation -> memberReservation.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 예약이거나, 해당 회원의 예약이 아닙니다."));
    }

    private void updateReservation(final UpdateReservationRequest request, final Reservation reservation,
                                   final Lane lane) {
        reservation.update(
                lane,
                request.memberCount(),
                request.gameCount(),
                request.reservationDate(),
                request.reservationTime()
        );
    }
}
