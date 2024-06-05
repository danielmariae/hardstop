import { Component } from '@angular/core';
import { SessionTokenService } from '../../../../services/session-token.service';
import { NavigationService } from '../../../../services/navigation.service';
import { FormBuilder, FormControl } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { SidebarService } from '../../../../services/sidebar.service';

@Component({
  selector: 'app-header-admin',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './header-admin.component.html',
  styleUrl: './header-admin.component.css'
})
export class HeaderAdminComponent {
  usuarioLogado: boolean = false;
  admLogado: boolean = false;

  buscadorForm: FormControl;

  constructor(
    private sessionTokenService: SessionTokenService,
    private navigationService: NavigationService,
    private formBuilder: FormBuilder,
    private sidebarService: SidebarService
  ){
    this.buscadorForm = this.formBuilder.control('');
  }

  
  toggleSidebar() {
    this.sidebarService.toggleSidebar();
  }

  ngOnInit(
  ): void {
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

  deslogarUsuario(): void {
    // Limpa o estado de login e remove os dados do sessionStorage
    this.usuarioLogado = false;
    sessionStorage.removeItem('usuarioLogado');
    this.admLogado = false;
    sessionStorage.removeItem('admLogado');

    // Limpa a sessão do token
    this.sessionTokenService.clearSessionToken();
  }

  buscarProduto(): void{
    if(this.buscadorForm.value !== null){
      console.log("Buscando por: ", this.buscadorForm.value);
      this.navigationService.navigateTo('home/buscador/'+this.buscadorForm.value);  
    }else{
      this.navigationService.navigateTo('home/buscador/%');
    }
  }

}
