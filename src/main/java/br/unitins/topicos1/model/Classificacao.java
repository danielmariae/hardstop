package br.unitins.topicos1.model;

import br.unitins.topicos1.dto.ClassificacaoDTO;
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
    public static Classificacao valueOf(ClassificacaoDTO classificacao) {
        return new Classificacao(classificacao.id(), classificacao.nome());
    }
}  
