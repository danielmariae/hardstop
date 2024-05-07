import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Cliente } from '../../../../models/cliente.model';
import { ClienteService } from '../../../../services/cliente.service';
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
import { NavigationService } from '../../../../services/navigation.service';

@Component({
    selector: 'app-cliente-list',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule,
    MatFormFieldModule, MatInputModule, ReactiveFormsModule, AsyncPipe,],
    templateUrl: './cliente-list.component.html',
    styleUrl: './cliente-list.component.css'
})

export class ClienteListComponent implements OnInit {

    // Variáveis relacionadas com a caixa de busca
    myControl = new FormControl('');
    filteredOptions: Observable<Cliente[]>;
    todosClientes: Cliente[] = [];
    filteredClientes: Cliente[] = [];
    

    myControlClass = new FormControl('');

    // variaveis de controle de paginacao
    totalRecords = 0;
    page = 0;
    pageSize = 0;
    totalPages = 0;
    clientes: Cliente[] = [];
    searchText: string = '';

    // Variável relacionada com as colunas da página html
    // displayedColumns: string[] = ['id', 'descricao', 'classificacao', 'modelo', 'marca', 'quantidade', 'valorVenda', 'acao'];

    constructor(private clienteService: ClienteService,
        private router: Router, private route: ActivatedRoute,
        private navigationService: NavigationService) {

          // Implementando o buscador para cliente
          this.filteredOptions = this.myControl.valueChanges.pipe(
            startWith(''),
            map(value => typeof value === 'string' ? value : value ? (value as Cliente).nome : ''),
            map(cpf => cpf ? this._filter(cpf) : this.todosClientes.slice())
          );

          // // Implementando o buscador para classificacao
          // this.filteredOptionsClass = this.myControlClass.valueChanges.pipe(
          //   startWith(''),
          //   map(value => typeof value === 'string' ? value : value ? (value as Classificacao).nome : ''),
          //   map(nome => nome ? this.class_filter(nome) : this.todasClassificacoes.slice())
          // );

    }

    // Implementando o buscador para cliente
    private _filter(value: string): Cliente[] {
        const filterValue = value.toLowerCase();
        return this.todosClientes.filter(option => option.nome.toLowerCase().includes(filterValue));
      }
    
      // Implementando o buscador para cliente
      displayFn(cliente: Cliente): string {
        return cliente && cliente.nome ? cliente.nome : '';
      }

      buscarCliente(): void {
        const searchTextLowerCase = this.searchText.toLowerCase();
        if (searchTextLowerCase.trim() === '') {
          this.filteredClientes = [];
      } else {
          this.filteredClientes = this.todosClientes.filter(cliente =>
              cliente.nome.toLowerCase().includes(searchTextLowerCase)
          );
        }
      }
  
      // Implementando o buscador para cliente
      // Enviando o cliente selecionado para uma página onde somente ele aparece
      selecionarCliente(cliente: Cliente) {
        this.router.navigate(['/adm/clientes', cliente.id]);
      }
      ngOnInit() {

        // Este foi o menor número que definimos no arquivo html
        this.pageSize = 2; 
        // Implementando o buscador para clientes
       this.buscarTodosClientes();

        // Atualizando os dados da página de acordo com a paginação ao carregar a página.
        this.atualizarDadosDaPagina();

        // Inscrevendo para receber notificações de novos clientes
        this.clienteService.clienteInserido$.subscribe(() => {
            // Recarrega os clientes ao receber uma notificação
          this.carregarClientes(this.page, this.pageSize);
          this.buscarTodosClientes(); 
          this.router.navigate(['clientes']);
        });        
      }

      
    onChange(event:any): void{
      const value = event.target.value;
      console.log(value);
      this.paginar({ pageIndex: 0, pageSize: parseInt(value), length: this.totalPages }); 
  }
 
  
// Buscando todos os clientes para carregar na lista de buscador de clientes
buscarTodosClientes(): void {
  this.clienteService.findTodos().subscribe({
    next: (todosClientes: Cliente[]) => {
      this.todosClientes = todosClientes;
    },
    error: (error) => {
      console.error('Erro ao carregar clientes:', error);
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
    this.carregarClientes(this.page, this.pageSize);
    this.clienteService.count().subscribe(data => {
      this.totalRecords = data;
      this.totalPages = Math.round(this.totalRecords/this.pageSize);
      if(this.totalPages < 1){
        this.totalPages = 1;
      }
    });
    console.log(this.totalPages);
  }

  // Método para paginar os resultados
  carregarClientes(page: number, pageSize: number): void {
    this.clienteService.findAll(this.page, this.pageSize).subscribe({
        next: (response) => {
            this.clientes = response;
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
            console.error('Erro:', error);
            window.alert(error);
        } 
    })
    this.totalPages = Math.round(this.totalRecords/this.pageSize);
    ;
}
inserirCliente(): void {
  this.navigationService.navigateTo("/adm/clientes/new");
}

 // Método para apagar um cliente escolhido
 apagarCliente(id: number): void {
    this.clienteService.delete(id).subscribe({
      next:  (response) => {
            this.clienteService.notificarClienteInserido();
        },
        error: (error) => {
        console.error(error);
        window.alert(error); // Exibe a mensagem de erro usando window.alert()
        }
    });
}

formatarData(data: string): string {
  const partesData = data.split('-');
  return `${partesData[2]}/${partesData[1]}/${partesData[0]}`;
}

formatarSexo(sexo: string): string {
  var sexoFormatado: string = ' ';
  if(sexo === 'M'){
    var sexoFormatado = 'Masculino'
    return sexoFormatado;
  }else if (sexo === 'F'){
    var sexoFormatado = 'Feminino'
    return sexoFormatado;
  }else if (sexo === 'O'){
    var sexoFormatado = 'Outros'
    return sexoFormatado;
  }
  return sexoFormatado;
}
formatarCPF(cpf: string): string {
  // Remove todos os caracteres não numéricos
  const cpfDigits = cpf.replace(/\D/g, '');

  // Verifica se o CPF possui 11 dígitos
  if (cpfDigits.length === 11) {
    // Formata o CPF no formato desejado
    return cpfDigits.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  } else {
    // Retorna o CPF original se não possuir 11 dígitos
    return cpf;
  }
}

  // Método para chamar o endpoint para edição de um Cliente escolhido
  editarCliente(id: number): void {
    const enderecoEdicao: string = "/adm/clientes/edit/" + id.toString();
    this.navigationService.navigateTo(enderecoEdicao);
  }

  navegarParaFornecedor(): void{
    this.navigationService.navigateTo("adm/fornecedores/");
  }
  
  navegarParaFuncionario(): void{
    this.navigationService.navigateTo("adm/funcionarios/");
  } 

  navegarParaProdutos(): void{
    this.navigationService.navigateTo("adm/produtos");
  }
}
