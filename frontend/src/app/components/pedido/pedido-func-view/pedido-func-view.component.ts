import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { PedidoRecebe } from '../../../models/pedidoRecebe.model';
import { Produto } from '../../../models/produto.model';
import { NavigationService } from '../../../services/navigation.service';
import { PedidoService } from '../../../services/pedido.service';
import { ProdutoService } from '../../../services/produto.service';
import { getFormattedCurrency } from '../../../utils/formatValues';
import { HeaderHomeComponent } from '../../template/home-template/header-home/header-home.component';
import { cpfValidator } from '../../../validators/cpf.validator';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';

@Component({
  selector: 'app-pedido-func-view',
  standalone: true,
  imports: [HeaderHomeComponent, ReactiveFormsModule, CommonModule, FormsModule, NgxMaskDirective],
  templateUrl: './pedido-func-view.component.html',
  styleUrl: './pedido-func-view.component.css',
  providers:[provideNgxMask()]
})
export class PedidoFuncViewComponent implements OnInit {

  pedidoItens: PedidoRecebe[] = [];
  produtosPorPedido: { [pedidoId: number]: Produto[] } = {};
  cpfForm: FormGroup;
  codRastForm: FormGroup;
  showResults: boolean = false;


  constructor(
    private pedidoService: PedidoService,
    public produtoService: ProdutoService,
    private navigationService: NavigationService,
    private formBuilder: FormBuilder
  ) {
    this.cpfForm = this.formBuilder.group({
      cpf: this.formBuilder.control('',{
        validators:[
          Validators.required,
          cpfValidator()
        ]
      }
      )
    });

    this.codRastForm = this.formBuilder.group({
      codigoDeRastreamento: ['', Validators.required]
    });   
  }

  ngOnInit(): void {
  }


  getUltimoStatus(pedido: PedidoRecebe): string | null {
    return pedido.statusDoPedido.at(pedido.statusDoPedido.length - 1)?.descricaoStatus || null;
  }

  getUltimoStatusId(pedido: PedidoRecebe): number | null {
    return pedido.statusDoPedido.at(pedido.statusDoPedido.length - 1)?.idStatus || null;
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

  carregarPedidosCPF(): void {
    const cpf = this.cpfForm.get('cpf')?.value;
    if (this.cpfForm.valid) {
      this.pedidoService.findByCpf(cpf).subscribe({
        next: (response) => {
          console.log(response);
          this.pedidoItens = response;
          this.showResults = true;
          this.carregarProdutosParaPedidos();
        },
        error: (error) => {
          // Este callback é executado quando ocorre um erro durante a emissão do valor
          console.error('Erro:', error);
          window.alert(error);
        }
      });
    } else {
      window.alert("Insira um CPF");
    }

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

  confirmarPagamento(index1: number): void {
    this.pedidoService.updatePedidoSeparadoEstoque(index1, 2).subscribe({
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

  rejeitarPagamento(index1: number): void {
    this.pedidoService.updatePedidoSeparadoEstoque(index1, 1).subscribe({
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

  separadoDoEstoque(index1: number): void {
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

  entregueAtransportadora(index1: number): void {
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

  entregueAoCliente(index1: number): void {
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

  clienteDesistiu(index1: number): void {
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
      next: (response) => {
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
