package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.pedido.Pedido;
import br.unitins.topicos1.model.pedido.Status;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido>{

    @Inject
    EntityManager entityManager;
    
    public List<Pedido> findAll(String login) {
        return find("cliente.login = ?1", login).list();
    }
    
    public List<Pedido> findAll(Long idUsuario) {
        return find("cliente.id = ?1", idUsuario).list();
    }

    // public List<Pedido> findAllStatus(Long idStatus) {
    //     String queryString = "SELECT p FROM Pedido p " +
    //                          "JOIN p.statusDoPedido s " +
    //                          "WHERE s.dataHora = (" +
    //                          "SELECT MAX(s1.dataHora) FROM StatusDoPedido s1 " +
    //                          "JOIN Pedido p1 ON s1 MEMBER OF p1.statusDoPedido " +
    //                          "WHERE p1.id = p.id" +
    //                          ") " +
    //                          "AND s.status.id = :idStatus";

    //     TypedQuery<Pedido> query = entityManager.createQuery(queryString, Pedido.class);
    //     query.setParameter("idStatus", idStatus);
    //     return query.getResultList();
    // }

    // public List<Pedido> findAllStatus(Long idStatus) {
    //     String queryString = "SELECT p FROM Pedido p " +
    //                           "JOIN p.statusDoPedido s " +  // Join with StatusDoPedido
    //                           "WHERE s.status = :idStatus " +  // Filter by status.id
    //                           "AND s.dataHora = ( " +  // Find MAX(dataHora) within subquery
    //                           "  SELECT MAX(sp1.dataHora) FROM StatusDoPedido sp1 " +
    //                           "  WHERE sp1.id_pedido = p.id " +  // Corrected reference
    //                           ")";
      
    //     TypedQuery<Pedido> query = entityManager.createQuery(queryString, Pedido.class);
    //     query.setParameter("idStatus", idStatus);
    //     return query.getResultList();
    //   }
      
      
    public List<Pedido> findAllStatus(Long idStatus) {
        Status status = Status.valueOf(Integer.valueOf(Long.toString(idStatus)));
            String queryString = "select p from Pedido p, StatusDoPedido sp where sp.status = :status AND sp.pedido = p AND NOT EXISTS (select sp1.pedido from StatusDoPedido sp1 where sp1.status > :status AND sp1.pedido = p)";

    
            TypedQuery<Pedido> query = entityManager.createQuery(queryString, Pedido.class);
            query.setParameter("status", status);
            return query.getResultList();
        }
      
      
      
      
      
      
}
