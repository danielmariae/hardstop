package br.unitins.topicos1.dto.logistica;

import java.util.List;

import br.unitins.topicos1.dto.endereco.EnderecoDTO;
import br.unitins.topicos1.dto.telefone.TelefoneDTO;
import br.unitins.topicos1.model.utils.Logistica;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LogisticaDTO(
  @NotBlank(message = "O campo nomeFantasia não pode ser nulo.")
  String nomeFantasia,
  @NotBlank(message = "O campo cnpj não pode ser nulo.")
  @Pattern(regexp = "([0-9]{2}[-./\s][0-9]{3}[-./\s][0-9]{3}[-./\s][0-9]{4}[-./\s][0-9]{2})|([0-9]{14})", message = "Valor digitado inválido!")
  String cnpj,
  String endSite,
   @Valid // Ativa a validação dos elementos da lista
  List<EnderecoDTO> listaEndereco,
  @Valid // Ativa a validação dos elementos da lista
  List<TelefoneDTO> listaTelefone
) {
  public static LogisticaDTO valueOf(Logistica logistica) {
    return new LogisticaDTO(
    logistica.getNomeFantasia(),
    logistica.getCnpj(),
    logistica.getEndSite(),
    logistica
        .getListaEndereco()
        .stream()
        .map(e -> EnderecoDTO.valueOf(e))
        .toList(),
      logistica
        .getListaTelefone()
        .stream()
        .map(t -> TelefoneDTO.valueOf(t))
        .toList()
    );
  }




}
