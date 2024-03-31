import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavigationService } from '../../../services/navigation.service';
import { Produto, PlacaMae, Processador, Classificacao } from '../../../models/Produto.model';
import { ProdutoService } from '../../../services/produto.service';
import { PlacaMaeFormComponent } from './placaMae-form/placaMae-form.component';
import { ProcessadorFormComponent } from './processador-form/processador-form.component';
import { ProcessadorFormService } from './processador-form/processador-form-service.component';

@Component({
    selector: 'app-produto-form',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, PlacaMaeFormComponent, ProcessadorFormComponent],
    templateUrl: './produto-form.component.html',
    styleUrl: './produto-form.component.css'
  })

export class ProdutoFormComponent implements OnInit {
  tipoProduto!: string;

  @Input() produto: Produto = new Produto();
  produtoForm: FormGroup;
  novaPlacaMae!: PlacaMae;
  novoProcessador!: Processador;
  classificacao: Classificacao[] = [];

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
    private processadorFormService: ProcessadorFormService
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
      nomeImagem: this.formBuilder.array([]),
    })
  }

  ngOnInit(): void {
    // Recuperando o nome do produto dos parâmetros da rota
    this.route.params.subscribe(params => {
        this.tipoProduto = params['tipoProduto'];
      });
      console.log(this.tipoProduto);
    // this.produtoForm = this.createProdutoForm();

     // Implementando o buscador para classificacao
     this.produtoService.getClassificacao().subscribe({
      next: (classificacao: Classificacao[]) => {
        this.classificacao = classificacao;
console.log(classificacao);
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
      }
    });

  }


get nomeImagem(): FormArray {
    return this.produtoForm.get('nomeImagem') as FormArray;
  }

  adicionarNomeImagens(): void {
    this.nomeImagem.push(this.criarNomeImagensFormGroup());
  }

  removerNomeImagens(index: number): void {
    this.nomeImagem.removeAt(index);
  }

  criarNomeImagensFormGroup(): FormGroup {
    return this.formBuilder.group({
      nomeImagem: [null],
    });
  }

