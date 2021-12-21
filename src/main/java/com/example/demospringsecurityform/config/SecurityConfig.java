package com.example.demospringsecurityform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 설정이 필요한 메소드를 Override하는 식으로 구현
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                    .mvcMatchers("/", "/info").permitAll()
                    .mvcMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().authenticated();

        // security에서 제공하는 기본 form login 기능
        httpSecurity.formLogin();
        httpSecurity.httpBasic();
    }

    // user 정보 설정
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // noop: 암호화하지 않음, noop 대신 password incoder 방식 선택 가능
        auth.inMemoryAuthentication()
            .withUser("min").password("{noop}1234").roles("USER")
            .and()
            .withUser("admin").password("{noop}1234").roles("ADMIN");
    }
}
