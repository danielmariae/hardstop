import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators, FormControl, FormsModule, ValidationErrors } from '@angular/forms';
import { LoteService } from '../../../services/lote.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NavigationService } from '../../../services/navigation.service';
import { Produto } from '../../../models/produto.model';
import { ProdutoService } from '../../../services/produto.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SessionTokenService } from '../../../services/session-token.service';
import { Observable } from 'rxjs';
import { Fornecedor } from '../../../models/fornecedor.model';
import { FornecedorService } from '../../../services/fornecedor.service';
import { startWith, map, catchError, toArray } from 'rxjs/operators';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from "@angular/router";
import { NgFor, CommonModule, AsyncPipe, DatePipe } from '@angular/common';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LoteEnvia } from '../../../models/loteEnvia.model';



@Component({
  selector: 'app-lote-form',
  standalone: true,
  imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule,
    MatFormFieldModule, MatInputModule, ReactiveFormsModule, AsyncPipe],
  templateUrl: './lote-form.component.html',
  styleUrl: './lote-form.component.css'
})

export class LoteFormComponent implements OnInit {

  @Input() lote: LoteEnvia = new LoteEnvia();
  loteForm: FormGroup;
  fornecedor: Fornecedor = new Fornecedor();
  produto: Produto = new Produto();
  value1!: number;
  value2!: string;



  // Variáveis relacionadas com a caixa de busca
  myControl = new FormControl('');
  filteredOptions: Observable<Produto[]>;
  todosProdutos: Produto[] = [];


  myControlClass = new FormControl('');
  filteredOptionsClass: Observable<Fornecedor[]>;
  todosFornecedores: Fornecedor[] = [];
  enviarQuantidadeUnidades!: boolean;

