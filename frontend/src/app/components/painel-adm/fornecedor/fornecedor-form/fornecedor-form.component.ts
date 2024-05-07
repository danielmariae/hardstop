import { Fornecedor } from '../../../../models/fornecedor.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators } from '@angular/forms';
import { FornecedorService } from '../../../../services/fornecedor.service'; 
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavigationService } from '../../../../services/navigation.service';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { NgxViacepService } from '@brunoc/ngx-viacep';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';

@Component({
  selector: 'app-fornecedor',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgxMaskDirective],
  templateUrl: './fornecedor-form.component.html',
  styleUrl: './fornecedor-form.component.css',
  providers: [provideNgxMask()]
})
export class FornecedorComponent {
  errorMessage: string = '';
  fornecedorForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];

  constructor(private formBuilder: FormBuilder, 
    private fornecedorService: FornecedorService,
    private router: Router, 
    private activatedRoute: ActivatedRoute,
    private navigationService: NavigationService,
    private viaCep: NgxViacepService) {
    this.tiposTelefone = [];
    this.uf = [];
    // Inicializar fornecedorForm no construtor
    this.fornecedorForm = formBuilder.group({
        id: [null],
        nomeFantasia: ['', Validators.required],
        cnpj: [''],
        endSite: ['http://'],
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
    const enderecoFormGroup = this.formBuilder.group({
      id: [null],
      nome: [''],
      cep: [''],
      logradouro: [''],
      numeroLote: [''],
      bairro: [''],
      complemento: [''],
      localidade: [''],
      uf: [''],
      pais: [''],
      cepInvalido: [false],
    });

    enderecoFormGroup.get('cep')?.valueChanges.pipe(
      debounceTime(300), // Aguarda 300ms após a última mudança no campo
      distinctUntilChanged() // Garante que a busca só será feita se o valor do campo for alterado
    ).subscribe((cep: string | null) => {
      if (cep !== null) {
        this.atualizarEndereco(cep, enderecoFormGroup);
      }
    });

    return enderecoFormGroup;
  }

  atualizarEndereco(cep: string, enderecoFormGroup: FormGroup): void {
    const cepValue = cep.replace(/\D/g, ''); // Remove caracteres não numéricos do CEP

    if (cepValue.length === 8) { // Verifica se o CEP possui 8 dígitos
      this.viaCep.buscarPorCep(cepValue).subscribe({
        next: (endereco) => {
          if (endereco && Object.keys(endereco).length > 0) { // Verifica se o objeto de endereço retornado não está vazio
            // Atualizando os valores do formulário com os dados do endereço
            enderecoFormGroup.patchValue({
              cep: this.formatarCep(endereco.cep),
              logradouro: endereco.logradouro,
              bairro: endereco.bairro,
              localidade: endereco.localidade,
              uf: endereco.uf,
              pais: 'Brasil',
              cepInvalido: false // Adiciona uma propriedade para indicar que o CEP é válido
            });
          } else {
            // Limpar campos de endereço se o CEP não for válido
            enderecoFormGroup.patchValue({
              logradouro: null,
              bairro: null,
              localidade: null,
              uf: null,
              cepInvalido: true // Adiciona uma propriedade para indicar que o CEP é inválido
            });
          }
        },
        error: (error) => {
          console.error('Erro ao buscar endereço por CEP:', error);
          // Limpar campos de endereço em caso de erro na busca
          enderecoFormGroup.patchValue({
            logradouro: null,
            bairro: null,
            localidade: null,
            uf: null,
            cepInvalido: true // Adiciona uma propriedade para indicar que o CEP é inválido
          });
        }
      });
    } else {
      // Limpar campos de endereço se o CEP não possuir 8 dígitos
      enderecoFormGroup.patchValue({
        logradouro: null,
        bairro: null,
        localidade: null,
        uf: null,
        cepInvalido: true // Adiciona uma propriedade para indicar que o CEP é inválido
      });
    }
  }

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

  cancelarInsercao(): void {
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateBack();
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

    // Chamando o serviço para inserir o fornecedor
    this.fornecedorService.insert(novoFornecedor).subscribe({
      next: (response) => {
      console.log('Resultado:', response);
      this.fornecedorService.notificarFornecedorInserido(); // Notificar outros componentes
      },
      error: (error) => {
        // Este callback é executado quando ocorre um erro durante a emissão do valor
        console.error('Erro:', error);
        window.alert(error);
      }
  });
  }
}
