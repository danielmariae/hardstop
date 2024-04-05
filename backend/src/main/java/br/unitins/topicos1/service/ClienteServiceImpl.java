package br.unitins.topicos1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jrimum.domkee.pessoa.CEP;

import br.unitins.topicos1.Formatadores.ClienteFormatador;
import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.DesejoResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.PatchCpfDTO;
import br.unitins.topicos1.dto.PatchEmailDTO;
import br.unitins.topicos1.dto.PatchLoginDTO;
import br.unitins.topicos1.dto.PatchNomeDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Pedido;
import br.unitins.topicos1.model.StatusDoPedido;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.TipoTelefone;
import br.unitins.topicos1.repository.ClienteRepository;
import br.unitins.topicos1.repository.EnderecoRepository;
import br.unitins.topicos1.repository.PedidoRepository;
import br.unitins.topicos1.validation.ValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ClienteServiceImpl implements ClienteService {

  @Inject
  ClienteRepository repository;

  @Inject
  PedidoRepository repositoryPedido;

  @Inject
  HashService hashservice;

  @Inject
  EnderecoRepository repositoryEndereco;


  @Override
  public ClienteResponseDTO findByIdCliente(Long id) {
    Cliente cliente = repository.findById(id);
    if (cliente == null) 
            throw new ValidationException("ID", "id inexistente");
    return ClienteResponseDTO.valueOf(cliente);
  }

  @Override
  public List<ClienteResponseDTO> findByNameCliente(String name) {
    return repository
      .findByName(name)
      .stream()
      .map(c -> ClienteResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  public ClienteResponseDTO findByCpfCliente(String cpf) {
    Cliente cliente = repository.findByCpf(cpf);
    if (cliente == null) 
            throw new ValidationException("CPF", "cpf inexistente");
    return ClienteResponseDTO.valueOf(cliente);
  }

  @Override
  public List<ClienteResponseDTO> findByAllCliente() {
    return repository
      .listAll()
      .stream()
      .map(c -> ClienteResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  @Transactional
  // Método para deletar o cliente junto com todos os seus relacionamentos.
  public void deleteCliente(Long id) {

// Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
if(!verificaUsuario1(id)) {
  throw new GeneralErrorException("400", "Bad Resquest", "ClienteServiceImpl(delete)", "id do usuário é nulo ou tem valor inferior a 1.");
}

// Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
Cliente cliente = repository.findById(id);
if(!verificaUsuario2(cliente)) {
  throw new GeneralErrorException("400", "Bad Resquest", "ClienteServiceImpl(delete)", "id do usuário não existe no banco de dados.");
}

// Desconecta o cliente da lista de desejos de produtos.
cliente.getListaProduto().clear();

  // podeDeletar verifica se todos os pedidos foram finalizados, retornando true ou false.
      if(!podeDeletar(repositoryPedido.findAll(id))) {
        throw new GeneralErrorException("400", "Bad Request", "ClienteServiceImpl(delete)", "Nada foi excluído porque este cliente possui pedidos ainda não finalizados!");
      }

        for(Pedido pedido : repositoryPedido.findAll(id)) {
          try {
            repositoryPedido.delete(pedido);
          } catch (Exception e) {
            throw new GeneralErrorException("500", "Server Error", "ClienteServiceImpl(delete)", "Erro ao tentar apagar os pedidos deste cliente no banco de dados!");
          }
          
        }
        try {
          repository.delete(cliente);
        } catch (Exception e) {
          throw new GeneralErrorException("500", "Server Error", "ClienteServiceImpl(delete)", "Erro ao tentar apagar o cliente no banco de dados!");
        }
  }

  @Override
  @Transactional
  public ClienteResponseDTO updateCliente(ClienteDTO clt, Long id) {
    Cliente cliente = repository.findById(id);

    cliente.setNome(clt.nome());
    cliente.setDataNascimento(
      ClienteFormatador.formataDataNascimento(clt.dataNascimento())
    );
    cliente.setCpf(ClienteFormatador.formataCpf(clt.cpf()));
    cliente.setSexo(clt.sexo());
    cliente.setLogin(clt.login());
    cliente.setSenha(hashservice.getHashSenha(clt.senha()));
    cliente.setEmail(clt.email());

    int i = 0;
    int j = 0;

    for (Telefone tele1 : cliente.getListaTelefone()) {
      i++;
      j = 0;
      for (TelefoneDTO tele : clt.listaTelefone()) {
        j++;
        if (i == j) {
          tele1.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
          tele1.setDdd(tele.ddd());
          tele1.setNumeroTelefone(
            TelefoneFormatador.formataNumeroTelefone(tele.numeroTelefone())
          );
        }
      }
    }

    int ie = 0;
    int je = 0;

    for (Endereco endereco : cliente.getListaEndereco()) {
      ie++;
      je = 0;
      for (EnderecoDTO end1 : clt.listaEndereco()) {
        je++;
        if (ie == je) {
          endereco.setNome(end1.nome());
          endereco.setLogradouro(end1.logradouro());
          endereco.setNumeroLote(end1.numeroLote());
          endereco.setBairro(end1.bairro());
          endereco.setComplemento(end1.complemento());
          endereco.setCep(new CEP(EnderecoFormatador.formataCep(end1.cep().getCep())));
          endereco.setLocalidade(end1.localidade());
          endereco.setUF(end1.uf());
          endereco.setPais(end1.pais());
        }
      }
    }

    //repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }

  @Override
  @Transactional
  public String updateNome(PatchNomeDTO nome, Long id) {
    Cliente cliente = repository.findById(id);
    cliente.setNome(nome.nome());
    return "Nome alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateCpf(PatchCpfDTO cpf, Long id) {
    Cliente cliente = repository.findById(id);
    verificaCpf(ClienteFormatador.formataCpf(cpf.cpf()));
    cliente.setCpf(ClienteFormatador.formataCpf(cpf.cpf()));
    return "Cpf alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateLogin(PatchLoginDTO login, Long id) {
    Cliente cliente = repository.findById(id);
    verificaLogin(login.login());
    cliente.setLogin(login.login());
    return "Login alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateEmail(PatchEmailDTO email, Long id) {
    Cliente cliente = repository.findById(id);
    cliente.setEmail(email.email());
    return "Email alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateSenhaCliente(PatchSenhaDTO senha, Long id) {
    Cliente cliente = repository.findById(id);

    if(hashservice.getHashSenha(senha.senhaAntiga()).equals(cliente.getSenha())) {
    cliente.setSenha(hashservice.getHashSenha(senha.senhaAtual()));
    //repository.persist(cliente);
    return "Senha alterada com sucesso.";
    } else {
     throw new ValidationException("updateSenha", "Favor inserir a senha antiga correta."); 
    }
  }


  @Override
  @Transactional
  public ClienteResponseDTO insertTelefoneCliente(TelefoneDTO tel, Long id) {
    Cliente cliente = repository.findById(id);
    
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
        telefone.setDdd(tel.ddd());
        telefone.setNumeroTelefone(TelefoneFormatador.formataNumeroTelefone(tel.numeroTelefone()));
        cliente.getListaTelefone().add(telefone);
    repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }


  @Override
  @Transactional
  public ClienteResponseDTO updateTelefoneCliente(TelefonePatchDTO tel,Long id) {
    Cliente cliente = repository.findById(id);

    if (
      cliente.getListaTelefone() != null ||
      !cliente.getListaTelefone().isEmpty()
    ) {

      Boolean chave = true;
      
      for (Telefone tele1 : cliente.getListaTelefone()) {
          if (tele1.getId() == tel.id()) {
            tele1.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
            tele1.setDdd(tel.ddd());
            tele1.setNumeroTelefone(TelefoneFormatador.formataNumeroTelefone(tel.numeroTelefone()));
            chave = false;
          }   
      }
      if(chave) {
        throw new GeneralErrorException("400", "Bad Request", "ClienteServiceImpl(updateTelefoneCliente)", "O id fornecido não corresponde a um id de telefone cadastrado para este usuario");
      }
    } else {
      throw new GeneralErrorException("400", "Bad Request", "ClienteServiceImpl(updateTelefoneCliente)", "Este usuário não possui nenhum telefone cadastrado.");
    }

    //repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }

  @Override
  @Transactional
  public ClienteResponseDTO updateEnderecoCliente(EnderecoPatchDTO end,Long id) {
    Cliente cliente = repository.findById(id);

    if (
      cliente.getListaEndereco() != null ||
      !cliente.getListaEndereco().isEmpty()
    ) {

      Boolean chave = true;

      for (Endereco endereco : cliente.getListaEndereco()) {
        
          if (endereco.getId() == end.id()) {
            endereco.setNome(end.nome());
            endereco.setLogradouro(end.logradouro());
            endereco.setNumeroLote(end.numeroLote());
            endereco.setBairro(end.bairro());
            endereco.setCep(new CEP(EnderecoFormatador.formataCep(end.cep().getCep())));
            endereco.setComplemento(end.complemento());
            endereco.setLocalidade(end.localidade());
            endereco.setUF(end.uf());
            endereco.setPais(end.pais());
            chave = false;
          }

      }

      if(chave) {
        throw new GeneralErrorException("400", "Bad Request", "ClienteServiceImpl(updateEndereçoCliente)", "O id fornecido não corresponde a um id de endereço cadastrado para este usuario");
      }
      
    } else {
      throw new GeneralErrorException("400", "Bad Request", "ClienteServiceImpl(updateEnderecoCliente)", "Este usuário não possui nenhum endereço cadastrado.");
    }
    //repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }


  @Override
  @Transactional
  public ClienteResponseDTO insertEnderecoCliente(EnderecoDTO end,Long id) {
    Cliente cliente = repository.findById(id);

     Endereco endereco = new Endereco();
      endereco.setNome(end.nome());
      endereco.setLogradouro(end.logradouro());
      endereco.setNumeroLote(end.numeroLote());
      endereco.setBairro(end.bairro());
      endereco.setComplemento(end.complemento());
      endereco.setCep(new CEP(EnderecoFormatador.formataCep(end.cep().getCep())));
      endereco.setLocalidade(end.localidade());
      endereco.setUF(end.uf());
      endereco.setPais(end.pais());

    if(cliente.getListaEndereco() == null)
      cliente.setListaEndereco(new ArrayList<Endereco>());

    repositoryEndereco.persist(endereco);

    cliente.getListaEndereco().add(endereco);
    repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }

  @Override
  @Transactional
  public ClienteResponseDTO insertCliente(ClienteDTO dto) {

    Cliente cliente = null;

    try {
      cliente = new Cliente();
    } catch (Exception e) {
      throw new GeneralErrorException("500", "Internal Server Error", "ClienteServiceImpl(insert)", "Não consegui alocar memória para o novo Cliente. Tente novamente mais tarde! " +  e.getCause());
    }

    cliente.setNome(dto.nome());
    cliente.setDataNascimento(
    ClienteFormatador.formataDataNascimento(dto.dataNascimento()));
    verificaCpf(ClienteFormatador.formataCpf(dto.cpf()));
    cliente.setCpf(ClienteFormatador.formataCpf(dto.cpf()));
    cliente.setSexo(dto.sexo());
    verificaLogin(dto.login());
    cliente.setLogin(dto.login());
    cliente.setSenha(hashservice.getHashSenha(dto.senha()));
    cliente.setEmail(dto.email());

    if (dto.listaTelefone() != null && !dto.listaTelefone().isEmpty()) {
      try {
        cliente.setListaTelefone(new ArrayList<Telefone>());
      } catch (Exception e) {
        throw new GeneralErrorException("500", "Internal Server Error", "ClienteServiceImpl(insert)", "Não consegui alocar memória para a lista telefônica do novo Cliente. Tente novamente mais tarde! " +  e.getCause());
      }
      
      for (TelefoneDTO tel : dto.listaTelefone()) {
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
        telefone.setDdd(tel.ddd());
        telefone.setNumeroTelefone(
        TelefoneFormatador.formataNumeroTelefone(tel.numeroTelefone()));
        cliente.getListaTelefone().add(telefone);
      }
    }

    if (dto.listaEndereco() != null && !dto.listaEndereco().isEmpty()) {
      try {
        cliente.setListaEndereco(new ArrayList<Endereco>());
      } catch (Exception e) {
        throw new GeneralErrorException("500", "Internal Server Error", "ClienteServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços do novo Cliente. Tente novamente mais tarde! " +  e.getCause());
      }
      
      for (EnderecoDTO end : dto.listaEndereco()) {
        Endereco endereco = new Endereco();
        endereco.setNome(end.nome());
        endereco.setLogradouro(end.logradouro());
        endereco.setNumeroLote(end.numeroLote());
        endereco.setBairro(end.bairro());
        endereco.setComplemento(end.complemento());
        endereco.setCep(new CEP(EnderecoFormatador.formataCep(end.cep().getCep())));
        endereco.setLocalidade(end.localidade());
        endereco.setUF(end.uf());
        endereco.setPais(end.pais());
        cliente.getListaEndereco().add(endereco);
      }
    }

    try {
      repository.persist(cliente);
    } catch (Exception e) {
      throw new GeneralErrorException("500", "Internal Server Error", "ClienteServiceImpl(insert)", "Não consegui persistir os dados do cliente no repositório " + e.getCause());
    }
    
    return ClienteResponseDTO.valueOf(cliente);
  }

  public List<DesejoResponseDTO> findListaDesejosCliente(Long id) {

     return repository
      .findById(id)
      .getListaProduto()
      .stream()
      .map(p -> DesejoResponseDTO.valueOf(p))
      .toList();
  }


  @Override
    public ClienteResponseDTO findByLoginAndSenha(String login, String senha) {
      Cliente cliente = repository.findByLoginAndSenha(login, senha);
      if(!verificaUsuario2(cliente)) {
        throw new ValidationException("login", "Login ou senha inválido");
      }
      return ClienteResponseDTO.valueOf(cliente);
    }

    @Override
    public ClienteResponseDTO findByLogin(String login) {
        Cliente cliente = repository.findByLogin(login);
        if (cliente == null) 
            throw new ValidationException("login", "Login inválido");
        
        return ClienteResponseDTO.valueOf(cliente);
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

    // Dá permissão para deletar o cliente que nunca fez um pedido
        if(listaPedidos == null || listaPedidos.isEmpty()) {
            return true;
        }

    // Todos os pedidos feitos pelo cliente já foram finalizados (Status.getId() == 5) ou tiveram pagamento não autorizado (Status.getId() == 1)? 
        Integer chaveDelecao = 0;
        for(Pedido pedido : listaPedidos) {
            for(StatusDoPedido statusPedido : pedido.getStatusDoPedido()) {
                if(statusPedido.getStatus().getId() == 5 || statusPedido.getStatus().getId() == 1) {
                    chaveDelecao++;
                }
            }
        }

  // Caso a igualdade seja verdadeira, significa que todos os pedidos do cliente foram finalizados ou tiverem pagamento não autorizado. Deste modo, poderemos deletar o cliente do banco de dados junto com todos os seus endereços.
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
    
    private Boolean verificaUsuario2(Cliente cliente) {
    
      if(cliente == null) {
          return false;
      } else {
          return true;
      }
    }
    @Override
    public List<ClienteResponseDTO> findByAll(int page, int pageSize) {

      List<Cliente> list = repository
          .findAll()
          .page(page, pageSize)
          .list();
      return list
          .stream()
          .map(c -> ClienteResponseDTO.valueOf(c))
          .collect(Collectors.toList());
  }

  @Override
  public long count() {
      return repository.count();
  }

}
