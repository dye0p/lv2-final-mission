package finalmission.woowabowling.member.controller;

import finalmission.woowabowling.member.controller.request.LoginRequest;
import finalmission.woowabowling.member.service.MemberService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Long> signup(@RequestBody final LoginRequest loginRequest) {
        final Long id = memberService.register(loginRequest.email(), loginRequest.password());
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

}
