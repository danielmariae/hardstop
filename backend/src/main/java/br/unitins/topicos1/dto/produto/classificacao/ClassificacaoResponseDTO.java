package br.unitins.topicos1.dto.produto.classificacao;

import br.unitins.topicos1.model.produto.Classificacao;

public record ClassificacaoResponseDTO(
    Long id, 
    String nome) {
        public static ClassificacaoResponseDTO valueOf(Classificacao classificacao){
            return new ClassificacaoResponseDTO(classificacao.getId(), classificacao.getNome());
        }
    }