salvarProduto(): void {
    if (this.produtoForm.invalid) {
      return;
    }
  
    // Obtendo os valores do formulário do componente de produto
    // const detalhesProduto = this.produtoForm.value;

    // const obj = this.produtoForm.value.nomeImagem;
    // console.log(this.produtoForm.value.nomeImagem);
    // const nomeImagemArray = [obj.nomeImagem]; // Cria um array com um único elemento contendo o valor de nomeImagem
    //const nomeImagemArray = this.produtoForm.value.nomeImagem.value;
    //const nomeImagemArray = this.produtoForm.get('nomeImagem').value.value;

    //const arr = JSON.parse(this.produtoForm.value.nomeImagem);
    // const arrayNomeImagem = this.produtoForm.value.nomeImagem.map(objeto => objeto.nomeImagem);
    // console.log(arrayNomeImagem); // Saída: ['effefef', 'ggtgt']
    // console.log(this.produtoForm.value.nomeImagem);
    const arraysNomeImagem = this.produtoForm.value.nomeImagem.map((objeto: { [s: string]: unknown; } | ArrayLike<unknown>) => {
      return Object.values(objeto);
    });
    
    const combinedArray = arraysNomeImagem.flat();
console.log(combinedArray);

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
      classificacao: this.produtoForm.value.classificacao,
      nomeImagem: combinedArray,
    }

    // Se o tipo de produto for processadores, obtenho também os valores do formulário do componente de processador
    if (this.tipoProduto === 'processadores') {
      const detalhesProcessador = this.processadorFormService.getProcessadorFormValue();
      // Ajuntando os detalhes do produto e do processador
      Object.assign(detalhesProduto, detalhesProcessador);


      this.produtoService.insert(detalhesProduto, this.tipoProduto).subscribe ({
                next: (response) => {
                console.log('Resultado:', response);
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



    // const dadosProduto = {
    //   tipoProduto: this.tipoProduto,
    //   dadosFormulario: {
    //     id: 0,
    //     nome: this.produtoForm.value.nome,
    //     modelo: this.produtoForm.value.modelo,
    //     marca: this.produtoForm.value.marca,
    //     descricao: this.produtoForm.value.descricao,
    //     codigoBarras: this.produtoForm.value.codigoBarras,
    //     altura: this.produtoForm.value.altura,
    //     largura: this.produtoForm.value.largura,
    //     comprimento: this.produtoForm.value.comprimento,
    //     peso: this.produtoForm.value.peso,
    //     valorVenda: this.produtoForm.value.valorVenda,
    //     quantidadeUnidades: this.produtoForm.value.quantidade,
    //     quantidadeNaoConvencional: this.produtoForm.value.quantidadeNaoConvencional,
    //     unidadeDeMedida: this.produtoForm.value.unidadeDeMedida,
    //     classificacao: this.produtoForm.value.classificacao,
    //     nomeImagem: this.produtoForm.value.nomeImagem,
    // }
  //}

  // this.produtoService.salvarProduto(dadosProduto);

  // }

//     if(this.tipoProduto === 'Processador') {
//       const novoProduto: Processador = {
//         id: 0,
//         nome: this.produtoForm.value.nome,
//         modelo: this.produtoForm.value.modelo,
//         marca: this.produtoForm.value.marca,
//         descricao: this.produtoForm.value.descricao,
//         codigoBarras: this.produtoForm.value.codigoBarras,
//         altura: this.produtoForm.value.altura,
//         largura: this.produtoForm.value.largura,
//         comprimento: this.produtoForm.value.comprimento,
//         peso: this.produtoForm.value.peso,
//         valorVenda: this.produtoForm.value.valorVenda,
//         quantidadeUnidades: this.produtoForm.value.quantidade,
//         quantidadeNaoConvencional: this.produtoForm.value.quantidadeNaoConvencional,
//         unidadeDeMedida: this.produtoForm.value.unidadeDeMedida,
//         classificacao: this.produtoForm.value.classificacao,
//         nomeImagem: this.produtoForm.value.nomeImagem,
//         soquete: this.processadorForm.value.soquete[0],
//         pistas: this.processadorForm.value.pistas[0],
//         bloqueado: this.processadorForm.value.bloqueado[0],
//         compatibilidadeChipset: this.processadorForm.value.compatibilidadeChipset[0],
//         canaisMemoria: this.processadorForm.value.canaisMemoria[0],
//         capacidadeMaxMemoria: this.processadorForm.value.capacidadeMaxMemoria[0],
//         pontenciaBase: this.processadorForm.value.pontenciaBase[0],
//         potenciaMaxima: this.processadorForm.value.potenciaMaxima[0],
//         frequenciaBase: this.processadorForm.value.frequenciaBase[0],
//         frequenciaMaxima: this.processadorForm.value.frequenciaMaxima[0],
//         tamanhoCacheL3: this.processadorForm.value.tamanhoCacheL3[0],
//         tamanhoCacheL2: this.processadorForm.value.tamanhoCacheL2[0],
//         numNucleosFisicos: this.processadorForm.value.numNucleosFisicos[0],
//         numThreads: this.processadorForm.value.numThreads[0],
//         velMaxMemoria: this.processadorForm.value.velMaxMemoria[0],
//         conteudoEmbalagem: this.processadorForm.value.conteudoEmbalagem[0],      
//       };
    
//       this.salvar(novoProduto);
//     }

//     if(this.tipoProduto === 'Placas Mãe') {
//       const novoProduto: PlacaMae = {
//         nome: this.produtoForm.value.nome,
//         modelo: this.produtoForm.value.modelo,
//         marca: this.produtoForm.value.marca,
//         descricao: this.produtoForm.value.descricao,
//         codigoBarras: this.produtoForm.value.codigoBarras,
//         altura: this.produtoForm.value.altura,
//         largura: this.produtoForm.value.largura,
//         comprimento: this.produtoForm.value.comprimento,
//         peso: this.produtoForm.value.peso,
//         valorVenda: this.produtoForm.value.valorVenda,
//         quantidadeUnidades: this.produtoForm.value.quantidade,
//         quantidadeNaoConvencional: this.produtoForm.value.quantidadeNaoConvencional,
//         unidadeDeMedida: this.produtoForm.value.unidadeDeMedida,
//         classificacao: this.produtoForm.value.classificacao,
//         nomeImagem: this.produtoForm.value.nomeImagem,
//         id: 0,
//     };
//     this.salvar(novoProduto);
//     }
//   }
    
//     salvar(novoProduto: Produto) {
//     this.produtoService.insert(novoProduto).subscribe ({
//         next: (response) => {
//         console.log('Resultado:', response);
//         this.produtoService.notificarProdutoInserido(); // Notificar outros componentes
//         },
//         error: (error) => {
//           // Este callback é executado quando ocorre um erro durante a emissão do valor
//           console.error('Erro:', error);
//           window.alert(error);
//         }
//     });
// }
cancelarInsercao(): void {
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateBack();
}
}












