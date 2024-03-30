import { MatOption } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Fornecedor } from "../../../models/fornecedor.model";
import { FornecedorService } from "../../../services/fornecedor.service";
import { NgFor, CommonModule, AsyncPipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatPaginatorModule } from '@angular/material/paginator';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, of } from 'rxjs';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { NavigationService } from '../../../services/navigation.service';


@Component({
  selector: 'app-fornecedor-escolhido',
  standalone: true,
  imports: [NgFor, MatOption, AsyncPipe, MatTableModule, MatToolbarModule, MatIconModule, MatButtonModule, RouterModule, CommonModule, MatPaginatorModule, MatAutocompleteModule, FormsModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule],
  templateUrl: './fornecedor-escolhido.component.html',
  styleUrl: './fornecedor-escolhido.component.css'
})
export class FornecedorEscolhidoComponent implements OnInit {
    
    filteredOptions: Observable<Fornecedor[]> = new Observable<Fornecedor[]>(); // Inicializando a propriedade filteredOptions
    displayedColumns: string[] = ['id', 'nomeFantasia', 'cnpj', 'endSite', 'endereco', 'telefone', 'acao'];
    fornecedor: Fornecedor = new Fornecedor();
    tiposTelefoneMap: Map<number, string> = new Map<number, string>();


  constructor(private route: ActivatedRoute,
    private fornecedorService: FornecedorService,
    private location: Location,
    private router: Router,
    private navigationService: NavigationService) { }

  ngOnInit(): void {
    this.carregarTiposTelefone();
    this.route.params.subscribe(params => {
      const fornecedorId = params['id'];
      console.log(fornecedorId);
      this.fornecedorService.findById(fornecedorId).subscribe({
        next: (fornecedor: Fornecedor) => {
          this.fornecedor = fornecedor;
          console.log(fornecedor);
        },
        error:(error) => {
          console.error('Erro ao carregar detalhes do fornecedor:', error);
          // Tratar o erro conforme necessário
        }
        });
    });
  }

  carregarTiposTelefone() {
    this.fornecedorService.getTipoTelefone().subscribe({
      next: (tiposTelefone) => {
        // Mapear os tipos de telefone para o mapa
        tiposTelefone.forEach(tipo => {
          this.tiposTelefoneMap.set(tipo.id, tipo.descricao);
        });
      },
      error: (error) => {
        console.error('Erro ao carregar tipos de telefone:', error);
      }
    });
}

apagarFornecedor(id: number): void {
    this.fornecedorService.delete(id).subscribe({
      next:  (response) => {
            this.fornecedorService.notificarFornecedorInserido();
        },
        error: (error) => {
        console.error(error);
        window.alert(error); // Exibe a mensagem de erro usando window.alert()
        }
    });
}

editarFornecedor(id: number): void {
    const enderecoEdicao: string = "fornecedores/edit/" + id.toString();
    this.navigationService.navigateTo(enderecoEdicao);
}

}
