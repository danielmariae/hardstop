package br.unitins.topicos1.model;

import jakarta.persistence.Entity;

@Entity
public class Lote extends DefaultEntity {
    private String lote;

    public String getLote() {
        return lote;
    }
    public void setLote(String lote) {
        this.lote = lote;
    }
}
  

