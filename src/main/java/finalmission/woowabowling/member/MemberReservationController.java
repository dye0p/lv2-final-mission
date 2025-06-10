package finalmission.woowabowling.member;

import finalmission.woowabowling.reservatoin.ReservationResponse;
import finalmission.woowabowling.reservatoin.ReservationService;
import finalmission.woowabowling.reservatoin.UpdateReservationRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<ReservationResponse>> findAll(final LoginMember loginMember) {
        final List<ReservationResponse> response = reservationService.finaAllByMember(loginMember);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservations-mine/{id}")
    public ResponseEntity<Long> cancel(final LoginMember loginMember,
                                       @PathVariable(name = "id") final Long reservationId) {
        final Long deleteId = reservationService.cancel(loginMember, reservationId);
        return ResponseEntity.ok(deleteId);
    }

    @PostMapping("/reservations-mine/{id}")
    public ResponseEntity<ReservationResponse> update(
            final LoginMember loginMember,
            @PathVariable(name = "id") final Long reservationId,
            @RequestBody final UpdateReservationRequest request
    ) {
        final ReservationResponse response = reservationService.update(loginMember, reservationId, request);
        return ResponseEntity.ok(response);
    }

}
