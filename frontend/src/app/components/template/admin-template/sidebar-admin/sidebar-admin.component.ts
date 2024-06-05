import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavigationService } from '../../../../services/navigation.service';
import { SidebarService } from '../../../../services/sidebar.service';

@Component({
  selector: 'app-sidebar-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar-admin.component.html',
  styleUrl: './sidebar-admin.component.css'
})
export class SidebarAdminComponent {

  constructor(
    private navigationService: NavigationService,
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
  
}
