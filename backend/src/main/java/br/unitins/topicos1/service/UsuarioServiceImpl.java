package br.unitins.topicos1.service;

import br.unitins.topicos1.Formatadores.UsuarioFormatador;
import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.UsuarioDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.dto.DesejoResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.PatchCpfDTO;
import br.unitins.topicos1.dto.PatchEmailDTO;
import br.unitins.topicos1.dto.PatchLoginDTO;
import br.unitins.topicos1.dto.PatchNomeDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.dto.TipoUsuarioDTO;
import br.unitins.topicos1.model.*;
import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.repository.UsuarioRepository;
import br.unitins.topicos1.repository.EnderecoRepository;
import br.unitins.topicos1.repository.PedidoRepository;
import br.unitins.topicos1.validation.ValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jrimum.domkee.pessoa.CEP;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

  @Inject
  UsuarioRepository repository;

  @Inject
  PedidoRepository repositoryPedido;

  @Inject
  HashService hashservice;

  @Inject
  EnderecoRepository repositoryEndereco;


  @Override
  public UsuarioResponseDTO findByIdUsuario(Long id) {
    Usuario usuario = repository.findById(id);
    if (usuario == null)
            throw new ValidationException("ID", "id inexistente");
    return UsuarioResponseDTO.valueOf(usuario);
  }

  @Override
  public List<UsuarioResponseDTO> findByNameUsuario(String name) {
    return repository
      .findByName(name)
      .stream()
      .map(c -> UsuarioResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  public UsuarioResponseDTO findByCpfUsuario(String cpf) {
    Usuario usuario = repository.findByCpf(cpf);
    if (usuario == null) 
            throw new ValidationException("CPF", "cpf inexistente");
    return UsuarioResponseDTO.valueOf(usuario);
  }

  @Override
  public List<UsuarioResponseDTO> findByAllUsuario() {
    return repository
      .listAll()
      .stream()
      .map(c -> UsuarioResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  @Transactional
  // Método para deletar o usuario junto com todos os seus relacionamentos.
  public void deleteUsuario(Long id) {

// Verifica o id do usuario. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
if(!verificaUsuario1(id)) {
  throw new GeneralErrorException("400", "Bad Resquest", "UsuarioServiceImpl(delete)", "id do usuário é nulo ou tem valor inferior a 1.");
}

// Verifica o usuario. Caso o id inexista no banco de dados, o sistema não realiza a operação.
Usuario usuario = repository.findById(id);
if(!verificaUsuario2(usuario)) {
  throw new GeneralErrorException("400", "Bad Resquest", "UsuarioServiceImpl(delete)", "id do usuário não existe no banco de dados.");
}

// Desconecta o usuario da lista de desejos de produtos.
usuario.getListaProduto().clear();

  // podeDeletar verifica se todos os pedidos foram finalizados, retornando true ou false.
      if(!podeDeletar(repositoryPedido.findAll(id))) {
        throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(delete)", "Nada foi excluído porque este usuario possui pedidos ainda não finalizados!");
      }

        for(Pedido pedido : repositoryPedido.findAll(id)) {
          try {
            repositoryPedido.delete(pedido);
          } catch (Exception e) {
            throw new GeneralErrorException("500", "Server Error", "UsuarioServiceImpl(delete)", "Erro ao tentar apagar os pedidos deste usuario no banco de dados!");
          }
          
        }
        try {
          repository.delete(usuario);
        } catch (Exception e) {
          throw new GeneralErrorException("500", "Server Error", "UsuarioServiceImpl(delete)", "Erro ao tentar apagar o usuario no banco de dados!");
        }
  }

  @Override
  @Transactional
  public UsuarioResponseDTO updateUsuario(UsuarioDTO usu, Long id) {
    Usuario usuario = repository.findById(id);

    usuario.setNome(usu.nome());
    usuario.setDataNascimento(
      UsuarioFormatador.validaDataNascimento(usu.dataNascimento())
    );
    usuario.setCpf(UsuarioFormatador.validaCpf(usu.cpf()));
    usuario.setSexo(usu.sexo());
    usuario.setLogin(usu.login());
    usuario.setSenha(hashservice.getHashSenha(usu.senha()));
    usuario.setEmail(usu.email());

    int ia = 0;
    int ja = 0;
    for(TipoUsuario tipo1 : usuario.getTipoUsuario()){
      ia++;
      ja=0;
      for(TipoUsuarioDTO tipo : usu.tiposUsuario()){
        ja++;
        if(ia==ja){
          tipo1.setDataCriacao(tipo.dataCriacao());
          tipo1.setPerfil(Perfil.valueOf(tipo.idTipoPerfil()));
        }
      }
    }

    int i = 0;
    int j = 0;

    for (Telefone tele1 : usuario.getListaTelefone()) {
      i++;
      j = 0;
      for (TelefoneDTO tele : usu.listaTelefone()) {
        j++;
        if (i == j) {
          tele1.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
          tele1.setDdd(tele.ddd());
          tele1.setNumeroTelefone(
            TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone())
          );
        }
      }
    }

    int ie = 0;
    int je = 0;

    for (Endereco endereco : usuario.getListaEndereco()) {
      ie++;
      je = 0;
      for (EnderecoDTO end1 : usu.listaEndereco()) {
        je++;
        if (ie == je) {
          endereco.setNome(end1.nome());
          endereco.setLogradouro(end1.logradouro());
          endereco.setNumeroLote(end1.numeroLote());
          endereco.setBairro(end1.bairro());
          endereco.setComplemento(end1.complemento());
          endereco.setCep(new CEP(EnderecoFormatador.validaCep(end1.cep().getCep())));
          endereco.setLocalidade(end1.localidade());
          endereco.setUF(end1.uf());
          endereco.setPais(end1.pais());
        }
      }
    }

    //repository.persist(usuario);
    return UsuarioResponseDTO.valueOf(usuario);
  }

  @Override
  @Transactional
  public String updateNome(PatchNomeDTO nome, Long id) {
    Usuario usuario = repository.findById(id);
    usuario.setNome(nome.nome());
    return "Nome alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateCpf(PatchCpfDTO cpf, Long id) {
    Usuario usuario = repository.findById(id);
    verificaCpf(UsuarioFormatador.validaCpf(cpf.cpf()));
    usuario.setCpf(UsuarioFormatador.validaCpf(cpf.cpf()));
    return "Cpf alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateLogin(PatchLoginDTO login, Long id) {
    Usuario usuario = repository.findById(id);
    verificaLogin(login.login());
    usuario.setLogin(login.login());
    return "Login alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateEmail(PatchEmailDTO email, Long id) {
    Usuario usuario = repository.findById(id);
    usuario.setEmail(email.email());
    return "Email alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateSenhaUsuario(PatchSenhaDTO senha, Long id) {
    Usuario usuario = repository.findById(id);

    if(hashservice.getHashSenha(senha.senhaAntiga()).equals(usuario.getSenha())) {
    usuario.setSenha(hashservice.getHashSenha(senha.senhaAtual()));
    //repository.persist(usuario);
    return "Senha alterada com sucesso.";
    } else {
     throw new ValidationException("updateSenha", "Favor inserir a senha antiga correta."); 
    }
  }


  @Override
  @Transactional
  public UsuarioResponseDTO insertTelefoneUsuario(TelefoneDTO tel, Long id) {
    Usuario usuario = repository.findById(id);
    
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
        telefone.setDdd(tel.ddd());
        telefone.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
        usuario.getListaTelefone().add(telefone);
    repository.persist(usuario);
    return UsuarioResponseDTO.valueOf(usuario);
  }


  @Override
  @Transactional
  public UsuarioResponseDTO updateTelefoneUsuario(TelefonePatchDTO tel, Long id) {
    Usuario usuario = repository.findById(id);

    if (
      usuario.getListaTelefone() != null ||
      !usuario.getListaTelefone().isEmpty()
    ) {

      Boolean chave = true;
      
      for (Telefone tele1 : usuario.getListaTelefone()) {
          if (tele1.getId() == tel.id()) {
            tele1.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
            tele1.setDdd(tel.ddd());
            tele1.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
            chave = false;
          }   
      }
      if(chave) {
        throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(updateTelefoneUsuario)", "O id fornecido não corresponde a um id de telefone cadastrado para este usuario");
      }
    } else {
      throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(updateTelefoneUsuario)", "Este usuário não possui nenhum telefone cadastrado.");
    }

    //repository.persist(usuario);
    return UsuarioResponseDTO.valueOf(usuario);
  }

  @Override
  @Transactional
  public UsuarioResponseDTO updateEnderecoUsuario(EnderecoPatchDTO end, Long id) {
    Usuario usuario = repository.findById(id);

    if (
      usuario.getListaEndereco() != null ||
      !usuario.getListaEndereco().isEmpty()
    ) {

      Boolean chave = true;

      for (Endereco endereco : usuario.getListaEndereco()) {
        
          if (endereco.getId() == end.id()) {
            endereco.setNome(end.nome());
            endereco.setLogradouro(end.logradouro());
            endereco.setNumeroLote(end.numeroLote());
            endereco.setBairro(end.bairro());
            endereco.setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
            endereco.setComplemento(end.complemento());
            endereco.setLocalidade(end.localidade());
            endereco.setUF(end.uf());
            endereco.setPais(end.pais());
            chave = false;
          }

      }

      if(chave) {
        throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(updateEndereçoUsuario)", "O id fornecido não corresponde a um id de endereço cadastrado para este usuario");
      }
      
    } else {
      throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(updateEnderecoUsuario)", "Este usuário não possui nenhum endereço cadastrado.");
    }
    //repository.persist(usuario);
    return UsuarioResponseDTO.valueOf(usuario);
  }


  @Override
  @Transactional
  public UsuarioResponseDTO insertEnderecoUsuario(EnderecoDTO end, Long id) {
    Usuario usuario = repository.findById(id);

     Endereco endereco = new Endereco();
      endereco.setNome(end.nome());
      endereco.setLogradouro(end.logradouro());
      endereco.setNumeroLote(end.numeroLote());
      endereco.setBairro(end.bairro());
      endereco.setComplemento(end.complemento());
      endereco.setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
      endereco.setLocalidade(end.localidade());
      endereco.setUF(end.uf());
      endereco.setPais(end.pais());

    if(usuario.getListaEndereco() == null)
      usuario.setListaEndereco(new ArrayList<Endereco>());

    repositoryEndereco.persist(endereco);

    usuario.getListaEndereco().add(endereco);
    repository.persist(usuario);
    return UsuarioResponseDTO.valueOf(usuario);
  }

  @Override
  @Transactional
  public UsuarioResponseDTO insertUsuario(UsuarioDTO dto) {

    Usuario usuario = null;

    try {
      usuario = new Usuario();
    } catch (Exception e) {
      throw new GeneralErrorException("500", "Internal Server Error", "UsuarioServiceImpl(insert)", "Não consegui alocar memória para o novo Usuario. Tente novamente mais tarde! " +  e.getCause());
    }

    usuario.setNome(dto.nome());
    usuario.setDataNascimento(
    UsuarioFormatador.validaDataNascimento(dto.dataNascimento()));
    verificaCpf(UsuarioFormatador.validaCpf(dto.cpf()));
    usuario.setCpf(UsuarioFormatador.validaCpf(dto.cpf()));
    usuario.setSexo(dto.sexo());
    verificaLogin(dto.login());
    usuario.setLogin(dto.login());
    usuario.setSenha(hashservice.getHashSenha(dto.senha()));
    usuario.setEmail(dto.email());
    
    if(dto.tiposUsuario() != null &&  !dto.tiposUsuario().isEmpty()){
      try {
        usuario.setTipoUsuario(new ArrayList<TipoUsuario>()); 
      } catch (Exception e) {
        throw new GeneralErrorException("500", "Internal Server Error", "UsuarioServiceImpl(insert)", "Tente novamente mais tarde.");
      }
      for (TipoUsuarioDTO tipo : dto.tiposUsuario()){
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setDataCriacao(LocalDateTime.now());
        tipoUsuario.setPerfil(Perfil.valueOf(tipo.idTipoPerfil()));
        usuario.getTipoUsuario().add(tipoUsuario);
      }
    }

    if (dto.listaTelefone() != null && !dto.listaTelefone().isEmpty()) {
      try {
        usuario.setListaTelefone(new ArrayList<Telefone>());
      } catch (Exception e) {
        throw new GeneralErrorException("500", "Internal Server Error", "UsuarioServiceImpl(insert)", "Não consegui alocar memória para a lista telefônica do novo Usuario. Tente novamente mais tarde! " +  e.getCause());
      }
      
      for (TelefoneDTO tel : dto.listaTelefone()) {
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
        telefone.setDdd(tel.ddd());
        telefone.setNumeroTelefone(
        TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
        usuario.getListaTelefone().add(telefone);
      }
    }

    if (dto.listaEndereco() != null && !dto.listaEndereco().isEmpty()) {
      try {
        usuario.setListaEndereco(new ArrayList<Endereco>());
      } catch (Exception e) {
        throw new GeneralErrorException("500", "Internal Server Error", "UsuarioServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços do novo Usuario. Tente novamente mais tarde! " +  e.getCause());
      }
      
      for (EnderecoDTO end : dto.listaEndereco()) {
        Endereco endereco = new Endereco();
        endereco.setNome(end.nome());
        endereco.setLogradouro(end.logradouro());
        endereco.setNumeroLote(end.numeroLote());
        endereco.setBairro(end.bairro());
        endereco.setComplemento(end.complemento());
        endereco.setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
        endereco.setLocalidade(end.localidade());
        endereco.setUF(end.uf());
        endereco.setPais(end.pais());
        usuario.getListaEndereco().add(endereco);
      }
    }

    try {
      repository.persist(usuario);
    } catch (Exception e) {
      throw new GeneralErrorException("500", "Internal Server Error", "UsuarioServiceImpl(insert)", "Não consegui persistir os dados do usuario no repositório " + e.getCause());
    }
    
    return UsuarioResponseDTO.valueOf(usuario);
  }

  public List<DesejoResponseDTO> findListaDesejosUsuario(Long id) {

     return repository
      .findById(id)
      .getListaProduto()
      .stream()
      .map(p -> DesejoResponseDTO.valueOf(p))
      .toList();
  }


  @Override
    public UsuarioResponseDTO findByLoginAndSenha(String login, String senha) {
      Usuario usuario = repository.findByLoginAndSenha(login, senha);
      if(!verificaUsuario2(usuario)) {
        throw new ValidationException("login", "Login ou senha inválido");
      }
      return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public UsuarioResponseDTO findByLogin(String login) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) 
            throw new ValidationException("login", "Login inválido");
        
        return UsuarioResponseDTO.valueOf(usuario);
    }


// Métodos de validação

 private void verificaLogin(String login) {
  if(repository.findByLogin(login) != null) {
    throw new ValidationException("login", "Login já existe no sistema. Favor escolher outro.");
  }
 }

private void verificaCpf(String cpf) {
  if(repository.findByCpf(cpf) != null) {
    throw new ValidationException("cpf", "Este cpf já existe no sistema. Usuário já está cadastrado no sistema.");
  }
}

 private boolean podeDeletar(List<Pedido> listaPedidos) {

    // Dá permissão para deletar o usuario que nunca fez um pedido
        if(listaPedidos == null || listaPedidos.isEmpty()) {
            return true;
        }

    // Todos os pedidos feitos pelo usuario já foram finalizados (Status.getId() == 5) ou tiveram pagamento não autorizado (Status.getId() == 1)? 
        Integer chaveDelecao = 0;
        for(Pedido pedido : listaPedidos) {
            for(StatusDoPedido statusPedido : pedido.getStatusDoPedido()) {
                if(statusPedido.getStatus().getId() == 5 || statusPedido.getStatus().getId() == 1) {
                    chaveDelecao++;
                }
            }
        }

  // Caso a igualdade seja verdadeira, significa que todos os pedidos do usuario foram finalizados ou tiverem pagamento não autorizado. Deste modo, poderemos deletar o usuario do banco de dados junto com todos os seus endereços.
        if(chaveDelecao == listaPedidos.size()) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean verificaUsuario1(Long id) {

      if(id == null) {
          return false;
      }
    
      if(id < 1) {
          return false;
      }
    
      return true;
    }
    
    private Boolean verificaUsuario2(Usuario usuario) {
    
      if(usuario == null) {
          return false;
      } else {
          return true;
      }
    }

}
