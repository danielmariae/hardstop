import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HerePoiService {
  private apiKey = 'LuvImLI1DFm0bsMS9fygMkNz18F9xbW7Te7yH2OpKIk ';
  private apiUrl = 'https://discover.search.hereapi.com/v1/discover';

  constructor(private http: HttpClient) {}

  // Método para buscar POIs (Pontos de Interesse)
  searchPoiByCategory(latitude: number, longitude: number, category: string): Observable<any> {
    const url = `${this.apiUrl}?at=${latitude},${longitude}&q=${category}&apiKey=${this.apiKey}`;
    return this.http.get(url);
  }

  // Método para buscar POIs por nome
  searchPoiByName(latitude: number, longitude: number, name: string): Observable<any> {
    const url = `${this.apiUrl}?at=${latitude},${longitude}&q=${name}&apiKey=${this.apiKey}`;
    return this.http.get(url);
  }
}
