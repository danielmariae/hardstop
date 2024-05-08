import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SessionTokenService } from './services/session-token.service';
import { CommonModule } from '@angular/common';
import { NavigationService } from './services/navigation.service';
import { ClienteService } from './services/cliente.service';
import { FuncionarioService } from './services/funcionario.service';
import { Observable, catchError, of } from 'rxjs';
import { Perfil } from './models/perfil.model';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'frontend';
  usuarioLogado = false;
  admLogado = false;

  constructor(
    private sessionTokenService: SessionTokenService,
    private navigationService: NavigationService,
    private clienteService: ClienteService,
    private funcionarioService: FuncionarioService
  ){}

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

    this.getPerfil();
  }

  getPerfil(): void {
    let profile: Observable<Perfil>;
  
    profile = this.clienteService.getPerfil();
  
    profile.subscribe(
      response => {
        if (response && response.id === 0) {
          this.usuarioLogado = true;
        }
      },
      error => {
        // Verifica se o erro é do tipo HttpErrorResponse e se o status é 400
        if (error.status !== 403) {
          console.error('Ocorreu um erro ao obter o perfil:', error);
        }
      }
    );
  
    // O mesmo para a função getPerfil() de funcionário
    let funcionarioProfile: Observable<Perfil>;
    funcionarioProfile = this.funcionarioService.getPerfil();
  
    funcionarioProfile.subscribe(
      response => {
        if (response && response.id === 1 || response.id === 2) {
          this.admLogado = true;
        }
      },
      error => {
        // Verifica se o erro é do tipo HttpErrorResponse e se o status é 400
        if (error.status !== 403) {
          console.error('Ocorreu um erro ao obter o perfil do funcionário:', error);
        }
      }
    );
  
    console.log('Usuário está logado? ', this.usuarioLogado);
    console.log('Adm está logado? ', this.admLogado);
  }
  
  

  deslogarUsuario(): void {
    this.sessionTokenService.clearSessionToken();
    this.navigationService.navigateTo('/home');
    
  }
}

