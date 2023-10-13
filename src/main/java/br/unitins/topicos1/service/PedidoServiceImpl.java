package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.unitins.topicos1.TrataErro.CriaPedido;
import br.unitins.topicos1.TrataErro.DeletePedido;
import br.unitins.topicos1.dto.ItemDaVendaDTO;
import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.dto.PedidoResponseDTO;
import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.FormaDePagamento;
import br.unitins.topicos1.model.ItemDaVenda;
import br.unitins.topicos1.model.Pedido;
import br.unitins.topicos1.model.Produto;
import br.unitins.topicos1.model.Status;
import br.unitins.topicos1.model.StatusDoPedido;
import br.unitins.topicos1.repository.ClienteRepository;
import br.unitins.topicos1.repository.EnderecoRepository;
import br.unitins.topicos1.repository.PedidoRepository;
import br.unitins.topicos1.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

  @Inject
  ClienteRepository repository;

  @Inject
  PedidoRepository repositoryPedido;

  @Inject
  ProdutoRepository repositoryProduto;

  @Inject
  EnderecoRepository repositoryEndereco;

  @Override
  public CriaPedido insert(PedidoDTO dto, Long id) {

   
    CriaPedido trataErroId = new CriaPedido();
    
    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    trataErroId.verificaUsuario1(id);
    if(!trataErroId.isCriou()) {
      return(trataErroId);
    }
    
    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(id);
    trataErroId.verificaUsuario2(cliente);
    if(!trataErroId.isCriou()) {
      return(trataErroId);
    }

    Pedido pedido = new Pedido();
    //pedido.setCodigoDeRastreamento(dto.codigoDeRastreamento());

    FormaDePagamento pagamento = new FormaDePagamento();
    pagamento.setNome(dto.formaDePagamento().nome());
    pedido.setFormaDePagamento(pagamento);


    // Verifica se o id fornecido para o endereço de entrega do pedido é nulo
    if(dto.idEndereco() != null || dto.idEndereco() < 1) {
    // Caso não queira utilizar um endereço já cadastrado no banco de dados do cliente é necessário fixar a variável idEndereco para um valor abaixo de 1. Caso contrário, precisa fixar o valor da variável idEndereco com o id de algum endereço válido vinculado ao cliente. 
   
      Endereco endereco = repositoryEndereco.findById(dto.idEndereco());
      CriaPedido trataErro = CriaPedido.verificaEnderecoCliente(cliente, endereco);
      // Retorna true se o dto.idEndereco() é de um endereço que pertença ao cliente.
      if(trataErro.isCriou()) { 
      pedido.setEndereco(endereco);
      } else {
        return trataErro;
      } 
    } else {
      CriaPedido trataErro = new CriaPedido(false, "Id do Endereço é nulo ou possui valor menor que 1!");
    return trataErro;
  }

    pedido.setItemDaVenda(new ArrayList<ItemDaVenda>());
    for (ItemDaVendaDTO idv : dto.itemDaVenda()) {
      ItemDaVenda item = new ItemDaVenda();
      item.setPreco(idv.preco());
      item.setQuantidade(idv.quantidade());
      Produto produto = repositoryProduto.findById((Long)idv.idProduto());
      CriaPedido trataErro = CriaPedido.temEmEstoque(produto.getQuantidade(), idv.quantidade());
      if(trataErro.isCriou()) {
        produto.setQuantidade(produto.getQuantidade() - idv.quantidade());
        try {
          repositoryProduto.persist(produto);
        } catch (Exception e) {
          CriaPedido trataErroPedido = new CriaPedido(false, "Erro ao alterar o produto no banco de dados! " + e.getMessage());
          return trataErroPedido;
        }
        
        item.setProduto(produto);
        pedido.getItemDaVenda().add(item);
      } else {
        return trataErro;
      }
    }
      /* Produto produto = new Produto();
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
        } */
    

    pedido.setStatusDoPedido(new ArrayList<StatusDoPedido>());
    StatusDoPedido status = new StatusDoPedido();
    status.setDataHora(LocalDateTime.now());
    status.setStatus(Status.valueOf(0));
    pedido.getStatusDoPedido().add(status);

    cliente.getListaPedido().add(pedido);

    // Verifica se Cliente foi persistido no banco de dados.
    try {
      repository.persist(cliente);
    } catch (Exception e) {
      CriaPedido trataErro = new CriaPedido(false, "Erro ao gravar o pedido no banco de dados! " + e.getMessage());
      return trataErro;
    }

    CriaPedido trataErro = new CriaPedido(true, "Pedido realizado com sucesso!");
    return trataErro;
  }

  @Override
  @Transactional
  public DeletePedido deletePedidoByCliente(Long idCliente, Long idPedido) {
    Cliente cliente = repository.findById(idCliente);
    Pedido pedido = repositoryPedido.findById(idPedido);

    if(pedido != null) {
      DeletePedido deletaPedido = DeletePedido.podeDeletar(cliente, pedido);
      if(deletaPedido.isDeletou()) {
        // Primeiro removo o pedido da lista de pedidos do cliente
        cliente.getListaPedido().remove(pedido);
        // Depois persito a alteração no banco
        repositoryPedido.delete(pedido);
        return deletaPedido;
      } else {
        return deletaPedido;
      }
      } else {
        return new DeletePedido(false, "Este cliente não possui nenhum pedido para excluir!");
    }
  }

  @Override
  @Transactional
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
