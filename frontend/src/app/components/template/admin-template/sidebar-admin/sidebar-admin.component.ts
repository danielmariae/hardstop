import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavigationService } from '../../../../services/navigation.service';
import { SidebarService } from '../../../../services/sidebar.service';
import { SessionTokenService } from '../../../../services/session-token.service';

@Component({
  selector: 'app-sidebar-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar-admin.component.html',
  styleUrl: './sidebar-admin.component.css'
})
export class SidebarAdminComponent {

  admLogado: boolean = false;

  constructor(
    private navigationService: NavigationService,
    private sessionTokenService: SessionTokenService,
  ){}


  navegarParaFornecedor(): void{
    this.navigationService.navigateTo("adm/fornecedores/");
  }
  
  navegarParaFuncionario(): void{
    this.navigationService.navigateTo("adm/funcionarios/");
  } 

  navegarParaProdutos(): void{
    this.navigationService.navigateTo("adm/produtos");
  }

  navegarParaClientes(): void{
    this.navigationService.navigateTo("/adm/clientes");
  }
  
  navegarParaLotes(): void{
    this.navigationService.navigateTo("/adm/lotes");
  }

  navegarParaPedidosFiltrados(): void{
    this.navigationService.navigateTo("adm/pedidos");
  }
  
  navegarParaPedidosStatus(): void {
    this.navigationService.navigateTo("adm/pedidos/status");
  }
  deslogarUsuario(){
    // Limpa o estado de login e remove os dados do sessionStorage
    this.admLogado = false;
    sessionStorage.removeItem('admLogado');

    // Limpa a sessão do token
    this.sessionTokenService.clearSessionToken();
    
    // Limpa o Funcionário Logado
    this.sessionTokenService.removeFuncionarioLogado();
    this.navigationService.navigateTo('login/adm');
    
}
}
