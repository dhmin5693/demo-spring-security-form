package com.example.demospringsecurityform.book;

import com.example.demospringsecurityform.account.Account;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ManyToOne
    private Account author;

    @Builder
    public Book(Long id, String title, Account author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}
