import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Classificacao, PlacaMae, Processador, Produto } from '../../../../models/produto.model';
import { ProdutoService } from '../../../../services/produto.service';
import { NgFor, CommonModule, AsyncPipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { PageEvent } from '@angular/material/paginator';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, of } from 'rxjs';
import { startWith, map, catchError, toArray } from 'rxjs/operators';
import { NavigationService } from '../../../../services/navigation.service';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
    selector: 'app-fornecedor-list',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule,
        MatFormFieldModule, MatInputModule, ReactiveFormsModule, AsyncPipe, MatTooltipModule],
    templateUrl: './produto-escolhido.component.html',
    styleUrl: './produto-escolhido.component.css'
})

export class ProdutoEscolhidoComponent implements OnInit {
    dataSource: Produto[] | PlacaMae[] | Processador[] = [];
    todasClassificacoes: Classificacao[] = [];
    produtoId: number = 0;

    // Variável relacionada com as colunas da página html
    displayedColumns: string[] = ['id', 'descricao', 'classificacao', 'modelo', 'marca', 'quantidade', 'valorVenda', 'acao'];

    constructor(private produtoService: ProdutoService,
        private router: Router, private route: ActivatedRoute,
        private navigationService: NavigationService) { }

    ngOnInit() {
        this.route.params.subscribe(params => {
            this.produtoId = params['id'];

            this.produtoService.findById(this.produtoId).subscribe({
                next: (response) => {
                    console.log(response);
                    this.dataSource = [response];
                },
                error: (error) => {
                    console.error('Erro ao carregar detalhes do produto:', error);
                    // Tratar o erro conforme necessário
                }
            });
        });

        // Implementando o buscador para classificacao
        this.produtoService.getClassificacao().subscribe({
            next: (todasClassificacoes: Classificacao[]) => {
                this.todasClassificacoes = todasClassificacoes;
            },
            error: (error) => {
                console.error('Erro ao carregar produtos:', error);
            }
        });
    }
    // Método para apagar um produto escolhido
    apagarProduto(id: number): void {
        this.produtoService.delete(id).subscribe({
            next: (response) => {
                this.produtoService.notificarProdutoInserido();
            },
            error: (error) => {
                console.error(error);
                window.alert(error); // Exibe a mensagem de erro usando window.alert()
            }
        });
    }

    // Método para chamar o endpoint para edição de um Produto escolhido
    editarProduto(id: number): void {
        const enderecoEdicao: string = "produtos/edit/" + id.toString();
        this.navigationService.navigateTo(enderecoEdicao);
    }

    listarLote(id: number): void {
        const listaLote: string = "lotes/" + id.toString();
        this.navigationService.navigateTo(listaLote);
    }

    criarLote(): void {
        const criaLote: string = "lotes/new";
        this.navigationService.navigateTo(criaLote);
    }

    produtoDefeito(): void {
        const produtoDefeito: string = "produtos/garantia/" + this.produtoId;
        this.navigationService.navigateTo(produtoDefeito);
    }

}
