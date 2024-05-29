import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Cliente } from '../../../../models/cliente.model';
import { ClienteService } from '../../../../services/cliente.service';
import { NavigationService } from '../../../../services/navigation.service';
import { validarSenhaUpdate } from '../../../../validators/update-senha.validator';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { CepService } from '../../../../services/cep.service';


@Component({
  selector: 'app-cliente-view',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './cliente-view.component.html',
  styleUrls: ['./cliente-view.component.css'],
  providers: [provideNgxMask()]
})

export class ClienteViewComponent implements OnInit {
  errorMessage: string | null = null;
  errorDetails: any | null = null; // Objeto JSON para armazenar os detalhes do erro

  cliente: Cliente;
  clienteForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];
  id: number = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clienteService: ClienteService,
    private formBuilder: FormBuilder,
    private navigationService: NavigationService,
    private cepService: CepService
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

    //const idParam = this.route.snapshot.paramMap.get('id');
    console.log(this.id);
    if (this.id !== null) {
        this.clienteService.findById(this.id).subscribe(cliente => {
          this.cliente = cliente;

          // Inserindo valores nos campos de nome, cnpj e endSite
          this.clienteForm.patchValue({
            nome: cliente.nome,
            dataNascimento: this.formatarData(cliente.dataNascimento),
            cpf: this.formatarCPF(cliente.cpf),
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

  desativarSeletores(): boolean{
    return true;
  }

get telefones(): FormArray {
  return this.clienteForm.get('telefones') as FormArray;
}

  adicionarTelefone(telefone?: any): void {
    const telefoneFormGroup = this.formBuilder.group({
      ddd: [telefone ? telefone.ddd : '', Validators.required],
      numeroTelefone: [telefone ? this.formatarTelefone(telefone.numeroTelefone) : '', Validators.required],
      tipo: [telefone ? telefone.tipo : '']
    });

    this.telefones.push(telefoneFormGroup);
  }

formatarTelefone(telefone: string): string {
  // Remove todos os caracteres não numéricos
  const telefoneDigits = telefone.replace(/\D/g, '');

  // Verifica se o telefone possui 10 ou 11 dígitos
  if (telefoneDigits.length === 10) {
    // Formata o telefone no formato para telefones fixos (0000-0000)
    return telefoneDigits.replace(/(\d{4})(\d{4})/, '$1-$2');
  } else if (telefoneDigits.length === 11) {
    // Formata o telefone no formato para celulares (00000-0000)
    return telefoneDigits.replace(/(\d{5})(\d{4})/, '$1-$2');
  } else {
    // Retorna o telefone original se não possuir 10 ou 11 dígitos
    return telefone;
  }
}


  get enderecos(): FormArray {
    return this.clienteForm.get('enderecos') as FormArray;
  }

  formatarData(data: string): string {
    const partesData = data.split('-');
    return `${partesData[2]}/${partesData[1]}/${partesData[0]}`;
  }

  formatarCPF(cpf: string): string {
    // Remove todos os caracteres não numéricos
    const cpfDigits = cpf.replace(/\D/g, '');

    // Verifica se o CPF possui 11 dígitos
    if (cpfDigits.length === 11) {
      // Formata o CPF no formato desejado
      return cpfDigits.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    } else {
      // Retorna o CPF original se não possuir 11 dígitos
      return cpf;
    }
  }

adicionarEndereco(endereco?: any): void {
  const enderecoFormGroup = this.formBuilder.group({
    nome: [endereco && endereco.nome ? endereco.nome : '', Validators.required],
    logradouro: [endereco && endereco.logradouro ? endereco.logradouro : ''],
    numeroLote: [endereco && endereco.numeroLote ? endereco.numeroLote : ''],
    bairro: [endereco && endereco.bairro ? endereco.bairro : ''],
    complemento: [endereco && endereco.complemento ? endereco.complemento : ''],
    cep: [endereco && endereco.cep ? endereco.cep : ''],
    localidade: [endereco && endereco.localidade ? endereco.localidade : ''],
    uf: [endereco && endereco.uf ? endereco.uf : ''],
    pais: [endereco && endereco.pais ? endereco.pais : ''],
    cepInvalido: [false],
  });

  this.enderecos.push(enderecoFormGroup);
}

  // Método para apagar um fornecedor escolhido
    apagarCliente(id: number): void {
      this.clienteService.delete(id).subscribe({
        next:  (response) => {
              this.clienteService.notificarClienteInserido();
              this.navigationService.navigateTo("adm/clientes");
          },
          error: (error) => {
            console.error('Erro:', error);
            //window.alert(error)
            this.errorMessage = error.error.errorMessage;
            this.errorDetails = error;
          }
      });

  }

  // Método para chamar o endpoint para edição de um Cliente escolhido
  editarCliente(id: number): void {
    const enderecoEdicao: string = "/adm/clientes/edit/" + id.toString();
    this.navigationService.navigateTo(enderecoEdicao);
}

verClientes(): void{
  const enderecoList: string = "/adm/clientes";
  this.navigationService.navigateTo(enderecoList);
}
}
