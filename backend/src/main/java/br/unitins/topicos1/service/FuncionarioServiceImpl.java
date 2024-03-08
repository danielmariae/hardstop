// package br.unitins.topicos1.service;

// import java.util.ArrayList;
// import java.util.List;

// import org.jrimum.domkee.pessoa.CEP;

// import br.unitins.topicos1.Formatadores.EnderecoFormatador;
// import br.unitins.topicos1.Formatadores.TelefoneFormatador;
// import br.unitins.topicos1.Formatadores.UsuarioFormatador;
// import br.unitins.topicos1.application.GeneralErrorException;
// import br.unitins.topicos1.dto.EnderecoDTO;
// import br.unitins.topicos1.dto.EnderecoFuncDTO;
// import br.unitins.topicos1.dto.EnderecoFuncPatchDTO;
// import br.unitins.topicos1.dto.PatchCpfDTO;
// import br.unitins.topicos1.dto.PatchEmailDTO;
// import br.unitins.topicos1.dto.PatchLoginDTO;
// import br.unitins.topicos1.dto.PatchNomeDTO;
// import br.unitins.topicos1.dto.PatchSenhaDTO;
// import br.unitins.topicos1.dto.TelefoneDTO;
// import br.unitins.topicos1.dto.TelefonePatchDTO;
// import br.unitins.topicos1.dto.UsuarioDTO;
// import br.unitins.topicos1.dto.UsuarioResponseDTO;
// import br.unitins.topicos1.model.Endereco;
// import br.unitins.topicos1.model.Perfil;
// import br.unitins.topicos1.model.Telefone;
// import br.unitins.topicos1.model.TipoTelefone;
// import br.unitins.topicos1.model.TipoUsuario;
// import br.unitins.topicos1.model.Usuario;
// import br.unitins.topicos1.repository.EnderecoRepository;
// import br.unitins.topicos1.repository.UsuarioRepository;
// import br.unitins.topicos1.validation.ValidationException;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;
// import jakarta.transaction.Transactional;

// @ApplicationScoped
// public class FuncionarioServiceImpl implements UsuarioService {

//   // @Inject
//   // UsuarioRepository repository;

//   @Inject
//   UsuarioRepository repository;


//   @Inject
//   EnderecoRepository repositoryEndereco;

//   @Inject
//   HashService hashservice;


//   @Override
//   public UsuarioResponseDTO findByIdUsuario(Long id) {
//     Usuario usuario = repository.findById(id);
//     if (usuario == null) 
//             throw new ValidationException("ID", "id inexistente");
//     return UsuarioResponseDTO.valueOf(usuario);
//   }

//   @Override
//   public List<UsuarioResponseDTO> findByNameUsuario(String name) {
//     return repository
//       .findByName(name)
//       .stream()
//       .map(c -> UsuarioResponseDTO.valueOf(c))
//       .toList();
//   }

//   @Override
//   public UsuarioResponseDTO findByCpfUsuario(String cpf) {
//     Usuario usuario = repository.findByCpf(cpf);
//     if (usuario == null) 
//             throw new ValidationException("CPF", "cpf inexistente");
//     return UsuarioResponseDTO.valueOf(usuario);
//   }

//   @Override
//   public List<UsuarioResponseDTO> findByAllUsuario() {
//     return repository
//       .listAll()
//       .stream()
//       .map(c -> UsuarioResponseDTO.valueOf(c))
//       .toList();
//   }

//   @Override
//   @Transactional
//   public void deleteUsuario(Long id) {
//     Usuario user = repository.findById(id);
//     for(TipoUsuario tipo : user.getTipoUsuario()){
//       repository.deleteById(tipo.getId());
//     }
//     for (Endereco end : user.getListaEndereco()) {
//       repository.deleteById(end.getId());
//     }
//     for (Telefone tel : user.getListaTelefone()) {
//       repository.deleteById(tel.getId());
//     }
//     repository.deleteById(id);
//   }

//   @Override
//   @Transactional
//   public UsuarioResponseDTO updateUsuario(UsuarioDTO user, Long id) {
//     Usuario usuario = repository.findById(id);

//     usuario.setNome(user.nome());
//     usuario.setDataNascimento(
//       UsuarioFormatador.validaDataNascimento(user.dataNascimento())
//     );
//     usuario.setCpf(UsuarioFormatador.validaCpf(user.cpf()));
//     usuario.setSexo(user.sexo());
//     usuario.setLogin(user.login());
//     usuario.setSenha(hashservice.getHashSenha(user.senha()));
//     usuario.setEmail(user.email());

//     int i = 0;
//     int j = 0;

//     for (Telefone tele1 : usuario.getListaTelefone()) {
//       i++;
//       j = 0;
//       for (TelefoneDTO tele : user.listaTelefone()) {
//         j++;
//         if (i == j) {
//           tele1.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
//           tele1.setDdd(tele.ddd());
//           tele1.setNumeroTelefone(
//             TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone())
//           );
//         }
//       }
//     }

