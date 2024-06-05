import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../services/pedido.service';
import { ActivatedRoute } from '@angular/router';
import { PedidoRecebe } from '../../../models/pedidoRecebe.model';
import { CartaoRecebe } from '../../../models/cartaoRecebe.model';
import { StatusDoPedido } from '../../../models/statusDoPedido.model';
import { ProdutoService } from '../../../services/produto.service';
import { ItemDaVendaRecebe } from '../../../models/itemRecebe';

@Component({
  selector: 'app-pedido-user-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pedido-user-view.component.html',
  styleUrls: ['./pedido-user-view.component.css']
})
export class PedidoUserViewComponent implements OnInit {
  errorMessage: string | null = null;
  errorDetails: any | null = null; // Objeto JSON para armazenar os detalhes do erro

  modalidades: any[] = [];
  pedido: PedidoRecebe | undefined;
  statusPadrao: any[] = [];
  ultimoStatus: StatusDoPedido | undefined;
  cartaoDeCredito: CartaoRecebe | undefined;
  formaPagamento: string | null = null;

  showStatus: boolean = false;

  id: number = 1;

  constructor(
    private pedidoService: PedidoService,
    private route: ActivatedRoute, 
    public produtoService: ProdutoService
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    console.log(`ID do pedido: ${this.id}`);

    this.pedidoService.getModalidadePagamento().subscribe({
      next: (data) => {
        this.modalidades = data;
        console.log('Modalidades carregadas:', this.modalidades);
      },
      error: (error) => {
        console.error('Erro ao carregar modalidades:', error);
      }
    });

    this.pedidoService.getStatusPedidoPadrao().subscribe({
      next: (data) => {
        this.statusPadrao = data;
        console.log("Status carregados: ", this.statusPadrao)
      }
    })

    this.loadPedido();

  }

  loadPedido(): void {
    this.pedidoService.findByIdCliente(this.id).subscribe({
      next: (response) => {
        console.log('Pedido carregado:', response);
        this.pedido = response;
        this.getDescricaoById(response.formaDePagamento.modalidade);
        this.loadUltimoStatus();
      },
      error: (error) => {
        console.error('Erro ao carregar pedido:', error);
        window.alert(error);
      }
    });
  }

  loadUltimoStatus(): void {
    this.ultimoStatus = this.pedido?.statusDoPedido.at(this.pedido.statusDoPedido.length-1);
    if(this.ultimoStatus?.idStatus !== 1 && this.ultimoStatus?.idStatus !== 6 && this.ultimoStatus?.idStatus !== 7){
      this.showStatus = true;
    }
    console.log(this.ultimoStatus);
  }


  statusIgual(id: number): boolean{
      if(id === this.ultimoStatus?.idStatus)
        return true;
      else 
        return false;
  }

  getHoraPorStatus(id: number): void{
    this.pedidoService.findByIdStatus(id).subscribe({
      next: (data) => {
          console.log('Modalidades carregadas:', this.modalidades);
      },
      error: (error) => {
        console.error('Erro ao carregar modalidades:', error);
      }
    });
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

  getDescricaoById(id: number | null): string {
    console.log(`Buscando descrição para modalidade ID: ${id}`);
    const modalidade = this.modalidades.find(modalidade => modalidade.id === id);
    if (modalidade) {
      console.log(`Descrição encontrada: ${modalidade.descricao}`);
      if(modalidade.id === 0){
        this.getCartaoDeCredito(this.id);
      }
      this.formaPagamento = modalidade.descricao;
    } else {
      console.log('Descrição não encontrada');
    }
    return modalidade ? modalidade.descricao : 'Descrição não encontrada';
  }

  getCartaoDeCredito(id: number): void {
    this.pedidoService.findCartaoById(this.id).subscribe({
      next: (response) => {
        console.log('Cartão de crédito carregado:', response);
        this.cartaoDeCredito = response;
      },
      error: (error) => {
        console.error('Erro ao carregar cartão de crédito:', error);
      }
    });
  }

  getSubtotal(idv: ItemDaVendaRecebe): number{
    if(idv.preco && idv.quantidadeUnidades)
      return (idv.preco * idv.quantidadeUnidades);
    else
      return 0;
  }
}
