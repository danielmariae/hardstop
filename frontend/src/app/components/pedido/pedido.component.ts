import { Component, OnInit } from "@angular/core";
import { HeaderHomeComponent } from "../template/home-template/header-home/header-home.component";
import { PedidoService } from "../../services/pedido.service";
import { PedidoRecebe } from "../../models/pedidoRecebe.modelo";
import { CommonModule } from "@angular/common";
import { getFormattedCurrency } from "../../utils/formatValues";
import { Produto } from "../../models/produto.model";
import { ProdutoService } from "../../services/produto.service";
import { Observable, catchError, forkJoin, map, of } from "rxjs";
import { ItemDaVenda } from "../../models/itemDaVenda";
import { NavigationService } from "../../services/navigation.service";

@Component({
    selector: 'app-pedido',
    standalone: true,
    imports: [HeaderHomeComponent, CommonModule],
    templateUrl: './pedido.component.html',
    styleUrls: ['./pedido.component.css'] // Correção de 'styleUrl' para 'styleUrls'
})
export class PedidoComponent implements OnInit {
    pedidoItens: PedidoRecebe[] = [];
    produtosPorPedido: { [pedidoId: number]: Produto[] } = {};

    constructor(
      private pedidoService: PedidoService,
      public produtoService: ProdutoService,
      private navigationService: NavigationService
    ) {}

    ngOnInit(): void {
      this.carregarPedidos();
      this.carregarProdutosParaPedidos();
    }

    
    getUltimoStatus(pedido: PedidoRecebe): string | null {
      return pedido.statusDoPedido.at(pedido.statusDoPedido.length - 1)?.status || null;
    }

    getDataHoraPedido(pedido: PedidoRecebe): string | null {
      return pedido.statusDoPedido.at(0)?.dataHora || null;
    }

    getTotalPedido(pedido: PedidoRecebe): number {
      return pedido.itemDaVenda.reduce((total, itemPedido) => {
        if (itemPedido.preco && itemPedido.quantidadeUnidades) {
          return total + (itemPedido.preco * itemPedido.quantidadeUnidades);
        }
        return total;
      }, 0);
    }

    // getTotalPorItemVenda(itemDaVenda: ItemDaVenda | undefined | null): number | undefined | null{
    //   if(itemDaVenda === undefined)
    //     return undefined;
    //   else if(itemDaVenda === null)
    //     return null;
    //   else if(itemDaVenda.preco !== null && itemDaVenda.preco !== undefined && itemDaVenda.quantidadeUnidades !== null && itemDaVenda.quantidadeUnidades !== undefined)
    //     return itemDaVenda.preco * itemDaVenda.quantidadeUnidades;
    // }

    carregarPedidos(): void {
      this.pedidoService.findAll().subscribe({
          next: (response) => {
              console.log(response);
              this.pedidoItens = response;
              this.carregarProdutosParaPedidos();
          },
          error: (error) => {
              // Este callback é executado quando ocorre um erro durante a emissão do valor
              console.error('Erro:', error);
              window.alert(error);
          } 
      })
    }

    carregarProdutosParaPedidos(): void {
      // Mapeia cada ID de pedido aos seus produtos correspondentes
      this.pedidoItens.forEach(pedido => {
        pedido.itemDaVenda.forEach(itemPedido => {
          if (itemPedido.idProduto) {
            this.produtoService.findById(itemPedido.idProduto).subscribe(
              produto => {
                if (produto && pedido.id) {
                  // Adiciona o produto ao array de produtos do pedido
                  this.produtosPorPedido[pedido.id] = this.produtosPorPedido[pedido.id] || [];
                  this.produtosPorPedido[pedido.id].push(produto);
                }
              },
              error => {
                console.error(`Erro ao buscar produto com id ${itemPedido.idProduto}:`, error);
              }
            );
          }
        });
      });
    }
            
    // getProdutosOfPedido(pedido: PedidoRecebe): Observable<Produto[]> {
    //   const observables = pedido.itemDaVenda
    //       .filter(itemPedido => itemPedido.idProduto !== null && itemPedido.idProduto !== undefined)
    //       .map(itemPedido => 
    //           this.produtoService.findById(itemPedido.idProduto as number)
    //             .pipe(
    //               catchError(error => {
    //                 console.error(`Erro ao buscar produto com id ${itemPedido.idProduto}:`, error);
    //                 return of(null); // Retorna null em caso de erro
    //               })
    //             )
    //       );

    //     console.log(observables);

    //   return forkJoin(observables).pipe(
    //     map(produtos => produtos.filter(produto => produto !== null)) // Filtra produtos válidos
    //   );
    // }

    getProdutoOfPedido(id: number): Observable<Produto> {
      return this.produtoService.findById(id);
    }

    formatValues(valor: number): string {
      return getFormattedCurrency(valor);
    }      

    comprarMaisProdutos(): void{
      this.navigationService.navigateTo('/home');
    }
  }
