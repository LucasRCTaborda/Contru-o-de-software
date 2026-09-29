package com.pucrs.calculadora.service;

import com.pucrs.calculadora.dto.CalculoRequest;
import com.pucrs.calculadora.dto.CalculoResponse;
import com.pucrs.calculadora.exception.OperacaoInvalidaException;
import com.pucrs.calculadora.model.HistoricoCalculo;
import com.pucrs.calculadora.model.Operacao;
import com.pucrs.calculadora.repository.HistoricoCalculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Parte 2: Isolando Dependências com Mockito - HistoricoService")
class HistoricoServiceMockitoTest {

    @Mock
    private CalculadoraService calculadoraService;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private HistoricoCalculoRepository historicoRepository;

    @InjectMocks
    private HistoricoService historicoService;

    @Nested
    @DisplayName("Execução e Salvamento com Mocks e ArgumentCaptor")
    class ExecutarESalvarTests {

        @Test
        @DisplayName("Deve executar cálculo, registrar auditoria, salvar histórico e capturar argumentos")
        void executarESalvar_quandoDadosValidos_retornaResponseECapturaEntidade() {
            // 1. Arrange (Stubbing)
            String usuario = "lucas.taborda";
            CalculoRequest request = new CalculoRequest(Operacao.SOMA, 20.0, 30.0);
            double resultadoCalculado = 50.0;
            String expressaoEsperada = "20.00 + 30.00 = 50.0000";

            when(auditoriaService.isOperacaoPermitida(Operacao.SOMA, usuario)).thenReturn(true);
            when(calculadoraService.calcular(Operacao.SOMA, 20.0, 30.0)).thenReturn(resultadoCalculado);
            when(calculadoraService.formatarExpressao(Operacao.SOMA, 20.0, 30.0, resultadoCalculado)).thenReturn(expressaoEsperada);

            HistoricoCalculo historicoSalvo = new HistoricoCalculo(
                    1L,
                    Operacao.SOMA,
                    20.0,
                    30.0,
                    resultadoCalculado,
                    LocalDateTime.of(2026, 9, 23, 20, 0)
            );
            when(historicoRepository.save(any(HistoricoCalculo.class))).thenReturn(historicoSalvo);

            // 2. Act
            CalculoResponse response = historicoService.executarESalvar(request, usuario);

            // 3. Assert
            assertNotNull(response);
            assertEquals(1L, response.getId());
            assertEquals(Operacao.SOMA, response.getOperacao());
            assertEquals(resultadoCalculado, response.getResultado());
            assertEquals(expressaoEsperada, response.getExpressao());

            // 4. Verify & ArgumentCaptor
            verify(auditoriaService, times(1)).isOperacaoPermitida(Operacao.SOMA, usuario);
            verify(calculadoraService, times(1)).calcular(Operacao.SOMA, 20.0, 30.0);
            verify(auditoriaService, times(1)).registrarAuditoria(Operacao.SOMA, resultadoCalculado, usuario);

            ArgumentCaptor<HistoricoCalculo> captor = ArgumentCaptor.forClass(HistoricoCalculo.class);
            verify(historicoRepository, times(1)).save(captor.capture());

            HistoricoCalculo entidadeCapturada = captor.getValue();
            assertEquals(Operacao.SOMA, entidadeCapturada.getOperacao());
            assertEquals(20.0, entidadeCapturada.getOperandoA());
            assertEquals(30.0, entidadeCapturada.getOperandoB());
            assertEquals(50.0, entidadeCapturada.getResultado());
        }

