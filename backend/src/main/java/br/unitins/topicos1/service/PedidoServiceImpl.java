package br.unitins.topicos1.service;

import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.cliente.DesejoResponseDTO;
import br.unitins.topicos1.dto.endereco.EnderecoDTO;
import br.unitins.topicos1.dto.endereco.EnderecoResponseDTO;
import br.unitins.topicos1.dto.pedido.PedidoDTO;
import br.unitins.topicos1.dto.pedido.PedidoPatchEnderecoDTO;
import br.unitins.topicos1.dto.pedido.PedidoPatchStatusDTO;
import br.unitins.topicos1.dto.pedido.PedidoResponseDTO;
import br.unitins.topicos1.dto.pedido.itemVenda.ItemDaVendaDTO;
import br.unitins.topicos1.model.pagamento.Boleto;
import br.unitins.topicos1.model.pagamento.CartaoDeCredito;
import br.unitins.topicos1.model.pagamento.ModalidadePagamento;
import br.unitins.topicos1.model.pagamento.Pix;
import br.unitins.topicos1.model.pedido.ItemDaVenda;
// import br.unitins.topicos1.model.pedido.ObjetoRetorno;
import br.unitins.topicos1.model.pedido.Pedido;
import br.unitins.topicos1.model.pedido.Status;
import br.unitins.topicos1.model.pedido.StatusDoPedido;
import br.unitins.topicos1.model.produto.Produto;
import br.unitins.topicos1.model.utils.Cliente;
import br.unitins.topicos1.model.utils.Empresa;
import br.unitins.topicos1.model.utils.Endereco;
import br.unitins.topicos1.repository.ClienteRepository;
import br.unitins.topicos1.repository.EmpresaRepository;
import br.unitins.topicos1.repository.EnderecoRepository;
import br.unitins.topicos1.repository.LoteRepository;
import br.unitins.topicos1.repository.PedidoRepository;
import br.unitins.topicos1.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// import org.eclipse.microprofile.reactive.messaging.Channel;
// import org.eclipse.microprofile.reactive.messaging.Emitter;
// import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

  // @Channel("card-out")
  // Emitter<String> cardRequestEmitter;
  
  @Inject
  ClienteRepository repository;

  @Inject
  PedidoRepository repositoryPedido;

  @Inject
  ProdutoRepository repositoryProduto;

  @Inject
  EnderecoRepository repositoryEndereco;

  @Inject
  EmpresaRepository repositoryEmpresa;

  @Inject
  LoteRepository repositoryLote;

  @PersistenceUnit
  EntityManagerFactory emf;

  @Override
  @Transactional
  public PedidoResponseDTO insert(PedidoDTO dto) {
   
    // Carrega os dados da empresa.
    Empresa empresa = repositoryEmpresa.findById(Long.valueOf(1));

    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaCliente1(dto.idCliente())) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(insert)",
        "id do usuário é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(dto.idCliente());
    if (!verificaCliente2(cliente)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(insert)",
        "id do usuário não existe no banco de dados."
      );
    }

    Pedido pedido = new Pedido();
    //pedido.setCodigoDeRastreamento(dto.codigoDeRastreamento());

    pedido.setCliente(cliente);

    pedido.setItemDaVenda(new ArrayList<ItemDaVenda>());
    Double valorCompra = 0.0;
    for (ItemDaVendaDTO idv : dto.itemDaVenda()) {
      ItemDaVenda item = new ItemDaVenda();
      item.setPreco(idv.preco());
     // System.out.println(item.getPreco());
      item.setQuantidadeUnidades(idv.quantidadeUnidades());
      //System.out.println(item.getQuantidadeUnidades());

      // Verifica o id do produto. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
      System.out.println("Verificação ID PRODUTO: "+idv.idProduto());
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
      System.out.println("Id do produto buscado no BD: "+produto.getId());
      Integer quant;
      try{
        quant = produto.getQuantidadeUnidades() - idv.quantidadeUnidades();
        System.out.println("Quantidade: "+quant);
        if(quant > 0) {
          produto.setQuantidadeUnidades(quant);
          item.setProduto(produto);
         //System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT ");
          pedido.getItemDaVenda().add(item);
          valorCompra = valorCompra + idv.quantidadeUnidades() * idv.preco();
        } else if(quant == 0) {
          produto.setQuantidadeUnidades(0);
          LocalDateTime agora = LocalDateTime.now();
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
          String dataFormatada = agora.format(formatter);
          LocalDateTime novoDateTime = LocalDateTime.parse(dataFormatada, formatter);
          produto.getLoteAtual().setDataHoraUltimoVendido(novoDateTime);
          item.setProduto(produto);
          pedido.getItemDaVenda().add(item);
          valorCompra = valorCompra + idv.quantidadeUnidades() * idv.preco();
        } else if(quant < 0) {
          throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(insert)",
          "Inexiste esta quantidade de produto em estoque."
        );
        }
      } catch (OptimisticLockException e) {
        throw new GeneralErrorException(
    "500",
    "Server Error",
    "PedidoServiceImpl(insert)",
    "Não consegui realizar o insert do pedido por conta de concorrência no banco de dados. Tente novamente mais tarde." + e);
    } 

      /* 
      // Analisa se tem o produto em estoque, subtrai a quantidade desejada e persiste no banco
      EntityManager em = emf.createEntityManager();
      EntityTransaction transaction = em.getTransaction();

      try {
        transaction.begin();
        String sql1 = "SELECT quantidade FROM produto WHERE id = ?1 FOR SHARE ";
        Query query = em
          .createNativeQuery(sql1)
          .setParameter(1, idv.idProduto());
        Integer quantidade = (Integer) query.getSingleResult();
        Integer quantFinal = quantidade - idv.quantidade();
        if (quantFinal >= 0) {
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
          if(quantFinal == 0) {
            produto.getLoteAtual().setDataHoraUltimoVendido(LocalDateTime.now());
          }
        } else {
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
      } */
    }

    // Verifica se o id fornecido para o endereço de entrega do pedido é nulo
    if (!verificaEndereco1(dto.idEndereco())) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(insert)",
        "id do endereço é nulo ou tem valor inferior a 1."
      );
    }
    //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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
