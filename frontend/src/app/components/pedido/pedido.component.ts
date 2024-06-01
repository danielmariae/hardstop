import { Component, OnInit } from "@angular/core";
import { HeaderHomeComponent } from "../template/home-template/header-home/header-home.component";
import { PedidoService } from "../../services/pedido.service";
import { PedidoRecebe } from "../../models/pedidoRecebe.modelo";
import { CommonModule } from "@angular/common";

@Component({
    selector: 'app-pedido',
    standalone: true,
    imports: [HeaderHomeComponent, CommonModule],
    templateUrl: './pedido.component.html',
    styleUrl: './pedido.component.css'
  })
  export class PedidoComponent implements OnInit {
    pedidoItens: PedidoRecebe[] = [];

    constructor(private pedidoService: PedidoService) {}


    ngOnInit(): void {
      this.pedidoService.findAll().subscribe( (itens: PedidoRecebe[]) => {
        this.pedidoItens = itens;
        console.log(this.pedidoItens);
      });
    }
  }
  