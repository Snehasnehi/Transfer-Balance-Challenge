package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FundTransferResponse {
	private Long accountFromId;

	private BigDecimal balanceAfterTransfer;
}
