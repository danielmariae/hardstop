package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.FuncionarioPatchSenhaDTO;
import br.unitins.topicos1.dto.FuncionarioResponseDTO;
import br.unitins.topicos1.Formatadores.FuncionarioFormatador;
import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.dto.EnderecoPatchDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.TelefonePatchDTO;
import br.unitins.topicos1.model.Funcionario;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.TipoTelefone;
import br.unitins.topicos1.repository.FuncionarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FuncionarioServiceImpl implements FuncionarioService {

  @Inject
  FuncionarioRepository repository;

  @Override
  public FuncionarioResponseDTO findById(Long id) {
    return FuncionarioResponseDTO.valueOf(repository.findById(id));
  }

  @Override
  public List<FuncionarioResponseDTO> findByName(String name) {
    return repository
      .findByName(name)
      .stream()
      .map(c -> FuncionarioResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  public FuncionarioResponseDTO findByCpf(String cpf) {
    return FuncionarioResponseDTO.valueOf(repository.findByCpf(cpf));
  }

  @Override
  public List<FuncionarioResponseDTO> findByAll() {
    return repository
      .listAll()
      .stream()
      .map(c -> FuncionarioResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  @Transactional
  public void delete(Long id) {
    Funcionario func = repository.findById(id);
    repository.deleteById(func.getEndereco().getId());
    for(Telefone tel : func.getListaTelefone()) {
      repository.deleteById(tel.getId());
    }
    repository.deleteById(id);
  }

  @Override
  @Transactional
  public FuncionarioResponseDTO update(FuncionarioDTO func, Long id) {
    Funcionario funcionario = repository.findById(id);

    funcionario.setNome(func.nome());
    funcionario.setDataNascimento(FuncionarioFormatador.validaDataNascimento(func.dataNascimento()));
    funcionario.setCpf(FuncionarioFormatador.validaCpf(func.cpf()));
    funcionario.setSexo(func.sexo());
    funcionario.setLogin(func.login());
    funcionario.setEmail(func.email());

    int i=0;
    int j=0;

    for (Telefone tele1 : funcionario.getListaTelefone()) {
      i++;
      j=0;
      for (TelefoneDTO tele : func.listaTelefone()) {
        j++;
        if (i==j) {
          tele1.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
          tele1.setDdd(tele.ddd());
          tele1.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone()));
        }
      }
    }

        funcionario.getEndereco().setNome(func.endereco().nome());
        funcionario.getEndereco().setRua(func.endereco().rua());
        funcionario.getEndereco().setNumero(func.endereco().numero());
        funcionario.getEndereco().setLote(func.endereco().lote());
        funcionario.getEndereco().setBairro(func.endereco().bairro());
        funcionario.getEndereco().setComplemento(func.endereco().complemento());
        funcionario.getEndereco().setCep(EnderecoFormatador.validaCep(func.endereco().cep()));
        funcionario.getEndereco().setMunicipio(func.endereco().municipio());
        funcionario.getEndereco().setEstado(func.endereco().estado());
        funcionario.getEndereco().setPais(func.endereco().pais());

    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  // FuncionarioResponseDTO não tem senha, por isso trabalhamos com FuncionarioDTO
  @Override
  @Transactional
  public FuncionarioDTO updateSenha(FuncionarioPatchSenhaDTO senha) {
    Funcionario funcionario = repository.findById(senha.id());
    funcionario.setSenha(senha.senha());
    repository.persist(funcionario);
    return FuncionarioDTO.valueOf(funcionario);
  }

  @Override
  @Transactional
  public FuncionarioResponseDTO updateTelefone(List<TelefonePatchDTO> tel, Long id) {
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
          tele1.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone()));
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
        telefone.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone()));
        funcionario.getListaTelefone().add(telefone);
      }
    }
    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  @Override
  @Transactional
  public FuncionarioResponseDTO updateEndereco(EnderecoPatchDTO end, Long id) {
    Funcionario funcionario = repository.findById(id);

        funcionario.getEndereco().setNome(end.nome());
        funcionario.getEndereco().setRua(end.rua());
        funcionario.getEndereco().setNumero(end.numero());
        funcionario.getEndereco().setLote(end.lote());
        funcionario.getEndereco().setBairro(end.bairro());
        funcionario.getEndereco().setCep(EnderecoFormatador.validaCep(end.cep()));
        funcionario.getEndereco().setComplemento(end.complemento());
        funcionario.getEndereco().setMunicipio(end.municipio());
        funcionario.getEndereco().setEstado(end.estado());
        funcionario.getEndereco().setPais(end.pais());
          
    repository.persist(funcionario);
    return FuncionarioResponseDTO.valueOf(funcionario);
  }

  public FuncionarioDTO insert(FuncionarioDTO dto) {
    Funcionario funcionario = new Funcionario();
    funcionario.setNome(dto.nome());
    funcionario.setDataNascimento(FuncionarioFormatador.validaDataNascimento(dto.dataNascimento()));
    funcionario.setCpf(FuncionarioFormatador.validaCpf(dto.cpf()));
    funcionario.setSexo(dto.sexo());
    funcionario.setLogin(dto.login());
    funcionario.setSenha(dto.senha());
    funcionario.setEmail(dto.email());
    funcionario.setListaTelefone(new ArrayList<Telefone>());
    funcionario.setEndereco(new Endereco());

    if (dto.listaTelefone() != null && !dto.listaTelefone().isEmpty()) {
      funcionario.setListaTelefone(new ArrayList<Telefone>());
      for (TelefoneDTO tel : dto.listaTelefone()) {
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
        telefone.setDdd(tel.ddd());
        telefone.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
        funcionario.getListaTelefone().add(telefone);
      }
    }

    
        Endereco endereco = new Endereco();
        endereco.setNome(dto.endereco().nome());
        endereco.setRua(dto.endereco().rua());
        endereco.setNumero(dto.endereco().numero());
        endereco.setLote(dto.endereco().lote());
        endereco.setBairro(dto.endereco().bairro());
        endereco.setComplemento(dto.endereco().complemento());
        endereco.setCep(EnderecoFormatador.validaCep(dto.endereco().cep()));
        endereco.setMunicipio(dto.endereco().municipio());
        endereco.setEstado(dto.endereco().estado());
        endereco.setPais(dto.endereco().pais());
        funcionario.setEndereco(endereco);
  
    repository.persist(funcionario);
    return FuncionarioDTO.valueOf(funcionario);
  }
}

