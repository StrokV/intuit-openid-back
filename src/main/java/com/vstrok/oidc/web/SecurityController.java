package com.vstrok.oidc.web;

import com.vstrok.oidc.model.BearerTokenResponse;
import com.vstrok.oidc.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.vstrok.oidc.utils.OidcConstants.*;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private OAuthService oAuthService;

    @RequestMapping("/oauth")
    public void authorise(HttpServletResponse response, HttpSession session) throws IOException {
        String csrfToken = UUID.randomUUID().toString();;
        session.setAttribute(CSRF, csrfToken);
        response.sendRedirect(oAuthService.prepareAuthCodeUrl(csrfToken));
    }

    @RequestMapping("/oauth/callback")
    public void authCallback(@RequestParam("code") String authCode,
                             @RequestParam("state") String state,
                             HttpSession session, HttpServletResponse response) throws IOException {
        String csrfToken = String.valueOf(session.getAttribute(CSRF));
        oAuthService.validateState(state, csrfToken);
        BearerTokenResponse bearerTokenResponse = oAuthService.retrieveTokens(authCode);
        persistToSession(session, authCode, bearerTokenResponse);
        response.sendRedirect("/user/openid");
    }

    private void persistToSession(HttpSession session, String authCode, BearerTokenResponse bearerTokenResponse) {
        session.setAttribute(AUTH_CODE, authCode);
        session.setAttribute(ACCESS_TOKEN, bearerTokenResponse.getAccessToken());
        session.setAttribute(REFRESH_TOKEN, bearerTokenResponse.getRefreshToken());
    }



}
