import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Cliente } from '../../../models/cliente.model';
import { ClienteService } from '../../../services/cliente.service';
import { NavigationService } from '../../../services/navigation.service';
import { validarSenhaUpdate } from '../../../validators/update-senha-validator';


@Component({
  selector: 'app-cliente-edit',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './cliente-edit.component.html',
  styleUrls: ['./cliente-edit.component.css']
})

export class ClienteEditComponent implements OnInit {
  cliente: Cliente;
  clienteForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];
  enderecoAnterior: string = '';
  id: number = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clienteService: ClienteService,
    private formBuilder: FormBuilder,
    private navigationService: NavigationService
  ) {
    this.tiposTelefone = [];
    this.uf = [];
    this.cliente = new Cliente(); // Inicialização no construtor
    this.clienteForm = formBuilder.group({
      id: [null],
      nome: [''],
      dataNascimento: [''],
      cpf: [''],
      sexo: [''],
      login: [''],
      senha: this.formBuilder.control('',validarSenhaUpdate()),
      email: [''],
      telefones: this.formBuilder.array([]),
      enderecos: this.formBuilder.array([]),
  });
}
ngOnInit(): void {

    this.id = Number(this.route.snapshot.paramMap.get('id'));
    //this.enderecoAnterior = this.navigationService

    this.clienteService.getTipoTelefone().subscribe(data => {
      this.tiposTelefone = data;
    });

    this.clienteService.getUF().subscribe(data => {
      this.uf = data;
    });

    //const idParam = this.route.snapshot.paramMap.get('id');
    console.log(this.id);
    if (this.id !== null) {
        this.clienteService.findById(this.id).subscribe(cliente => {
          this.cliente = cliente;

          // Inserindo valores nos campos de nome, cnpj e endSite
          this.clienteForm.patchValue({
            nome: cliente.nome,
            dataNascimento: this.formatarData(cliente.dataNascimento),
            cpf: cliente.cpf,
            sexo: cliente.sexo,
            login: cliente.login,
            senha: cliente.senha,
            email: cliente.email,
            });

          // Populando o FormArray com os dados existentes de cliente.listaTelefone
          cliente.listaTelefone.forEach(telefone => {
            this.adicionarTelefone(telefone);
          });

          // Populando o FormArray com os dados existentes de cliente.listaEndereco
          cliente.listaEndereco.forEach(endereco => {
            this.adicionarEndereco(endereco);
          });

          console.log(this.cliente);
        });
      } else {
        console.error('O parâmetro "id" não foi fornecido ou é inválido.');
      }
  }

get telefones(): FormArray {
  return this.clienteForm.get('telefones') as FormArray;
}

adicionarTelefone(telefone?: any): void {
  const telefoneFormGroup = this.formBuilder.group({
    ddd: [telefone ? telefone.ddd : ''],
    numeroTelefone: [telefone ? telefone.numeroTelefone : ''],
    tipo: [telefone ? telefone.tipo : '']
  });

  this.telefones.push(telefoneFormGroup);
}

removerTelefone(index: number): void {
  this.telefones.removeAt(index);
}

formatarData(data: string): string {
  const partesData = data.split('-');
  return `${partesData[2]}/${partesData[1]}/${partesData[0]}`;
}


get enderecos(): FormArray {
  return this.clienteForm.get('enderecos') as FormArray;
}

adicionarEndereco(endereco?: any): void {
  const enderecoFormGroup = this.formBuilder.group({
    nome: [endereco && endereco.nome ? endereco.nome : ''],
    logradouro: [endereco && endereco.logradouro ? endereco.logradouro : ''],
    numeroLote: [endereco && endereco.numeroLote ? endereco.numeroLote : ''],
    bairro: [endereco && endereco.bairro ? endereco.bairro : ''],
    complemento: [endereco && endereco.complemento ? endereco.complemento : ''],
    cep: this.formBuilder.group({
      prefixo: [endereco && endereco.cep && endereco.cep.prefixo ? endereco.cep.prefixo : ''],
      sufixo: [endereco && endereco.cep && endereco.cep.sufixo ? endereco.cep.sufixo : ''],
      cep: [endereco && endereco.cep && endereco.cep.cep ? endereco.cep.cep : '']
    }),
    localidade: [endereco && endereco.localidade ? endereco.localidade : ''],
    uf: [endereco && endereco.uf ? endereco.uf : ''],
    pais: [endereco && endereco.pais ? endereco.pais : ''],
  });

  this.enderecos.push(enderecoFormGroup);
}

removerEndereco(index: number): void {
  this.enderecos.removeAt(index);
}

  cancelarEdicao(): void {
    // Redireciona o usuário para outra rota anterior
    this.navigationService.navigateTo('clientes');
  }


  salvarAlteracoes(): void {
    
    // const idParam = Number(this.route.snapshot.paramMap.get('id'));
    console.log(this.id);
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
    
    // Lógica para salvar as alterações do cliente
    this.clienteService.update(novoCliente, this.cliente.id).subscribe({
      next: (response) => {
        console.log(novoCliente);
        if(this.navigationService.getPreviousEndPoint() === 'clientees') {
          this.clienteService.notificarClienteInserido(); // Notificar outros componentes
        } else {
          this.navigationService.navigateTo('clientes');
        }
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
