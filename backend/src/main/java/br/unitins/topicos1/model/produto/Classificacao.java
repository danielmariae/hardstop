package br.unitins.topicos1.model.produto;

import br.unitins.topicos1.model.utils.DefaultEntity;
import jakarta.persistence.Entity;

@Entity
public class Classificacao extends DefaultEntity {
    private String nome;

    public Classificacao() {
    }
    
    public Classificacao(Long id, String nome) {
        this.setId(id);
        this.setNome(nome);
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

}  
