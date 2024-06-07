import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule, NgFor } from '@angular/common';
import { MatCard, MatCardActions, MatCardContent, MatCardFooter, MatCardTitle } from '@angular/material/card';
import { MatButton } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProdutoService } from '../../services/produto.service';
import { CarrinhoService } from '../../services/carrinho.service';
import { PageEvent } from '@angular/material/paginator';
import { Produto } from '../../models/produto.model';
import { getFormattedCurrency } from '../../utils/formatValues';
import { HeaderHomeComponent } from '../template/home-template/header-home/header-home.component';
import { FooterHomeComponent } from '../template/home-template/footer-home/footer-home.component';
import { ClienteService } from '../../services/cliente.service';
import { SessionTokenService } from '../../services/session-token.service';
import { NavigationService } from '../../services/navigation.service';
import { LocalStorageService } from '../../services/local-storage.service';

// tipo personalizado de dados, como classes e interfaces, porém mais simples.
type Card = {
  idProduto: number;
  titulo: string;
  preco: number;
  quantidade: number;
  urlImagem: string;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MatCard, MatCardActions, MatCardContent, MatCardTitle, MatCardFooter,
    MatButton, RouterOutlet, HeaderHomeComponent, FooterHomeComponent, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  cards = signal<Card[]>([]);
  produtos: Produto[] = [];
  totalRecords = 0;
  page = 0;
  pageSize = 0;
  totalPages = 0;
  imagensBase64: { [produtoId: number]: string } = {};

  usuarioLogado: boolean = false;
  admLogado: boolean = false;

  sucessMessage: string | null = null;
  errorMessage: string | null = null;

  carrinho: any;

  constructor(
    private produtoService: ProdutoService,
    private carrinhoService: CarrinhoService,
    private navigationService: NavigationService,
    private clienteService: ClienteService,
    private sessionTokenService: SessionTokenService,
    private localStorageService: LocalStorageService
  ) { }

  ngOnInit(): void {
    this.pageSize = 4;
    this.page = 0;

    // Verifica se há um estado de login armazenado no sessionStorage ao inicializar o componente
    const usuarioLogadoState = sessionStorage.getItem('usuarioLogado');
    if (usuarioLogadoState !== null) {
      this.usuarioLogado = JSON.parse(usuarioLogadoState);
    }

    const admLogadoState = sessionStorage.getItem('admLogado');
    if (admLogadoState !== null) {
      this.admLogado = JSON.parse(admLogadoState);
    }

    // Subscreve-se aos eventos de login bem-sucedido para atualizar o estado de login
    this.sessionTokenService.loginAdmSuccess$.subscribe(() => {
      this.admLogado = true;
      sessionStorage.setItem('admLogado', JSON.stringify(true));
    });

    this.sessionTokenService.loginClienteSuccess$.subscribe(() => {
      this.usuarioLogado = true;
      sessionStorage.setItem('usuarioLogado', JSON.stringify(true));
    });

    this.atualizarDadosDaPagina();
    console.log(this.produtos);
  }


  carregarProdutos(page: number, pageSize: number): void {
    this.produtoService.findAll(this.page, this.pageSize).subscribe({
      next: (response) => {
        console.log(response);
        this.produtos = response;
          this.totalPages = Math.round(this.totalRecords / this.pageSize);
        this.carregarImagensParaProdutos();
        this.carregarCards();
      },
      error: (error) => {
        // Este callback é executado quando ocorre um erro durante a emissão do valor
        console.error('Erro:', error);
        this.criarMensagemDeErro(error.statusText);
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

  atualizarCarrinho() {
    this.carrinho = this.localStorageService.getItem('carrinho');
    console.log(this.carrinho);
  }

  // carregarCards() {
  //   const cards: Card[] = [];
  //   this.consultas.forEach(consulta => {
  //     cards.push({
  //       idConsulta: consulta.id,
  //       titulo: consulta.nome,
  //       preco: consulta.valorVenda,
  //       quantidade: consulta.quantidadeUnidades,
  //       urlImagem: this.consultaService.getUrlImagem(consulta.imagemPrincipal)
  //     });
  //   });
  //   this.cards.set(cards);
  // }

  carregarCards() {
    const cards: Card[] = [];
    this.produtos.forEach(produto => {
      cards.push({
        idProduto: produto.id,
        titulo: produto.nome,
        preco: produto.valorVenda,
        quantidade: produto.quantidadeUnidades,
        urlImagem: this.produtoService.getUrlImagem(produto.imagemPrincipal)
      });
    });
    this.cards.set(cards);
  }
  adicionarAoCarrinho(card: Card) {
    if(card.quantidade === 0){
      console.error("Produto sem estoque!");
      this.criarMensagemDeErro('Produto sem estoque!');
      return;
    }
    this.carrinhoService.adicionar({
        id: card.idProduto,
        nome: card.titulo,
        preco: card.preco,
        quantidade: 1,
        quantidadeLimite: card.quantidade,
        urlImagem: card.urlImagem
      });
      this.atualizarCarrinho();
      // if(this.carrinho.find())
      //   this.criarMensagemSucesso('Produto '+card.titulo+'adicionado ao carrinho!');
  }

  adicionarAfavoritos(card: Card) {
    this.clienteService.insertListaDesejos(card.idProduto).subscribe({
      next: (response) => {
        console.log('Resultado:', response);
        this.criarMensagemSucesso('')
      },
      error: (error) => {
        // Este callback é executado quando ocorre um erro durante a emissão do valor
        console.error('Erro ao inserir produto na Lista de Favoritos:', error);
        this.criarMensagemDeErro(error.error.subjectError.message);
      }
    });
  }

  // Método para paginar os resultados
  paginar(event: PageEvent): void {
    this.page = event.pageIndex;
    this.pageSize = event.pageSize;
    this.atualizarDadosDaPagina();
  }

  onChange(event: any): void {
    const value = event.target.value;
    console.log(value);
    this.paginar({ pageIndex: 0, pageSize: parseInt(value), length: this.totalPages });
  }

  // Método para paginar os resultados
  atualizarDadosDaPagina(): void {
    this.carregarProdutos(this.page, this.pageSize);
    this.produtoService.count().subscribe(data => {
      this.totalRecords = data;
      this.totalPages = Math.round(this.totalRecords / this.pageSize);
      if (this.totalPages <= 1) {
        this.totalPages = 1;
      }
    });
  }

  formatValues(valor: number) {
    return getFormattedCurrency(valor);
  }

  
  criarMensagemDeErro(erro: string) {
    this.errorMessage = erro;
    setTimeout(() => {
      this.errorMessage = '';
    }, 3000);
  }
  
  criarMensagemSucesso(sucess: string) {
    this.sucessMessage = sucess;
    setTimeout(() => {
      this.sucessMessage = '';
    }, 3000);
  }
}