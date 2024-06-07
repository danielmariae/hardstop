import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Produto } from '../../../../models/produto.model';
import { NgFor, CommonModule, AsyncPipe } from '@angular/common';
import { PageEvent } from '@angular/material/paginator';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Fornecedor } from '../../../../models/fornecedor.model';
import { LoteService } from '../../../../services/lote.service';
import { LoteRecebe } from '../../../../models/loteRecebe.model';
import { MatTooltipModule } from '@angular/material/tooltip';
import { LoteRecebeClass } from '../../../../models/loteRecebeClass.model';

@Component({
    selector: 'app-lote-list',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule,
        MatFormFieldModule, MatInputModule, ReactiveFormsModule, AsyncPipe, MatTooltipModule],
    templateUrl: './lote-list.component.html',
    styleUrl: './lote-list.component.css'
})

export class LoteListComponent implements OnInit {
    errorMessage: string | null = null;
    errorDetails: any | null = null;
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

    constructor(
        private loteService: LoteService,
    ) {
        this.statusDoLote = [];
    }

    ngOnInit(): void {
        // Este foi o menor número que definimos no arquivo html
        this.pageSize = 2;
        this.atualizarDadosDaPaginaTeste();
    }

    onChange(event: any): void {
        const value = event.target.value;
        console.log(value);
        this.paginar({ pageIndex: 0, pageSize: parseInt(value), length: this.totalPages });
    }

    // Método para paginar os resultados
    paginar(event: PageEvent): void {
        this.page = event.pageIndex;
        this.pageSize = event.pageSize;
        this.atualizarDadosDaPaginaTeste();
    }

    atualizarDadosDaPaginaTeste(): void {
        this.carregarLotesEnvia(this.page, this.pageSize);
        this.loteService.countAll().subscribe(data => {
            this.totalRecords = data;
            this.totalPages = Math.round(this.totalRecords / this.pageSize);
            if (this.totalPages < 1)
                this.totalPages = 1;
            console.log(this.totalPages);
        })
        console.log(this.carregarLotesEnvia);
    }

    carregarLotesEnvia(page: number, pageSize: number): void {
        this.loteService.findByAll(page, pageSize).subscribe({
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
        this.totalPages = Math.round(this.totalRecords / this.pageSize);
    }

    ativarLote(id: number): void {
        this.loteService.ativaLote(id).subscribe({
            next: (response) => {
                console.log(response);
            },
            error: (error) => {
                // Este callback é executado quando ocorre um erro durante a emissão do valor
                this.errorDetails = error;
                this.errorMessage = error.subjectError.message;
                console.error('Erro:', this.errorDetails);
            }
        });
    }
}