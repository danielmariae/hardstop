import { Injectable } from "@angular/core";
import { Pedido } from "../models/pedido.model";
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError, catchError } from 'rxjs';
import { SessionTokenService } from "./session-token.service";
import { Cliente } from "../models/cliente.model";
import { ListaEndereco } from "../models/endereco.model";
import { PedidoRecebe } from "../models/pedidoRecebe.model";
import { CartaoRecebe } from "../models/cartaoRecebe.model";
import { StatusDoPedido } from "../models/statusDoPedido.model";
@Injectable({
    providedIn: 'root'
})

export class PedidoService {
    
    pedido: Pedido = new Pedido();
    private baseUrl = 'http://localhost:8080/pedidos';

    constructor(private httpClient: HttpClient,
                private sessionTokenService: SessionTokenService
    ) { }


    getModalidadePagamento(): Observable<any[]> {
      const url = 'http://localhost:8080/enum/modalidadePagamento';
      return this.httpClient.get<any[]>(url)
        .pipe(
          catchError(this.handleError)
        );
    }

    getStatusPedido(): Observable<any[]> {
      const url = 'http://localhost:8080/enum/statusPedido';
      return this.httpClient.get<any[]>(url)
        .pipe(
          catchError(this.handleError)
        );
    }

    insert(pedido: Pedido): Observable<Pedido> {

      const url = 'http://localhost:8080/pedidos/insert';
      const headers = this.sessionTokenService.getSessionHeader();

      for (const i in pedido.itemDaVenda) {
        console.log(i);
      }
      
      if(headers) {
        console.log(pedido);
        console.log(headers);
      return this.httpClient.post<Pedido>(url, pedido, { headers });
      } else {
        return this.httpClient.post<Pedido>(url, pedido);
      }
    }

      insertEndereco(endereco: ListaEndereco): Observable<ListaEndereco> {

        const url = 'http://localhost:8080/pedidos/insert/endereco';
        const headers = this.sessionTokenService.getSessionHeader();
        if(headers) {
        //  console.log(endereco);
        //  console.log(headers);
        return this.httpClient.post<ListaEndereco>(url, endereco, { headers });
        
        } else {
          return this.httpClient.post<ListaEndereco>(url, endereco);
          
        }
    }

    findAll(): Observable<PedidoRecebe[]> {

      const url = 'http://localhost:8080/pedidos/search/pedidos/';
      const headers = this.sessionTokenService.getSessionHeader();
      if(headers) {
        console.log(headers);
      return this.httpClient.get<PedidoRecebe[]>(url, { headers });
      
      } else {
        return this.httpClient.get<PedidoRecebe[]>(url);
        
      }
    }

    findAllFuncStatus(idStatus: number): Observable<PedidoRecebe[]> {

      const url = `http://localhost:8080/pedidos/func/search/status/all/${idStatus}`;
      const headers = this.sessionTokenService.getSessionHeader();
      console.log(this.sessionTokenService.getSessionToken());
      console.log(headers);
      console.log(url);
      if(headers) {
        // console.log(headers);
      return this.httpClient.get<PedidoRecebe[]>(url, { headers });
      
      } else {
        return this.httpClient.get<PedidoRecebe[]>(url);
        
      }
    }

    
    findAllFuncCliente(id: number): Observable<PedidoRecebe[]> {

      const url = 'http://localhost:8080/pedidos/func/search/pedidos/cliente/${id}';
      const headers = this.sessionTokenService.getSessionHeader();
      if(headers) {
        console.log(headers);
      return this.httpClient.get<PedidoRecebe[]>(url, { headers });
      
      } else {
        return this.httpClient.get<PedidoRecebe[]>(url);
        
      }
    }

    deletePedidoByFunc(id: number): Observable<any> {

      const headers = this.sessionTokenService.getSessionHeader();
      const url = `${this.baseUrl}/func/delete/${id}`; // Concatena o ID à URL base

      if (headers) {
          // Faz a requisição HTTP com o token de autenticação no cabeçalho
          return this.httpClient.delete<any>(url, { headers })
              .pipe(
                  catchError(this.handleError)
              );
      } else {
          // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
          return this.httpClient.delete<any>(url)
              .pipe(
                  catchError(this.handleError)
              );
      }
  }

    // ESSE AQUI DEVE SER USADO POR ADM
    findById(id: number): Observable<PedidoRecebe> {
      const url = `http://localhost:8080/pedidos/func/search/pedidos/${id}`;
      const headers = this.sessionTokenService.getSessionHeader();
      if(headers) {
        return this.httpClient.get<PedidoRecebe>(url, { headers });
      } else {
        return this.httpClient.get<PedidoRecebe>(url);
      }
    }

