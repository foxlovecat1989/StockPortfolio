package com.moresby.ed.stockportfolio.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

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
}
