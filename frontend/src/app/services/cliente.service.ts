import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, Subject, throwError, catchError } from 'rxjs';
import { Cliente } from '../models/cliente.model';
import { SessionTokenService } from './session-token.service';
import { Perfil } from '../models/perfil.model';
import { SenhaUpdate } from '../models/senhaUpdate.model';



@Injectable({
  providedIn: 'root'
})

export class ClienteService {

  private baseUrl = 'http://localhost:8080/cliente';

  // Comunicar os componentes do Angular sobre alguma mudança e direcionar para a página de lista de fornecedores
  private clienteInseridoSubject = new Subject<void>();
  clienteInserido$ = this.clienteInseridoSubject.asObservable();


  constructor(private httpClient: HttpClient, private sessionTokenService: SessionTokenService) { }

  // Comunicar os componentes do Angular sobre alguma mudança e direcionar para a página de lista de fornecedores
  notificarClienteInserido(): void {
    this.clienteInseridoSubject.next();
  }

  // Método para trazer todas as instâncias de Clientes do banco de dados do servidor
  findTodos(): Observable<Cliente[]> {

    const headers = this.sessionTokenService.getSessionHeader();

    if (headers) {
      // Faz a requisição HTTP com o token de autenticação no cabeçalho
      return this.httpClient.get<Cliente[]>(this.baseUrl, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.get<Cliente[]>(this.baseUrl)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  // Método para trazer todas as instâncias de Clientes de acordo com a paginação
  findAll(page?: number, pageSize?: number): Observable<Cliente[]> {

    const headers = this.sessionTokenService.getSessionHeader();

    if (page !== undefined && pageSize !== undefined) {
      const params = new HttpParams()
        .set('page', page.toString())
        .set('pageSize', pageSize.toString());

      if (headers) {
        // Faz a requisição HTTP com o token de autenticação no cabeçalho
        return this.httpClient.get<Cliente[]>(this.baseUrl, { headers: headers, params: params })
          .pipe(
            catchError(this.handleError)
          );
      } else {
        // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
        return this.httpClient.get<Cliente[]>(this.baseUrl, { params })
          .pipe(
            catchError(this.handleError)
          );
      }
    } else {
      if (headers) {
        // Faz a requisição HTTP com o token de autenticação no cabeçalho
        return this.httpClient.get<Cliente[]>(this.baseUrl, { headers })
          .pipe(
            catchError(this.handleError)
          );
      } else {
        // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
        return this.httpClient.get<Cliente[]>(this.baseUrl)
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

 
  // Método para inserir uma nova instância de Cliente no banco de dados do servidor
  insert(cliente: Cliente): Observable<Cliente> {

    const headers = this.sessionTokenService.getSessionHeader();
  // Remover o ID do cliente, já que ele será gerado automaticamente pelo servidor
  const { id, ...clienteSemId } = cliente;

  // Remover o ID de cada endereço
  const enderecosSemId = clienteSemId.listaEndereco.map(endereco => {
    const { id: enderecoId, ...enderecoSemId } = endereco;
    return enderecoSemId;
  });

  // Remover o ID de cada telefone
  const telefonesSemId = clienteSemId.listaTelefone.map(telefone => {
    const { id: telefoneId, ...telefoneSemId } = telefone;
    return telefoneSemId;
  });

  // Montar o objeto cliente sem IDs nos endereços e telefones
  const clienteSemIds = {
    ...clienteSemId,
    listaEndereco: enderecosSemId,
    listaTelefone: telefonesSemId
  };
      console.log(clienteSemIds);
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.post<Cliente>(this.baseUrl, clienteSemId)
        .pipe(
          catchError(this.handleError)
        );
    }

  // Método para alterar uma única instância de Cliente no banco de dados do servidor
  update(cliente: Cliente, id: number): Observable<Cliente> {

    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base

    if (headers) {
      return this.httpClient.put<Cliente>(url, cliente, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      return this.httpClient.put<Cliente>(url, cliente)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  updateNS(cliente: Cliente, id: number): Observable<Cliente> {

    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/ns/${id}`; // Concatena o ID à URL base

    const { senha, ...clienteSemSenha } = cliente;

    if (headers) {
      return this.httpClient.put<Cliente>(url, clienteSemSenha, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      return this.httpClient.put<Cliente>(url, clienteSemSenha)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  updateThis(cliente: Cliente): Observable<Cliente> {

    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/this`; // Concatena o ID à URL base

    const { senha, ...clienteSemSenha } = cliente;
    const { id, ...clienteSemId } = clienteSemSenha;

    if (headers) {
      return this.httpClient.put<Cliente>(url, clienteSemId, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      return this.httpClient.put<Cliente>(url, clienteSemId)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  
  updateThisSenha(senha: SenhaUpdate): Observable<SenhaUpdate> {

    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/patch/senha`; // Concatena o ID à URL base

    if (headers) {
      console.log("CAIU AQUI NO HEADERS.")
      return this.httpClient.patch<SenhaUpdate>(url, senha, { headers })
      .pipe(
        catchError(this.handleError)
      );
    } else {
      console.log("CAIU NO ELSE.")
      return this.httpClient.patch<SenhaUpdate>(url, senha)
        .pipe(
          catchError(this.handleError)
        );
    }
  }



  // Método para apagar uma única instância de Cliente do banco de dados do servidor
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

  findById(id: number): Observable<Cliente> {
    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/${id}`; // Concatena o ID à URL base

    if (headers) {
      // Faz a requisição HTTP com o token de autenticação no cabeçalho
      return this.httpClient.get<Cliente>(url, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.get<Cliente>(url)
        .pipe(
          catchError(this.handleError)
        );
    }
  }

  findThis(): Observable<Cliente> {
    const headers = this.sessionTokenService.getSessionHeader();
    const url = `${this.baseUrl}/this`; // Concatena o ID à URL base

    if (headers) {
      // Faz a requisição HTTP com o token de autenticação no cabeçalho
      return this.httpClient.get<Cliente>(url, { headers })
        .pipe(
          catchError(this.handleError)
        );
    } else {
      // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
      return this.httpClient.get<Cliente>(url)
        .pipe(
          catchError(this.handleError)
        );
    }
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

