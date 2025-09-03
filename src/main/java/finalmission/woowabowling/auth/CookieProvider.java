package finalmission.woowabowling.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public Cookie createCookie(final String name, final String accessToken) {
        final Cookie cookie = new Cookie(name, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

}
