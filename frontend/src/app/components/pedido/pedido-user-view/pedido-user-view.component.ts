import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PedidoService } from '../../../services/pedido.service';
import { ActivatedRoute, Route } from '@angular/router';
import { PedidoRecebe } from '../../../models/pedidoRecebe.modelo';

@Component({
  selector: 'app-pedido-user-view',
  standalone: true,
  imports: [
    CommonModule,

  ],
  templateUrl: './pedido-user-view.component.html',
  styleUrl: './pedido-user-view.component.css'
})
export class PedidoUserViewComponent implements OnInit {
  errorMessage: string | null = null;
  errorDetails: any | null = null; // Objeto JSON para armazenar os detalhes do erro

  pedido: PedidoRecebe;
  id: number = 1;

  constructor(
    private pedidoService: PedidoService,
    private route: ActivatedRoute
  ){
    this.pedido = new PedidoRecebe();
  }

  ngOnInit(): void {
      this.id = Number(this.route.snapshot.paramMap.get('id'));
      console.log(this.id);

      this.pedidoService.findById(this.id).subscribe(pedido =>{
        this.pedido = pedido;
        console.log(this.pedido);
      });
    }
}
