package org.example.desafiosalles.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.example.desafiosalles.domain.Conta;
import org.example.desafiosalles.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportacaoService {
    @Autowired
    private ContaRepository contaRepository;

    public void importarContasCSV(MultipartFile file) throws Exception {
        List<Conta> contas = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                .withSkipLines(1)
                .build()) {

            List<String[]> linhas = csvReader.readAll();
            for (String[] linha : linhas) {
                Conta conta = new Conta();
                conta.setDescricao(linha[0].trim());
                conta.setDataVencimento(LocalDate.parse(linha[1].trim()));
                conta.setDataPagamento(LocalDate.parse(linha[2].trim()));
                conta.setValor(new BigDecimal(linha[3].trim()));
                conta.setSituacao(Conta.Situacao.valueOf(linha[4].trim().toUpperCase()));
                contas.add(conta);
            }
            contaRepository.saveAll(contas);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}