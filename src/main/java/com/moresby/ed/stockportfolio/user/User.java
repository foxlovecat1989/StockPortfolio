package com.moresby.ed.stockportfolio.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moresby.ed.stockportfolio.account.Account;
import com.moresby.ed.stockportfolio.inventory.Inventory;
import com.moresby.ed.stockportfolio.trade.model.entity.Trade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity(name = "User")
@Table(name = "app_user")
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"password"})
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @Column(
            name = "username",
            nullable = false,
            columnDefinition = "TEXT",
            length = 20
    )
    private String username;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT",
            updatable = false,
            unique = true
    )
    private String email;

    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Inventory> inventories;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Trade> trades;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_account_id")
    )
    private Account account;

    @Builder
    public User(String username, String email, String password, Account account) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.account = account;
    }
}
