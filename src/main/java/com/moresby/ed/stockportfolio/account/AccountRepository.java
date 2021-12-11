package com.moresby.ed.stockportfolio.account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query(value = "SELECT a FROM Account a WHERE a.user.id = ?1")
    Optional<Account> findOneByUserId(Long userId);
}
