package com.example.demospringsecurityform.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}
