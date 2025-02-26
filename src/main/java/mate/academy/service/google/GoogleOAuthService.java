package mate.academy.service.google;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.User;
import mate.academy.repository.user.UserRepository;
import mate.academy.security.AuthenticationService;
import mate.academy.security.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final Environment env;

    public String authenticationWithGoogle(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);

        String redirectUri = env.getProperty("google.redirect-uri");
        params.add("redirect_uri", redirectUri);

        String clientId = env.getProperty("google.client-id");
        params.add("client_id", clientId);

        String clientSecret = env.getProperty("google.client-secret");
        params.add("client_secret", clientSecret);

        String tokenUri = env.getProperty("google.token-uri");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Error getting access token from Google");
        }

        String accessToken = (String) response.getBody().get("access_token");

        String userInfoUri = env.getProperty("google.user-info-uri");
        HttpHeaders userinfoHeaders = new HttpHeaders();
        userinfoHeaders.setBearerAuth(accessToken);

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userinfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUri, HttpMethod.GET, userInfoRequest, Map.class
        );

        if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Error getting user information");
        }

        Map<String, Object> userInfo = userInfoResponse.getBody();

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(name);
            newUser.setLastName("");
            newUser.setPassword("");
            newUser.setTokens(0);
            newUser.setRole(User.Role.CUSTOMER);
            newUser.setDeleted(false);
            return userRepository.save(newUser);
        });
        logger.error("Method authenticationWithGoogle was called, user: {}", user.getEmail());

        return jwtUtil.generateToken(user.getEmail());
    }
}
