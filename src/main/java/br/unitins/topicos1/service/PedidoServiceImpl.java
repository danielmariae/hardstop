package br.unitins.topicos1.service;

import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.ItemDaVendaDTO;
import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.dto.PedidoResponseDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.FormaDePagamento;
import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.ItemDaVenda;
import br.unitins.topicos1.model.Lote;
import br.unitins.topicos1.model.Pedido;
import br.unitins.topicos1.model.Produto;
import br.unitins.topicos1.model.Status;
import br.unitins.topicos1.model.StatusDoPedido;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.TipoTelefone;
import br.unitins.topicos1.repository.ClienteRepository;
import br.unitins.topicos1.repository.PedidoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PathParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

  @Inject
  ClienteRepository repository;

  @Inject
  PedidoRepository repositoryPedido;

  @Inject
  FornecedorRepository repositoryFornecedor;

  @Override
  public PedidoResponseDTO insert(PedidoDTO dto, @PathParam("id") Long id) {
    Cliente cliente = repository.findById(id);
    Pedido pedido = new Pedido();

    pedido.setCodigoDeRastreamento(dto.codigoDeRastreamento());
    FormaDePagamento pagamento = new FormaDePagamento();
    pagamento.setNome(dto.formaDePagamento().nome());
    pedido.setFormaDePagamento(pagamento);
    Endereco endereco = new Endereco();
    endereco.setNome(dto.endereco().nome());
    endereco.setRua(dto.endereco().rua());
    endereco.setNumero(dto.endereco().numero());
    endereco.setLote(dto.endereco().lote());
    endereco.setBairro(dto.endereco().bairro());
    endereco.setComplemento(dto.endereco().complemento());
    endereco.setCep(dto.endereco().cep());
    endereco.setMunicipio(dto.endereco().municipio());
    endereco.setEstado(dto.endereco().estado());
    endereco.setPais(dto.endereco().pais());
    pedido.setEndereco(endereco);

    pedido.setItemDaVenda(new ArrayList<ItemDaVenda>());
    for (ItemDaVendaDTO idv : dto.itemDaVenda()) {
      ItemDaVenda item = new ItemDaVenda();
      item.setPreco(idv.preco());
      item.setQuantidade(idv.quantidade());
      Produto produto = new Produto();
      produto.setDescricao(idv.produto().descricao());
      produto.setCodigoBarras(idv.produto().codigoBarras());
      produto.setMarca(idv.produto().marca());
      produto.setAltura(idv.produto().altura());
      produto.setLargura(idv.produto().largura());
      produto.setComprimento(idv.produto().comprimento());
      produto.setPeso(idv.produto().peso());
      produto.setCustoCompra(idv.produto().custoCompra());
      produto.setValorVenda(idv.produto().valorVenda());
      produto.setQuantidade(idv.produto().quantidade());
      produto.setListaLote(new ArrayList<Lote>());

      if (idv.produto().listaLote() != null && (!idv.produto().listaLote().isEmpty())) {
        for (LoteDTO dll : idv.produto().listaLote()) {
          Lote lote = new Lote();
          lote.setLote(dll.lote());
          Fornecedor fornecedor = new Fornecedor();
          fornecedor.setCnpj(dll.fornecedor().cnpj());
          fornecedor.setNomeFantasia(dll.fornecedor().nomeFantasia());
          fornecedor.setEndSite(dll.fornecedor().endSite());

          if (dll.fornecedor().listaEndereco() != null && (!dll.fornecedor().listaEndereco().isEmpty())) {
            fornecedor.setListaEndereco(new ArrayList<Endereco>());
            for (EnderecoDTO endf : dll.fornecedor().listaEndereco()) {
              Endereco endereco1 = new Endereco();
              endereco1.setNome(endf.nome());
              endereco1.setRua(endf.rua());
              endereco1.setNumero(endf.numero());
              endereco1.setLote(endf.lote());
              endereco1.setBairro(endf.bairro());
              endereco1.setComplemento(endf.complemento());
              endereco1.setCep(endf.cep());
              endereco1.setMunicipio(endf.municipio());
              endereco1.setEstado(endf.estado());
              endereco1.setPais(endf.pais());
              fornecedor.getListaEndereco().add(endereco1);
            }
          }

          if (dll.fornecedor().listaTelefone() != null && (!dll.fornecedor().listaTelefone().isEmpty())) {
            fornecedor.setListaTelefone(new ArrayList<Telefone>());
            for (TelefoneDTO telf : dll.fornecedor().listaTelefone()) {
              Telefone telefone = new Telefone();
              telefone.setNumeroTelefone(telf.numeroTelefone());
              telefone.setDdd(telf.ddd());
              telefone.setTipoTelefone(TipoTelefone.valueOf(telf.tipo()));
              fornecedor.getListaTelefone().add(telefone);
            }
          }
          lote.setFornecedor(fornecedor);
          produto.getListaLote().add(lote);
        }
      }
      item.setProduto(produto);
      pedido.getItemDaVenda().add(item);
    }

    pedido.setStatusDoPedido(new ArrayList<StatusDoPedido>());
    StatusDoPedido status = new StatusDoPedido();
    status.setDataHora(LocalDateTime.now());
    status.setStatus(Status.valueOf(0));
    pedido.getStatusDoPedido().add(status);

    cliente.setListaPedido(new ArrayList<Pedido>());
    cliente.getListaPedido().add(pedido);
    //repositoryPedido.persist(pedido);
    //repository.persist(cliente);
    return PedidoResponseDTO.valueOf(pedido);
  }

  @Override
  @Transactional
  public void deletePedidoByCliente(Long idCliente, Long idProduto) {
    Cliente cliente = repository.findById(idCliente);
    for (Pedido pedido : cliente.getListaPedido()) {
      if (pedido.getId() == idProduto) {
        for (StatusDoPedido status : pedido.getStatusDoPedido()) {
          repositoryPedido.deleteById(status.getId());
        }
        for (ItemDaVenda item : pedido.getItemDaVenda()) {
          repositoryPedido.deleteById(item.getId());
        }

        repositoryPedido.deleteById(pedido.getFormaDePagamento().getId());
        repositoryPedido.deleteById(pedido.getEndereco().getId());
        repositoryPedido.deleteById(pedido.getId());
      }
    }
  }

  @Override
  public PedidoResponseDTO updateStatusDoPedido(PedidoPatchStatusDTO ppsdto) {
    Pedido pedido = repositoryPedido.findById(ppsdto.idPedido());

    StatusDoPedido statusVenda = new StatusDoPedido();
    statusVenda.setDataHora(LocalDateTime.now());
    statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
    pedido.getStatusDoPedido().add(statusVenda);
    repositoryPedido.persist(pedido);
    return PedidoResponseDTO.valueOf(pedido);
  }

  @Override
  public List<PedidoResponseDTO> findPedidoByCliente(Long idcliente) {
    Cliente cliente = repository.findById(idcliente);
    return cliente
      .getListaPedido()
      .stream()
      .map(p -> PedidoResponseDTO.valueOf(p))
      .toList();
  }
}
