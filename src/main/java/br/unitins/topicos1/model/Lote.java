package br.unitins.topicos1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


   @Entity
   public class Lote extends DefaultEntity {
       private String lote;

       private LocalDateTime dataHoraChegadaLote;
       private LocalDateTime dataHoraUltimoVendido;

   
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
}
  

