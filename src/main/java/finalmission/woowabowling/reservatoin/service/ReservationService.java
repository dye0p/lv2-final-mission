package finalmission.woowabowling.reservatoin.service;

import finalmission.woowabowling.lane.domain.Lane;
import finalmission.woowabowling.lane.domain.LaneRepository;
import finalmission.woowabowling.member.domain.Member;
import finalmission.woowabowling.member.domain.MemberRepository;
import finalmission.woowabowling.reservatoin.controller.request.ReservationRegisterRequest;
import finalmission.woowabowling.reservatoin.controller.request.ReservationUpdateRequest;
import finalmission.woowabowling.reservatoin.domain.Reservation;
import finalmission.woowabowling.reservatoin.domain.ReservationRepository;
import finalmission.woowabowling.reservatoin.service.response.ReservationRegisterResponse;
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
    public ReservationRegisterResponse register(final ReservationRegisterRequest request, final Long memberId) {
        final Member member = findMember(memberId);
        final Lane lane = findLane(request.laneId());
        final Reservation reservation = request.toReservation(member, lane);
        final Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationRegisterResponse.of(savedReservation);
    }

    public List<ReservationRegisterResponse> findAll() {
        final List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationRegisterResponse::of)
                .toList();
    }

    public List<ReservationRegisterResponse> findAllByMember(final Long loginMemberId) {
        final List<Reservation> reservations = reservationRepository.findByMemberId(loginMemberId);
        return reservations.stream()
                .map(ReservationRegisterResponse::of)
                .toList();
    }

    @Transactional
    public Long cancel(final Long loginMemberId, final Long id) {
        final Member member = findMember(loginMemberId);
        final List<Reservation> memberReservations = reservationRepository.findByMemberId(member.getId());

        final Reservation reservation = findReservationBy(id, memberReservations);
        reservationRepository.deleteById(reservation.getId());

        return reservation.getId();
    }

    @Transactional
    public ReservationRegisterResponse update(final Long loginMemberId, final Long id,
                                              final ReservationUpdateRequest request) {
        final Member member = findMember(loginMemberId);
        final Lane lane = findLane(request.laneId());
        final List<Reservation> memberReservations = reservationRepository.findByMemberId(member.getId());

        final Reservation reservation = findReservationBy(id, memberReservations);
        reservation.update(
                lane,
                request.memberCount(),
                request.gameCount(),
                request.reservationDate(),
                request.reservationTime()
        );

        return ReservationRegisterResponse.of(reservation);
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

}
