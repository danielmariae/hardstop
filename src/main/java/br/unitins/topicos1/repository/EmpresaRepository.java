package br.unitins.topicos1.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import br.unitins.topicos1.model.Empresa;

@ApplicationScoped
public class EmpresaRepository implements PanacheRepository<Empresa>{

    public Empresa findByCpf(String cpf) {
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

public Empresa findByCnpj(String cnpj) {
    if(cnpj.matches("[0-9]{2}[-./\s][0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{4}[-./\s][0-9]{2}")) {
      String parte1cnpj, parte2cnpj, parte3cnpj, parte4cnpj, parte5cnpj;
      parte1cnpj = cnpj.substring(0,2);
      parte2cnpj = cnpj.substring(3,6);
      parte3cnpj = cnpj.substring(7,10);
      parte4cnpj = cnpj.substring(11,15);
      parte5cnpj = cnpj.substring(16,18);
      return find("cnpj", parte1cnpj.concat(parte2cnpj).concat(parte3cnpj).concat(parte4cnpj).concat(parte5cnpj)).firstResult();
    }
    return find("cnpj = :cnpj", Parameters.with("cnpj", cnpj)).firstResult();
}

public List<Empresa> findByNomeReal(String nomeReal) {
    return find("UPPER(nomeReal) LIKE UPPER(?1) ", "%"+nomeReal+"%").list();
}

public List<Empresa> findByNomeFantasia(String nomeFantasia) {
    return find("UPPER(nomeFantasia) LIKE UPPER(?1) ", "%"+nomeFantasia+"%").list();
}

public List<Empresa> findByNomeResponsavel(String nomeResponsavel) {
    return find("UPPER(nomeResponsavel) LIKE UPPER(?1) ", "%"+nomeResponsavel+"%").list();
}

}
