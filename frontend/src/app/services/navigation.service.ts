import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class NavigationService {
    private previousEndpoint: string = '';

    constructor(private router: Router) { }

    getPreviousEndPoint() {
        return this.previousEndpoint;
    }

    navigateTo(endpoint: string): void {
        // Salva o endpoint atual antes de navegar para o próximo
        this.previousEndpoint = this.router.url;
        this.router.navigate([endpoint]);
    }

    navigateBack(): void {
        // Navega de volta para o endpoint anterior
        if (this.previousEndpoint) {
            this.router.navigateByUrl(this.previousEndpoint);
        } else {
            // Se não houver um endpoint anterior, navega para a página inicial
            this.router.navigate(['/']);
        }
    }
}
