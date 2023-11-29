package br.unitins.topicos1.service;

import java.util.List;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoFornecedorPatch;
import br.unitins.topicos1.dto.ProdutoPatchDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.ProdutoValorPatch;
import br.unitins.topicos1.model.Fornecedor;


public interface ProdutoService {

    // INSERIR NOVO PRODUTO:
    ProdutoResponseDTO insert(ProdutoDTO dto);

    // ATUALIZAR DETALHES DO PRODUTO E CLASSIFICAÇÃO JUNTO:
    ProdutoResponseDTO update(ProdutoPatchDTO dto);

    // DELETAR PRODUTO POR ID:
    void delete(Long id);

    // PROCURAR POR ID:
    ProdutoResponseDTO findById(Long id);

    // PROCURAR POR CÓDIGO DE BARRAS:
    ProdutoResponseDTO findByCodigoBarras(String cpf);

    // PROCURAR POR NOME:
    List<ProdutoResponseDTO> findByName(String nome);

    // LISTAR TODOS:
    List<ProdutoResponseDTO> findByAll();

    // UPDATE VALOR DE VENDA DO PRODUTO
    ProdutoResponseDTO updateValorVenda(ProdutoValorPatch dto);

    // ENCONTRAR O FORNECEDOR DE UM PRODUTO ESTRAGADO
    Fornecedor encontraFornecedor(ProdutoFornecedorPatch dto);

}
