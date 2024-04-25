import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Cliente } from '../../../models/cliente.model';
import { ClienteService } from '../../../services/cliente.service';
import { NavigationService } from '../../../services/navigation.service';
import { validarSenhaUpdate } from '../../../validators/update-senha-validator';
import { formatarDataNascimento } from '../../../converters/date-converter';
import { NgxViacepService } from '@brunoc/ngx-viacep';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';


@Component({
  selector: 'app-cliente-view',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './cliente-view.component.html',
  styleUrls: ['./cliente-view.component.css'],
  providers: [provideNgxMask()]
})

export class ClienteViewComponent implements OnInit {
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
    private navigationService: NavigationService,
    private viaCep: NgxViacepService
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

    // Adicionar um observador para o campo de CEP dentro do FormGroup
    enderecoFormGroup.get('cep')?.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe((cep: string | null) => {
      if (cep !== null) {
        this.atualizarEndereco(cep, enderecoFormGroup);
      }
    });
  
  this.enderecos.push(enderecoFormGroup);
}

atualizarEndereco(cep: string, enderecoFormGroup: FormGroup): void {
  const cepValue = cep.replace(/\D/g, ''); // Remove caracteres não numéricos do CEP

  if (cepValue.length === 8) { // Verifica se o CEP possui 8 dígitos
    this.viaCep.buscarPorCep(cepValue).subscribe({
      next: (endereco) => {
        if (endereco && Object.keys(endereco).length > 0) { // Verifica se o objeto de endereço retornado não está vazio
          // Atualizando os valores do formulário com os dados do endereço
          enderecoFormGroup.patchValue({
            cep: endereco.cep,
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


removerEndereco(index: number): void {
  this.enderecos.removeAt(index);
}

  cancelarEdicao(): void {
    // Redireciona o usuário para outra rota anterior
    this.navigationService.navigateTo('clientes');
  }

  


  salvarAlteracoes(): void {
    // const idParam = Number(this.route.snapshot.paramMap.get('id'));
    console.log(this.clienteForm.value.id)
    if (this.clienteForm.errors) {
      console.error('Formulário inválido. Por favor, corrigir campos incorretos.')
      return;
    }

    const novoCliente: Cliente = {
      id: this.clienteForm.value.id,
      nome: this.clienteForm.value.nome,
      dataNascimento: formatarDataNascimento(this.clienteForm.value.dataNascimento),
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
        if(this.navigationService.getPreviousEndPoint() === 'clientes') {
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

    // Método para apagar um fornecedor escolhido
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

  // Método para chamar o endpoint para edição de um Cliente escolhido
  editarCliente(id: number): void {
    const enderecoEdicao: string = "clientes/edit/" + id.toString();
    this.navigationService.navigateTo(enderecoEdicao);
}

verClientes(): void{
  const enderecoList: string = "clientes";
  this.navigationService.navigateTo(enderecoList);
}
}
