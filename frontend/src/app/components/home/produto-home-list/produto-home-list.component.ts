import { Component, OnInit } from '@angular/core';
import { Produto } from '../../../models/produto.model';
import { ActivatedRoute } from '@angular/router';
import { ProdutoService } from '../../../services/produto.service';
import { CommonModule } from '@angular/common';
import { Title } from '@angular/platform-browser';
import { HeaderHomeComponent } from "../../template/home-template/header-home/header-home.component";

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

  constructor(
    private route: ActivatedRoute,
    private produtoService: ProdutoService,
    private titleService: Title
  ){}

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
            this.produtos = response;
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
            console.error('Erro:', error);
            window.alert(error);
        } 
    })
    this.totalPages = this.totalRecords/this.pageSize;
    ;
}

}
