import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, Subject, throwError, catchError } from 'rxjs';
import { Funcionario } from '../models/funcionario.model';
import { SessionTokenService } from './session-token.service';
import { Perfil } from '../models/perfil.model';



@Injectable({
  providedIn: 'root'
})

export class FuncionarioService {

  private baseUrl = 'http://localhost:8080/funcionario';

  // Comunicar os componentes do Angular sobre alguma mudança e direcionar para a página de lista de fornecedores
  private funcionarioInseridoSubject = new Subject<void>();
  funcionarioInserido$ = this.funcionarioInseridoSubject.asObservable();


  constructor(private httpClient: HttpClient, private sessionTokenService: SessionTokenService) { }

  // Comunicar os componentes do Angular sobre alguma mudança e direcionar para a página de lista de fornecedores
  notificarFuncionarioInserido(): void {
    this.funcionarioInseridoSubject.next();
  }

  // Método para trazer todas as instâncias de Funcionarios do banco de dados do servidor
  findTodos(): Observable<Funcionario[]> {

    const headers = this.sessionTokenService.getSessionHeader();

    if (headers) {
      // Faz a requisição HTTP com o token de autenticação no cabeçalho
      return this.httpClient.get<Funcionario[]>(this.baseUrl, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.get<Funcionario[]>(this.baseUrl)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  // Método para trazer todas as instâncias de Funcionarios de acordo com a paginação
  findAll(page?: number, pageSize?: number): Observable<Funcionario[]> {

    const headers = this.sessionTokenService.getSessionHeader();

    if (page !== undefined && pageSize !== undefined) {
      const params = new HttpParams()
        .set('page', page.toString())
        .set('pageSize', pageSize.toString());

      if (headers) {
        // Faz a requisição HTTP com o token de autenticação no cabeçalho
        return this.httpClient.get<Funcionario[]>(this.baseUrl, { headers: headers, params: params })
          .pipe(
            catchError(this.handleError)
          );
      } else {
        // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
        return this.httpClient.get<Funcionario[]>(this.baseUrl, { params })
          .pipe(
            catchError(this.handleError)
          );
      }
    } else {
      if (headers) {
        // Faz a requisição HTTP com o token de autenticação no cabeçalho
        return this.httpClient.get<Funcionario[]>(this.baseUrl, { headers })
          .pipe(
            catchError(this.handleError)
          );
      } else {
        // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
        return this.httpClient.get<Funcionario[]>(this.baseUrl)
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

 
  // Método para inserir uma nova instância de Funcionario no banco de dados do servidor
  insert(funcionario: Funcionario): Observable<Funcionario> {

    const headers = this.sessionTokenService.getSessionHeader();
    // Remover o ID do cliente, já que ele será gerado automaticamente pelo servidor
    const { id, ...funcionarioSemId } = funcionario;


      console.log(funcionarioSemId);
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      if(headers){
        return this.httpClient.post<Funcionario>(this.baseUrl, funcionarioSemId, { headers })
        .pipe(
          catchError(this.handleError)
        );
      }else{
        return this.httpClient.put<Funcionario>(this.baseUrl, funcionarioSemId)
        .pipe(
          catchError(this.handleError)
        );
      }
    }
  // Método para alterar uma única instância de Funcionario no banco de dados do servidor
  update(funcionario: Funcionario, id: number): Observable<Funcionario> {

    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base

    if (headers) {
      return this.httpClient.put<Funcionario>(url, funcionario, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      return this.httpClient.put<Funcionario>(url, funcionario)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  updateNS(funcionario: Funcionario, id: number): Observable<Funcionario> {

    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/ns/${id}`; // Concatena o ID à URL base

    const { senha, ...funcionarioSemSenha } = funcionario;

    if (headers) {
      return this.httpClient.put<Funcionario>(url, funcionarioSemSenha, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      return this.httpClient.put<Funcionario>(url, funcionarioSemSenha)
        .pipe(
          catchError(this.handleError)
        );
    }
  }


  // Método para apagar uma única instância de Funcionario do banco de dados do servidor
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

  findById(id: number): Observable<Funcionario> {
    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base

    if (headers) {
      // Faz a requisição HTTP com o token de autenticação no cabeçalho
      return this.httpClient.get<Funcionario>(url, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.get<Funcionario>(url)
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

  getTipoPerfil(): Observable<any[]> {
    const url = 'http://localhost:8080/enum/perfil';
    return this.httpClient.get<any[]>(url)
      .pipe(
        catchError(this.handleError)
      );
  }

    getPerfil(): Observable<Perfil> {
      const headers = this.sessionTokenService.getSessionHeader();
      const url = `${this.baseUrl}/this/perfil`;

      let profile: Observable<Perfil>; // Remova a atribuição inicial aqui

      if (headers) {
        profile = this.httpClient.get<Perfil>(url, { headers });
      } else {
        // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
        profile = this.httpClient.get<Perfil>(url);
      }

      // profile.subscribe(
      //   response => {
      //     console.log(response); // Verifique a resposta recebida
      //   },
      //   error => {
      //     console.error(error); // Em caso de erro, imprima o erro
      //   }
      // );
      return profile;
    }

  getUF(): Observable<any[]> {
    const url = 'http://localhost:8080/enum/uf';
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

