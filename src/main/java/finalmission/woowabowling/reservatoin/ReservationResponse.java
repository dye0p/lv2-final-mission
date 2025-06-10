package finalmission.woowabowling.reservatoin;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(

        long id,
        String memberName,
        long lane,
        long memberCount,
        long gameCount,
        LocalDate reservationDate,
        LocalTime reservationTime
) {
    public static ReservationResponse of(final Reservation savedReservation) {
        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getMemberName(),
                savedReservation.getLaneId(),
                savedReservation.getMemberCount(),
                savedReservation.getGameCount(),
                savedReservation.getDate(),
                savedReservation.getTime()
        );
    }
}
