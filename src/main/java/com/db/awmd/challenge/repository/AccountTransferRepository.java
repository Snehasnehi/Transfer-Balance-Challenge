package com.db.awmd.challenge.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.db.awmd.challenge.domain.Account;

@Transactional(readOnly = true)
public interface AccountTransferRepository extends JpaRepository<Account, Long> {
	

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Transactional
	@Query("SELECT a FROM Account a WHERE a.accountId = ?1")
	Optional<Account> getAccountForUpdate(Long id);

}
