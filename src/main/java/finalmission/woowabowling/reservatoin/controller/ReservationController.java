package finalmission.woowabowling.reservatoin.controller;

import finalmission.woowabowling.reservatoin.service.ReservationService;
import finalmission.woowabowling.reservatoin.service.response.ReservationRegisterResponse;
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
    public ResponseEntity<List<ReservationRegisterResponse>> findAll() {
        final List<ReservationRegisterResponse> response = reservationService.findAll();
        return ResponseEntity.ok(response);
    }
}
