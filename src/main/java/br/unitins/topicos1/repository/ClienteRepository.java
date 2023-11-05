package br.unitins.topicos1.repository;

import br.unitins.topicos1.model.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

import java.util.List;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

  public List<Cliente> findByName(String nome) {
    try {
      return find("UPPER(nome) LIKE UPPER(?1) ", "%"+nome+"%").list();
    } catch (NoResultException e) {
          e.printStackTrace();
          return null;  
    }
    
  }

  public Cliente findByCpf(String cpf) {
    if(cpf.matches("[0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{2}")) {
      String parte1cpf, parte2cpf, parte3cpf, parte4cpf;
      parte1cpf = cpf.substring(0,3);
      parte2cpf = cpf.substring(4,7);
      parte3cpf = cpf.substring(8,11);
      parte4cpf = cpf.substring(12,14);
      try {
        return find("cpf", parte1cpf.concat(parte2cpf).concat(parte3cpf).concat(parte4cpf)).firstResult();
      } catch (NoResultException e) {
          e.printStackTrace();
          return null;  
    } 
    }
    try {
      return find("cpf = :cpf", Parameters.with("cpf", cpf)).firstResult();
    } catch (NoResultException e) {
          e.printStackTrace();
          return null;  
    } 
  }

 public Cliente findByLogin(String login) {
        try {
            return find("login = ?1 ", login).singleResult();
        } catch (NoResultException e) {
          e.printStackTrace();
          return null;  
        }
    }

    public Cliente findByLoginAndSenha(String login, String senha) {
        try {
            return find("login = ?1 AND senha = ?2 ", login, senha).singleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
        
    }

}
