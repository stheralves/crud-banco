package com.banco.crudbanco.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.crudbanco.exception.ContaException;
import com.banco.crudbanco.model.Conta;
import com.banco.crudbanco.model.SaldoDTO;
import com.banco.crudbanco.model.TipoTransacaoEnum;
import com.banco.crudbanco.model.Transacao;
import com.banco.crudbanco.repository.IContaRepository;
import com.banco.crudbanco.repository.ITransacaoRepository;

@RestController
@RequestMapping("/api")
public class ContaController {

	@Autowired
	private IContaRepository contaRepository;

	@Autowired
	private ITransacaoRepository transacaoRepository;

	@GetMapping("{numConta}")
	public ResponseEntity<Conta> getConta(@PathVariable Long numConta) throws ContaException {
		if (null == numConta) {
			throw new ContaException("Número da conta é obrigatório");
		}

		Conta conta = contaRepository.findById(numConta)
				.orElseThrow(() -> new ContaException("Número de Conta inexistente."));
		return ResponseEntity.ok().body(conta);
	}

	@PostMapping("/criar-conta")
	public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) throws ContaException {
		if (Objects.isNull(conta.getTitular())) {
			throw new ContaException("O titular é obrigatório.");
		}

		conta = contaRepository.save(conta);
		return ResponseEntity.ok().body(conta);
	}

	@PutMapping("/alterar-conta")
	public ResponseEntity<Conta> alterarConta(@RequestBody Conta conta) throws ContaException {
		if (Objects.isNull(conta.getNumeroConta())) {
			throw new ContaException("O número da conta é obrigatório.");
		}

		if (Objects.isNull(conta.getTitular())) {
			throw new ContaException("O titular é obrigatório.");
		}
		Conta contaDB = contaRepository.findById(conta.getNumeroConta())
				.orElseThrow(() -> new ContaException("Número de Conta inexistente."));

		contaDB.setTitular(conta.getTitular());
		contaDB = contaRepository.save(contaDB);

		return ResponseEntity.ok().body(contaDB);
	}

	@DeleteMapping("/cancelar-conta/{numConta}")
	public ResponseEntity<String> cancelarConta(@PathVariable Long numConta) throws ContaException {
		if (null == numConta) {
			throw new ContaException("Número da conta é obrigatório");
		}

		contaRepository.findById(numConta).orElseThrow(() -> new ContaException("Número de Conta inexistente."));
		contaRepository.deleteById(numConta);

		return ResponseEntity.ok().body(
				!contaRepository.existsById(numConta) ? "Conta cancelada com sucesso!" : "Erro ao cancelar conta");
	}

	@PostMapping("/depositar")
	public ResponseEntity<String> depositar(@RequestBody Transacao transacao) throws ContaException {
		if (Objects.isNull(transacao.getConta()) || Objects.isNull(transacao.getConta().getNumeroConta())) {
			throw new ContaException("O número da conta é obrigatório.");
		}
		contaRepository.findById(transacao.getConta().getNumeroConta())
				.orElseThrow(() -> new ContaException("Número de Conta inexistente, impossível sacar!"));

		Transacao t = new Transacao();
		t.setConta(transacao.getConta());
		t.setTipoTransacao(TipoTransacaoEnum.CREDITO.getRotulo());
		t.setValor(transacao.getValor());
		transacaoRepository.save(t);

		return ResponseEntity.ok().body("Depósito Efetuado com sucesso!");
	}

	@PostMapping("/sacar")
	public ResponseEntity<String> sacar(@RequestBody Transacao transacao) throws ContaException {
		if (Objects.isNull(transacao.getConta()) || Objects.isNull(transacao.getConta().getNumeroConta())) {
			throw new ContaException("O número da conta é obrigatório.");
		}
		contaRepository.findById(transacao.getConta().getNumeroConta())
				.orElseThrow(() -> new ContaException("Número de Conta inexistente, impossível sacar!"));

		Transacao t = new Transacao();
		t.setConta(transacao.getConta());
		t.setTipoTransacao(TipoTransacaoEnum.DEBITO.getRotulo());
		t.setValor(transacao.getValor());
		transacaoRepository.save(t);

		return ResponseEntity.ok().body("Saque Efetuado com sucesso!");
	}

	@GetMapping("extrato/{numConta}")
	public ResponseEntity<List<Transacao>> getExtrato(@PathVariable Long numConta) throws ContaException {
		if (null == numConta) {
			throw new ContaException("Número da conta é obrigatório");
		}

		Conta conta = contaRepository.findById(numConta)
				.orElseThrow(() -> new ContaException("Número de Conta inexistente."));

		List<Transacao> t = transacaoRepository.findByConta(conta);

		return ResponseEntity.ok().body(t);
	}

	@GetMapping("saldo/{numConta}")
	public ResponseEntity<SaldoDTO> getSaldo(@PathVariable Long numConta) throws ContaException {
		if (null == numConta) {
			throw new ContaException("Número da conta é obrigatório");
		}

		Conta conta = contaRepository.findById(numConta)
				.orElseThrow(() -> new ContaException("Número de Conta inexistente."));

		List<Transacao> t = transacaoRepository.findByConta(conta);
		SaldoDTO saldo = new SaldoDTO();

		BigDecimal somaCredito = t.stream().filter(s -> TipoTransacaoEnum.CREDITO.getRotulo() == s.getTipoTransacao())
				.map(Transacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal somaDebito = t.stream().filter(s -> TipoTransacaoEnum.DEBITO.getRotulo() == s.getTipoTransacao())
				.map(Transacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);

		saldo.setSaldo(somaCredito.subtract(somaDebito));
		return ResponseEntity.ok().body(saldo);
	}

}
