package br.unitins.topicos1.service;

import java.util.ArrayList;
import java.util.List;

import org.jrimum.domkee.pessoa.CEP;

import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.Formatadores.FuncionarioFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.EnderecoFuncDTO;
import br.unitins.topicos1.dto.EnderecoFuncPatchDTO;
import br.unitins.topicos1.dto.PatchCpfDTO;
import br.unitins.topicos1.dto.PatchEmailDTO;
import br.unitins.topicos1.dto.PatchLoginDTO;
import br.unitins.topicos1.dto.PatchNomeDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Perfil;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.TipoTelefone;
import br.unitins.topicos1.model.Funcionario;
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
      FuncionarioFormatador.validaDataNascimento(func.dataNascimento())
    );
    funcionario.setCpf(FuncionarioFormatador.validaCpf(func.cpf()));
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
            TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone())
          );
        }
      }
    }

    if (func.endereco() != null) {
      try {
        Endereco endereco = new Endereco();
        endereco.setNome(func.nome());
        endereco.setLogradouro(func.endereco().logradouro());
        endereco.setNumeroLote(func.endereco().numeroLote());
        endereco.setBairro(func.endereco().bairro());
        endereco.setComplemento(func.endereco().complemento());
        endereco.setCep(new CEP(EnderecoFormatador.validaCep(func.endereco().cep().getCep())));
        endereco.setLocalidade(func.endereco().localidade());
        endereco.setUF(func.endereco().uf());
        endereco.setPais(func.endereco().pais());
        funcionario.setEndereco(endereco);
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
    verificaCpf(FuncionarioFormatador.validaCpf(cpf.cpf()));
    funcionario.setCpf(FuncionarioFormatador.validaCpf(cpf.cpf()));
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
    telefone.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
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
            tele1.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
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
        funcionario.getEndereco().setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
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
    Endereco endereco = new Endereco();

    endereco.setLogradouro(end.logradouro());
    endereco.setNumeroLote(end.numeroLote());
    endereco.setBairro(end.bairro());
    endereco.setComplemento(end.complemento());
    endereco.setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
    endereco.setLocalidade(end.localidade());
    endereco.setUF(end.uf());
    endereco.setPais(end.pais());
    repositoryEndereco.persist(endereco);
    funcionario.setEndereco(endereco);
    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  public FuncionarioDTO insertFuncionario(FuncionarioDTO dto) {
    Funcionario funcionario = new Funcionario();
    funcionario.setNome(dto.nome());
    funcionario.setDataNascimento(
      FuncionarioFormatador.validaDataNascimento(dto.dataNascimento())
    );
    funcionario.setCpf(FuncionarioFormatador.validaCpf(dto.cpf()));
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
          TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone())
        );
        funcionario.getListaTelefone().add(telefone);
      }
    }

    Endereco endereco = new Endereco();
    endereco.setLogradouro(dto.endereco().logradouro());
    endereco.setNumeroLote(dto.endereco().numeroLote());
    endereco.setBairro(dto.endereco().bairro());
    endereco.setComplemento(dto.endereco().complemento());
    endereco.setCep(new CEP(EnderecoFormatador.validaCep(dto.endereco().cep().getCep())));
    endereco.setLocalidade(dto.endereco().localidade());
    endereco.setUF(dto.endereco().uf());
    endereco.setPais(dto.endereco().pais());
    repositoryEndereco.persist(endereco);

    funcionario.setEndereco(endereco);
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
}