//     if (user.listaEndereco() != null && !user.listaEndereco().isEmpty()) {
//       try {
//         usuario.setListaEndereco(new ArrayList<Endereco>());
//       } catch (Exception e) {
//         throw new GeneralErrorException("500", "Internal Server Error", "UsuarioServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços do novo Usuario. Tente novamente mais tarde! " +  e.getCause());
//       }
      
//       for (EnderecoDTO end : user.listaEndereco()) {
//         Endereco endereco = new Endereco();
//         endereco.setNome(end.nome());
//         endereco.setLogradouro(end.logradouro());
//         endereco.setNumeroLote(end.numeroLote());
//         endereco.setBairro(end.bairro());
//         endereco.setComplemento(end.complemento());
//         endereco.setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
//         endereco.setLocalidade(end.localidade());
//         endereco.setUF(end.uf());
//         endereco.setPais(end.pais());
//         usuario.getListaEndereco().add(endereco);
//       }
//     }
//     repository.persist(usuario);
//     return UsuarioResponseDTO.valueOf(usuario);
//   }

//   @Override
//   @Transactional
//   public String updateNome(PatchNomeDTO nome, Long id) {
//     Usuario usuario = repository.findById(id);
//     usuario.setNome(nome.nome());
//     return "Nome alterado com sucesso.";
//   }

//   @Override
//   @Transactional
//   public String updateCpf(PatchCpfDTO cpf, Long id) {
//     Usuario usuario = repository.findById(id);
//     verificaCpf(UsuarioFormatador.validaCpf(cpf.cpf()));
//     usuario.setCpf(UsuarioFormatador.validaCpf(cpf.cpf()));
//     return "Cpf alterado com sucesso.";
//   }

//   @Override
//   @Transactional
//   public String updateLogin(PatchLoginDTO login, Long id) {
//     Usuario usuario = repository.findById(id);
//     verificaLogin(login.login());
//     usuario.setLogin(login.login());
//     return "Login alterado com sucesso.";
//   }

//   @Override
//   @Transactional
//   public String updateEmail(PatchEmailDTO email, Long id) {
//     Usuario usuario = repository.findById(id);
//     usuario.setEmail(email.email());
//     return "Email alterado com sucesso.";
//   }

//   @Override
//   @Transactional
//   public String updateSenhaUsuario(PatchSenhaDTO senha, Long id) {
//     Usuario usuario = repository.findById(id);

//     if(hashservice.getHashSenha(senha.senhaAntiga()).equals(usuario.getSenha())) {
//     usuario.setSenha(hashservice.getHashSenha(senha.senhaAtual()));
//     repository.persist(usuario);
//     return "Senha alterada com sucesso.";
//     } else {
//      throw new ValidationException("updateSenha", "Favor inserir a senha antiga correta."); 
//     }
//   }

//   @Override
//   @Transactional
//   public UsuarioResponseDTO insertTelefoneUsuario(TelefoneDTO tel, Long id) {
//     Usuario usuario = repository.findById(id);
    
//     Telefone telefone = new Telefone();
//     telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
//     telefone.setDdd(tel.ddd());
//     telefone.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
//     usuario.getListaTelefone().add(telefone);
//     repository.persist(usuario);
//     return UsuarioResponseDTO.valueOf(usuario);
//   }


//   @Override
//   @Transactional
//   public UsuarioResponseDTO updateTelefoneUsuario(TelefonePatchDTO tel,Long id) {
//     Usuario usuario = repository.findById(id);

//     if (
//       usuario.getListaTelefone() != null ||
//       !usuario.getListaTelefone().isEmpty()
//     ) {

//       Boolean chave = true;
      
//       for (Telefone tele1 : usuario.getListaTelefone()) {
//           if (tele1.getId() == tel.id()) {
//             tele1.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
//             tele1.setDdd(tel.ddd());
//             tele1.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
//             chave = false;
//           }   
//       }
//       if(chave) {
//         throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(updateTelefoneUsuario)", "O id fornecido não corresponde a um id de telefone cadastrado para este usuario");
//       }
//     } else {
//       throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(updateTelefoneUsuario)", "Este usuário não possui nenhum telefone cadastrado.");
//     }

//     //repository.persist(cliente);
//     return UsuarioResponseDTO.valueOf(usuario);
//   }

//   @Override
//   @Transactional
//   public UsuarioResponseDTO updateEnderecoUsuario(EnderecoFuncPatchDTO end,Long id) {
//     Usuario usuario = repository.findById(id);

