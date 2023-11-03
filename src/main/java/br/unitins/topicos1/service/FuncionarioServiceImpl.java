package br.unitins.topicos1.service;

import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.FuncionarioFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.dto.EnderecoFuncPatchDTO;
import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.dto.PatchSenhaDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Funcionario;
import br.unitins.topicos1.model.Perfil;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.TipoTelefone;
import br.unitins.topicos1.repository.EnderecoRepository;
import br.unitins.topicos1.repository.FuncionarioRepository;
import br.unitins.topicos1.validation.ValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import org.jrimum.domkee.pessoa.CEP;

@ApplicationScoped
public class FuncionarioServiceImpl implements FuncionarioService {

  @Inject
  FuncionarioRepository repository;

  @Inject
  EnderecoRepository repositoryEndereco;

  @Inject
  HashService hashservice;

  @Override
  public FuncionarioResponseDTO findByIdFuncionario(Long id) {
    return FuncionarioResponseDTO.valueOf(repository.findById(id));
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
    return FuncionarioResponseDTO.valueOf(repository.findByCpf(cpf));
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
    repository.deleteById(func.getEndereco().getId());
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

    funcionario.getEndereco().setNome(func.endereco().nome());
    funcionario.getEndereco().setLogradouro(func.endereco().logradouro());
    funcionario.getEndereco().setNumero(func.endereco().numero());
    funcionario.getEndereco().setLote(func.endereco().lote());
    funcionario.getEndereco().setBairro(func.endereco().bairro());
    funcionario.getEndereco().setComplemento(func.endereco().complemento());
    funcionario.getEndereco().setCep(new CEP(EnderecoFormatador.validaCep(func.endereco().cep().getCep())));
    funcionario.getEndereco().setLocalidade(func.endereco().localidade());
    funcionario.getEndereco().setUF(func.endereco().uf());
    funcionario.getEndereco().setPais(func.endereco().pais());

    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
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
  public FuncionarioResponseDTO updateTelefoneFuncionario(
    List<TelefonePatchDTO> tel,
    Long id
  ) {
    Funcionario funcionario = repository.findById(id);

    List<Long> id1 = new ArrayList<Long>();
    List<Long> id2 = new ArrayList<Long>();

    if (
      funcionario.getListaTelefone() != null ||
      !funcionario.getListaTelefone().isEmpty()
    ) {
      for (Telefone tele1 : funcionario.getListaTelefone()) {
        id1.add(tele1.getId());
      }
    }

    for (TelefonePatchDTO tele1 : tel) {
      id2.add(tele1.id());
    }

    for (Telefone tele1 : funcionario.getListaTelefone()) {
      for (TelefonePatchDTO tele : tel) {
        if (tele1.getId() == tele.id()) {
          tele1.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
          tele1.setDdd(tele.ddd());
          tele1.setNumeroTelefone(
            TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone())
          );
          id1.remove(id1.indexOf(tele1.getId()));
          id2.remove(id2.indexOf(tele1.getId()));
        }
      }
    }

    for (int i = 0; i < id2.size(); i++) {
      for (TelefonePatchDTO tele : tel) {
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
        telefone.setDdd(tele.ddd());
        telefone.setNumeroTelefone(
          TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone())
        );
        funcionario.getListaTelefone().add(telefone);
      }
    }
    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  @Override
  @Transactional
  public FuncionarioResponseDTO updateEnderecoFuncionario(EnderecoFuncPatchDTO end, Long id) {
    Funcionario funcionario = repository.findById(id);

    funcionario.getEndereco().setLogradouro(end.logradouro());
    funcionario.getEndereco().setNumero(end.numero());
    funcionario.getEndereco().setLote(end.lote());
    funcionario.getEndereco().setBairro(end.bairro());
    funcionario.getEndereco().setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
    funcionario.getEndereco().setComplemento(end.complemento());
    funcionario.getEndereco().setLocalidade(end.localidade());
    funcionario.getEndereco().setUF(end.uf());
    funcionario.getEndereco().setPais(end.pais());

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
    endereco.setNome(dto.endereco().nome());
    endereco.setLogradouro(dto.endereco().logradouro());
    endereco.setNumero(dto.endereco().numero());
    endereco.setLote(dto.endereco().lote());
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
}
