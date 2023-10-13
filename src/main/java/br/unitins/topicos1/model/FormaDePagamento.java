package br.unitins.topicos1.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name = "modalidade")
public class FormaDePagamento extends DefaultEntity {
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
}
