package org.example.desafiosalles.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    public enum Situacao {
        PENDENTE, PAGA, ATRASADA
    }
}