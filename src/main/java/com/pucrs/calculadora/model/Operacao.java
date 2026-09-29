package com.pucrs.calculadora.model;

public enum Operacao {
    SOMA("+", "Adição"),
    SUBTRACAO("-", "Subtração"),
    MULTIPLICACAO("*", "Multiplicação"),
    DIVISAO("/", "Divisão"),
    POTENCIA("^", "Potenciação"),
    RAIZ_QUADRADA("√", "Raiz Quadrada");

    private final String simbolo;
    private final String descricao;

    Operacao(String simbolo, String descricao) {
        this.simbolo = simbolo;
        this.descricao = descricao;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getDescricao() {
        return descricao;
    }
}
