import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, Subject, throwError, catchError } from 'rxjs';
import { PlacaMae, Processador, Produto } from '../models/produto.model';
import { SessionTokenService } from './session-token.service';
import { LoteEnvia } from '../models/loteEnvia.model';
import { LoteRecebe } from '../models/loteRecebe.model';
import { LoteRecebeClass } from '../models/loteRecebeClass.model';


@Injectable({
    providedIn: 'root'
})

export class LoteService {

    private baseUrl = 'http://localhost:8080/lotes';


    constructor(private httpClient: HttpClient, private sessionTokenService: SessionTokenService) { }


    // Método para realizar contagens. Envolvido com a paginação
    count(id: number): Observable<number> {
        const headers = this.sessionTokenService.getSessionHeader();

        return this.httpClient.get<number>(`${this.baseUrl}/count/${id}`);
    }
    // Método para realizar contagens. Envolvido com a paginação
    countAll(): Observable<number> {
        const headers = this.sessionTokenService.getSessionHeader();

        return this.httpClient.get<number>(`${this.baseUrl}/count/`);
    }

    // Método para trazer todas as instâncias de Lote de um dado produto de acordo com a paginação
    // findByIdProduto(id: number, page?: number, pageSize?: number): Observable<LoteRecebe[]> {

    //     const headers = this.sessionTokenService.getSessionHeader();
    //     const url = `${this.baseUrl}/search/idProduto/${id}`; // Concatena o ID à URL base
    //     console.log(id);

    //     if (page !== undefined && pageSize !== undefined) {
    //         const params = new HttpParams()
    //             .set('page', page.toString())
    //             .set('pageSize', pageSize.toString());

    //         if (headers) {
    //             // Faz a requisição HTTP com o token de autenticação no cabeçalho
    //             return this.httpClient.get<LoteRecebe[]>(url, { headers: headers, params: params })
    //                 .pipe(
    //                     catchError(this.handleError)
    //                 );
    //         } else {
    //             // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
    //             return this.httpClient.get<LoteRecebe[]>(url, { params })
    //                 .pipe(
    //                     catchError(this.handleError)
    //                 );
    //         }
    //     } else {
    //         if (headers) {
    //             // Faz a requisição HTTP com o token de autenticação no cabeçalho
    //             return this.httpClient.get<LoteRecebe[]>(url, { headers })
    //                 .pipe(
    //                     catchError(this.handleError)
    //                 );
    //         } else {
    //             // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
    //             return this.httpClient.get<LoteRecebe[]>(url)
    //                 .pipe(
    //                     catchError(this.handleError)
    //                 );
    //         }
    //     }
    // }

