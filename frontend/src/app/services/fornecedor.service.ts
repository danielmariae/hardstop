import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, Subject, throwError, catchError } from 'rxjs';
import { Fornecedor } from '../models/fornecedor.model';
import { SessionTokenService } from './session-token.service';



@Injectable({
  providedIn: 'root'
})

export class FornecedorService {

  private baseUrl = 'http://localhost:8080/fornecedores';

  // Comunicar os componentes do Angular sobre alguma mudança e direcionar para a página de lista de fornecedores
  private fornecedorInseridoSubject = new Subject<void>();
  fornecedorInserido$ = this.fornecedorInseridoSubject.asObservable();


  constructor(private httpClient: HttpClient, private sessionTokenService: SessionTokenService) { }

  // Comunicar os componentes do Angular sobre alguma mudança e direcionar para a página de lista de fornecedores
  notificarFornecedorInserido(): void {
    this.fornecedorInseridoSubject.next();
  }

  // Método para trazer todas as instâncias de Fornecedores do banco de dados do servidor
  findTodos(): Observable<Fornecedor[]> {

    const headers = this.sessionTokenService.getSessionHeader();

    if (headers) {
      // Faz a requisição HTTP com o token de autenticação no cabeçalho
      return this.httpClient.get<Fornecedor[]>(this.baseUrl, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.get<Fornecedor[]>(this.baseUrl)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  // Método para trazer todas as instâncias de Fornecedores de acordo com a paginação
  findAll(page?: number, pageSize?: number): Observable<Fornecedor[]> {

    const headers = this.sessionTokenService.getSessionHeader();

    if (page !== undefined && pageSize !== undefined) {
      const params = new HttpParams()
        .set('page', page.toString())
        .set('pageSize', pageSize.toString());

      if (headers) {
        // Faz a requisição HTTP com o token de autenticação no cabeçalho
        return this.httpClient.get<Fornecedor[]>(this.baseUrl, { headers: headers, params: params })
          .pipe(
            catchError(this.handleError)
          );
      } else {
        // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
        return this.httpClient.get<Fornecedor[]>(this.baseUrl, { params })
          .pipe(
            catchError(this.handleError)
          );
      }
    } else {
      if (headers) {
        // Faz a requisição HTTP com o token de autenticação no cabeçalho
        return this.httpClient.get<Fornecedor[]>(this.baseUrl, { headers })
          .pipe(
            catchError(this.handleError)
          );
      } else {
        // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
        return this.httpClient.get<Fornecedor[]>(this.baseUrl)
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


  // Método para trazer uma única instância de Fornecedor do banco de dados do servidor de acordo com seu id
  findById(id: number): Observable<Fornecedor> {
    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base

    if (headers) {
      // Faz a requisição HTTP com o token de autenticação no cabeçalho
      return this.httpClient.get<Fornecedor>(url, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.get<Fornecedor>(url)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  // Método para inserir uma nova instância de Fornecedor no banco de dados do servidor
  insert(fornecedor: Fornecedor): Observable<Fornecedor> {

    const headers = this.sessionTokenService.getSessionHeader();

    if (headers) {
      // Faz a requisição HTTP com o token de autenticação no cabeçalho
      console.log(fornecedor);
      return this.httpClient.post<Fornecedor>(this.baseUrl, fornecedor, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      console.log(fornecedor);
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.post<Fornecedor>(this.baseUrl, fornecedor)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  // Método para alterar uma única instância de Fornecedor no banco de dados do servidor
  update(fornecedor: Fornecedor): Observable<Fornecedor> {

    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/${fornecedor.id}`; // Concatena o ID à URL base

    if (headers) {
      return this.httpClient.put<Fornecedor>(url, fornecedor, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      return this.httpClient.put<Fornecedor>(url, fornecedor)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  // Método para apagar uma única instância de Fornecedor do banco de dados do servidor
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

  getTipoTelefone(): Observable<any[]> {
    const url = 'http://localhost:8080/enum/tipoTelefone';
    return this.httpClient.get<any[]>(url)
      .pipe(
        catchError(this.handleError)
      );
  }

  getUF(): Observable<any[]> {
    const url = 'http://localhost:8080/enum/uf';
    return this.httpClient.get<any[]>(url)
      .pipe(
        catchError(this.handleError)
      );
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
}
