package com.banco.crudbanco.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Entity
@Table(name = "TRANSACAO")
public @Data class Transacao implements Serializable{
	
	private static final long serialVersionUID = 1743254063506391460L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@JsonIgnore
    private long id;
	
	@ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="numeroConta", nullable=false)
	private Conta conta;
	
	@Column(name = "VALOR")
	private BigDecimal valor;
	
	@Column(name = "TP_TRANSACAO")
	private int tipoTransacao;
	

}
