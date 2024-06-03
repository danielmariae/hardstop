import { Component, OnInit, signal } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { HeaderHomeComponent } from "../header-home/header-home.component";
import { CommonModule, NgFor } from '@angular/common';
import { FooterHomeComponent } from "../footer-home/footer-home.component";
import { Consulta } from '../../../../models/consulta.model';
import { MatCard, MatCardActions, MatCardContent, MatCardFooter, MatCardTitle } from '@angular/material/card';
import { MatButton } from '@angular/material/button';
import { SessionTokenService } from '../../../../services/session-token.service';
import { CarrinhoService } from '../../../../services/carrinho.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConsultaService } from '../../../../services/consulta.service';
import { ClienteService } from '../../../../services/cliente.service';
import { ProdutoService } from '../../../../services/produto.service';
import { PageEvent } from '@angular/material/paginator';
import { Produto } from '../../../../models/produto.model';



// tipo personalizado de dados, como classes e interfaces, porém mais simples.
type Card = {
    idConsulta: number;
    titulo: string;
    preco: number;
    quantidade: number;
    urlImagem: string;
  }

@Component({
    selector: 'app-home-template',
    standalone: true,
    imports: [MatCard, MatCardActions, MatCardContent, MatCardTitle, MatCardFooter, NgFor, MatButton, RouterOutlet, HeaderHomeComponent, FooterHomeComponent, CommonModule],
    templateUrl: './home-template.component.html',
    styleUrl: './home-template.component.css',
})
export class HomeTemplateComponent implements OnInit{
    cards = signal<Card[]> ([]);
    consultas: Consulta[] = [];
    produtos: Produto[] = [];
    totalRecords = 0;
    page = 0;
    pageSize = 0;
    totalPages = 0;
    imagensBase64: { [produtoId: number]: string } = {};

    constructor(private consultaService: ConsultaService, 
        private produtoService: ProdutoService,
        private carrinhoService: CarrinhoService,
        private snackBar: MatSnackBar,
        private clienteService: ClienteService,
      ) {}

ngOnInit(): void {
  this.pageSize = 2; 
  this.page=0;
  this.carregarProdutos(this.page, this.pageSize);
  console.log(this.produtos);
}

carregarConsultas() {
    // buscando todos as consultas
    this.consultaService.findAll(0, 10).subscribe(data => {
      this.consultas = data;
      // console.log(data);
      this.carregarCards();
    });
  }

  carregarProdutos(page: number, pageSize: number): void {
    this.produtoService.findAll(this.page, this.pageSize).subscribe({
        next: (response) => {
            console.log(response);
            this.produtos = response;
            this.totalRecords = 
            this.totalPages = Math.round(this.totalRecords/this.pageSize);
            this.carregarImagensParaProdutos();
            this.carregarCards();
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

  carregarCards() {
    const cards: Card[] = [];
    this.consultas.forEach(consulta => {
      cards.push({
        idConsulta: consulta.id,
        titulo: consulta.nome,
        preco: consulta.valorVenda,
        quantidade: consulta.quantidadeUnidades,
        urlImagem: this.consultaService.getUrlImagem(consulta.imagemPrincipal)
      });
    });
    this.cards.set(cards);
  }

  adicionarAoCarrinho(card: Card) {
    this.showSnackbarTopPosition('Produto adicionado ao carrinho!', 'Fechar');
    this.carrinhoService.adicionar({
      id: card.idConsulta,
      nome: card.titulo,
      preco: card.preco,
      quantidade: 1,
      quantidadeLimite: card.quantidade,
      urlImagem: card.urlImagem
    })

  }

  adicionarAfavoritos(card: Card) {
    this.clienteService.insertListaDesejos(card.idConsulta).subscribe({
      next: (response) => {
      console.log('Resultado:', response);
      },
      error: (error) => {
        // Este callback é executado quando ocorre um erro durante a emissão do valor
        console.error('Erro ao inserir produto na Lista de Favoritos:', error);
        //window.alert(error)
      }
  });
  }

  showSnackbarTopPosition(content:any, action:any) {
    this.snackBar.open(content, action, {
      duration: 2000,
      verticalPosition: "top", // Allowed values are  'top' | 'bottom'
      horizontalPosition: "center" // Allowed values are 'start' | 'center' | 'end' | 'left' | 'right'
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

