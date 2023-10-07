package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.ClassificacaoDTO;
// import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;

public interface ProdutoService {

    // INSERIR NOVO PRODUTO:
    public ProdutoResponseDTO insert(ProdutoDTO dto);

    // ATUALIZAR DETALHES DO PRODUTO E CLASSIFICAÇÃO JUNTO:
    public ProdutoResponseDTO update(ProdutoDTO dto, Long id);

    // public ProdutoResponseDTO updateLote(List<LoteDTO> lt, Long id);

    // ATUALIZAR CLASSIFICAÇÃO DE UM CERTO ID.
    public ProdutoResponseDTO updateClassificacao(List<ClassificacaoDTO> cl, Long id);

    public void delete(Long id);

    public ProdutoResponseDTO findById(Long id);

    public ProdutoResponseDTO findByCodigoBarras(String cpf);

    public List<ProdutoResponseDTO> findByName(String nome);

    public List<ProdutoResponseDTO> findByAll();
}
