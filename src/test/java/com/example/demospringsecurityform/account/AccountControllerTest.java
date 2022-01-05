package com.example.demospringsecurityform.account;

import com.example.demospringsecurityform.IntegrationTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends IntegrationTestConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Test
    void index_anonymous() throws Exception {
        mockMvc.perform(get("/").with(anonymous()))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void index_anonymous2() throws Exception {
        mockMvc.perform(get("/"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void index_user() throws Exception {
        // user "min"이 로그인을 한 것으로 가정
       mockMvc.perform(get("/")
                           .with(user("min").roles("USER")))
              .andDo(print())
              .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "min", roles = "USER")
    void index_user2() throws Exception {
        // user "min"이 로그인을 한 것으로 가정
        mockMvc.perform(get("/"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void index_admin() throws Exception {
        // admin이 로그인을 한 것으로 가정
        mockMvc.perform(get("/admin")
                            .with(user("admin").roles("ADMIN")))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void index_admin2() throws Exception {
        // admin이 로그인을 한 것으로 가정
        mockMvc.perform(get("/admin"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void index_admin_with_user() throws Exception {
        mockMvc.perform(get("/admin")
                            .with(user("min").roles("USER")))
               .andDo(print())
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "min", roles = "USER")
    void index_admin_with_user2() throws Exception {
        mockMvc.perform(get("/admin"))
               .andDo(print())
               .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void login_fail() throws Exception {
        accountService.save("min", "1234", "USER");

        mockMvc.perform(formLogin()
                            .user("min")
                            .password("5678"))
               .andExpect(unauthenticated());
    }

    @Test
    @Transactional
    void login() throws Exception {
        accountService.save("min", "1234", "USER");

        mockMvc.perform(formLogin()
                            .user("min")
                            .password("1234"))
               .andExpect(authenticated());
    }
}
