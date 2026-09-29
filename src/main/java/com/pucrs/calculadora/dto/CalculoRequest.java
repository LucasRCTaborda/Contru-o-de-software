package com.pucrs.calculadora.dto;

import com.pucrs.calculadora.model.Operacao;
import jakarta.validation.constraints.NotNull;

public class CalculoRequest {

    @NotNull(message = "A operação matemática é obrigatória")
    private Operacao operacao;

    @NotNull(message = "O operando A é obrigatório")
    private Double operandoA;

    private Double operandoB;

    public CalculoRequest() {
    }

    public CalculoRequest(Operacao operacao, Double operandoA, Double operandoB) {
        this.operacao = operacao;
        this.operandoA = operandoA;
        this.operandoB = operandoB;
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
}
