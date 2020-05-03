package com.vstrok.oidc.web;

import com.vstrok.oidc.model.User;
import com.vstrok.oidc.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static com.vstrok.oidc.utils.OidcConstants.ACCESS_TOKEN;
import static com.vstrok.oidc.utils.OidcConstants.USER;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private OAuthService oAuthService;

    @Value("${cors.host}")
    private String corsHost;

    @GetMapping("/")
    public User getUserInfo(HttpSession session) {
        Object userObject = session.getAttribute(USER);
        if (userObject == null) {
            throw new RuntimeException("User is not authenticated");
        }
        return  (User) userObject;
    }

    @GetMapping("/openid")
    public void retrieveUserFromOpenIdProvider(HttpServletResponse response, HttpSession session) throws IOException {
        String accessToken = (String) session.getAttribute(ACCESS_TOKEN);
        User user = oAuthService.retrieveUser(accessToken);
        session.setAttribute(USER, user);
        response.sendRedirect(corsHost);
    }


}
