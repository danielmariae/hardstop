package br.unitins.topicos1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PatchSenhaDTO(
  @NotBlank(message = "O campo senhaAntiga não pode ser nulo.")
  String senhaAntiga,
  @NotBlank(message = "O campo senhaAtual não pode ser nulo.")
  @Pattern(
    regexp = "(?=^.{6,10}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+}{:;'?/<.>,])(?!.*\\s).*$",
    message = "Senha forte (pelo menos 1 letra maiúscula, 1 letra minúscula, 1 dígito, 1 caractere especial). Entre 6 a 10 caracteres."
  )
  String senhaAtual
) {}
