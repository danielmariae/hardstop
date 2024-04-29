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

    // Método para trazer todas as instâncias de Lote de um dado produto de acordo com a paginação
    findByIdProduto(id: number, page?: number, pageSize?: number): Observable<LoteRecebe[]> {

        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/search/idProduto/${id}`; // Concatena o ID à URL base
        console.log(id);

        if (page !== undefined && pageSize !== undefined) {
            const params = new HttpParams()
                .set('page', page.toString())
                .set('pageSize', pageSize.toString());

            if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
                return this.httpClient.get<LoteRecebe[]>(url, { headers: headers, params: params })
                    .pipe(
                        catchError(this.handleError)
                    );
            } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<LoteRecebe[]>(url, { params })
                    .pipe(
                        catchError(this.handleError)
                    );
            }
        } else {
            if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
                return this.httpClient.get<LoteRecebe[]>(url, { headers })
                    .pipe(
                        catchError(this.handleError)
                    );
            } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<LoteRecebe[]>(url)
                    .pipe(
                        catchError(this.handleError)
                    );
            }
        }
    }

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
        let errorMessage = 'Erro desconhecido';
        let errorMessage2 = 'Erro desconhecido';

        // Verifica se há uma resposta de erro do servidor
        if (error.error && error.error.message) {
            errorMessage = `Erro: ${error.error.message}`;
            errorMessage2 = `Erro: ${error.message}`;
            if (error.error.code) {
                errorMessage += `, Code: ${error.error.code}`;
            }
            if (error.error.errors) {
                errorMessage += `, Errors: ${JSON.stringify(error.error.errors)}`;
            }
        } else if (error.error instanceof ErrorEvent) {
            // Trata erros do lado do cliente
            errorMessage = `Erro: ${error.error.message}`;
        } else {
            // Trata outros tipos de erros
            errorMessage = `Código: ${error.status}, Mensagem: ${error.statusText}`;
            if (error.url) {
                errorMessage += `, URL: ${error.url}`;
            }
            if (error.message) {
                errorMessage += `, Erro: ${error.message}`;
            }
        }

        console.error('Erro1', errorMessage);
        console.log('Erro2:', errorMessage2);
        return throwError(() => new Error(errorMessage)); // Retorna a mensagem de erro diretamente
    }

    getStatus(): Observable<any[]> {
        const url = 'http://localhost:8080/enum/statusDoLote';
        return this.httpClient.get<any[]>(url)
          .pipe(
            catchError(this.handleError)
          );
      }
    

    }