package br.unitins.topicos1.repository;

import java.util.List;

import br.unitins.topicos1.model.TipoUsuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class TipoUsuarioRepository implements PanacheRepository<TipoUsuario> {
    public List<TipoUsuario> findByPerfil(Integer id){
        return find("perfil = ?1", id).list();
    }
}
