package br.unitins.topicos1.service;

import java.util.ArrayList;
// import java.util.ArrayList;
import java.util.List;

// import br.unitins.topicos1.dto.ClassificacaoDTO;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.model.Classificacao;
import br.unitins.topicos1.model.Lote;
import br.unitins.topicos1.model.Produto;
import br.unitins.topicos1.repository.LoteRepository;
import br.unitins.topicos1.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProdutoServiceImpl implements ProdutoService {

    @Inject
    ProdutoRepository repository;

    @Inject
    LoteRepository repositoryLote;

    @Override
    @Transactional
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
        produto.setCustoCompra(dto.custoCompra());
        produto.setValorVenda(dto.valorVenda());
        produto.setQuantidade(dto.quantidade());
        produto.setListaLote(new ArrayList<Lote>());
        produto.getListaLote().add(repositoryLote.findById(dto.idLoteProduto())); 

        if (dto.classificacao() != null) {
            Classificacao classificacao = new Classificacao();
            classificacao.setNome(dto.classificacao().nome());
            produto.setClassificacao(classificacao);
        }
        
        repository.persist(produto);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO update(ProdutoDTO dto, Long id) {
        Produto produto = repository.findById(id);

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setCodigoBarras(dto.codigoBarras());
        produto.setMarca(dto.marca());
        produto.setAltura(dto.altura());
        produto.setLargura(dto.largura());
        produto.setComprimento(dto.comprimento());
        produto.setPeso(dto.peso());
        produto.setCustoCompra(dto.custoCompra());
        produto.setValorVenda(dto.valorVenda());
        produto.setQuantidade(dto.quantidade());
        produto.getListaLote().add(repositoryLote.findById(dto.idLoteProduto()));

         if (dto.classificacao() != null) {
            Classificacao classificacao = new Classificacao();
            classificacao.setNome(dto.classificacao().nome());
            produto.setClassificacao(classificacao);
        }
        

        //repository.persist(produto);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO updateCustoCompra(Double custoCompra, Long id) {
        Produto produto = repository.findById(id);

        produto.setCustoCompra(custoCompra);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO updateValorVenda(Double valorVenda, Long id) {
        Produto produto = repository.findById(id);

        produto.setValorVenda(valorVenda);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO updateQuantidade(Integer quantidade, Long id) {
        Produto produto = repository.findById(id);

        produto.setQuantidade(quantidade);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO updateLote(Long idLote, Long id) {
        Produto produto = repository.findById(id);

        produto.getListaLote().add(repositoryLote.findById(idLote));
        return ProdutoResponseDTO.valueOf(produto);
    }

    // IMPLEMENTAÇÃO NA PROVA A2:
    // @Override
    // public ProdutoResponseDTO updateLote(List<ClassificacaoDTO> cl, Long id) {
    //     Produto produto = repository.findById(id);

    //     List<Long> id1 = new ArrayList<Long>();
    //     List<Long> id2 = new ArrayList<Long>();
        
    //     if(produto.getListaClassificacao() != null || !produto.getListaClassificacao().isEmpty()){
    //         for (Classificacao clf : produto.getListaClassificacao()){
    //             id1.add(clf.getId());
    //         }
    //     }

    //     for (ClassificacaoDTO clfDTO : cl){
    //         id2.add(clfDTO.id());
    //     }

    //     for(Classificacao clf1 : produto.getListaClassificacao()){
    //         for(ClassificacaoDTO clfDTO : cl){
    //             if(clf1.getId() == clfDTO.id()){
    //                 clf1.setNome(clfDTO.nome());
    //                 id1.remove(id1.indexOf(clf1.getId()));
    //                 id2.remove(id2.indexOf(clf1.getId()));
    //             }
    //         }
    //     }

    //     for (int i = 0; i < id2.size(); i++){
    //         for (ClassificacaoDTO clf : cl){
    //             Classificacao classificacao = new Classificacao();
    //             classificacao.setNome(clf.nome());
    //         }
    //     }

    //     repository.persist(produto);
    //     return ProdutoResponseDTO.valueOf(produto);
    // }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
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
    
}
