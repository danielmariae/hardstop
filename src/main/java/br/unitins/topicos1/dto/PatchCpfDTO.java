package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PatchCpfDTO(

    @NotBlank(message = "O campo cpf não pode ser nulo.")
    @Pattern(regexp = "([0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{2})|([0-9]{11})", message = "Valor digitado inválido!")
    String cpf
)
    
{}
