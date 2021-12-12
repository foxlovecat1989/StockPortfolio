package com.moresby.ed.stockportfolio.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moresby.ed.stockportfolio.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "Account")
@Table(name = "account")
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = "user")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToOne(mappedBy = "account")
    private User user;

    @Builder
    public Account(BigDecimal balance, User user) {
        this.balance = balance;
        this.user = user;
    }
}
