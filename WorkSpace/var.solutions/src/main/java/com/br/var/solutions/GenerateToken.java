package com.br.var.solutions;

import java.util.Date;

public class GenerateToken {

    private String token;
    private Date expiraEm;
    private long tempoValidacao;
    private String solicitante;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(Date expiraEm) {
        this.expiraEm = expiraEm;
    }

    public long getTempoValidacao() {
        return tempoValidacao;
    }

    public void setTempoValidacao(long tempoValidacao) {
        this.tempoValidacao = tempoValidacao;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
}
