package br.unitins.topicos1.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.unitins.topicos1.application.ErrorTP1;

// import java.util.List;

// import br.unitins.topicos1.dto.ClassificacaoDTO;
// import jakarta.validation.Valid;
// import jakarta.ws.rs.PATCH;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.model.form.ArchiveForm;
import br.unitins.topicos1.service.FileService;
import br.unitins.topicos1.service.ProdutoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
    public Response insert (@Valid ProdutoDTO dto){
        ProdutoResponseDTO retorno = service.insert(dto);
        return Response.status(201).entity(retorno).build();
    }

    @PUT
    @RolesAllowed({"Func", "Admin"})
    @Path("/put/{id}")
    public Response update(@Valid ProdutoDTO dto, @PathParam("id") Long id)
    {
        service.update(dto, id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @PATCH
    @RolesAllowed({"Func", "Admin"})
    @Path("/patch/lote/{id}")
    public Response updateLoteProduto(@PathParam("id") Long idProduto, Long idLote) {
        return Response.ok().entity(service.updateLote(idProduto, idLote)).build();
    }

    @PATCH
    @RolesAllowed({"Func", "Admin"})
    @Path("/patch/quantidade/{id}")
    public Response updateQuantidadeProduto(@PathParam("id") Long idProduto, Integer quantidade) {
        return Response.ok().entity(service.updateQuantidade(quantidade, idProduto)).build();
    }

    @PATCH
    @RolesAllowed({"Func", "Admin"})
    @Path("/patch/valorVenda/{id}")
    public Response updateValorVendaProduto(@PathParam("id") Long idProduto, Double valorVenda) {
        return Response.ok().entity(service.updateValorVenda(valorVenda, idProduto)).build();
    }

    @PATCH
    @RolesAllowed({"Func", "Admin"})
    @Path("/patch/custoCompra/{id}")
    public Response updateCustoCompraProduto(@PathParam("id") Long idProduto, Double custoCompra) {
        return Response.ok().entity(service.updateCustoCompra(custoCompra, idProduto)).build();
    }

    @DELETE
    @RolesAllowed({"Func", "Admin"})
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    public Response findAll() {
        return Response.ok(service.findByAll()).build();
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
