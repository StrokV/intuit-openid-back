package com.vstrok.oidc.service;

import com.vstrok.oidc.model.BearerTokenResponse;
import com.vstrok.oidc.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import static java.util.Collections.singletonList;

@Service
public class OAuthService {

    @Value("${oauth.client-id}")
    private String clientId;

    @Value("${oauth.client-secret}")
    private String clientSecret;

    @Value("${oauth.uri.server}")
    private String serverURL;

    @Value("${oauth.uri.token}")
    private String tokenURL;

    @Value("${oauth.uri.userinfo}")
    private String userURL;

    @Value("${oauth.scope}")
    private String scope;

    @Value("${oauth.uri.redirect}")
    private String redirectUri;

    private RestTemplate restTemplate = new RestTemplate();

    public String prepareAuthCodeUrl(String csrfToken) throws UnsupportedEncodingException {
        return serverURL
                + "?client_id=" + clientId
                + "&response_type=code"
                + "&scope=" + URLEncoder.encode(scope, "UTF-8")
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
                + "&state=" + csrfToken;
    }

    public void validateState(String state, String csrfToken) {
        if (!StringUtils.equals(csrfToken, state)) {
            throw new RuntimeException("Incorrect state. Security broken");
        }
    }

    public BearerTokenResponse retrieveTokens(String authCode) throws UnsupportedEncodingException {

        String requestBody = "grant_type=authorization_code"
                + "&code=" + authCode
                + "&redirect_uri=" + redirectUri;

        String clientCredentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(encodedCredentials);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(tokenURL, entity, BearerTokenResponse.class);
    }

    public User retrieveUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity(null, headers);
        ResponseEntity<User> userResponseEntity = restTemplate.exchange(userURL, HttpMethod.GET, entity, User.class);
        if (userResponseEntity.getStatusCode() == HttpStatus.OK) {
            return userResponseEntity.getBody();
        }
        return null;
    }
}
