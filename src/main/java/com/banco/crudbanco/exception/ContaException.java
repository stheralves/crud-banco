package com.banco.crudbanco.exception;

public class ContaException extends RuntimeException {

	private static final long serialVersionUID = 7974215619941649074L;
	
	private String mensagem;

	public ContaException(String mensagem) {
		this.mensagem = mensagem;
	}

	public ContaException() {
		super();
	}

	public String getMensagem() {
		return this.mensagem;
	}

}
