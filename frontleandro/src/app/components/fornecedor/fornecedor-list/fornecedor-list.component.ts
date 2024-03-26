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

    constructor(private fornecedorService: FornecedorService) {

    }

    ngOnInit(): void {
        this.fornecedorService.findAll().subscribe(data => {
            this.fornecedores = data;
        })
    }
}