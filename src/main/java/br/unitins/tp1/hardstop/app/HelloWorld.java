package br.unitins.tp1.hardstop.app;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloWorld { 
  
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello(){
        return "Olá, mundo!";
    }
}
