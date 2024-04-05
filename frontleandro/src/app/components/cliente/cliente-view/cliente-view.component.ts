import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClienteService } from '../../../services/cliente.service';
import { Cliente } from '../../../models/cliente.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { NavigationService } from '../../../services/navigation.service';


@Component({
  selector: 'app-cliente-edit',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './cliente-view.component.html',
  styleUrls: ['./cliente-view.component.css']
})

export class ClienteViewComponent implements OnInit {
  cliente: Cliente;
  clienteForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];
  enderecoAnterior: string = '';
  id: number = 1;
clientes: any;

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
      senha: [''],
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
            dataNascimento: cliente.dataNascimento,
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

  get telefones(): FormArray | null {
    return this.clienteForm ? this.clienteForm.get('telefones') as FormArray : null;
  }
  

  adicionarTelefone(telefone?: any): void {
    const telefoneFormGroup = this.formBuilder.group({
      ddd: [telefone?.ddd || ''],
      numeroTelefone: [telefone?.numeroTelefone || ''],
      tipo: [telefone?.tipo || '']
    });
  
    if (this.telefones) {
      this.telefones.push(telefoneFormGroup);
    } else {
      console.error('O FormArray de telefones é nulo.');
    }
  }
  

get enderecos(): FormArray {
  return this.clienteForm.get('enderecos') as FormArray;
}

adicionarEndereco(endereco?: any): void {
  const enderecoFormGroup = this.formBuilder.group({
    nome: [endereco?.nome || ''],
    logradouro: [endereco?.logradouro || ''],
    numeroLote: [endereco?.numeroLote || ''],
    bairro: [endereco?.bairro || ''],
    complemento: [endereco?.complemento || ''],
    cep: this.formBuilder.group({
      prefixo: [endereco?.cep?.prefixo || ''],
      sufixo: [endereco?.cep?.sufixo || ''],
      cep: [endereco?.cep?.cep || '']
    }),
    localidade: [endereco?.localidade || ''],
    uf: [endereco?.uf || ''],
    pais: [endereco?.pais || ''],
  });

  if (this.enderecos) {
    this.enderecos.push(enderecoFormGroup);
  } else {
    console.error('O FormArray de endereços é nulo.');
  }
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
  editarCliente(id: number): void {
    const enderecoEdicao: string = "clientes/edit/" + id.toString();
    this.navigationService.navigateTo(enderecoEdicao);
}
apagarCliente(id: number): void {
  this.clienteService.delete(id).subscribe({
    next:  (response) => {
          this.clienteService.notificarClienteInserido();
      },
      error: (error) => {
      console.error(error);
      window.alert(error); // Exibe a mensagem de erro usando window.alert()
      }
  });
}
}
