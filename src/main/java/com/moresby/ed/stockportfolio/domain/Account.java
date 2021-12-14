package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(
            name = "balance",
            columnDefinition="Decimal(16,2) default '0.00'"
    )
    private BigDecimal balance;

    @OneToOne(mappedBy = "account")
    private User user;

    @Builder
    public Account(BigDecimal balance, User user) {
        this.balance = balance;
        this.user = user;
    }
}
