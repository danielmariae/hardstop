package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.utils.Fornecedor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class FornecedorRepository implements PanacheRepository<Fornecedor>{
        public List<Fornecedor> findByNomeRegistro(String nomeRegistro) {
            return find("UPPER(nomeRegistro) LIKE UPPER(?1) ", "%"+nomeRegistro+"%").list();
        }

        // public Fornecedor findByCNPJ(String cnpj){
        //     return find("cnpj = ?1", cnpj).singleResult();
        // }

        public Fornecedor findByCnpj(String cnpj) {
            if(cnpj.matches("([0-9]{2}[-./\\s][0-9]{3}[-./\\s][0-9]{3}[-./\\s][0-9]{4}[-./\\s][0-9]{2})|([0-9]{14})")) {
            String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
                try { 
                    return find("cnpj = ?1", cnpjLimpo).firstResult();
                } catch (NoResultException e) {
                    e.printStackTrace();
                    return null;  
                } 
            }
            try {
                return find("cnpj = ?1", cnpj).firstResult();
            } catch (NoResultException e) {
                e.printStackTrace();
                return null;  
            } 
        }
}
