package com.vstrok.oidc.service;

import com.vstrok.oidc.model.BearerTokenResponse;
import com.vstrok.oidc.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
public class OAuthServiceTest {

    @Autowired
    OAuthService oAuthService;

    @Test
    public void shouldBuildCorrectAuthCodeURL() throws UnsupportedEncodingException {
        String expectedUrl = "oauth2Server?client_id=testClientId&response_type=code" +
                "&scope=openid+profile+email+phone+address" +
                "&redirect_uri=http%3A%2F%2Ftest.com%2Fcallback&state=csrfTest";
        String url = oAuthService.prepareAuthCodeUrl("csrfTest");
        assertEquals(expectedUrl, url, "URL is incorrect");
    }

    @Test
    public void validateStateShouldThrowException() {
        assertThrows(RuntimeException.class, () -> oAuthService.validateState("state", "csrf"));
    }

    @Test
    public void validateStateShouldNotThrowException() {
        oAuthService.validateState("csrf", "csrf");
    }

    @Test
    public void retrieveTokensShouldReturnBearerResponse() {
        BearerTokenResponse bearerTokenResponse = new BearerTokenResponse();
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        oAuthService.setRestTemplate(restTemplate);
        doReturn(bearerTokenResponse).when(restTemplate).postForObject(anyString(), any(), any());

        BearerTokenResponse bearerTokenResponseActual = oAuthService.retrieveTokens("testCode");

        assertEquals(bearerTokenResponse, bearerTokenResponseActual, "Token response is incorrect");
    }

    @Test
    public void retrieveUserShouldReturnUser() {
        User user = new User();
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        oAuthService.setRestTemplate(restTemplate);
        doReturn(responseEntity).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));
        doReturn(HttpStatus.OK).when(responseEntity).getStatusCode();
        doReturn(user).when(responseEntity).getBody();
        User userActual = oAuthService.retrieveUser("accessToken");

        assertEquals(user, userActual, "User is incorrect");
    }

    @Test
    public void retrieveUserShouldReturnNull() {
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        oAuthService.setRestTemplate(restTemplate);
        doReturn(responseEntity).when(restTemplate).exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));
        doReturn(HttpStatus.BAD_REQUEST).when(responseEntity).getStatusCode();
        User user = oAuthService.retrieveUser("accessToken");

        assertNull(user, "User should be null");
    }
}
