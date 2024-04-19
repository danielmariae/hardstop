package br.unitins.topicos1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.LogisticaDTO;
import br.unitins.topicos1.dto.LogisticaResponseDTO;
import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Logistica;
import br.unitins.topicos1.model.Telefone;
import br.unitins.topicos1.model.TipoTelefone;
import br.unitins.topicos1.repository.LogisticaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class LogisticaServiceImpl implements LogisticaService {

    @Inject
    LogisticaRepository repository;

    @Override
    @Transactional
    public LogisticaResponseDTO insert(LogisticaDTO dto) {
        Logistica logistica = new Logistica();
        logistica.setNomeFantasia(dto.nomeFantasia());
        logistica.setEndSite(dto.endSite());
        logistica.setCnpj(dto.cnpj());

        if (dto.listaEndereco() != null && !dto.listaEndereco().isEmpty()) {
            try {
                logistica.setListaEndereco(new ArrayList<Endereco>());
            } catch (Exception e) {
                throw new GeneralErrorException("500", "Internal Server Error", "LogisticaServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços da nova empresa de Logistica. Tente novamente mais tarde! " +  e.getCause());
            }

            for (EnderecoDTO end : dto.listaEndereco()) {
                Endereco endereco = new Endereco();
                endereco.setNome(end.nome());
                endereco.setLogradouro(end.logradouro());
                endereco.setNumeroLote(end.numeroLote());
                endereco.setBairro(end.bairro());
                endereco.setComplemento(end.complemento());
                endereco.setCep(end.cep());
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
                   throw new GeneralErrorException("500", "Internal Server Error", "LogisticaServiceImpl(insert)", "Não consegui alocar memória para a lista telefônica da nova empresa de Logística. Tente novamente mais tarde! " +  e.getCause());
               }
               for (TelefoneDTO tel : dto.listaTelefone()) {
                   Telefone telefone = new Telefone();
                   telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
                   telefone.setDdd(tel.ddd());
                   telefone.setNumeroTelefone(
                    TelefoneFormatador.formataNumeroTelefone(tel.numeroTelefone()));
                   logistica.getListaTelefone().add(telefone);
               }
           }
           try{
               repository.persist(logistica);
           } catch (Exception e) {
               throw new GeneralErrorException("500", "Internal Server Error", "LogisticaServiceImpl(insert)", "Não consegui persistir os dados da nova empresa de Logística no repositório " + e.getCause());
           }
        return LogisticaResponseDTO.valueOf(logistica);
    }


    @Override
    @Transactional
    public LogisticaResponseDTO update(LogisticaDTO dto, Long id) {
            Logistica logistica = repository.findById(id);
            logistica.setNomeFantasia(dto.nomeFantasia());
            logistica.setEndSite(dto.endSite());
            logistica.setCnpj(dto.cnpj());
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
                        TelefoneFormatador.formataNumeroTelefone(tele.numeroTelefone())
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
                        endereco.setNumeroLote(end1.numeroLote());
                        endereco.setBairro(end1.bairro());
                        endereco.setComplemento(end1.complemento());
                        endereco.setCep(end1.cep());
                        endereco.setLocalidade(end1.localidade());
                        endereco.setUF(end1.uf());
                        endereco.setPais(end1.pais());
                    }
                }
            }
    
        //repository.persist(logistica);
        return LogisticaResponseDTO.valueOf(logistica);
    }
  
    @Override
    public LogisticaResponseDTO findById(Long id) {
        return LogisticaResponseDTO.valueOf(repository.findById(id));
    }

    @Override
    public List<LogisticaResponseDTO> findAll() {
        return repository
                .listAll()
                .stream()
                .map(LogisticaResponseDTO::valueOf)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<LogisticaResponseDTO> findByAll(int page, int pageSize) {

        List<Logistica> list = repository
            .findAll()
            .page(page, pageSize)
            .list();
        return list
            .stream()
            .map(c -> LogisticaResponseDTO.valueOf(c))
            .collect(Collectors.toList());
    }
    
    @Override
    public long count() {
        return repository.count();
    }
    

}
