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
  templateUrl: './funcionario-view.component.html',
  styleUrls: ['./funcionario-view.component.css']
})

export class FuncionarioViewComponent implements OnInit {
  funcionario: Funcionario;
  funcionarioForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];
  enderecoAnterior: string = '';
  id: number = 1;
funcionarios: any;

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
            listaEndereco: funcionario.listaEndereco,
            perfil: funcionario.perfil
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

  get telefones(): FormArray | null {
    return this.funcionarioForm ? this.funcionarioForm.get('telefones') as FormArray : null;
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
  

get listaEndereco(): FormArray {
  return this.funcionarioForm.get('listaEndereco') as FormArray;
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

  if (this.listaEndereco) {
    this.listaEndereco.push(enderecoFormGroup);
  } else {
    console.error('O FormArray de endereços é nulo.');
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
