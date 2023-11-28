package br.unitins.topicos1.service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoFornecedorPatch;
import br.unitins.topicos1.dto.ProdutoPatchDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.ProdutoValorPatch;
import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Lote;
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
        produto.setCodigoBarras(dto.codigoBarras());
        produto.setMarca(dto.marca());
        produto.setAltura(dto.altura());
        produto.setLargura(dto.largura());
        produto.setComprimento(dto.comprimento());
        produto.setPeso(dto.peso());
        produto.setClassificacao(repositoryClassificacao.findById(dto.idClassificacao())); 
        
        repository.persist(produto);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO update(ProdutoPatchDTO dto) {
        Produto produto = repository.findById(dto.id());

        if(produto.getQuantidade() == 0) {

          try {
          produto.setValorVenda(dto.valorVenda());
          produto.setLoteAtual(repositoryLote.findById(dto.idLoteProduto()));
          produto.setQuantidade(dto.quantidade());
          
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
    public ProdutoResponseDTO updateValorVenda(ProdutoValorPatch dto) {
        Produto produto = repository.findById(dto.id());

        produto.setValorVenda(dto.valor());
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

    @Override
    public List<ProdutoResponseDTO> findByAll() {
        return repository
        .listAll()
        .stream()
        .map(p -> ProdutoResponseDTO.valueOf(p))
        .toList();
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
