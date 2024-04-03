package br.unitins.topicos1.dto;

import java.util.List;

import br.unitins.topicos1.model.Classificacao;
import br.unitins.topicos1.model.PlacaMae;

public record PlacaMaeResponseDTO(
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
    String cpu,
    String chipset,
    String memoria,
    String bios,
    String grafico,
    String lan,
    String slots,
    String armazenamento
) {
    public static PlacaMaeResponseDTO valueOf(PlacaMae placaMae) {
        return new PlacaMaeResponseDTO(
            placaMae.getId(),
            placaMae.getNome(),
            placaMae.getModelo(),
            placaMae.getMarca(),
            placaMae.getDescricao(),
            placaMae.getCodigoBarras(),
            placaMae.getAltura(),
            placaMae.getLargura(),
            placaMae.getComprimento(),
            placaMae.getPeso(),
            placaMae.getValorVenda(),
            placaMae.getQuantidadeUnidades(),
            placaMae.getQuantidadeNaoConvencional(),
            placaMae.getUnidadeDeMedida(),
            placaMae.getNomeImagem(),
            placaMae.getClassificacao(),
            placaMae.getCpu(),
            placaMae.getChipset(),
            placaMae.getMemoria(),
            placaMae.getBios(),
            placaMae.getGrafico(),
            placaMae.getLan(),
            placaMae.getSlots(),
            placaMae.getArmazenamento()
        );
    }
}
