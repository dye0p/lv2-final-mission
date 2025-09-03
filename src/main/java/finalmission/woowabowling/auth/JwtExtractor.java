package finalmission.woowabowling.auth;

import finalmission.woowabowling.exception.AuthException;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class JwtExtractor {

    public String extractTokenFromCookie(final String name, final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new AuthException("토큰을 찾을 수 없습니다."));
    }
}
