package br.unitins.tp1.hardstop.repository;

import java.util.List;

import br.unitins.tp1.hardstop.model.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente>{
    public List<Cliente> findByNome(String nome) {
        return find("nome LIKE ?1", "%"+nome+"%").list();
    }
}
