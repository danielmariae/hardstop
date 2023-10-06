package br.unitins.topicos1.repository;

import br.unitins.topicos1.model.Funcionario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class FuncionarioRepository implements PanacheRepository<Funcionario> {

  public List<Funcionario> findByName(String nome) {
    return find("UPPER(nome) LIKE UPPER(?1) ", "%"+nome+"%").list();
  }

  public Funcionario findByCpf(String cpf) {
    if(cpf.matches("[0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{2}")) {
      String parte1cpf, parte2cpf, parte3cpf, parte4cpf;
      parte1cpf = cpf.substring(0,3);
      parte2cpf = cpf.substring(4,7);
      parte3cpf = cpf.substring(8,11);
      parte4cpf = cpf.substring(12,14);
      return find("cpf", parte1cpf.concat(parte2cpf).concat(parte3cpf).concat(parte4cpf)).firstResult();
    }
    return find("cpf = :cpf", Parameters.with("cpf", cpf)).firstResult();
  }
}