    findByIdProdutoEnviado(id: number, page?: number, pageSize?: number): Observable<LoteRecebeClass[]> {

        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/search/idProduto2/${id}`; // Concatena o ID à URL base
        console.log(id);

        if (page !== undefined && pageSize !== undefined) {
            const params = new HttpParams()
                .set('page', page.toString())
                .set('pageSize', pageSize.toString());

            if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
                return this.httpClient.get<LoteRecebeClass[]>(url, { headers: headers, params: params })
                    .pipe(
                        catchError(this.handleError)
                    );
            } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<LoteRecebeClass[]>(url, { params })
                    .pipe(
                        catchError(this.handleError)
                    );
            }
        } else {
            if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
                return this.httpClient.get<LoteRecebeClass[]>(url, { headers })
                    .pipe(
                        catchError(this.handleError)
                    );
            } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<LoteRecebeClass[]>(url)
                    .pipe(
                        catchError(this.handleError)
                    );
            }
        }
        
    }

    findByAll(page?: number, pageSize?: number): Observable<LoteRecebeClass[]> {
        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/search/all`; // Concatena o ID à URL base

        if (page !== undefined && pageSize !== undefined) {
            const params = new HttpParams()
                .set('page', page.toString())
                .set('pageSize', pageSize.toString());

            if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
                return this.httpClient.get<LoteRecebeClass[]>(url, { headers: headers, params: params })
                    .pipe(
                        catchError(this.handleError)
                    );
            } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<LoteRecebeClass[]>(url, { params })
                    .pipe(
                        catchError(this.handleError)
                    );
            }
        } else {
            if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
                return this.httpClient.get<LoteRecebeClass[]>(url, { headers })
                    .pipe(
                        catchError(this.handleError)
                    );
            } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<LoteRecebeClass[]>(url)
                    .pipe(
                        catchError(this.handleError)
                    );
            }
        }

    }


    // Método para trazer uma única instância de Lote do banco de dados do servidor de acordo com seu id
    findById(id: number): Observable<LoteRecebe> {
        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/search/id/${id}`; // Concatena o ID à URL base

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.get<LoteRecebe>(url, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.get<LoteRecebe>(url)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }

    // Método para inserir uma nova instância de Lote no banco de dados do servidor
    insert(lote: LoteEnvia): Observable<LoteEnvia> {

        const url = `${this.baseUrl}/insert/lote`; // Concatena o ID à URL base
        const headers = this.sessionTokenService.getSessionHeader();

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.post<any>(url, lote, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.post<any>(url, lote)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }
    
    // Método para inserir uma nova instância de Lote no banco de dados do servidor
    insertTeste(lote: LoteEnvia): Observable<LoteEnvia> {

        const url = `${this.baseUrl}/insert/lote`; // Concatena o ID à URL base
        const headers = this.sessionTokenService.getSessionHeader();

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.post<any>(url, lote, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.post<any>(url, lote)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }

    // Método para alterar uma única instância de Lote no banco de dados do servidor
    update(produto: Produto, tipoProduto: string, id: number): Observable<Produto> {
        let url: string;

        if(tipoProduto == 'processadores') {
            url = this.baseUrl + '/update/processador/' + id; 
        } else if(tipoProduto == 'placas mãe') {
            url = this.baseUrl + '/update/placaMae/' + id; 
        } else {
            url = this.baseUrl + '/update/produto/' + id; // É apenas Produto do tipo puro 
        }

        const headers = this.sessionTokenService.getSessionHeader();

        if (headers) {
            return this.httpClient.put<Produto>(url, produto, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            return this.httpClient.put<Produto>(url, produto)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }

    // Método para apagar uma única instância de Lote do banco de dados do servidor
    ativaLote(id: number): Observable<LoteRecebe> {

        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/patch/ativalote/${id}`; // Concatena o ID à URL base

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.get<LoteRecebe>(url, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.get<LoteRecebe>(url)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }


    // Método para apagar uma única instância de Lote do banco de dados do servidor
    delete(id: number): Observable<any> {

        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/delete/${id}`; // Concatena o ID à URL base

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.delete(url, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.delete(url)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }

    private handleError(error: HttpErrorResponse) {
      let errorDetails: any = {}; // Objeto para armazenar os detalhes do erro
    
     if (error.error && error.error.code === "500" || error.error.code === "400") {
        // Trata erro com código "500"
        errorDetails.code = error.error.code;
        errorDetails.message = error.error.message;
        errorDetails.subjectError = {
          subjectName: error.error.subjectError.subjectName,
          message: error.error.subjectError.message
        };
      } else if (error.error instanceof ErrorEvent) {
        // Trata erros do lado do cliente
        errorDetails.error = error.error.message;
      } else {
        // Trata outros tipos de erros
        errorDetails.code = error.status;
        errorDetails.message = error.statusText;
      }
    
      console.error(errorDetails); // Log dos detalhes do erro
    
      return throwError(() => errorDetails); // Retorna os detalhes do erro como uma Observable
    }
          
    getStatus(): Observable<any[]> {
        const url = 'http://localhost:8080/enum/statusDoLote';
        return this.httpClient.get<any[]>(url)
          .pipe(
            catchError(this.handleError)
          );
      }
    

    }