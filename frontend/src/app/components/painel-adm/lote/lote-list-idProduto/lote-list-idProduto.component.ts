import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Produto } from '../../../../models/produto.model';
import { ProdutoService } from '../../../../services/produto.service';
import { NgFor, CommonModule, AsyncPipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { PageEvent } from '@angular/material/paginator';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, of } from 'rxjs';
import { startWith, map, catchError, toArray } from 'rxjs/operators';
import { NavigationService } from '../../../../services/navigation.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SessionTokenService } from '../../../../services/session-token.service';
import { Fornecedor } from '../../../../models/fornecedor.model';
import { FornecedorService } from '../../../../services/fornecedor.service';
import { LoteService } from '../../../../services/lote.service';
import { LoteRecebe } from '../../../../models/loteRecebe.model';
import { MatTooltipModule } from '@angular/material/tooltip';
import { LoteRecebeClass } from '../../../../models/loteRecebeClass.model';
import { LoteTesteService } from '../../../../services/loteTeste.service';

@Component({
    selector: 'app-lote-list-idProduto',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule,
        MatFormFieldModule, MatInputModule, ReactiveFormsModule, AsyncPipe, MatTooltipModule],
    templateUrl: './lote-list-idProduto.component.html',
    styleUrl: './lote-list-idProduto.component.css'
})

export class LoteListComponent implements OnInit {
    id: number = 0;
    produto: Produto = new Produto();
    fornecedores: Fornecedor[] = [];
    statusDoLote: any[] = [];
    isBotaoHabilitado: boolean = false;

    // variaveis de controle de paginacao
    totalRecords = 0;
    page = 0;
    pageSize = 0;
    totalPages = 0;
    lotes: LoteRecebe[] = [];
    lotesTeste: LoteRecebeClass[] = [];

    // Variável relacionada com as colunas da página html
    displayedColumns: string[] = ['id', 'lote', 'descricaoStatusDoLote', 'cnpjFornecedor', 'modeloProduto', 'quantidadeUnidades', 'custoCompra', 'valorVenda', 'garantiaMeses', 'dataHoraChegadaLote', 'dataHoraAtivacaoLote', 'dataHoraUltimoVendido', 'acao'];

    constructor(private fornecedorService: FornecedorService,
        private produtoService: ProdutoService,
        private route: ActivatedRoute,
        private navigationService: NavigationService,
        private http: HttpClient,
        private sessionTokenService: SessionTokenService,
        private loteService: LoteService,
        private loteTesteService: LoteTesteService,
    ) {
        this.statusDoLote = [];
    }

    ngOnInit(): void {
        // Este foi o menor número que definimos no arquivo html
        this.pageSize = 2;

        this.id = Number(this.route.snapshot.params['id']);
        console.log(this.id);
        
        // this.carregarProduto();
        // this.carregarFornecedor();
        // this.carregarStatus();

        // Atualizando os dados da página de acordo com a paginação ao carregar a página.
        //this.atualizarDadosDaPagina();
        this.atualizarDadosDaPaginaTeste();
    }

    onChange(event:any): void{
        const value = event.target.value;
        console.log(value);
        this.paginar({ pageIndex: 0, pageSize: parseInt(value), length: this.totalPages }); 
    }
   

    //   carregarProduto() {
    //     this.produtoService.findById(this.id).subscribe({
    //         next: (produto: Produto) => {
    //             this.produto = produto;
    //         },
    //         error:(error) => {
    //             console.error(error);
    //         }
    //     });
    //   }

    //   carregarFornecedor() {
    //     this.fornecedorService.findTodos().subscribe({
    //         next: (fornecedor: Fornecedor[]) => {
    //             this.fornecedores = fornecedor;
    //         },
    //         error: (error) => {
    //             console.log(error);
    //         }
    //     });
    //   }

    //   carregarStatus() {
    //     this.loteService.getStatus().subscribe(data => {
    //             this.statusDoLote = data;
    //     });
    //   }

        // Verifica se o status é 'ativo' ou 'desativado'
    //     return this.statusDoLote.every(status => status.descricao !== 'Lote já foi finalizado' && status.descricao !== 'Lote ativo no momento');
    //   }

    // Método para paginar os resultados
    paginar(event: PageEvent): void {
        this.page = event.pageIndex;
        this.pageSize = event.pageSize;
        this.atualizarDadosDaPaginaTeste();
    }

    // Método para paginar os resultados
    atualizarDadosDaPagina(): void {
        this.carregarLotes(this.page, this.pageSize);
        this.loteService.count(this.id).subscribe(data => {
            this.totalRecords = data;
            this.totalPages = Math.round(this.totalRecords/this.pageSize);
        });
        console.log(this.lotes);
    }

    atualizarDadosDaPaginaTeste():void{
        this.carregarLotesEnvia(this.page, this.pageSize);
        this.loteService.count(this.id).subscribe(data => {
            this.totalRecords = data;
            this.totalPages = Math.round(this.totalRecords/this.pageSize);
            if(this.totalPages < 1)
                this.totalPages = 1;
            console.log(this.totalPages);
        })
        console.log(this.carregarLotesEnvia);
    }
    // Método para paginar os resultados
    carregarLotes(page: number, pageSize: number): void {
        this.loteService.findByIdProduto(this.id, page, pageSize).subscribe({
            next: (response) => {
                this.lotes = response;
                console.log(this.lotes);
            },
            error: (error) => {
                // Este callback é executado quando ocorre um erro durante a emissão do valor
                console.error('Erro:', error.message);
                window.alert(error);
            }
        });
        this.totalPages = Math.round(this.totalRecords/this.pageSize);
    }

    carregarLotesEnvia(page: number, pageSize: number): void {
        this.loteService.findByIdProdutoEnviado(this.id, page, pageSize).subscribe({
            next: (response) => {
                this.lotesTeste = response;
                console.log(this.lotesTeste);
            },
            error: (error) => {
                // Este callback é executado quando ocorre um erro durante a emissão do valor
                console.error('Erro:', error.message);
                window.alert(error);
            }
        });
        console.log(this.lotesTeste);
        this.totalPages = Math.round(this.totalRecords/this.pageSize);
    }

ativarLote(id: number): void {
    this.loteService.ativaLote(id).subscribe({
        next: (response) => {
            console.log(response);
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
            console.error('Erro:', error.message);
            window.alert(error); 
        }
    });

}
}