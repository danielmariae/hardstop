package br.unitins.topicos1.service;

import java.util.List;

// import br.unitins.topicos1.dto.ClassificacaoDTO;
// import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;


public interface ProdutoService {

    // INSERIR NOVO PRODUTO:
    public ProdutoResponseDTO insert(ProdutoDTO dto);

    // ATUALIZAR DETALHES DO PRODUTO E CLASSIFICAÇÃO JUNTO:
    public ProdutoResponseDTO update(ProdutoDTO dto, Long id);

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
}
