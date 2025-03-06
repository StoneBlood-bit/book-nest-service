package mate.academy.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private final Map<String, List<String>> excludedPathsMap = Map.of(
            "/auth/", List.of("GET", "POST"),
            "/health", List.of("GET"),
            "/books", List.of("GET"),
            "/auth/callback/google/", List.of("GET")
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (isExcludedPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getToken(request);

        if (token != null && jwtUtil.isValidToken(token)) {
            String username = jwtUtil.getUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("Header: {} = {}", headerName, request.getHeader(headerName));
        }

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            logger.info("Param: {} = {}", paramName, request.getParameter(paramName));
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                logger.info("Cookie: {} = {}", cookie.getName(), cookie.getValue());
            }
        }

        logger.info("Request Path: {}", request.getRequestURI());
        logger.info("Request Method: {}", request.getMethod());

        try {
            StringBuilder body = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            logger.info("Request Body: {}", body.toString());
        } catch (IOException e) {
            logger.error("Error reading request body", e);
        }

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                logger.info("COOKIE: {} = {}", cookie.getName(), cookie.getValue());
                if ("token".equals(cookie.getName())) {
                    logger.info("returned: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private boolean isExcludedPath(HttpServletRequest request) {
        String requestPath = request.getServletPath();
        String method = request.getMethod();

        boolean isExcluded = excludedPathsMap.entrySet().stream()
                .anyMatch(entry -> requestPath
                        .startsWith(entry.getKey()) && entry.getValue().contains(method));
        return isExcluded;
    }
}
