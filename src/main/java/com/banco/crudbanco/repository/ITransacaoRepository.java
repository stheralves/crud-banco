package com.banco.crudbanco.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.banco.crudbanco.model.Conta;
import com.banco.crudbanco.model.Transacao;

@Repository
public interface ITransacaoRepository extends CrudRepository<Transacao, Long>{
	
	List<Transacao> findByConta(Conta numeroConta);

}
