package br.unitins.topicos1;

import br.unitins.topicos1.dto.UsuarioDTO;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.jrimum.domkee.pessoa.CEP;
import org.jrimum.domkee.pessoa.UnidadeFederativa;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;


@QuarkusTest
public class ClienteResourceTest {
    @Inject
    UsuarioService clienteService;

    @Test
    public void testFindAll() {
        given()
          .when().get("/clientes")
          .then()
             .statusCode(200);
    }

    @Test
    public void testInsertCliente(){
        List<EnderecoDTO> enderecos = new ArrayList<>();
        enderecos.add(new EnderecoDTO("Endereço 1", "605 Sul Alameda 24 QI 14", "s/n", "Lote 16", "Plano Diretor Sul", "Casa de Esquina na Rua Principal", new CEP("77016-446"), "Palmas", UnidadeFederativa.TO, "Brasil"));
        List<TelefoneDTO> telefones = new ArrayList<>();
        telefones.add(new TelefoneDTO(1, "63", "99963-2459"));

        
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                "João Silva",
                "1990-01-01",
                "123.456.789-09",
                "M",
                "joao123",
                "Senha@123",
                "joao@example.com",
                enderecos,
                telefones  
        );

        // Fazer a requisição POST para um endpoint fictício (substitua pelo endpoint real)
        given()
            .contentType(ContentType.JSON)
            .body(usuarioDTO)
        .when()
            .post("/clientes")
        .then()
            .statusCode(201)
            .body("nome", equalTo("João Silva"))
            .body("dataNascimento", equalTo("1990-01-01"))
            .body("cpf", equalTo("12345678909"))
            .body("sexo", equalTo("M"))
            .body("login", equalTo("joao123"))
            .body("email", equalTo("joao@example.com"));
    }
}
