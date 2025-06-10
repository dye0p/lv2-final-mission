package finalmission.woowabowling.member;

import finalmission.woowabowling.reservatoin.ReservationResponse;
import finalmission.woowabowling.reservatoin.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
