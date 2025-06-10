package finalmission.woowabowling.auth;

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
                .orElseThrow(() -> new IllegalArgumentException("토큰을 찾을 수 없습니다."));
    }
}
