package br.unitins.topicos1.model;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CARTAO_DE_CREDITO")
public class CartaoDeCredito extends FormaDePagamento{

    private String numeroCartao;
    private Integer mesValidade;
    private Integer anoValidade;
    private Integer codSeguranca;
    private Double valorPago;
    private LocalDateTime dataHoraPagamento;
    private LocalDateTime dataHoraEfetivadoPagamento;
    
    public LocalDateTime getDataHoraEfetivadoPagamento() {
        return dataHoraEfetivadoPagamento;
    }
    public void setDataHoraEfetivadoPagamento(LocalDateTime dataHoraEfetivadoPagamento) {
        this.dataHoraEfetivadoPagamento = dataHoraEfetivadoPagamento;
    }
    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }
    public void setDataHoraPagamento(LocalDateTime dataHoraPagamento) {
        this.dataHoraPagamento = dataHoraPagamento;
    }
    public String getNumeroCartao() {
        return numeroCartao;
    }
    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }
    public Integer getMesValidade() {
        return mesValidade;
    }
    public void setMesValidade(Integer mesValidade) {
        this.mesValidade = mesValidade;
    }
    public Integer getAnoValidade() {
        return anoValidade;
    }
    public void setAnoValidade(Integer anoValidade) {
        this.anoValidade = anoValidade;
    }
    public Integer getCodSeguranca() {
        return codSeguranca;
    }
    public void setCodSeguranca(Integer codSeguranca) {
        this.codSeguranca = codSeguranca;
    }
    public Double getValorPago() {
        return valorPago;
    }
    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }
    
}
