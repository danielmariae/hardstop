import { Fornecedor } from '../../../models/fornecedor.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators } from '@angular/forms';
import { FornecedorService } from '../../../services/fornecedor.service'; 
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-fornecedor',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './fornecedor-form.component.html',
  styleUrl: './fornecedor-form.component.css'
})
export class FornecedorComponent {
  fornecedorForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private fornecedorService: FornecedorService,
    private router: Router, private activatedRoute: ActivatedRoute) {
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
    // Nada a fazer aqui no momento
    // this.initializeForm();
  }

  initializeForm() {
    const fornecedor: Fornecedor = this.activatedRoute.snapshot.data['fornecedor'];
    this.fornecedorForm = this.formBuilder.group({
        id: [(fornecedor && fornecedor.id) ? fornecedor.id : null],
        nomeFantasia: [(fornecedor && fornecedor.nomeFantasia) ? fornecedor.nomeFantasia : ''],
        cnpj: [(fornecedor && fornecedor.cnpj) ? fornecedor.cnpj : ''],
        endSite: [(fornecedor && fornecedor.endSite) ? fornecedor.endSite : '']
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
      tipo: [''],
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
      uf: [''],
      pais: ['']
    });
  }

  salvarFornecedor(): void {
    if (this.fornecedorForm.invalid) {
      return;
    }

    const novoFornecedor: Fornecedor = {
      id: this.fornecedorForm.value.id, // Pode definir como necessário
      nomeFantasia: this.fornecedorForm.value.nomeFantasia,
      cnpj: this.fornecedorForm.value.cnpj,
      endSite: this.fornecedorForm.value.endSite,
      listaTelefone: this.fornecedorForm.value.telefones,
      listaEndereco: this.fornecedorForm.value.enderecos
    };

    console.log(novoFornecedor);
    // Chame o serviço para inserir o fornecedor
    this.fornecedorService.insert(novoFornecedor).subscribe(() => {
      // Lógica após a inserção bem-sucedida
      this.fornecedorService.notificarFornecedorInserido(); // Notificar outros componentes
    });
  }
}
