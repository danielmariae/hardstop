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
    public ProdutoResponseDTO insert(ProdutoDTO dto);

    // ATUALIZAR DETALHES DO PRODUTO E CLASSIFICAÇÃO JUNTO:
    public ProdutoResponseDTO update(ProdutoPatchDTO dto);

    // IMPLEMENTAÇÃO NA PROVA A2:
    // public ProdutoResponseDTO updateLote(List<LoteDTO> lt, Long id);

    // DELETAR PRODUTO POR ID:
    public void delete(Long id);

    // PROCURAR POR ID:
    public ProdutoResponseDTO findById(Long id);

    // PROCURAR POR CÓDIGO DE BARRAS:
    public ProdutoResponseDTO findByCodigoBarras(String cpf);

    // PROCURAR POR NOME:
    public List<ProdutoResponseDTO> findByName(String nome);

    // LISTAR TODOS:
    public List<ProdutoResponseDTO> findByAll();

    // UPDATE VALOR DE VENDA DO PRODUTO
    public ProdutoResponseDTO updateValorVenda(ProdutoValorPatch dto);

    // ENCONTRAR O FORNECEDOR DE UM PRODUTO ESTRAGADO
    public Fornecedor encontraFornecedor(ProdutoFornecedorPatch dto);

}
