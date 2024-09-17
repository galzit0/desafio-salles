package org.example.desafiosalles.service;

import org.example.desafiosalles.domain.Conta;
import org.example.desafiosalles.domain.Conta.Situacao;
import org.example.desafiosalles.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    private Conta conta;

    @BeforeEach
    void mock() {
        MockitoAnnotations.openMocks(this);
        conta = new Conta();
        conta.setId(1L);
        conta.setDescricao("Conta de Luz");
        conta.setDataVencimento(LocalDate.now().plusDays(10));
        conta.setDataPagamento(LocalDate.now());
        conta.setValor(new BigDecimal("150.00"));
        conta.setSituacao(Situacao.PENDENTE);
    }

    @Test
    void cadastrarConta() {
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta novaConta = contaService.cadastrarConta(conta);

        assertNotNull(novaConta);
        assertEquals("Conta de Luz", novaConta.getDescricao());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void obterContaPorId() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Optional<Conta> contaOptional = contaService.obterContaPorId(1L);

        assertTrue(contaOptional.isPresent());
        assertEquals("Conta de Luz", contaOptional.get().getDescricao());
        verify(contaRepository, times(1)).findById(1L);
    }

    @Test
    void atualizarConta() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta contaAtualizada = new Conta();
        contaAtualizada.setDataPagamento(LocalDate.now().plusDays(2));
        contaAtualizada.setDescricao("Conta de Água");
        contaAtualizada.setValor(new BigDecimal("200.00"));
        contaAtualizada.setSituacao(Situacao.PAGA);

        Conta resultado = contaService.atualizarConta(1L, contaAtualizada);

        assertEquals("Conta de Água", resultado.getDescricao());
        assertEquals(Situacao.PAGA, resultado.getSituacao());
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void listarContasAPagar() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> page = new PageImpl<>(Arrays.asList(conta));

        when(contaRepository.findContaByDataVencimentoOrDescricaoContainingIgnoreCase(any(), anyString(), eq(pageable))).thenReturn(page);

        Page<Conta> result = contaService.listarContasApagar(LocalDate.now(), "Conta", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(contaRepository, times(1)).findContaByDataVencimentoOrDescricaoContainingIgnoreCase(any(), anyString(), eq(pageable));
    }

    @Test
    void alterarSituacaaoDaConta() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        contaService.alterarSituacaoConta(1L, Situacao.PAGA);

        assertEquals(Situacao.PAGA, conta.getSituacao());
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void obterValorTotalPagoPorPerodo() {
        when(contaRepository.obterValorTotalPagoPorPeriodo(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new BigDecimal("500.00"));

        BigDecimal valorTotal = contaService.obterValorTotalPagoPorPeriodo(LocalDate.now().minusMonths(1), LocalDate.now());

        assertEquals(new BigDecimal("500.00"), valorTotal);
        verify(contaRepository, times(1)).obterValorTotalPagoPorPeriodo(any(LocalDate.class), any(LocalDate.class));
    }
}
