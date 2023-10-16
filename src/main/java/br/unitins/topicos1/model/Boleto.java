package br.unitins.topicos1.model;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("boleto")
public class Boleto extends FormaDePagamento{
    private String nomeBanco;
    private LocalDateTime dataHoraGeracao;
    private LocalDateTime dataHoraLimitePag;
    private LocalDateTime dataHoraEfetivadoPagamento;
    private Double valorPago;

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
    public Double getValorPago() {
        return valorPago;
    }
    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }
}
