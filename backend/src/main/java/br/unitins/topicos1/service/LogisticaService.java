package br.unitins.topicos1.service;

import java.util.List;

// import br.unitins.topicos1.dto.ClassificacaoDTO;
// import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.LogisticaDTO;
import br.unitins.topicos1.dto.LogisticaResponseDTO;


public interface LogisticaService {

    // INSERIR NOVO PRODUTO:
    public LogisticaResponseDTO insert(LogisticaDTO dto);

    // ATUALIZAR DETALHES DO PRODUTO E CLASSIFICAÇÃO JUNTO:
    public LogisticaResponseDTO update(LogisticaDTO dto, Long id);

    // IMPLEMENTAÇÃO NA PROVA A2:
    // public LogisticaResponseDTO updateLote(List<LoteDTO> lt, Long id);

    // DELETAR PRODUTO POR ID:
    public void delete(Long id);

    // PROCURAR POR ID:
    public LogisticaResponseDTO findById(Long id);

    // LISTAR TODOS:
    public List<LogisticaResponseDTO> findAll();

        // LISTAR DE FORMA PAGINADA
        public List<LogisticaResponseDTO> findByAll(int page, int pageSize);

        // CONTADOR:
        public long count();
    
}
