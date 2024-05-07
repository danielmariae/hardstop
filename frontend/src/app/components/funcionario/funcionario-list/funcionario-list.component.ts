import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Funcionario } from '../../../models/funcionario.model';
import { FuncionarioService } from '../../../services/funcionario.service';
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
    selector: 'app-funcionario-list',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule,
    MatFormFieldModule, MatInputModule, ReactiveFormsModule, AsyncPipe,],
    templateUrl: './funcionario-list.component.html',
    styleUrl: './funcionario-list.component.css'
})

export class FuncionarioListComponent implements OnInit {

    // Variáveis relacionadas com a caixa de busca
    myControl = new FormControl('');
    filteredOptions: Observable<Funcionario[]>;
    todosFuncionarios: Funcionario[] = [];
    filteredFuncionarios: Funcionario[] = [];

    myControlClass = new FormControl('');

    // variaveis de controle de paginacao
    totalRecords = 0;
    page = 0;
    pageSize = 0;
    totalPages = 0;
    funcionarios: Funcionario[] = [];
    searchText: string = '';

    // Variável relacionada com as colunas da página html
    // displayedColumns: string[] = ['id', 'descricao', 'classificacao', 'modelo', 'marca', 'quantidade', 'valorVenda', 'acao'];

    constructor(private funcionarioService: FuncionarioService,
        private router: Router, private route: ActivatedRoute,
        private navigationService: NavigationService) {

          // Implementando o buscador para funcionario
          this.filteredOptions = this.myControl.valueChanges.pipe(
            startWith(''),
            map(value => typeof value === 'string' ? value : value ? (value as Funcionario).nome : ''),
            map(cpf => cpf ? this._filter(cpf) : this.todosFuncionarios.slice())
          );

          // // Implementando o buscador para classificacao
          // this.filteredOptionsClass = this.myControlClass.valueChanges.pipe(
          //   startWith(''),
          //   map(value => typeof value === 'string' ? value : value ? (value as Classificacao).nome : ''),
          //   map(nome => nome ? this.class_filter(nome) : this.todasClassificacoes.slice())
          // );

    }

    // Implementando o buscador para funcionario
    private _filter(value: string): Funcionario[] {
        const filterValue = value.toLowerCase();
        return this.todosFuncionarios.filter(option => option.nome.toLowerCase().includes(filterValue));
      }
    
      // Implementando o buscador para funcionario
      displayFn(funcionario: Funcionario): string {
        return funcionario && funcionario.nome ? funcionario.nome : '';
      }
  
      // Implementando o buscador para funcionario
      // Enviando o funcionario selecionado para uma página onde somente ele aparece
      selecionarFuncionario(funcionario: Funcionario) {
        this.router.navigate(['/funcionarios', funcionario.id]);
      }
      ngOnInit() {

        // Este foi o menor número que definimos no arquivo html
        this.pageSize = 2; 
        // Implementando o buscador para funcionarios
       this.buscarTodosFuncionarios();

        // Atualizando os dados da página de acordo com a paginação ao carregar a página.
        this.atualizarDadosDaPagina();

        // Inscrevendo para receber notificações de novos funcionarios
        this.funcionarioService.funcionarioInserido$.subscribe(() => {
            // Recarrega os funcionarios ao receber uma notificação
          this.carregarFuncionarios(this.page, this.pageSize);
          this.buscarTodosFuncionarios(); 
          this.router.navigate(['funcionarios']);
        });        
      }

      buscarFuncionario(): void {
        const searchTextLowerCase = this.searchText.toLowerCase();
        if (searchTextLowerCase.trim() === '') {
          this.filteredFuncionarios = [];
      } else {
          this.filteredFuncionarios = this.todosFuncionarios.filter(funcionario =>
              funcionario.nome.toLowerCase().includes(searchTextLowerCase)
          );
        }
      }
  
// Buscando todos os funcionarios para carregar na lista de buscador de funcionarios
buscarTodosFuncionarios(): void {
  this.funcionarioService.findTodos().subscribe({
    next: (todosFuncionarios: Funcionario[]) => {
      this.todosFuncionarios = todosFuncionarios;
    },
    error: (error) => {
      console.error('Erro ao carregar funcionarios:', error);
    }
  });
}

// Método para paginar os resultados
paginar(event: PageEvent) : void {
    this.page = event.pageIndex;
    this.pageSize = event.pageSize;
    this.atualizarDadosDaPagina();
  }

  onChange(event:any): void{
    const value = event.target.value;
    console.log(value);
    this.paginar({ pageIndex: 0, pageSize: parseInt(value), length: this.totalPages }); 
}

  // Método para paginar os resultados
  atualizarDadosDaPagina(): void {
    this.carregarFuncionarios(this.page, this.pageSize);
    this.funcionarioService.count().subscribe(data => {
      this.totalRecords = data;
      this.totalPages = Math.round(this.totalRecords/this.pageSize);
      if(this.totalPages < 1){
        this.totalPages = 1;
      }
    });
    console.log(this.totalPages);
  }

  // Método para paginar os resultados
  carregarFuncionarios(page: number, pageSize: number): void {
    this.funcionarioService.findAll(this.page, this.pageSize).subscribe({
        next: (response) => {
            this.funcionarios = response;
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
inserirFuncionario(): void {
  const enderecoEdicao: string = "funcionarios/new";
  this.navigationService.navigateTo(enderecoEdicao);
}

 // Método para apagar um funcionario escolhido
 apagarFuncionario(id: number): void {
    this.funcionarioService.delete(id).subscribe({
      next:  (response) => {
            this.funcionarioService.notificarFuncionarioInserido();
        },
        error: (error) => {
        console.error(error);
        window.alert(error); // Exibe a mensagem de erro usando window.alert()
        }
    });
}

 // Método para chamar o endpoint para edição de um Funcionario escolhido
 editarFuncionario(id: number): void {
    const enderecoEdicao: string = "funcionarios/edit/" + id.toString();
    this.navigationService.navigateTo(enderecoEdicao);
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

}
