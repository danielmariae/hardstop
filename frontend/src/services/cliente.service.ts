import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
 
@Injectable({
    providedIn: 'root'
})
export class ClienteService{
    private baseUrl = 'http://localhost:8080/clientes'
    
    constructor(private HttpClient: HttpClient){    }

    
}