package com.vstrok.oidc.web;

import com.vstrok.oidc.model.User;
import com.vstrok.oidc.service.OAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static com.vstrok.oidc.utils.OidcConstants.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OAuthService oAuthService;

    @Test
    public void openidCallbackShouldAddUserToSession() throws Exception {

        User user = new User();
        doReturn(user).when(oAuthService).retrieveUser(any());

        HttpSession session = mockMvc.perform(get("/user/openid"))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(user, session.getAttribute(USER), "User should be persisted in session");
    }

    @Test
    public void userInfoShouldReturnUser() throws Exception {

        User user = new User()
                .setGivenName("TestName")
                .setFamilyName("TestFamilyName")
                .setEmail("TestEmail");

        String content = mockMvc.perform(get("/user/").sessionAttr(USER, user))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(content.contains("TestName"), "Response should contain user name");
        assertTrue(content.contains("TestFamilyName"), "Response should contain user family name");
        assertTrue(content.contains("TestEmail"), "Response should contain user email");
    }

}
