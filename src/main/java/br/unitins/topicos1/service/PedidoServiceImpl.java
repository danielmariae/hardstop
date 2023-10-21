package br.unitins.topicos1.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.unitins.topicos1.TrataErro.CriaPedido;
import br.unitins.topicos1.TrataErro.DeletePedido;
import br.unitins.topicos1.application.GeneralErrorException;
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
    if(dto.idEndereco() != null || dto.idEndereco() > 1) {
    // Caso não queira utilizar um endereço já cadastrado no banco de dados do cliente é necessário fixar a variável idEndereco para um valor abaixo de 1. Caso contrário, precisa fixar o valor da variável idEndereco com o id de algum endereço válido vinculado ao cliente. 
   
      Endereco endereco = repositoryEndereco.findById(dto.idEndereco());
      
      // Retorna true se o dto.idEndereco() é de um endereço que pertença ao cliente.
      if(verificaEnderecoCliente(cliente, endereco)) { 
      pedido.setEndereco(endereco);
      } else {
        throw new GeneralErrorException("400", "Bad Request", "PedidoServiceImpl(insert)", "O id passado como índice de endereço não pertence ao cliente!");
      } 
    } else {
      throw new GeneralErrorException("400", "Bad Request", "PedidoServiceImpl(insert)", "id do Endereço é nulo ou possui valor menor que 1!");
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

  public CriaPedido updateEndereco(PedidoPatchEnderecoDTO dto, Long id){
    Cliente cliente = repository.findById(id);
    Pedido pedido = repositoryPedido.findById(dto.idPedido());
    CriaPedido cp = new CriaPedido();

    Integer chave = 0;
    for(Pedido pedidot : cliente.getListaPedido()) {
      if(pedidot.getId() == pedido.getId()) {
        chave = 1;
      }
    }

    Integer chave2 = 0;
    pedido.setId(dto.idEndereco());
    if(chave == 1) {
      for(Endereco end : cliente.getListaEndereco()) {
        if(end.getId() == pedido.getEndereco().getId()){
          chave2 = 1;
        }
      }
    } else {
      cp.setCriou(false);
      cp.setMensagem("O pedido em questão não é do Cliente!");
      return cp;
    }
    
    if(chave2 == 1) {
      cp.setPedido(PedidoResponseDTO.valueOf(pedido));
      return cp;
    } else {
      cp.setCriou(false);
      cp.setMensagem("O endereço escolhido não pertence do Cliente!");
      return cp;
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



}
