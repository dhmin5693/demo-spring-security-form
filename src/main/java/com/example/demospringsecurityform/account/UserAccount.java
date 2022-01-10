package com.example.demospringsecurityform.account;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
@EqualsAndHashCode(callSuper = false)
public class UserAccount extends User {

    private static final long serialVersionUID = 1302331352028284514L;

    private final Account account;

    public UserAccount(Account account) {
        super(account.getUsername(), account.getPassword(),
              List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole())));
        this.account = account;
    }
}
