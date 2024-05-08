import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SessionTokenService } from './services/session-token.service';
import { CommonModule } from '@angular/common';
import { NavigationService } from './services/navigation.service';


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
  constructor(
    private sessionTokenService: SessionTokenService,
    private navigationService: NavigationService,
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

    const headers = this.sessionTokenService.getSessionHeader();
    
    if(headers)
      this.usuarioLogado = true;
    else
      this.usuarioLogado = false;

      console.log(this.usuarioLogado);
  }

  deslogarUsuario(): void {
    this.sessionTokenService.clearSessionToken();
    this.navigationService.navigateTo('/home');
    
  }
}

