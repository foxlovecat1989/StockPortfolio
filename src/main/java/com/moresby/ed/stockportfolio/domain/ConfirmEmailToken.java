package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "ConfirmEmailToken")
@Table(name = "confirm_email_token")
@JsonIgnoreProperties(value = {"user"})
public class ConfirmEmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(name = "email")
    private String email;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    @Builder
    public ConfirmEmailToken(
            String token,
            String email,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime confirmedAt,
            User user) {
        this.token = token;
        this.email = email;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt = confirmedAt;
        this.user = user;
    }
}
