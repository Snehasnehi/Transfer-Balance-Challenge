package com.db.awmd.challenge.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransferEntity;
import com.db.awmd.challenge.domain.FundTransferResponse;
import com.db.awmd.challenge.exception.RecordNotFoundException;
import com.db.awmd.challenge.exception.SystemException;
import com.db.awmd.challenge.exception.ZeroBalanceException;
import com.db.awmd.challenge.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

	@Autowired
	private AccountsService accountsService;
	
	@Value("${endpoint.getbalance}")		
	private String getAccountBalanceUrl;
	
	
	@PostMapping("v1/createcccount")
	public ResponseEntity<Account> createAccount(@RequestBody Account account) {
		log.info("In createAccount method....");
		Account updated = accountsService.createAccount(account);
		return new ResponseEntity<Account>(updated, new HttpHeaders(), HttpStatus.OK);

	}

	@GetMapping("v1/getaccount/{id}")
	public ResponseEntity<Account> getAccountBalance(@PathVariable("id") Long id) throws RecordNotFoundException {
		log.info("In getAccountBalance method....");
		Account acc = accountsService.getAccount(id);
		return new ResponseEntity<Account>(acc, new HttpHeaders(), HttpStatus.OK);

	}
	// Method to transfer the money from one account to another account 
	@PostMapping("v1/transfermoney")
	public ResponseEntity<FundTransferEntity> transferAmount(@RequestBody @Valid FundTransferEntity fundTransferEntity)
			throws RecordNotFoundException {
		log.info("In getAccount method....");
		
		if (fundTransferEntity.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ZeroBalanceException("Account balance is not suffiecient", "Zero Balance");
		} else {
			accountsService.transferMoney(fundTransferEntity);
			FundTransferResponse fundTransferResponse = new FundTransferResponse();
			fundTransferResponse.setAccountFromId(fundTransferEntity.getAccountFromId());
			try {
				fundTransferResponse.setBalanceAfterTransfer(accountsService.checkBalance(fundTransferEntity.getAccountFromId()));
			} catch (SystemException e) {
				e.printStackTrace();
			}

		}
		return new ResponseEntity<FundTransferEntity>(new HttpHeaders(), HttpStatus.OK);

	}

}
