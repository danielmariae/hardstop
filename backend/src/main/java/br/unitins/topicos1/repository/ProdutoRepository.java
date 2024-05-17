package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {
    public List<Produto> findByName(String nome, int page, int pageSize) {
        return find("UPPER(nome) LIKE UPPER(?1) ", "%"+nome+"%").page(page, pageSize).list();
    }

    public List<Produto> findByClassificacao(Long idClassificacao) {
        return find("classificacao_id = ?1", idClassificacao).list();
    }

    public Produto findByCodigoBarras(String codigoBarras){
        try {
            return find("codigo_barras = ?1", codigoBarras).singleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

}
