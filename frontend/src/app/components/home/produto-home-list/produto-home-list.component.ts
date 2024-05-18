import { Component, OnInit } from '@angular/core';
import { Produto } from '../../../models/produto.model';
import { ActivatedRoute } from '@angular/router';
import { ProdutoService } from '../../../services/produto.service';
import { CommonModule } from '@angular/common';
import { Title } from '@angular/platform-browser';
import { HeaderHomeComponent } from "../../template/home-template/header-home/header-home.component";
import { PageEvent } from '@angular/material/paginator';

@Component({
    selector: 'app-produto-home-list',
    standalone: true,
    templateUrl: './produto-home-list.component.html',
    styleUrl: './produto-home-list.component.css',
    imports: [CommonModule, HeaderHomeComponent]
})
export class ProdutoHomeListComponent implements OnInit {
  nomeBusca: string = '';
  
  totalRecords = 0;
  page = 0;
  pageSize = 0;
  totalPages = 0;
  produtos: Produto[] = [];
  imagensBase64: { [produtoId: number]: string } = {};

  constructor(
    private route: ActivatedRoute,
    private produtoService: ProdutoService,
    private titleService: Title
  ){  }

  ngOnInit(): void {
    this.pageSize = 2; 
  
    this.route.paramMap.subscribe(params => {
      this.nomeBusca = params.get('nome') || '';
      this.page=0;
      this.carregarProdutos(this.page, this.pageSize);
      this.titleService.setTitle(`Você buscou por "${this.nomeBusca}"`)
    })
    console.log(this.produtos);
  }


  carregarProdutos(page: number, pageSize: number): void {
    this.produtoService.findByNome(this.nomeBusca, this.page, this.pageSize).subscribe({
        next: (response) => {
            console.log(response);
            this.produtos = response.produtos;
            this.totalRecords = response.totalItems;
            this.totalPages = Math.round(this.totalRecords/this.pageSize);
            this.carregarImagensParaProdutos();
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
            console.error('Erro:', error);
            window.alert(error);
        } 
    })
  }


  carregarImagensParaProdutos(): void {
    this.produtos.forEach(produto => {
      if (produto.imagemPrincipal) {
        this.produtoService.getImageAsBase64(produto.imagemPrincipal)
          .then(imagemBase64 => {
            if (produto.id !== undefined) {
              this.imagensBase64[produto.id] = imagemBase64;
            }
          })
          .catch(error => {
            console.error(`Erro ao carregar imagem para o produto ${produto.id}:`, error);
          });
      } else {
        if (produto.id !== undefined) {
          this.imagensBase64[produto.id] = '';
        }
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
    this.carregarProdutos(this.page, this.pageSize);
}
}
