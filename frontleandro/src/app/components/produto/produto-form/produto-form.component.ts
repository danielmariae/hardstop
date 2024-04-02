import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavigationService } from '../../../services/navigation.service';
import { Produto, PlacaMae, Processador, Classificacao } from '../../../models/Produto.model';
import { ProdutoService } from '../../../services/produto.service';
import { PlacaMaeFormComponent } from './placaMae-form/placaMae-form.component';
import { ProcessadorFormComponent } from './processador-form/processador-form.component';
import { ProcessadorFormService } from './processador-form/processador-form-service.component';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SessionTokenService } from '../../../services/session-token.service';

@Component({
    selector: 'app-produto-form',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, PlacaMaeFormComponent, ProcessadorFormComponent],
    templateUrl: './produto-form.component.html',
    styleUrl: './produto-form.component.css'
  })

export class ProdutoFormComponent implements OnInit {
  @ViewChild('fileInput') fileInput: any;
  tipoProduto!: string;
  arquivosSelecionados: File[] = [];
  fileUrls: string[] = [];

  @Input() produto: Produto = new Produto();
  produtoForm: FormGroup;
  novaPlacaMae!: PlacaMae;
  novoProcessador!: Processador;
  classificacao: Classificacao[] = [];
  selectedFile!: File;

  get placaMae(): PlacaMae {
    return this.produto as PlacaMae;
  }

  get processador(): Processador {
    return this.produto as Processador;
  }

  constructor(private formBuilder: FormBuilder,
    private produtoService: ProdutoService,
    private route: ActivatedRoute,
    private navigationService: NavigationService,
    private processadorFormService: ProcessadorFormService,
    private http: HttpClient,
    private sessionTokenService: SessionTokenService,
    ) {

    this.produtoForm = formBuilder.group({
      id: [null],
      nome: [''],
      modelo: [''],
      marca: [''],
      descricao: [''],
      codigoBarras: [''],
      altura: [null],
      largura: [null],
      comprimento: [null],
      peso: [null],
      valorVenda: [null],
      quantidadeUnidades: [null],
      quantidadeNaoConvencional: [null],
      unidadeDeMedida: [null],
      classificacao: [null],
      arquivos: this.formBuilder.array([]),
      nomeImagem: [null],
    })
  }


  onFileSelected(event: any) {
    const files: FileList = event.target.files;
    for (let i = 0; i < files.length; i++) {
      this.arquivosSelecionados.push(files[i]);
    }
  }
  
  selecionarArquivos(input: HTMLInputElement) {
    input.click(); // Simula o clique no input file
  }

  uploadFiles(idProduto: number) {
    const formData = new FormData();
    let url: string; // Declaração e inicialização da variável url
    for (let i = 0; i < this.arquivosSelecionados.length; i++) {
      formData.append('nomeImagem', this.arquivosSelecionados[i].name);
      const reader = new FileReader();
      reader.readAsArrayBuffer(this.arquivosSelecionados[i]);
      reader.onload = () => { // Aguarda a conclusão da leitura do arquivo
      const arrayBuffer = reader.result as ArrayBuffer;
     const bytes = new Uint8Array(arrayBuffer);
     const blob = new Blob([bytes]);
     formData.append('imagem', blob);
    url = 'http://localhost:8080/produtos/upload/imagem/id/' + idProduto;
    
    // Envia os arquivos usando o HttpClient
    this.http.post(url , formData).subscribe({
      next: (response) => {
        // Sucesso!
      },
      error: (error) => {
        console.log(error);
      }
    });
  };
    }
    // Limpa os arquivos selecionados após o envio
    this.arquivosSelecionados = [];
  }
  
  removerArquivo(index: number) {
    this.arquivosSelecionados.splice(index, 1);
  }

  removerArquivoSelecionado(arquivo: File) {
    const index = this.arquivosSelecionados.indexOf(arquivo);
    if (index !== -1) {
      this.arquivosSelecionados.splice(index, 1);
    }
  }
  
  ngOnInit(): void {
    // Recuperando o nome do produto dos parâmetros da rota
    this.route.params.subscribe(params => {
        this.tipoProduto = params['tipoProduto'];
      });

     // Implementando o buscador para classificacao
     this.produtoService.getClassificacao().subscribe({
      next: (classificacao: Classificacao[]) => {
        this.classificacao = classificacao;
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
      }
    });

  }

 get arquivos(): FormArray {
   return this.produtoForm.get('arquivos') as FormArray;
 }

  encontrarClassificacao(nomeProcurado: string): Classificacao | undefined {
    for (const classifi of this.classificacao) {
        if (classifi.nome.toLowerCase() === nomeProcurado) {
            return classifi;
        }
    }
    return undefined; // Se não encontrar, retorna undefined
}

salvarProduto(): void {
    if (this.produtoForm.invalid) {
      return;
    }

  const classificacaoEncontrada = this.encontrarClassificacao(this.tipoProduto);

    const detalhesProduto: Produto = {
      id: this.produtoForm.value.id,
      nome: this.produtoForm.value.nome,
      modelo: this.produtoForm.value.modelo,
      marca: this.produtoForm.value.marca,
      descricao: this.produtoForm.value.descricao,
      codigoBarras: this.produtoForm.value.codigoBarras,
      altura: this.produtoForm.value.altura,
      largura: this.produtoForm.value.largura,
      comprimento: this.produtoForm.value.comprimento,
      peso: this.produtoForm.value.peso,
      valorVenda: this.produtoForm.value.valorVenda,
      quantidadeUnidades: this.produtoForm.value.quantidadeUnidades,
      quantidadeNaoConvencional: this.produtoForm.value.quantidadeNaoConvencional,
      unidadeDeMedida: this.produtoForm.value.unidadeDeMedida,
      classificacao: classificacaoEncontrada,
      nomeImagem: null,
    }

    // Se o tipo de produto for processadores, obtenho também os valores do formulário do componente de processador
    if (this.tipoProduto === 'processadores') {
      const detalhesProcessador = this.processadorFormService.getProcessadorFormValue();
      // Ajuntando os detalhes do produto e do processador
      Object.assign(detalhesProduto, detalhesProcessador);


      this.produtoService.insert(detalhesProduto, this.tipoProduto).subscribe ({
                next: (response) => {
                // Faz o upload de arquivos para o produto recém cadastrado
                // Usa o id do resultado positivo do cadastro do produto
                this.uploadFiles(response.id);
                this.produtoService.notificarProdutoInserido(); // Notificar outros componentes
                },
                error: (error) => {
                  // Este callback é executado quando ocorre um erro durante a emissão do valor
                  console.error('Erro:', error);
                  window.alert(error);
                }
            });
    }
  }

cancelarInsercao(): void {
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateBack();
}
}












