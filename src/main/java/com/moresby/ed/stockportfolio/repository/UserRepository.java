package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findUserByUsername(String username);

    @Query(value = "SELECT u FROM User u WHERE u.userNumber = ?1")
    Optional<User> findUserByUserNumber(String userNumber);
}
