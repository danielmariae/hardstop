package br.unitins.topicos1.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.unitins.topicos1.application.ErrorTP1;
import br.unitins.topicos1.dto.fornecedor.FornecedorResponseDTO;
import br.unitins.topicos1.dto.produto.ProdutoDTO;
import br.unitins.topicos1.dto.produto.ProdutoFornecedorPatch;
import br.unitins.topicos1.dto.produto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.produto.placaMae.PlacaMaeDTO;
import br.unitins.topicos1.dto.produto.placaMae.PlacaMaeResponseDTO;
import br.unitins.topicos1.dto.produto.processador.ProcessadorDTO;
import br.unitins.topicos1.dto.produto.processador.ProcessadorResponseDTO;
import br.unitins.topicos1.model.form.ArchiveForm;
import br.unitins.topicos1.model.produto.PlacaMae;
import br.unitins.topicos1.model.produto.Processador;
import br.unitins.topicos1.model.produto.Produto;
import br.unitins.topicos1.service.FileService;
import br.unitins.topicos1.service.ProdutoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {
    
    @Inject
    ProdutoService service;

    @Inject
    FileService fileService;

    @Inject
    JsonWebToken jwt;
    
    @POST
    @RolesAllowed({"Func", "Admin"})
    @Path("/insert/produto")
    public Response insert (@Valid ProdutoDTO dto){
        ProdutoResponseDTO retorno = service.insert(dto);
        return Response.status(201).entity(retorno).build();
    }

    @POST
    @RolesAllowed({"Func", "Admin"})
    @Path("/insert/processador")
    public Response insert (@Valid ProcessadorDTO dto){
        ProcessadorResponseDTO retorno = service.insertProcessador(dto);
        return Response.status(201).entity(retorno).build();
    }

    @POST
    @RolesAllowed({"Func", "Admin"})
    @Path("/insert/placaMae")
    public Response insert (@Valid PlacaMaeDTO dto){
        PlacaMaeResponseDTO retorno = service.insertPlacaMae(dto);
        return Response.status(201).entity(retorno).build();
    }

    @PUT
    @RolesAllowed({"Func", "Admin"})
    @Path("/update/produto/{id}")
    public Response update(@Valid ProdutoDTO dto, @PathParam("id") Long id)
    {
        service.update(dto, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @RolesAllowed({"Func", "Admin"})
    @Path("/update/processador/{id}")
    public Response update(@Valid ProcessadorDTO dto, @PathParam("id") Long id)
    {
        service.updateProcessador(dto, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @RolesAllowed({"Func", "Admin"})
    @Path("/update/placaMae/{id}")
    public Response update(@Valid PlacaMaeDTO dto, @PathParam("id") Long id)
    {
        service.updatePlacaMae(dto, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @RolesAllowed({"Func", "Admin"})
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    // @RolesAllowed({"Func", "Admin"})
    public Response findAll(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("pageSize") @DefaultValue("100") int pageSize) {

        return Response.ok(service.findByAll(page, pageSize)).build();
    }

    @GET
    @Path("/count")
    public Long count() {
        return service.count();
    }

    @GET
    // @RolesAllowed({"Func", "Admin"})
    public Response findTodos() {
        return Response.ok(service.findTodos()).build();
    }

    @GET
    @Path("/search/id/{id}")
    public Response findById(@PathParam("id") Long id) {
        Produto produto = service.findById(id);
        if(produto instanceof PlacaMae) {
            PlacaMae placaMae = (PlacaMae) produto;
            return Response.ok(PlacaMaeResponseDTO.valueOf(placaMae)).build();
        } else if(produto instanceof Processador) {
            Processador processador = (Processador) produto;
            return Response.ok(ProcessadorResponseDTO.valueOf(processador)).build();
        } else {
            return Response.ok(ProdutoResponseDTO.valueOf(produto)).build();
        }
    }

    @GET
    @Path("/search/codigobarra/{codigobarra}")
    public Response findById(@PathParam("codigobarra") String codigoBarra) {
        return Response.ok(service.findByCodigoBarras(codigoBarra)).build();
    }


// @GET
// @Path("/search/nome/{nome}")
// public Response findByName(
//     @PathParam("nome") String nome,
//     @QueryParam("page") @DefaultValue("0") int page,
//     @QueryParam("pageSize") @DefaultValue("100") int pageSize
// ) {
//     List<ProdutoResponseDTO> produtos = service.findByName(nome, page, pageSize);
//     long totalItems = service.countByName(nome); // Obtém o total de itens disponíveis
//     // Crie um objeto que contenha tanto os itens quanto o total de itens

//     Map<String, Object> responseMap = new HashMap<>();
//     responseMap.put("produtos", produtos);
//     responseMap.put("totalItems", totalItems);

//     return Response.ok(responseMap).build();
// }

@GET
@Path("/search/nome/{nome}")
public Response findByName(
    @PathParam("nome") String nome,
    @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("pageSize") @DefaultValue("100") int pageSize
) {
    List<ProdutoResponseDTO> produtos = service.findByName(nome, page, pageSize);
    long totalItems = service.countByName(nome); // Obtém o total de itens disponíveis
    // Crie um objeto que contenha tanto os itens quanto o total de itens

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("produtos",produtos);
    responseMap.put("totalItems", totalItems);

    return Response.ok(responseMap).build();
}
    @GET
    @Path("/search/nome/{idClassificacao}")
    public Response findByClassificacao(@PathParam("idClassificacao") Long id) {
        return Response.ok(service.findByClassificacao(id)).build();
    }

    @GET
    @Path("/classificacao")
    public Response retornaClassificacao() {
        return Response.ok(service.retornaClassificacao()).build();
    }


    @GET
    @Path("/search/fornecedor")
    @RolesAllowed({"Func", "Admin"})
    public Response fornecedor(
        @QueryParam("idProduto") Long idProduto,
        @QueryParam("dataHoraVenda") String dataHoraVenda
    ) {
        ProdutoFornecedorPatch dto = new ProdutoFornecedorPatch(idProduto, dataHoraVenda);
         FornecedorResponseDTO retorno = service.encontraFornecedor(dto);
        return Response.status(201).entity(retorno).build();
    }

    @POST
    @Path("/upload/imagem/id/{id}")
    //@RolesAllowed({"Func", "Admin"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(@PathParam("id") Long id, @MultipartForm ArchiveForm form) {
    // public Response salvarImagem(@MultipartForm ArchiveForm form) {
        String nomeImagem;
        try {
           nomeImagem = fileService.salvarP(form.getNomeArquivo(), form.getArquivo());
        } catch (Exception e) {
            ErrorTP1 error = new ErrorTP1("409", e.getMessage());
            return Response.status(Status.CONFLICT).entity(error).build();
        }

        return Response.ok(fileService.updateNomeImagemP(id, nomeImagem)).build();

    }

    @GET
    @Path("/download/imagem/{nomeImagem}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem){

        return Response
                .ok(fileService.obterP(nomeImagem), MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=" + nomeImagem)
                .build();
    }
}   
