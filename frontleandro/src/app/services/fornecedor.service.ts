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
    
      delete(fornecedor: Fornecedor): Observable<any> {
        return this.httpClient.delete<any>(`${this.baseUrl}/${fornecedor.id}`);
      }

    // findAll(): Observable<Fornecedor[]> {
    //     return this.httpClient.get<Fornecedor[]>(this.baseUrl);
    // }
    
    // findById(id: string): Observable<Fornecedor> {
    //     return this.httpClient.get<Fornecedor>(`${this.baseUrl}/${id}`)
    // }
    
    // insert(fornecedor: Fornecedor): Observable<Fornecedor> {
    //     return this.httpClient.post<Fornecedor>(this.baseUrl, fornecedor);
    // }

    // update(fornecedor: Fornecedor): Observable<Fornecedor> {
    //     return this.httpClient.put<Fornecedor>(`${this.baseUrl}/${fornecedor.id}`, fornecedor);
    // }

    // delete(fornecedor: Fornecedor): Observable<any> {
    //     return this.httpClient.delete<any>(`${this.baseUrl}/${fornecedor.id}`);
    // }
}

