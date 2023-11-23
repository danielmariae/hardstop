package br.unitins.topicos1.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import org.jrimum.domkee.pessoa.Endereco;
import org.jrimum.utilix.DateFormat;
import com.github.braully.boleto.LayoutsSuportados;
import com.github.braully.boleto.RemessaArquivo;
import com.github.braully.boleto.RetornoArquivo;
import com.github.braully.boleto.TituloArquivo;
import br.unitins.topicos1.model.Cliente;
import br.unitins.topicos1.model.Empresa;


@ApplicationScoped
public class GerarBoleto {

        private static final String PATH_USER_BOLETO = System.getProperty("user.home") +
        File.separator + "quarkus" +
        File.separator + "boleto" +
        File.separator + "usuario" +
        File.separator;



	  /*
     * Ver exemplo mais detalhado em:
     * com.github.braully.boleto.LayoutsSuportados._LAYOUT_FEBRABAN_CNAB240
     */
	/* public void geraLayout() {
		TagLayout arquivo = tag("arquivo");
        arquivo.with(
                tag("cabecalho").with(
                        //a linha de cabeçalho será ignorada
                        tag("branco").length(240)
                ),
                tag("detalhe").with(
                        tag("codigoBanco").length(3),
                        //Val é usado para setar um campo literal fixo: espaçoes em branco, codigos, literais e etc
                        tag("branco").val("  "),
                        tag("codigoMoeda").val("09"),
                        //As tags com id são importantes pra determinar o tipo da linha no layout de retorno
                        tag("codigoRegistro").length(1).id(true),
                        tag("segmento").id(true).value("D"),
                        //Alguns campos podemos precisar de formatação ou parser personalizado, exemplo data
                        tag("dataVencimento").length(8).format(new SimpleDateFormat("ddMMyyyy"))
                ),
                tag("rodape").with(
                        //a linha de rodape será ignorada
                        tag("branco").length(240)
                )
        );

        //
        System.out.println(arquivo);
	} */

	/* Um arquivo de remessa de boletos de cobrança contem um ou mais lotes de boletos. Cada lote pode conter um ou mais boletos.

	Nesse exemplo simples iremos criar um arquivo de remessa com apenas um lote de boletos e dois boletos fictícios. */
	public void geraRemessa() {
		RemessaArquivo remessa = new RemessaArquivo(LayoutsSuportados.LAYOUT_BB_CNAB240_COBRANCA_REMESSA);
        //Cabeçalho do arquivo de remessa: obrigatório
        remessa.addNovoCabecalho()
                .sequencialArquivo(1)
                .dataGeracao(new Date()).setVal("horaGeracao", new Date())
                .banco("0", "Banco").cedente("ACME S.A LTDA.", "1")
                .convenio("1", "1", "1", "1")
                .carteira("00");
        //Cabeçalho do lote: obrigatório
        remessa.addNovoCabecalhoLote()
                .operacao("R")//Operação de remessa
                .servico(1)//Cobrança
                .forma(1)//Crédito em Conta Corrente
                .banco("0", "Banco")
                .cedente("ACME S.A LTDA.", "1")
                .convenio("1", "1", "1", "1")
                .carteira("00");;

        int contadorRegistroLote = 1;
        //Detalhes do boleto #1  
        // Boleto emitido pela empresa ACME S.A. ltada
        // Para o cliente: Fulano de Tal cpf: 000.000.000-00
        // Valor R$ 0,01 com vencimento na data de 13/12/2024
        remessa.addNovoDetalheSegmentoP()
                //Dados da cobrança
                .valor(1)
                .valorDesconto(0).valorAcrescimo(0)//opcionais
                .dataGeracao(new Date())
                //.dataVencimento(new Date())
                .dataVencimento(DateFormat.DDMMYYYY_B.parse("13/12/2024"))
                .numeroDocumento(1).nossoNumero(1)
                //Dados da Empresa ACM S.A. LTDA
                .banco("0", "Banco")
                .cedente("ACME S.A LTDA.", "1")
                .convenio("1", "1", "1", "1")
                //.sequencialRegistro(1)
                .sequencialRegistro(contadorRegistroLote++)
                .carteira("00");

        remessa.addNovoDetalheSegmentoQ()
                //Dados do cliente
                .sacado("Fulano de Tal", "00000000000")
                .banco("0", "Banco")
                .cedente("ACME S.A LTDA.", "1")
                .convenio("1", "1", "1", "1")
                //.sequencialRegistro(2)
                .sequencialRegistro(contadorRegistroLote++)
                .carteira("00");

        //Detalhes do boleto #2 Boleto emitido pela empresa ACME S.A. ltada
        // Para o cliente: Ciclano de Tal cpf: 11111111111
        // Valor R$ 2,50 com vencimento na data de hoje
        remessa.addNovoDetalheSegmentoP()
                .valor(2.50)
                .valorDesconto(0).valorAcrescimo(0)//opcionais
                .dataGeracao(new Date())
                .dataVencimento(new Date())
                .numeroDocumento(2).nossoNumero(2)
                .banco("0", "Banco")
                .cedente("ACME S.A LTDA.", "1")
                .convenio("1", "1", "1", "1")
                //.sequencialRegistro(3)
                .sequencialRegistro(contadorRegistroLote++)
                .carteira("00");
        //Detalhes do boleto #2
        remessa.addNovoDetalheSegmentoQ()
                .sacado("Ciclano de Tal", "11111111111")
                .banco("0", "Banco")
                .cedente("ACME S.A LTDA.", "1")
                .convenio("1", "1", "1", "1")
                //.sequencialRegistro(4)
                .sequencialRegistro(contadorRegistroLote++)
                .carteira("00");

        //Rodapé do lote: obrigatório
        remessa.addNovoRodapeLote()
                .quantidadeRegistros(2)
                .valorTotalRegistros(1)
                .banco("0", "Banco")
                .cedente("ACME S.A LTDA.", "1")
                .convenio("1", "1", "1", "1")
                .carteira("00");

        //Rodapé do arquivo de remessa: obrigatório
        remessa.addNovoRodape()
                .quantidadeRegistros(1)
                .valorTotalRegistros(1)
                .setVal("codigoRetorno", "1")
                .banco("0", "Banco").cedente("ACME S.A LTDA.", "1")
                .convenio("1", "1", "1", "1")
                .carteira("00");

        String remessaStr = remessa.render();
        System.out.println(remessaStr);
    }
	
