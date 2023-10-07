package br.unitins.topicos1.model;

import jakarta.persistence.Entity;

@Entity
public class Lote extends DefaultEntity {
    private String lote;

    public Lote() {}
    
    public Lote(Long id, String lote) {
        setId(id);
        setLote(lote);
    }
    public String getLote() {
        return lote;
    }
    public void setLote(String lote) {
        this.lote = lote;
    }
    public static Lote valueOf(Lote lote){
        return new Lote(lote.getId(), lote.getLote());
    }
}

