package com.banco.crudbanco.controller;

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
import com.banco.crudbanco.model.ContaDTO;
import com.banco.crudbanco.repository.IContaRepository;

@RestController
@RequestMapping("/api")
public class ContaController {

	@Autowired
	private IContaRepository contaRepository;

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
		if(Objects.isNull(conta.getSaldo()) || Objects.isNull(conta.getTitular())) {
			throw new ContaException("Os campos titular e saldo são obrigatórios para abertura de conta.");
		}

	    conta = contaRepository.save(conta);
		return ResponseEntity.ok().body(conta);
	}
	
	@PutMapping("/alterar-conta")
	public ResponseEntity<Conta> alterarConta(@RequestBody Conta conta) throws ContaException {
		if(Objects.isNull(conta.getNumeroConta())) {
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
		
		return ResponseEntity.ok().body(!contaRepository.existsById(numConta) ? "Conta cancelada com sucesso!" : "Erro ao cancelar conta");
	}
	
	
	@PutMapping("/sacar")
	public ResponseEntity<Conta> sacar(@RequestBody ContaDTO dto) throws ContaException {
		if(Objects.isNull(dto.getNumeroConta())) {
			throw new ContaException("O número da conta é obrigatório.");
		}
		Conta contaDB = contaRepository.findById(dto.getNumeroConta())
				.orElseThrow(() -> new ContaException("Número de Conta inexistente."));
		
		contaDB.setSaldo(contaDB.getSaldo().subtract(dto.getValor()));
		contaDB = contaRepository.save(contaDB);
		
		return ResponseEntity.ok().body(contaDB);
	}
	
	@PutMapping("/depositar")
	public ResponseEntity<Conta> depsitar(@RequestBody ContaDTO dto) throws ContaException {
		if(Objects.isNull(dto.getNumeroConta())) {
			throw new ContaException("O número da conta é obrigatório.");
		}
		Conta contaDB = contaRepository.findById(dto.getNumeroConta())
				.orElseThrow(() -> new ContaException("Número de Conta inexistente."));
		
		contaDB.setSaldo(contaDB.getSaldo().add(dto.getValor()));
		contaDB = contaRepository.save(contaDB);
		
		return ResponseEntity.ok().body(contaDB);
	}

}
