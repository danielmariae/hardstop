import { Component } from '@angular/core';
import { NavigationService } from '../../../../services/navigation.service';

@Component({
  selector: 'app-sidebar-user',
  standalone: true,
  imports: [],
  templateUrl: './sidebar-user.component.html',
  styleUrl: './sidebar-user.component.css'
})
export class SidebarUserComponent {
  constructor(
    private navigationService: NavigationService,
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
}
