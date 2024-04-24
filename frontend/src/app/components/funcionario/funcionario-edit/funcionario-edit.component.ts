import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FuncionarioService } from '../../../services/funcionario.service';
import { Funcionario } from '../../../models/funcionario.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { NavigationService } from '../../../services/navigation.service';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { NgxViacepService } from '@brunoc/ngx-viacep';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';


@Component({
  selector: 'app-funcionario-edit',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './funcionario-edit.component.html',
  styleUrls: ['./funcionario-edit.component.css'],
  providers: [provideNgxMask()]
})

export class FuncionarioEditComponent implements OnInit {
  funcionario: Funcionario;
  funcionarioForm: FormGroup;
  tiposTelefone: any[];
  tiposPerfil: any[];
  uf: any[];
  id: number = 1;

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
      nome: ['', Validators.required],
      dataNascimento: ['', Validators.required],
      cpf: ['', Validators.required],
      sexo: ['', Validators.required],
      login: ['', Validators.required],
      senha: ['', Validators.required],
      email: ['', Validators.required],
      idperfil: ['',  Validators.required],
      telefones: this.formBuilder.array([]),
      endereco: this.formBuilder.group({
        nome: ['', Validators.required],
        cep: ['', Validators.required],
        logradouro: [''],
        numeroLote: [''],
        complemento: ['', Validators.required],
        bairro: [''],
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
    })

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
            this.adicionarTelefone(telefone);
          });
          
          // Formatando o número de telefone antes de adicionar ao FormArray
          funcionario.listaTelefone.forEach(telefone => {
            telefone.numeroTelefone = this.formatarTelefone(telefone.numeroTelefone);
          });

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
  
  cancelarEdicao(): void {
    // Redireciona o usuário para outra rota anterior
    this.navigationService.navigateTo('funcionarios');
  }


  salvarAlteracoes(): void {
    
    // const idParam = Number(this.route.snapshot.paramMap.get('id'));
    console.log(this.id);
    const novoFuncionario: Funcionario = {
      id: this.funcionarioForm.value.id,
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
    
    // IMPLEMENTAR LÓGICA DE ATUALIZAR SEM SENHA NOVA
    if(this.funcionarioForm.value.senha === undefined || this.funcionarioForm.value.senha === null){
      this.funcionarioService.updateNS(novoFuncionario, this.funcionario.id).subscribe({
        next: (response) => {
          console.log(novoFuncionario);
          if(this.navigationService.getPreviousEndPoint() === 'clientes') {
            this.funcionarioService.notificarFuncionarioInserido(); // Notificar outros componentes
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
  
    }else{
          // Lógica para salvar as alterações do funcionario
    this.funcionarioService.update(novoFuncionario, this.funcionario.id).subscribe({
      next: (response) => {
        console.log(novoFuncionario);
        if(this.navigationService.getPreviousEndPoint() === 'funcionarios') {
          this.funcionarioService.notificarFuncionarioInserido(); // Notificar outros componentes
        } else {
          this.navigationService.navigateTo('funcionarios');
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
}
