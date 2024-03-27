package br.unitins.topicos1.service;

import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.Formatadores.TelefoneFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.*;
import br.unitins.topicos1.model.*;
import br.unitins.topicos1.repository.FornecedorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.jrimum.domkee.pessoa.CEP;

import java.util.*;

@ApplicationScoped
public class FornecedorServiceImpl implements FornecedorService {

    @Inject
    FornecedorRepository repository;
    
    @Override
    @Transactional
    public FornecedorResponseDTO insert(FornecedorDTO dto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeFantasia(dto.nomeFantasia());
        fornecedor.setCnpj(dto.cnpj());
        fornecedor.setEndSite(dto.endSite());
        if (dto.listaEndereco() != null && !dto.listaEndereco().isEmpty()) {
            try {
                fornecedor.setListaEndereco(new ArrayList<Endereco>());
            } catch (Exception e) {
                throw new GeneralErrorException("500", "Internal Server Error", "FornecedorServiceImpl(insert)", "Não consegui alocar memória para a lista de endereços do novo Fornecedor. Tente novamente mais tarde! " +  e.getCause());
            }

            for (EnderecoDTO end : dto.listaEndereco()) {
                Endereco endereco = new Endereco();
                endereco.setNome(end.nome());
                endereco.setLogradouro(end.logradouro());
                endereco.setNumeroLote(end.numeroLote());
                endereco.setBairro(end.bairro());
                endereco.setComplemento(end.complemento());
                endereco.setCep(new CEP(EnderecoFormatador.validaCep(end.cep().getCep())));
                endereco.setLocalidade(end.localidade());
                endereco.setUF(end.uf());
                endereco.setPais(end.pais());
                fornecedor.getListaEndereco().add(endereco);
            }
           }
           if (dto.listaTelefone() != null && !dto.listaTelefone().isEmpty()) {
               try {
                   fornecedor.setListaTelefone(new ArrayList<Telefone>());
               } catch (Exception e) {
                   throw new GeneralErrorException("500", "Internal Server Error", "FornecedorServiceImpl(insert)", "Não consegui alocar memória para a lista telefônica do novo Fornecedor. Tente novamente mais tarde! " +  e.getCause());
               }
               for (TelefoneDTO tel : dto.listaTelefone()) {
                   Telefone telefone = new Telefone();
                   telefone.setTipoTelefone(TipoTelefone.valueOf(tel.tipo()));
                   telefone.setDdd(tel.ddd());
                   telefone.setNumeroTelefone(
                           TelefoneFormatador.validaNumeroTelefone(tel.numeroTelefone()));
                   fornecedor.getListaTelefone().add(telefone);
               }
           }
           try{
               repository.persist(fornecedor);
           } catch (Exception e) {
               throw new GeneralErrorException("500", "Internal Server Error", "FornecedorServiceImpl(insert)", "Não consegui persistir os dados do Fornecedor no repositório " + e.getCause());
           }
        return FornecedorResponseDTO.valueOf(fornecedor);
    }


    @Override
    @Transactional
    public FornecedorResponseDTO update(FornecedorDTO dto, Long id) {
            Fornecedor fornecedor = repository.findById(id);
            fornecedor.setNomeFantasia(dto.nomeFantasia());
            fornecedor.setCnpj(dto.cnpj());
            fornecedor.setEndSite(dto.endSite());
         


            List<Telefone> tel = fornecedor.getListaTelefone();
            tel.clear();
            for (TelefoneDTO tele : dto.listaTelefone()) {
                Telefone tele1 = new Telefone();
                tele1.setTipoTelefone(TipoTelefone.valueOf(tele.tipo()));
                tele1.setDdd(tele.ddd());
                tele1.setNumeroTelefone(TelefoneFormatador.validaNumeroTelefone(tele.numeroTelefone()));
                tel.add(tele1);
            }
            
            

            List<Endereco> end = fornecedor.getListaEndereco();
            end.clear();

           
                for (EnderecoDTO end1 : dto.listaEndereco()) {
                    
                        Endereco endereco = new Endereco();
                        endereco.setNome(end1.nome());
                        endereco.setLogradouro(end1.logradouro());
                        endereco.setNumeroLote(end1.numeroLote());
                        endereco.setBairro(end1.bairro());
                        endereco.setComplemento(end1.complemento());
                        endereco.setCep(new CEP(EnderecoFormatador.validaCep(end1.cep().getCep())));
                        endereco.setLocalidade(end1.localidade());
                        endereco.setUF(end1.uf());
                        endereco.setPais(end1.pais());
                        end.add(endereco);
                }
           
        //repository.persist(fornecedor);
        return FornecedorResponseDTO.valueOf(fornecedor);
    }
 
    @Override
    public FornecedorResponseDTO findById(Long id) {
        return FornecedorResponseDTO.valueOf(repository.findById(id));
    }

    @Override
    public List<FornecedorResponseDTO> findByAll() {
        return repository
                .listAll()
                .stream()
                .map(FornecedorResponseDTO::valueOf)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
