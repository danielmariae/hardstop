package br.unitins.topicos1.service;


import java.util.List;

import br.unitins.topicos1.dto.lote.LoteDTO;
import br.unitins.topicos1.dto.lote.LotePatchQDTO;
import br.unitins.topicos1.dto.lote.LotePatchVDTO;
import br.unitins.topicos1.dto.lote.LoteResponseCDTO;
import br.unitins.topicos1.dto.lote.LoteResponseDTO;

public interface LoteService {

    // Traz a lista de Lotes relacionada a um produto com determinado id
    List<LoteResponseDTO> findByIdProduto(Long idProduto, int page, int pageSize);
    List<LoteResponseCDTO> findByIdProdutoTeste(Long idProduto, int page, int pageSize);


    // CONTADOR:
    public Long count2(Long id);
    public Long count();

    LoteResponseDTO insert(LoteDTO dto);
    LoteResponseCDTO insertTeste(LoteDTO dto);

    // ATUALIZAR LOTE:
    // Cuidado ao utilizar updateQuantidade - esse método somente deve ser usado quando chegam novas unidades de um produto mantendo TODOS os dados anteriores para Lote e Produto sem alteração. Neste caso o método soma a quantidade de produtos que chegou à quantidade pré-existente de produtos e atualiza esses dados em Lote e Produto.
    LoteResponseDTO updateQuantidade(LotePatchQDTO dto);
    LoteResponseDTO updateValorVenda(LotePatchVDTO dto);

    // DELETAR LOTE:
    void delete(Long id);

    // PROCURAR POR ID:
    LoteResponseDTO findById(Long id);
    LoteResponseCDTO findByIdTeste(Long id);

    // PROCURAR POR NOME:
    List<LoteResponseDTO> findByName(String lote);

    // LISTAR TODOS:
    List<LoteResponseDTO> findByAll();

    // ATIVA UM LOTE PRÉ CADASTRADO
    LoteResponseDTO ativaLote(Long idLote);
}
