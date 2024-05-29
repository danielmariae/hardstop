package br.unitins.topicos1.repository;

import br.unitins.topicos1.model.produto.Classificacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;



@ApplicationScoped
public class ClassificacaoRepository implements PanacheRepository<Classificacao> {
    public Classificacao findByName(String classificacao) {
        return find("UPPER(classificacao) LIKE UPPER(?1) ", "%" + classificacao + "%").firstResult();
    }
}