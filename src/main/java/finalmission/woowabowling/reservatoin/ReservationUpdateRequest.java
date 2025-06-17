package finalmission.woowabowling.reservatoin;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationUpdateRequest(
        long laneId,
        long memberCount,
        long gameCount,
        LocalDate reservationDate,
        LocalTime reservationTime
) {
}
