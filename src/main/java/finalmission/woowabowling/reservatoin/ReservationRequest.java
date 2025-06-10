package finalmission.woowabowling.reservatoin;

import finalmission.woowabowling.member.Member;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(

        long memberId,
        long laneId,
        long memberCount,
        long gameCount,
        LocalDate reservationDate,
        LocalTime reservationTime
) {
    public Reservation toReservation(final Member member) {
        return Reservation.from(
                member,
                laneId,
                memberCount,
                gameCount,
                reservationDate,
                reservationTime
        );
    }
}
