package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.EnderecoResponseDTO;
import br.unitins.topicos1.dto.ItemDaVendaDTO;
import br.unitins.topicos1.dto.PedidoDTO;
import br.unitins.topicos1.dto.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.PedidoPatchStatusDTO;
import br.unitins.topicos1.dto.PedidoResponseDTO;
import br.unitins.topicos1.model.Boleto;
import br.unitins.topicos1.model.CartaoDeCredito;
import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.ItemDaVenda;
import br.unitins.topicos1.model.ModalidadePagamento;
import br.unitins.topicos1.model.Pedido;
import br.unitins.topicos1.model.Pix;
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
  public PedidoResponseDTO insert(PedidoDTO dto, Long id) {
    
    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if(!verificaUsuario1(id)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "id do usuário é nulo ou tem valor inferior a 1.");
    }
    
    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(id);
    if(!verificaUsuario2(cliente)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "id do usuário não existe no banco de dados.");
    }

    Pedido pedido = new Pedido();
    //pedido.setCodigoDeRastreamento(dto.codigoDeRastreamento());

    pedido.setItemDaVenda(new ArrayList<ItemDaVenda>());
    Double valorCompra = 0.0;
    for (ItemDaVendaDTO idv : dto.itemDaVenda()) {
      ItemDaVenda item = new ItemDaVenda();
      item.setPreco(idv.preco());
      item.setQuantidade(idv.quantidade());
      Produto produto = repositoryProduto.findById((Long)idv.idProduto());
      if(temEmEstoque(produto.getQuantidade(), idv.quantidade())) {
        produto.setQuantidade(produto.getQuantidade() - idv.quantidade());
        try {
          repositoryProduto.persist(produto);
        } catch (Exception e) {
          throw new GeneralErrorException("500", "Server Error", "PedidoServiceImpl(insert)", "Erro ao alterar o produto no banco de dados! " + e.getMessage());
        }
        
        item.setProduto(produto);
        pedido.getItemDaVenda().add(item);
        valorCompra = valorCompra + idv.quantidade() * idv.preco();
      } else {
        throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "Quantidade de produto indisponível!");
      }
    }

    if(dto.formaDePagamento().modalidade() == 0) {
      CartaoDeCredito pagamento = new CartaoDeCredito();
      pagamento.setModalidade(ModalidadePagamento.valueOf(dto.formaDePagamento().modalidade()));
      pagamento.setValorPago(valorCompra);
      pagamento.setNumeroCartao(dto.formaDePagamento().numeroCartao());
      pagamento.setMesValidade(dto.formaDePagamento().mesValidade());
      pagamento.setAnoValidade(dto.formaDePagamento().anoValidade());
      pagamento.setCodSeguranca(dto.formaDePagamento().codSeguranca());
      pagamento.setDataHoraPagamento(LocalDateTime.now());
      pedido.setFormaDePagamento(pagamento);
    }else if(dto.formaDePagamento().modalidade() == 1) {
      Boleto pagamento = new Boleto();
      pagamento.setValorPago(valorCompra);
      pagamento.setModalidade(ModalidadePagamento.valueOf(dto.formaDePagamento().modalidade()));
      pagamento.setNomeBanco(dto.formaDePagamento().nomeBanco());
      pagamento.setDataHoraGeracao(LocalDateTime.now());
      LocalDateTime pegDateTime = LocalDateTime.now();
      LocalDateTime limite = pegDateTime.plusDays(15);
      limite = limite.with(LocalTime.of(23,59,59));
      pagamento.setDataHoraLimitePag(limite);
      pedido.setFormaDePagamento(pagamento);
    } else if(dto.formaDePagamento().modalidade() == 2) {
      Pix pagamento = new Pix();
      pagamento.setModalidade(ModalidadePagamento.valueOf(dto.formaDePagamento().modalidade()));
      pagamento.setValorPago(valorCompra);
      pagamento.setNomeCliente(dto.formaDePagamento().nomeCliente());
      pagamento.setNomeRecebedor(dto.formaDePagamento().nomeRecebedor());
      pagamento.setChaveRecebedor(dto.formaDePagamento().chaveRecebedor());
      pagamento.setDataHoraGeracao(LocalDateTime.now());
      pedido.setFormaDePagamento(pagamento);
    } else {
      throw new GeneralErrorException("400", "Bad Request", "PedidoServiceImpl(insert)", "O id passado como índice de forma de pagamento não existe! Os ids válidos são 0,1 ou 2.");
    }
    
    
    // Verifica se o id fornecido para o endereço de entrega do pedido é nulo
    if(!verificaEndereco1(id)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "id do endereço é nulo ou tem valor inferior a 1.");
    }
   
      Endereco endereco = repositoryEndereco.findById(dto.idEndereco());

      // Verifica se o id fornecido aponta para um endereço que existe no banco de dados.
      if(!verificaEndereco2(endereco)) {
        throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "id do endereço não existe no banco de dados.");
      }
      
      // Retorna true se o dto.idEndereco() é de um endereço que pertença ao cliente.
      if(verificaEnderecoCliente(cliente, endereco)) { 
      pedido.setEndereco(endereco);
      } else {
        throw new GeneralErrorException("400", "Bad Request", "PedidoServiceImpl(insert)", "O id passado como índice de endereço não pertence ao cliente!");
      } 

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
    throw new GeneralErrorException("500", "Server error", "PedidoServiceImpl(insert)", "Não consegui gravar o pedido para este cliente no banco de dados!");
    }

    return PedidoResponseDTO.valueOf(pedido);
  }

  @Override
  @Transactional
  public void deletePedidoByCliente(Long idCliente, Long idPedido) {

// Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if(!verificaUsuario1(idCliente)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(deletePedidoByCliente)", "id do usuário é nulo ou tem valor inferior a 1.");
    }
    
    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(idCliente);
    if(!verificaUsuario2(cliente)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(deletePedidoByCliente)", "id do usuário não existe no banco de dados.");
    }

    // Verifica o id do pedido. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if(!verificaPedido1(idPedido)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(deletePedidoByCliente)", "id do pedido é nulo ou tem valor inferior a 1.");
    }
    
    // Verifica o pedido. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Pedido pedido = repositoryPedido.findById(idPedido);
    if(!verificaPedido2(pedido)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(deletePedidoByCliente)", "id do pedido não existe no banco de dados.");
    }
    
      if(podeDeletar(cliente, pedido)) {
        throw new GeneralErrorException("400", "Bad Request", "PedidoServiceImpl(deletePedidoByCliente)", "O pedido não pode ser desfeito!");
      }
        // Primeiro removo o pedido da lista de pedidos do cliente
        cliente.getListaPedido().remove(pedido);
      
        try {
        // Persito a alteração no banco
        repositoryPedido.delete(pedido);
        } catch (Exception e) {
          throw new GeneralErrorException("500", "Server Error", "PedidoServiceImpl(deletePedidoByCliente)", "Não consegui apagar o pedido no banco de dados!");
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

  public EnderecoResponseDTO updateEndereco(PedidoPatchEnderecoDTO dto, Long id){
    Cliente cliente = repository.findById(id);
    Pedido pedido = repositoryPedido.findById(dto.idPedido());

    Integer chave = 0;
    for(Pedido pedidot : cliente.getListaPedido()) {
      if(pedidot.getId() == pedido.getId()) {
        chave = 1;
      }
    }

    Integer chave2 = 0;
    if(chave == 1) {
      for(Endereco end : cliente.getListaEndereco()) {
        if(end.getId() == dto.idEndereco()){
          chave2 = 1;
        }
      }
    } else {
      throw new GeneralErrorException("400", "Bad Request", "PedidoServiceImpl(insert)", "O pedido em questão não é do Cliente!");
    }
    
    if(chave2 == 1) {
      pedido.setEndereco(repositoryEndereco.findById(dto.idEndereco()));
      repositoryPedido.persist(pedido);
      return EnderecoResponseDTO.valueOf(pedido.getEndereco());
    } else {
      throw new GeneralErrorException("400", "Bad Request", "PedidoServiceImpl(insert)", "O endereço escolhido não pertence do Cliente!");
    }
  }

  private Boolean verificaEnderecoCliente(Cliente cliente, Endereco endereco) {
    for (Endereco end : cliente.getListaEndereco()) {
        // O pedido repassado ao método pertence ao Cliente repassado ao método.
        if (end.getId() == endereco.getId()) {
            return true;
        }
    }
    return false;
}

private Boolean temEmEstoque(Integer num_produtos_banco, Integer num_produtos_pedido) {
  Integer sub;
  sub = num_produtos_banco - num_produtos_pedido;
  // Existem produtos em quantidade adequada no banco
  if(sub >= 0){
     
      return true;
  // Não existem produtos em quantidade adequada no banco
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

private Boolean verificaEndereco1(Long id) {

  if(id == null) {
      return false;
  }

  if(id < 1) {
      return false;
  }

  return true;
}

private Boolean verificaEndereco2(Endereco endereco) {

  if(endereco == null) {
      return false;
  } else {
      return true;
  }
}

private Boolean verificaPedido1(Long id) {

  if(id == null) {
      return false;
  }

  if(id < 1) {
      return false;
  }

  return true;
}

private Boolean verificaPedido2(Pedido pedido) {

  if(pedido == null) {
      return false;
  } else {
      return true;
  }
}



private Boolean podeDeletar(Cliente cliente, Pedido pedido) {
  for (Pedido pedidoteste : cliente.getListaPedido()) {
    // O pedido repassado ao método pertence ao Cliente repassado ao método.
    if (pedidoteste.getId() == pedido.getId()) {
      // Coletando o Status de número mais elevado neste pedido
      Integer chaveDelecao = 0;
      for (StatusDoPedido statusPedido : pedido.getStatusDoPedido()) {
        if (chaveDelecao < statusPedido.getStatus().getId()) {
          chaveDelecao = statusPedido.getStatus().getId();
        }
      }

      // Pedidos com status do tipo: AGUARDANDO_PAGAMENTO ou PAGAMENTO_NÃO_AUTORIZADO podem ser deletados
      if (chaveDelecao == 0) {

        //Em caso de compras no cartão de crédito precisa primeiro comunicar a financeira antes de permitir essa deleção! Caso a financeira desfaça a ordem de pagamento, aí essa deleção será permitida. Caso a compra seja na forma de boleto ou pix o pedido poderá ser deletado imediatamente.
        if(pedido.getFormaDePagamento().getModalidade().getId() != 0) {
          // Pedido AGUARDANDO_PAGAMENTO foi excluído com sucesso! Em caso do pagamento escolhido tiver sido cartão de crédito é necessária comunicação com a financeira para concluir esta operação.
          return true;
        
        } else {
          // IMPORTANTE!!!!! Chama função que comunica a financeira o desfazimento da negociação e aguarda a confirmação da financeira para deletar o pedido.
          return true;
      
        }
      } else if (chaveDelecao == 1) {
        // Pedido PAGAMENTO_NÃO_AUTORIZADO foi excluído com sucesso!
        return true;
          
        // Pedidos com status do tipo: PAGO, SEPARADO_DO_ESTOQUE, ENTREGUE_A_TRANSPORTADORA e ENTREGUE não podem ser deletados
      } else if (chaveDelecao == 2) {
        return false;
      } else if (chaveDelecao == 3) {
        return false;
      } else if (chaveDelecao == 4) {
        return false;
      } else if (chaveDelecao == 5) {
        return false;
      }
    } 
  }
  // O pedido repassado ao método não pertence ao Cliente repassado ao método.
  return false;
}




}
