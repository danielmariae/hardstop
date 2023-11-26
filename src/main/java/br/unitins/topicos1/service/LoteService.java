package br.unitins.topicos1.service;


import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.LoteResponseDTO;

import java.util.List;

public interface LoteService {
    LoteResponseDTO insert(LoteDTO dto);

    // ATUALIZAR LOTE:
    LoteResponseDTO update(LoteDTO dto, Long id);

    // IMPLEMENTAÇÃO NA PROVA A2:
    // public ProdutoResponseDTO updateLote(List<LoteDTO> lt, Long id);

    // DELETAR LOTE:
    void delete(Long id);

    // PROCURAR POR ID:
    LoteResponseDTO findById(Long id);

    // PROCURAR POR NOME:
    List<LoteResponseDTO> findByName(String lote);

    // LISTAR TODOS:
    List<LoteResponseDTO> findByAll();
}
