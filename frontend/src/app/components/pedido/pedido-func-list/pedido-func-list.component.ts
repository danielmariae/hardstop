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
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";

@Component({
    selector: 'app-pedido-func-list',
    standalone: true,
    imports: [HeaderHomeComponent, ReactiveFormsModule, CommonModule, FormsModule],
    templateUrl: './pedido-func-list.component.html',
    styleUrls: ['./pedido-func-list.component.css'] // Correção de 'styleUrl' para 'styleUrls'
})
export class PedidoFuncListComponent implements OnInit {
    pedidoItens: PedidoRecebe[] = [];
    produtosPorPedido: { [pedidoId: number]: Produto[] } = {};
    statusPedidos: any[];
    statusPedidosForm: FormGroup;
    statusEscolhido: number | null;
    codRastForm: FormGroup;
  
   
    constructor(
      private pedidoService: PedidoService,
      public produtoService: ProdutoService,
      private navigationService: NavigationService,
      private formBuilder: FormBuilder
    ) {
      this.statusPedidosForm = formBuilder.group({
        statusSelecionado: [null]
      });
      this.codRastForm = formBuilder.group({
        codigoDeRastreamento: ['', Validators.required]
      });
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

    }

    onStatusChange(value: any) {
      this.statusEscolhido = Number(value);
      console.log(this.statusEscolhido);
      this.carregarPedidosStatus(this.statusEscolhido);
    }
    
    getUltimoStatus(pedido: PedidoRecebe): string | null {
      return pedido.statusDoPedido.at(pedido.statusDoPedido.length - 1)?.descricaoStatus || null;
    }

    getDataHoraPedido(pedido: PedidoRecebe): string | null {
      return pedido.statusDoPedido.at(0)?.dataHora || null;
    }

    getCodRastreamento(pedido: PedidoRecebe): string | null {
      return pedido.codigoDeRastreamento || null;
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

    getProdutoOfPedido(id: number): Observable<Produto> {
      return this.produtoService.findById(id);
    }

    formatValues(valor: number): string {
      return getFormattedCurrency(valor);
    }      

    separadoDoEstoque(index1: number): void{
      this.pedidoService.updatePedidoSeparadoEstoque(index1, 3).subscribe({
        next: (response) => {
            console.log(response);
            window.location.reload();
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
            console.error('Erro:', error);
            window.alert(error);
        } 
    })
      
      //this.navigationService.navigateTo('/home');
    }

    entregueAtransportadora(index1: number): void{
      if (this.codRastForm.valid) {
        const codigoDeRastreamento = this.codRastForm.get('codigoDeRastreamento')?.value;
        this.pedidoService.updatePedidoEntregue(index1, 4, codigoDeRastreamento).subscribe({
        next: (response) => {
            //console.log(response);
            window.location.reload();
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
           // console.error('Erro:', error);
            window.alert(error);
        } 
    })
      } else {
        window.alert("Insira um Código de Rastreamento");
      }
    }

    entregueAoCliente(index1: number): void{
      this.pedidoService.updatePedidoEntregueCliente(index1, 5).subscribe({
        next: (response) => {
            console.log(response);
            window.location.reload();
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
            console.error('Erro:', error);
            window.alert(error);
        } 
    })
    }

    clienteDesistiu(index1: number): void{
      this.pedidoService.updatePedidoDesistido(index1, 7).subscribe({
        next: (response) => {
            console.log(response);
            window.location.reload();
        },
        error: (error) => {
            // Este callback é executado quando ocorre um erro durante a emissão do valor
            console.error('Erro:', error);
            window.alert(error);
        } 
    })
    }

    deletarPedido(id: number): void {

      this.pedidoService.deletePedidoByFunc(id).subscribe({
        next:  (response) => {
          console.log(response);
          window.location.reload();
          },
          error: (error) => {
          console.error(error);
          window.alert(error); // Exibe a mensagem de erro usando window.alert()
          }
      });
    }


  }
