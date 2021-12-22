package com.example.demospringsecurityform.account;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // 단순 회원 생성을 위한 핸들러
    @GetMapping("/account/{role}/{username}/{password}")
    public Account createAccount(@PathVariable String role,
                                 @PathVariable String username,
                                 @PathVariable String password) {
        return accountRepository.save(Account.builder()
                                             .username(username)
                                             .password(password)
                                             .role(role)
                                             .build());
    }
}
