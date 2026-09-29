package com.pucrs.calculadora.repository;

import com.pucrs.calculadora.model.HistoricoCalculo;
import com.pucrs.calculadora.model.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoCalculoRepository extends JpaRepository<HistoricoCalculo, Long> {

    List<HistoricoCalculo> findByOperacao(Operacao operacao);

    List<HistoricoCalculo> findTop10ByOrderByDataHoraDesc();
}
