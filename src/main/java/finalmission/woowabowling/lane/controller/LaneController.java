package finalmission.woowabowling.lane.controller;

import finalmission.woowabowling.lane.controller.request.LaneRegisterRequest;
import finalmission.woowabowling.lane.service.LaneService;
import finalmission.woowabowling.lane.service.response.LaneRegisterResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LaneController {

    private final LaneService laneService;

    @PostMapping("/lanes")
    public ResponseEntity<LaneRegisterResponse> register(@RequestBody final LaneRegisterRequest request) {
        final LaneRegisterResponse response = laneService.register(request.number(), request.patternId());
        return ResponseEntity.created(URI.create("/lanes/" + response.id())).body(response);
    }
}
