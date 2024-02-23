package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Classificacao;

public record ClassificacaoResponseDTO(
    Long id, 
    String nome) {
        public static ClassificacaoResponseDTO valueOf(Classificacao classificacao){
            return new ClassificacaoResponseDTO(classificacao.getId(), classificacao.getNome());
        }
    }