//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    // Retorna true se o dto.idEndereco() é de um endereço que pertença ao cliente.
   // if (verificaEnderecoCliente(cliente, endereco)) {
    pedido.setEndereco(endereco);
  //System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEee");
    // } else {
    //   throw new GeneralErrorException(
    //     "400",
    //     "Bad Request",
    //     "PedidoServiceImpl(insert)",
    //     "O id passado como índice de endereço não pertence ao cliente!"
    //   );
    // }

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
      LocalDateTime agora = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
      String dataFormatada = agora.format(formatter);
      LocalDateTime novoDateTime = LocalDateTime.parse(dataFormatada, formatter);
      pagamento.setDataHoraPagamento(novoDateTime);
      pedido.setFormaDePagamento(pagamento);
    } else if (dto.formaDePagamento().modalidade() == 1) {
      Boleto pagamento = new Boleto();
      pagamento.setValorPago(valorCompra);
      pagamento.setModalidade(
        ModalidadePagamento.valueOf(dto.formaDePagamento().modalidade())
      );
      pagamento.setNomeBanco(empresa.getNomeBanco());
      pagamento.setDataHoraGeracao(LocalDateTime.now());
      LocalDateTime pegDateTime = LocalDateTime.now();
      LocalDateTime limite = pegDateTime.plusDays(
        dto.formaDePagamento().diasVencimento()
      );
      limite = limite.with(LocalTime.of(23, 59, 59));
      pagamento.setDataHoraLimitePag(limite);
      try {
        String nomeArquivo;
        nomeArquivo = GerarBoleto.geraBoletoFinal(
        dto.formaDePagamento().diasVencimento(),
        valorCompra,
        cliente,
        empresa,
        endereco
      );
      pagamento.setNomeArquivo(nomeArquivo);
      } catch (Exception e) {
        throw new GeneralErrorException(
        "400",
        "Bad Request",
        "PedidoServiceImpl(insert)",
        "Ocorreu algum erro na geração do boleto."
      );
      }
      
      pedido.setFormaDePagamento(pagamento);
    } else if (dto.formaDePagamento().modalidade() == 2) {
      Pix pagamento = new Pix();
      pagamento.setModalidade(
        ModalidadePagamento.valueOf(dto.formaDePagamento().modalidade())
      );
      pagamento.setValorPago(valorCompra);
      pagamento.setNomeCliente(cliente.getNome());
      pagamento.setNomeRecebedor(empresa.getNomeFantasia());
      pagamento.setChaveRecebedor(empresa.getChavePixAleatoria());
      pagamento.setDataHoraGeracao(LocalDateTime.now());
      pagamento.setNomeCidade(
        empresa.getListaEndereco().get(0).getLocalidade()
      );
      try {
        String nomeArquivo;
        nomeArquivo = GerarPix.QrCodePix(pagamento);
        pagamento.setNomeArquivo(nomeArquivo);
      } catch (Exception e) {
        throw new GeneralErrorException(
        "400",
        "Bad Request",
        "PedidoServiceImpl(insert)",
        "Ocorreu algum erro na geração da chave pix ou do QRCode."
      );
      }
      
      pedido.setFormaDePagamento(pagamento);
    } else {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "PedidoServiceImpl(insert)",
        "O id passado como índice de forma de pagamento não existe! Os ids válidos são 0,1 ou 2."
      );
    }

    pedido.setStatusDoPedido(new ArrayList<StatusDoPedido>());
    StatusDoPedido status = new StatusDoPedido();
    LocalDateTime agora = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String dataFormatada = agora.format(formatter);
    LocalDateTime novoDateTime = LocalDateTime.parse(dataFormatada, formatter);
    status.setDataHora(novoDateTime);
    status.setStatus(Status.valueOf(0));
    status.setPedido(pedido);
    pedido.getStatusDoPedido().add(status);

    try {
       repositoryPedido.persist(pedido);
     } catch (Exception e) {
        throw new GeneralErrorException(
    "500",
    "Server Error",
    "PedidoServiceImpl(insert)",
    "Não consegui realizar o insert do pedido no banco de dados. Tente novamente mais tarde." + e);
    } 
   
    //if(pedido.getFormaDePagamento().getModalidade().getId() == 0)
  //   if(pedido.getFormaDePagamento() instanceof CartaoDeCredito)
  //   enviaDadosCartao((CartaoDeCredito) pedido.getFormaDePagamento(), cliente.getNome(), cliente.getCpf(), pedido.getId());

     return PedidoResponseDTO.valueOf(pedido);
   }

  @Override
  @Transactional
  public void deletePedidoByCliente(Long idCliente, Long idPedido) {
    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaCliente1(idCliente)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "id do usuário é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(idCliente);
    if (!verificaCliente2(cliente)) {
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

    if (!podeDeletar(cliente, pedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "O pedido não pode ser desfeito!"
      );
    }

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
  public void deletePedidoByFunc(Long idPedido) {
    
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

    if (!podeDeletar(pedido)) {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "PedidoServiceImpl(deletePedidoByCliente)",
        "O pedido não pode ser desfeito!"
      );
    }

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
    LocalDateTime agora = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String dataFormatada = agora.format(formatter);
    LocalDateTime novoDateTime = LocalDateTime.parse(dataFormatada, formatter);
    statusVenda.setDataHora(novoDateTime);
    statusVenda.setPedido(pedido);

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
      try {
      statusVenda.setStatus(Status.valueOf(ppsdto.idStatus()));
      pedido.getStatusDoPedido().add(statusVenda);

      // Devolve o produto separado ao estoque
      for (ItemDaVenda idv : pedido.getItemDaVenda()) {

        idv.getProduto().setQuantidadeUnidades(idv.getProduto().getQuantidadeUnidades()+idv.getQuantidadeUnidades());
        idv.getProduto().getLoteAtual().setDataHoraUltimoVendido(null);
        /*
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
          transaction.begin();
          String sql1 =
            "SELECT quantidade FROM produto WHERE id = ?1 FOR SHARE";
          Query query = em
            .createNativeQuery(sql1)
            .setParameter(1, idv.getProduto().getId());
          Integer quantidade = (Integer) query.getSingleResult();
          Integer quantFinal = quantidade + idv.getQuantidade();

          String sql4 = "UPDATE produto SET quantidade = ?1 WHERE id = ?2";
          em
            .createNativeQuery(sql4)
            .setParameter(1, quantFinal)
            .setParameter(2, idv.getProduto().getId())
            .executeUpdate();
          transaction.commit();
          idv.getProduto().getLoteAtual().setDataHoraUltimoVendido(null);
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
        } */
      }
      
       } catch (OptimisticLockException e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "PedidoServiceImpl(updateStatusDoPedido)",
          "Não consegui persistir o pedido no banco de dados!" + e
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
      System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKk");
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
    return repositoryPedido
      .findAll(idcliente)
      .stream()
      .map(p -> PedidoResponseDTO.valueOf(p))
      .toList();
  }

  @Override
  public List<PedidoResponseDTO> findPedidoByClienteId(Long idcliente) {
    return repositoryPedido
      .findAll(idcliente)
      .stream()
      .map(p -> PedidoResponseDTO.valueOf(p))
      .toList();
  }

  @Override
  public List<PedidoResponseDTO> findAllFuncStatus(Long idStatus) {
    return repositoryPedido
      .findAllStatus(idStatus)
      .stream()
      .map(p -> PedidoResponseDTO.valueOf(p))
      .toList();
  }

  @Override
  @Transactional
  public EnderecoResponseDTO insertEndereco(EnderecoDTO dto) {
      try {
        Endereco listaEndereco = new Endereco();
        listaEndereco.setNome(dto.nome());
        listaEndereco.setLogradouro(dto.logradouro());
        listaEndereco.setNumeroLote(dto.numeroLote());
        listaEndereco.setBairro(dto.bairro());
        listaEndereco.setComplemento(dto.complemento());
        listaEndereco.setCep(dto.cep());
        listaEndereco.setLocalidade(dto.localidade());
        listaEndereco.setUF(dto.uf());
        listaEndereco.setPais(dto.pais());
        repositoryEndereco.persist(listaEndereco);
        return EnderecoResponseDTO.valueOf(listaEndereco);
      } catch (Exception e) {
        throw new GeneralErrorException("500", "Internal Server Error", "PedidoServiceImpl(insertEndereco)", "Não consegui alocar memória para o novo Endereço. Tente novamente mais tarde! " +  e.getCause());
      }
      
    }



  @Override
  @Transactional
  public EnderecoResponseDTO updateEndereco(
    PedidoPatchEnderecoDTO dto,
    Long id
  ) {
    // Verifica o id do cliente. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
    if (!verificaCliente1(id)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(updateEndereco)",
        "id do usuário é nulo ou tem valor inferior a 1."
      );
    }

    // Verifica o cliente. Caso o id inexista no banco de dados, o sistema não realiza a operação.
    Cliente cliente = repository.findById(id);
    if (!verificaCliente2(cliente)) {
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
    // if (!verificaEndereco3(cliente, dto.idEndereco())) {
    //   throw new GeneralErrorException(
    //     "400",
    //     "Bad Resquest",
    //     "PedidoServiceImpl(updateEndereco)",
    //     "O novo endereço não pertence a este cliente."
    //   );
    // }

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

  @Override
  @Transactional
  public DesejoResponseDTO insertDesejos(Long idProduto, Long idCliente) {
    Cliente cliente = repository.findById(idCliente);
    if (!verificaCliente2(cliente)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(insertDesejos)",
        "id do usuário não existe no banco de dados."
      );
    }
    // Verifica o id do prduto. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
      if (!verificaProduto1(idProduto)) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(insertDesejos)",
          "id do produto é nulo ou tem valor inferior a 1."
        );
      }

      // Verifica o produto. Caso o id inexista no banco de dados, o sistema não realiza a operação.
      Produto produto = repositoryProduto.findById(idProduto);
      if (!verificaProduto2(produto)) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(insertDesejos)",
          "id do produto não existe no banco de dados."
        );
      }
      if(cliente.getListaProduto() == null) {
        cliente.setListaProduto(new ArrayList<Produto>());
        cliente.getListaProduto().add(produto);
      } else {
        for(Produto prod : cliente.getListaProduto()) {
          if(prod.getId() == produto.getId()) {
            throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(insertDesejos)",
          "Produto já existe na lista de desejos deste cliente."
        );
          }
        }
        cliente.getListaProduto().add(produto);
      }

        
    
    /* try {
      repository.persist(cliente);
    } catch (Exception e) {
      throw new GeneralErrorException(
        "500",
        "Server Error",
        "PedidoServiceImpl(insertDesejos)",
        "O produto não pôde ser inserido na lista de desejos do Cliente no banco de dados."
      );
    } */
    return DesejoResponseDTO.valueOf(produto);
  }

  
  @Override
  @Transactional
  public void deleteDesejos(Long idProduto, Long idCliente) {
 
    Cliente cliente = repository.findById(idCliente);
    if (!verificaCliente2(cliente)) {
      throw new GeneralErrorException(
        "400",
        "Bad Resquest",
        "PedidoServiceImpl(deleteDesejos)",
        "id do usuário não existe no banco de dados."
      );
    }
    // Verifica o id do prduto. Caso o id seja nulo ou negativo, o sistema não realiza a operação.
      if (!verificaProduto1(idProduto)) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(deleteDesejos)",
          "id do produto é nulo ou tem valor inferior a 1."
        );
      }

      // Verifica o produto. Caso o id inexista no banco de dados, o sistema não realiza a operação.
      Produto produto = repositoryProduto.findById(idProduto);
      if (!verificaProduto2(produto)) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(deleteDesejos)",
          "id do produto não existe no banco de dados."
        );
      }

      Boolean chave = true;
      for(Produto prod : cliente.getListaProduto()) {
        System.out.println(prod.getId() + " " + idProduto);
        if(Long.valueOf(prod.getId()).equals(idProduto)) {
          chave = false;
          break;
        }
      }
      System.out.println(chave);

      if(chave) {
        throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "PedidoServiceImpl(deleteDesejos)",
          "Produto não existe na lista de desejos do cliente."
        );
      }

      
      cliente.getListaProduto().remove(produto);
      
  }

  // private Boolean verificaEnderecoCliente(Cliente cliente, Endereco endereco) {
  //   for (Endereco end : cliente.getListaEndereco()) {
  //     // O pedido repassado ao método pertence ao Cliente repassado ao método.
  //     if (end.getId() == endereco.getId()) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }

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

  private Boolean verificaCliente1(Long id) {
    if (id == null) {
      return false;
    }

    if (id < 1) {
      return false;
    }

    return true;
  }

  private Boolean verificaCliente2(Cliente cliente) {
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

  // private Boolean verificaEndereco3(Cliente cliente, Long idEndereco) {
  //   for (Endereco end : cliente.getListaEndereco()) {
  //     if (end.getId() == idEndereco) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }

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
    for (Pedido pedidoteste : repositoryPedido.findAll(cliente.getId())) {
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
    for (Pedido pedidoteste : repositoryPedido.findAll(cliente.getId())) {
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

            // Devolve o produto separado ao estoque
            for (ItemDaVenda idv : pedido.getItemDaVenda()) {
              EntityManager em1 = emf.createEntityManager();
              EntityTransaction transaction = em1.getTransaction();

              try {
                transaction.begin();
                String sql1 =
                  "SELECT quantidade FROM produto WHERE id = ?1 FOR SHARE";
                Query query = em1
                  .createNativeQuery(sql1)
                  .setParameter(1, idv.getProduto().getId());
                Integer quantidade = (Integer) query.getSingleResult();
                Integer quantFinal = quantidade + idv.getQuantidadeUnidades();

                String sql4 =
                  "UPDATE produto SET quantidade = ?1 WHERE id = ?2";
                em1
                  .createNativeQuery(sql4)
                  .setParameter(1, quantFinal)
                  .setParameter(2, idv.getProduto().getId())
                  .executeUpdate();
                transaction.commit();
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
                em1.close();
              }
            }
            return true;
          } else {
            // IMPORTANTE!!!!! Chama função que comunica a financeira o desfazimento da negociação e aguarda a confirmação da financeira para deletar o pedido.

            // Devolve o produto separado ao estoque
            for (ItemDaVenda idv : pedido.getItemDaVenda()) {
              EntityManager em2 = emf.createEntityManager();
              EntityTransaction transaction = em2.getTransaction();

              try {
                transaction.begin();
                String sql1 =
                  "SELECT quantidade FROM produto WHERE id = ?1 FOR SHARE";
                Query query = em2
                  .createNativeQuery(sql1)
                  .setParameter(1, idv.getProduto().getId());
                Integer quantidade = (Integer) query.getSingleResult();
                Integer quantFinal = quantidade + idv.getQuantidadeUnidades();

                String sql4 =
                  "UPDATE produto SET quantidade = ?1 WHERE id = ?2";
                em2
                  .createNativeQuery(sql4)
                  .setParameter(1, quantFinal)
                  .setParameter(2, idv.getProduto().getId())
                  .executeUpdate();
                transaction.commit();
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
                em2.close();
              }
            }
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

  // private void enviaDadosCartao(CartaoDeCredito pagamento, String clienteNome, String cpf, Long idPedido) {
  //   String dadosCartao;
  //   String dataHora[] = pagamento.getDataHoraPagamento().toString().split("\\.");
  //   dadosCartao = clienteNome + "+" + pagamento.getNumeroCartao() + "+" + pagamento.getAnoValidade().toString() + "+" + pagamento.getMesValidade().toString() + "+" + pagamento.getCodSeguranca().toString() + "+" + dataHora[0] + "+" + pagamento.getValorPago().toString() + "+" + cpf + "+" + idPedido.toString();

   
    // String dadoCriptografado;
    // try {
    //   dadoCriptografado = Criptografia.criptografar(dadosCartao);
    //   cardRequestEmitter.send(dadoCriptografado);
    // } catch (Exception e) {
    //  throw new GeneralErrorException(
    //                 "400",
    //                 "Bad Resquest",
    //                 "PedidoServiceImpl(enviaDadosCartao)",
    //                 "Não consegui criptografar a String. " + e.getMessage()
    //               );
    // }

    
  // }


  private Boolean podeDeletar(Pedido pedido) {
  
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

            // Devolve o produto separado ao estoque
            for (ItemDaVenda idv : pedido.getItemDaVenda()) {
              EntityManager em1 = emf.createEntityManager();
              EntityTransaction transaction = em1.getTransaction();

              try {
                transaction.begin();
                String sql1 =
                  "SELECT quantidade FROM produto WHERE id = ?1 FOR SHARE";
                Query query = em1
                  .createNativeQuery(sql1)
                  .setParameter(1, idv.getProduto().getId());
                Integer quantidade = (Integer) query.getSingleResult();
                Integer quantFinal = quantidade + idv.getQuantidadeUnidades();

                String sql4 =
                  "UPDATE produto SET quantidade = ?1 WHERE id = ?2";
                em1
                  .createNativeQuery(sql4)
                  .setParameter(1, quantFinal)
                  .setParameter(2, idv.getProduto().getId())
                  .executeUpdate();
                transaction.commit();
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
                em1.close();
              }
            }
            return true;
          } else {
            // IMPORTANTE!!!!! Chama função que comunica a financeira o desfazimento da negociação e aguarda a confirmação da financeira para deletar o pedido.

            // Devolve o produto separado ao estoque
            for (ItemDaVenda idv : pedido.getItemDaVenda()) {
              EntityManager em2 = emf.createEntityManager();
              EntityTransaction transaction = em2.getTransaction();

              try {
                transaction.begin();
                String sql1 =
                  "SELECT quantidade FROM produto WHERE id = ?1 FOR SHARE";
                Query query = em2
                  .createNativeQuery(sql1)
                  .setParameter(1, idv.getProduto().getId());
                Integer quantidade = (Integer) query.getSingleResult();
                Integer quantFinal = quantidade + idv.getQuantidadeUnidades();

                String sql4 =
                  "UPDATE produto SET quantidade = ?1 WHERE id = ?2";
                em2
                  .createNativeQuery(sql4)
                  .setParameter(1, quantFinal)
                  .setParameter(2, idv.getProduto().getId())
                  .executeUpdate();
                transaction.commit();
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
                em2.close();
              }
            }
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
    // O pedido repassado ao método não pertence ao Cliente repassado ao método.
    return false;
  }















    // @Incoming("card-in")
  //   @Transactional
  //   public void alteraStatusCompraCredito(ObjetoRetorno objR) {

  //   if(objR.getBoo()) {
  //     Long idPedido = Long.parseLong(objR.getIdPedido());
  //     PedidoPatchStatusDTO update = new PedidoPatchStatusDTO(idPedido, 2, null);
  //     updateStatusDoPedido(update);

  //   } else {
  //     Long idPedido = Long.parseLong(objR.getIdPedido());
  //     PedidoPatchStatusDTO update = new PedidoPatchStatusDTO(idPedido, 1, null);
  //     updateStatusDoPedido(update);
 
  //   }
  // }

    @Override
    public PedidoResponseDTO findPedidoById(Long id) {
      return PedidoResponseDTO.valueOf(repositoryPedido.findById(id));
    }

}
