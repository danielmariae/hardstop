package br.unitins.topicos1.model;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PIX")
public class Pix extends FormaDePagamento{
    private String nomeCliente;
    private String nomeRecebedor;
    private String chaveRecebedor;
    private Double valorPago;
    private LocalDateTime dataHoraGeracao;
    private LocalDateTime dataHoraEfetivadoPagamento;

    public LocalDateTime getDataHoraGeracao() {
        return dataHoraGeracao;
    }
    public void setDataHoraGeracao(LocalDateTime dataHoraGeracao) {
        this.dataHoraGeracao = dataHoraGeracao;
    }
    public LocalDateTime getDataHoraEfetivadoPagamento() {
        return dataHoraEfetivadoPagamento;
    }
    public void setDataHoraEfetivadoPagamento(LocalDateTime dataHoraEfetivadoPagamento) {
        this.dataHoraEfetivadoPagamento = dataHoraEfetivadoPagamento;
    }
    public String getNomeCliente() {
        return nomeCliente;
    }
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    public String getNomeRecebedor() {
        return nomeRecebedor;
    }
    public void setNomeRecebedor(String nomeRecebedor) {
        this.nomeRecebedor = nomeRecebedor;
    }
    public String getChaveRecebedor() {
        return chaveRecebedor;
    }
    public void setChaveRecebedor(String chaveRecebedor) {
        this.chaveRecebedor = chaveRecebedor;
    }
    public Double getValorPago() {
        return valorPago;
    }
    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }
    
}
