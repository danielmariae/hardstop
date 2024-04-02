package br.unitins.topicos1.service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.ProcessadorDTO;
import br.unitins.topicos1.dto.ProcessadorResponseDTO;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoFornecedorPatch;
import br.unitins.topicos1.dto.ProdutoPatchDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.model.Classificacao;
import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Lote;
import br.unitins.topicos1.model.Processador;
import br.unitins.topicos1.model.Produto;
import br.unitins.topicos1.repository.ClassificacaoRepository;
import br.unitins.topicos1.repository.LoteRepository;
import br.unitins.topicos1.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;

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
     // Primeiro cadastro o produto. Em seguida cadastro o lote.
     public ProcessadorResponseDTO insertProcessador(ProcessadorDTO dto) {
      System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
      Processador produto = new Processador();
      System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
      produto.setNome(dto.nome());
      produto.setDescricao(dto.descricao());
      produto.setModelo(dto.modelo());
      produto.setCodigoBarras(dto.codigoBarras());
      produto.setMarca(dto.marca());
      produto.setAltura(dto.altura());
      produto.setLargura(dto.largura());
      produto.setComprimento(dto.comprimento());
      produto.setPeso(dto.peso());
      System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCc");
      produto.setNomeImagem(dto.nomeImagem());
      System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
      produto.setClassificacao(dto.classificacao());
      //produto.setClassificacao(repositoryClassificacao.findById(dto.classificacao()));
      System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
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
      System.out.println("1111111111111111111111111111111111111111111111111111111111111");
      repository.persist(produto);
      System.out.println("22222222222222222222222222222222222222222222222222222222222222");
      return ProcessadorResponseDTO.valueOf(produto);
  }

    @Override
    @Transactional
    // Este método é chamado de forma automatizada pelo método ativaLote de LoteService
    public ProdutoResponseDTO update(ProdutoPatchDTO dto) {
        Produto produto = repository.findById(dto.id());

        if(dto.quantidadeUnidades() != null) {
          // Este if verifica se a quantidade desse produto em estoque é zero. Isso é necessário para que não ocorra mistura de Lotes diferentes (adicionar as novas unidades de um produto que vieram do Fornecedor B, às unidades preexistentes desse mesmo produto que vieram do Fornecedor A). Para o caso onde as novas unidades de um produto vem do mesmo fornecedor, obtidas pelo mesmo preço e mantidas no mesmo valor, existe o método updateQuantidade de LoteService.
        if(produto.getQuantidadeUnidades() == 0) {

          try {
          produto.setValorVenda(dto.valorVenda());
          produto.setLoteAtual(repositoryLote.findById(dto.idLoteProduto()));
          produto.setQuantidadeUnidades(dto.quantidadeUnidades());
          
          } catch (OptimisticLockException e) {
              throw new GeneralErrorException(
          "500",
          "Server Error",
          "ProdutoServiceImpl(update)",
          "Não consegui realizar o update do produto por conta de concorrência no banco de dados. Tente novamente." + e);
          }

        } else {
          throw new GeneralErrorException(
            "400",
            "Bad Resquest",
            "ProdutoServiceImpl(update)",
            "Não posso realizar update se a quantidade de produto em estoque for diferente de zero.");
        }
      } else {

        if(produto.getQuantidadeNaoConvencional() == 0) {

          try {
          produto.setValorVenda(dto.valorVenda());
          produto.setLoteAtual(repositoryLote.findById(dto.idLoteProduto()));
          produto.setQuantidadeNaoConvencional(dto.quantidadeNaoConvencional());
          
          } catch (OptimisticLockException e) {
              throw new GeneralErrorException(
          "500",
          "Server Error",
          "ProdutoServiceImpl(update)",
          "Não consegui realizar o update do produto por conta de concorrência no banco de dados. Tente novamente." + e);
          }

        } else {
          throw new GeneralErrorException(
            "400",
            "Bad Resquest",
            "ProdutoServiceImpl(update)",
            "Não posso realizar update se a quantidade de produto em estoque for diferente de zero.");
        }


      }


       /* 
      // Analisa se tem o produto em estoque, soma a nova quantidade e persiste no banco
      EntityManager em = emf.createEntityManager();
      EntityTransaction transaction = em.getTransaction();

      try {
        transaction.begin();
        String sql1 = "SELECT quantidade FROM produto WHERE id = ?1 FOR SHARE ";
        Query query = em
          .createNativeQuery(sql1)
          .setParameter(1, id);
        Integer quantidade = (Integer) query.getSingleResult();
        Integer quantFinal = quantidade + dto.quantidade();

          String sql4 = "UPDATE produto SET quantidade = ?1 WHERE id = ?2";
          em
            .createNativeQuery(sql4)
            .setParameter(1, quantFinal)
            .setParameter(2, id)
            .executeUpdate();
          transaction.commit();
      } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
          transaction.rollback();
          throw new GeneralErrorException(
            "400",
            "Bad Resquest",
            "ProdutoServiceImpl(update)",
            "Não consegui gravar no banco. " + e.getMessage()
          );
        }
      } finally {
        em.close();
      } */
        
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
    public ProdutoResponseDTO findById(Long id) {
        return ProdutoResponseDTO.valueOf(repository.findById(id));
    }

    @Override
    public ProdutoResponseDTO findByCodigoBarras(String codigoBarras) {
        return ProdutoResponseDTO.valueOf(repository.findByCodigoBarras(codigoBarras));
    }

    @Override
    public List<ProdutoResponseDTO> findByName(String nome) {
        return repository
        .findByName(nome)
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

    public Fornecedor encontraFornecedor(ProdutoFornecedorPatch dto) {
      
      LocalDateTime agora = LocalDateTime.now();
      DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String dataFormatador = agora.format(formatador);
      LocalDateTime tempDateTime = LocalDateTime.parse(dataFormatador, formatador);
      
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime novoDateTime = LocalDateTime.parse(dto.dataHoraVenda(), formatter);

      for(Lote lt : repositoryLote.findAll(dto.idProduto())) {

        if(lt.getDataHoraUltimoVendido() == null) {
          if(lt.getDataHoraChegadaLote().isBefore(novoDateTime) && tempDateTime.isAfter(novoDateTime))
            return lt.getFornecedor();
        } else {
          if(lt.getDataHoraChegadaLote().isBefore(novoDateTime) && tempDateTime.isAfter(novoDateTime))
            return lt.getFornecedor();
        }
      }
      throw new GeneralErrorException(
            "400",
            "Bad Resquest",
            "ProdutoServiceImpl(encontraFornecedor)",
            "Não consegui encontrar o Fornecedor deste produto na data estipulada");
    }

}
