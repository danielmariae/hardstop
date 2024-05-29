import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FornecedorService } from '../../../../services/fornecedor.service';
import { Fornecedor } from '../../../../models/fornecedor.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { NavigationService } from '../../../../services/navigation.service';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { NgxViacepService } from '@brunoc/ngx-viacep';


@Component({
  selector: 'app-fornecedor-view',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './fornecedor-view.component.html',
  styleUrls: ['./fornecedor-view.component.css']
})

export class FornecedorViewComponent implements OnInit {
    errorMessage: string | null = null;
    errorDetails: any | null = null;
  fornecedor: Fornecedor;
  fornecedorForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];
  enderecoAnterior: string = '';
  id: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fornecedorService: FornecedorService,
    private formBuilder: FormBuilder,
    private navigationService: NavigationService,
    private viaCep: NgxViacepService
  ) {
    this.tiposTelefone = [];
    this.uf = [];
    this.fornecedor = new Fornecedor(); // Inicialização no construtor
    this.fornecedorForm = this.formBuilder.group({
      nomeFantasia: [''],
      cnpj: [''],
      endSite: [''],
      telefones: this.formBuilder.array([]),
      enderecos: this.formBuilder.array([]),
    });
  }

  ngOnInit(): void {

    this.id = Number(this.route.snapshot.paramMap.get('id'));
    //this.enderecoAnterior = this.navigationService

    this.fornecedorService.getTipoTelefone().subscribe(data => {
      this.tiposTelefone = data;
    });

    this.fornecedorService.getUF().subscribe(data => {
      this.uf = data;
    });

    //const idParam = this.route.snapshot.paramMap.get('id');
    console.log(this.id);
    if (this.id !== null) {
        this.fornecedorService.findById(this.id).subscribe(fornecedor => {
          this.fornecedor = fornecedor;

          // Inserindo valores nos campos de nome, cnpj e endSite
          this.fornecedorForm.patchValue({
            nomeFantasia: fornecedor.nomeFantasia,
            cnpj: fornecedor.cnpj,
            endSite: fornecedor.endSite
          });

          // Populando o FormArray com os dados existentes de fornecedor.listaTelefone
          fornecedor.listaTelefone.forEach(telefone => {
            this.adicionarTelefone(telefone);
          });

          // Populando o FormArray com os dados existentes de fornecedor.listaEndereco
          fornecedor.listaEndereco.forEach(endereco => {
            this.adicionarEndereco(endereco);
          });

          console.log(this.fornecedor);
        });
      } else {
        console.error('O parâmetro "id" não foi fornecido ou é inválido.');
      }
  }

get telefones(): FormArray {
  return this.fornecedorForm.get('telefones') as FormArray;
}

adicionarTelefone(telefone?: any): void {
  const telefoneFormGroup = this.formBuilder.group({
    ddd: [telefone ? telefone.ddd : ''],
    numeroTelefone: [telefone ? telefone.numeroTelefone : ''],
    tipo: [telefone ? telefone.tipo : '']
  });

  this.telefones.push(telefoneFormGroup);
}

get enderecos(): FormArray {
  return this.fornecedorForm.get('enderecos') as FormArray;
}

adicionarEndereco(endereco?: any): void {
  const enderecoFormGroup = this.formBuilder.group({
    nome: [endereco && endereco.nome ? endereco.nome : '', Validators.required],
    logradouro: [endereco && endereco.logradouro ? endereco.logradouro : ''],
    numeroLote: [endereco && endereco.numeroLote ? endereco.numeroLote : ''],
    bairro: [endereco && endereco.bairro ? endereco.bairro : ''],
    complemento: [endereco && endereco.complemento ? endereco.complemento : ''],
    cep: [endereco && endereco.cep ? this.formatarCep(endereco.cep) : ''],
    localidade: [endereco && endereco.localidade ? endereco.localidade : ''],
    uf: [endereco && endereco.uf ? endereco.uf : ''],
    pais: [endereco && endereco.pais ? endereco.pais : ''],
    cepInvalido: [false],
  });

  this.enderecos.push(enderecoFormGroup);
}
 // Método para converter o CEP para o formato desejado
  formatarCep(cep: string): string {
    // Remove caracteres não numéricos do CEP
    const cepDigits = cep.replace(/\D/g, '');

    // Verifica se o CEP possui 8 dígitos
    if (cepDigits.length === 8) {
      // Formata o CEP no formato desejado
      return cepDigits.replace(/(\d{5})(\d{3})/, '$1-$2');
    } else {
      return cep; // Retorna o CEP original se não possuir 8 dígitos
    }
  }


    // Método para apagar um fornecedor escolhido
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

    // Método para chamar o endpoint para edição de um Fornecedor escolhido
    editarFornecedor(id: number): void {
      const enderecoEdicao: string = "adm/fornecedores/edit/" + id.toString();
      this.navigationService.navigateTo(enderecoEdicao);
  }

  verFornecedores(): void{
    const enderecoList: string = "adm/fornecedores";
    this.navigationService.navigateTo(enderecoList);
  }
}
