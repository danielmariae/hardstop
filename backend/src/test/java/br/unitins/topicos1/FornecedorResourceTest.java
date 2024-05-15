// package br.unitins.topicos1;

// import io.quarkus.test.junit.QuarkusTest;
// import io.restassured.http.ContentType;
// import jakarta.inject.Inject;

// import org.junit.jupiter.api.Test;

// import br.unitins.topicos1.dto.EnderecoDTO;
// import br.unitins.topicos1.dto.FornecedorDTO;
// import br.unitins.topicos1.dto.TelefoneDTO;
// import br.unitins.topicos1.service.FornecedorService;

// import static io.restassured.RestAssured.given;
// import static org.hamcrest.Matchers.equalTo;

// import java.util.ArrayList;
// import java.util.List;

// import org.jrimum.domkee.pessoa.UnidadeFederativa;

// @QuarkusTest
// public class FornecedorResourceTest{
//     @Inject
//     FornecedorService fornecedorService;
//        @Test
//        public void testFornecedorDTO() {

//        List<EnderecoDTO> enderecos = new ArrayList<>();
//        enderecos.add(new EnderecoDTO("Endereço 1", "605 Sul Alameda 24 QI 14", "Lote 16", "Plano Diretor Sul", "Casa de Esquina na Rua Principal", "77016-446", "Palmas", UnidadeFederativa.TO, "Brasil"));
//        List<TelefoneDTO> telefones = new ArrayList<>();
//        telefones.add(new TelefoneDTO(1, "63", "99963-2459"));

//        FornecedorDTO fornecedorDTO = new FornecedorDTO(
//                "Empresa ABC",
//                "12.345.678/0001-90",
//                "http://www.empresaabc.com.br",
//                enderecos,  // Lista de endereços (pode ser nula para este exemplo)
//                telefones   // Lista de telefones (pode ser nula para este exemplo)
//        );

//        String tokenAdm = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJoYXJkc3RvcC1qd3QiLCJzdWIiOiJtYXJ0YWNlc2FyZSIsImdyb3VwcyI6WyJBZG1pbiJdLCJleHAiOjE3MDE1NDI3NTEsImlhdCI6MTcwMTQ1NjM1MSwianRpIjoiZTBjMTAwZjctMGVhOS00MzAzLWE4YWEtNTIzZjE1ZDBjYmFjIn0.GzZw4ud9dDxdJMU0fWVp5BNyhCVqukRK7YvZxu1Wf8bt2R3XLZQRaGYlSPbbjFgDeJuyV001OlUitsm09rECExrhRw5tCi9lT-XmHYxA7AH96r_QvVfGDs29CUDBvC1756BtIdtaoP3sWlBVGvzzPKGMnHcR7CKNMgwuVnHGtrTX0HZBUD3w8XqFqk_2SXvmCpVbqEljHYjwfItkavuTRHUrSfXK4_femzRIhwusME-knG0BTCKvuQoaMDxes-pWa3hQf6ZpJLpTmiaJnSGp5CyMcMwVN09DPG2gL9G_vcJ-3H6YsFu5iAhFNR2nqO79PTqBqeDwRIO78CIyP22s2A";

//        given()
//        .headers("Authorization", "Bearer " + tokenAdm)
//        .contentType(ContentType.JSON)
//        .body(fornecedorDTO)
//        .when()
//        .post("/fornecedores")  // Substitua pelo seu endpoint real
//        .then()
//            .statusCode(201)
//            .body("nomeFantasia", equalTo("Empresa ABC"))
//            .body("cnpj", equalTo("12.345.678/0001-90"))
//            .body("endSite", equalTo("http://www.empresaabc.com.br"));
//    }

// }