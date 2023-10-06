package br.unitins.topicos1.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;

public class Produto extends DefaultEntity {
    private String nome;
    private String descricao;
    private String codigoBarras;
    private String marca;
    private Double altura;
    private Double largura;
    private Double comprimento;
    private Double peso;
    private Double custoCompra;
    private Double valorVenda;
    private Long quantidade;

    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
        name = "fornecedor_produto",
        joinColumns = @JoinColumn(name = "id_fornecedor"),
        inverseJoinColumns = @JoinColumn(name = "id_produto")
    )
    private List<Fornecedor> fornecedor;
    
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
    public Double getCustoCompra() {
        return custoCompra;
    }
    public void setCustoCompra(Double custoCompra) {
        this.custoCompra = custoCompra;
    }
    public Double getValorVenda() {
        return valorVenda;
    }
    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }
    public Long getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}
