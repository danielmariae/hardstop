import { Funcionario } from '../../../models/funcionario.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators, FormControl } from '@angular/forms';
import { FuncionarioService } from '../../../services/funcionario.service'; 
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavigationService } from '../../../services/navigation.service';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { NgxViacepService } from '@brunoc/ngx-viacep';

@Component({
  selector: 'app-funcionario',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './funcionario-form.component.html',
  styleUrl: './funcionario-form.component.css'
})
export class FuncionarioFormComponent {
  errorMessage: string = '';
  funcionarioForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];

  constructor(private formBuilder: FormBuilder, 
    private funcionarioService: FuncionarioService,
    private router: Router, 
    private activatedRoute: ActivatedRoute,
    private navigationService: NavigationService,
    private viaCep: NgxViacepService) {
    this.tiposTelefone = [];
    this.uf = [];
    // Inicializar funcionarioForm no construtor
    this.funcionarioForm = formBuilder.group({
        nome: [''],
        dataNascimento: [''],
        cpf: [''],
        sexo: [''],
        login: [''],
        senha: [''],
        email: [''],
        idperfil: [''],
        telefones: this.formBuilder.array([]),
        endereco: this.formBuilder.group({
          nome: ['', Validators.required],
          cep: ['', Validators.required],
          logradouro: [''],
          numeroLote: [''],
          bairro: [''],
          complemento: [''],
          localidade: [''],
          uf: [''],
          pais: [''],
          cepInvalido: [false],
        }),
    });
    
    // Adicionar um observador para o campo de CEP
    this.endereco.get('cep')?.valueChanges.pipe(
        debounceTime(300), // Aguarda 300ms após a última mudança no campo
        distinctUntilChanged() // Garante que a busca só será feita se o valor do campo for alterado
          ).subscribe((cep: string | null) => {
            if (cep !== null) {
              this.atualizarEndereco(cep, this.endereco);
              }
    })
  }
  
  ngOnInit(): void {
    this.funcionarioService.getTipoTelefone().subscribe(data => {
      this.tiposTelefone = data;
    });

    this.funcionarioService.getUF().subscribe(data => {
      this.uf = data;
    });
  }

  get telefones(): FormArray {
    return this.funcionarioForm.get('telefones') as FormArray;
  }

  adicionarTelefone(): void {
    this.telefones.push(this.criarTelefoneFormGroup());
  }

  removerTelefone(index: number): void {
    this.telefones.removeAt(index);
  }

  criarTelefoneFormGroup(): FormGroup {
    return this.formBuilder.group({
      ddd: [''],
      numeroTelefone: [''],
      tipo: [null],
    });
  }

  get endereco(): FormGroup {
    return this.funcionarioForm.get('endereco') as FormGroup;
  }

  // Método atualizarEndereco atualizado para receber apenas o CEP e o FormGroup:
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

  cancelarInsercao(): void {
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateTo('funcionarios');
}

  salvarFuncionario(): void {
    if (this.funcionarioForm.invalid) {
      return;
    }

    const novoFuncionario: Funcionario = {
      id: 0,
      nome: this.funcionarioForm.value.nome,
      dataNascimento: this.funcionarioForm.value.dataNascimento,
      cpf: this.funcionarioForm.value.cpf,
      sexo: this.funcionarioForm.value.sexo,
      idperfil: this.funcionarioForm.value.idperfil,
      login: this.funcionarioForm.value.login,
      senha: this.funcionarioForm.value.senha,
      email: this.funcionarioForm.value.email,
      listaTelefone: this.funcionarioForm.value.telefones,
      listaEndereco: this.funcionarioForm.value.endereco,
    };

    // Chamando o serviço para inserir o funcionario
    this.funcionarioService.insert(novoFuncionario).subscribe({
      next: (response) => {
      console.log('Resultado:', response);
      this.funcionarioService.notificarFuncionarioInserido(); // Notificar outros componentes
      this.navigationService.navigateTo('funcionarios')
      },
      error: (error) => {
        // Este callback é executado quando ocorre um erro durante a emissão do valor
        console.error('Erro:', error);
        window.alert(error);
      }
  });
  }
}
