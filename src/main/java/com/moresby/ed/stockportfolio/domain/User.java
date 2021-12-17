package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moresby.ed.stockportfolio.enumeration.UserRole;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

@Entity(name = "User")
@Table(name = "app_user")
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(value = {"isEnabled", "isAccountNonLocked"})
public class User implements Serializable {
    @Id
    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_sequence"
    )
    @Column(updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @Column(name = "user_number")
    private String userNumber;
    @Column(
            name = "username",
            nullable = false,
            columnDefinition = "TEXT",
            length = 20,
            unique = true
    )
    @NotBlank
    private String username;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT",
            updatable = false,
            unique = true
    )
    @Email
    private String email;

    @Column(
            name = "password",
            nullable = false
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "last_login_date")
    private Date lastLoginDate;

    @Column(name = "last_login_date_display")
    private Date lastLoginDateDisplay;

    @Column(name = "join_date")
    private Date joinDate;

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

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true
    )
    private List<ConfirmEmailToken> confirmEmailTokens;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true
    )
    private List<Watchlist> watchlists;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean isAccountNonLocked = true;
    private Boolean isEnabled = false;

    @Builder
    public User(String user_number,
                String username,
                String email,
                String password,
                String profileImageUrl,
                Date lastLoginDate,
                Date lastLoginDateDisplay,
                Date joinDate,
                Account account,
                UserRole userRole,
                Boolean isAccountNonLocked,
                Boolean isEnabled) {
        this.userNumber = userNumber;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.joinDate = joinDate;
        this.account = account;
        this.userRole = userRole;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isEnabled = isEnabled;
    }

    public void addConfirmEmailToken(ConfirmEmailToken confirmEmailToken){
        this.confirmEmailTokens =
                this.getConfirmEmailTokens() != null ? this.getConfirmEmailTokens() : new ArrayList<>();
        this.confirmEmailTokens.add(confirmEmailToken);
    }

}
