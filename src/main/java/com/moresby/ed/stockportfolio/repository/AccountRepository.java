package com.moresby.ed.stockportfolio.repository;

import com.moresby.ed.stockportfolio.domain.Account;
import org.springframework.data.repository.CrudRepository;


public interface AccountRepository extends CrudRepository<Account, Long> {

}
