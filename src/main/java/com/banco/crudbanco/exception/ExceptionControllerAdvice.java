package com.banco.crudbanco.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@ConditionalOnWebApplication
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionControllerAdvice {

	private static final String MENSAGEM_ERRO = "Erro ao executar operação. ";

	@ExceptionHandler({ ContaException.class, RuntimeException.class })
	public ResponseEntity<Object> handleExcecaoNegocio(ContaException e, HttpServletRequest servletRequest) {
		List<String> mensagens = new ArrayList<>();
		mensagens.add(e.getMensagem());
		ErrorDetails error = new ErrorDetails(HttpStatus.BAD_REQUEST, MENSAGEM_ERRO, mensagens,
				servletRequest.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

}
