package com.moresby.ed.stockportfolio.user;

import com.moresby.ed.stockportfolio.user.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByEmailEquals(String email);
}