package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moresby.ed.stockportfolio.enumeration.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity(name = "User")
@Table(name = "app_user")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(value = {"password"})
public class User implements UserDetails {
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

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean isAccountNonLocked = false;
    private Boolean isEnabled = false;


    @Builder
    public User(
            String username,
            String email,
            String password,
            Account account,
            UserRole userRole,
            Boolean isAccountNonLocked,
            Boolean isEnabled,
            List<ConfirmEmailToken> confirmEmailTokens
            ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.account = account;
        this.userRole = userRole;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isEnabled = isEnabled;
        this.confirmEmailTokens = confirmEmailTokens;
    }

    public void addConfirmEmailToken(ConfirmEmailToken confirmEmailToken){
        this.confirmEmailTokens =
                this.getConfirmEmailTokens() != null ? this.getConfirmEmailTokens() : new ArrayList<>();
        this.confirmEmailTokens.add(confirmEmailToken);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());

        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