        @Test
        @DisplayName("Deve lançar OperacaoInvalidaException e NUNCA chamar repositório quando usuário não autorizado")
        void executarESalvar_quandoUsuarioNaoAutorizado_lancaExcecaoENaoPersiste() {
            // Arrange
            String usuarioInvalido = "";
            CalculoRequest request = new CalculoRequest(Operacao.DIVISAO, 10.0, 2.0);

            when(auditoriaService.isOperacaoPermitida(Operacao.DIVISAO, usuarioInvalido)).thenReturn(false);

            // Act & Assert
            OperacaoInvalidaException exception = assertThrows(
                    OperacaoInvalidaException.class,
                    () -> historicoService.executarESalvar(request, usuarioInvalido)
            );

            assertEquals("Usuário não autorizado a realizar operações de cálculo", exception.getMessage());

            // Verify: Garante que métodos subsequentes NUNCA foram chamados
            verify(calculadoraService, never()).calcular(any(), any(), any());
            verify(historicoRepository, never()).save(any(HistoricoCalculo.class));
            verify(auditoriaService, never()).registrarAuditoria(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("Consultas de Histórico com Mocks")
    class ConsultasTests {

        @Test
        @DisplayName("Deve retornar cálculo existente por ID")
        void buscarPorId_quandoExiste_retornaResponse() {
            // Arrange
            Long id = 42L;
            HistoricoCalculo mockHistorico = new HistoricoCalculo(
                    id,
                    Operacao.MULTIPLICACAO,
                    5.0,
                    6.0,
                    30.0,
                    LocalDateTime.now()
            );

            when(historicoRepository.findById(id)).thenReturn(Optional.of(mockHistorico));
            when(calculadoraService.formatarExpressao(Operacao.MULTIPLICACAO, 5.0, 6.0, 30.0))
                    .thenReturn("5.00 * 6.00 = 30.0000");

            // Act
            CalculoResponse response = historicoService.buscarPorId(id);

            // Assert
            assertNotNull(response);
            assertEquals(42L, response.getId());
            assertEquals(30.0, response.getResultado());
            verify(historicoRepository, times(1)).findById(id);
        }

        @Test
        @DisplayName("Deve lançar OperacaoInvalidaException quando ID não for encontrado")
        void buscarPorId_quandoNaoExiste_lancaExcecao() {
            // Arrange
            Long idInexistente = 999L;
            when(historicoRepository.findById(idInexistente)).thenReturn(Optional.empty());

            // Act & Assert
            OperacaoInvalidaException exception = assertThrows(
                    OperacaoInvalidaException.class,
                    () -> historicoService.buscarPorId(idInexistente)
            );

            assertTrue(exception.getMessage().contains("Cálculo não encontrado com o ID: 999"));
            verify(historicoRepository, times(1)).findById(idInexistente);
        }

        @Test
        @DisplayName("Deve listar histórico recente mapeado corretamente")
        void listarHistoricoRecente_quandoExistemRegistros_retornaListaDTO() {
            // Arrange
            HistoricoCalculo h1 = new HistoricoCalculo(1L, Operacao.SOMA, 1.0, 2.0, 3.0, LocalDateTime.now());
            HistoricoCalculo h2 = new HistoricoCalculo(2L, Operacao.POTENCIA, 2.0, 4.0, 16.0, LocalDateTime.now());

            when(historicoRepository.findTop10ByOrderByDataHoraDesc()).thenReturn(List.of(h1, h2));
            when(calculadoraService.formatarExpressao(any(), any(), any(), any())).thenReturn("expressao-teste");

            // Act
            List<CalculoResponse> lista = historicoService.listarHistoricoRecente();

            // Assert
            assertEquals(2, lista.size());
            verify(historicoRepository, times(1)).findTop10ByOrderByDataHoraDesc();
            verifyNoMoreInteractions(historicoRepository);
        }

        @Test
        @DisplayName("Deve listar cálculos filtrados por operação")
        void listarPorOperacao_quandoFiltrado_retornaListaCorrespondente() {
            // Arrange
            HistoricoCalculo h1 = new HistoricoCalculo(1L, Operacao.SOMA, 10.0, 20.0, 30.0, LocalDateTime.now());
            when(historicoRepository.findByOperacao(Operacao.SOMA)).thenReturn(List.of(h1));
            when(calculadoraService.formatarExpressao(any(), any(), any(), any())).thenReturn("10.00 + 20.00 = 30.0000");

            // Act
            List<CalculoResponse> lista = historicoService.listarPorOperacao(Operacao.SOMA);

            // Assert
            assertEquals(1, lista.size());
            assertEquals(Operacao.SOMA, lista.get(0).getOperacao());
            verify(historicoRepository, times(1)).findByOperacao(Operacao.SOMA);
        }
    }
}
