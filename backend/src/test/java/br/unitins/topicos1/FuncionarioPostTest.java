// package br.unitins.topicos1;

// import static io.restassured.RestAssured.given;
// import static org.hamcrest.CoreMatchers.equalTo;

// import java.util.ArrayList;
// import java.util.List;

// import org.jrimum.domkee.pessoa.UnidadeFederativa;
// import org.junit.jupiter.api.Test;

// import br.unitins.topicos1.dto.funcionario.EnderecoFuncDTO;
// import br.unitins.topicos1.dto.funcionario.FuncionarioDTO;
// import br.unitins.topicos1.dto.funcionario.FuncionarioResponseDTO;
// import br.unitins.topicos1.dto.telefone.TelefoneDTO;
// import br.unitins.topicos1.service.FuncionarioService;
// import br.unitins.topicos1.service.HashService;
// import br.unitins.topicos1.service.JwtService;
// import io.quarkus.test.junit.QuarkusTest;
// import io.restassured.http.ContentType;
// import jakarta.inject.Inject;

// @QuarkusTest
// public class FuncionarioPostTest{
//     @Inject
//     FuncionarioService funcionarioService;
    
//     @Inject 
//     JwtService jwtService;

//     @Inject
//     HashService hashService;



//         @Test
//         public void testFuncionarioDTO() {
//         List<TelefoneDTO> telefones = new ArrayList<>();
//         telefones.add(new TelefoneDTO(1, "63", "99963-2459"));

//         FuncionarioDTO funcionarioDTO = new FuncionarioDTO(
//             "João Silva",
//             "1990-01-01",
//             "123.456.789-09",
//             "M",
//             "joao123",
//             "Senha@123",
//             "joao@example.com",
//             new EnderecoFuncDTO("ARSE 51 Alameda 10", "34", "Plano Diretor Sul",
//              "Comunidade Shalom", "77021-686", "Palmas", UnidadeFederativa.TO, "Brasil"),
//             1,
//             telefones 
//         );
            
//         String senha = hashService.getHashSenha("GfT12-");
//         FuncionarioResponseDTO admin = funcionarioService.findByLoginAndSenha("martacesare", senha);

//         String jwt = jwtService.generateJwt(admin);

//         // Fazer a requisição POST para um endpoint fictício (substitua pelo endpoint real)
//         given()
//             .headers("Authorization", "Bearer " + jwt)
//             .contentType(ContentType.JSON)
//             .body(funcionarioDTO)
//         .when()
//             .post("/funcionario")  // Substitua pelo seu endpoint real
//         .then()
//             .statusCode(201)
//             .body("nome", equalTo("João Silva"))
//             .body("dataNascimento", equalTo("1990-01-01"))
//             .body("cpf", equalTo("12345678909"))
//             .body("sexo", equalTo("M"))
//             .body("login", equalTo("joao123"))
//             .body("email", equalTo("joao@example.com"));
//         // Adicione verificações para outras propriedades conforme necessário
//     }
// }