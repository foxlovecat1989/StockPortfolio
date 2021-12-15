package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "User")
@Table(name = "app_user")
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(value = {"password", "isEnabled", "isAccountNonLocked"})
public class User implements UserDetails {
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
    private String user_number;
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

    private String userRole;

    @Transient
    private String[] authorities;

    private Boolean isAccountNonLocked = false;
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
                String userRole,
                String[] authorities,
                Boolean isAccountNonLocked,
                Boolean isEnabled) {
        this.user_number = user_number;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.joinDate = joinDate;
        this.account = account;
        this.userRole = userRole;
        this.authorities = authorities;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isEnabled = isEnabled;
    }

    public void addConfirmEmailToken(ConfirmEmailToken confirmEmailToken){
        this.confirmEmailTokens =
                this.getConfirmEmailTokens() != null ? this.getConfirmEmailTokens() : new ArrayList<>();
        this.confirmEmailTokens.add(confirmEmailToken);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.asList(authorities).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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
