package br.unitins.topicos1.dto.cliente;

import java.time.LocalDate;
import java.util.List;

import br.unitins.topicos1.dto.endereco.EnderecoResponseDTO;
import br.unitins.topicos1.dto.produto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.telefone.TelefoneResponseDTO;
import br.unitins.topicos1.model.utils.Cliente;
import br.unitins.topicos1.model.utils.Perfil;

public record ClienteResponseDTO(
  Long id,
  String nome,
  LocalDate dataNascimento,
  String cpf,
  String sexo,
  String login,
  String email,
  String nomeImagem,
  Perfil perfil,
  List<EnderecoResponseDTO> listaEndereco,
  List<TelefoneResponseDTO> listaTelefone,
  // List<PedidoResponseDTO> listaPedidos,
  List<ProdutoResponseDTO> listaDesejos

) {
  public static ClienteResponseDTO valueOf(Cliente cliente) {
    return new ClienteResponseDTO(
      cliente.getId(),
      cliente.getNome(),
      cliente.getDataNascimento(),
      cliente.getCpf(),
      cliente.getSexo(),
      cliente.getLogin(),
      cliente.getEmail(),
      cliente.getNomeImagem(),
      cliente.getPerfil(),
      cliente
        .getListaEndereco()
        .stream()
        .map(e -> EnderecoResponseDTO.valueOf(e))
        .toList(),
      cliente
        .getListaTelefone()
        .stream()
        .map(t -> TelefoneResponseDTO.valueOf(t))
        .toList(), 
      cliente
        .getListaProduto()
        .stream()
        .map(p -> ProdutoResponseDTO.valueOf(p))
        .toList()

      );
  }
}
