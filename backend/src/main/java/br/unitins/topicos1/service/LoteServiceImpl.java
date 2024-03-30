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
        lote.setQuantidadeUnidades(dto.quantidadeUnidades());
        lote.setQuantidadeNaoConvencional(dto.quantidadeNaoConvencional());
        lote.setUnidadeDeMedida(dto.unidadeDeMedida());
        lote.setValorVenda(dto.valorVenda());
        lote.setCustoCompra(dto.custoCompra());
        lote.setFornecedor(repositoryFornecedor.findById(dto.idFornecedor()));
        lote.setProduto(repositoryProduto.findById(dto.idProduto()));
        
        try {
            repository.persist(lote); 
        } catch (Exception e) {
            throw new GeneralErrorException(
            "500",
            "Server Error",
            "LoteServiceImpl(insert)",
            "Não consegui persistir o lote no banco de dados.");
        }
        // O estoque do produto já acabou. O Lote recém inserido será automaticamente ativado.
        if(repositoryProduto.findById(dto.idProduto()).getQuantidadeUnidades() == 0 || repositoryProduto.findById(dto.idProduto()).getQuantidadeNaoConvencional() == 0) {
            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String dataFormatada = agora.format(formatter);
            LocalDateTime novoDateTime = LocalDateTime.parse(dataFormatada, formatter);
            // Ativa o Lote
            lote.setDataHoraChegadaLote(novoDateTime);
            ProdutoPatchDTO prodpatch = new ProdutoPatchDTO(dto.idProduto(), dto.valorVenda(), dto.quantidadeUnidades(), dto.quantidadeNaoConvencional(), lote.getId());
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
    // Chegaram mais unidades do mesmo produto, do mesmo fornecedor, pelo mesmo custo de compra e irei manter o mesmo valor de venda. Neste caso só altero a quantidade e mais nada.
    public LoteResponseDTO updateQuantidade(LotePatchQDTO dto) {
        Lote lote = repository.findById(dto.id());

        if(dto.quantidadeUnidades() != null) {

        if(lote != null) {
           try {
            lote.getProduto().setQuantidadeUnidades(lote.getProduto().getQuantidadeUnidades()+dto.quantidadeUnidades());
            lote.setQuantidadeUnidades(lote.getQuantidadeUnidades()+dto.quantidadeUnidades());
         } catch (OptimisticLockException e) {
            throw new GeneralErrorException(
        "500",
        "Server Error",
        "LoteServiceImpl(updateQuantidade)",
        "Não consegui realizar o insert de Lote por conta de concorrência no banco de dados. Tente novamente." + e);
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
        if(lote != null) {
            try {
             lote.getProduto().setQuantidadeNaoConvencional(lote.getProduto().getQuantidadeNaoConvencional()+dto.quantidadeNaoConvencional());
             lote.setQuantidadeNaoConvencional(lote.getQuantidadeNaoConvencional()+dto.quantidadeNaoConvencional());
          } catch (OptimisticLockException e) {
             throw new GeneralErrorException(
         "500",
         "Server Error",
         "LoteServiceImpl(updateQuantidade)",
         "Não consegui realizar o insert de Lote por conta de concorrência no banco de dados. Tente novamente." + e);
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
        if(lote != null) {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dataFormatada = agora.format(formatter);
        LocalDateTime novoDateTime = LocalDateTime.parse(dataFormatada, formatter);
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
        "Não consegui realizar o insert de Lote por algum motivo. Tente novamente." + e);
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

    @Override
    public LoteResponseDTO findById(Long id) {
        return LoteResponseDTO.valueOf(repository.findById(id));
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
        return repository
                .listAll()
                .stream()
                .map(LoteResponseDTO::valueOf)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Lote lote = repository.findById(id);
        if(lote.getDataHoraUltimoVendido() == null) {
            throw new GeneralErrorException(
        "400",
        "Bad Request",
        "LoteServiceImpl(delete)",
        "Lote ativo. Impossível deletar.");
        } else {
            try {
                repository.deleteById(id);
            } catch (Exception e) {
                throw new GeneralErrorException(
        "500",
        "Server Error",
        "LoteServiceImpl(delete)",
        "Não consegui deletar o produto em questão. Tente novamente mais tarde." + e);
            }
        
        }
    }

    public LoteResponseDTO ativaLote(Long idProduto) {
        // O for abaixo coleta todos os lotes relacionados ao Produto de idProduto
        for(Lote lt : repository.findAll(idProduto)) {
            // O if abaixo encontra o último Lote cadastrado contendo o Produto de idProduto. Esse Lote em questão já está cadastrado no sistema, porém ainda não está ativado pois possui dataHoraChegadaLote == null
          if(lt.getDataHoraChegadaLote() == null) {
            Lote lote = lt;
            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String dataFormatada = agora.format(formatter);
            LocalDateTime novoDateTime = LocalDateTime.parse(dataFormatada, formatter);
            // Ativa o Lote
            lote.setDataHoraChegadaLote(novoDateTime);
            ProdutoPatchDTO prodpatch = new ProdutoPatchDTO(lote.getProduto().getId(), lote.getValorVenda(), lote.getQuantidadeUnidades(), lote.getQuantidadeNaoConvencional(), lote.getId());
            // O método abaixo altera os valores de venda, quantidade e id do Lote atual no produto em questão. O Lote torna-se ativado e o produto agora aponta para ele.
            serviceProduto.update(prodpatch);
            return LoteResponseDTO.valueOf(lote);
          }
        }
        
        throw new GeneralErrorException(
        "400",
        "Bad Request",
        "LoteServiceImpl(ativaLote)",
        "Não existe Lote pré cadastrado para este produto e por isso não pode ser ativado. Utilize o método LoteServiceImpl(insert).");
  
    }

}
