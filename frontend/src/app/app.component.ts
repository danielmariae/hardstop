import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'frontend';

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
  }
}

