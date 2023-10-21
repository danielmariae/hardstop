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
  public PedidoResponseDTO insert(PedidoDTO dto, Long id) {
    
    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if(!verificaUsuario1(id)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "id do usuário é nulo ou tem valor inferior a 1. " + e.getMessage());
    }
    
    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(id);
    if(!verificaUsuario2(cliente)) {
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "id do usuário não existe no banco de dados. " + e.getMessage());
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
      throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "id do endereço é nulo ou tem valor inferior a 1. " + e.getMessage());
    }
   
      Endereco endereco = repositoryEndereco.findById(dto.idEndereco());

      // Verifica se o id fornecido aponta para um endereço que existe no banco de dados.
      if(!verificaEndereco2(endereco)) {
        throw new GeneralErrorException("400", "Bad Resquest", "PedidoServiceImpl(insert)", "id do endereço não existe no banco de dados. " + e.getMessage());
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

}
