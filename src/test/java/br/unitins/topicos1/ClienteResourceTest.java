package br.unitins.topicos1;

import org.junit.jupiter.api.Test;

import com.google.inject.Inject;

import br.unitins.topicos1.service.ClienteService;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;

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
