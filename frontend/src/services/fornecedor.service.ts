import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Fornecedor } from '../models/fornecedor.model';


@Injectable({
    providedIn: 'root'
})

export class FornecedorService {
    private baseUrl = 'http://localhost:8080/fornecedores';

    constructor(private httpClient: HttpClient) {}

    findAll(): Observable<Fornecedor[]> {
        return this.httpClient.get<Fornecedor[]>(this.baseUrl);
    }
    
    findById(id: string): Observable<Fornecedor> {
        return this.httpClient.get<Fornecedor>(`${this.baseUrl}/${id}`)
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
}

