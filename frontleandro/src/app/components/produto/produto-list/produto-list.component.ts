import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Produto } from '../../../models/Produto.model';
import { ProdutoService } from '../../../services/produto.service';
import { NgFor, CommonModule, AsyncPipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { PageEvent } from '@angular/material/paginator';
import { MatPaginatorModule } from '@angular/material/paginator';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, of } from 'rxjs';
import { startWith, map, catchError, toArray } from 'rxjs/operators';
import { NavigationService } from '../../../services/navigation.service';

@Component({
    selector: 'app-fornecedor-list',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule,
    MatFormFieldModule, MatInputModule, ReactiveFormsModule, AsyncPipe,],
    templateUrl: './produto-list.component.html',
    styleUrl: './produto-list.component.css'
})

export class ProdutoListComponent implements OnInit {
    // Variáveis relacionadas com a caixa de busca
    myControl = new FormControl('');
    filteredOptions: Observable<Produto[]>;
    todosProdutos: Produto[] = [];
    // variaveis de controle de paginacao
    totalRecords = 0;
    page = 0;
    pageSize = 0;
    produtos: Produto[] = [];

    // Variável relacionada com as colunas da página html
    displayedColumns: string[] = ['id', 'descricao', 'classificacao', 'modelo', 'marca', 'quantidade', 'valorVenda', 'acao'];

    constructor(private produtoService: ProdutoService,
        private router: Router, private route: ActivatedRoute,
        private navigationService: NavigationService) {

          // Implementando o buscador para fornecedor
          this.filteredOptions = this.myControl.valueChanges.pipe(
            startWith(''),
            map(value => typeof value === 'string' ? value : value ? (value as Produto).nome : ''),
            map(cnpj => cnpj ? this._filter(cnpj) : this.todosProdutos.slice())
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
        console.log(produto);
        this.router.navigate(['/produtos', produto.id]);
      }
  
      ngOnInit() {

        // Este foi o menor número que definimos no arquivo html
        this.pageSize = 2; 
        // Implementando o buscador para produtos
        this.produtoService.findTodos().subscribe({
          next: (todosProdutos: Produto[]) => {
            this.todosProdutos = todosProdutos;
          },
          error: (error) => {
            console.error('Erro ao carregar produtos:', error);
          }
        });

        // Atualizando os dados da página de acordo com a paginação ao carregar a página.
        this.atualizarDadosDaPagina();
  
        // Inscrevendo para receber notificações de novos produtos
        this.produtoService.produtoInserido$.subscribe(() => {
            // Recarrega os produtos ao receber uma notificação
          this.carregarProdutos(this.page, this.pageSize); 
          this.router.navigate(['produtos']);
        });
      }
  
// Método para paginar os resultados
paginar(event: PageEvent) : void {
    this.page = event.pageIndex;
    this.pageSize = event.pageSize;
    this.atualizarDadosDaPagina();
  }

  // Método para paginar os resultados
  atualizarDadosDaPagina(): void {
    this.carregarProdutos(this.page, this.pageSize);
    this.produtoService.count().subscribe(data => {
      this.totalRecords = data;
      console.log(this.totalRecords);
    });
  }

  // Método para paginar os resultados
  carregarProdutos(page: number, pageSize: number): void {
    this.produtoService.findAll(this.page, this.pageSize).subscribe({
        next: (response) => {
            console.log('Resultado:', response);
            this.produtos = response;
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
            console.error('Erro:', error);
            window.alert(error);
        } 
    });
}

 // Método para apagar um produto escolhido
 apagarProduto(id: number): void {
    this.produtoService.delete(id).subscribe({
      next:  (response) => {
            this.produtoService.notificarProdutoInserido();
        },
        error: (error) => {
        console.error(error);
        window.alert(error); // Exibe a mensagem de erro usando window.alert()
        }
    });
}

 // Método para chamar o endpoint para edição de um Fornecedor escolhido
 editarProduto(id: number): void {
    const enderecoEdicao: string = "produtos/edit/" + id.toString();
    this.navigationService.navigateTo(enderecoEdicao);
}

// Método para chamar o endpoint para inserção de novo Fornecedor
inserirProduto(): void {
  const enderecoEdicao: string = "produtos/new";
  this.navigationService.navigateTo(enderecoEdicao);
}

}
