package com.pucrs.calculadora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucrs.calculadora.dto.CalculoRequest;
import com.pucrs.calculadora.dto.CalculoResponse;
import com.pucrs.calculadora.exception.DivisaoPorZeroException;
import com.pucrs.calculadora.exception.OperacaoInvalidaException;
import com.pucrs.calculadora.model.Operacao;
import com.pucrs.calculadora.service.CalculadoraService;
import com.pucrs.calculadora.service.HistoricoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculadoraController.class)
@DisplayName("Parte 3: Teste da Camada Web (Controller) com @WebMvcTest")
class CalculadoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CalculadoraService calculadoraService;

    @MockBean
    private HistoricoService historicoService;

    @Nested
    @DisplayName("Endpoint POST /api/calculadora/calcular")
    class CalcularEndpointTests {

        @Test
        @DisplayName("POST /api/calculadora/calcular - Retorna 201 Created quando payload for válido")
        void calcular_quandoPayloadValido_retornaStatus201EJson() throws Exception {
            // Arrange
            CalculoRequest request = new CalculoRequest(Operacao.SOMA, 10.0, 20.0);
            CalculoResponse mockResponse = new CalculoResponse(
                    1L,
                    Operacao.SOMA,
                    10.0,
                    20.0,
                    30.0,
                    "10.00 + 20.00 = 30.0000",
                    LocalDateTime.now()
            );

            given(historicoService.executarESalvar(any(CalculoRequest.class), eq("aluno-pucrs")))
                    .willReturn(mockResponse);

            // Act & Assert
            mockMvc.perform(post("/api/calculadora/calcular")
                            .header("X-Usuario", "aluno-pucrs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.operacao", is("SOMA")))
                    .andExpect(jsonPath("$.resultado", is(30.0)))
                    .andExpect(jsonPath("$.expressao", is("10.00 + 20.00 = 30.0000")));
        }

        @Test
        @DisplayName("POST /api/calculadora/calcular - Retorna 400 Bad Request quando payload for inválido (faltando campos obrigatórios)")
        void calcular_quandoCamposInvalidos_retornaStatus400() throws Exception {
            // Arrange: Objeto vazio sem operacao e operandoA (viola @NotNull)
            CalculoRequest requestInvalido = new CalculoRequest();

            // Act & Assert
            mockMvc.perform(post("/api/calculadora/calcular")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestInvalido)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.detalhes", not(empty())));
        }
    }

    @Nested
    @DisplayName("Endpoints GET de Operações Diretas")
    class OperacoesGetTests {

        @Test
        @DisplayName("GET /api/calculadora/somar - Retorna 200 OK com resultado da soma")
        void somar_quandoValoresValidos_retornaStatus200() throws Exception {
            CalculoResponse mockResponse = new CalculoResponse(2L, Operacao.SOMA, 7.0, 3.0, 10.0, "7.00 + 3.00 = 10.0000", LocalDateTime.now());
            given(historicoService.executarESalvar(any(CalculoRequest.class), any())).willReturn(mockResponse);

            mockMvc.perform(get("/api/calculadora/somar").param("a", "7.0").param("b", "3.0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultado", is(10.0)))
                    .andExpect(jsonPath("$.operacao", is("SOMA")));
        }

        @Test
        @DisplayName("GET /api/calculadora/subtrair - Retorna 200 OK com resultado da subtração")
        void subtrair_quandoValoresValidos_retornaStatus200() throws Exception {
            CalculoResponse mockResponse = new CalculoResponse(3L, Operacao.SUBTRACAO, 15.0, 5.0, 10.0, "15.00 - 5.00 = 10.0000", LocalDateTime.now());
            given(historicoService.executarESalvar(any(CalculoRequest.class), any())).willReturn(mockResponse);

            mockMvc.perform(get("/api/calculadora/subtrair").param("a", "15.0").param("b", "5.0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultado", is(10.0)));
        }

        @Test
        @DisplayName("GET /api/calculadora/multiplicar - Retorna 200 OK com resultado da multiplicação")
        void multiplicar_quandoValoresValidos_retornaStatus200() throws Exception {
            CalculoResponse mockResponse = new CalculoResponse(4L, Operacao.MULTIPLICACAO, 6.0, 7.0, 42.0, "6.00 * 7.00 = 42.0000", LocalDateTime.now());
            given(historicoService.executarESalvar(any(CalculoRequest.class), any())).willReturn(mockResponse);

            mockMvc.perform(get("/api/calculadora/multiplicar").param("a", "6.0").param("b", "7.0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultado", is(42.0)));
        }

        @Test
        @DisplayName("GET /api/calculadora/potencia - Retorna 200 OK com resultado da exponenciação")
        void potencia_quandoValoresValidos_retornaStatus200() throws Exception {
            CalculoResponse mockResponse = new CalculoResponse(5L, Operacao.POTENCIA, 2.0, 3.0, 8.0, "2.00 ^ 3.00 = 8.0000", LocalDateTime.now());
            given(historicoService.executarESalvar(any(CalculoRequest.class), any())).willReturn(mockResponse);

            mockMvc.perform(get("/api/calculadora/potencia").param("base", "2.0").param("expoente", "3.0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultado", is(8.0)));
        }

        @Test
        @DisplayName("GET /api/calculadora/raiz - Retorna 200 OK com resultado da raiz quadrada")
        void raiz_quandoValoresValidos_retornaStatus200() throws Exception {
            CalculoResponse mockResponse = new CalculoResponse(6L, Operacao.RAIZ_QUADRADA, 64.0, null, 8.0, "√64.00 = 8.0000", LocalDateTime.now());
            given(historicoService.executarESalvar(any(CalculoRequest.class), any())).willReturn(mockResponse);

            mockMvc.perform(get("/api/calculadora/raiz").param("valor", "64.0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultado", is(8.0)));
        }

        @Test
        @DisplayName("GET /api/calculadora/dividir - Retorna 422 Unprocessable Entity quando divisão por zero")
        void dividir_porZero_retornaStatus422Tratado() throws Exception {
            given(historicoService.executarESalvar(any(CalculoRequest.class), any()))
                    .willThrow(new DivisaoPorZeroException("Divisão por zero não é permitida"));

            mockMvc.perform(get("/api/calculadora/dividir")
                            .param("a", "10.0")
                            .param("b", "0.0"))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.status", is(422)))
                    .andExpect(jsonPath("$.mensagem", is("Divisão por zero não é permitida")));
        }

        @Test
        @DisplayName("GET /api/calculadora/raiz - Retorna 400 Bad Request quando raiz de número negativo")
        void raiz_negativo_retornaStatus400Tratado() throws Exception {
            given(historicoService.executarESalvar(any(CalculoRequest.class), any()))
                    .willThrow(new OperacaoInvalidaException("Não é possível calcular raiz quadrada de número negativo"));

            mockMvc.perform(get("/api/calculadora/raiz")
                            .param("valor", "-9.0"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.mensagem", containsString("raiz quadrada de número negativo")));
        }
    }

    @Nested
    @DisplayName("Endpoints de Histórico")
    class HistoricoEndpointTests {

        @Test
        @DisplayName("GET /api/calculadora/historico - Retorna lista de históricos com 200 OK")
        void listarHistorico_semFiltro_retornaStatus200ELista() throws Exception {
            CalculoResponse resp1 = new CalculoResponse(1L, Operacao.SOMA, 2.0, 3.0, 5.0, "2 + 3 = 5", LocalDateTime.now());
            CalculoResponse resp2 = new CalculoResponse(2L, Operacao.MULTIPLICACAO, 4.0, 5.0, 20.0, "4 * 5 = 20", LocalDateTime.now());

            given(historicoService.listarHistoricoRecente()).willReturn(List.of(resp1, resp2));

            mockMvc.perform(get("/api/calculadora/historico"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].resultado", is(5.0)))
                    .andExpect(jsonPath("$[1].resultado", is(20.0)));
        }

        @Test
        @DisplayName("GET /api/calculadora/historico?operacao=SOMA - Retorna lista filtrada por operação")
        void listarHistorico_comFiltroOperacao_retornaStatus200EListaFiltrada() throws Exception {
            CalculoResponse resp1 = new CalculoResponse(1L, Operacao.SOMA, 10.0, 15.0, 25.0, "10 + 15 = 25", LocalDateTime.now());

            given(historicoService.listarPorOperacao(Operacao.SOMA)).willReturn(List.of(resp1));

            mockMvc.perform(get("/api/calculadora/historico").param("operacao", "SOMA"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].operacao", is("SOMA")));
        }

        @Test
        @DisplayName("GET /api/calculadora/historico/{id} - Retorna cálculo específico por ID")
        void buscarPorId_quandoExiste_retornaStatus200() throws Exception {
            CalculoResponse resp = new CalculoResponse(10L, Operacao.POTENCIA, 2.0, 4.0, 16.0, "2 ^ 4 = 16", LocalDateTime.now());

            given(historicoService.buscarPorId(10L)).willReturn(resp);

            mockMvc.perform(get("/api/calculadora/historico/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(10)))
                    .andExpect(jsonPath("$.resultado", is(16.0)));
        }
    }
}
