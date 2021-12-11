package com.moresby.ed.stockportfolio.account;

import com.moresby.ed.stockportfolio.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "Account")
@Table(name = "account")
@RequiredArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToOne(mappedBy = "account")
    private User user;
}
