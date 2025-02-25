package mate.academy.service.facebook;

import lombok.RequiredArgsConstructor;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.User;
import mate.academy.repository.user.UserRepository;
import mate.academy.security.JwtUtil;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FacebookOAuthService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final Environment env;

    public String authenticationWithFacebook(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", env.getProperty("facebook.client-id"));
        params.add("redirect_uri", env.getProperty("facebook.redirect-uri"));
        params.add("client_secret", env.getProperty("facebook.client-secret"));
        params.add("code", code);

        String tokenUri = env.getProperty("facebook.token-uri");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Error getting access token from Facebook");
        }

        String accessToken = (String) response.getBody().get("access_token");

        String userInfoUri = env.getProperty("facebook.user-info-uri");
        HttpHeaders userinfoHeaders = new HttpHeaders();
        userinfoHeaders.setBearerAuth(accessToken);

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userinfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUri, HttpMethod.GET, userInfoRequest, Map.class
        );

        if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Error getting user information from Facebook");
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

        return jwtUtil.generateToken(user.getEmail());
    }
}
