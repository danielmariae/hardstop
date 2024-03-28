import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { NgFor } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Fornecedor } from "../../../models/fornecedor.model";
import { FornecedorService } from "../../../services/fornecedor.service";
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';


@Component({
    selector: 'app-fornecedor-list',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule],
    templateUrl: './fornecedor-list.component.html',
    styleUrl: './fornecedor-list.component.css'
})

export class FornecedorListComponent implements OnInit {
    displayedColumns: string[] = ['id', 'nomeFantasia', 'cnpj', 'endSite', 'endereco', 'telefone', 'acao'];
    fornecedores: Fornecedor[] = [];

    constructor(private fornecedorService: FornecedorService,
        private router: Router,
        private route: ActivatedRoute) {
    }

    ngOnInit(): void {

        this.carregarFornecedores(); // Carrega os fornecedores ao inicializar o componente

        // Inscreva-se para receber notificações de novos fornecedores
        this.fornecedorService.fornecedorInserido$.subscribe(() => {
          this.carregarFornecedores(); // Recarrega os fornecedores ao receber uma notificação
          this.router.navigate(['fornecedores']);
        });
    }

    carregarFornecedores(): void {
        this.fornecedorService.findAll().subscribe(data => {
            this.fornecedores = data;
        });
    }

    apagarFornecedor(id: number): void {
        this.fornecedorService.delete(id).subscribe(data => {
            this.fornecedorService.notificarFornecedorInserido();
        });
    }
}