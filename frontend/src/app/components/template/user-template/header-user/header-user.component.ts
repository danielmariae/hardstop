import { Component, OnInit } from '@angular/core';
import { SessionTokenService } from '../../../../services/session-token.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';
import { NavigationService } from '../../../../services/navigation.service';

@Component({
  selector: 'app-header-user',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './header-user.component.html',
  styleUrl: './header-user.component.css'
})
export class HeaderUserComponent implements OnInit {
  usuarioLogado: boolean = false;

  buscadorForm: FormControl;

  constructor(
      private sessionTokenService: SessionTokenService,
      private navigationService: NavigationService,
      private formBuilder: FormBuilder,
  ){
    this.buscadorForm = this.formBuilder.control('');
  }
  
  ngOnInit(): void {
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
    
        this.sessionTokenService.loginClienteSuccess$.subscribe(() => {
          this.usuarioLogado = true;
          sessionStorage.setItem('usuarioLogado', JSON.stringify(true));
        });
  }

  deslogarUsuario(): void {
    // Limpa o estado de login e remove os dados do sessionStorage
    this.usuarioLogado = false;
    sessionStorage.removeItem('usuarioLogado');

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
