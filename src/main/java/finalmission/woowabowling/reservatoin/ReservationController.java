package finalmission.woowabowling.reservatoin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> findAll() {
        final List<ReservationResponse> response = reservationService.findAll();
        return ResponseEntity.ok(response);
    }
}
