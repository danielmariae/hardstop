package br.unitins.topicos1;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.service.ClienteService;

import static io.restassured.RestAssured.given;

import br.unitins.topicos1.dto.ClienteResponseDTO;


@QuarkusTest
public class ClienteResourceTest {
    @Inject
    ClienteService clienteService;

    @Test
    public void testFindAll() {
        given()
          .when().get("/clientes")
          .then()
             .statusCode(200);
    }

    

}
