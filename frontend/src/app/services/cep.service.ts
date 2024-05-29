import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {ListaEndereco} from "../models/endereco.model";

@Injectable({
  providedIn: 'root'
})
export class CepService {
  private baseUrl = 'https://viacep.com.br/ws/'
  constructor(
    private httpClient: HttpClient
  ) { }

  findByStringCep(cep: string): Observable<ListaEndereco> {
    const url = `${this.baseUrl}/${cep}/json`; // Concatena o ID à URL base

    return this.httpClient.get<ListaEndereco>(url).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorDetails: any = {}; // Objeto para armazenar os detalhes do erro

    // Verifica se há uma resposta de erro do servidor
    if (error.error && error.error.message) {
      errorDetails.error = error.error.message;
      if (error.error.code) {
        errorDetails.code = error.error.code;
      }
      if (error.error.errors) {
        errorDetails.errors = error.error.errors;
      }
    }
    else if (error.error instanceof ErrorEvent) {
      // Trata erros do lado do cliente
      errorDetails.error = error.error.message;
    } else {
      // Trata outros tipos de erros
      errorDetails.status = error.status;
      errorDetails.statusText = error.statusText;
      if (error.url) {
        errorDetails.url = error.url;
      }
      if (error.message) {
        errorDetails.error = error.message;
      }
    }

    console.error(errorDetails); // Log dos detalhes do erro

    return throwError(() => errorDetails); // Retorna os detalhes do erro como uma Observable
  }
}
