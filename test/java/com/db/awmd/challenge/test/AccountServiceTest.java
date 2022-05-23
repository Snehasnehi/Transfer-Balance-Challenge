package com.db.awmd.challenge.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransferEntity;
import com.db.awmd.challenge.repository.AccountTransferRepository;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@Mock
	AccountsRepository accountsRepository;
	@Mock
	private AccountTransferRepository accountTransferRepository;

	@InjectMocks
	AccountsService accService;
	@Mock
	Account account;

	@Test
	public void testCreateAccount() {
		when(accountsRepository.save(new Account(3L, new BigDecimal(1000)))).thenReturn(account);
		assertEquals(new Account(3L, new BigDecimal(1000)), new Account(3L, new BigDecimal(1000)));
	}

	@Test
	public void testGetAccount() {
		when(accountsRepository.findByAccountId(1L)).thenReturn(new Account(1L, new BigDecimal(100)));
		assertEquals(BigDecimal.ONE, accService.getAccount(1L));

	}

	@Test
	public void testTransferMoney() {
		Long accountFromId = 1L;
		Long accountFromTo = 2L;
		BigDecimal amount = new BigDecimal(10);
		FundTransferEntity fundTransferEntity = new FundTransferEntity();
		fundTransferEntity.setAccountFromId(accountFromId);
		fundTransferEntity.setAccountToId(accountFromTo);
		fundTransferEntity.setAmount(amount);
		Account accFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accTo = new Account(accountFromId, BigDecimal.TEN);
		when(accountTransferRepository.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accountTransferRepository.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));
		accService.transferMoney(fundTransferEntity);
		assertEquals(BigDecimal.ZERO, accFrom.getBalance());
		assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), accTo.getBalance());
	}
}
