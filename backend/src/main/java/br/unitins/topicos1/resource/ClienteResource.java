package br.unitins.topicos1.resource;


import br.unitins.topicos1.dto.ClienteDTO;
import br.unitins.topicos1.service.ClienteService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

  @Inject
  ClienteService service;

  @POST
  @Transactional
  public Response insertCliente(@Valid ClienteDTO dto) {
    return Response.status(201).entity(service.insertCliente(dto)).build();
  }

}
