package br.unitins.topicos1.service;


import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.LotePatchDTO;
import br.unitins.topicos1.dto.LoteResponseDTO;

import java.util.List;

public interface LoteService {
    LoteResponseDTO insert(LoteDTO dto);

    // ATUALIZAR LOTE:
    LoteResponseDTO update(LotePatchDTO dto);

    // DELETAR LOTE:
    void delete(Long id);

    // PROCURAR POR ID:
    LoteResponseDTO findById(Long id);

    // PROCURAR POR NOME:
    List<LoteResponseDTO> findByName(String lote);

    // LISTAR TODOS:
    List<LoteResponseDTO> findByAll();
}
