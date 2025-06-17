package finalmission.woowabowling.reservatoin;

import finalmission.woowabowling.lane.Lane;
import finalmission.woowabowling.member.Member;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(
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
