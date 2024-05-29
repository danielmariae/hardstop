package br.unitins.topicos1.dto.produto.processador;

import java.util.List;

import br.unitins.topicos1.model.produto.Classificacao;
import br.unitins.topicos1.model.produto.Processador;

public record ProcessadorResponseDTO(
    
    Long id,
    String nome,
    String modelo,
    String marca,
    String descricao,
    String codigoBarras,
    Double altura,
    Double largura,
    Double comprimento,
    Double peso,
    Double valorVenda,
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida,
    List<String> nomeImagem,
    Classificacao classificacao,
    String soquete,
    String pistas,
    String bloqueado,
    String compatibilidadeChipset,
    String canaisMemoria,
    String capacidadeMaxMemoria,
    String pontenciaBase,
    String potenciaMaxima,
    String frequenciaBase,
    String frequenciaMaxima,
    String tamanhoCacheL3,
    String tamanhoCacheL2,
    String numNucleosFisicos,
    String numThreads,
    String velMaxMemoria,
    String conteudoEmbalagem
) {
    public static ProcessadorResponseDTO valueOf(Processador proc) {
        return new ProcessadorResponseDTO(
            proc.getId(),
            proc.getNome(),
            proc.getModelo(),
            proc.getMarca(),
            proc.getDescricao(),
            proc.getCodigoBarras(),
            proc.getAltura(),
            proc.getLargura(),
            proc.getComprimento(),
            proc.getPeso(),
            proc.getValorVenda(),
            proc.getQuantidadeUnidades(),
            proc.getQuantidadeNaoConvencional(),
            proc.getUnidadeDeMedida(),
            proc.getNomeImagem(),
            proc.getClassificacao(),
            proc.getSoquete(),
            proc.getPistas(),
            proc.getBloqueado(),
            proc.getCompatibilidadeChipset(),
            proc.getCanaisMemoria(),
            proc.getCapacidadeMaxMemoria(),
            proc.getPontenciaBase(),
            proc.getPotenciaMaxima(),
            proc.getFrequenciaBase(),
            proc.getFrequenciaMaxima(),
            proc.getTamanhoCacheL3(),
            proc.getTamanhoCacheL2(),
            proc.getNumNucleosFisicos(),
            proc.getNumThreads(),
            proc.getVelMaxMemoria(),
            proc.getConteudoEmbalagem()
        );
    }
    
}
