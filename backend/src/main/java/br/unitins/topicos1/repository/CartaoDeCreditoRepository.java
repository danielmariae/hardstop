package br.unitins.topicos1.repository;

import br.unitins.topicos1.model.pagamento.CartaoDeCredito;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@ApplicationScoped
public class CartaoDeCreditoRepository implements PanacheRepository<CartaoDeCredito>{
    @Inject
    EntityManager entityManager;

    public CartaoDeCredito findByFormaPagamentoId(Long id){
        Query query = entityManager.createQuery("SELECT c FROM CartaoDeCredito c WHERE c.id = :id");
        query.setParameter("id", id);
        return (CartaoDeCredito) query.getSingleResult();
    }
}
