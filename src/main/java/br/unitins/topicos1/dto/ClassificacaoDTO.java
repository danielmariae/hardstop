package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Classificacao;

public record ClassificacaoDTO(
    Long id, 
    String nome) {
        public static ClassificacaoDTO valueOf(Classificacao classificacao) {
            return new ClassificacaoDTO(
                classificacao.getId(),
                classificacao.getNome()
            ); 
        }
    }
