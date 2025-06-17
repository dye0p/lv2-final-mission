package finalmission.woowabowling.reservatoin.controller;

import finalmission.woowabowling.member.LoginMember;
import finalmission.woowabowling.reservatoin.ReservationRequest;
import finalmission.woowabowling.reservatoin.ReservationResponse;
import finalmission.woowabowling.reservatoin.UpdateReservationRequest;
import finalmission.woowabowling.reservatoin.service.ReservationService;
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
    public ResponseEntity<ReservationResponse> register(final LoginMember loginMember,
                                                        @RequestBody final ReservationRequest request) {
        final ReservationResponse response = reservationService.register(request, loginMember.id());
        return ResponseEntity.created(URI.create("/reservations" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll(final LoginMember loginMember) {
        final List<ReservationResponse> response = reservationService.finaAllByMember(loginMember);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> cancel(final LoginMember loginMember,
                                       @PathVariable(name = "id") final Long reservationId) {
        final Long deleteId = reservationService.cancel(loginMember, reservationId);
        return ResponseEntity.ok(deleteId);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ReservationResponse> update(
            final LoginMember loginMember,
            @PathVariable(name = "id") final Long reservationId,
            @RequestBody final UpdateReservationRequest request
    ) {
        final ReservationResponse response = reservationService.update(loginMember, reservationId, request);
        return ResponseEntity.ok(response);
    }

}
