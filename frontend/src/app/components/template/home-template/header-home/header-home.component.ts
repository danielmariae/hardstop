import { Component } from '@angular/core';
import { FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { SessionTokenService } from '../../../../services/session-token.service';
import { NavigationService } from '../../../../services/navigation.service';
import { CommonModule } from '@angular/common';
import { LocalStorageService } from '../../../../services/local-storage.service';
import { CarrinhoService } from '../../../../services/carrinho.service';
import { RouterModule } from '@angular/router';
import { Observable, Subscription, map, of } from 'rxjs';
import { Cliente } from '../../../../models/cliente.model';


@Component({
  selector: 'app-header-home',
  standalone: true,
  imports: [RouterOutlet, CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './header-home.component.html',
  styleUrl: './header-home.component.css'
})
export class HeaderHomeComponent {
  usuarioLogado: boolean = false;
  admLogado: boolean = false;
  clienteLogado: Cliente | null = null;
  private subscription = new Subscription();
  mostrarBalao: boolean = false;
  
  carrinhoSize: number = 0;
  

  constructor(
    private sessionTokenService: SessionTokenService,
    private navigationService: NavigationService,
    private carrinhoService: CarrinhoService,
  ){
  }

  ngOnInit(
  ): void {
    
    this.obterQtdItensCarrinho();
    this.clienteLogado = JSON.parse(localStorage.getItem('clienteLogado') || 'null');
    if (!this.clienteLogado) {
      this.obterClienteLogado();
    }

    // Get all "navbar-burger" elements
    const navbarBurgers = Array.from(document.querySelectorAll('.navbar-burger'));

    // Add a click event on each of them
    navbarBurgers.forEach(el => {
      el.addEventListener('click', () => {

        // Get the target from the "data-target" attribute
        const target = el.getAttribute('data-target');
        if (target !== null) {
          const targetElement = document.getElementById(target);

          // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
          el.classList.toggle('is-active');
          if (targetElement) {
            targetElement.classList.toggle('is-active');
          }
        }
      });
    });

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
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

        obterQtdItensCarrinho() {
         this.carrinhoService.carrinho$.subscribe(itens => {
           this.carrinhoSize = itens.length;
         });
        }

        obterClienteLogado() {
          this.subscription.add(this.sessionTokenService.getClienteLogado().subscribe(
          cliente => this.clienteLogado = cliente
        ));
        }

       get carrinhoRoute(): string {
      //  console.log(this.sessionTokenService.hasValidSessionToken());
      //  console.log(this.sessionTokenService.getSessionToken());
      //  console.log(this.clienteLogado);
       return this.sessionTokenService.hasValidSessionToken() ? '/carrinho' : '/login/user';
      }

  deslogarUsuario(): void {
    // Limpa o estado de login e remove os dados do sessionStorage
    this.usuarioLogado = false;
    sessionStorage.removeItem('usuarioLogado');
    this.admLogado = false;
    sessionStorage.removeItem('admLogado');

    // Limpa a sessão do token
    this.sessionTokenService.clearSessionToken();

    // Limpa o carrinho de compras
    this.carrinhoService.removerTudo();

    // Limpa o Cliente Logado
    this.sessionTokenService.removeClienteLogado();
  }
}




// import { Component, OnDestroy, OnInit } from '@angular/core';
// import { MatIcon } from '@angular/material/icon';
// import { MatToolbar } from '@angular/material/toolbar';
// import { MatBadge } from '@angular/material/badge';
// import { Usuario } from '../../../../models/usuario.model';
// import { SessionTokenService } from '../../../../services/session-token.service';
// import { LocalStorageService } from '../../../../services/local-storage.service';
// import { SidebarService } from '../../../../services/sidebar.service';
// import { CarrinhoService } from '../../../../services/carrinho.service';
// import { Subscription } from 'rxjs';
// import { MatButton, MatIconButton } from '@angular/material/button';
// import { RouterModule } from '@angular/router';

// @Component({
//   selector: 'app-header-home',
//   standalone: true,
//   imports: [MatToolbar, MatIcon, MatBadge, MatButton, MatIconButton, RouterModule],
//   templateUrl: './header-home.component.html',
//   styleUrl: './header-home.component.css'
// })
// export class HeaderHomeComponent implements OnInit, OnDestroy {

//   usuarioLogado: Usuario | null = null;
//   private subscription = new Subscription();

//   qtdItensCarrinho: number = 0;

//   constructor(private sidebarService: SidebarService,
//     private carrinhoService: CarrinhoService,
//     //private authService: AuthService,
//     private localStorageService: LocalStorageService) {

//   }

//   ngOnInit(): void {
//     this.obterQtdItensCarrinho();
//     //this.obterUsuarioLogado();
//   }

//   ngOnDestroy() {
//     this.subscription.unsubscribe();
//   }

//   clickMenu() {
//     this.sidebarService.toggle();
//   }

//   obterQtdItensCarrinho() {
//     this.carrinhoService.carrinho$.subscribe(itens => {
//       this.qtdItensCarrinho = itens.length
//     });
//   }

//   // obterUsuarioLogado() {
//   //   this.subscription.add(this.authService.getUsuarioLogado().subscribe(
//   //     usuario => this.usuarioLogado = usuario
//   //   ));
//   // }

//   // deslogar() {
//   //   this.authService.removeToken()
//   //   this.authService.removeUsuarioLogado();
//   // }
// }