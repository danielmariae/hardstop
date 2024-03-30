package br.unitins.topicos1.model;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity
public class ItemDaVenda extends DefaultEntity {
    
    private Double preco;
    private Integer quantidadeUnidades;
    private Double quantidadeNaoConvencional;
    private String unidadeDeMedida;

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


    /**
     * @return Integer return the quantidadeUnidades
     */
    public Integer getQuantidadeUnidades() {
        return quantidadeUnidades;
    }

    /**
     * @param quantidadeUnidades the quantidadeUnidades to set
     */
    public void setQuantidadeUnidades(Integer quantidadeUnidades) {
        this.quantidadeUnidades = quantidadeUnidades;
    }

    /**
     * @return Double return the quantidadeNaoConvencional
     */
    public Double getQuantidadeNaoConvencional() {
        return quantidadeNaoConvencional;
    }

    /**
     * @param quantidadeNaoConvencional the quantidadeNaoConvencional to set
     */
    public void setQuantidadeNaoConvencional(Double quantidadeNaoConvencional) {
        this.quantidadeNaoConvencional = quantidadeNaoConvencional;
    }


    /**
     * @return String return the unidadeDeMedida
     */
    public String getUnidadeDeMedida() {
        return unidadeDeMedida;
    }

    /**
     * @param unidadeDeMedida the unidadeDeMedida to set
     */
    public void setUnidadeDeMedida(String unidadeDeMedida) {
        this.unidadeDeMedida = unidadeDeMedida;
    }

}
