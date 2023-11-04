package br.unitins.topicos1.model;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity
public class ItemDaVenda extends DefaultEntity {
    
    private Double preco;
    private Integer quantidade;

@ManyToOne
    private Produto produto;

    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    public Double getPreco() {
        return preco;
    }
    public void setPreco(Double preco) {
        this.preco = preco;
    }
    public Integer getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}
