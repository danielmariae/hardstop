package br.unitins.topicos1.dto.patch;

import jakarta.validation.constraints.NotBlank;

public record PatchNomeDTO(

@NotBlank(message = "O campo nome não pode ser nulo.")
String nome
) {
    
}
