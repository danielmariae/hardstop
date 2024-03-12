// package br.unitins.topicos1.dto;

// import br.unitins.topicos1.model.Usuario;

// import java.time.LocalDate;
// import java.util.List;

// public record UsuarioResponseDTO(
//   Long id,
//   String nome,
//   LocalDate dataNascimento,
//   String cpf,
//   String sexo,
//   String login,
//   String email,
//   String nomeImagem,
//   List<TipoUsuarioDTO> tipoUsuario,
//   List<EnderecoResponseDTO> listaEndereco,
//   List<TelefoneResponseDTO> listaTelefone
// ) {
//   public static UsuarioResponseDTO valueOf(Usuario usuario) {
//     return new UsuarioResponseDTO(
//       usuario.getId(),
//       usuario.getNome(),
//       usuario.getDataNascimento(),
//       usuario.getCpf(),
//       usuario.getSexo(),
//       usuario.getLogin(),
//       usuario.getEmail(),
//       usuario.getNomeImagem(),
//       usuario
//         .getTipoUsuario()
//         .stream()
//         .map(t -> TipoUsuarioDTO.valueOf(t))
//         .toList(),
//       usuario
//         .getListaEndereco()
//         .stream()
//         .map(e -> EnderecoResponseDTO.valueOf(e))
//         .toList(),
//       usuario
//         .getListaTelefone()
//         .stream()
//         .map(t -> TelefoneResponseDTO.valueOf(t))
//         .toList()
//     );
//   }
// }
