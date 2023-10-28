package br.unitins.topicos1.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.banco.Agencia;
import org.jrimum.domkee.banco.Carteira;
import org.jrimum.domkee.banco.Cedente;
import org.jrimum.domkee.banco.ContaBancaria;
import org.jrimum.domkee.banco.NumeroDaConta;
import org.jrimum.domkee.banco.Sacado;
import org.jrimum.domkee.banco.TipoDeTitulo;
import org.jrimum.domkee.banco.Titulo;
import org.jrimum.domkee.banco.Titulo.Aceite;
import org.jrimum.domkee.pessoa.CEP;
import org.jrimum.domkee.pessoa.Endereco;
import org.jrimum.domkee.pessoa.UnidadeFederativa;

@ApplicationScoped
public class GerarBoleto {

  public static void geraBoleto() {
    // Cedente
    Cedente cedente = new Cedente(
      "FADESP - Fundação de Amparo e Desenvolvimento da Pesquisa",
      "10.687.566/0001-97"
    );

    // Sacado
    Sacado sacado = new Sacado("Rayan Teixeira", "00864683243");

    // Endereço do sacado
    Endereco endereco = new Endereco();
    endereco.setUF(UnidadeFederativa.PA);
    endereco.setLocalidade("Ananindeua");
    endereco.setCep(new CEP("66645-000"));
    endereco.setBairro("AGUAS LINDAS");
    endereco.setLogradouro("BR 316 - KM 05");
    endereco.setNumero("1010");

    sacado.addEndereco(endereco);

    // Criando o título
    ContaBancaria contaBancaria = new ContaBancaria(
      BancosSuportados.BANCO_DO_BRASIL.create()
    );
    contaBancaria.setAgencia(new Agencia(1674, "8"));
    contaBancaria.setNumeroDaConta(new NumeroDaConta(2941653));
    //contaBancaria.setNumeroDaConta(new NumeroDaConta(101739, "X"));
    //contaBancaria.setCarteira(new Carteira(17, TipoDeCobranca.COM_REGISTRO));
    contaBancaria.setCarteira(new Carteira(17));

    Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
    titulo.setNumeroDoDocumento("0000000066");
    titulo.setNossoNumero("28588450000000066");
    titulo.setDigitoDoNossoNumero("7");

    titulo.setValor(BigDecimal.valueOf(100.00));

    LocalDateTime dataHoraAtual = LocalDateTime.now();
    // Converta o LocalDateTime em Instant
    Instant instant = dataHoraAtual.atZone(ZoneId.systemDefault()).toInstant();
    // Crie uma instância de Date a partir do Instant
    Date date = Date.from(instant);
    titulo.setDataDoDocumento(date);

    LocalDateTime dataVencimento = dataHoraAtual
      .plusDays(7)
      .with(LocalTime.of(23, 59, 59));
    // Converta o LocalDateTime em Instant
    Instant instantVen = dataVencimento
      .atZone(ZoneId.systemDefault())
      .toInstant();
    // Crie uma instância de Date a partir do Instant
    Date dateVen = Date.from(instantVen);
    titulo.setDataDoVencimento(dateVen);

    titulo.setTipoDeDocumento(TipoDeTitulo.DS_DUPLICATA_DE_SERVICO);

    titulo.setAceite(Aceite.N);

    // Dados do boleto
    Boleto boleto = new Boleto(titulo);
    boleto.setLocalPagamento("Pagar preferencialmente no Banco do Brasil");
    boleto.setInstrucaoAoSacado("Evite multas, pague em dias suas contas.");

    boleto.setInstrucao1(
      "Após o vencimento, aplicar multa de 2,00% e juros de 1,00% ao mês"
    );

    BoletoViewer boletoViewer = new BoletoViewer(boleto);

    File arquivoPdf = boletoViewer.getPdfAsFile("boletoBB.pdf");
	mostreBoletoNaTela(arquivoPdf);
  }

