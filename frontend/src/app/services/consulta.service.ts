import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Consulta } from '../models/consulta.model';
import { Observable } from 'rxjs';
import { SessionTokenService } from './session-token.service';

@Injectable({
  providedIn: 'root'
})
export class ConsultaService {

  private baseUrl = 'http://localhost:8080/produtos';

  constructor(private http: HttpClient, private sessionTokenService: SessionTokenService) { }

  findAll(page?: number, pageSize?: number): Observable<Consulta[]> {

    const headers = this.sessionTokenService.getSessionHeader();

    if (page !== undefined && pageSize !== undefined) {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('pageSize', pageSize.toString());

       // if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
          //  return this.http.get<Consulta[]>(this.baseUrl, { headers: headers, params: params });
       // } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.http.get<Consulta[]>(this.baseUrl, { params });
       // }
    } else {
        //if (headers) {
            // Faz a requisição HTTP com o token de autenticação no cabeçalho
           // return this.http.get<Consulta[]>(this.baseUrl, { headers });
       // } else {
            // Se o token de sessão não estiver disponível, faz a requisição sem o token de autenticação
            return this.http.get<Consulta[]>(this.baseUrl);
       // }
    }
}


  findById(id: string): Observable<Consulta> {
    return this.http.get<Consulta>(`${this.baseUrl}/${id}`);
  }

  findByNome(nome: string, pagina: number, tamanhoPagina: number): Observable<Consulta[]> {
    const params = {
      page: pagina.toString(),
      pageSize: tamanhoPagina.toString()
    }
    return this.http.get<Consulta[]>(`${this.baseUrl}/search/${nome}`, { params });
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/count`);
  }

  countByNome(nome: string): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/search/${nome}/count`);
  }

  getUrlImagem(nomeImagem: string): string {
    return `${this.baseUrl}/download/imagem/${nomeImagem}`;
  }

  uploadImagem(id: number, nomeImagem: string, imagem: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('id', id.toString());
    formData.append('nomeImagem', imagem.name);
    formData.append('imagem', imagem, imagem.name);
    
    return this.http.patch<Consulta>(`${this.baseUrl}/image/upload`, formData);
  }

  save(consulta: Consulta): Observable<Consulta> {
    const obj = {
      nome: consulta.nome,
      preco: consulta.valorVenda,
      quantidade: consulta.quantidadeUnidades
    }
    return this.http.post<Consulta>(`${this.baseUrl}`, obj);
  }

  update(consulta: Consulta): Observable<Consulta> {
    const obj = {
      nome: consulta.nome,
      preco: consulta.valorVenda,
      quantidade: consulta.quantidadeUnidades
    }
    return this.http.put<Consulta>(`${this.baseUrl}/${consulta.id}`, obj);
  }

  delete(consulta: Consulta): Observable<any> {
    return this.http.delete<Consulta>(`${this.baseUrl}/${consulta.id}`);
  }

}
