package org.example.desafiosalles.service;

import org.example.desafiosalles.domain.Conta;
import org.example.desafiosalles.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Conta cadastrarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    public Optional<Conta> obterContaPorId(Long id) {
        return contaRepository.findById(id);
    }

    public Conta atualizarConta(Long id, Conta contaAtualizada) {
        return contaRepository.findById(id)
                .map(conta -> {
                    conta.setDataPagamento(contaAtualizada.getDataPagamento());
                    conta.setDataVencimento(contaAtualizada.getDataVencimento());
                    conta.setDescricao(contaAtualizada.getDescricao());
                    conta.setValor(contaAtualizada.getValor());
                    conta.setSituacao(contaAtualizada.getSituacao());
                    return contaRepository.save(conta);
                })
                .orElseThrow(() -> new RuntimeException("Conta não encontradaa"));
    }

    public Page<Conta> listarContasApagar(LocalDate dtVencimento, String descricao, Pageable pageable) {
        return contaRepository.findContaByDataVencimentoOrDescricaoContainingIgnoreCase(
                dtVencimento, descricao, pageable);
    }

    public void alterarSituacaoConta(Long id, Conta.Situacao situacao) {
        contaRepository.findById(id).ifPresent(conta -> {
            conta.setSituacao(situacao);
            contaRepository.save(conta);
        });
    }

    public BigDecimal obterValorTotalPagoPorPeriodo(LocalDate startDate, LocalDate endDate) {
        return contaRepository.obterValorTotalPagoPorPeriodo(startDate, endDate);
    }
}
