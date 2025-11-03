package br.unitins.topicos1.service;

import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.fornecedor.FornecedorResponseDTO;
import br.unitins.topicos1.dto.produto.ProdutoDTO;
import br.unitins.topicos1.dto.produto.ProdutoFornecedorPatch;
import br.unitins.topicos1.dto.produto.ProdutoPatchDTO;
import br.unitins.topicos1.dto.produto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.produto.placaMae.PlacaMaeDTO;
import br.unitins.topicos1.dto.produto.placaMae.PlacaMaeResponseDTO;
import br.unitins.topicos1.dto.produto.processador.ProcessadorDTO;
import br.unitins.topicos1.dto.produto.processador.ProcessadorResponseDTO;
import br.unitins.topicos1.model.lote.Lote;
import br.unitins.topicos1.model.produto.Classificacao;
import br.unitins.topicos1.model.produto.PlacaMae;
import br.unitins.topicos1.model.produto.Processador;
import br.unitins.topicos1.model.produto.Produto;
import br.unitins.topicos1.repository.ClassificacaoRepository;
import br.unitins.topicos1.repository.LoteRepository;
import br.unitins.topicos1.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProdutoServiceImpl implements ProdutoService {

  @Inject
  ProdutoRepository repository;

  @Inject
  LoteRepository repositoryLote;

  @Inject
  ClassificacaoRepository repositoryClassificacao;

  @PersistenceUnit
  EntityManagerFactory emf;

  @Override
  @Transactional
  // Primeiro cadastro o produto. Em seguida cadastro o lote.
  public ProdutoResponseDTO insert(ProdutoDTO dto) {
    Produto produto = new Produto();
    produto.setNome(dto.nome());
    produto.setDescricao(dto.descricao());
    produto.setModelo(dto.modelo());
    produto.setCodigoBarras(dto.codigoBarras());
    produto.setMarca(dto.marca());
    produto.setAltura(dto.altura());
    produto.setLargura(dto.largura());
    produto.setComprimento(dto.comprimento());
    produto.setPeso(dto.peso());
    repository.persist(produto);
    return ProdutoResponseDTO.valueOf(produto);
  }

  @Transactional
    @Override
  public PlacaMaeResponseDTO insertPlacaMae(PlacaMaeDTO dto) {
    PlacaMae produto = new PlacaMae();
    produto.setNome(dto.nome());
    produto.setDescricao(dto.descricao());
    produto.setModelo(dto.modelo());
    produto.setCodigoBarras(dto.codigoBarras());
    produto.setMarca(dto.marca());
    produto.setAltura(dto.altura());
    produto.setLargura(dto.largura());
    produto.setComprimento(dto.comprimento());
    produto.setPeso(dto.peso());
    produto.setNomeImagem(dto.nomeImagem());
    produto.setClassificacao(dto.classificacao());
    // produto.setClassificacao(repositoryClassificacao.findById(dto.classificacao()));
    produto.setCpu(dto.cpu());
    produto.setChipset(dto.chipset());
    produto.setMemoria(dto.memoria());
    produto.setBios(dto.bios());
    produto.setGrafico(dto.grafico());
    produto.setLan(dto.lan());
    produto.setSlots(dto.slots());
    produto.setArmazenamento(dto.armazenamento());
    repository.persist(produto);
    return PlacaMaeResponseDTO.valueOf(produto);
  }

  @Transactional
  // Primeiro cadastro o produto. Em seguida cadastro o lote.
    @Override
  public ProcessadorResponseDTO insertProcessador(ProcessadorDTO dto) {
    Processador produto = new Processador();
    produto.setNome(dto.nome());
    produto.setDescricao(dto.descricao());
    produto.setModelo(dto.modelo());
    produto.setCodigoBarras(dto.codigoBarras());
    produto.setMarca(dto.marca());
    produto.setAltura(dto.altura());
    produto.setLargura(dto.largura());
    produto.setComprimento(dto.comprimento());
    produto.setPeso(dto.peso());
    produto.setNomeImagem(dto.nomeImagem());
    produto.setClassificacao(dto.classificacao());
    // produto.setClassificacao(repositoryClassificacao.findById(dto.classificacao()));
    produto.setSoquete(dto.soquete());
    produto.setPistas(dto.pistas());
    produto.setCompatibilidadeChipset(dto.compatibilidadeChipset());
    produto.setBloqueado(dto.bloqueado());
    produto.setCanaisMemoria(dto.canaisMemoria());
    produto.setCapacidadeMaxMemoria(dto.capacidadeMaxMemoria());
    produto.setPontenciaBase(dto.pontenciaBase());
    produto.setPotenciaMaxima(dto.potenciaMaxima());
    produto.setFrequenciaBase(dto.frequenciaBase());
    produto.setFrequenciaMaxima(dto.frequenciaMaxima());
    produto.setTamanhoCacheL3(dto.tamanhoCacheL3());
    produto.setTamanhoCacheL2(dto.tamanhoCacheL2());
    produto.setNumNucleosFisicos(dto.numNucleosFisicos());
    produto.setNumThreads(dto.numThreads());
    produto.setVelMaxMemoria(dto.velMaxMemoria());
    produto.setConteudoEmbalagem(dto.conteudoEmbalagem());
    repository.persist(produto);
    return ProcessadorResponseDTO.valueOf(produto);
  }

  @Override
  @Transactional
  public void update(ProdutoDTO dto, Long id) {
    Produto produto = repository.findById(id);
    produto.setNome(dto.nome());
    produto.setDescricao(dto.descricao());
    produto.setModelo(dto.modelo());
    produto.setCodigoBarras(dto.codigoBarras());
    produto.setMarca(dto.marca());
    produto.setAltura(dto.altura());
    produto.setLargura(dto.largura());
    produto.setComprimento(dto.comprimento());
    produto.setPeso(dto.peso());
    repository.persist(produto);
  }

  @Transactional
    @Override
  public void updatePlacaMae(PlacaMaeDTO dto, Long id) {
    PlacaMae produto = (PlacaMae) repository.findById(id);
    produto.setNome(dto.nome());
    produto.setDescricao(dto.descricao());
    produto.setModelo(dto.modelo());
    produto.setCodigoBarras(dto.codigoBarras());
    produto.setMarca(dto.marca());
    produto.setAltura(dto.altura());
    produto.setLargura(dto.largura());
    produto.setComprimento(dto.comprimento());
    produto.setPeso(dto.peso());
    produto.setNomeImagem(dto.nomeImagem());
    produto.setClassificacao(dto.classificacao());
    // produto.setClassificacao(repositoryClassificacao.findById(dto.classificacao()));
    produto.setCpu(dto.cpu());
    produto.setChipset(dto.chipset());
    produto.setMemoria(dto.memoria());
    produto.setBios(dto.bios());
    produto.setGrafico(dto.grafico());
    produto.setLan(dto.lan());
    produto.setSlots(dto.slots());
    produto.setArmazenamento(dto.armazenamento());
    repository.persist(produto);
  }

    @Transactional
   public void updateProcessador(ProcessadorDTO dto, Long id) {
    Processador produto = (Processador) repository.findById(id);
    produto.setNome(dto.nome());
    produto.setDescricao(dto.descricao());
    produto.setModelo(dto.modelo());
    produto.setCodigoBarras(dto.codigoBarras());
    produto.setMarca(dto.marca());
    produto.setAltura(dto.altura());
    produto.setLargura(dto.largura());
    produto.setComprimento(dto.comprimento());
    produto.setPeso(dto.peso());
    produto.setNomeImagem(dto.nomeImagem());
    produto.setClassificacao(dto.classificacao());
    // produto.setClassificacao(repositoryClassificacao.findById(dto.classificacao()));
    produto.setSoquete(dto.soquete());
    produto.setPistas(dto.pistas());
    produto.setCompatibilidadeChipset(dto.compatibilidadeChipset());
    produto.setBloqueado(dto.bloqueado());
    produto.setCanaisMemoria(dto.canaisMemoria());
    produto.setCapacidadeMaxMemoria(dto.capacidadeMaxMemoria());
    produto.setPontenciaBase(dto.pontenciaBase());
    produto.setPotenciaMaxima(dto.potenciaMaxima());
    produto.setFrequenciaBase(dto.frequenciaBase());
    produto.setFrequenciaMaxima(dto.frequenciaMaxima());
    produto.setTamanhoCacheL3(dto.tamanhoCacheL3());
    produto.setTamanhoCacheL2(dto.tamanhoCacheL2());
    produto.setNumNucleosFisicos(dto.numNucleosFisicos());
    produto.setNumThreads(dto.numThreads());
    produto.setVelMaxMemoria(dto.velMaxMemoria());
    produto.setConteudoEmbalagem(dto.conteudoEmbalagem());
    repository.persist(produto);
  }

  @Override
  @Transactional
  // Este método é chamado de forma automatizada pelo método ativaLote de
  // LoteService
  public ProdutoResponseDTO update(ProdutoPatchDTO dto) {
    Produto produto = repository.findById(dto.id());

    if (dto.quantidadeUnidades() != null) {
      // Este if verifica se a quantidade desse produto em estoque é zero. Isso é
      // necessário para que não ocorra mistura de Lotes diferentes (adicionar as
      // novas unidades de um produto que vieram do Fornecedor B, às unidades
      // preexistentes desse mesmo produto que vieram do Fornecedor A). Para o caso
      // onde as novas unidades de um produto vem do mesmo fornecedor, obtidas pelo
      // mesmo preço e mantidas no mesmo valor, existe o método updateQuantidade de
      // LoteService.
      if (produto.getQuantidadeUnidades() == null || produto.getQuantidadeUnidades() == 0) {
        try {
          produto.setValorVenda(dto.valorVenda());
          produto.setLoteAtual(repositoryLote.findById(dto.idLoteProduto()));
          produto.setQuantidadeUnidades(dto.quantidadeUnidades());

        } catch (OptimisticLockException e) {
          throw new GeneralErrorException(
              "500",
              "Server Error",
              "ProdutoServiceImpl(update)",
              "Não consegui realizar o update do produto por conta de concorrência no banco de dados. Tente novamente."
                  + e);
        }

      } else {
        throw new GeneralErrorException(
            "500",
            "Server Error",
            "ProdutoServiceImpl(update)",
            "Não consegui realizar o update do produto porque quantidadeUnidades do produto atual não é zero!");
      }
    } else {
      if (produto.getQuantidadeNaoConvencional() == null || produto.getQuantidadeNaoConvencional() == 0) {
        try {
          produto.setValorVenda(dto.valorVenda());
          produto.setLoteAtual(repositoryLote.findById(dto.idLoteProduto()));
          produto.setQuantidadeNaoConvencional(dto.quantidadeNaoConvencional());

        } catch (OptimisticLockException e) {
          throw new GeneralErrorException(
              "500",
              "Server Error",
              "ProdutoServiceImpl(update)",
              "Não consegui realizar o update do produto por conta de concorrência no banco de dados. Tente novamente."
                  + e);
        }

      } else {
        throw new GeneralErrorException(
            "500",
            "Server Error",
            "ProdutoServiceImpl(update)",
            "Não consegui realizar o update do produto porque quantidadeNaoConvencional do produto atual não é zero!");
      }

    }

    /*
     * // Analisa se tem o produto em estoque, soma a nova quantidade e persiste no
     * banco
     * EntityManager em = emf.createEntityManager();
     * EntityTransaction transaction = em.getTransaction();
     * 
     * try {
     * transaction.begin();
     * String sql1 = "SELECT quantidade FROM produto WHERE id = ?1 FOR SHARE ";
     * Query query = em
     * .createNativeQuery(sql1)
     * .setParameter(1, id);
     * Integer quantidade = (Integer) query.getSingleResult();
     * Integer quantFinal = quantidade + dto.quantidade();
     * 
     * String sql4 = "UPDATE produto SET quantidade = ?1 WHERE id = ?2";
     * em
     * .createNativeQuery(sql4)
     * .setParameter(1, quantFinal)
     * .setParameter(2, id)
     * .executeUpdate();
     * transaction.commit();
     * } catch (Exception e) {
     * if (transaction != null && transaction.isActive()) {
     * transaction.rollback();
     * throw new GeneralErrorException(
     * "400",
     * "Bad Resquest",
     * "ProdutoServiceImpl(update)",
     * "Não consegui gravar no banco. " + e.getMessage()
     * );
     * }
     * } finally {
     * em.close();
     * }
     */

    return ProdutoResponseDTO.valueOf(produto);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    try {
      repository.deleteById(id);
    } catch (Exception e) {
      throw new GeneralErrorException(
          "400",
          "Bad Resquest",
          "ProdutoServiceImpl(deleteById)",
          "Não posso deletar o objeto por conta de relações ativas com outras entidades.");
    }

  }

  @Override
  public Produto findById(Long id) {
    return repository.findById(id);
  }

    public long countByName(String nome){
      return repository.countByName(nome);
    }

  @Override
  public ProdutoResponseDTO findByCodigoBarras(String codigoBarras) {
    return ProdutoResponseDTO.valueOf(repository.findByCodigoBarras(codigoBarras));
  }

  @Override
  public List<ProdutoResponseDTO> findByName(String nome, int page, int pageSize) {

    return repository
        .findByName(nome, page, pageSize)
        .stream()
        .map(p -> ProdutoResponseDTO.valueOf(p))
        .toList();
  }

    public List<ProdutoResponseDTO> findByClassificacao(Long idClassificacao) {
      return repository
      .findByClassificacao(idClassificacao)
      .stream()
      .map(p -> ProdutoResponseDTO.valueOf(p))
      .toList();
  }

    @Override
  public List<Classificacao> retornaClassificacao() {
    return repositoryClassificacao
        .listAll();

  }
     public List<ProdutoResponseDTO> findTodos() {
       return repository
                .listAll()
                .stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    } 

  @Override
  public List<ProdutoResponseDTO> findByAll(int page, int pageSize) {
    List<Produto> list = repository
        .findAll()
        .page(page, pageSize)
        .list();
    return list
        .stream()
        .map(f -> ProdutoResponseDTO.valueOf(f))
        .collect(Collectors.toList());

  }

  @Override
  public long count() {
    return repository.count();
  }

    public FornecedorResponseDTO encontraFornecedor(ProdutoFornecedorPatch dto) {
      
      LocalDateTime agora = LocalDateTime.now();
      DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String dataFormatador = agora.format(formatador);
      LocalDateTime tempDateTime = LocalDateTime.parse(dataFormatador, formatador);
      
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime novoDateTime = LocalDateTime.parse(dto.dataHoraVenda(), formatter);

      for(Lote lt : repositoryLote.findByIdProduto(dto.idProduto())) {

        if(lt.getDataHoraUltimoVendido() == null) {
          if(lt.getDataHoraChegadaLote().isBefore(novoDateTime) && tempDateTime.isAfter(novoDateTime))
          System.out.println(lt.getFornecedor());
            return FornecedorResponseDTO.valueOf(lt.getFornecedor());
        } else {
          if(lt.getDataHoraChegadaLote().isBefore(novoDateTime) && tempDateTime.isAfter(novoDateTime))
          System.out.println(lt.getFornecedor());
            return FornecedorResponseDTO.valueOf(lt.getFornecedor());
        }
      }
      throw new GeneralErrorException(
            "400",
            "Bad Resquest",
            "ProdutoServiceImpl(encontraFornecedor)",
            "Não consegui encontrar o Fornecedor deste produto na data estipulada");
    }

}
