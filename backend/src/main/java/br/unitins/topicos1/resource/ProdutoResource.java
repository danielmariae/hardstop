package br.unitins.topicos1.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.unitins.topicos1.application.ErrorTP1;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoFornecedorPatch;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.model.form.ArchiveForm;
import br.unitins.topicos1.service.FileService;
import br.unitins.topicos1.service.ProdutoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
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
    @Path("/insert/")
    public Response insert (@Valid ProdutoDTO dto){
        ProdutoResponseDTO retorno = service.insert(dto);
        return Response.status(201).entity(retorno).build();
    }

    @DELETE
    @RolesAllowed({"Func", "Admin"})
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @RolesAllowed({"Func", "Admin"})
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
    @RolesAllowed({"Func", "Admin"})
    public Response findTodos() {
        return Response.ok(service.findTodos()).build();
    }

    @GET
    @Path("/search/id/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }

    @GET
    @Path("/search/codigobarra/{codigobarra}")
    public Response findById(@PathParam("codigobarra") String codigoBarra) {
        return Response.ok(service.findByCodigoBarras(codigoBarra)).build();
    }


    @GET
    @Path("/search/nome/{nome}")
    public Response findByName(@PathParam("nome") String nome) {
        return Response.ok(service.findByName(nome)).build();
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


    @POST
    @Path("/search/fornecedor")
    @RolesAllowed({"Func", "Admin"})
    public Response fornecedor(ProdutoFornecedorPatch dto) {
        return Response.ok(service.encontraFornecedor(dto)).build();
    }

    @PATCH
    @Path("/upload/imagem/id/{id}")
    @RolesAllowed({"Func", "Admin"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(@PathParam("id") Long id, @MultipartForm ArchiveForm form) {
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
    @RolesAllowed({"Func", "Admin"})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem){

        return Response
                .ok(fileService.obterP(nomeImagem), MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=" + nomeImagem)
                .build();
    }
}   
