package com.example.demospringsecurityform.account;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var maybeAccount = accountRepository.findByUsername(username);
        if (maybeAccount.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        var account = maybeAccount.get();
        return User.builder()
                   .username(account.getUsername())
                   .password(account.getPassword())
                   .roles(account.getRole())
                   .build();
    }

    public Account save(String username, String password, String role) {
        var account = Account.builder()
                             .username(username)
                             .password(password)
                             .role(role)
                             .build();

        return accountRepository.save(account.encoded());
    }
}
