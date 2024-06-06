import { Component } from '@angular/core';
import { NavigationService } from '../../../../services/navigation.service';
import { SessionTokenService } from '../../../../services/session-token.service';
import { CarrinhoService } from '../../../../services/carrinho.service';

@Component({
  selector: 'app-sidebar-user',
  standalone: true,
  imports: [],
  templateUrl: './sidebar-user.component.html',
  styleUrl: './sidebar-user.component.css'
})
export class SidebarUserComponent {
  usuarioLogado: boolean = false;

  constructor(
    private navigationService: NavigationService,
    private sessionTokenService: SessionTokenService,
    private carrinhoService: CarrinhoService
  ){}

  editarDados(): void{
    this.navigationService.navigateTo("/user/edit");
  }

  atualizarSenha(): void{
      this.navigationService.navigateTo('/user/senha')
  }

  listarFavoritos(): void{
    this.navigationService.navigateTo('/user/favoritos');
  } 
  listarPedidos(): void{
    this.navigationService.navigateTo('/user/pedidos');
  }
  
    

  deslogarUsuario(): void {
    // Limpa o estado de login e remove os dados do sessionStorage
    // Limpa o estado de login e remove os dados do sessionStorage
    this.usuarioLogado = false;
    sessionStorage.removeItem('usuarioLogado');

    // Limpa a sessão do token
    this.sessionTokenService.clearSessionToken();

    // Limpa o carrinho de compras
    this.carrinhoService.removerTudo();

    // Limpa o Cliente Logado
    this.sessionTokenService.removeClienteLogado();
    this.navigationService.navigateTo('home');
  }
}
