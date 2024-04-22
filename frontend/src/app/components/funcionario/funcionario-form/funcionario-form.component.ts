import { Funcionario } from '../../../models/funcionario.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators } from '@angular/forms';
import { FuncionarioService } from '../../../services/funcionario.service'; 
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavigationService } from '../../../services/navigation.service';

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

  constructor(private formBuilder: FormBuilder, private funcionarioService: FuncionarioService,
    private router: Router, private activatedRoute: ActivatedRoute,
    private navigationService: NavigationService) {
    this.tiposTelefone = [];
    this.uf = [];
    // Inicializar funcionarioForm no construtor
    this.funcionarioForm = formBuilder.group({
        id: [null],
        nome: [''],
        dataNascimento: [''],
        cpf: [''],
        sexo: [''],
        login: [''],
        senha: [''],
        email: [''],
        telefones: this.formBuilder.array([]),
        endereco: this.formBuilder,
        perfil: [''],
    });
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
      id: [null],
      ddd: [''],
      numeroTelefone: [''],
      tipo: [null],
    });
  }

  get enderecos(): FormArray {
    return this.funcionarioForm.get('enderecos') as FormArray;
  }

  adicionarEndereco(): void {
    this.enderecos.push(this.criarEnderecoFormGroup());
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
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateTo('funcionarios');
}

  salvarFuncionario(): void {
    if (this.funcionarioForm.invalid) {
      return;
    }

    const novoFuncionario: Funcionario = {
      id: this.funcionarioForm.value.id,
      nome: this.funcionarioForm.value.nome,
      dataNascimento: this.funcionarioForm.value.dataNascimento,
      cpf: this.funcionarioForm.value.cpf,
      sexo: this.funcionarioForm.value.sexo,
      login: this.funcionarioForm.value.login,
      senha: this.funcionarioForm.value.senha,
      email: this.funcionarioForm.value.email,
      listaTelefone: this.funcionarioForm.value.telefones,
      listaEndereco: this.funcionarioForm.value.enderecos,
      perfil: this.funcionarioForm.value.perfil
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
