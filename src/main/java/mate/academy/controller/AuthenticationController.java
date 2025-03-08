package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserLoginRequestDto;
import mate.academy.dto.user.UserLoginResponseDto;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserRegistrationResponseDto;
import mate.academy.exception.RegistrationException;
import mate.academy.security.AuthenticationService;
import mate.academy.security.CookieUtil;
import mate.academy.service.facebook.FacebookOAuthService;
import mate.academy.service.google.GoogleOAuthService;
import mate.academy.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "User registration",
        description = "Endpoints for registration and authentication user"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    public static final String RESPONSE_CONTENT_TYPE = "text/html;charset=UTF-8";
    public static final String RESPONSE_WRITE_CONTENT = "<script>window.location.href="
            + "'https://driven-truly-mule.ngrok-free.app/redirect';</script>";
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final GoogleOAuthService googleOAuthService;
    private final FacebookOAuthService facebookOAuthService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "login user", description = "user authentication")
    @PostMapping("/login")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto requestDto,
            HttpServletResponse response
    ) throws IOException {
        UserLoginResponseDto authenticate = authenticationService.authenticate(requestDto, response);
        return authenticate;
    }

    @Operation(summary = "registration user", description = "registration a new user")
    @PostMapping("/registration")
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto requestDto
    )
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = "login user", description = "user authentication from Google")
    @GetMapping("/callback/google")
    public void handleGoogleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response) throws IOException {
        String token = googleOAuthService.authenticationWithGoogle(code);
        cookieUtil.addTokenCookie(response, token);
        sendJsRedirect(response);
    }

    @Operation(summary = "login user", description = "user authentication from Facebook")
    @GetMapping("/callback/facebook")
    public void handleFacebookCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {
        String token = facebookOAuthService.authenticationWithFacebook(code);
        cookieUtil.addTokenCookie(response, token);
        sendJsRedirect(response);
    }

    private void sendJsRedirect(HttpServletResponse response) throws IOException {
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.getWriter().write(RESPONSE_WRITE_CONTENT);
    }
}
