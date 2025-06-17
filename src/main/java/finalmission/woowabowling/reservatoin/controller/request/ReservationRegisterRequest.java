package finalmission.woowabowling.reservatoin.controller.request;

import finalmission.woowabowling.lane.domain.Lane;
import finalmission.woowabowling.member.domain.Member;
import finalmission.woowabowling.reservatoin.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRegisterRequest(
        long laneId,
        long memberCount,
        long gameCount,
        LocalDate reservationDate,
        LocalTime reservationTime
) {

    public Reservation toReservation(final Member member, final Lane lane) {
        return Reservation.from(
                member,
                lane,
                memberCount,
                gameCount,
                reservationDate,
                reservationTime
        );
    }
}
