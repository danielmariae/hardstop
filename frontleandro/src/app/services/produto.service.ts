import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, Subject, throwError, catchError } from 'rxjs';
import { PlacaMae, Processador, Produto } from '../models/Produto.model';
import { SessionTokenService } from './session-token.service';

@Injectable({
    providedIn: 'root'
})

export class ProdutoService {
    produto: Produto = new Produto();
    private baseUrl = 'http://localhost:8080/produtos';

    // Comunicar os componentes do Angular sobre alguma mudança e direcionar para a página de lista de produtos
    private produtoInseridoSubject = new Subject<void>();
    produtoInserido$ = this.produtoInseridoSubject.asObservable();


    constructor(private httpClient: HttpClient, private sessionTokenService: SessionTokenService) { }

    // Comunicar os componentes do Angular sobre alguma mudança e direcionar para a página de lista de produtos
    notificarProdutoInserido(): void {
        this.produtoInseridoSubject.next();
    }

    // Método para trazer todas as instâncias de Produto do banco de dados do servidor
    findTodos(): Observable<Produto[]> {

        const headers = this.sessionTokenService.getSessionHeader();

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.get<Produto[]>(this.baseUrl, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.get<Produto[]>(this.baseUrl)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }

    // Método para trazer todas as instâncias de Produto de acordo com a paginação
    findAll(page?: number, pageSize?: number): Observable<Produto[]> {

        const headers = this.sessionTokenService.getSessionHeader();

        if (page !== undefined && pageSize !== undefined) {
            const params = new HttpParams()
                .set('page', page.toString())
                .set('pageSize', pageSize.toString());

            if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
                return this.httpClient.get<Produto[]>(this.baseUrl, { headers: headers, params: params })
                    .pipe(
                        catchError(this.handleError)
                    );
            } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<Produto[]>(this.baseUrl, { params })
                    .pipe(
                        catchError(this.handleError)
                    );
            }
        } else {
            if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
                return this.httpClient.get<Produto[]>(this.baseUrl, { headers })
                    .pipe(
                        catchError(this.handleError)
                    );
            } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<Produto[]>(this.baseUrl)
                    .pipe(
                        catchError(this.handleError)
                    );
            }
        }
    }

    // Método para realizar contagens. Envolvido com a paginação
    count(): Observable<number> {
        return this.httpClient.get<number>(`${this.baseUrl}/count`);
    }


    // Método para trazer uma única instância de Produto do banco de dados do servidor de acordo com seu id
    findById(id: number): Observable<Produto> {
        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.get<Produto>(url, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.get<Produto>(url)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }

    // Método para inserir uma nova instância de Produto no banco de dados do servidor
    insert(produto: Produto): Observable<Produto> {

        const headers = this.sessionTokenService.getSessionHeader();

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            console.log(produto);
            return this.httpClient.post<Produto>(this.baseUrl, produto, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            console.log(produto);
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.post<Produto>(this.baseUrl, produto)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }

    // Método para alterar uma única instância de Produto no banco de dados do servidor
    update(produto: Produto): Observable<Produto> {

        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/${produto.id}`; // Concatena o ID à URL base

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

    // Método para apagar uma única instância de Produto do banco de dados do servidor
    delete(id: number): Observable<any> {

        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base

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

    private handleError(error: HttpErrorResponse) {
        let errorMessage = 'Erro desconhecido';

        // Verifica se há uma resposta de erro do servidor
        if (error.error && error.error.message) {
            errorMessage = `Erro: ${error.error.message}`;
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

        console.error(errorMessage);
        return throwError(() => new Error(errorMessage)); // Retorna a mensagem de erro diretamente
    }

    getClassificacao(): Observable<any[]> {
        const url = 'http://localhost:8080/produtos/classificacao';
        return this.httpClient.get<any[]>(url)
          .pipe(
            catchError(this.handleError)
          );
      }
    

    isPlacaMae(): boolean {
        return this.produto instanceof PlacaMae;
    }

    isProcessador(): boolean {
        return this.produto instanceof Processador;
    }
}