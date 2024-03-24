import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Fornecedor } from '../models/fornecedor.model';
import { SessionTokenService } from './session-token.service';


@Injectable({
    providedIn: 'root'
})

export class FornecedorService {
    private baseUrl = 'http://localhost:8080/fornecedores';

    constructor(private httpClient: HttpClient, private sessionTokenService: SessionTokenService) {  }

    findAll(): Observable<Fornecedor[]> {
        // Obtenha o token de sessão do armazenamento local
        const token = this.sessionTokenService.getSessionToken();
    
        // Verifique se o token de sessão está disponível
        if (token) {
          // Se estiver disponível, adicione-o ao cabeçalho da requisição
          //const headers = new HttpHeaders().set('Authorization', sessionToken.token);
          const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + token
          });
    
          // Faça a requisição HTTP com o token de autenticação no cabeçalho
          return this.httpClient.get<Fornecedor[]>(this.baseUrl, { headers });
        } else {
          // Se o token de sessão não estiver disponível, faça a requisição sem o token de autenticação
          return this.httpClient.get<Fornecedor[]>(this.baseUrl);
        }
      }
      
      findById(id: string): Observable<Fornecedor> {
        return this.httpClient.get<Fornecedor>(`${this.baseUrl}/${id}`);
      }
      
      insert(fornecedor: Fornecedor): Observable<Fornecedor> {
        return this.httpClient.post<Fornecedor>(this.baseUrl, fornecedor);
      }
    
      update(fornecedor: Fornecedor): Observable<Fornecedor> {
        return this.httpClient.put<Fornecedor>(`${this.baseUrl}/${fornecedor.id}`, fornecedor);
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

