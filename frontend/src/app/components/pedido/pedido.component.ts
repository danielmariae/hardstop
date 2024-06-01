import { Component, OnInit } from "@angular/core";
import { HeaderHomeComponent } from "../template/home-template/header-home/header-home.component";

@Component({
    selector: 'app-pedido',
    standalone: true,
    imports: [HeaderHomeComponent],
    templateUrl: './pedido.component.html',
    styleUrl: './pedido.component.css'
  })
  export class PedidoComponent implements OnInit {
    ngOnInit(): void {
        
    }
  }
  