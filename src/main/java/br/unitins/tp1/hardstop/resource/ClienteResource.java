package br.unitins.tp1.hardstop.resource;
import java.util.List;

import br.unitins.tp1.hardstop.dto.ClienteDTO;
import br.unitins.tp1.hardstop.dto.ClienteResponseDTO;
import br.unitins.tp1.hardstop.model.Cliente;
import br.unitins.tp1.hardstop.service.ClienteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;



@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    ClienteService service;

    @POST
    public ClienteResponseDTO insert(ClienteDTO dto) {
        return service.insert(dto);
    }

    @PUT
    @Path("/{id}")
    public ClienteResponseDTO update(ClienteDTO dto, @PathParam("id") Long id) {
        return service.update(dto, id);
    }
    
    @DELETE
    @Path("/id")
    public void delete(ClienteResponseDTO dto, @PathParam("id") Long id){
        service.delete(id);
    }

    @GET
    @Path("/listarTodos")
    public List<ClienteResponseDTO> findAll() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    public Cliente findById(@PathParam("id") Long id) {
        return service.findById(id);
    }

    @GET
    @Path("/search/nome/{nome}")
    public List<ClienteResponseDTO> findByNome(@PathParam("nome") String nome) {
        return service.findByNome(nome);
    }

}