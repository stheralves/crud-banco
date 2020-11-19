package com.banco.crudbanco.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CONTA")
public @Data class Conta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long numeroConta;
	
	@Column(name = "TITULAR")
	private String titular;

}
