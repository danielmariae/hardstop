package br.unitins.topicos1.model;

import jakarta.persistence.Entity;

@Entity
public class ItemDaVenda extends DefaultEntity {
    
    private Double preco;
    private Long quantidade;
    
    public Double getPreco() {
        return preco;
    }
    public void setPreco(Double preco) {
        this.preco = preco;
    }
    public Long getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

}
