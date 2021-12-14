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
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "User")
@Table(name = "app_user")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(value = {"password"})
public class User implements UserDetails, Serializable {

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
    @Column(
            nullable = false,
            updatable = false
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;


    @Column(name = "user_number")
    private String userNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

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

    @Column(name = "last_login-date_display")
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

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Transient
    private List<String> authorities;
    private Boolean isAccountNonLocked = false;
    private Boolean isEnabled = false;

    @Builder
    public User(
            String userNumber,
            String firstName,
            String lastName,
            String username,
            String email,
            String password,
            String profileImageUrl,
            Date lastLoginDate,
            Date lastLoginDateDisplay,
            Date joinDate,
            UserRole userRole,
            List<String> authorities,
            Boolean isAccountNonLocked,
            Boolean isEnabled,
            Account account
    ) {
        this.userNumber = userNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.joinDate = joinDate;
        this.userRole = userRole;
        this.authorities = authorities;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isEnabled = isEnabled;
        this.account = account;
    }


    public void addConfirmEmailToken(ConfirmEmailToken confirmEmailToken){
        this.confirmEmailTokens =
                this.getConfirmEmailTokens() != null ? this.getConfirmEmailTokens() : new ArrayList<>();
        this.confirmEmailTokens.add(confirmEmailToken);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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
