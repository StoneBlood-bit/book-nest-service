package mate.academy.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    private static final String TOKEN_COOKIE_NAME = "token";
    private static final String COOKIE_ATTRIBUTE_NAME = "SameSite";
    private static final String COOKIE_ATTRIBUTE_VALUE = "None";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60;

    public void addTokenCookie(HttpServletResponse response, String token) throws IOException {
        if (token == null || token.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token");
            return;
        }
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setAttribute(COOKIE_ATTRIBUTE_NAME, COOKIE_ATTRIBUTE_VALUE);
        response.addCookie(cookie);
    }

    public void clearTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
