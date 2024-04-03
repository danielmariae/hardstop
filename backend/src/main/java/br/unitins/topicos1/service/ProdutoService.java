package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.PlacaMaeDTO;
import br.unitins.topicos1.dto.PlacaMaeResponseDTO;
import br.unitins.topicos1.dto.ProcessadorDTO;
import br.unitins.topicos1.dto.ProcessadorResponseDTO;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoFornecedorPatch;
import br.unitins.topicos1.dto.ProdutoPatchDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.model.Classificacao;
import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Produto;


public interface ProdutoService {

    // INSERIR NOVO PRODUTO:
    ProdutoResponseDTO insert(ProdutoDTO dto);

    // INSERIR NOVO PRODUTO:
    ProcessadorResponseDTO insertProcessador(ProcessadorDTO dto);

    // INSERIR NOVO PRODUTO:
    PlacaMaeResponseDTO insertPlacaMae(PlacaMaeDTO dto);

    // ATUALIZAR UM PRODUTO
    void update(ProdutoDTO dto, Long id);

    // ATUALIZAR UM PRODUTO
    void updateProcessador(ProcessadorDTO dto, Long id);

    // ATUALIZAR UM PRODUTO
    void updatePlacaMae(PlacaMaeDTO dto, Long id);

    // ATUALIZAR DETALHES DO PRODUTO E CLASSIFICAÇÃO JUNTO:
    ProdutoResponseDTO update(ProdutoPatchDTO dto);

    // DELETAR PRODUTO POR ID:
    void delete(Long id);

    // PROCURAR POR ID:
    Produto findById(Long id);

    // PROCURAR POR CÓDIGO DE BARRAS:
    ProdutoResponseDTO findByCodigoBarras(String cpf);

    // PROCURAR POR NOME:
    List<ProdutoResponseDTO> findByName(String nome);

    // PROCURAR POR CLASSIFICAÇÃO
    List<ProdutoResponseDTO> findByClassificacao(Long idClassificacao);

    // PROCURAR POR CLASSIFICAÇÃO
    List<Classificacao> retornaClassificacao();

    // LISTAR TODOS:
    public List<ProdutoResponseDTO> findTodos();

    // LISTAR DE FORMA PAGINADA
    List<ProdutoResponseDTO> findByAll(int page, int pageSize);

    // ENCONTRAR O FORNECEDOR DE UM PRODUTO ESTRAGADO
    Fornecedor encontraFornecedor(ProdutoFornecedorPatch dto);

    // CONTADOR:
    public long count();

}
