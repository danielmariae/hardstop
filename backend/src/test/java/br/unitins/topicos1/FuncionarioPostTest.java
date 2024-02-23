package br.unitins.topicos1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.EnderecoFuncDTO;
import br.unitins.topicos1.dto.FuncionarioDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.service.FuncionarioService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

@QuarkusTest
public class FuncionarioPostTest{
    @Inject
    FuncionarioService funcionarioService;

        @Test
        public void testFuncionarioDTO() {
        List<TelefoneDTO> telefones = new ArrayList<>();
        telefones.add(new TelefoneDTO(1, "63", "99963-2459"));

        FuncionarioDTO funcionarioDTO = new FuncionarioDTO(
            "João Silva",
            "1990-01-01",
            "123.456.789-09",
            "M",
            "joao123",
            "Senha@123",
            "joao@example.com",
            null,
            null,
            telefones 
        );
            
        String tokenAdm = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJoYXJkc3RvcC1qd3QiLCJzdWIiOiJtYXJ0YWNlc2FyZSIsImdyb3VwcyI6WyJBZG1pbiJdLCJleHAiOjE3MDE1NDI3NTEsImlhdCI6MTcwMTQ1NjM1MSwianRpIjoiZTBjMTAwZjctMGVhOS00MzAzLWE4YWEtNTIzZjE1ZDBjYmFjIn0.GzZw4ud9dDxdJMU0fWVp5BNyhCVqukRK7YvZxu1Wf8bt2R3XLZQRaGYlSPbbjFgDeJuyV001OlUitsm09rECExrhRw5tCi9lT-XmHYxA7AH96r_QvVfGDs29CUDBvC1756BtIdtaoP3sWlBVGvzzPKGMnHcR7CKNMgwuVnHGtrTX0HZBUD3w8XqFqk_2SXvmCpVbqEljHYjwfItkavuTRHUrSfXK4_femzRIhwusME-knG0BTCKvuQoaMDxes-pWa3hQf6ZpJLpTmiaJnSGp5CyMcMwVN09DPG2gL9G_vcJ-3H6YsFu5iAhFNR2nqO79PTqBqeDwRIO78CIyP22s2A";


        // Fazer a requisição POST para um endpoint fictício (substitua pelo endpoint real)
        given()
            .headers("Authorization", "Bearer " + tokenAdm)
            .contentType(ContentType.JSON)
            .body(funcionarioDTO)
        .when()
            .post("/funcionariologado/insert/funcionario")  // Substitua pelo seu endpoint real
        .then()
            .statusCode(200)
            .body("nome", equalTo("João Silva"))
            .body("dataNascimento", equalTo("1990-01-01"))
            .body("cpf", equalTo("123.456.789-09"))
            .body("sexo", equalTo("M"))
            .body("login", equalTo("joao123"))
            .body("email", equalTo("joao@example.com"));
            // Adicione verificações para outras propriedades conforme necessário
    }


}