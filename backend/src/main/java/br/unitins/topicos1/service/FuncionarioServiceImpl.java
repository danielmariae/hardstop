package br.unitins.topicos1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.Formatadores.FuncionarioFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.funcionario.EnderecoFuncDTO;
import br.unitins.topicos1.dto.funcionario.EnderecoFuncPatchDTO;
import br.unitins.topicos1.dto.funcionario.FuncionarioDTO;
import br.unitins.topicos1.dto.funcionario.FuncionarioNSDTO;
import br.unitins.topicos1.dto.funcionario.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.login.PerfilDTO;
import br.unitins.topicos1.dto.patch.PatchCpfDTO;
import br.unitins.topicos1.dto.patch.PatchEmailDTO;
import br.unitins.topicos1.dto.patch.PatchLoginDTO;
import br.unitins.topicos1.dto.patch.PatchNomeDTO;
import br.unitins.topicos1.dto.patch.PatchSenhaDTO;
import br.unitins.topicos1.dto.telefone.TelefoneDTO;
import br.unitins.topicos1.dto.telefone.TelefonePatchDTO;
import br.unitins.topicos1.model.utils.Endereco;
import br.unitins.topicos1.model.utils.Funcionario;
import br.unitins.topicos1.model.utils.Perfil;
import br.unitins.topicos1.model.utils.Telefone;
import br.unitins.topicos1.model.utils.TipoTelefone;
import br.unitins.topicos1.repository.EnderecoRepository;
import br.unitins.topicos1.repository.FuncionarioRepository;
import br.unitins.topicos1.validation.ValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FuncionarioServiceImpl implements FuncionarioService {

  // @Inject
  // FuncionarioRepository repository;

  @Inject
  FuncionarioRepository repository;


  @Inject
  EnderecoRepository repositoryEndereco;

  @Inject
  HashService hashservice;


  @Override
  public FuncionarioResponseDTO findByIdFuncionario(Long id) {
    Funcionario funcionario = repository.findById(id);
    if (funcionario == null) 
            throw new ValidationException("ID", "id inexistente");
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  @Override
  public List<FuncionarioResponseDTO> findByNameFuncionario(String name) {
    return repository
      .findByName(name)
      .stream()
      .map(c -> FuncionarioResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  public FuncionarioResponseDTO findByCpfFuncionario(String cpf) {
    Funcionario funcionario = repository.findByCpf(cpf);
    if (funcionario == null) 
            throw new ValidationException("CPF", "cpf inexistente");
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  @Override
  public List<FuncionarioResponseDTO> findByAllFuncionario() {
    return repository
      .listAll()
      .stream()
      .map(c -> FuncionarioResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  @Transactional
  public void deleteFuncionario(Long id) {
    Funcionario func = repository.findById(id);
    for (Telefone tel : func.getListaTelefone()) {
      repository.deleteById(tel.getId());
    }
    repository.deleteById(id);
  }

  @Override
  @Transactional
  public FuncionarioResponseDTO updateFuncionario(FuncionarioDTO func, Long id) {
    Funcionario funcionario = repository.findById(id);

    funcionario.setNome(func.nome());
    funcionario.setDataNascimento(
      FuncionarioFormatador.formataDataNascimento(func.dataNascimento())
    );
    funcionario.setCpf(FuncionarioFormatador.formataCpf(func.cpf()));
    funcionario.setSexo(func.sexo());
    funcionario.setLogin(func.login());
    funcionario.setSenha(hashservice.getHashSenha(func.senha()));
    funcionario.setEmail(func.email());

    int i = 0;
    int j = 0;

    for (Telefone tele1 : funcionario.getListaTelefone()) {
      i++;
      j = 0;
      for (TelefoneDTO tele : func.listaTelefone()) {
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

    if (func.listaEndereco() != null) {
      try {
        Endereco listaEndereco = new Endereco();
        listaEndereco.setNome(func.nome());
        listaEndereco.setLogradouro(func.listaEndereco().logradouro());
        listaEndereco.setNumeroLote(func.listaEndereco().numeroLote());
        listaEndereco.setBairro(func.listaEndereco().bairro());
        listaEndereco.setComplemento(func.listaEndereco().complemento());
        listaEndereco.setCep(func.listaEndereco().cep());
        listaEndereco.setLocalidade(func.listaEndereco().localidade());
        listaEndereco.setUF(func.listaEndereco().uf());
        listaEndereco.setPais(func.listaEndereco().pais());
        funcionario.setEndereco(listaEndereco);
      } catch (Exception e) {
        throw new GeneralErrorException("500", "Internal Server Error", "FuncionarioServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços do novo Funcionario. Tente novamente mais tarde! " +  e.getCause());
      }
    }
    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  @Override
  @Transactional
  public FuncionarioResponseDTO updateFuncionarioNS(FuncionarioNSDTO func, Long id) {
    Funcionario funcionario = repository.findById(id);

    funcionario.setNome(func.nome());
    funcionario.setDataNascimento(
      FuncionarioFormatador.formataDataNascimento(func.dataNascimento())
    );
    funcionario.setCpf(FuncionarioFormatador.formataCpf(func.cpf()));
    funcionario.setSexo(func.sexo());
    funcionario.setLogin(func.login());
    funcionario.setEmail(func.email());

    int i = 0;
    int j = 0;

    for (Telefone tele1 : funcionario.getListaTelefone()) {
      i++;
      j = 0;
      for (TelefoneDTO tele : func.listaTelefone()) {
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

    if (func.listaEndereco() != null) {
      try {
        Endereco listaEndereco = new Endereco();
        listaEndereco.setNome(func.nome());
        listaEndereco.setLogradouro(func.listaEndereco().logradouro());
        listaEndereco.setNumeroLote(func.listaEndereco().numeroLote());
        listaEndereco.setBairro(func.listaEndereco().bairro());
        listaEndereco.setComplemento(func.listaEndereco().complemento());
        listaEndereco.setCep(func.listaEndereco().cep());
        listaEndereco.setLocalidade(func.listaEndereco().localidade());
        listaEndereco.setUF(func.listaEndereco().uf());
        listaEndereco.setPais(func.listaEndereco().pais());
        funcionario.setEndereco(listaEndereco);
      } catch (Exception e) {
        throw new GeneralErrorException("500", "Internal Server Error", "FuncionarioServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços do novo Funcionario. Tente novamente mais tarde! " +  e.getCause());
      }
    }
    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }


  @Override
  @Transactional
  public String updateNome(PatchNomeDTO nome, Long id) {
    Funcionario funcionario = repository.findById(id);
    funcionario.setNome(nome.nome());
    return "Nome alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateCpf(PatchCpfDTO cpf, Long id) {
    Funcionario funcionario = repository.findById(id);
    verificaCpf(FuncionarioFormatador.formataCpf(cpf.cpf()));
    funcionario.setCpf(FuncionarioFormatador.formataCpf(cpf.cpf()));
    return "Cpf alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateLogin(PatchLoginDTO login, Long id) {
    Funcionario funcionario = repository.findById(id);
    verificaLogin(login.login());
    funcionario.setLogin(login.login());
    return "Login alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateEmail(PatchEmailDTO email, Long id) {
    Funcionario funcionario = repository.findById(id);
    funcionario.setEmail(email.email());
    return "Email alterado com sucesso.";
  }

  @Override
  @Transactional
  public String updateSenhaFuncionario(PatchSenhaDTO senha, Long id) {
    Funcionario funcionario = repository.findById(id);

    if(hashservice.getHashSenha(senha.senhaAntiga()).equals(funcionario.getSenha())) {
    funcionario.setSenha(hashservice.getHashSenha(senha.senhaAtual()));
    repository.persist(funcionario);
    return "Senha alterada com sucesso.";
    } else {
     throw new ValidationException("updateSenha", "Favor inserir a senha antiga correta."); 
    }
  }

  @Override
  @Transactional
  public FuncionarioResponseDTO insertTelefoneFuncionario(TelefoneDTO tel, Long id) {
    Funcionario funcionario = repository.findById(id);
    
    Telefone telefone = new Telefone();
    telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
    telefone.setDdd(tel.ddd());
    telefone.setNumeroTelefone(TelefoneFormatador.formataNumeroTelefone(tel.numeroTelefone()));
    funcionario.getListaTelefone().add(telefone);
    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }


  @Override
  @Transactional
  public FuncionarioResponseDTO updateTelefoneFuncionario(TelefonePatchDTO tel,Long id) {
    Funcionario funcionario = repository.findById(id);

    if (
      funcionario.getListaTelefone() != null ||
      !funcionario.getListaTelefone().isEmpty()
    ) {

      Boolean chave = true;
      
      for (Telefone tele1 : funcionario.getListaTelefone()) {
          if (tele1.getId() == tel.id()) {
            tele1.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
            tele1.setDdd(tel.ddd());
            tele1.setNumeroTelefone(TelefoneFormatador.formataNumeroTelefone(tel.numeroTelefone()));
            chave = false;
          }   
      }
      if(chave) {
        throw new GeneralErrorException("400", "Bad Request", "FuncionarioServiceImpl(updateTelefoneFuncionario)", "O id fornecido não corresponde a um id de telefone cadastrado para este funcionario");
      }
    } else {
      throw new GeneralErrorException("400", "Bad Request", "FuncionarioServiceImpl(updateTelefoneFuncionario)", "Este usuário não possui nenhum telefone cadastrado.");
    }

    //repository.persist(cliente);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  @Override
  @Transactional
  public FuncionarioResponseDTO updateEnderecoFuncionario(EnderecoFuncPatchDTO end,Long id) {
    Funcionario funcionario = repository.findById(id);

    if(funcionario.getEndereco()!= null) {
      if(funcionario.getEndereco().getId() == end.id()) {
        funcionario.getEndereco().setLogradouro(end.logradouro());
        funcionario.getEndereco().setNumeroLote(end.numeroLote());
        funcionario.getEndereco().setBairro(end.bairro());
        funcionario.getEndereco().setCep(end.cep());
        funcionario.getEndereco().setComplemento(end.complemento());
        funcionario.getEndereco().setLocalidade(end.localidade());
        funcionario.getEndereco().setUF(end.uf());
        funcionario.getEndereco().setPais(end.pais());
        } else {
          throw new GeneralErrorException("400", "Bad Request", "FuncionarioServiceImpl(updateTelefoneFuncionario)", "O id fornecido não corresponde a um id de endereço cadastrado para este funcionario");
        }
    } else {
      throw new GeneralErrorException("400", "Bad Request", "FuncionarioServiceImpl(updateEnderecoFuncionario)", "Este usuário não possui nenhum endereço cadastrado.");
    }
    //repository.persist(cliente);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }


  @Override
  @Transactional
  public FuncionarioResponseDTO insertEnderecoFuncionario(EnderecoFuncDTO end,Long id) {
    Funcionario funcionario = repository.findById(id);
    Endereco listaEndereco = new Endereco();

    listaEndereco.setLogradouro(end.logradouro());
    listaEndereco.setNumeroLote(end.numeroLote());
    listaEndereco.setBairro(end.bairro());
    listaEndereco.setComplemento(end.complemento());
    listaEndereco.setCep(end.cep());
    listaEndereco.setLocalidade(end.localidade());
    listaEndereco.setUF(end.uf());
    listaEndereco.setPais(end.pais());
    repositoryEndereco.persist(listaEndereco);
    funcionario.setEndereco(listaEndereco);
    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  public FuncionarioDTO insertFuncionario(FuncionarioDTO dto) {
    Funcionario funcionario = new Funcionario();
    funcionario.setNome(dto.nome());
    funcionario.setDataNascimento(
      FuncionarioFormatador.formataDataNascimento(dto.dataNascimento())
    );
    funcionario.setCpf(FuncionarioFormatador.formataCpf(dto.cpf()));
    funcionario.setSexo(dto.sexo());
    funcionario.setLogin(dto.login());
    funcionario.setSenha(hashservice.getHashSenha(dto.senha()));
    funcionario.setEmail(dto.email());
    funcionario.setPerfil(Perfil.valueOf(dto.idperfil()));
    funcionario.setListaTelefone(new ArrayList<Telefone>());

    if (dto.listaTelefone() != null && !dto.listaTelefone().isEmpty()) {
      funcionario.setListaTelefone(new ArrayList<Telefone>());
      for (TelefoneDTO tel : dto.listaTelefone()) {
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
        telefone.setDdd(tel.ddd());
        telefone.setNumeroTelefone(
          TelefoneFormatador.formataNumeroTelefone(tel.numeroTelefone())
        );
        funcionario.getListaTelefone().add(telefone);
      }
    }

    Endereco listaEndereco = new Endereco();
    listaEndereco.setLogradouro(dto.listaEndereco().logradouro());
    listaEndereco.setNumeroLote(dto.listaEndereco().numeroLote());
    listaEndereco.setBairro(dto.listaEndereco().bairro());
    listaEndereco.setComplemento(dto.listaEndereco().complemento());
    listaEndereco.setCep(dto.listaEndereco().cep());
    listaEndereco.setLocalidade(dto.listaEndereco().localidade());
    listaEndereco.setUF(dto.listaEndereco().uf());
    listaEndereco.setPais(dto.listaEndereco().pais());
    repositoryEndereco.persist(listaEndereco);

    funcionario.setEndereco(listaEndereco);
    repository.persist(funcionario);
    return FuncionarioDTO.valueOf(funcionario);
  }

  @Override
  public FuncionarioResponseDTO findByLoginAndSenha(
    String login,
    String senha
  ) {
    Funcionario funcionario = repository.findByLoginAndSenha(login, senha);
    if (funcionario == null) {
      throw new ValidationException("Login", "Login e/ou Senha incorretos");
    }
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  @Override
  public FuncionarioResponseDTO findByLogin(String login) {
    Funcionario funcionario = repository.findByLogin(login);
    if (funcionario == null) {
      throw new ValidationException("Login", "Login incorreto");
    }
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

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

  public List<FuncionarioResponseDTO> findByAll(int page, int pageSize) {

    List<Funcionario> list = repository
        .findAll()
        .page(page, pageSize)
        .list();
    return list
        .stream()
        .map(c -> FuncionarioResponseDTO.valueOf(c))
        .collect(Collectors.toList());
}

@Override
public long count() {
    return repository.count();
}

    @Override
    public PerfilDTO findPerfilByLogin(String login) {
      Funcionario funcionario = repository.findByLogin(login);
      if(funcionario == null)
        throw new ValidationException("login", "Login inválido");
      
      return PerfilDTO.valueOf(funcionario.getPerfil());
    }
}
