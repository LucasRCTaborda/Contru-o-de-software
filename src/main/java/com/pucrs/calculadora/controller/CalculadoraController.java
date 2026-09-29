package com.pucrs.calculadora.controller;

import com.pucrs.calculadora.dto.CalculoRequest;
import com.pucrs.calculadora.dto.CalculoResponse;
import com.pucrs.calculadora.model.Operacao;
import com.pucrs.calculadora.service.CalculadoraService;
import com.pucrs.calculadora.service.HistoricoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calculadora")
public class CalculadoraController {

    private final CalculadoraService calculadoraService;
    private final HistoricoService historicoService;

    public CalculadoraController(CalculadoraService calculadoraService, HistoricoService historicoService) {
        this.calculadoraService = calculadoraService;
        this.historicoService = historicoService;
    }

    @PostMapping("/calcular")
    public ResponseEntity<CalculoResponse> calcular(
            @Valid @RequestBody CalculoRequest request,
            @RequestHeader(value = "X-Usuario", defaultValue = "usuario-padrao") String usuario) {
        CalculoResponse response = historicoService.executarESalvar(request, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/somar")
    public ResponseEntity<CalculoResponse> somar(
            @RequestParam double a,
            @RequestParam double b,
            @RequestHeader(value = "X-Usuario", defaultValue = "usuario-padrao") String usuario) {
        CalculoRequest request = new CalculoRequest(Operacao.SOMA, a, b);
        CalculoResponse response = historicoService.executarESalvar(request, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subtrair")
    public ResponseEntity<CalculoResponse> subtrair(
            @RequestParam double a,
            @RequestParam double b,
            @RequestHeader(value = "X-Usuario", defaultValue = "usuario-padrao") String usuario) {
        CalculoRequest request = new CalculoRequest(Operacao.SUBTRACAO, a, b);
        CalculoResponse response = historicoService.executarESalvar(request, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/multiplicar")
    public ResponseEntity<CalculoResponse> multiplicar(
            @RequestParam double a,
            @RequestParam double b,
            @RequestHeader(value = "X-Usuario", defaultValue = "usuario-padrao") String usuario) {
        CalculoRequest request = new CalculoRequest(Operacao.MULTIPLICACAO, a, b);
        CalculoResponse response = historicoService.executarESalvar(request, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dividir")
    public ResponseEntity<CalculoResponse> dividir(
            @RequestParam double a,
            @RequestParam double b,
            @RequestHeader(value = "X-Usuario", defaultValue = "usuario-padrao") String usuario) {
        CalculoRequest request = new CalculoRequest(Operacao.DIVISAO, a, b);
        CalculoResponse response = historicoService.executarESalvar(request, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/potencia")
    public ResponseEntity<CalculoResponse> potencia(
            @RequestParam double base,
            @RequestParam double expoente,
            @RequestHeader(value = "X-Usuario", defaultValue = "usuario-padrao") String usuario) {
        CalculoRequest request = new CalculoRequest(Operacao.POTENCIA, base, expoente);
        CalculoResponse response = historicoService.executarESalvar(request, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/raiz")
    public ResponseEntity<CalculoResponse> raiz(
            @RequestParam double valor,
            @RequestHeader(value = "X-Usuario", defaultValue = "usuario-padrao") String usuario) {
        CalculoRequest request = new CalculoRequest(Operacao.RAIZ_QUADRADA, valor, null);
        CalculoResponse response = historicoService.executarESalvar(request, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/historico")
    public ResponseEntity<List<CalculoResponse>> listarHistorico(
            @RequestParam(required = false) Operacao operacao) {
        List<CalculoResponse> historico = (operacao != null)
                ? historicoService.listarPorOperacao(operacao)
                : historicoService.listarHistoricoRecente();
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/historico/{id}")
    public ResponseEntity<CalculoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historicoService.buscarPorId(id));
    }
}
