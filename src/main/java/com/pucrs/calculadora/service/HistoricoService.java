package com.pucrs.calculadora.service;

import com.pucrs.calculadora.dto.CalculoRequest;
import com.pucrs.calculadora.dto.CalculoResponse;
import com.pucrs.calculadora.exception.OperacaoInvalidaException;
import com.pucrs.calculadora.model.HistoricoCalculo;
import com.pucrs.calculadora.model.Operacao;
import com.pucrs.calculadora.repository.HistoricoCalculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoricoService {

    private final CalculadoraService calculadoraService;
    private final AuditoriaService auditoriaService;
    private final HistoricoCalculoRepository historicoRepository;

    public HistoricoService(CalculadoraService calculadoraService,
                            AuditoriaService auditoriaService,
                            HistoricoCalculoRepository historicoRepository) {
        this.calculadoraService = calculadoraService;
        this.auditoriaService = auditoriaService;
        this.historicoRepository = historicoRepository;
    }

    @Transactional
    public CalculoResponse executarESalvar(CalculoRequest request, String usuario) {
        if (!auditoriaService.isOperacaoPermitida(request.getOperacao(), usuario)) {
            throw new OperacaoInvalidaException("Usuário não autorizado a realizar operações de cálculo");
        }

        double resultado = calculadoraService.calcular(request.getOperacao(), request.getOperandoA(), request.getOperandoB());

        // Salvar no histórico
        HistoricoCalculo historico = new HistoricoCalculo(
                request.getOperacao(),
                request.getOperandoA(),
                request.getOperandoB(),
                resultado
        );
        HistoricoCalculo salvo = historicoRepository.save(historico);

        // Registrar auditoria
        auditoriaService.registrarAuditoria(request.getOperacao(), resultado, usuario);

        String expressao = calculadoraService.formatarExpressao(
                request.getOperacao(),
                request.getOperandoA(),
                request.getOperandoB(),
                resultado
        );

        return new CalculoResponse(
                salvo.getId(),
                salvo.getOperacao(),
                salvo.getOperandoA(),
                salvo.getOperandoB(),
                salvo.getResultado(),
                expressao,
                salvo.getDataHora()
        );
    }

    @Transactional(readOnly = true)
    public List<CalculoResponse> listarHistoricoRecente() {
        return historicoRepository.findTop10ByOrderByDataHoraDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CalculoResponse> listarPorOperacao(Operacao operacao) {
        return historicoRepository.findByOperacao(operacao)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CalculoResponse buscarPorId(Long id) {
        HistoricoCalculo historico = historicoRepository.findById(id)
                .orElseThrow(() -> new OperacaoInvalidaException("Cálculo não encontrado com o ID: " + id));
        return mapToResponse(historico);
    }

    private CalculoResponse mapToResponse(HistoricoCalculo h) {
        String expressao = calculadoraService.formatarExpressao(h.getOperacao(), h.getOperandoA(), h.getOperandoB(), h.getResultado());
        return new CalculoResponse(
                h.getId(),
                h.getOperacao(),
                h.getOperandoA(),
                h.getOperandoB(),
                h.getResultado(),
                expressao,
                h.getDataHora()
        );
    }
}
