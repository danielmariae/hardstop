package br.unitins.topicos1.service;

import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.*;
import br.unitins.topicos1.model.*;
import br.unitins.topicos1.repository.LogisticaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jrimum.domkee.pessoa.CEP;

import java.util.*;

@ApplicationScoped
public class LogisticaServiceImpl implements LogisticaService {

    @Inject
    LogisticaRepository repository;
    @Override
    public LogisticaResponseDTO insert(LogisticaDTO dto) {
        Logistica logistica = new Logistica();
        logistica.setNomeFantasia(dto.nomeFantasia());
        logistica.setEndSite(dto.endSite());
        if (dto.listaEndereco() != null && !dto.listaEndereco().isEmpty()) {
            try {
                logistica.setListaEndereco(new ArrayList<Endereco>());
            } catch (Exception e) {
                throw new GeneralErrorException("500", "Internal Server Error", "LogisticaServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços do novo Logistica. Tente novamente mais tarde! " +  e.getCause());
            }

            for (EnderecoDTO end : dto.listaEndereco()) {
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
                logistica.getListaEndereco().add(endereco);
            }
           }
           if (dto.listaTelefone() != null && !dto.listaTelefone().isEmpty()) {
               try {
                   logistica.setListaTelefone(new ArrayList<Telefone>());
               } catch (Exception e) {
                   throw new GeneralErrorException("500", "Internal Server Error", "ClienteServiceImpl(insert)", "Não consegui alocar memória para a lista telefônica do novo Cliente. Tente novamente mais tarde! " +  e.getCause());
               }
               for (TelefoneDTO tel : dto.listaTelefone()) {
                   Telefone telefone = new Telefone();
                   telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
                   telefone.setDdd(tel.ddd());
                   telefone.setNumeroTelefone(
                           TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
                   logistica.getListaTelefone().add(telefone);
               }
           }
           try{
               repository.persist(logistica);
           } catch (Exception e) {
               throw new GeneralErrorException("500", "Internal Server Error", "LogisticaServiceImpl(insert)", "Não consegui persistir os dados do cliente no repositório " + e.getCause());
           }
        return LogisticaResponseDTO.valueOf(logistica);
    }


    @Override
    public LogisticaResponseDTO update(LogisticaDTO dto, Long id) {
            Logistica logistica = new Logistica();
            logistica.setNomeFantasia(dto.nomeFantasia());
            logistica.setEndSite(dto.endSite());
            int i = 0;
            int j = 0;

            for (Telefone tele1 : logistica.getListaTelefone()) {
                i++;
                j = 0;
                for (TelefoneDTO tele : dto.listaTelefone()) {
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

            for (Endereco endereco : logistica.getListaEndereco()) {
                ie++;
                je = 0;
                for (EnderecoDTO end1 : dto.listaEndereco()) {
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
    
        repository.persist(logistica);
        return LogisticaResponseDTO.valueOf(logistica);
    }
    //    @Override
    //    public void delete(Long id) {
    @Override
    public LogisticaResponseDTO findById(Long id) {
        return LogisticaResponseDTO.valueOf(repository.findById(id));
    }

    @Override
    public List<LogisticaResponseDTO> findByAll() {
        return repository
                .listAll()
                .stream()
                .map(LogisticaResponseDTO::valueOf)
                .toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
