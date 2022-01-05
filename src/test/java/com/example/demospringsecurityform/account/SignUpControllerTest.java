package com.example.demospringsecurityform.account;

import com.example.demospringsecurityform.IntegrationTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SignUpControllerTest extends IntegrationTestConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/signup"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("_csrf")));
    }

    @Test
    void signup() throws Exception {
        mockMvc.perform(post("/signup").param("username", "min")
                                       .param("password", "123")
                                       .with(csrf()))
               .andDo((print()))
               .andExpect(status().is3xxRedirection());
    }
}
