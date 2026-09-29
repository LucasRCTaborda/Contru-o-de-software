package com.pucrs.calculadora;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Smoke Test - Inicialização do Contexto Spring Boot")
class CalculadoraApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve carregar o ApplicationContext com todos os beans essenciais")
    void contextLoads() {
        // Assert
        assertThat(applicationContext).isNotNull();
        assertThat(applicationContext.containsBean("calculadoraController")).isTrue();
        assertThat(applicationContext.containsBean("calculadoraService")).isTrue();
        assertThat(applicationContext.containsBean("historicoService")).isTrue();
        assertThat(applicationContext.containsBean("auditoriaService")).isTrue();
        assertThat(applicationContext.containsBean("historicoCalculoRepository")).isTrue();
    }
}
