package com.pucrs.calculadora.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucrs.calculadora.dto.CalculoRequest;
import com.pucrs.calculadora.model.Operacao;
import com.pucrs.calculadora.repository.HistoricoCalculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Parte 3: Teste de Integração Ponta a Ponta (E2E) com @SpringBootTest")
class CalculadoraIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoricoCalculoRepository repository;

    @BeforeEach
    void setUp() {
        // Limpar o repositório H2 antes de cada teste de integração
        repository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo E2E: Deve calcular adição via POST, persistir no H2 e consultar no histórico")
    void fluxoCompleto_calculoEHistorico_sucesso() throws Exception {
        // 1. Arrange: Payload para cálculo de 50 + 25
        CalculoRequest request = new CalculoRequest(Operacao.SOMA, 50.0, 25.0);

        // 2. Act & Assert: POST /api/calculadora/calcular
        mockMvc.perform(post("/api/calculadora/calcular")
                        .header("X-Usuario", "estudante-pucrs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.operacao", is("SOMA")))
                .andExpect(jsonPath("$.operandoA", is(50.0)))
                .andExpect(jsonPath("$.operandoB", is(25.0)))
                .andExpect(jsonPath("$.resultado", is(75.0)))
                .andExpect(jsonPath("$.expressao", containsString("50.00 + 25.00 = 75.0000")));

        // 3. Verificar persistência real no banco de dados H2
        assertEquals(1, repository.count(), "Deveria existir exatamente 1 registro no banco H2");

        // 4. Act & Assert: GET /api/calculadora/historico
        mockMvc.perform(get("/api/calculadora/historico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].resultado", is(75.0)))
                .andExpect(jsonPath("$[0].operacao", is("SOMA")));
    }

    @Test
    @DisplayName("Fluxo E2E: Deve calcular raiz quadrada e salvar no H2")
    void fluxoCompleto_raizQuadrada_sucesso() throws Exception {
        // Act & Assert: GET /api/calculadora/raiz?valor=144
        mockMvc.perform(get("/api/calculadora/raiz")
                        .param("valor", "144.0")
                        .header("X-Usuario", "estudante-pucrs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado", is(12.0)))
                .andExpect(jsonPath("$.operacao", is("RAIZ_QUADRADA")));

        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("Fluxo E2E: Erro de divisão por zero deve retornar 422 e não persistir cálculo com erro")
    void fluxoCompleto_divisaoPorZero_retorna422ENaoPersiste() throws Exception {
        // Act & Assert: GET /api/calculadora/dividir?a=100&b=0
        mockMvc.perform(get("/api/calculadora/dividir")
                        .param("a", "100.0")
                        .param("b", "0.0")
                        .header("X-Usuario", "estudante-pucrs"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath("$.mensagem", is("Divisão por zero não é permitida")));

        assertEquals(0, repository.count(), "Nenhum cálculo com erro deve ser salvo no histórico");
    }
}
