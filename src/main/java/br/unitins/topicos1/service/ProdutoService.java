package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.EnderecoDTO;
import br.unitins.topicos1.dto.LoteDTO;
import br.unitins.topicos1.dto.ProdutoDTO;
import br.unitins.topicos1.dto.ProdutoResponseDTO;
import br.unitins.topicos1.dto.TelefoneDTO;

public interface ProdutoService {
    public ProdutoResponseDTO insert(ProdutoDTO dto);

    public ProdutoResponseDTO update(ProdutoDTO dto, Long id);

    public ProdutoResponseDTO updateLote(List<LoteDTO> lt, Long id);

    public ProdutoResponseDTO updateTelefone(List<TelefoneDTO> tel, Long id);

    public ProdutoResponseDTO updateEndereco(EnderecoDTO end, Long id);

    public ProdutoDTO updateSenha(String senha, Long id);

    public void delete(Long id);

    public ProdutoResponseDTO findById(Long id);

    public ProdutoResponseDTO findByCodigoBarras(String cpf);

    public List<ProdutoResponseDTO> findByName(String nome);

    public List<ProdutoResponseDTO> findByAll();
}
