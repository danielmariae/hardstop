package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.Fornecedor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FornecedorRepository implements PanacheRepository<Fornecedor>{
        public List<Fornecedor> findByNomeRegistro(String nomeRegistro) {
            return find("UPPER(nomeRegistro) LIKE UPPER(?1) ", "%"+nomeRegistro+"%").list();
        }

        public Fornecedor findByCNPJ(String cnpj){
            return find("cnpj = ?1", cnpj).singleResult();
        } 
}
