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
import { PageEvent } from '@angular/material/paginator';
import { MatPaginatorModule } from '@angular/material/paginator';


@Component({
    selector: 'app-fornecedor-list',
    standalone: true,
    imports: [NgFor, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule],
    templateUrl: './fornecedor-list.component.html',
    styleUrl: './fornecedor-list.component.css'
})

export class FornecedorListComponent implements OnInit {
    // variaveis de controle de paginacao
    totalRecords = 0;
    page = 0;
    pageSize = 0;
    displayedColumns: string[] = ['id', 'nomeFantasia', 'cnpj', 'endSite', 'endereco', 'telefone', 'acao'];
    fornecedores: Fornecedor[] = [];
    tiposTelefoneMap: Map<number, string> = new Map<number, string>();

    constructor(private fornecedorService: FornecedorService,
        private router: Router,
        private route: ActivatedRoute) {
    }

    ngOnInit(): void {
      this.carregarTiposTelefone();
      this.pageSize = 2; // Este foi o menor número que definimos no arquivo html
      this.atualizarDadosDaPagina();

      // Inscreva-se para receber notificações de novos fornecedores
      this.fornecedorService.fornecedorInserido$.subscribe(() => {
        this.carregarFornecedores(this.page, this.pageSize); // Recarrega os fornecedores ao receber uma notificação
        this.router.navigate(['fornecedores']);
      });
    }

    // Método para paginar os resultados
    paginar(event: PageEvent) : void {
      this.page = event.pageIndex;
      this.pageSize = event.pageSize;
      this.atualizarDadosDaPagina();
    }

    atualizarDadosDaPagina(): void {
      this.carregarFornecedores(this.page, this.pageSize);
    
      this.fornecedorService.count().subscribe(data => {
        this.totalRecords = data;
        console.log(this.totalRecords);
      });
    }

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
            // Lide com o erro conforme necessário
          }
        });
    }

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
}