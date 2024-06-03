import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, Subject, throwError, catchError, switchMap, map } from 'rxjs';
import { PlacaMae, Processador, Produto, ProdutoResponse } from '../models/produto.model';
import { SessionTokenService } from './session-token.service';
import { Fornecedor } from '../models/fornecedor.model';

@Injectable({
    providedIn: 'root'
})

export class ProdutoService {
    private produtoFormValue: any = {};
    produto: Produto = new Produto();
    private baseUrl = 'http://localhost:8080/produtos';

    // Comunica os componentes do Angular sobre alguma mudança e direciona para a página de lista de produtos
    private produtoInseridoSubject = new Subject<void>();
    produtoInserido$ = this.produtoInseridoSubject.asObservable();


    constructor(private httpClient: HttpClient, private sessionTokenService: SessionTokenService) { }

     // Atualiza os valores do formulário do componente de produto
    updateProdutoFormValue(formValue: any) {
    this.produtoFormValue = formValue;
  }

    // Obtém os valores do formulário do componente de produto
    getProdutoFormValue() {
        return this.produtoFormValue;
    }

    // Comunica os componentes do Angular sobre alguma mudança e direcionar para a página de lista de produtos
    notificarProdutoInserido(): void {
        this.produtoInseridoSubject.next();
    }

    // Método para buscar as imagens da API e convertê-las para Base64
    async getImagesAsBase64(nomesImagens: string[]): Promise<string[]> {
        try {
        const promises = nomesImagens.map(nomeImagem => this.getImageAsBase64(nomeImagem));
        return await Promise.all(promises);
        } catch (error) {
        console.error('Erro ao buscar as imagens:', error);
        throw error;
        }
    }

    // Método para buscar uma imagem da API e convertê-la para Base64
    async getImageAsBase64(nomeImagem: string): Promise<string> {
        try {
        const url = `http://localhost:8080/produtos/download/imagem/${nomeImagem}`;
        const blob = await this.httpClient.get(url, { responseType: 'blob' }).toPromise();
        if (!blob) {
            throw new Error('A resposta da solicitação não contém dados de imagem');
        }
        return this.blobToBase64(blob);
        } catch (error) {
        console.error(`Erro ao buscar a imagem ${nomeImagem}:`, error);
        throw error;
        }
    }

    // Método para converter um Blob em uma string Base64
    private blobToBase64(blob: Blob): Promise<string> {
        return new Promise<string>((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => {
            const base64String = reader.result as string;
            resolve(base64String);
        };
        reader.onerror = () => {
            reject('Erro ao ler a imagem como Base64');
        };
        reader.readAsDataURL(blob);
        });
    }

    getUrlImagem(nomeImagem: string): string {
        return `${this.baseUrl}/download/imagem/${nomeImagem}`;
    }
    
    uploadImagem(id: number, nomeImagem: string, imagem: File): Observable<any> {
        const formData: FormData = new FormData();
        formData.append('id', id.toString());
        formData.append('nomeImagem', imagem.name);
        formData.append('imagem', imagem, imagem.name);
        
        return this.httpClient.patch<Produto>(`${this.baseUrl}/image/upload`, formData);
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

           // if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
              //  return this.httpClient.get<Produto[]>(this.baseUrl, { headers: headers, params: params })
               //     .pipe(
                //        catchError(this.handleError)
                //    );
          //  } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<Produto[]>(this.baseUrl, { params })
                    .pipe(
                        catchError(this.handleError)
                    );
         //   }
        } else {
           // if (headers) {
                // Faz a requisição HTTP com o token de autenticação no cabeçalho
              //  return this.httpClient.get<Produto[]>(this.baseUrl, { headers })
               //     .pipe(
                  //      catchError(this.handleError)
                //    );
          //  } else {
                // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
                return this.httpClient.get<Produto[]>(this.baseUrl)
                    .pipe(
                        catchError(this.handleError)
                    );
           // }
        }
    }

    findByNome(nome: string, page?: number, pageSize?: number): Observable<ProdutoResponse>{
        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/search/nome/${nome}`;

        if (page !== undefined && pageSize !== undefined) {
            const params = new HttpParams()
                .set('page', page.toString())
                .set('pageSize', pageSize.toString());
            
            return this.httpClient.get<ProdutoResponse>(url, { params })
                .pipe(
                    catchError(this.handleError)
                );
        }else{
            return this.httpClient.get<ProdutoResponse>(url)
            .pipe(
                catchError(this.handleError)
            );
        }
    }

    // Método para realizar contagens. Envolvido com a paginação
    count(): Observable<number> {
        return this.httpClient.get<number>(`${this.baseUrl}/count`);
    }


    // Método para trazer uma única instância de Produto do banco de dados do servidor de acordo com seu id
    findById(id: number): Observable<any> {
        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/search/id/${id}`; // Concatena o ID à URL base

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.get<any>(url, { headers })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.get<any>(url)
                .pipe(
                    catchError(this.handleError)
                );
        }
    }


   
    findProdutoEstragado(idProduto: number,  dataHoraVenda: string): Observable<Fornecedor> {
        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/search/fornecedor`; 

        const params = {
            idProduto: +idProduto,
            dataHoraVenda
        }

    //     let params = new HttpParams()
    // .set('idProduto', idProduto.toString())
    // .set('dataHoraVenda', dataHoraVenda);

    console.log(params);
        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            return this.httpClient.get<Fornecedor>(url, { headers: headers, params: params })
                .pipe(
                    catchError(this.handleError)
                );
        } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.get<Fornecedor>(url, {params})
                .pipe(
                    catchError(this.handleError)
                );
        }
    }

    // Método para inserir uma nova instância de Produto no banco de dados do servidor
    insert(produto: Produto, tipoProduto: string): Observable<any> {

       let url: string;

        if(tipoProduto == 'processadores') {
            url = this.baseUrl + '/insert/processador'; 
        } else if(tipoProduto == 'placas mãe') {
            url = this.baseUrl + '/insert/placaMae'; 
        } else {
            url = this.baseUrl + '/insert'; // É apenas Produto do tipo puro 
        }
        const headers = this.sessionTokenService.getSessionHeader();

        if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
            console.log(produto);
            console.log(url);
            return this.httpClient.post<any>(url, produto, { headers })
                .pipe(
                    catchError(this.handleError)
                );
            console.log(tipoProduto);
        } else {
            console.log(produto);
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.httpClient.post<any>(url, produto)
                .pipe(
                    catchError(this.handleError)
                );
            console.log(tipoProduto);
        }
    }

    // Método para alterar uma única instância de Produto no banco de dados do servidor
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

    // Método para apagar uma única instância de Produto do banco de dados do servidor
    delete(id: number): Observable<any> {

        const headers = this.sessionTokenService.getSessionHeader();
        const url = `${this.baseUrl}/delete/${id}`; // Concatena o ID à URL base

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