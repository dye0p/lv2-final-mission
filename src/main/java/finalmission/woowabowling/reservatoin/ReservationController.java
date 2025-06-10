package finalmission.woowabowling.reservatoin;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> register(@RequestBody final ReservationRequest request) {
        final ReservationResponse response = reservationService.register(request);
        return ResponseEntity.created(URI.create("/reservations" + response.id())).body(response);
    }

}
