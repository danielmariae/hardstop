package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.utils.Logistica;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class LogisticaRepository implements PanacheRepository<Logistica>{
        public List<Logistica> findByNomeRegistro(String nomeRegistro) {
            return find("UPPER(nomeRegistro) LIKE UPPER(?1) ", "%"+nomeRegistro+"%").list();
        }

        public List<Logistica> findByNomeFantasia(String nomeFantasia){
            try {
                return find("UPPER(nomeFantasia) LIKE UPPER(?1) ", "%"+nomeFantasia+"%").list();
            } catch (NoResultException e) {
                e.printStackTrace();
                return null;
            }
        } 
}
