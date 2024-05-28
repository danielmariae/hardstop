package br.unitins.topicos1.model.pagamento;

import java.time.LocalDateTime;

// import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
// @DiscriminatorValue("CARTAO_DE_CREDITO") Caso eu tivesse escolhido a opção SINGLE_TABLE na classe pai ao invés de JOINED
@PrimaryKeyJoinColumn(name = "formaPagamento_id") // Cria uma chave estrangeira que precisa ter exatamente o mesmo valor do id da classe pai que contem o restante das informações
public class CartaoDeCredito extends FormaDePagamento{

    private String numeroCartao;
    private Integer mesValidade;
    private Integer anoValidade;
    private Integer codSeguranca;
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
}
