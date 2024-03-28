import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Fornecedor } from '../models/fornecedor.model';
import { SessionTokenService } from './session-token.service';


@Injectable({
    providedIn: 'root'
})

export class FornecedorService {
    private baseUrl = 'http://localhost:8080/fornecedores';

    constructor(private httpClient: HttpClient, private sessionTokenService: SessionTokenService) {  }

    findAll(): Observable<Fornecedor[]> {
        
        const headers = this.sessionTokenService.getSessionHeader();
    
        if(headers) {
          // Faça a requisição HTTP com o token de autenticação no cabeçalho
          return this.httpClient.get<Fornecedor[]>(this.baseUrl, { headers });
        } else {
          // Se o token de sessão não estiver disponível, faça a requisição sem o token de autenticação
          return this.httpClient.get<Fornecedor[]>(this.baseUrl);
        }
}
      
      findById(id: number): Observable<Fornecedor> {
        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base
    
        if(headers) {
          // Faça a requisição HTTP com o token de autenticação no cabeçalho
          return this.httpClient.get<Fornecedor>(url, { headers });
        } else {
          // Se o token de sessão não estiver disponível, faça a requisição sem o token de autenticação
          return this.httpClient.get<Fornecedor>(url);
        }
      }
      
      insert(fornecedor: Fornecedor): Observable<Fornecedor> {

        const headers = this.sessionTokenService.getSessionHeader();
    
        if(headers) {
          // Faça a requisição HTTP com o token de autenticação no cabeçalho
          console.log(fornecedor);
          return this.httpClient.post<Fornecedor>(this.baseUrl, fornecedor, { headers });
        } else {
          console.log(fornecedor);
          // Se o token de sessão não estiver disponível, faça a requisição sem o token de autenticação
          return this.httpClient.post<Fornecedor>(this.baseUrl, fornecedor);
        }
      }

      private fornecedorInseridoSubject = new Subject<void>();

      fornecedorInserido$ = this.fornecedorInseridoSubject.asObservable();

      notificarFornecedorInserido(): void {
        this.fornecedorInseridoSubject.next();
      }
    
      update(fornecedor: Fornecedor): Observable<Fornecedor> {
    
        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/${fornecedor.id}`; // Concatena o ID à URL base

        if(headers) {
        return this.httpClient.put<Fornecedor>(url, fornecedor, { headers });
        } else {
        return this.httpClient.put<Fornecedor>(url, fornecedor); 
        }
      }
    
      delete(id: number): Observable<any> {

        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base
    
        if(headers) {
          // Faça a requisição HTTP com o token de autenticação no cabeçalho
          return this.httpClient.delete<any>(url, { headers });
        } else {
          
          // Se o token de sessão não estiver disponível, faça a requisição sem o token de autenticação
          return this.httpClient.delete<any>(url);
        }
      }

      getTipoTelefone(): Observable<any[]> {
        const url = 'http://localhost:8080/enum/tipoTelefone';
        return this.httpClient.get<any[]>(url);
      }

      getUF(): Observable<any[]> {
        const url = 'http://localhost:8080/enum/uf';
        return this.httpClient.get<any[]>(url);
      }
}

