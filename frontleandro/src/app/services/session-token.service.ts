import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SessionTokenService {
  private baseUrl = 'http://localhost:8080/auth'; // URL base da sua API

  constructor(private httpClient: HttpClient) { }

  // Método para salvar o token de sessão no armazenamento local
  saveSessionToken(token: string) {
    localStorage.setItem('sessionToken', token);
  }

  // Método para obter o token de sessão do armazenamento local
  getSessionToken(): string | null {
    return localStorage.getItem('sessionToken');
  }

  // Método para limpar o token de sessão do armazenamento local
  clearSessionToken() {
    localStorage.removeItem('sessionToken');
  }
  
  getSessionHeader(): HttpHeaders | null {
    const token = this.getSessionToken();
    
        // Verifique se o token de sessão está disponível
        if (token) {
          // Se estiver disponível, adicione-o ao cabeçalho da requisição
          //const headers = new HttpHeaders().set('Authorization', sessionToken.token);
          const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + token
          });
          return headers;
        } else {
          return null;
  }
}

  // Método para verificar se o usuário está autenticado
  isAuthenticated(): Observable<boolean> {
    const sessionToken = this.getSessionToken();
    // Verificar se o token de sessão está presente e válido (adicione sua lógica de validação aqui)
    const isAuthenticated = !!sessionToken; // Neste exemplo, estamos verificando apenas se o token de sessão está presente
    return of(isAuthenticated);
  }

  // Exemplo de método para fazer uma solicitação HTTP para a API para autenticar o Funcionario
    authenticateUserF(username: string, password: string): Observable<any> {
     const loginUrl = `${this.baseUrl}/loginF`;
     return this.httpClient.post(loginUrl, { login: username, senha: password });
    }

    // Exemplo de método para fazer uma solicitação HTTP para a API para autenticar o Cliente
    authenticateUserC(username: string, password: string): Observable<any> {
      const loginUrl = `${this.baseUrl}/loginU`;
      return this.httpClient.post(loginUrl, { login: username, senha: password });
     }
}
