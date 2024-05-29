package br.unitins.topicos1.dto.produto;

import java.util.List;

import br.unitins.topicos1.model.produto.Classificacao;
// import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;

public record ProdutoDTO(

    @NotBlank(message = "O campo Nome não pode ser nulo.")
    String nome,
    // @NotBlank(message = "O campo Modelo não pode ser nulo.")
    String modelo,
    // @NotBlank(message = "O campo Marca não pode ser nulo.")
    String marca,
    @NotBlank(message = "O campo Descricao não pode ser nulo.")
    String descricao,
    //@NotBlank(message = "O campo Código de Barras não pode ser nulo.")
    String codigoBarras,
    // @NotNull(message = "O campo altura não pode ser nulo")
    // @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double altura,
    // @NotNull(message = "O campo largura não pode ser nulo")
    // @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double largura,
    // @NotNull(message = "O campo comprimento não pode ser nulo")
    // @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double comprimento,
    // @NotNull(message = "O campo Peso do produto não pode ser nulo")
    // @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double peso,
    // @NotNull(message = "O campo quantidade não pode ser nulo")
    // @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double valorVenda,
    // @NotNull(message = "O campo Quantidade em unidades não pode ser nulo")
    Integer quantidadeUnidades,
    // @NotNull(message = "O campo Quantidade não convencional não pode ser nulo")
    // @Digits(integer = 5, fraction = 5, message = "Por favor, digite um número válido")
    Double quantidadeNaoConvencional,
    //@NotBlank(message = "O campo unidade de medida não pode ser nulo.")
    String unidadeDeMedida,
    List<String> nomeImagem,
    Classificacao classificacao
    ) {}
