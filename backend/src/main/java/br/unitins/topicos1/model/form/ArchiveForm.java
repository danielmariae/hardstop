package br.unitins.topicos1.model.form;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import jakarta.ws.rs.FormParam;

public class ArchiveForm {
    
    @FormParam("nomeImagem")
    private String nomeArquivo;

    @FormParam("imagem")
    @PartType("application/octet-stream")
    private byte[] arquivo;

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

}