	/* O código abaixo mostra como ler um arquivo de retorno de boleto CNAB 240. Normalmente compatível com todos os bancos, porém existem personalizações possíveis para cada banco. Favor conferir a documentação do banco para maiores detalhes. A versão do layout também podem apresentar diferenças, porém o código abaixo é compatível com a versão 5.0 do layout. */
	
	public void lerRetorno() throws FileNotFoundException, IOException {
		BufferedReader leitorArquivo = new 
        //Arquivo de retorno fornecido pelo banco
        BufferedReader(new FileReader("RETORNO001.txt"));

        List<String> linhasLidas = new ArrayList<>();
        String linha = null;
        while (null != (linha = leitorArquivo.readLine())) {
            linhasLidas.add(linha);
        }

        // Layout de arquivo de retorno
        RetornoArquivo retorno = new RetornoArquivo(LayoutsSuportados.LAYOUT_FEBRABAN_CNAB240);
        // Parse do arquivo lido no layout LAYOUT_FEBRABAN_CNAB240
        retorno.parse(linhasLidas);

        System.out.println("Detalhes as Titulos: ");

        List<TituloArquivo> titulos = retorno.detalhesAsTitulos();
        //Titulos encontrados no arquivo de retorno
        // E principais dados disponiveis
        for (TituloArquivo titulo : titulos) {
            String segmento = titulo.segmento();
            String numeroDocumento = titulo.numeroDocumento();
            String nossoNumero = titulo.nossoNumero();
            String valorPagamento = titulo.valorPagamento();
            String valorLiquido = titulo.valorLiquido();
            String dataOcorrencia = titulo.dataOcorrencia();
            String movimentoCodigo = titulo.movimentoCodigo();
            String rejeicoes = titulo.rejeicoes();
            String valorTarifaCustas = titulo.valorTarifaCustas();

            // Print dos dados
            System.out.println("Campos: {segmento=" + segmento + " numeroDocumento=" + numeroDocumento);
            System.out.println("\tnossoNumero=" + nossoNumero + " valorPagamento=" + valorPagamento);
            System.out.println("\tvalorLiquido=" + valorLiquido + " dataOcorrencia=" + dataOcorrencia);
            System.out.println("\tmovimentoCodigo=" + movimentoCodigo + " rejeicoes=" + rejeicoes);
            System.out.println("\tvalorTarifaCustas=" + valorTarifaCustas + " rejeicoes=" + valorTarifaCustas + "}");
        }
		leitorArquivo.close();
	}




public static String geraBoletoFinal(Integer intervalo, Double valorCompra, Cliente cliente, Empresa empresa, br.unitins.topicos1.model.Endereco enderecoCliente) throws IOException {

        // Cedente
    Cedente cedente = new Cedente(empresa.getNomeReal(), empresa.getCnpj());

    // Sacado
    Sacado sacado = new Sacado(cliente.getNome(), cliente.getCpf());

    // Endereço do sacado
        Endereco endereco = new Endereco();
    endereco.setUF(enderecoCliente.getUF());
    endereco.setLocalidade(enderecoCliente.getLocalidade());
    endereco.setCep(enderecoCliente.getCep());
    endereco.setBairro(enderecoCliente.getBairro());
    endereco.setLogradouro(enderecoCliente.getLogradouro());
    endereco.setNumero(enderecoCliente.getNumero());

    sacado.addEndereco(endereco); 

    // Criando o título
    ContaBancaria contaBancaria = new ContaBancaria(
      BancosSuportados.BANCO_DO_BRASIL.create()
    );

    if(empresa.getNumeroAgencia().matches("\\d+-\\w")) {
        String[] agencia = empresa.getNumeroAgencia().split("-");
        contaBancaria.setAgencia(new Agencia(Integer.parseInt(agencia[0]), agencia[1]));
    } else {
        contaBancaria.setAgencia(new Agencia(Integer.parseInt(empresa.getNumeroAgencia())));
    }

    if(empresa.getNumeroConta().matches("\\d+-\\w")) {
        String[] conta = empresa.getNumeroConta().split("-");
        contaBancaria.setNumeroDaConta(new NumeroDaConta(Integer.parseInt(conta[0]), conta[1])); 
    } else {
        contaBancaria.setNumeroDaConta(new NumeroDaConta(Integer.parseInt(empresa.getNumeroConta())));
    }
    
    //contaBancaria.setCarteira(new Carteira(17, TipoDeCobranca.COM_REGISTRO));
    contaBancaria.setCarteira(new Carteira(17));

    Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
    titulo.setNumeroDoDocumento("0000000066");
    titulo.setNossoNumero("28588450000000066");
    titulo.setDigitoDoNossoNumero("7");


    titulo.setValor(BigDecimal.valueOf(valorCompra));

    LocalDateTime dataHoraAtual = LocalDateTime.now();
    // Converta o LocalDateTime em Instant
    Instant instant = dataHoraAtual.atZone(ZoneId.systemDefault()).toInstant();
    // Crie uma instância de Date a partir do Instant
    Date date = Date.from(instant);
    titulo.setDataDoDocumento(date);

    LocalDateTime dataVencimento = dataHoraAtual
      .plusDays(intervalo);
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

    // criar diretorio caso nao exista
    Path diretorio = Paths.get(PATH_USER_BOLETO);
    Files.createDirectories(diretorio);

    String extensao = "pdf";
    Boolean flipflop = false;
    Path filePath;
    do {
      // criando o nome do arquivo randomico
      String novoNomeArquivo = UUID.randomUUID() + "." + extensao;

      // definindo o caminho completo do arquivo
      filePath = diretorio.resolve(novoNomeArquivo);
      flipflop = filePath.toFile().exists();
    } while (flipflop);


        BoletoViewer create = BoletoViewer.create(boleto);
        File arquivoPdf = create.getPdfAsFile(filePath.toString());
        mostreBoletoNaTela(arquivoPdf);

        return filePath.toFile().getName();
}

private static void mostreBoletoNaTela(File arquivoBoleto) {

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        try {
                desktop.open(arquivoBoleto);
        } catch (IOException e) {
                e.printStackTrace();
        }
}

 
 /* public static void geraBoletoConvenio() {
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
		Boleto boleto = new Boleto(titulo); */

		/* cria uma informação fake para o usuário, pois  foi necessáio o nº convênio em contaBancaria.setNumeroDaConta para 
		*  poder mostrar agencia e conta para o usuário
		*/
		/* boleto.addTextosExtras("txtFcAgenciaCodigoCedente", "1674-8/101.912-0"); 
		boleto.addTextosExtras("txtRsAgenciaCodigoCedente", "1674-8/101.912-0"); 
		boleto.setLocalPagamento("Pagar preferencialmente no Banco do Brasil");
		boleto.setInstrucaoAoSacado("Evite multas, pague em dias suas contas.");

		boleto.setInstrucao1("NÃO ACEITAR PAGAMENTO EM CHEQUE");
		boleto.setInstrucao3("EM CASO DE ATRASO COBRAR MULTA DE 2%, MAIS JUROS DE 1% AO MÊS");
		boleto.setInstrucao4("PROJETO: 3426 | SUBPROJETO: 10 - CURSOS LIVRES-CLLE/UFPA - 1º NÍVEL ");

		BoletoViewer boletoViewer = new BoletoViewer(boleto);

		File arquivoPdf = boletoViewer.getPdfAsFile("boletoConvBB.pdf");
		 mostreBoletoNaTela(arquivoPdf);
  } */
}
