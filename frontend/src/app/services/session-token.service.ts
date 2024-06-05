import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { ClienteService } from './cliente.service';
import { FuncionarioService } from './funcionario.service';
import { Cliente } from '../models/cliente.model';
import { LocalStorageService } from './local-storage.service';
import { Funcionario } from '../models/funcionario.model';

@Injectable({
  providedIn: 'root'
})
export class SessionTokenService {
  private baseUrl = 'http://localhost:8080/auth'; // URL base da sua API
  private loginAdmSubject = new Subject<void>();
  private loginClienteSubject = new Subject<void>();
  private clienteLogadoKey = 'clienteLogado';
  private funcionarioLogadoKey = 'funcionarioLogado';
  private clienteLogadoSubject = new BehaviorSubject<Cliente | null>(null);
  private funcionarioLogadoSubject = new BehaviorSubject<Funcionario | null>(null);
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidSessionToken());

  loginAdmSuccess$ = this.loginAdmSubject.asObservable();
  loginClienteSuccess$ = this.loginClienteSubject.asObservable();


  constructor(
    private httpClient: HttpClient,
    private localStorageService: LocalStorageService) { }

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

  // Método para verificar se existe um token de sessão válido
  hasValidSessionToken(): boolean {
    const sessionToken = this.getSessionToken();
    // Verificar se o token de sessão está presente e válido (adicione sua lógica de validação aqui)
    return !!sessionToken; // Verifique se o token é válido
  }

  // Método para verificar se o usuário está autenticado
  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  // Exemplo de método para fazer uma solicitação HTTP para a API para autenticar o Funcionario
    authenticateUserF(username: string, password: string): Observable<any> {
     const loginUrl = `${this.baseUrl}/loginF`;
     const params = {
      login: username, 
      senha: password
    }

    return this.httpClient.post(loginUrl, params, {observe: 'response'}).pipe(
      tap((res: any) => {
        console.log(res);
        const authToken = res.headers.get('authorization') ?? '';
        if (authToken) {
          // console.log(authToken);
          this.saveSessionToken(authToken);
          const funcionarioLogado = res.body;
         // console.log(funcionarioLogado);
          if (funcionarioLogado) {
            this.setFuncionarioLogado(funcionarioLogado);
            this.funcionarioLogadoSubject.next(funcionarioLogado);
          }
        }
        this.notifyLoginAdmSucess();
      })
    );
    }

  setFuncionarioLogado(funcionario: Funcionario): void {
      this.localStorageService.setItem(this.funcionarioLogadoKey, funcionario);
  }

  getFuncionarioLogado() {
    return this.funcionarioLogadoSubject.asObservable();
  }

  removeFuncionarioLogado(): void {
    this.localStorageService.removeItem(this.funcionarioLogadoKey);
    this.funcionarioLogadoSubject.next(null);
  }

    // Exemplo de método para fazer uma solicitação HTTP para a API para autenticar o Cliente
    authenticateUserC(username: string, password: string): Observable<any> {
      const loginUrl = `${this.baseUrl}/loginU`;
      const params = {
        login: username, 
        senha: password
      }

      return this.httpClient.post(loginUrl, params, {observe: 'response'}).pipe(
        tap((res: any) => {
         // console.log(res);
          const authToken = res.headers.get('authorization') ?? '';
          if (authToken) {
            // console.log(authToken);
            this.saveSessionToken(authToken);
            const usuarioLogado = res.body;
            // console.log(usuarioLogado);
            if (usuarioLogado) {
              this.setClienteLogado(usuarioLogado);
              this.clienteLogadoSubject.next(usuarioLogado);
            }
          }
          this.notifyLoginClienteSucess();
        })
      );
     }

    setClienteLogado(cliente: Cliente): void {
        this.localStorageService.setItem(this.clienteLogadoKey, cliente);
    }

    getClienteLogado() {
      return this.clienteLogadoSubject.asObservable();
    }

    removeClienteLogado(): void {
      this.localStorageService.removeItem(this.clienteLogadoKey);
      this.clienteLogadoSubject.next(null);
    }

     notifyLoginAdmSucess(){
      this.loginAdmSubject.next();
     }
     
     notifyLoginClienteSucess(){
      this.loginClienteSubject.next();
     }


}
