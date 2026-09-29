package com.pucrs.calculadora.service;

import com.pucrs.calculadora.exception.DivisaoPorZeroException;
import com.pucrs.calculadora.exception.OperacaoInvalidaException;
import com.pucrs.calculadora.model.Operacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Parte 1: Testes Unitários com JUnit 5 - CalculadoraService")
class CalculadoraServiceUnitTest {

    private CalculadoraService calculadoraService;

    @BeforeEach
    void setUp() {
        // Arrange comum a cada teste
        calculadoraService = new CalculadoraService();
    }

    @Nested
    @DisplayName("Operação de Adição")
    class AdicaoTests {

        @Test
        @DisplayName("Deve somar dois números positivos com sucesso")
        void somar_doisNumerosPositivos_retornaSomaCorreta() {
            // Arrange
            double a = 10.5;
            double b = 5.5;

            // Act
            double resultado = calculadoraService.somar(a, b);

            // Assert
            assertEquals(16.0, resultado, 0.0001, "10.5 + 5.5 deve ser igual a 16.0");
        }

        @Test
        @DisplayName("Deve somar números negativos e zero corretamente")
        void somar_numerosComNegativoEZero_retornaResultadoEsperado() {
            // Arrange & Act & Assert (assertAll para múltiplas validações)
            assertAll("Validação de somas com negativos e zero",
                    () -> assertEquals(-5.0, calculadoraService.somar(-2.0, -3.0), "Soma de dois negativos"),
                    () -> assertEquals(7.0, calculadoraService.somar(7.0, 0.0), "Soma com zero"),
                    () -> assertEquals(0.0, calculadoraService.somar(-5.0, 5.0), "Soma resultando em zero")
            );
        }

        @ParameterizedTest(name = "[{index}] {0} + {1} = {2}")
        @CsvSource({
                "1.0, 1.0, 2.0",
                "0.0, 0.0, 0.0",
                "-10.0, 15.0, 5.0",
                "100.5, 200.25, 300.75"
        })
        @DisplayName("Deve somar corretamente múltiplos pares de valores via @ParameterizedTest")
        void somar_multiplosValores_retornaSomaEsperada(double a, double b, double esperado) {
            // Act
            double resultado = calculadoraService.somar(a, b);

            // Assert
            assertEquals(esperado, resultado, 0.0001);
        }
    }

    @Nested
    @DisplayName("Operação de Subtração")
    class SubtracaoTests {

        @Test
        @DisplayName("Deve subtrair dois valores e retornar a diferença correta")
        void subtrair_doisNumeros_retornaDiferenca() {
            // Arrange
            double a = 20.0;
            double b = 8.0;

            // Act
            double resultado = calculadoraService.subtrair(a, b);

            // Assert
            assertEquals(12.0, resultado, 0.0001);
        }
    }

    @Nested
    @DisplayName("Operação de Multiplicação")
    class MultiplicacaoTests {

        @Test
        @DisplayName("Deve multiplicar dois números positivos corretamente")
        void multiplicar_doisNumeros_retornaProduto() {
            // Arrange
            double a = 6.0;
            double b = 7.0;

            // Act
            double resultado = calculadoraService.multiplicar(a, b);

            // Assert
            assertEquals(42.0, resultado, 0.0001);
        }

        @Test
        @DisplayName("Multiplicação por zero deve retornar zero")
        void multiplicar_porZero_retornaZero() {
            // Act
            double resultado = calculadoraService.multiplicar(999.0, 0.0);

            // Assert
            assertEquals(0.0, resultado, 0.0001);
        }
    }

    @Nested
    @DisplayName("Operação de Divisão e Tratamento de Exceções")
    class DivisaoTests {

        @Test
        @DisplayName("Deve dividir dois números quando o divisor for diferente de zero")
        void dividir_divisorNaoZero_retornaQuociente() {
            // Arrange
            double a = 15.0;
            double b = 3.0;

            // Act
            double resultado = calculadoraService.dividir(a, b);

            // Assert
            assertEquals(5.0, resultado, 0.0001);
        }

