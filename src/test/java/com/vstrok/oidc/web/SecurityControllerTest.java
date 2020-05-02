package com.vstrok.oidc.web;

import com.vstrok.oidc.model.BearerTokenResponse;
import com.vstrok.oidc.service.OAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static com.vstrok.oidc.utils.OidcConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OAuthService oAuthService;

    @Test
    public void oAuthPathShouldRedirectToOAuthUrl() throws Exception {

        doReturn("http://test.com/").when(oAuthService).prepareAuthCodeUrl(anyString());

        mockMvc.perform(get("/security/oauth"))
                .andExpect(status().is(302))
                .andExpect(header().string("Location", "http://test.com/"));
    }

    @Test
    public void oAuthCallbackShouldPopulateSessionWithTokens() throws Exception {

        BearerTokenResponse bearerTokenResponse = new BearerTokenResponse()
                .setAccessToken("access_token_value")
                .setRefreshToken("refresh_token_value");

        String authCode = "auth_code_value";

        doNothing().when(oAuthService).validateState(any(), any());
        doReturn(bearerTokenResponse).when(oAuthService).retrieveTokens(authCode);

        HttpSession session = mockMvc.perform(get("/security/oauth/callback")
                .param("code", authCode)
                .param("state", "state_value"))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession();

        assertTokensInSession(session);
    }

    private void assertTokensInSession(HttpSession session) {
        assertEquals("access_token_value", session.getAttribute(ACCESS_TOKEN),
                "Access token should be persisted in session");
        assertEquals("refresh_token_value", session.getAttribute(REFRESH_TOKEN),
                "Refresh token should be persisted in session");
        assertEquals("auth_code_value", session.getAttribute(AUTH_CODE),
                "Auth code should be persisted in session");
    }

}
