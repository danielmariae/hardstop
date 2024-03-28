import { Fornecedor } from '../../../models/fornecedor.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators } from '@angular/forms';
import { FornecedorService } from '../../../services/fornecedor.service'; 
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-fornecedor',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './fornecedor-form.component.html',
  styleUrl: './fornecedor-form.component.css'
})
export class FornecedorComponent {
  errorMessage: string = '';
  fornecedorForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];

  constructor(private formBuilder: FormBuilder, private fornecedorService: FornecedorService,
    private router: Router, private activatedRoute: ActivatedRoute) {
    this.tiposTelefone = [];
    this.uf = [];
    // Inicializar fornecedorForm no construtor
    this.fornecedorForm = formBuilder.group({
        id: [null],
        nomeFantasia: [''],
        cnpj: [''],
        endSite: [''],
        telefones: this.formBuilder.array([]),
        enderecos: this.formBuilder.array([]),
    });
  }
  
  ngOnInit(): void {
    this.fornecedorService.getTipoTelefone().subscribe(data => {
      this.tiposTelefone = data;
    });

    this.fornecedorService.getUF().subscribe(data => {
      this.uf = data;
    });
  }

  get telefones(): FormArray {
    return this.fornecedorForm.get('telefones') as FormArray;
  }

  adicionarTelefone(): void {
    this.telefones.push(this.criarTelefoneFormGroup());
  }

  removerTelefone(index: number): void {
    this.telefones.removeAt(index);
  }

  criarTelefoneFormGroup(): FormGroup {
    return this.formBuilder.group({
      id:[null],
      ddd: [''],
      numeroTelefone: [''],
      tipo: [null],
    });
  }

  get enderecos(): FormArray {
    return this.fornecedorForm.get('enderecos') as FormArray;
  }

  adicionarEndereco(): void {
    this.enderecos.push(this.criarEnderecoFormGroup());
  }

  removerEndereco(index: number): void {
    this.enderecos.removeAt(index);
  }

  criarEnderecoFormGroup(): FormGroup {
    return this.formBuilder.group({
      id: [null],
      nome: [''],
      logradouro: [''],
      numeroLote: [''],
      bairro: [''],
      complemento: [''],
      cep: this.formBuilder.group({
        prefixo: [''],
        sufixo: [null],
        cep: ['']
      }),
      localidade: [''],
      uf: [null],
      pais: ['']
    });
  }

  cancelarInsercao(): void {
    // Redireciona o usuário para outra rota
    this.router.navigate(['fornecedores']);
}

  salvarFornecedor(): void {
    if (this.fornecedorForm.invalid) {
      return;
    }

    const novoFornecedor: Fornecedor = {
      id: this.fornecedorForm.value.id,
      nomeFantasia: this.fornecedorForm.value.nomeFantasia,
      cnpj: this.fornecedorForm.value.cnpj,
      endSite: this.fornecedorForm.value.endSite,
      listaTelefone: this.fornecedorForm.value.telefones,
      listaEndereco: this.fornecedorForm.value.enderecos
    };

    // Chame o serviço para inserir o fornecedor
    this.fornecedorService.insert(novoFornecedor).subscribe({
      next: (response) => {
      console.log('Resultado:', response);
      // Lógica após a inserção bem-sucedida
      this.fornecedorService.notificarFornecedorInserido(); // Notificar outros componentes
      },
      error: (error) => {
        // Este callback é executado quando ocorre um erro durante a emissão do valor
        console.error('Erro:', error);
        // Aqui você pode lidar com o erro de acordo com sua lógica de negócio
        // Por exemplo, exibir uma mensagem de erro para o usuário
        window.alert(error);
      }
  });
  }
}
