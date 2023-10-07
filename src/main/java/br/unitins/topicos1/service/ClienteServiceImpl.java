package br.unitins.topicos1.service;

import br.unitins.topicos1.Formatadores.ClienteFormatador;
import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.dto.ClientePatchSenhaDTO;
import br.unitins.topicos1.dto.ClienteResponseDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.ItemDaVendaDTO;
import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.StatusDoPedidoDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.FormaDePagamento;
import br.unitins.topicos1.model.ItemDaVenda;
import br.unitins.topicos1.model.Pedido;
import br.unitins.topicos1.model.Status;
import br.unitins.topicos1.model.StatusDoPedido;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.TipoTelefone;
import br.unitins.topicos1.repository.ClienteRepository;
import br.unitins.topicos1.repository.PedidoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ClienteServiceImpl implements ClienteService {

  @Inject
  ClienteRepository repository;

  @Inject
  PedidoRepository repositoryPedido;

  @Override
  public ClienteResponseDTO findById(Long id) {
    return ClienteResponseDTO.valueOf(repository.findById(id));
  }

  @Override
  public List<ClienteResponseDTO> findByName(String name) {
    return repository
      .findByName(name)
      .stream()
      .map(c -> ClienteResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  public ClienteResponseDTO findByCpf(String cpf) {
    return ClienteResponseDTO.valueOf(repository.findByCpf(cpf));
  }

  @Override
  public List<ClienteResponseDTO> findByAll() {
    return repository
      .listAll()
      .stream()
      .map(c -> ClienteResponseDTO.valueOf(c))
      .toList();
  }

  @Override
  @Transactional
  public void delete(Long id) {
    Cliente clt = repository.findById(id);
    for (Endereco end : clt.getListaEndereco()) {
      repository.deleteById(end.getId());
    }
    for (Telefone tel : clt.getListaTelefone()) {
      repository.deleteById(tel.getId());
    }
    for (Pedido pedido : clt.getListaPedido()) {
      for (StatusDoPedido status : pedido.getStatusDoPedido()) {
        repositoryPedido.deleteById(status.getId());
      }
      for (ItemDaVenda item : pedido.getItemDaVenda()) {
        repositoryPedido.deleteById(item.getId());
      }
      repositoryPedido.deleteById(pedido.getFormaDePagamento().getId());
      repositoryPedido.deleteById(pedido.getEndereco().getId());
      repository.deleteById(pedido.getId());
    }
    repository.deleteById(id);
  }

  @Override
  @Transactional
  public ClienteResponseDTO update(ClienteDTO clt, Long id) {
    Cliente cliente = repository.findById(id);

    cliente.setNome(clt.nome());
    cliente.setDataNascimento(
      ClienteFormatador.validaDataNascimento(clt.dataNascimento())
    );
    cliente.setCpf(ClienteFormatador.validaCpf(clt.cpf()));
    cliente.setSexo(clt.sexo());
    cliente.setLogin(clt.login());
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
            TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone())
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
          endereco.setRua(end1.rua());
          endereco.setNumero(end1.numero());
          endereco.setLote(end1.lote());
          endereco.setBairro(end1.bairro());
          endereco.setComplemento(end1.complemento());
          endereco.setCep(EnderecoFormatador.validaCep(end1.cep()));
          endereco.setMunicipio(end1.municipio());
          endereco.setEstado(end1.estado());
          endereco.setPais(end1.pais());
        }
      }
    }

    int ia = 0;
    int ja = 0;
    for (Pedido pedido : cliente.getListaPedido()) {
      ia++;
      ja = 0;
      for (PedidoDTO ped : clt.listaPedido()) {
        ja++;
        if (ia == ja) {
          pedido.setCodigoDeRastreamento(ped.codigoDeRastreamento());
          pedido.getFormaDePagamento().setNome(ped.formaDePagamento().nome());
          pedido.getEndereco().setNome(ped.endereco().nome());
          pedido.getEndereco().setRua(ped.endereco().rua());
          pedido.getEndereco().setNumero(ped.endereco().numero());
          pedido.getEndereco().setLote(ped.endereco().lote());
          pedido.getEndereco().setBairro(ped.endereco().bairro());
          pedido.getEndereco().setComplemento(ped.endereco().complemento());
          pedido.getEndereco().setCep(ped.endereco().cep());
          pedido.getEndereco().setMunicipio(ped.endereco().municipio());
          pedido.getEndereco().setEstado(ped.endereco().estado());
          pedido.getEndereco().setPais(ped.endereco().pais());

          int ia1 = 0;
          int ja1 = 0;
          for (ItemDaVenda item : pedido.getItemDaVenda()) {
            ia1++;
            ja1 = 0;
            for (ItemDaVendaDTO it : ped.itemDaVenda()) {
              ja1++;
              if (ia1 == ja1) {
                item.setPreco(it.preco());
                item.setQuantidade(it.quantidade());
              }
            }
          }

          int ia2 = 0;
          int ja2 = 0;
          for (StatusDoPedido status : pedido.getStatusDoPedido()) {
            ia2++;
            ja2 = 0;
            for (StatusDoPedidoDTO st : ped.statusDoPedido()) {
              ja2++;
              if (ia2 == ja2) {
                status.setDataHora(st.dataHora());
                status.setStatus(Status.valueOf(st.status()));
              }
            }
          }
        }
      }
    }

    repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }

  // ClienteResponseDTO não tem senha, por isso trabalhamos com ClienteDTO
  @Override
  @Transactional
  public ClienteDTO updateSenha(ClientePatchSenhaDTO senha) {
    Cliente cliente = repository.findById(senha.id());
    cliente.setSenha(senha.senha());
    repository.persist(cliente);
    return ClienteDTO.valueOf(cliente);
  }


  @Override
  @Transactional
  public ClienteResponseDTO updateTelefone(List<TelefoneDTO> tel, Long id) {
    Cliente cliente = repository.findById(id);

    List<Long> id1 = new ArrayList<Long>();
    List<Long> id2 = new ArrayList<Long>();

    if (
      cliente.getListaTelefone() != null ||
      !cliente.getListaTelefone().isEmpty()
    ) {
      for (Telefone tele1 : cliente.getListaTelefone()) {
        id1.add(tele1.getId());
      }
    }

    for (TelefoneDTO tele1 : tel) {
      id2.add(tele1.id());
    }

    for (Telefone tele1 : cliente.getListaTelefone()) {
      for (TelefoneDTO tele : tel) {
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
      for (TelefoneDTO tele : tel) {
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
        telefone.setDdd(tele.ddd());
        telefone.setNumeroTelefone(
          TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone())
        );
        cliente.getListaTelefone().add(telefone);
      }
    }
    repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }

  @Override
  @Transactional
  public ClienteResponseDTO updateEndereco(List<EnderecoDTO> end, Long id) {
    Cliente cliente = repository.findById(id);

    List<Long> d1 = new ArrayList<Long>();
    List<Long> d2 = new ArrayList<Long>();

    if (
      cliente.getListaEndereco() != null ||
      !cliente.getListaEndereco().isEmpty()
    ) {
      for (Endereco end1 : cliente.getListaEndereco()) {
        d1.add(end1.getId());
      }
    }

    for (EnderecoDTO end2 : end) {
      d2.add(end2.id());
    }

    for (Endereco endereco : cliente.getListaEndereco()) {
      for (EnderecoDTO end1 : end) {
        if (end1.id() == endereco.getId()) {
          endereco.setNome(end1.nome());
          endereco.setRua(end1.rua());
          endereco.setNumero(end1.numero());
          endereco.setLote(end1.lote());
          endereco.setBairro(end1.bairro());
          endereco.setCep(EnderecoFormatador.validaCep(end1.cep()));
          endereco.setComplemento(end1.complemento());
          endereco.setMunicipio(end1.municipio());
          endereco.setEstado(end1.estado());
          endereco.setPais(end1.pais());
          d1.remove(d1.indexOf(end1.id()));
          d2.remove(d2.indexOf(end1.id()));
        }
      }
    }

    for (int i = 0; i < d2.size(); i++) {
      for (EnderecoDTO end1 : end) {
        Endereco endereco = new Endereco();
        endereco.setNome(end1.nome());
        endereco.setRua(end1.rua());
        endereco.setNumero(end1.numero());
        endereco.setLote(end1.lote());
        endereco.setBairro(end1.bairro());
        endereco.setComplemento(end1.complemento());
        endereco.setCep(EnderecoFormatador.validaCep(end1.cep()));
        endereco.setMunicipio(end1.municipio());
        endereco.setEstado(end1.estado());
        endereco.setPais(end1.pais());
        cliente.getListaEndereco().add(endereco);
      }
    }
    repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }

  public ClienteResponseDTO insert(ClienteDTO dto) {
    Cliente cliente = new Cliente();
    cliente.setNome(dto.nome());
    cliente.setDataNascimento(
      ClienteFormatador.validaDataNascimento(dto.dataNascimento())
    );
    cliente.setCpf(ClienteFormatador.validaCpf(dto.cpf()));
    cliente.setSexo(dto.sexo());
    cliente.setLogin(dto.login());
    cliente.setSenha(dto.senha());
    cliente.setEmail(dto.email());

    if (dto.listaTelefone() != null && !dto.listaTelefone().isEmpty()) {
      cliente.setListaTelefone(new ArrayList<Telefone>());
      for (TelefoneDTO tel : dto.listaTelefone()) {
        Telefone telefone = new Telefone();
        telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
        telefone.setDdd(tel.ddd());
        telefone.setNumeroTelefone(
          TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone())
        );
        cliente.getListaTelefone().add(telefone);
      }
    }

    if (dto.listaEndereco() != null && !dto.listaEndereco().isEmpty()) {
      cliente.setListaEndereco(new ArrayList<Endereco>());
      for (EnderecoDTO end : dto.listaEndereco()) {
        Endereco endereco = new Endereco();
        endereco.setNome(end.nome());
        endereco.setRua(end.rua());
        endereco.setNumero(end.numero());
        endereco.setLote(end.lote());
        endereco.setBairro(end.bairro());
        endereco.setComplemento(end.complemento());
        endereco.setCep(EnderecoFormatador.validaCep(end.cep()));
        endereco.setMunicipio(end.municipio());
        endereco.setEstado(end.estado());
        endereco.setPais(end.pais());
        cliente.getListaEndereco().add(endereco);
      }
    }

    if (dto.listaPedido() != null && !dto.listaPedido().isEmpty()) {
      cliente.setListaPedido(new ArrayList<Pedido>());

      for (PedidoDTO pdd : dto.listaPedido()) {
        Pedido pedido = new Pedido();
        pedido.setCodigoDeRastreamento(pdd.codigoDeRastreamento());

        FormaDePagamento forma = new FormaDePagamento();
        forma.setNome(pdd.formaDePagamento().nome());
        pedido.setFormaDePagamento(forma);

        Endereco endereco = new Endereco();
        endereco.setNome(pdd.endereco().nome());
        endereco.setRua(pdd.endereco().rua());
        endereco.setNumero(pdd.endereco().numero());
        endereco.setLote(pdd.endereco().lote());
        endereco.setBairro(pdd.endereco().bairro());
        endereco.setComplemento(pdd.endereco().complemento());
        endereco.setCep(EnderecoFormatador.validaCep(pdd.endereco().cep()));
        endereco.setMunicipio(pdd.endereco().municipio());
        endereco.setEstado(pdd.endereco().estado());
        endereco.setPais(pdd.endereco().pais());
        pedido.setEndereco(endereco);

        pedido.setItemDaVenda(new ArrayList<ItemDaVenda>());
        for (ItemDaVendaDTO itens : pdd.itemDaVenda()) {
          ItemDaVenda itemVenda = new ItemDaVenda();
          itemVenda.setPreco(itens.preco());
          itemVenda.setQuantidade(itens.quantidade());
          pedido.getItemDaVenda().add(itemVenda);
        }

        pedido.setStatusDoPedido(new ArrayList<StatusDoPedido>());
        StatusDoPedido statusVenda = new StatusDoPedido();
        statusVenda.setDataHora(LocalDateTime.now());
        statusVenda.setStatus(Status.valueOf(0));
        pedido.getStatusDoPedido().add(statusVenda);

        cliente.getListaPedido().add(pedido);
      }
    }

    repository.persist(cliente);
    return ClienteResponseDTO.valueOf(cliente);
  }
}
