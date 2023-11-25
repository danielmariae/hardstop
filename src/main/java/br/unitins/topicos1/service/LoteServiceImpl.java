package br.unitins.topicos1.service;

import br.unitins.topicos1.Formatadores.EnderecoFormatador;
import br.unitins.topicos1.application.GeneralErrorException;
import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.LoteResponseDTO;
import br.unitins.topicos1.model.Endereco;
import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Lote;
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
            try{
                repository.persist(lote);
            } catch (Exception e) {
                throw new GeneralErrorException("500", "Internal Server Error", "LoteServiceImpl(insert)", "Não consegui persistir os dados do cliente no repositório " + e.getCause());
            }
        }
        return LoteResponseDTO.valueOf(lote);
    }


    @Override
    public LoteResponseDTO update(LoteResponseDTO dto, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public LoteResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public List<LoteResponseDTO> findByName(String lote) {
        return null;
    }

    @Override
    public List<LoteResponseDTO> findByAll() {
        return null;
    }
}
