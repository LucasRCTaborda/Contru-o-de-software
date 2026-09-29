package com.pucrs.calculadora.dto;

import com.pucrs.calculadora.model.Operacao;
import java.time.LocalDateTime;

public class CalculoResponse {

    private Long id;
    private Operacao operacao;
    private Double operandoA;
    private Double operandoB;
    private Double resultado;
    private String expressao;
    private LocalDateTime dataHora;

    public CalculoResponse() {
    }

    public CalculoResponse(Long id, Operacao operacao, Double operandoA, Double operandoB, Double resultado, String expressao, LocalDateTime dataHora) {
        this.id = id;
        this.operacao = operacao;
        this.operandoA = operandoA;
        this.operandoB = operandoB;
        this.resultado = resultado;
        this.expressao = expressao;
        this.dataHora = dataHora;
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

    public String getExpressao() {
        return expressao;
    }

    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
