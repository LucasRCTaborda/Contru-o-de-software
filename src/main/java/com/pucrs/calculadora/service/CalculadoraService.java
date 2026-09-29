package com.pucrs.calculadora.service;

import com.pucrs.calculadora.exception.DivisaoPorZeroException;
import com.pucrs.calculadora.exception.OperacaoInvalidaException;
import com.pucrs.calculadora.model.Operacao;
import org.springframework.stereotype.Service;

@Service
public class CalculadoraService {

    public double somar(double a, double b) {
        return a + b;
    }

    public double subtrair(double a, double b) {
        return a - b;
    }

    public double multiplicar(double a, double b) {
        return a * b;
    }

    public double dividir(double a, double b) {
        if (b == 0.0) {
            throw new DivisaoPorZeroException("Divisão por zero não é permitida");
        }
        return a / b;
    }

    public double potencia(double base, double expoente) {
        return Math.pow(base, expoente);
    }

    public double raizQuadrada(double valor) {
        if (valor < 0.0) {
            throw new OperacaoInvalidaException("Não é possível calcular raiz quadrada de número negativo no conjunto dos reais: " + valor);
        }
        return Math.sqrt(valor);
    }

    public double calcular(Operacao operacao, Double a, Double b) {
        if (operacao == null) {
            throw new OperacaoInvalidaException("A operação não pode ser nula");
        }
        if (a == null) {
            throw new OperacaoInvalidaException("O primeiro operando (a) não pode ser nulo");
        }

        return switch (operacao) {
            case SOMA -> {
                validarSegundoOperandoObrigatorio(operacao, b);
                yield somar(a, b);
            }
            case SUBTRACAO -> {
                validarSegundoOperandoObrigatorio(operacao, b);
                yield subtrair(a, b);
            }
            case MULTIPLICACAO -> {
                validarSegundoOperandoObrigatorio(operacao, b);
                yield multiplicar(a, b);
            }
            case DIVISAO -> {
                validarSegundoOperandoObrigatorio(operacao, b);
                yield dividir(a, b);
            }
            case POTENCIA -> {
                validarSegundoOperandoObrigatorio(operacao, b);
                yield potencia(a, b);
            }
            case RAIZ_QUADRADA -> raizQuadrada(a);
        };
    }

    public String formatarExpressao(Operacao operacao, Double a, Double b, Double resultado) {
        if (operacao == Operacao.RAIZ_QUADRADA) {
            return String.format(java.util.Locale.US, "√%.2f = %.4f", a, resultado);
        }
        return String.format(java.util.Locale.US, "%.2f %s %.2f = %.4f", a, operacao.getSimbolo(), b, resultado);
    }

    private void validarSegundoOperandoObrigatorio(Operacao operacao, Double b) {
        if (b == null) {
            throw new OperacaoInvalidaException("O segundo operando (b) é obrigatório para a operação " + operacao);
        }
    }
}
