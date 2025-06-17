package finalmission.woowabowling.reservatoin;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateReservationRequest(
        long laneId,
        long memberCount,
        long gameCount,
        LocalDate reservationDate,
        LocalTime reservationTime
) {
}
