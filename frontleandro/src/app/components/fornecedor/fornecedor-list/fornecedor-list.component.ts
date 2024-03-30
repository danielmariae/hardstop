import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Fornecedor } from "../../../models/fornecedor.model";
import { FornecedorService } from "../../../services/fornecedor.service";
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
    templateUrl: './fornecedor-list.component.html',
    styleUrl: './fornecedor-list.component.css'
})

export class FornecedorListComponent implements OnInit {
// Variáveis relacionadas com a caixa de busca
  myControl = new FormControl('');
  filteredOptions: Observable<Fornecedor[]>;
todosFornecedores: Fornecedor[] = [];
    // variaveis de controle de paginacao
    totalRecords = 0;
    page = 0;
    pageSize = 0;
fornecedores: Fornecedor[] = [];
// Variável relacionada com as colunas da página html
    displayedColumns: string[] = ['id', 'nomeFantasia', 'cnpj', 'endSite', 'endereco', 'telefone', 'acao'];
    
    
    tiposTelefoneMap: Map<number, string> = new Map<number, string>();

    constructor(private fornecedorService: FornecedorService,
        private router: Router, private route: ActivatedRoute,
        private navigationService: NavigationService) {

          // Implementando o buscador para fornecedor
          this.filteredOptions = this.myControl.valueChanges.pipe(
            startWith(''),
            map(value => typeof value === 'string' ? value : value ? (value as Fornecedor).cnpj : ''),
            map(cnpj => cnpj ? this._filter(cnpj) : this.todosFornecedores.slice())
          );
    }

    // Implementando o buscador para fornecedor
    private _filter(value: string): Fornecedor[] {
      const filterValue = value.toLowerCase();
      return this.todosFornecedores.filter(option => option.cnpj.toLowerCase().includes(filterValue));
    }
  
    // Implementando o buscador para fornecedor
    displayFn(fornecedor: Fornecedor): string {
      return fornecedor && fornecedor.nomeFantasia ? fornecedor.nomeFantasia : '';
    }

    // Implementando o buscador para fornecedor
    // Enviando o fornecedor selecionado para uma página onde somente ele aparece
    selecionarFornecedor(fornecedor: Fornecedor) {
      console.log(fornecedor);
      this.router.navigate(['/fornecedores', fornecedor.id]);
    }

    ngOnInit() {
      // Carregando o Enum Tipos de Telfone para realizar mapeamento entre número de id do objeto Telefone e sua descrição no Enum para apresentar a descrição na página html
      this.carregarTiposTelefone();
      // Este foi o menor número que definimos no arquivo html
      this.pageSize = 2; 
      // Implementando o buscador para fornecedor
      this.fornecedorService.findTodos().subscribe({
        next: (todosFornecedores: Fornecedor[]) => {
          this.todosFornecedores = todosFornecedores;
        },
        error: (error) => {
          console.error('Erro ao carregar fornecedores:', error);
        }
      });

      // Atualizando os dados da página de acordo com a paginação ao carregar a página.
      this.atualizarDadosDaPagina();

      // Inscrevendo para receber notificações de novos fornecedores
      this.fornecedorService.fornecedorInserido$.subscribe(() => {
        this.carregarFornecedores(this.page, this.pageSize); // Recarrega os fornecedores ao receber uma notificação
        this.router.navigate(['fornecedores']);
      });
    }

     // Método para carregar um conversor de id para descrição de Telefone no html
     carregarTiposTelefone() {
      this.fornecedorService.getTipoTelefone().subscribe({
        next: (tiposTelefone) => {
          // Mapear os tipos de telefone para o mapa
          tiposTelefone.forEach(tipo => {
            this.tiposTelefoneMap.set(tipo.id, tipo.descricao);
          });
        },
        error: (error) => {
          console.error('Erro ao carregar tipos de telefone:', error);
        }
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
      this.carregarFornecedores(this.page, this.pageSize);
      this.fornecedorService.count().subscribe(data => {
        this.totalRecords = data;
        console.log(this.totalRecords);
      });
    }

  // Método para paginar os resultados
    carregarFornecedores(page: number, pageSize: number): void {
        this.fornecedorService.findAll(this.page, this.pageSize).subscribe({
            next: (response) => {
                console.log('Resultado:', response);
                this.fornecedores = response;
            },
            error: (error) => {
                // Este callback é executado quando ocorre um erro durante a emissão do valor
                console.error('Erro:', error);
                window.alert(error);
            } 

        });
    }

    // Método para apagar um fornecedor escolhido
    apagarFornecedor(id: number): void {
        this.fornecedorService.delete(id).subscribe({
          next:  (response) => {
                this.fornecedorService.notificarFornecedorInserido();
            },
            error: (error) => {
            console.error(error);
            window.alert(error); // Exibe a mensagem de erro usando window.alert()
            }
        });
    }

    // Método para chamar o endpoint para edição de um Fornecedor escolhido
    editarFornecedor(id: number): void {
      const enderecoEdicao: string = "fornecedores/edit/" + id.toString();
      this.navigationService.navigateTo(enderecoEdicao);
  }

  // Método para chamar o endpoint para inserção de novo Fornecedor
  inserirFornecedor(): void {
    const enderecoEdicao: string = "fornecedores/new";
    this.navigationService.navigateTo(enderecoEdicao);
}

}