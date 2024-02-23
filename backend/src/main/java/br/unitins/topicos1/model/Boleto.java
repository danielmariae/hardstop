package br.unitins.topicos1.model;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOLETO_BANCARIO")
public class Boleto extends FormaDePagamento{
    private String nomeBanco;
    private LocalDateTime dataHoraGeracao;
    private LocalDateTime dataHoraLimitePag;
    private LocalDateTime dataHoraEfetivadoPagamento;
    private String nomeArquivo;

    public LocalDateTime getDataHoraEfetivadoPagamento() {
        return dataHoraEfetivadoPagamento;
    }
    public void setDataHoraEfetivadoPagamento(LocalDateTime dataHoraEfetivadoPagamento) {
        this.dataHoraEfetivadoPagamento = dataHoraEfetivadoPagamento;
    }
    

    
   

    public String getNomeBanco() {
        return nomeBanco;
    }
    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }
    public LocalDateTime getDataHoraGeracao() {
        return dataHoraGeracao;
    }
    public void setDataHoraGeracao(LocalDateTime dataHoraGeracao) {
        this.dataHoraGeracao = dataHoraGeracao;
    }
    public LocalDateTime getDataHoraLimitePag() {
        return dataHoraLimitePag;
    }
    public void setDataHoraLimitePag(LocalDateTime dataHoraLimitePag) {
        this.dataHoraLimitePag = dataHoraLimitePag;
    }

    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

}
