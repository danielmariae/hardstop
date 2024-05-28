package br.unitins.topicos1.dto.login;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO( 
    @NotEmpty(message = "O campo Login não pode ser vazio.")
    String login,
    @NotEmpty(message = "O campo Senha não pode ser vazio.")
    String senha
    // @NotEmpty(message = "Você precisa informar se quer acessar o painel ADM ou o painel de Cliente.")
    // Integer idAcesso
    // 
    )
    {
}
