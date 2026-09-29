package com.pucrs.calculadora.exception;

public class DivisaoPorZeroException extends RuntimeException {
    public DivisaoPorZeroException(String message) {
        super(message);
    }
}
