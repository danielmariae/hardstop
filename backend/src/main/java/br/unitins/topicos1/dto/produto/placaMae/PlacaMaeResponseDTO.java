package br.unitins.topicos1.dto.produto.placaMae;

import java.util.List;

import br.unitins.topicos1.model.produto.Classificacao;
import br.unitins.topicos1.model.produto.PlacaMae;

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
    String imagemPrincipal,
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
            placaMae.getImagemPrincipal(),
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
