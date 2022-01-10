package com.example.demospringsecurityform.common;

import com.example.demospringsecurityform.account.Account;
import com.example.demospringsecurityform.account.AccountService;
import com.example.demospringsecurityform.book.Book;
import com.example.demospringsecurityform.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultDataGenerator implements ApplicationRunner {

    private final AccountService accountService;
    private final BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createBook("주식투자 절대원칙", createUser("min"));
        createBook("돈의 법칙", createUser("kim"));
    }

    private void createBook(String title, Account account) {
        bookRepository.save(
            Book.builder()
                .title(title)
                .author(account)
                .build()
        );
    }

    private Account createUser(String username) {
        return accountService.save(username, "1234", "USER");
    }
}
