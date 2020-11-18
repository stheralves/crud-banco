package com.banco.crudbanco.model;

import java.math.BigDecimal;

import lombok.Data;

public @Data class ContaDTO {
	private Long numeroConta;
	private String titular;
	private BigDecimal valor;

}
