package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query(value = "SELECT a FROM Account a WHERE a.user.id = ?1")
    Optional<Account> findOneByUserId(Long userId);
}
