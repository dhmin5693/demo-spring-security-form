package com.example.demospringsecurityform.form;

import com.example.demospringsecurityform.account.AccountService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
class SampleServiceTest {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @DisplayName("dashboard 권한 테스트")
    @ValueSource(strings = { "USER", "ADMIN" })
    @ParameterizedTest
    void dashboardTest(String role) {
        String name = UUID.randomUUID().toString().substring(0, 6);
        String password = "1234";

        accountService.save(name, password, role);
        var userDetails = accountService.loadUserByUsername(name);

        var token = new UsernamePasswordAuthenticationToken(userDetails, password);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(token));

        sampleService.dashboard();
    }

    @DisplayName("dashboard 권한 테스트 2")
    @WithMockUser
    @Test
    void dashboardTest2() {
        sampleService.dashboard();
    }
}
