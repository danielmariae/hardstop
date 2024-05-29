package br.unitins.topicos1.dto.produto.classificacao;

import br.unitins.topicos1.model.produto.Classificacao;
import jakarta.validation.constraints.NotBlank;

public record ClassificacaoDTO(
     
    @NotBlank(message = "O campo nome não pode ser nulo.")
    String nome) {
        public static ClassificacaoDTO valueOf(Classificacao classificacao) {
            return new ClassificacaoDTO(
            
                classificacao.getNome()
            ); 
        }
    }
