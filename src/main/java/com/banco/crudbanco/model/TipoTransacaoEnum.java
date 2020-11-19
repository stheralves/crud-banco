package com.banco.crudbanco.model;

public enum TipoTransacaoEnum {

	CREDITO(1), DEBITO(2);

	private int rotulo;

	TipoTransacaoEnum(int rotulo) {
		this.rotulo = rotulo;
	}

	public int getRotulo() {
		return rotulo;
	}
}