  constructor(private produtoService: ProdutoService,
    private router: Router, private route: ActivatedRoute,
    private navigationService: NavigationService,
    private fornecedorService: FornecedorService,
    private formBuilder: FormBuilder,
    private loteService: LoteService) {

    this.loteForm = this.formBuilder.group({
      lote: [null],
      dataHoraChegadaLote: [''],
      fornecedor: [null],
      produto: [null],
      quantidadeUnidades: [null, Validators.required],
      quantidadeNaoConvencional: [null, Validators.required],
      unidadeDeMedida: [null, Validators.required],
      custoCompra: [null],
      valorVenda: [null],
      garantiaMeses: [null],
      status: [null]
    });

    this.adicionarObservadores();
    //this.atualizaCampos();

    // Implementando o buscador para produto
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => typeof value === 'string' ? value : value ? (value as Produto).nome : ''),
      map(cnpj => cnpj ? this._filter(cnpj) : this.todosProdutos.slice())
    );

    // Implementando o buscador para fornecedor
    this.filteredOptionsClass = this.myControlClass.valueChanges.pipe(
      startWith(''),
      map(value => typeof value === 'string' ? value : value ? (value as Fornecedor).cnpj : ''),
      map(nome => nome ? this.forn_filter(nome) : this.todosFornecedores.slice())
    );
  }

  // Implementando o buscador para produto
  private _filter(value: string): Produto[] {
    const filterValue = value.toLowerCase();
    return this.todosProdutos.filter(option => option.nome.toLowerCase().includes(filterValue));
  }

  // Implementando o buscador para produto
  displayFn(produto: Produto): string {
    return produto && produto.nome ? produto.nome : '';
  }

  // Implementando o buscador para produto
  // Enviando o produto selecionado para uma página onde somente ele aparece
  selecionarProduto(produto: Produto) {
    this.produto = produto;
    // this.router.navigate(['/produtos', produto.id]);
  }

  // Implementando o buscador para fornecedor
  private forn_filter(value: string): Fornecedor[] {
    const filterValue = value.toLowerCase();
    return this.todosFornecedores.filter(option => option.cnpj.toLowerCase().includes(filterValue));
  }

  // Implementando o buscador para fornecedor
  displayClass(fornecedor: Fornecedor): string {
    return fornecedor && fornecedor.cnpj ? fornecedor.cnpj : '';
  }

  // Implementando o buscador para fornecedor
  // Enviando o fornecedor selecionado para uma página onde somente ele aparece
  selecionarFornecedor(fornecedor: Fornecedor) {
    this.fornecedor = fornecedor;
    //
  }

  ngOnInit(): void {

    

    // Implementando o buscador para produtos
    this.buscarTodosProdutos();

    // Implementando o buscador para fornecedores
    this.buscarTodosFornecedores();

  }

  // Garante que ao escrever no campo quantidadeUnidades, os campos quantidadeNaoConvencional e unidadeDeMedida não poderão ser acessados e vice-versa
  private adicionarObservadores(): void {
    this.loteForm.get('quantidadeUnidades')?.valueChanges.subscribe(value => {
      if (value) {
        console.log(value);
        this.loteForm.get('quantidadeNaoConvencional')?.disable();
        this.loteForm.get('unidadeDeMedida')?.disable();
      } else {
        this.loteForm.get('quantidadeNaoConvencional')?.enable();
        this.loteForm.get('unidadeDeMedida')?.enable();
      }
    });

    this.loteForm.get('quantidadeNaoConvencional')?.valueChanges.subscribe(value1 => {
      this.value1 = value1;
  });


  this.loteForm.get('unidadeDeMedida')?.valueChanges.subscribe(value2 => {
    this.value2 = value2;
   
  });

  }

   // Garante que ao escrever no campo quantidadeUnidades, os campos quantidadeNaoConvencional e unidadeDeMedida não poderão ser acessados e vice-versa. O restante desta implementação encontr-se no arquivo html com o uso da função do método (ngModelChange)
  atualizaCampos() {
    if(this.value1 || this.value2) {
      this.loteForm.get('quantidadeUnidades')?.disable();
    } else if(this.value1 == null && (this.value2 === null || this.value2 === undefined || this.value2 === '')) {
      this.loteForm.get('quantidadeUnidades')?.enable();
    }
    }





  // Buscando todos os produtos para carregar na lista de buscador de produtos
  buscarTodosProdutos(): void {
    this.produtoService.findTodos().subscribe({
      next: (todosProdutos: Produto[]) => {
        this.todosProdutos = todosProdutos;
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
      }
    });
  }

  // Buscando todos os fornecedores para carregar na lista de buscador de fornecedores
  buscarTodosFornecedores(): void {
    this.fornecedorService.findAll().subscribe({
      next: (todosFornecedores: Fornecedor[]) => {
        this.todosFornecedores = todosFornecedores;
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
      }
    });
  }

  //   salvarLote(): void {
  //     if (this.loteForm.invalid) {
  //       return;
  //     }

  //     const data = new Date(this.loteForm.value.dataHoraChegadaLote);
  //     const dataPipe = new DatePipe('en-US');
  //     const dataEnviada = dataPipe.transform(data, 'yyyy-MM-dd HH:mm:ss');

  //     console.log(dataEnviada);
  //     const detalhesLote: LoteEnvia = {
  //       id: this.loteForm.value.id,
  //       lote: this.loteForm.value.lote,
  //       dataHoraChegadaLote: dataEnviada,
  //       fornecedor: this.fornecedor,
  //       produto: this.produto,
  //       quantidadeUnidades: this.loteForm.value.quantidadeUnidades,
  //       quantidadeNaoConvencional: this.loteForm.value.quantidadeNaoConvencional,
  //       unidadeDeMedida: this.loteForm.value.unidadeDeMedida,
  //       custoCompra: this.loteForm.value.custoCompra,
  //       valorVenda: this.loteForm.value.valorVenda,
  //       garantiaMeses: this.loteForm.value.garantiaMeses,
  //       status: this.loteForm.value.status, // Todo Lote recém cadastrado tem status 2 (EM ESPERA) por default. Esse valor será atribuído no backend. Aqui irá como null.
  //     }
  // console.log(detalhesLote);
  //       this.loteService.insert(detalhesLote).subscribe ({
  //                 next: (response) => {
  //                 },
  //                 error: (error) => {
  //                   console.error('Erro:', error);
  //                   window.alert(error);
  //                 }
  //             });
  //   }

  salvarLote() {
    console.log(this.loteForm.errors);
    console.log(this.loteForm);
    if (this.loteForm.invalid) {
      return;
    }
    console.log("TESTE2");

    const data = new Date(this.loteForm.value.dataHoraChegadaLote);
    const dataPipe = new DatePipe('en-US');
    const dataEnviada = dataPipe.transform(data, 'yyyy-MM-dd HH:mm:ss');

    const detalhesLote: LoteEnvia = {
      id: this.loteForm.value.id,
      lote: this.loteForm.value.lote,
      dataHoraChegadaLote: dataEnviada,
      fornecedor: this.fornecedor,
      produto: this.produto,
      quantidadeUnidades: this.loteForm.value.quantidadeUnidades,
      quantidadeNaoConvencional: this.loteForm.value.quantidadeNaoConvencional,
      unidadeDeMedida: this.loteForm.value.unidadeDeMedida,
      custoCompra: this.loteForm.value.custoCompra,
      valorVenda: this.loteForm.value.valorVenda,
      garantiaMeses: this.loteForm.value.garantiaMeses,
      status: this.loteForm.value.status, // Todo Lote recém cadastrado tem status 2 (EM ESPERA) por default. Esse valor será atribuído no backend. Aqui irá como null.
    };

    console.log(detalhesLote);

    this.loteService.insert(detalhesLote)
      .subscribe({
        next: (response) => {
          // Exibir mensagem de sucesso (opcional)
          console.log('Lote salvo com sucesso!');
          // Redirecionar para outra página (opcional)
        },
        error: (error) => {
          console.error('Erro ao salvar o lote:', error);
          window.alert('Erro ao salvar o lote. Tente novamente mais tarde.');
        }
      });
  }






  cancelarInsercao(): void {
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateBack();
  }


}