package com.banco.crudbanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banco.crudbanco.model.Conta;

@Repository
public interface IContaRepository extends JpaRepository<Conta, Long> {

}
