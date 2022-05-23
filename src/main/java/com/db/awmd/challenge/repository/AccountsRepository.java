package com.db.awmd.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {

	Account findByAccountId(Long accountId) throws DuplicateAccountIdException;

	void clearAccounts();

}
