package br.unitins.topicos1.service;

import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.*;
import br.unitins.topicos1.model.*;
import br.unitins.topicos1.repository.FornecedorRepository;
import br.unitins.topicos1.repository.LoteRepository;
import br.unitins.topicos1.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class LoteServiceImpl implements LoteService {

  @Inject
  LoteRepository repository;

  @Inject
  FornecedorRepository repositoryFornecedor;

  @Inject
  ProdutoRepository repositoryProduto;

  @Inject
  ProdutoServiceImpl serviceProduto;

  @Override
  @Transactional
  // Chegaram mais unidades de um mesmo produto, de um fornecedor diferente e/ou por um custo de compra diferente e/ou por um valor de venda diferente. Neste caso preciso criar um novo Lote. Este novo Lote ficará inativo até que o estoque do produto acabe. Caso o estoque já tenha acabado, este Lote será automaticamente ativado.
  public LoteResponseDTO insert(LoteDTO dto) {
    Lote lote = new Lote();
    lote.setLote(dto.lote());
    // Formato esperado da data e hora na String
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm:ss"
        );
    lote.setDataHoraChegadaLote(LocalDateTime.parse(dto.dataHoraChegadaLote(), formatter));
    lote.setQuantidadeUnidades(dto.quantidadeUnidades());
    lote.setQuantidadeNaoConvencional(dto.quantidadeNaoConvencional());
    lote.setUnidadeDeMedida(dto.unidadeDeMedida());
    lote.setValorVenda(dto.valorVenda());
    lote.setCustoCompra(dto.custoCompra());
    lote.setGarantiaMeses(dto.garantiaMeses());
    lote.setFornecedor(repositoryFornecedor.findById(dto.fornecedor().getId()));
    lote.setProduto(repositoryProduto.findById(dto.produto().getId()));
    lote.setStatusDoLote(StatusDoLote.valueOf(2)); // Todo Lote recém cadastrado recebe status de EM ESPERA
  
    try {
       
      repository.persist(lote);
    } catch (Exception e) {
      throw new GeneralErrorException(
        "500",
        "Server Error",
        "LoteServiceImpl(insert)",
        "Não consegui persistir o lote no banco de dados."
      );
    }
    // O estoque do produto já acabou. O Lote recém inserido será automaticamente ativado.
    if (
      (repositoryProduto.findById(dto.produto().getId()).getQuantidadeUnidades() ==
      null || repositoryProduto.findById(dto.produto().getId()).getQuantidadeUnidades() == 0) ||
      (repositoryProduto
        .findById(dto.produto().getId())
        .getQuantidadeNaoConvencional() ==
      null || repositoryProduto
      .findById(dto.produto().getId())
      .getQuantidadeNaoConvencional() ==
    0)
    ) {
      LocalDateTime agora = LocalDateTime.now();
      String dataFormatada = agora.format(formatter);
      LocalDateTime novoDateTime = LocalDateTime.parse(
        dataFormatada,
        formatter
      );
      // Ativa o Lote
      lote.setDataHoraAtivacaoLote(novoDateTime);
      lote.setStatusDoLote(StatusDoLote.valueOf(1));
      ProdutoPatchDTO prodpatch = new ProdutoPatchDTO(
        dto.produto().getId(),
        dto.valorVenda(),
        dto.quantidadeUnidades(),
        dto.quantidadeNaoConvencional(),
        dto.unidadeDeMedida(),
        lote.getId()
      );
      // O método abaixo altera os valores de venda, quantidade e id do Lote atual no produto em questão. O Lote torna-se ativado e o produto agora aponta para ele.
      serviceProduto.update(prodpatch);
    }
    /* try {
            repositoryProduto.findById(dto.idProduto()).setLoteAtual(lote);
            repositoryProduto.findById(dto.idProduto()).setQuantidade(repositoryProduto.findById(dto.idProduto()).getQuantidade()+dto.quantidade());
         } catch (OptimisticLockException e) {
            throw new GeneralErrorException(
        "500",
        "Server Error",
        "LoteServiceImpl(insert)",
        "Não consegui realizar o insert de Lote por conta de concorrência no banco de dados. Tente novamente." + e);
        } */

    return LoteResponseDTO.valueOf(lote);
  }

  @Override
  @Transactional
  // Chegaram mais unidades de um mesmo produto, de um fornecedor diferente e/ou por um custo de compra diferente e/ou por um valor de venda diferente. Neste caso preciso criar um novo Lote. Este novo Lote ficará inativo até que o estoque do produto acabe. Caso o estoque já tenha acabado, este Lote será automaticamente ativado.
  public LoteResponseCDTO insertTeste(LoteDTO dto) {
    Lote lote = new Lote();
    lote.setLote(dto.lote());
    // Formato esperado da data e hora na String
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm:ss"
        );
    lote.setDataHoraChegadaLote(LocalDateTime.parse(dto.dataHoraChegadaLote(), formatter));
    lote.setQuantidadeUnidades(dto.quantidadeUnidades());
    lote.setQuantidadeNaoConvencional(dto.quantidadeNaoConvencional());
    lote.setUnidadeDeMedida(dto.unidadeDeMedida());
    lote.setValorVenda(dto.valorVenda());
    lote.setCustoCompra(dto.custoCompra());
    lote.setGarantiaMeses(dto.garantiaMeses());
    lote.setFornecedor(repositoryFornecedor.findById(dto.fornecedor().getId()));
    lote.setProduto(repositoryProduto.findById(dto.produto().getId()));
    lote.setStatusDoLote(StatusDoLote.valueOf(2)); // Todo Lote recém cadastrado recebe status de EM ESPERA
  
    try {
       
      repository.persist(lote);
    } catch (Exception e) {
      throw new GeneralErrorException(
        "500",
        "Server Error",
        "LoteServiceImpl(insert)",
        "Não consegui persistir o lote no banco de dados."
      );
    }
    // O estoque do produto já acabou. O Lote recém inserido será automaticamente ativado.
    if (
      (repositoryProduto.findById(dto.produto().getId()).getQuantidadeUnidades() ==
      null || repositoryProduto.findById(dto.produto().getId()).getQuantidadeUnidades() == 0) ||
      (repositoryProduto
        .findById(dto.produto().getId())
        .getQuantidadeNaoConvencional() ==
      null || repositoryProduto
      .findById(dto.produto().getId())
      .getQuantidadeNaoConvencional() ==
    0)
    ) {
      LocalDateTime agora = LocalDateTime.now();
      String dataFormatada = agora.format(formatter);
      LocalDateTime novoDateTime = LocalDateTime.parse(
        dataFormatada,
        formatter
      );
      // Ativa o Lote
      lote.setDataHoraAtivacaoLote(novoDateTime);
      lote.setStatusDoLote(StatusDoLote.valueOf(1));
      ProdutoPatchDTO prodpatch = new ProdutoPatchDTO(
        dto.produto().getId(),
        dto.valorVenda(),
        dto.quantidadeUnidades(),
        dto.quantidadeNaoConvencional(),
        dto.unidadeDeMedida(),
        lote.getId()
      );
      // O método abaixo altera os valores de venda, quantidade e id do Lote atual no produto em questão. O Lote torna-se ativado e o produto agora aponta para ele.
      serviceProduto.update(prodpatch);
    }
    /* try {
            repositoryProduto.findById(dto.idProduto()).setLoteAtual(lote);
            repositoryProduto.findById(dto.idProduto()).setQuantidade(repositoryProduto.findById(dto.idProduto()).getQuantidade()+dto.quantidade());
         } catch (OptimisticLockException e) {
            throw new GeneralErrorException(
        "500",
        "Server Error",
        "LoteServiceImpl(insert)",
        "Não consegui realizar o insert de Lote por conta de concorrência no banco de dados. Tente novamente." + e);
        } */

    return LoteResponseCDTO.valueOf(lote);
  }


  @Override
  @Transactional
  // Chegaram mais unidades do mesmo produto, do mesmo fornecedor, pelo mesmo custo de compra e irei manter o mesmo valor de venda. Neste caso só altero a quantidade e mais nada.
  public LoteResponseDTO updateQuantidade(LotePatchQDTO dto) {
    Lote lote = repository.findById(dto.id());

    if (dto.quantidadeUnidades() != null) {
      if (lote != null) {
        try {
          lote
            .getProduto()
            .setQuantidadeUnidades(
              lote.getProduto().getQuantidadeUnidades() +
              dto.quantidadeUnidades()
            );
          lote.setQuantidadeUnidades(
            lote.getQuantidadeUnidades() + dto.quantidadeUnidades()
          );
        } catch (OptimisticLockException e) {
          throw new GeneralErrorException(
            "500",
            "Server Error",
            "LoteServiceImpl(updateQuantidade)",
            "Não consegui realizar o insert de Lote por conta de concorrência no banco de dados. Tente novamente." +
            e
          );
        }
        return LoteResponseDTO.valueOf(lote);
      } else {
        throw new GeneralErrorException(
          "400",
          "Bad Request",
          "LoteServiceImpl(updateQuantidade)",
          "O id passado como índice de lote não existe no banco de dados."
        );
      }
    } else {
      if (lote != null) {
        try {
          lote
            .getProduto()
            .setQuantidadeNaoConvencional(
              lote.getProduto().getQuantidadeNaoConvencional() +
              dto.quantidadeNaoConvencional()
            );
          lote.setQuantidadeNaoConvencional(
            lote.getQuantidadeNaoConvencional() +
            dto.quantidadeNaoConvencional()
          );
        } catch (OptimisticLockException e) {
          throw new GeneralErrorException(
            "500",
            "Server Error",
            "LoteServiceImpl(updateQuantidade)",
            "Não consegui realizar o insert de Lote por conta de concorrência no banco de dados. Tente novamente." +
            e
          );
        }
        return LoteResponseDTO.valueOf(lote);
      } else {
        throw new GeneralErrorException(
          "400",
          "Bad Request",
          "LoteServiceImpl(updateQuantidade)",
          "O id passado como índice de lote não existe no banco de dados."
        );
      }
    }
  }

  @Override
  @Transactional
  // Quero simplesmente alterar o valor de venda de um produto. Neste caso sou obrigado a criar um novo Lote (novo id, nova String para designar o novo Lote e novo valor de venda do produto),absorvendo os demais dados do Lote atual e fechar o Lote atual.
  public LoteResponseDTO updateValorVenda(LotePatchVDTO dto) {
    Lote lote = repository.findById(dto.id());
    Lote loteN = new Lote();
    if (lote != null) {
      try {
        loteN.setLote(dto.lote());
        loteN.setQuantidadeUnidades(lote.getQuantidadeUnidades());
        loteN.setQuantidadeNaoConvencional(lote.getQuantidadeNaoConvencional());
        loteN.setUnidadeDeMedida(lote.getUnidadeDeMedida());
        loteN.setValorVenda(dto.valorVenda());
        loteN.setCustoCompra(lote.getCustoCompra());
        loteN.setFornecedor(lote.getFornecedor());
        loteN.setProduto(lote.getProduto());

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm:ss"
        );
        String dataFormatada = agora.format(formatter);
        LocalDateTime novoDateTime = LocalDateTime.parse(
          dataFormatada,
          formatter
        );
        // Ativa o novo Lote que absorveu os dados do Lote anterior que será desativado.
        loteN.setDataHoraChegadaLote(novoDateTime);
        // Desativa o Lote anterior
        lote.setDataHoraUltimoVendido(novoDateTime);

        // Atualiza o valor de venda no Produto
        loteN.getProduto().setValorVenda(dto.valorVenda());
      } catch (OptimisticLockException e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "LoteServiceImpl(updateValorVenda)",
          "Não consegui realizar o insert de Lote por algum motivo. Tente novamente." +
          e
        );
      }
      return LoteResponseDTO.valueOf(loteN);
    } else {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "LoteServiceImpl(updateValorVenda)",
        "O id passado como índice de lote não existe no banco de dados."
      );
    }
  }

  public List<LoteResponseDTO> findByIdProduto(
    Long idProduto,
    int page,
    int pageSize
  ) {
    List<Lote> temp = repository.listAll();
    List<Lote> temp2 = new ArrayList<Lote>();
    List<LoteResponseDTO> loteResponse = new ArrayList<LoteResponseDTO>();
    Integer count = 0;
    for (Lote lote : temp) {
      if (lote.getProduto().getId().equals(idProduto)) {
        temp2.add(lote);
        count++;
      }
    }
    // Calcula o índice inicial e final para a página atual
    int startIndex = page * pageSize;
    int endIndex = Math.min(startIndex + pageSize, temp2.size());
    // Obtém a lista paginada usando subList
    List<Lote> listaPaginada = temp2.subList(startIndex, endIndex);
  
    // Transforma a lista paginada em lista de DTOs
    for (Lote lote : listaPaginada) {
      LoteResponseDTO dto = LoteResponseDTO.valueOf(lote);
      loteResponse.add(dto);
    }
    return loteResponse;
  }

  public List<LoteResponseCDTO> findByIdProdutoTeste(
    Long idProduto,
    int page,
    int pageSize
  ) {
    List<Lote> temp = repository.listAll();
    List<Lote> temp2 = new ArrayList<Lote>();
    List<LoteResponseCDTO> loteResponse = new ArrayList<LoteResponseCDTO>();
    Integer count = 0;
    for (Lote lote : temp) {
      if (lote.getProduto().getId().equals(idProduto)) {
        temp2.add(lote);
        count++;
      }
    }
    // Calcula o índice inicial e final para a página atual
    int startIndex = page * pageSize;
    int endIndex = Math.min(startIndex + pageSize, temp2.size());
    // Obtém a lista paginada usando subList
    List<Lote> listaPaginada = temp2.subList(startIndex, endIndex);
  
    // Transforma a lista paginada em lista de DTOs
    for (Lote lote : listaPaginada) {
      LoteResponseCDTO dto = LoteResponseCDTO.valueOf(lote);
      loteResponse.add(dto);
    }
    return loteResponse;
  }


  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public LoteResponseDTO findById(Long id) {
    return LoteResponseDTO.valueOf(repository.findById(id));
  }

  @Override
  public LoteResponseCDTO findByIdTeste(Long id) {
    return LoteResponseCDTO.valueOf(repository.findById(id));
  }

  @Override
  public List<LoteResponseDTO> findByName(String lote) {
    return repository
      .findByName(lote)
      .stream()
      .map(LoteResponseDTO::valueOf)
      .toList();
  }

  @Override
  public List<LoteResponseDTO> findByAll() {
    return repository.listAll().stream().map(LoteResponseDTO::valueOf).toList();
  }

  @Override
  @Transactional
  public void delete(Long id) {
    Lote lote = repository.findById(id);
    if (lote.getDataHoraUltimoVendido() == null) {
      throw new GeneralErrorException(
        "400",
        "Bad Request",
        "LoteServiceImpl(delete)",
        "Lote ativo. Impossível deletar."
      );
    } else {
      try {
        repository.deleteById(id);
      } catch (Exception e) {
        throw new GeneralErrorException(
          "500",
          "Server Error",
          "LoteServiceImpl(delete)",
          "Não consegui deletar o produto em questão. Tente novamente mais tarde." +
          e
        );
      }
    }
  }

  @Transactional
  public LoteResponseDTO ativaLote(Long idLote) {
        System.out.println(idLote);
        Lote lote = repository.findById(idLote);
        System.out.println(idLote);
        System.out.println(lote);
      // O if abaixo verifica se dataHoraAtivacaoLote == null
      if (lote.getDataHoraAtivacaoLote() == null) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss"
          );
        String dataFormatada = agora.format(formatter);
        LocalDateTime novoDateTime = LocalDateTime.parse(
            dataFormatada,
            formatter
        );
        // Ativa o Lote
        lote.setDataHoraAtivacaoLote(novoDateTime);
        ProdutoPatchDTO prodpatch = new ProdutoPatchDTO(
          lote.getProduto().getId(),
          lote.getValorVenda(),
          lote.getQuantidadeUnidades(),
          lote.getQuantidadeNaoConvencional(),
          lote.getUnidadeDeMedida(),
          lote.getId()
        );
        // O método abaixo altera os valores de venda, quantidade e id do Lote atual no produto em questão. O Lote torna-se ativado e o produto agora aponta para ele.
        System.out.println("111111111111111111111111111111111111111111111111111111111111");
        serviceProduto.update(prodpatch);
        System.out.println("2222222222222222222222222222222222222222222222222222222222222");
        lote.setStatusDoLote(StatusDoLote.valueOf(1));
        System.out.println("333333333333333333333333333333333333333333333333333333333333333");
        repository.persist(lote);
        return LoteResponseDTO.valueOf(lote);
      }

    throw new GeneralErrorException(
      "400",
      "Bad Request",
      "LoteServiceImpl(ativaLote)",
      "Não existe Lote pré cadastrado para este produto e por isso não pode ser ativado. Utilize o método LoteServiceImpl(insert)."
    );
  }
}
