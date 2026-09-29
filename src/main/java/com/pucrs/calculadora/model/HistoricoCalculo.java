package com.pucrs.calculadora.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "historico_calculos")
public class HistoricoCalculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Operacao operacao;

    @Column(nullable = false)
    private Double operandoA;

    @Column(nullable = true)
    private Double operandoB;

    @Column(nullable = false)
    private Double resultado;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public HistoricoCalculo() {
        this.dataHora = LocalDateTime.now();
    }

    public HistoricoCalculo(Operacao operacao, Double operandoA, Double operandoB, Double resultado) {
        this.operacao = operacao;
        this.operandoA = operandoA;
        this.operandoB = operandoB;
        this.resultado = resultado;
        this.dataHora = LocalDateTime.now();
    }

    public HistoricoCalculo(Long id, Operacao operacao, Double operandoA, Double operandoB, Double resultado, LocalDateTime dataHora) {
        this.id = id;
        this.operacao = operacao;
        this.operandoA = operandoA;
        this.operandoB = operandoB;
        this.resultado = resultado;
        this.dataHora = dataHora != null ? dataHora : LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public Double getOperandoA() {
        return operandoA;
    }

    public void setOperandoA(Double operandoA) {
        this.operandoA = operandoA;
    }

    public Double getOperandoB() {
        return operandoB;
    }

    public void setOperandoB(Double operandoB) {
        this.operandoB = operandoB;
    }

    public Double getResultado() {
        return resultado;
    }

    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoCalculo that = (HistoricoCalculo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
