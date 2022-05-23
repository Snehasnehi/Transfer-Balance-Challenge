package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferEntity {

	@NotNull
	@ApiModelProperty(required = true)
	private Long accountFromId;

	@NotNull
	@ApiModelProperty(required = true)
	private String accountToId;

	@NotNull
	@Min(value = 0, message = "Transaction amount can not be less than zero")
	private BigDecimal amount;

}
