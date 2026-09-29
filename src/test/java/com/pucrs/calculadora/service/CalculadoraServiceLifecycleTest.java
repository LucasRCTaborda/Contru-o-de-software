package com.pucrs.calculadora.service;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Parte 1: Demonstração do Ciclo de Vida do JUnit 5 (@BeforeAll, @BeforeEach, @AfterEach, @AfterAll)")
class CalculadoraServiceLifecycleTest {

    private static int contadorGeral;
    private CalculadoraService service;
    private int contadorLocal;

    @BeforeAll
    static void setupGlobal() {
        contadorGeral = 0;
        System.out.println("🟢 [@BeforeAll] Inicialização estática antes de todos os testes da classe.");
    }

    @BeforeEach
    void setupBeforeEach() {
        service = new CalculadoraService();
        contadorGeral++;
        contadorLocal = 10;
        System.out.printf("  🔄 [@BeforeEach] Preparando estado antes do teste (Contador Geral: %d)%n", contadorGeral);
    }

    @Test
    @DisplayName("Teste de Ciclo 1: Soma e validação de isolamento de instância")
    void testeCiclo1_soma() {
        System.out.println("    ▶️ Executando Teste de Ciclo 1");
        double resultado = service.somar(5, 5);
        assertEquals(10, resultado);
        contadorLocal += 5;
        assertEquals(15, contadorLocal);
    }

    @Test
    @DisplayName("Teste de Ciclo 2: Multiplicação garantindo que contadorLocal foi reinicializado")
    void testeCiclo2_multiplicacao() {
        System.out.println("    ▶️ Executando Teste de Ciclo 2");
        assertNotNull(service);
        assertEquals(10, contadorLocal, "contadorLocal deve ser 10 pois @BeforeEach roda em cada teste de forma isolada");
        double resultado = service.multiplicar(4, 3);
        assertEquals(12, resultado);
    }

    @AfterEach
    void tearDownAfterEach() {
        System.out.println("  🧹 [@AfterEach] Limpeza executada após cada método de teste individual.");
    }

    @AfterAll
    static void tearDownGlobal() {
        System.out.printf("🔴 [@AfterAll] Finalização global executada uma única vez. Total de testes executados: %d%n", contadorGeral);
    }
}
