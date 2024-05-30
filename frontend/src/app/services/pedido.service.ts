import { Injectable } from "@angular/core";
import { Pedido } from "../models/pedido.model";
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError, catchError } from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class PedidoService {
    
    pedido: Pedido = new Pedido();
    private baseUrl = 'http://localhost:8080/pedidos';

    constructor(private httpClient: HttpClient) { }


    getModalidadePagamento(): Observable<any[]> {
        const url = 'http://localhost:8080/enum/modalidadePagamento';
        return this.httpClient.get<any[]>(url)
          .pipe(
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
