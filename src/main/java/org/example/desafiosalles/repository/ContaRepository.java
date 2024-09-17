package org.example.desafiosalles.repository;

import org.example.desafiosalles.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Page<Conta> findContaByDataVencimentoOrDescricaoContainingIgnoreCase(LocalDate dataVencimento, String descricao, Pageable pageable);

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento BETWEEN :dataVencimento AND :dataPagamento AND c.situacao = 'PAGA'")
    BigDecimal obterValorTotalPagoPorPeriodo(@Param("dataVencimento") LocalDate dataVencimento, @Param("dataPagamento") LocalDate dataPagamento);
}

