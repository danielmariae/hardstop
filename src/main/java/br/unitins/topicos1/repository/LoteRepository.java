package br.unitins.topicos1.repository;

import br.unitins.topicos1.model.Lote;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LoteRepository implements PanacheRepository<Lote> {
    public List<Lote> findByName(String lote) {
        return find("UPPER(lote) LIKE UPPER(?1) ", "%" + lote + "%").list();
    }

    public List<Lote> findAll(Long idProduto) {
        return find("produto.id = ?1", idProduto).list();
    }
}
