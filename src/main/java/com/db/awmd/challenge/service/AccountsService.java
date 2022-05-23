package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.db.awmd.challenge.constant.ErrorCode;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransferEntity;
import com.db.awmd.challenge.exception.AccountNotExistException;
import com.db.awmd.challenge.exception.OverDraftException;
import com.db.awmd.challenge.exception.SystemException;
import com.db.awmd.challenge.repository.AccountTransferRepository;
import com.db.awmd.challenge.repository.AccountsRepository;

@Service
public class AccountsService {

	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private AccountTransferRepository accountTransferRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${endpoint.getbalance}")
	private String getAccountBalanceUrl;

	public Account createAccount(Account account) {
		return accountsRepository.save(account);
	}

	public Account getAccount(Long accountId) {
		return accountsRepository.findByAccountId(accountId);
	}
	
	//service to transfer fund from one account to another
	public FundTransferEntity transferMoney(FundTransferEntity fundTransferEntity) {
		Account accountFrom = accountTransferRepository.getAccountForUpdate(fundTransferEntity.getAccountFromId())
				.orElseThrow(() -> new AccountNotExistException(
						"Account with id:" + fundTransferEntity.getAccountFromId() + " does not exist.",
						ErrorCode.ACCOUNT_ERROR));

		Account accountTo = accountTransferRepository.getAccountForUpdate(fundTransferEntity.getAccountFromId())
				.orElseThrow(() -> new AccountNotExistException(
						"Account with id:" + fundTransferEntity.getAccountFromId() + " does not exist.",
						ErrorCode.ACCOUNT_ERROR));

		if (accountFrom.getBalance().compareTo(accountTo.getBalance()) < 0) {
			throw new OverDraftException("Account with id:" + accountFrom.getAccountId() + " no balance to transfer.",
					ErrorCode.ACCOUNT_ERROR);
		}

		accountFrom.setBalance(accountFrom.getBalance().subtract(fundTransferEntity.getAmount()));
		accountTo.setBalance(accountTo.getBalance().add(fundTransferEntity.getAmount()));
		return fundTransferEntity;
	}

	// Method to set the current balance after transfer calling the get account method using rest template
	public BigDecimal checkBalance(@NotNull Long accountFromId) throws SystemException {
		try {
			String url = getAccountBalanceUrl.replace("{id}", accountFromId.toString());

			ResponseEntity<Account> balanceCheckResult = restTemplate.getForEntity(url, Account.class);

			if (balanceCheckResult.getStatusCode().is2xxSuccessful()) {
				if (balanceCheckResult.hasBody()) {
					return balanceCheckResult.getBody().getBalance();
				}
			}
		} catch (ResourceAccessException ex) {
			final String errorMessage = "Encounter timeout error, please check with system administrator.";

		}
		// for any other fail cases
		throw new SystemException("Encounter internal server error, please check with system administrator.",
				ErrorCode.SYSTEM_ERROR);
	}

}
