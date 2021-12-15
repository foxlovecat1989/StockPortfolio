package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query(value = "SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmailEquals(String email);
}
