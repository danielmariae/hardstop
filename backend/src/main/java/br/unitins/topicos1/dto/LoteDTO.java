package br.unitins.topicos1.dto;

import br.unitins.topicos1.model.Fornecedor;
import br.unitins.topicos1.model.Produto;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoteDTO(
    @NotBlank(message = "O campo lote não pode ser nulo.")
    String lote,
    @NotBlank(message = "O campo dataHoraChegadaLote não pode ser nulo.")
    String dataHoraChegadaLote,
    @NotNull(message = "O campo Fornecedor não pode ser nulo")
    Fornecedor fornecedor,
    @NotNull(message = "O campo Produto não pode ser nulo")
    Produto produto,
    //@NotNull(message = "O campo quantidade não pode ser nulo")
    Integer quantidadeUnidades,
    Double quantidadeNaoConvencional,
    String unidadeDeMedida,
    @NotNull(message = "O campo Custo da compra não pode ser nulo")
    @Digits(integer = 6, fraction = 3, message = "Por favor, digite um número válido")
    Double custoCompra,
    @NotNull(message = "O campo Valor da venda não pode ser nulo")
    @Digits(integer = 6, fraction = 3, message = "Por favor, digite um número válido")
    Double valorVenda,
    @NotNull(message = "O campo garantia não pode ser nulo")
    Integer garantiaMeses
    // LoteDTO não recebe informação de status porque ela será atribuída no insert do LoteService como 2 (EM ESPERA)
) {}
