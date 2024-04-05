import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FuncionarioService } from '../../../services/funcionario.service';
import { Funcionario } from '../../../models/funcionario.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { NavigationService } from '../../../services/navigation.service';


@Component({
  selector: 'app-funcionario-edit',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './funcionario-edit.component.html',
  styleUrls: ['./funcionario-edit.component.css']
})

export class FuncionarioEditComponent implements OnInit {
  funcionario: Funcionario;
  funcionarioForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];
  enderecoAnterior: string = '';
  id: number = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private funcionarioService: FuncionarioService,
    private formBuilder: FormBuilder,
    private navigationService: NavigationService
  ) {
    this.tiposTelefone = [];
    this.uf = [];
    this.funcionario = new Funcionario(); // Inicialização no construtor
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
      listaEndereco: this.formBuilder,
      perfil: ['']
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

    //const idParam = this.route.snapshot.paramMap.get('id');
    console.log(this.id);
    if (this.id !== null) {
        this.funcionarioService.findById(this.id).subscribe(funcionario => {
          this.funcionario = funcionario;

          // Inserindo valores nos campos de nome, cnpj e endSite
          this.funcionarioForm.patchValue({
            nome: funcionario.nome,
            dataNascimento: funcionario.dataNascimento,
            cpf: funcionario.cpf,
            sexo: funcionario.sexo,
            login: funcionario.login,
            senha: funcionario.senha,
            email: funcionario.email,
            perfil: funcionario.perfil,
            listaEndereco: funcionario.listaEndereco,
            });

          // Populando o FormArray com os dados existentes de funcionario.listaTelefone
          funcionario.listaTelefone.forEach(telefone => {
            this.adicionarTelefone(telefone);
          });

          
          console.log(this.funcionario);
        });
      } else {
        console.error('O parâmetro "id" não foi fornecido ou é inválido.');
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

get listaEndereco(): FormArray {
  return this.funcionarioForm.get('listaEndereco') as FormArray;
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

  this.listaEndereco.push(enderecoFormGroup);
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
      login: this.funcionarioForm.value.login,
      senha: this.funcionarioForm.value.senha,
      email: this.funcionarioForm.value.email,
      listaTelefone: this.funcionarioForm.value.telefones,
      listaEndereco: this.funcionarioForm.value.listaEndereco,
      perfil: this.funcionarioForm.value.perfil
    };
    
    // Lógica para salvar as alterações do funcionario
    this.funcionarioService.update(novoFuncionario, this.funcionario.id).subscribe({
      next: (response) => {
        console.log(novoFuncionario);
        if(this.navigationService.getPreviousEndPoint() === 'funcionarioes') {
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
