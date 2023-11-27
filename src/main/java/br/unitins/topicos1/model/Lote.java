package br.unitins.topicos1.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


   @Entity
   public class Lote extends DefaultEntity {
        private String lote;
        private LocalDateTime dataHoraChegadaLote;
        private LocalDateTime dataHoraUltimoVendido;
        private Integer quantidade;
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


    public Integer getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
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
    
}
  

