package br.unitins.topicos1.model.pagamento;

import java.time.LocalDateTime;

// import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
// @DiscriminatorValue("PIX") Caso eu tivesse escolhido a opção SINGLE_TABLE na classe pai ao invés de JOINED
@PrimaryKeyJoinColumn(name = "formaPagamento_id") // Cria uma chave estrangeira que precisa ter exatamente o mesmo valor do id da classe pai que contem o restante das informações
public class Pix extends FormaDePagamento{
    private String nomeCliente;
    private String nomeRecebedor;
    private String chaveRecebedor;
    private LocalDateTime dataHoraGeracao;
    private LocalDateTime dataHoraEfetivadoPagamento;
    private String nomeCidade;
    private String nomeArquivo;

    public String getNomeCidade() {
        return nomeCidade;
    }
    public void setNomeCidade(String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }
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

    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

}
