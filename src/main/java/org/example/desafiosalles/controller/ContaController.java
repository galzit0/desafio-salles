package org.example.desafiosalles.controller;

import org.example.desafiosalles.domain.Conta;
import org.example.desafiosalles.service.ContaService;
import org.example.desafiosalles.service.CsvImportacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;
    @Autowired
    private CsvImportacaoService csvImportacaoService;

    @PostMapping
    public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta conta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaService.cadastrarConta(conta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conta> atualizarConta(@PathVariable Long id, @RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.atualizarConta(id, conta));
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<Void> alterarSituacaoConta(@PathVariable Long id, @RequestBody Conta.Situacao situacao) {
        contaService.alterarSituacaoConta(id, situacao);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Conta>> listarContasApagar(
            @RequestParam(required = false) LocalDate dtVencimento,
            @RequestParam(required = false) String descricao,
            Pageable pageable) {
        return ResponseEntity.ok(contaService.listarContasApagar(dtVencimento, descricao, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> obterContaPorId(@PathVariable Long id) {
        return contaService.obterContaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/total-pago")
    public ResponseEntity<BigDecimal> obterValorTotalPagoPorPeriodo(
            @RequestParam("dataVencimento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVencimento,
            @RequestParam("dataPagamento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPagamento) {
        BigDecimal totalPago = contaService.obterValorTotalPagoPorPeriodo(dataVencimento, dataPagamento);
        return ResponseEntity.ok(totalPago);
    }

    @PostMapping("/importar-csv")
    public ResponseEntity<String> importarCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo vazio");
        }

        try {
            csvImportacaoService.importarContasCSV(file);
            return ResponseEntity.status(HttpStatus.OK).body("Arquivo importado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro");
        }
    }
}