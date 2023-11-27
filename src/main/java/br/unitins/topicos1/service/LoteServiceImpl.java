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
    public LoteResponseDTO insert(LoteDTO dto) {

        Lote lote = new Lote();
        lote.setLote(dto.lote());
        lote.setQuantidade(dto.quantidade());
        lote.setValorVenda(dto.valorVenda());
        lote.setCustoCompra(dto.custoCompra());
        lote.setFornecedor(repositoryFornecedor.findById(dto.idFornecedor()));
        lote.setProduto(repositoryProduto.findById(dto.idProduto()));
        lote.setDataHoraChegadaLote(LocalDateTime.now());
        try {
            repository.persist(lote); 
        } catch (Exception e) {
            throw new GeneralErrorException(
            "500",
            "Server Error",
            "LoteServiceImpl(insert)",
            "Não consegui persistir o lote no banco de dados.");
        }
        if(repositoryProduto.findById(dto.idProduto()).getQuantidade() == 0) {
            ProdutoPatchDTO prodpatch = new ProdutoPatchDTO(dto.idProduto(), dto.custoCompra(), dto.valorVenda(), dto.quantidade(), lote.getId());
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
    public LoteResponseDTO update(LotePatchDTO dto) {
        Lote lote = repository.findById(dto.id());

        if(lote != null) {
           try {
            lote.getProduto().setQuantidade(lote.getProduto().getQuantidade()+dto.quantidade());
            lote.setQuantidade(lote.getQuantidade()+dto.quantidade());
         } catch (OptimisticLockException e) {
            throw new GeneralErrorException(
        "500",
        "Server Error",
        "LoteServiceImpl(insert)",
        "Não consegui realizar o insert de Lote por conta de concorrência no banco de dados. Tente novamente." + e);
        } 
            return LoteResponseDTO.valueOf(lote);
        } else {
            throw new GeneralErrorException(
        "400",
        "Bad Request",
        "LoteServiceImpl(update)",
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
        repository.deleteById(id);
    }

}
