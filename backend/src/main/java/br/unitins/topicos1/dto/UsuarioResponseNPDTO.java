// package br.unitins.topicos1.dto;

// import br.unitins.topicos1.model.Usuario;
// import java.time.LocalDate;
// import java.util.List;

// public record UsuarioResponseNPDTO(
//   Long id,
//   String nome,
//   LocalDate dataNascimento,
//   String cpf,
//   String sexo,
//   String login,
//   String email,
//   List<EnderecoResponseDTO> listaEndereco,
//   List<TelefoneResponseDTO> listaTelefone
// ) {
//   public static UsuarioResponseNPDTO valueOf(Usuario usuario) {
//     return new UsuarioResponseNPDTO(
//       usuario.getId(),
//       usuario.getNome(),
//       usuario.getDataNascimento(),
//       usuario.getCpf(),
//       usuario.getSexo(),
//       usuario.getLogin(),
//       usuario.getEmail(),
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
