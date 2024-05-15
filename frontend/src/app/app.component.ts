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
  usuarioLogado: boolean = false;
  admLogado: boolean = false;

  constructor(
    private sessionTokenService: SessionTokenService,
    private navigationService: NavigationService,
    private clienteService: ClienteService,
    private funcionarioService: FuncionarioService
  ){}

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
}

