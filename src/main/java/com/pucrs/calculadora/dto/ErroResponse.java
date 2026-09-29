package com.pucrs.calculadora.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErroResponse {

    private int status;
    private String erro;
    private String mensagem;
    private String path;
    private LocalDateTime timestamp;
    private List<String> detalhes;

    public ErroResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErroResponse(int status, String erro, String mensagem, String path) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public ErroResponse(int status, String erro, String mensagem, String path, List<String> detalhes) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        this.detalhes = detalhes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(List<String> detalhes) {
        this.detalhes = detalhes;
    }
}