    // ESSE AQUI DEVE SER USADO POR CLIENTE
    findByIdCliente(id: number): Observable<PedidoRecebe> {

      const url = `http://localhost:8080/pedidos/search/pedidos/${id}`;
      const headers = this.sessionTokenService.getSessionHeader();
      if(headers) {
        return this.httpClient.get<PedidoRecebe>(url, { headers });
      } else {
        return this.httpClient.get<PedidoRecebe>(url);
      }
    }

    updatePedidoSeparadoEstoque(idPedido: number, idStatus: number): Observable<PedidoRecebe> {
      const url = 'http://localhost:8080/pedidos/patch/status/';
      const headers = this.sessionTokenService.getSessionHeader();
      // const params = new HttpParams()
      //           .set('idPedido', idPedido)
      //           .set('idStatus', idStatus);

      const body = {
        idPedido: idPedido,
        idStatus: idStatus,
        codigoDeRastreamento: null 
      };

      if(headers) {
      return this.httpClient.patch<PedidoRecebe>(url, body, { headers });
      } else {
        return this.httpClient.patch<PedidoRecebe>(url, body);
      }
    }

    updatePedidoEntregue(idPedido: number, idStatus: number, codRast: string): Observable<PedidoRecebe> {
      const url = 'http://localhost:8080/pedidos/patch/status/';
      const headers = this.sessionTokenService.getSessionHeader();
      // const params = new HttpParams()
      //           .set('idPedido', idPedido)
      //           .set('idStatus', idStatus);
      // const body = JSON.stringify(params);

      const body = {
        idPedido: idPedido,
        idStatus: idStatus,
        codigoDeRastreamento: codRast,
        idTransportadora: null // Adicione aqui se tiver de um valor
      };

      if(headers) {
      return this.httpClient.patch<PedidoRecebe>(url, body, { headers });
      } else {
        return this.httpClient.patch<PedidoRecebe>(url, body);
      }
    }

    updatePedidoEntregueCliente(idPedido: number, idStatus: number): Observable<PedidoRecebe> {
      const url = 'http://localhost:8080/pedidos/patch/status/';
      const headers = this.sessionTokenService.getSessionHeader();
      // const params = new HttpParams()
      //           .set('idPedido', idPedido)
      //           .set('idStatus', idStatus);

      const body = {
        idPedido: idPedido,
        idStatus: idStatus,
      };

      if(headers) {
      return this.httpClient.patch<PedidoRecebe>(url, body, { headers });
      } else {
        return this.httpClient.patch<PedidoRecebe>(url, body);
      }
    }

    updatePedidoDesistido(idPedido: number, idStatus: number): Observable<PedidoRecebe> {
      const url = 'http://localhost:8080/pedidos/patch/status/';
      const headers = this.sessionTokenService.getSessionHeader();
      // const params = new HttpParams()
      //           .set('idPedido', idPedido)
      //           .set('idStatus', idStatus);

      const body = {
        idPedido: idPedido,
        idStatus: idStatus,
      };

      if(headers) {
      return this.httpClient.patch<PedidoRecebe>(url, body, { headers });
      } else {
        return this.httpClient.patch<PedidoRecebe>(url, body);
      }
    }

    findByIdStatus(id: number): Observable<StatusDoPedido> {
      const url = `http://localhost:8080/pedidos/search/pedidos/status/${id}`;
      const headers = this.sessionTokenService.getSessionHeader();
      if(headers) {
        return this.httpClient.get<StatusDoPedido>(url, { headers });
      } else {
        return this.httpClient.get<StatusDoPedido>(url);
      }
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

    findCartaoById(id: number): Observable<CartaoRecebe> {
      const url = `http://localhost:8080/pedidos/search/pedidos/cartao/${id}`;
      const headers = this.sessionTokenService.getSessionHeader();

      if(headers) {
        console.log(headers);
        return this.httpClient.get<CartaoRecebe>(url, { headers });
      } else {
        return this.httpClient.get<CartaoRecebe>(url);
      }
    }
    getFormaPagamento(): Observable<any[]> {
      const url = 'http://localhost:8080/enum/modalidadePagamento';
      return this.httpClient.get<any[]>(url)
        .pipe(
          catchError(this.handleError)
        );
    }
    getStatusPedidoPadrao(): Observable<any[]> {
      const url = 'http://localhost:8080/enum/statusDoPedido/default';
      return this.httpClient.get<any[]>(url)
        .pipe(
          catchError(this.handleError)
        );
    }

  }
