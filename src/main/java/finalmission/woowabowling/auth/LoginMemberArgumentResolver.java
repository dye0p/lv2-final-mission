package finalmission.woowabowling.auth;

import finalmission.woowabowling.auth.service.AuthService;
import finalmission.woowabowling.auth.service.response.LoginMember;
import finalmission.woowabowling.exception.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final JwtExtractor jwtExtractor;

    public LoginMemberArgumentResolver(final AuthService authService, final JwtExtractor jwtExtractor) {
        this.authService = authService;
        this.jwtExtractor = jwtExtractor;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final Cookie[] cookies = request.getCookies();
        validateNull(cookies);

        final String token = jwtExtractor.extractTokenFromCookie("token", cookies);
        return authService.findLoginMemberByToken(token);
    }

    private void validateNull(final Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new AuthException("토큰을 찾을 수 없습니다.");
        }
    }

}
