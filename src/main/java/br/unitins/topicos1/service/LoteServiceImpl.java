package br.unitins.topicos1.service;

import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.*;
import br.unitins.topicos1.model.*;
import br.unitins.topicos1.repository.LoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jrimum.domkee.pessoa.CEP;

import java.util.*;

@ApplicationScoped
public class LoteServiceImpl implements LoteService {

    @Inject
    LoteRepository repository;
    @Override
    public LoteResponseDTO insert(LoteDTO dto) {
        Lote lote = new Lote();
        lote.setLote(dto.lote());
        if(dto.fornecedor() != null) {
           Fornecedor fornecedor = new Fornecedor();
           fornecedor.setNomeFantasia(dto.fornecedor().nomeFantasia());
           fornecedor.setCnpj(dto.fornecedor().cnpj());
           fornecedor.setEndSite(dto.fornecedor().endSite());
           if (dto.fornecedor().listaEndereco() != null && !dto.fornecedor().listaEndereco().isEmpty()) {
                try {
                    fornecedor.setListaEndereco(new ArrayList<Endereco>());
                } catch (Exception e) {
                    throw new GeneralErrorException("500", "Internal Server Error", "LoteServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços do novo Fornecedor. Tente novamente mais tarde! " +  e.getCause());
                }

                for (EnderecoDTO end : dto.fornecedor().listaEndereco()) {
                    Endereco endereco = new Endereco();
                    endereco.setNome(end.nome());
                    endereco.setLogradouro(end.logradouro());
                    endereco.setNumero(end.numero());
                    endereco.setLote(end.lote());
                    endereco.setBairro(end.bairro());
                    endereco.setComplemento(end.complemento());
                    endereco.setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
                    endereco.setLocalidade(end.localidade());
                    endereco.setUF(end.uf());
                    endereco.setPais(end.pais());
                    fornecedor.getListaEndereco().add(endereco);
                }
           }
           if (dto.fornecedor().listaTelefone() != null && !dto.fornecedor().listaTelefone().isEmpty()) {
               try {
                   fornecedor.setListaTelefone(new ArrayList<Telefone>());
               } catch (Exception e) {
                   throw new GeneralErrorException("500", "Internal Server Error", "ClienteServiceImpl(insert)", "Não consegui alocar memória para a lista telefônica do novo Cliente. Tente novamente mais tarde! " +  e.getCause());
               }
               for (TelefoneDTO tel : dto.fornecedor().listaTelefone()) {
                   Telefone telefone = new Telefone();
                   telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
                   telefone.setDdd(tel.ddd());
                   telefone.setNumeroTelefone(
                           TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
                   fornecedor.getListaTelefone().add(telefone);
               }
           }
           try{
               repository.persist(lote);
           } catch (Exception e) {
               throw new GeneralErrorException("500", "Internal Server Error", "LoteServiceImpl(insert)", "Não consegui persistir os dados do cliente no repositório " + e.getCause());
           }
        }
        return LoteResponseDTO.valueOf(lote);
    }


    @Override
    public LoteResponseDTO update(LoteDTO dto, Long id) {
        Lote lote = repository.findById(id);
        lote.setLote(dto.lote());
        if(dto.fornecedor() != null) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNomeFantasia(dto.fornecedor().nomeFantasia());
            fornecedor.setCnpj(dto.fornecedor().cnpj());
            fornecedor.setEndSite(dto.fornecedor().endSite());
            int i = 0;
            int j = 0;

            for (Telefone tele1 : lote.getFornecedor().getListaTelefone()) {
                i++;
                j = 0;
                for (TelefoneDTO tele : dto.fornecedor().listaTelefone()) {
                    j++;
                    if (i == j) {
                        tele1.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
                        tele1.setDdd(tele.ddd());
                        tele1.setNumeroTelefone(
                                TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone())
                        );
                    }
                }
            }

            int ie = 0;
            int je = 0;

            for (Endereco endereco : lote.getFornecedor().getListaEndereco()) {
                ie++;
                je = 0;
                for (EnderecoDTO end1 : dto.fornecedor().listaEndereco()) {
                    je++;
                    if (ie == je) {
                        endereco.setNome(end1.nome());
                        endereco.setLogradouro(end1.logradouro());
                        endereco.setNumero(end1.numero());
                        endereco.setLote(end1.lote());
                        endereco.setBairro(end1.bairro());
                        endereco.setComplemento(end1.complemento());
                        endereco.setCep(new CEP(EnderecoFormatador.validaCep(end1.cep().getCep())));
                        endereco.setLocalidade(end1.localidade());
                        endereco.setUF(end1.uf());
                        endereco.setPais(end1.pais());
                    }
                }
            }
            lote.setFornecedor(fornecedor);
        }
        repository.persist(lote);
        return LoteResponseDTO.valueOf(lote);
    }
    //    @Override
    //    public void delete(Long id) {
    @Override
    public LoteResponseDTO findById(Long id) {
        return LoteResponseDTO.valueOf(repository.findById(id));
    }

    @Override
    public List<LoteResponseDTO> findByName(String lote) {
        return repository
                .findByName(lote)
                .stream()
                .map(LoteResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<LoteResponseDTO> findByAll() {
        return repository
                .listAll()
                .stream()
                .map(LoteResponseDTO::valueOf)
                .toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
