package com.pucrs.calculadora.service;

import com.pucrs.calculadora.model.Operacao;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    public void registrarAuditoria(Operacao operacao, Double resultado, String usuario) {
        // Simulação de registro em serviço de telemetria/auditoria externo
        System.out.printf("[AUDITORIA] Usuário '%s' executou %s com resultado: %.4f%n", usuario, operacao, resultado);
    }

    public boolean isOperacaoPermitida(Operacao operacao, String usuario) {
        // Regra de segurança/permissão
        return usuario != null && !usuario.isBlank();
    }
}
