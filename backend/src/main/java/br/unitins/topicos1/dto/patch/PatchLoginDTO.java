package br.unitins.topicos1.dto.patch;

import jakarta.validation.constraints.NotBlank;

public record PatchLoginDTO(
    @NotBlank(message = "O campo login não pode ser nulo.")
    String login
) {
    
}
