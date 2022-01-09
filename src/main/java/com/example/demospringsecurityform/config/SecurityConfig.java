package com.example.demospringsecurityform.config;

import com.example.demospringsecurityform.account.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;

    // 설정이 필요한 메소드를 Override하는 식으로 구현
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/", "info", "/account/**", "/signup").permitAll()
            .mvcMatchers("/admin").hasRole("ADMIN")
            .mvcMatchers("/user").hasRole("USER")
            // 인증을 무시하기보단 모든 권한을 주는 정도의 차이
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .anyRequest().authenticated()
            .expressionHandler(securityExpressionHandler());
//            .accessDecisionManager(accessDecisionManager());

        // security에서 제공하는 기본 form login 기능
        http.formLogin()
            .loginPage("/login") // 지정하게 되면 default loginGeneratingFilter 사용 불가
            .permitAll();

        // basic 인증 지원. Authorization 헤더에 base64로 암호화한 username:password 추가
        http.httpBasic();

        // off csrf
//        http.csrf().disable();

        http.logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .deleteCookies(); // 쿠키 기반의 로그인을 사용한다면 검토해볼만한 옵션

        http.sessionManagement()
            .sessionFixation() // 세션 고정
            .changeSessionId() // 세션 ID 변경
            .invalidSessionUrl("/")
            .maximumSessions(1) // 동시에 1개의 세션만 허용
            .maxSessionsPreventsLogin(true); // 세션 최대 시 기존 로그인은 세션 만료

        // 현재 스레드에서 생성한 하위 스레드에도 SecurityContext를 공유
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        http.exceptionHandling()
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                                                                           .getAuthentication()
                                                                           .getPrincipal();
                String username = principal.getUsername();
                System.out.println(username + " is denied to access " + request.getRequestURI());
                response.sendRedirect("/access-denied");
            });

        http.rememberMe()
            .alwaysRemember(false)
            .rememberMeParameter("remember-me")
            .tokenValiditySeconds(600)
            .userDetailsService(accountService)
            .key("remember-me-sample");
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        // static resource는 인증 미적용, 필터 자체를 미적용
//        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

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
