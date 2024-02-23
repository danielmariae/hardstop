package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO( 
    @NotEmpty(message = "O campo Login não pode ser vazio.")
    String login,
    @NotEmpty(message = "O campo Senha não pode ser vazio.")
    String senha
    ) {
   
}
