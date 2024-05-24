import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CepService {
  private baseUrl = 'https://viacep.com.br/ws/'
  constructor(
    private httpClient: HttpClient
  ) { }
}
