import { Component, OnInit } from "@angular/core";
import { HeaderHomeComponent } from "../template/home-template/header-home/header-home.component";
import { ClienteService } from "../../services/cliente.service";
import { CommonModule } from "@angular/common";
import { Produto } from "../../models/produto.model";
import { ConsultaService } from "../../services/consulta.service";

@Component({
    selector: 'app-favoritos',
    standalone: true,
    imports: [HeaderHomeComponent, CommonModule],
    templateUrl: './favoritos.component.html',
    styleUrl: './favoritos.component.css'
  })
  export class FavoritosComponent implements OnInit {
    produtoItens: Produto[] = [];

    constructor(private clienteService: ClienteService,
                private consultaService: ConsultaService
    ) {}


    ngOnInit(): void {
      this.clienteService.findListaDesejos().subscribe( (itens: Produto[]) => {
        this.produtoItens = itens;
        for(let i=0; i<this.produtoItens.length; i++) {
          this.produtoItens[i].imagemPrincipal =  this.consultaService.getUrlImagem(this.produtoItens[i].imagemPrincipal);
        }
      });
    }

    removerItem(id: number) {
      this.clienteService.deleteListaDesejos(id).subscribe({
        next: (response) => {
        console.log('Resultado:', response);
        window.location.reload();
        },
        error: (error) => {
          // Este callback é executado quando ocorre um erro durante a emissão do valor
          console.error('Erro ao tentar remover produto na Lista de Favoritos:', error);
          //window.alert(error)
        }
    });
  
    }



  }
  