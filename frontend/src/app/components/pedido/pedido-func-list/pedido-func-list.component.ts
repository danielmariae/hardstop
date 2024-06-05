import { Component, OnInit } from "@angular/core";
import { HeaderHomeComponent } from "../../template/home-template/header-home/header-home.component";
import { PedidoService } from "../../../services/pedido.service";
import { PedidoRecebe } from "../../../models/pedidoRecebe.model";
import { CommonModule } from "@angular/common";
import { getFormattedCurrency } from "../../../utils/formatValues";
import { Produto } from "../../../models/produto.model";
import { ProdutoService } from "../../../services/produto.service";
import { Observable, catchError, forkJoin, map, of } from "rxjs";
import { ItemDaVenda } from "../../../models/itemDaVenda";
import { NavigationService } from "../../../services/navigation.service";
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";

@Component({
    selector: 'app-pedido-func-list',
    standalone: true,
    imports: [HeaderHomeComponent, ReactiveFormsModule, CommonModule],
    templateUrl: './pedido-func-list.component.html',
    styleUrls: ['./pedido-func-list.component.css'] // Correção de 'styleUrl' para 'styleUrls'
})
export class PedidoFuncListComponent implements OnInit {
    pedidoItens: PedidoRecebe[] = [];
    produtosPorPedido: { [pedidoId: number]: Produto[] } = {};
    statusPedidos: any[];
    statusPedidosForm: FormGroup;
    statusEscolhido: number | null;


    constructor(
      private pedidoService: PedidoService,
      public produtoService: ProdutoService,
      private navigationService: NavigationService,
      private formBuilder: FormBuilder
    ) {
      this.statusPedidosForm = formBuilder.group({
        statusSelecionado: [null]
      })
      this.statusPedidos = [];
      this.statusEscolhido = null;
    }

    ngOnInit(): void {
      this.pedidoService.getStatusPedido().subscribe(data => { 
        this.statusPedidos = data;
        console.log(this.statusPedidos);
        for(let i=0; i<this.statusPedidos.length; i++) {
          console.log(this.statusPedidos[i]);
        }
      });

      this.statusPedidosForm.get('statusSelecionado')?.valueChanges.subscribe(value => {
        this.onStatusChange(value);
      });

      // this.carregarProdutosParaPedidos();
    }

    onStatusChange(value: any) {
      //console.log('Valor selecionado:', value);
      this.statusEscolhido = Number(value);
      console.log(this.statusEscolhido);
      this.carregarPedidosStatus(this.statusEscolhido);
      // Aqui você pode adicionar lógica adicional para manipular o valor selecionado
    }
    
    getUltimoStatus(pedido: PedidoRecebe): string | null {
      return pedido.statusDoPedido.at(pedido.statusDoPedido.length - 1)?.descricaoStatus || null;
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

  

    carregarPedidosStatus(status: number): void {
      this.pedidoService.findAllFuncStatus(status).subscribe({
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

    separarDoEstoque(): void{
      //this.navigationService.navigateTo('/home');
    }
  }
