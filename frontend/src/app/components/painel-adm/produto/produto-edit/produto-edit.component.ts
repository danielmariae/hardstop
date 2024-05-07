import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavigationService } from '../../../../services/navigation.service';
import { Produto, PlacaMae, Processador, Classificacao } from '../../../../models/produto.model';
import { ProdutoService } from '../../../../services/produto.service';
import { PlacaMaeFormComponent } from '../produto-form/placaMae-form/placaMae-form.component';
import { ProcessadorFormComponent } from '../produto-form/processador-form/processador-form.component';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SessionTokenService } from '../../../../services/session-token.service';

@Component({
    selector: 'app-produto-edit',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, PlacaMaeFormComponent, ProcessadorFormComponent],
    templateUrl: './produto-edit.component.html',
    styleUrl: './produto-edit.component.css'
})

export class ProdutoEditComponent implements OnInit {
    produto: Produto;
    @ViewChild('fileInput') fileInput: any;
    produtoForm!: FormGroup;
    classificacao: Classificacao[] = [];
    arquivosSelecionados: File[] = [];
    selectedFile!: File;
    id: number = 0;
    tipoProduto!: string;
    placaMaeForm!: FormGroup; // Adicione FormGroup para o formulário de PlacaMae
    processadorForm!: FormGroup; // Adicione FormGroup para o formulário de Processador
    placaMae!: PlacaMae;
    processador!: Processador;

    constructor(private formBuilder: FormBuilder,
        private produtoService: ProdutoService,
        private route: ActivatedRoute,
        private navigationService: NavigationService,
        private http: HttpClient,
        private sessionTokenService: SessionTokenService,
    ) {
        this.produto = new Produto();
        this.placaMae = new PlacaMae();
        this.processador = new Processador();
        this.produtoForm = this.formBuilder.group({
            nome: [''],
            modelo: [''],
            marca: [''],
            descricao: [''],
            codigoBarras: [''],
            altura: [''],
            largura: [''],
            comprimento: [''],
            peso: [''],
            valorVenda: [''],
            quantidadeUnidades: [''],
            quantidadeNaoConvencional: [''],
            unidadeDeMedida: [''],
            nomeImagem: [''],
            classificacao: [''],
            arquivos: this.formBuilder.array([]), 
        });
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
      if(this.arquivosSelecionados.length != 0){
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
        this.id = Number(this.route.snapshot.params['id']);
        //this.tipoProduto = this.route.snapshot.params['tipoProduto'];
        this.carregarClassificacao();
        this.carregarProduto();
      }
      get arquivos(): FormArray {
        return this.produtoForm.get('arquivos') as FormArray;
      }
    
      carregarProduto() {
        this.produtoService.findById(this.id)
          .subscribe(produto => {
            this.produto = produto;
            this.tipoProduto = this.produto.classificacao?.nome?.toLowerCase() ?? '';
            console.log(produto);
            console.log(this.tipoProduto);

            //Chamadas dos métodos para inicializar os formulários específicos
            if (this.tipoProduto === 'placas mãe') {
                this.placaMae = this.produto as PlacaMae; // Conversão explícita para PlacaMae
                this.inicializarPlacaMaeForm();
            } else if (this.tipoProduto === 'processadores') {
                this.processador = this.produto as Processador; // Conversão explícita para Processador
                this.inicializarProcessadorForm();
            } else {
            this.inicializarProdutoForm();
            }
          });
          
      }
    
      carregarClassificacao() {
        this.produtoService.getClassificacao()
          .subscribe(classificacao => {
            this.classificacao = classificacao;
          });
      }

      inicializarPlacaMaeForm(): void {
        this.produtoForm.patchValue(this.placaMae);
        console.log(this.produtoForm);
      }

      inicializarProcessadorForm(): void {
        this.produtoForm.patchValue(this.processador);
        console.log(this.produtoForm);
      }

      inicializarProdutoForm(): void {
        this.produtoForm.patchValue(this.produto);
      }


    cancelarEdicao(): void {
        // Redireciona o usuário para outra rota anterior
        this.navigationService.navigateBack();
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
          nomeImagem: this.produto.nomeImagem,
        }
    
        // Otenho também os valores do formulário específicos para o componente do produto a ser inserido
          const detalhesTipoProduto = this.produtoService.getProdutoFormValue();
          // Ajuntando os detalhes do produto pai e do produto filho
          Object.assign(detalhesProduto, detalhesTipoProduto);

        console.log(detalhesProduto);
        this.produtoService.update(detalhesProduto, this.tipoProduto, this.produto.id).subscribe ({
            next: (response) => {
            // Faz o upload de arquivos para o produto recém modificado
            // Usa o id do produto para carregar mais imagens
            this.uploadFiles(this.produto.id);
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