package com.moresby.ed.stockportfolio.user.registration.token;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfirmEmailTokenRepository extends CrudRepository<ConfirmEmailToken, Long> {
    @Query(value = "SELECT c FROM ConfirmEmailToken c WHERE c.token = ?1")
    Optional<ConfirmEmailToken> findByToken(String token);


}