//     if(usuario.getEndereco()!= null) {
//       if(usuario.getEndereco().getId() == end.id()) {
//         usuario.getEndereco().setLogradouro(end.logradouro());
//         usuario.getEndereco().setNumeroLote(end.numeroLote());
//         usuario.getEndereco().setBairro(end.bairro());
//         usuario.getEndereco().setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
//         usuario.getEndereco().setComplemento(end.complemento());
//         usuario.getEndereco().setLocalidade(end.localidade());
//         usuario.getEndereco().setUF(end.uf());
//         usuario.getEndereco().setPais(end.pais());
//         } else {
//           throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(updateTelefoneUsuario)", "O id fornecido não corresponde a um id de endereço cadastrado para este usuario");
//         }
//     } else {
//       throw new GeneralErrorException("400", "Bad Request", "UsuarioServiceImpl(updateEnderecoUsuario)", "Este usuário não possui nenhum endereço cadastrado.");
//     }
//     //repository.persist(cliente);
//     return UsuarioResponseDTO.valueOf(usuario);
//   }


//   @Override
//   @Transactional
//   public UsuarioResponseDTO insertEnderecoUsuario(EnderecoFuncDTO end,Long id) {
//     Usuario usuario = repository.findById(id);
//     Endereco endereco = new Endereco();

//     endereco.setLogradouro(end.logradouro());
//     endereco.setNumeroLote(end.numeroLote());
//     endereco.setBairro(end.bairro());
//     endereco.setComplemento(end.complemento());
//     endereco.setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
//     endereco.setLocalidade(end.localidade());
//     endereco.setUF(end.uf());
//     endereco.setPais(end.pais());
//     repositoryEndereco.persist(endereco);
//     usuario.setEndereco(endereco);
//     repository.persist(usuario);
//     return UsuarioResponseDTO.valueOf(usuario);
//   }

//   public UsuarioDTO insertUsuario(UsuarioDTO dto) {
//     Usuario usuario = new Usuario();
//     usuario.setNome(dto.nome());
//     usuario.setDataNascimento(
//       UsuarioFormatador.validaDataNascimento(dto.dataNascimento())
//     );
//     usuario.setCpf(UsuarioFormatador.validaCpf(dto.cpf()));
//     usuario.setSexo(dto.sexo());
//     usuario.setLogin(dto.login());
//     usuario.setSenha(hashservice.getHashSenha(dto.senha()));
//     usuario.setEmail(dto.email());
//     usuario.setPerfil(Perfil.valueOf(dto.idperfil()));
//     usuario.setListaTelefone(new ArrayList<Telefone>());

//     if (dto.listaTelefone() != null && !dto.listaTelefone().isEmpty()) {
//       usuario.setListaTelefone(new ArrayList<Telefone>());
//       for (TelefoneDTO tel : dto.listaTelefone()) {
//         Telefone telefone = new Telefone();
//         telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
//         telefone.setDdd(tel.ddd());
//         telefone.setNumeroTelefone(
//           TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone())
//         );
//         usuario.getListaTelefone().add(telefone);
//       }
//     }

//     Endereco endereco = new Endereco();
//     endereco.setLogradouro(dto.endereco().logradouro());
//     endereco.setNumeroLote(dto.endereco().numeroLote());
//     endereco.setBairro(dto.endereco().bairro());
//     endereco.setComplemento(dto.endereco().complemento());
//     endereco.setCep(new CEP(EnderecoFormatador.validaCep(dto.endereco().cep().getCep())));
//     endereco.setLocalidade(dto.endereco().localidade());
//     endereco.setUF(dto.endereco().uf());
//     endereco.setPais(dto.endereco().pais());
//     repositoryEndereco.persist(endereco);

//     usuario.setEndereco(endereco);
//     repository.persist(usuario);
//     return UsuarioDTO.valueOf(usuario);
//   }

//   @Override
//   public UsuarioResponseDTO findByLoginAndSenha(
//     String login,
//     String senha
//   ) {
//     Usuario usuario = repository.findByLoginAndSenha(login, senha);
//     if (usuario == null) {
//       throw new ValidationException("Login", "Login e/ou Senha incorretos");
//     }
//     return UsuarioResponseDTO.valueOf(usuario);
//   }

//   @Override
//   public UsuarioResponseDTO findByLogin(String login) {
//     Usuario usuario = repository.findByLogin(login);
//     if (usuario == null) {
//       throw new ValidationException("Login", "Login incorreto");
//     }
//     return UsuarioResponseDTO.valueOf(usuario);
//   }

//   private void verificaLogin(String login) {
//     if(repository.findByLogin(login) != null) {
//       throw new ValidationException("login", "Login já existe no sistema. Favor escolher outro.");
//     }
//    }
  
//   private void verificaCpf(String cpf) {
//     if(repository.findByCpf(cpf) != null) {
//       throw new ValidationException("cpf", "Este cpf já existe no sistema. Usuário já está cadastrado no sistema.");
//     }
//   }
// }
