import { Cliente } from '../../../models/cliente.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators } from '@angular/forms';
import { ClienteService } from '../../../services/cliente.service'; 
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavigationService } from '../../../services/navigation.service';

@Component({
  selector: 'app-cliente',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './cliente-form.component.html',
  styleUrl: './cliente-form.component.css'
})
export class ClienteFormComponent {
  errorMessage: string = '';
  clienteForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];

  constructor(private formBuilder: FormBuilder, private clienteService: ClienteService,
    private router: Router, private activatedRoute: ActivatedRoute,
    private navigationService: NavigationService) {
    this.tiposTelefone = [];
    this.uf = [];
    // Inicializar clienteForm no construtor
    this.clienteForm = formBuilder.group({
        id: [null],
        nome: [''],
        dataNascimento: [''],
        cpf: [''],
        sexo: [''],
        login: [''],
        senha: [''],
        email: [''],
        telefones: this.formBuilder.array([]),
        enderecos: this.formBuilder.array([]),
    });
  }
  
  ngOnInit(): void {
    this.clienteService.getTipoTelefone().subscribe(data => {
      this.tiposTelefone = data;
    });

    this.clienteService.getUF().subscribe(data => {
      this.uf = data;
    });
  }

  get telefones(): FormArray {
    return this.clienteForm.get('telefones') as FormArray;
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
    return this.clienteForm.get('enderecos') as FormArray;
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
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateTo('clientes');
}

  salvarCliente(): void {
    if (this.clienteForm.invalid) {
      return;
    }

    const novoCliente: Cliente = {
      id: this.clienteForm.value.id,
      nome: this.clienteForm.value.nome,
      dataNascimento: this.clienteForm.value.dataNascimento,
      cpf: this.clienteForm.value.cpf,
      sexo: this.clienteForm.value.sexo,
      login: this.clienteForm.value.login,
      senha: this.clienteForm.value.senha,
      email: this.clienteForm.value.email,
      listaTelefone: this.clienteForm.value.telefones,
      listaEndereco: this.clienteForm.value.enderecos
    };

    // Chamando o serviço para inserir o cliente
    this.clienteService.insert(novoCliente).subscribe({
      next: (response) => {
      console.log('Resultado:', response);
      this.clienteService.notificarClienteInserido(); // Notificar outros componentes
      this.navigationService.navigateTo('clientes')
      },
      error: (error) => {
        // Este callback é executado quando ocorre um erro durante a emissão do valor
        console.error('Erro:', error);
        window.alert(error);
      }
  });
  }
}
