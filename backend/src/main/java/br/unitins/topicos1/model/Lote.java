package br.unitins.topicos1.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


   @Entity
   public class Lote extends DefaultEntity {
        private String lote;
        private LocalDateTime dataHoraChegadaLote;
        private LocalDateTime dataHoraUltimoVendido;
        private Integer quantidadeUnidades;
        private Double quantidadeNaoConvencional;
        private String unidadeDeMedida;
        private Double custoCompra;
        private Double valorVenda;

        @ManyToOne
        private Produto produto;

        @ManyToOne
        private Fornecedor fornecedor;

        public LocalDateTime getDataHoraChegadaLote() {
            return dataHoraChegadaLote;
        }
        public void setDataHoraChegadaLote(LocalDateTime dataHoraChegadaLote) {
            this.dataHoraChegadaLote = dataHoraChegadaLote;
        }
        public LocalDateTime getDataHoraUltimoVendido() {
            return dataHoraUltimoVendido;
        }
        public void setDataHoraUltimoVendido(LocalDateTime dataHoraUltimoVendido) {
            this.dataHoraUltimoVendido = dataHoraUltimoVendido;
        }
        public Fornecedor getFornecedor() {
            return fornecedor;
        }
        public void setFornecedor(Fornecedor fornecedor) {
            this.fornecedor = fornecedor;
        }
        public String getLote() {
            return lote;
        }
        public void setLote(String lote) {
            this.lote = lote;
        }

        public Produto getProduto() {
            return this.produto;
        }

        public void setProduto(Produto produto) {
            this.produto = produto;
        }

    public Double getCustoCompra() {
        return this.custoCompra;
    }

    public void setCustoCompra(Double custoCompra) {
        this.custoCompra = custoCompra;
    }
       
    public Double getValorVenda() {
        return this.valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
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
  

