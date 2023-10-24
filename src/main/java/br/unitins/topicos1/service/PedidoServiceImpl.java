package br.unitins.topicos1.service;

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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

  @PersistenceUnit
  EntityManagerFactory emf;

  @Override
  public PedidoResponseDTO insert(PedidoDTO dto, Long id) {
    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaUsuario1(id)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(insert)",
        "id do usuário é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(id);
    if (!verificaUsuario2(cliente)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(insert)",
        "id do usuário não existe no banco de dados."
      );
    }

    Pedido pedido = new Pedido();
    //pedido.setCodigoDeRastreamento(dto.codigoDeRastreamento());

    pedido.setItemDaVenda(new ArrayList<ItemDaVenda>());
    Double valorCompra = 0.0;
    for (ItemDaVendaDTO idv : dto.itemDaVenda()) {
      ItemDaVenda item = new ItemDaVenda();
      item.setPreco(idv.preco());
      item.setQuantidade(idv.quantidade());

      // Verifica o id do prduto. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
      if (!verificaProduto1(idv.idProduto())) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(insert)",
          "id do produto é nulo ou tem valor inferior a 1."
        );
      }

      // Verifica o pedido. Caso o id inexista no banco de dados, o sistema não realiza a operação.
      Produto produto = repositoryProduto.findById(idv.idProduto());
      if (!verificaProduto2(produto)) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(insert)",
          "id do produto não existe no banco de dados."
        );
      }

      if (!temEmEstoque(produto.getQuantidade(), idv.quantidade())) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(insert)",
          "Inexiste esta quantidade de produto em estoque."
        );
      }

      // Analisa se tem o produto em estoque e 
      EntityManager em = emf.createEntityManager();
      EntityTransaction transaction = em.getTransaction();

      try {
        transaction.begin();
        String sql1 = "SELECT quantidade FROM produto WHERE id = ?1 FOR UPDATE";
        Query query = em
          .createNativeQuery(sql1)
          .setParameter(1, idv.idProduto());
        Integer quantidade = (Integer) query.getSingleResult();
        Integer quantFinal = quantidade - idv.quantidade();
        if (quantFinal >= 0) {
          /* String sql3 =
            "INSERT INTO itemDaVenda (preco, quantidade, produto_id) VALUES (?1, ?2, ?3)";
          em
            .createNativeQuery(sql3)
            .setParameter(1, idv.preco())
            .setParameter(2, idv.quantidade())
            .setParameter(3, idv.idProduto())
            .executeUpdate(); */
          String sql4 = "UPDATE produto SET quantidade = ?1 WHERE id = ?2";
          em
            .createNativeQuery(sql4)
            .setParameter(1, quantFinal)
            .setParameter(2, idv.idProduto())
            .executeUpdate();
          transaction.commit();
          item.setProduto(produto);
          pedido.getItemDaVenda().add(item);
          valorCompra = valorCompra + idv.quantidade() * idv.preco();
        } else {
          transaction.rollback();
          throw new GeneralErrorException(
            "400",
            "Bad Resquest",
            "PedidoServiceImpl(insert)",
            "Não existe quantidade suficiente deste produto em estoque."
          );
        }
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
          throw new GeneralErrorException(
            "400",
            "Bad Resquest",
            "PedidoServiceImpl(insert)",
            "Não consegui gravar no banco. " + e.getMessage()
          );
        }
      } finally {
        em.close();
      }
    }

    if (dto.formaDePagamento().modalidade() == 0) {
      CartaoDeCredito pagamento = new CartaoDeCredito();
      pagamento.setModalidade(
        ModalidadePagamento.valueOf(dto.formaDePagamento().modalidade())
      );
      pagamento.setValorPago(valorCompra);
      pagamento.setNumeroCartao(dto.formaDePagamento().numeroCartao());
      pagamento.setMesValidade(dto.formaDePagamento().mesValidade());
      pagamento.setAnoValidade(dto.formaDePagamento().anoValidade());
      pagamento.setCodSeguranca(dto.formaDePagamento().codSeguranca());
      pagamento.setDataHoraPagamento(LocalDateTime.now());
      pedido.setFormaDePagamento(pagamento);
    } else if (dto.formaDePagamento().modalidade() == 1) {
      Boleto pagamento = new Boleto();
      pagamento.setValorPago(valorCompra);
      pagamento.setModalidade(
        ModalidadePagamento.valueOf(dto.formaDePagamento().modalidade())
      );
      pagamento.setNomeBanco(dto.formaDePagamento().nomeBanco());
      pagamento.setDataHoraGeracao(LocalDateTime.now());
      LocalDateTime pegDateTime = LocalDateTime.now();
      LocalDateTime limite = pegDateTime.plusDays(15);
      limite = limite.with(LocalTime.of(23, 59, 59));
      pagamento.setDataHoraLimitePag(limite);
      pedido.setFormaDePagamento(pagamento);
    } else if (dto.formaDePagamento().modalidade() == 2) {
      Pix pagamento = new Pix();
      pagamento.setModalidade(
        ModalidadePagamento.valueOf(dto.formaDePagamento().modalidade())
      );
      pagamento.setValorPago(valorCompra);
      pagamento.setNomeCliente(dto.formaDePagamento().nomeCliente());
      pagamento.setNomeRecebedor(dto.formaDePagamento().nomeRecebedor());
      pagamento.setChaveRecebedor(dto.formaDePagamento().chaveRecebedor());
      pagamento.setDataHoraGeracao(LocalDateTime.now());
      pedido.setFormaDePagamento(pagamento);
    } else {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "PedidoServiceImpl(insert)",
        "O id passado como índice de forma de pagamento não existe! Os ids válidos são 0,1 ou 2."
      );
    }

    // Verifica se o id fornecido para o endereço de entrega do pedido é nulo
    if (!verificaEndereco1(id)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(insert)",
        "id do endereço é nulo ou tem valor inferior a 1."
      );
    }

    Endereco endereco = repositoryEndereco.findById(dto.idEndereco());

    // Verifica se o id fornecido aponta para um endereço que existe no banco de dados.
    if (!verificaEndereco2(endereco)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(insert)",
        "id do endereço não existe no banco de dados."
      );
    }

    // Retorna true se o dto.idEndereco() é de um endereço que pertença ao cliente.
    if (verificaEnderecoCliente(cliente, endereco)) {
      pedido.setEndereco(endereco);
    } else {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "PedidoServiceImpl(insert)",
        "O id passado como índice de endereço não pertence ao cliente!"
      );
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
      throw new GeneralErrorException(
        "500",
        "Server error",
        "PedidoServiceImpl(insert)",
        "Não consegui gravar o pedido para este cliente no banco de dados!"
      );
    }

    return PedidoResponseDTO.valueOf(pedido);
  }

  @Override
  @Transactional
  public void deletePedidoByCliente(Long idCliente, Long idPedido) {
    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaUsuario1(idCliente)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "id do usuário é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(idCliente);
    if (!verificaUsuario2(cliente)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "id do usuário não existe no banco de dados."
      );
    }

    // Verifica o id do pedido. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaPedido1(idPedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "id do pedido é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o pedido. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Pedido pedido = repositoryPedido.findById(idPedido);
    if (!verificaPedido2(pedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "id do pedido não existe no banco de dados."
      );
    }

    if (podeDeletar(cliente, pedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "O pedido não pode ser desfeito!"
      );
    }
    // Primeiro removo o pedido da lista de pedidos do cliente
    cliente.getListaPedido().remove(pedido);

    try {
      // Persito a alteração no banco
      repositoryPedido.delete(pedido);
    } catch (Exception e) {
      throw new GeneralErrorException(
        "500",
        "Server Error",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "Não consegui apagar o pedido no banco de dados!"
      );
    }
  }

  @Override
  @Transactional
  public PedidoResponseDTO updateStatusDoPedido(PedidoPatchStatusDTO ppsdto) {
    // Verifica o id do pedido. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaPedido1(ppsdto.idPedido())) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateStatusDoPedido)",
        "id do pedido é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o pedido. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Pedido pedido = repositoryPedido.findById(ppsdto.idPedido());
    if (!verificaPedido2(pedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateStatusDoPedido)",
        "id do pedido não existe no banco de dados."
      );
    }

    StatusDoPedido statusVenda = new StatusDoPedido();
    statusVenda.setDataHora(LocalDateTime.now());

    // Coletando o Status de número mais elevado neste pedido
    Integer status = 0;
    for (StatusDoPedido statusPedido : pedido.getStatusDoPedido()) {
      if (status < statusPedido.getStatus().getId()) {
        status = statusPedido.getStatus().getId();
      }
    }

    if (ppsdto.idStatus() <= status) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateStatusDoPedido)",
        "id do status é inferior ou igual ao status atual."
      );
    }

    // Os status são do tipo: AGUARDANDO_PAGAMENTO (0), PAGAMENTO_NÃO_AUTORIZADO (1), PAGO (2), SEPARADO_DO_ESTOQUE (3), ENTREGUE_A_TRANSPORTADORA (4) e ENTREGUE (5).
    if (status == 0) {
      statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
      pedido.getStatusDoPedido().add(statusVenda);
      try {
        repositoryPedido.persist(pedido);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "PedidoServiceImpl(updateStatusDoPedido)",
          "Não consegui persistir o pedido no banco de dados!"
        );
      }
      return PedidoResponseDTO.valueOf(pedido);
    } else if (status == 1) {
      // IMPORTANTE!!! Este status será gerado por comunicação com a administradora do cartão de crédito que negou o pedido ou por perder o prazo do pagamento no caso de ser pix ou boleto.
      statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
      pedido.getStatusDoPedido().add(statusVenda);
      try {
        repositoryPedido.persist(pedido);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "PedidoServiceImpl(updateStatusDoPedido)",
          "Não consegui persistir o pedido no banco de dados!"
        );
      }
      return PedidoResponseDTO.valueOf(pedido);
    } else if (status == 2) {
      statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
      pedido.getStatusDoPedido().add(statusVenda);
      try {
        repositoryPedido.persist(pedido);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "PedidoServiceImpl(updateStatusDoPedido)",
          "Não consegui persistir o pedido no banco de dados!"
        );
      }
      return PedidoResponseDTO.valueOf(pedido);
    } else if (status == 3) {
      statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
      pedido.getStatusDoPedido().add(statusVenda);
      try {
        repositoryPedido.persist(pedido);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "PedidoServiceImpl(updateStatusDoPedido)",
          "Não consegui persistir o pedido no banco de dados!"
        );
      }
      return PedidoResponseDTO.valueOf(pedido);
    } else if (status == 4) {
      statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
      pedido.getStatusDoPedido().add(statusVenda);
      // IMPORTANTE!! Precisa gerar o cógigo de rastreamento via conexão com a empresa de logística.
      pedido.setCodigoDeRastreamento(ppsdto.codigoDeRastreamento());
      try {
        repositoryPedido.persist(pedido);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "PedidoServiceImpl(updateStatusDoPedido)",
          "Não consegui persistir o pedido no banco de dados!"
        );
      }
      return PedidoResponseDTO.valueOf(pedido);
    } else if (status == 5) {
      statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
      pedido.getStatusDoPedido().add(statusVenda);
      try {
        repositoryPedido.persist(pedido);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "PedidoServiceImpl(updateStatusDoPedido)",
          "Não consegui persistir o pedido no banco de dados!"
        );
      }
      return PedidoResponseDTO.valueOf(pedido);
    }

    throw new GeneralErrorException(
      "500",
      "Server Error",
      "PedidoServiceImpl(updateStatusDoPedido)",
      "Aconteceu algum erro inesperado!"
    );
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

  public EnderecoResponseDTO updateEndereco(
    PedidoPatchEnderecoDTO dto,
    Long id
  ) {
    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaUsuario1(id)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateEndereco)",
        "id do usuário é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(id);
    if (!verificaUsuario2(cliente)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateEndereco)",
        "id do usuário não existe no banco de dados."
      );
    }

    // Verifica o id do pedido. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaPedido1(dto.idPedido())) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateEndereco)",
        "id do pedido é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o pedido. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Pedido pedido = repositoryPedido.findById(dto.idPedido());
    if (!verificaPedido2(pedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateEndereco)",
        "id do pedido não existe no banco de dados."
      );
    }

    // Verifica se o pedido pertence ao cliente. Caso o pedido não pertença ao cliente, o sistema não realiza a operação.
    if (!verificaPedido3(cliente, pedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateEndereco)",
        "Este pedido não pertence a este cliente."
      );
    }

    // Verifica se o endereço pertence ao cliente. Caso o endereço não pertença ao cliente, o sistema não realiza a operação.
    if (!verificaEndereco3(cliente, dto.idEndereco())) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateEndereco)",
        "O novo endereço não pertence a este cliente."
      );
    }

    // Verifica se o endereço pode ser alterado de acordo com o status do pedido.
    if (!podeTrocarEndereco(pedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateEndereco)",
        "O pedido não pode ter seu endereço alterado porque já foi entregue a transportadora ou teve seu pagamento recusado."
      );
    }

    pedido.setEndereco(repositoryEndereco.findById(dto.idEndereco()));

    try {
      repositoryPedido.persist(pedido);
    } catch (Exception e) {
      throw new GeneralErrorException(
        "500",
        "Server Error",
        "PedidoServiceImpl(updateEndereco)",
        "O pedido não pôde ser persistido no banco de dados."
      );
    }
    return EnderecoResponseDTO.valueOf(pedido.getEndereco());
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

  private Boolean temEmEstoque(
    Integer num_produtos_banco,
    Integer num_produtos_pedido
  ) {
    Integer sub;
    sub = num_produtos_banco - num_produtos_pedido;
    // Existem produtos em quantidade adequada no banco
    if (sub >= 0) {
      return true;
      // Não existem produtos em quantidade adequada no banco
    } else {
      return false;
    }
  }

  private Boolean verificaProduto1(Long id) {
    if (id == null) {
      return false;
    }

    if (id < 1) {
      return false;
    }

    return true;
  }

  private Boolean verificaProduto2(Produto produto) {
    if (produto == null) {
      return false;
    } else {
      return true;
    }
  }

  private Boolean verificaUsuario1(Long id) {
    if (id == null) {
      return false;
    }

    if (id < 1) {
      return false;
    }

    return true;
  }

  private Boolean verificaUsuario2(Cliente cliente) {
    if (cliente == null) {
      return false;
    } else {
      return true;
    }
  }

  private Boolean verificaEndereco1(Long id) {
    if (id == null) {
      return false;
    }

    if (id < 1) {
      return false;
    }

    return true;
  }

  private Boolean verificaEndereco2(Endereco endereco) {
    if (endereco == null) {
      return false;
    } else {
      return true;
    }
  }

  private Boolean verificaEndereco3(Cliente cliente, Long idEndereco) {
    for (Endereco end : cliente.getListaEndereco()) {
      if (end.getId() == idEndereco) {
        return true;
      }
    }
    return false;
  }

  private Boolean verificaPedido1(Long id) {
    if (id == null) {
      return false;
    }

    if (id < 1) {
      return false;
    }

    return true;
  }

  private Boolean verificaPedido2(Pedido pedido) {
    if (pedido == null) {
      return false;
    } else {
      return true;
    }
  }

  private Boolean verificaPedido3(Cliente cliente, Pedido pedido) {
    for (Pedido pedidoteste : cliente.getListaPedido()) {
      // O pedido repassado ao método pertence ao Cliente repassado ao método.
      if (pedidoteste.getId() == pedido.getId()) {
        return true;
      }
    }
    return false;
  }

  private Boolean podeTrocarEndereco(Pedido pedido) {
    // Coletando o Status de número mais elevado neste pedido
    Integer status = 0;
    for (StatusDoPedido statusPedido : pedido.getStatusDoPedido()) {
      if (status < statusPedido.getStatus().getId()) {
        status = statusPedido.getStatus().getId();
      }
    }
    // Pedidos com status do tipo: AGUARDANDO_PAGAMENTO (0), PAGO (2), SEPARADO_DO_ESTOQUE (3), podem ter seus endereços mudados. Pedidos com status do tipo: ENTREGUE_A_TRANSPORTADORA (4) e ENTREGUE (5) não podem ter seus endereços mudados.
    if (status == 0) {
      return true;
    } else if (status == 2) {
      return true;
    } else if (status == 3) {
      return true;
    } else if (status == 4) {
      return false;
    } else if (status == 5) {
      return false;
    }
    // Pedido com status do tipo: PAGAMENTO_NÃO_AUTORIZADO (1) não podem ter seu endereço alterado. Pedidos com outros status, não existentes no ENUM do sistema, não podem ter seus endereços alterados.
    return false;
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
          if (pedido.getFormaDePagamento().getModalidade().getId() != 0) {
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
