package com.pucrs.calculadora.repository;

import com.pucrs.calculadora.model.HistoricoCalculo;
import com.pucrs.calculadora.model.Operacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Parte 3: Teste da Camada de Persistência com @DataJpaTest")
class HistoricoCalculoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HistoricoCalculoRepository repository;

    @Test
    @DisplayName("Deve persistir e buscar cálculo por ID com sucesso")
    void saveAndFindById_retornaEntidadePersistida() {
        // Arrange
        HistoricoCalculo novoCalculo = new HistoricoCalculo(
                Operacao.SOMA,
                15.0,
                25.0,
                40.0
        );
        HistoricoCalculo salvo = entityManager.persistAndFlush(novoCalculo);

        // Act
        Optional<HistoricoCalculo> encontrado = repository.findById(salvo.getId());

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(salvo.getId());
        assertThat(encontrado.get().getOperacao()).isEqualTo(Operacao.SOMA);
        assertThat(encontrado.get().getResultado()).isEqualTo(40.0);
    }

    @Test
    @DisplayName("Deve buscar todos os cálculos filtrados por Operacao")
    void findByOperacao_retornaApenasOperacoesEspecificadas() {
        // Arrange
        entityManager.persist(new HistoricoCalculo(Operacao.SOMA, 1.0, 2.0, 3.0));
        entityManager.persist(new HistoricoCalculo(Operacao.SOMA, 10.0, 20.0, 30.0));
        entityManager.persist(new HistoricoCalculo(Operacao.DIVISAO, 10.0, 2.0, 5.0));
        entityManager.flush();

        // Act
        List<HistoricoCalculo> somas = repository.findByOperacao(Operacao.SOMA);
        List<HistoricoCalculo> divisoes = repository.findByOperacao(Operacao.DIVISAO);

        // Assert
        assertThat(somas).hasSize(2);
        assertThat(divisoes).hasSize(1);
        assertThat(somas).allMatch(c -> c.getOperacao() == Operacao.SOMA);
    }

    @Test
    @DisplayName("Deve retornar os 10 cálculos mais recentes ordenados por dataHora decrescente")
    void findTop10ByOrderByDataHoraDesc_retornaOrdenado() {
        // Arrange
        LocalDateTime baseTime = LocalDateTime.of(2026, 9, 23, 10, 0);
        for (int i = 1; i <= 15; i++) {
            HistoricoCalculo h = new HistoricoCalculo(
                    null,
                    Operacao.SOMA,
                    (double) i,
                    (double) i,
                    (double) (i * 2),
                    baseTime.plusMinutes(i)
            );
            entityManager.persist(h);
        }
        entityManager.flush();

        // Act
        List<HistoricoCalculo> top10 = repository.findTop10ByOrderByDataHoraDesc();

        // Assert
        assertThat(top10).hasSize(10);
        // O primeiro deve ser o mais recente (i = 15)
        assertThat(top10.get(0).getOperandoA()).isEqualTo(15.0);
        assertThat(top10.get(9).getOperandoA()).isEqualTo(6.0);
    }
}
