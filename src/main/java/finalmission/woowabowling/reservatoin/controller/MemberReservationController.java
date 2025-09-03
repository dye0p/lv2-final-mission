package finalmission.woowabowling.reservatoin.controller;

import finalmission.woowabowling.auth.service.response.LoginMember;
import finalmission.woowabowling.reservatoin.controller.request.ReservationRegisterRequest;
import finalmission.woowabowling.reservatoin.controller.request.ReservationUpdateRequest;
import finalmission.woowabowling.reservatoin.service.ReservationService;
import finalmission.woowabowling.reservatoin.service.response.ReservationRegisterResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/reservations-mine")
@RestController
public class MemberReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationRegisterResponse> register(final LoginMember loginMember,
                                                                @RequestBody final ReservationRegisterRequest request) {
        final ReservationRegisterResponse response = reservationService.register(request, loginMember.id());
        return ResponseEntity.created(URI.create("/reservations-mine/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationRegisterResponse>> findAll(final LoginMember loginMember) {
        final List<ReservationRegisterResponse> response = reservationService.findAllByMember(loginMember.id());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> cancel(final LoginMember loginMember,
                                       @PathVariable(name = "id") final Long reservationId) {
        final Long deleteId = reservationService.cancel(loginMember.id(), reservationId);
        return ResponseEntity.ok(deleteId);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ReservationRegisterResponse> update(
            final LoginMember loginMember,
            @PathVariable(name = "id") final Long reservationId,
            @RequestBody final ReservationUpdateRequest request
    ) {
        final ReservationRegisterResponse response = reservationService.update(loginMember.id(), reservationId,
                request);
        return ResponseEntity.ok(response);
    }

}
