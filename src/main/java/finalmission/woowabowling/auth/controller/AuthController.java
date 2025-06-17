package finalmission.woowabowling.auth.controller;

import finalmission.woowabowling.auth.CookieProvider;
import finalmission.woowabowling.auth.service.AuthService;
import finalmission.woowabowling.member.controller.request.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private static final String COOKIE_NAME = "token";

    private final AuthService authService;
    private final CookieProvider cookieProvider;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid final LoginRequest request,
                                      final HttpServletResponse response) {
        final String accessToken = authService.createToken(request.email());
        final Cookie cookie = cookieProvider.createCookie(COOKIE_NAME, accessToken);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
