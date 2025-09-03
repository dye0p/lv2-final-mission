package finalmission.woowabowling.reservatoin.service.response;

import finalmission.woowabowling.reservatoin.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRegisterResponse(
        long id,
        String memberName,
        int laneNumber,
        long memberCount,
        long gameCount,
        LocalDate reservationDate,
        LocalTime reservationTime
) {

    public static ReservationRegisterResponse of(final Reservation savedReservation) {
        return new ReservationRegisterResponse(
                savedReservation.getId(),
                savedReservation.getMemberName(),
                savedReservation.getLanNumber(),
                savedReservation.getMemberCount(),
                savedReservation.getGameCount(),
                savedReservation.getDate(),
                savedReservation.getTime()
        );
    }
}
