package br.unitins.topicos1.service;

import java.util.List;

// import br.unitins.topicos1.dto.ClassificacaoDTO;
// import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.FornecedorDTO;
import br.unitins.topicos1.dto.FornecedorResponseDTO;


public interface FornecedorService {

    // INSERIR NOVO PRODUTO:
    public FornecedorResponseDTO insert(FornecedorDTO dto);

    // ATUALIZAR DETALHES DO PRODUTO E CLASSIFICAÇÃO JUNTO:
    public FornecedorResponseDTO update(FornecedorDTO dto, Long id);

    // IMPLEMENTAÇÃO NA PROVA A2:
    // public FornecedorResponseDTO updateLote(List<LoteDTO> lt, Long id);

    // DELETAR PRODUTO POR ID:
    public void delete(Long id);

    // PROCURAR POR ID:
    public FornecedorResponseDTO findById(Long id);

    // LISTAR TODOS:
    public List<FornecedorResponseDTO> findByAll();
}
