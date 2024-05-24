import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FuncionarioService } from '../../../../services/funcionario.service';
import { Funcionario } from '../../../../models/funcionario.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { NavigationService } from '../../../../services/navigation.service';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { NgxViacepService } from '@brunoc/ngx-viacep';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';


@Component({
  selector: 'app-funcionario-edit',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './funcionario-view.component.html',
  styleUrls: ['./funcionario-view.component.css'], 
  providers: [provideNgxMask()]
})

export class FuncionarioViewComponent implements OnInit {
    errorMessage: string | null = null;
    errorDetails: any | null = null;
  funcionario: Funcionario;
  funcionarioForm: FormGroup;
  tiposTelefone: any[];
  tiposPerfil: any[];
  uf: any[];
  id: number = 1;
funcionarios: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private funcionarioService: FuncionarioService,
    private formBuilder: FormBuilder,
    private navigationService: NavigationService,
    private viaCep: NgxViacepService
  ) {
    this.tiposTelefone = [];
    this.tiposPerfil = [];
    this.uf = [];
    this.funcionario = new Funcionario(); // Inicialização no construtor

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
}

  ngOnInit(): void {

    this.id = Number(this.route.snapshot.paramMap.get('id'));
    //this.enderecoAnterior = this.navigationService

    this.funcionarioService.getTipoTelefone().subscribe(data => {
      this.tiposTelefone = data;
    });

    this.funcionarioService.getUF().subscribe(data => {
      this.uf = data;
    });

    this.funcionarioService.getTipoPerfil().subscribe(data => {
      this.tiposPerfil = data;
    });

    //const idParam = this.route.snapshot.paramMap.get('id');
    console.log(this.id);
    if (this.id !== null) {
        this.funcionarioService.findById(this.id).subscribe(funcionario => {
          this.funcionario = funcionario;
          // Inserindo valores nos campos de nome, cnpj e endSite
          this.funcionarioForm.patchValue({
            nome: funcionario.nome,
            dataNascimento: this.formatarData(funcionario.dataNascimento),
            cpf: this.formatarCPF(funcionario.cpf),
            sexo: funcionario.sexo,
            idperfil: funcionario.idperfil,
            login: funcionario.login,
            senha: funcionario.senha,
            email: funcionario.email,
            endereco: funcionario.listaEndereco,
            });

          // Populando o FormArray com os dados existentes de funcionario.listaTelefone
          funcionario.listaTelefone.forEach(telefone => {
            telefone.numeroTelefone = this.formatarTelefone(telefone.numeroTelefone);
            this.adicionarTelefone(telefone);
          }
        );
          
          console.log(this.funcionario);
        });
      } else {
        console.error('O parâmetro "id" não foi fornecido ou é inválido.');
      }

      const cepControl = this.endereco.get('cep');
      if (cepControl?.value !== '') {
        cepControl?.valueChanges.pipe(
          debounceTime(300), // Aguarda 300ms após a última mudança no campo
          distinctUntilChanged() // Garante que a busca só será feita se o valor do campo for alterado
        ).subscribe((cep: string | null) => {
          if (cep !== null) {
            this.atualizarEndereco(cep, this.endereco);
          }
        });

  }
}

get telefones(): FormArray {
  return this.funcionarioForm.get('telefones') as FormArray;
}

adicionarTelefone(telefone?: any): void {
  const numeroTelefoneFormatado = telefone ? this.formatarTelefone(telefone.numeroTelefone) : '';

  const telefoneFormGroup = this.formBuilder.group({
    ddd: [telefone ? telefone.ddd : ''],
    numeroTelefone: [numeroTelefoneFormatado],
    tipo: [telefone ? telefone.tipo : '']
  });
  
  this.telefones.push(telefoneFormGroup);
}

removerTelefone(index: number): void {
  this.telefones.removeAt(index);
}

get endereco(): FormGroup {
  return this.funcionarioForm.get('endereco') as FormGroup;
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
  
  
  verFuncionarios(): void{
    const enderecoList: string = "funcionarios";
    this.navigationService.navigateTo(enderecoList);
  }

  editarFuncionario(id: number): void {
    const enderecoEdicao: string = "funcionarios/edit/" + id.toString();
    this.navigationService.navigateTo(enderecoEdicao);
}
apagarFuncionario(id: number): void {
  this.funcionarioService.delete(id).subscribe({
    next:  (response) => {
          this.funcionarioService.notificarFuncionarioInserido();
      },
      error: (error) => {
      console.error(error);
      window.alert(error); // Exibe a mensagem de erro usando window.alert()
      }
  });
}
}