  public static void geraBoletoConvenio() {
	int convenio = 2866935 ;
	String nDocumento = "0000000003";

	// Cedente
		Cedente cedente = new Cedente("FADESP - Fundação de Amparo e Desenvolvimento da Pesquisa", "05.572.870/0001-59");

		// Sacado
		Sacado sacado = new Sacado("FRANCISCO DEMARIM DE AGUIAR JUNIOR", "00299967247");

		// Endereço do sacado
		Endereco endereco = new Endereco();
		endereco.setUF(UnidadeFederativa.PA);
		endereco.setLocalidade("Belém");
		endereco.setCep(new CEP("66075-110"));
		endereco.setBairro("Guamá");
		endereco.setLogradouro("Rua Augusto Corrêa, Lab. de Engenharia de Software(LABES)");
		endereco.setNumero("1");

		sacado.addEndereco(endereco);

		// Criando o título
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_DO_BRASIL.create());
		contaBancaria.setAgencia(new Agencia(1674, "8"));
		contaBancaria.setNumeroDaConta(new NumeroDaConta(convenio));
		//contaBancaria.setNumeroDaConta(new NumeroDaConta(333006, "0"));
		//contaBancaria.setCarteira(new Carteira(17, TipoDeCobranca.COM_REGISTRO));
		contaBancaria.setCarteira(new Carteira(17));

		Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
		titulo.setNumeroDoDocumento(nDocumento);
		//titulo.setNumeroDoDocumento("0000000066");

		titulo.setNossoNumero(convenio+nDocumento);
		//titulo.setDigitoDoNossoNumero("7");

		titulo.setValor(BigDecimal.valueOf(1.00));


		LocalDateTime dataHoraAtual = LocalDateTime.now();
    // Converta o LocalDateTime em Instant
    Instant instant = dataHoraAtual.atZone(ZoneId.systemDefault()).toInstant();
    // Crie uma instância de Date a partir do Instant
    Date date = Date.from(instant);
    titulo.setDataDoDocumento(date);
		LocalDateTime dataVencimento = dataHoraAtual
      .plusDays(7)
      .with(LocalTime.of(23, 59, 59));
    // Converta o LocalDateTime em Instant
    Instant instantVen = dataVencimento
      .atZone(ZoneId.systemDefault())
      .toInstant();
    // Crie uma instância de Date a partir do Instant
    Date dateVen = Date.from(instantVen);
    titulo.setDataDoVencimento(dateVen);

		titulo.setTipoDeDocumento(TipoDeTitulo.DS_DUPLICATA_DE_SERVICO);

		titulo.setAceite(Aceite.N);

		// Dados do boleto
		Boleto boleto = new Boleto(titulo);

		/* cria uma informação fake para o usuário, pois  foi necessáio o nº convênio em contaBancaria.setNumeroDaConta para 
		*  poder mostrar agencia e conta para o usuário
		*/
		boleto.addTextosExtras("txtFcAgenciaCodigoCedente", "1674-8/101.912-0"); 
		boleto.addTextosExtras("txtRsAgenciaCodigoCedente", "1674-8/101.912-0"); 
		boleto.setLocalPagamento("Pagar preferencialmente no Banco do Brasil");
		boleto.setInstrucaoAoSacado("Evite multas, pague em dias suas contas.");

		boleto.setInstrucao1("NÃO ACEITAR PAGAMENTO EM CHEQUE");
		boleto.setInstrucao3("EM CASO DE ATRASO COBRAR MULTA DE 2%, MAIS JUROS DE 1% AO MÊS");
		boleto.setInstrucao4("PROJETO: 3426 | SUBPROJETO: 10 - CURSOS LIVRES-CLLE/UFPA - 1º NÍVEL ");

		BoletoViewer boletoViewer = new BoletoViewer(boleto);

		File arquivoPdf = boletoViewer.getPdfAsFile("boletoConvBB.pdf");
		 mostreBoletoNaTela(arquivoPdf);
  }



  private static void mostreBoletoNaTela(File arquivoBoleto) {

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        try {
                desktop.open(arquivoBoleto);
        } catch (IOException e) {
                e.printStackTrace();
        }
}
}