        @Test
        @DisplayName("Deve lançar DivisaoPorZeroException ao dividir por zero")
        void dividir_porZero_lancaDivisaoPorZeroException() {
            // Arrange
            double a = 10.0;
            double divisorZero = 0.0;

            // Act & Assert
            DivisaoPorZeroException exception = assertThrows(
                    DivisaoPorZeroException.class,
                    () -> calculadoraService.dividir(a, divisorZero),
                    "Deveria ter lançado DivisaoPorZeroException"
            );

            assertEquals("Divisão por zero não é permitida", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Operações Avançadas: Potência e Raiz Quadrada")
    class OperacoesAvancadasTests {

        @Test
        @DisplayName("Deve calcular a potência corretamente (base ^ expoente)")
        void potencia_baseEExpoenteValidos_retornaResultadoCorreto() {
            // Arrange
            double base = 2.0;
            double expoente = 3.0;

            // Act
            double resultado = calculadoraService.potencia(base, expoente);

            // Assert
            assertEquals(8.0, resultado, 0.0001);
        }

        @Test
        @DisplayName("Deve calcular a raiz quadrada de números positivos e zero")
        void raizQuadrada_numeroPositivoOuZero_retornaRaiz() {
            // Act & Assert
            assertAll("Cálculo de raiz quadrada",
                    () -> assertEquals(5.0, calculadoraService.raizQuadrada(25.0), 0.0001),
                    () -> assertEquals(0.0, calculadoraService.raizQuadrada(0.0), 0.0001),
                    () -> assertEquals(3.0, calculadoraService.raizQuadrada(9.0), 0.0001)
            );
        }

        @Test
        @DisplayName("Deve lançar OperacaoInvalidaException ao tentar raiz de número negativo")
        void raizQuadrada_numeroNegativo_lancaOperacaoInvalidaException() {
            // Arrange
            double negativo = -4.0;

            // Act & Assert
            OperacaoInvalidaException exception = assertThrows(
                    OperacaoInvalidaException.class,
                    () -> calculadoraService.raizQuadrada(negativo)
            );

            assertTrue(exception.getMessage().contains("Não é possível calcular raiz quadrada de número negativo"));
        }
    }

    @Nested
    @DisplayName("Método Genérico calcular() e Validações de Entrada")
    class CalcularGenericoTests {

        @Test
        @DisplayName("Deve despachar corretamente para a operação via enum")
        void calcular_comEnumValido_executaOperacaoCorreta() {
            // Act & Assert
            assertAll("Despacho de operações",
                    () -> assertEquals(10.0, calculadoraService.calcular(Operacao.SOMA, 6.0, 4.0), 0.0001),
                    () -> assertEquals(2.0, calculadoraService.calcular(Operacao.SUBTRACAO, 6.0, 4.0), 0.0001),
                    () -> assertEquals(24.0, calculadoraService.calcular(Operacao.MULTIPLICACAO, 6.0, 4.0), 0.0001),
                    () -> assertEquals(1.5, calculadoraService.calcular(Operacao.DIVISAO, 6.0, 4.0), 0.0001),
                    () -> assertEquals(16.0, calculadoraService.calcular(Operacao.POTENCIA, 4.0, 2.0), 0.0001),
                    () -> assertEquals(4.0, calculadoraService.calcular(Operacao.RAIZ_QUADRADA, 16.0, null), 0.0001)
            );
        }

        @Test
        @DisplayName("Deve lançar OperacaoInvalidaException quando operacao for nula")
        void calcular_operacaoNula_lancaOperacaoInvalidaException() {
            assertThrows(OperacaoInvalidaException.class, () -> calculadoraService.calcular(null, 5.0, 2.0));
        }

        @Test
        @DisplayName("Deve lançar OperacaoInvalidaException quando operando A for nulo")
        void calcular_operandoANulo_lancaOperacaoInvalidaException() {
            assertThrows(OperacaoInvalidaException.class, () -> calculadoraService.calcular(Operacao.SOMA, null, 2.0));
        }

        @Test
        @DisplayName("Deve lançar OperacaoInvalidaException quando segundo operando for obrigatório mas for nulo")
        void calcular_operandoBNuloEmOperacaoBinaria_lancaOperacaoInvalidaException() {
            assertThrows(OperacaoInvalidaException.class, () -> calculadoraService.calcular(Operacao.DIVISAO, 10.0, null));
        }

        @Test
        @DisplayName("Deve formatar expressão matemática em String legível")
        void formatarExpressao_cenariosValidos_retornaStringFormatada() {
            String expressaoSoma = calculadoraService.formatarExpressao(Operacao.SOMA, 2.0, 3.0, 5.0);
            String expressaoRaiz = calculadoraService.formatarExpressao(Operacao.RAIZ_QUADRADA, 9.0, null, 3.0);

            assertNotNull(expressaoSoma);
            assertTrue(expressaoSoma.contains("+"));
            assertTrue(expressaoRaiz.contains("√"));
        }
    }
}
