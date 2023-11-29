package br.unitins.topicos1.model;

 import java.util.List;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import jakarta.persistence.Entity;

@Entity
public class Produto extends DefaultEntity {

    private String nome;
    private String descricao;
    private String codigoBarras;
    private String marca;
    private Double altura;
    private Double largura;
    private Double comprimento;
    private Double peso;
    private Double valorVenda;
    private Integer quantidade;
    private List<String> nomeImagem;

    @OneToOne
    private Lote loteAtual;

    @ManyToOne
    private Classificacao classificacao;

    @Version
    private int version;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getLargura() {
        return largura;
    }

    public void setLargura(Double largura) {
        this.largura = largura;
    }

    public Double getComprimento() {
        return comprimento;
    }

    public void setComprimento(Double comprimento) {
        this.comprimento = comprimento;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public List<String> getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(List<String> nomeImagem) {
        this.nomeImagem = nomeImagem;
    }

    public Classificacao getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }
   

    public Lote getLoteAtual() {
        return this.loteAtual;
    }

    public void setLoteAtual(Lote loteAtual) {
        this.loteAtual = loteAtual;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
