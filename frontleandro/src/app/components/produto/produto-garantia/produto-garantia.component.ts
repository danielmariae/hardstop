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
import { RouterModule } from '@angular/router';
import { NgFor, CommonModule, AsyncPipe, DatePipe } from '@angular/common';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LoteEnvia } from '../../../models/loteEnvia.model';


@Component({
    selector: 'app-produto-garantia',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule,
        MatFormFieldModule, MatInputModule, ReactiveFormsModule, AsyncPipe],
    templateUrl: './produto-garantia.component.html',
    styleUrl: './produto-garantia.component.css'
})

export class ProdutoGarantiaComponent implements OnInit {
    produtoForm: FormGroup;
    produto: Produto = new Produto();
    produtoId: number = 0;


    // Variáveis relacionadas com a caixa de busca
    // myControl = new FormControl('');
    // filteredOptions: Observable<Produto[]>;
    // todosProdutos: Produto[] = [];


    constructor(private formBuilder: FormBuilder,
        private produtoService: ProdutoService,
        private navigationService: NavigationService,
        private route: ActivatedRoute) {
        this.produtoForm = formBuilder.group({
            dataHoraVenda: [''],
        })


        // Implementando o buscador para produto
    //     this.filteredOptions = this.myControl.valueChanges.pipe(
    //         startWith(''),
    //         map(value => typeof value === 'string' ? value : value ? (value as Produto).nome : ''),
    //         map(cnpj => cnpj ? this._filter(cnpj) : this.todosProdutos.slice())
    //     );
     }

    // Implementando o buscador para produto
    // private _filter(value: string): Produto[] {
    //     const filterValue = value.toLowerCase();
    //     return this.todosProdutos.filter(option => option.nome.toLowerCase().includes(filterValue));
    // }

    // Implementando o buscador para produto
    // displayFn(produto: Produto): string {
    //     return produto && produto.nome ? produto.nome : '';
    // }

    // Implementando o buscador para produto
    // Enviando o produto selecionado para uma página onde somente ele aparece
    // selecionarProduto(produto: Produto) {
    //     this.produto = produto;
    // }

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            this.produtoId = params['id'];
        });
        // Implementando o buscador para produtos
        // this.buscarTodosProdutos();

    }

    // Buscando todos os produtos para carregar na lista de buscador de produtos
    // buscarTodosProdutos(): void {
    //     this.produtoService.findTodos().subscribe({
    //         next: (todosProdutos: Produto[]) => {
    //             this.todosProdutos = todosProdutos;
    //         },
    //         error: (error) => {
    //             console.error('Erro ao carregar produtos:', error);
    //         }
    //     });
    // }

    enviarDados() {

        const data = new Date(this.produtoForm.value.dataHoraVenda);
        const dataPipe = new DatePipe('en-US');
        const dataVenda = dataPipe.transform(data, 'yyyy-MM-dd HH:mm:ss');

        if (dataVenda) {

            this.produtoService.findProdutoEstragado(this.produtoId, dataVenda).subscribe({
                next: (response: Fornecedor) => {
                    // Exibir mensagem de sucesso (opcional)
                       // Redirecionar para a página anterior
                       const fornecedorCulpado: string = "fornecedores/" + response.id;
                       this.navigationService.navigateTo(fornecedorCulpado);
                },
                error: (error) => {
                    console.error('Erro ao salvar o lote:', error);
                    window.alert('Erro ao salvar o lote. Tente novamente mais tarde.');
                }
            });
        }
    }
    cancelarInsercao(): void {
        // Redireciona o usuário para a rota anterior
        this.navigationService.navigateBack();
      }
}

