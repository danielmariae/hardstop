package br.unitins.topicos1;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.endereco.EnderecoDTO;
import br.unitins.topicos1.dto.fornecedor.FornecedorDTO;
import br.unitins.topicos1.dto.login.LoginDTO;
import br.unitins.topicos1.dto.telefone.TelefoneDTO;
import br.unitins.topicos1.resource.AuthResource;
import br.unitins.topicos1.service.HashService;
import br.unitins.topicos1.service.JwtService;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.jrimum.domkee.pessoa.UnidadeFederativa;

import br.unitins.topicos1.dto.funcionario.FuncionarioResponseDTO;
import br.unitins.topicos1.service.FuncionarioService;


@QuarkusTest
public class FornecedorResourceTest{

    @Inject
    JwtService jwtService;

    @Inject 
    HashService hashService;

    @Inject 
    FuncionarioService funcionarioService;
    
       @Test
       public void testFornecedorDTO() {

       List<EnderecoDTO> listaEndereco = new ArrayList<>();
       listaEndereco.add(new EnderecoDTO("Endereço 1", "605 Sul Alameda 24 QI 14", "16", "Plano Diretor Sul", "Casa de Esquina na Rua Principal", "77016-446", "Palmas", UnidadeFederativa.TO, "Brasil"));
       List<TelefoneDTO> listaTelefone = new ArrayList<>();
       listaTelefone.add(new TelefoneDTO(1, "63", "99963-2459"));

       FornecedorDTO fornecedorDTO = new FornecedorDTO(
               "Empresa ABC",
               "12.345.678/0001-90",
               "http://www.empresaabc.com.br",
               listaEndereco,  // Lista de endereços (pode ser nula para este exemplo)
               listaTelefone   // Lista de telefones (pode ser nula para este exemplo)
       );
    
       LoginDTO login = new LoginDTO("martacesare", "GfT12-");
       String hash = hashService.getHashSenha(login.senha());
       FuncionarioResponseDTO funcionario = funcionarioService.findByLoginAndSenha(login.login(), hash);
       
       String tokenAdm = jwtService.generateJwt(funcionario);
     
       given()
       .headers("Authorization", "Bearer " + tokenAdm)
       .contentType(ContentType.JSON)
       .body(fornecedorDTO)
       .when()
       .post("/fornecedores")  // Substitua pelo seu endpoint real
       .then()
           .statusCode(201)
           .body("nomeFantasia", equalTo("Empresa ABC"))
           .body("cnpj", equalTo("12345678000190"))
           .body("endSite", equalTo("http://www.empresaabc.com.br"));
   }

}