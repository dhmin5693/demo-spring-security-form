package com.example.demospringsecurityform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 설정이 필요한 메소드를 Override하는 식으로 구현
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/", "info", "/account/**").permitAll()
            .mvcMatchers("admin").hasRole("ADMIN")
            .anyRequest().authenticated();

        // security에서 제공하는 기본 form login 기능
        http.formLogin();
        http.httpBasic();
    }
}
