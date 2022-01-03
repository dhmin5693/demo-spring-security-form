package com.example.demospringsecurityform.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 설정이 필요한 메소드를 Override하는 식으로 구현
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/", "info", "/account/**").permitAll()
            .mvcMatchers("/admin").hasRole("ADMIN")
            .mvcMatchers("/user").hasRole("USER")
            .anyRequest().authenticated()
            .expressionHandler(securityExpressionHandler());
//            .accessDecisionManager(accessDecisionManager());

        // security에서 제공하는 기본 form login 기능
        http.formLogin();
        http.httpBasic();
    }

    public SecurityExpressionHandler<FilterInvocation> securityExpressionHandler() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(hierarchy);

        return handler;
    }

    public AccessDecisionManager accessDecisionManager() {
        WebExpressionVoter voter = new WebExpressionVoter();
        voter.setExpressionHandler(securityExpressionHandler());

        return new AffirmativeBased(List.of(voter));
    }
}
