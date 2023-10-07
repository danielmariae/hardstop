package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Classificacao;
import jakarta.validation.constraints.NotBlank;

public record ClassificacaoDTO(
    Long id, 
    @NotBlank(message = "O campo nome não pode ser nulo.")
    String nome) {
        public static ClassificacaoDTO valueOf(Classificacao classificacao) {
            return new ClassificacaoDTO(
                classificacao.getId(),
                classificacao.getNome()
            ); 
        }
    }
