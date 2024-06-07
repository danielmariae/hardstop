import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { Funcionario } from '../../../../models/funcionario.model';
import { FuncionarioService } from '../../../../services/funcionario.service';
import { NavigationService } from '../../../../services/navigation.service';
import { cpfValidator } from '../../../../validators/cpf.validator';
import { dataValidator } from '../../../../validators/data.validator';
import { idadeValidator } from '../../../../validators/idade.validator';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { formatarDataNascimento } from '../../../../utils/date-converter';
import {CepService} from "../../../../services/cep.service";

@Component({
  selector: 'app-funcionario-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgxMaskDirective],
  templateUrl: './funcionario-form.component.html',
  styleUrl: './funcionario-form.component.css',
  providers: [provideNgxMask()]
})
export class FuncionarioFormComponent {
  errorMessage: string | null = null;
  errorDetails: any | null = null;
  funcionarioForm: FormGroup;
  tiposTelefone: any[];
  tiposPerfil: any[];
  uf: any[];

  constructor(private formBuilder: FormBuilder,
    private funcionarioService: FuncionarioService,
    private navigationService: NavigationService,
    private cepService: CepService) {
    this.tiposTelefone = [];
    this.uf = [];
    this.tiposPerfil = [];
    // Inicializar funcionarioForm no construtor
    this.funcionarioForm = formBuilder.group({
      nome: ['', Validators.required],
      dataNascimento: this.formBuilder.control('',
        {
          validators: [
            Validators.required,
            Validators.pattern(/^(0[1-9]|[1-2][0-9]|3[0-1])\/(0[1-9]|1[0-2])\/[0-9]{4}$/),
            idadeValidator(),
            dataValidator()
          ]
        }
      ),
      cpf: this.formBuilder.control('', {
        validators: [
          Validators.required, cpfValidator()
        ],
        nonNullable: true,
      }),
      sexo: this.formBuilder.control('', {
        validators: [
          Validators.required
        ],
        nonNullable: true,
      }),
        login: ['', Validators.required],
        senha: this.formBuilder.control('', {
          validators: [
            Validators.required,
            Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[-[!“#$%&'()*+,-./:;<=>?@[\]^_`{|}~]+).{6,10}$/)
          ],
          nonNullable: true,
        }),
        email: this.formBuilder.control('', {
          validators: [
            Validators.required,
            Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')
          ],
          nonNullable: true,
        }),
        idperfil: this.formBuilder.control('', {
          validators: [
            Validators.required
          ],
          nonNullable: true,
        }),
        telefones: this.formBuilder.array([]),
        endereco: this.formBuilder.group({
          cep: ['', Validators.required],
          logradouro: [''],
          numeroLote: ['', Validators.required],
          complemento: ['', Validators.required],
          bairro: [''],
          localidade: [''],
          uf: [''],
          pais: [''],
          cepInvalido: [false],
        }),
    });

    // Adicionar um observador para o campo de CEP
    this.endereco.get('cep')?.valueChanges.pipe(
        debounceTime(300), // Aguarda 300ms após a última mudança no campo
        distinctUntilChanged() // Garante que a busca só será feita se o valor do campo for alterado
          ).subscribe((cep: string | null) => {
            if (cep !== null) {
              this.atualizarEndereco(cep, this.endereco);
              }
    })
  }

  ngOnInit(): void {
    this.funcionarioService.getTipoTelefone().subscribe(data => {
      this.tiposTelefone = data;
    });

    this.funcionarioService.getTipoPerfil().subscribe(data => {
      this.tiposPerfil = data;
    })

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
      ddd: ['', Validators.required],
      numeroTelefone: ['', Validators.pattern],
      tipo: ['', Validators.required],
    });
  }

  get endereco(): FormGroup {
    return this.funcionarioForm.get('endereco') as FormGroup;
  }

  // Método atualizarEndereco atualizado para receber apenas o CEP e o FormGroup:
  atualizarEndereco(cep: string, enderecoFormGroup: FormGroup): void {
    const cepValue = cep.replace(/\D/g, ''); // Remove caracteres não numéricos do CEP

    if (cepValue.length === 8) { // Verifica se o CEP possui 8 dígitos
      this.cepService.findByStringCep(cepValue).subscribe({
        next: (endereco) => {
          if (endereco && !endereco.erro) { // Verifica se o campo erro é false
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

  cancelarInsercao(): void {
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateTo('adm/funcionarios');
}

  salvarFuncionario(): void {
    console.log(this.funcionarioForm.value.id)
    if (this.funcionarioForm.errors) {
      console.error('Formulário inválido. Por favor, corrigir campos incorretos.')
      return;
    }

    const novoFuncionario: Funcionario = {
      id: 0,
      nome: this.funcionarioForm.value.nome,
      dataNascimento: formatarDataNascimento(this.funcionarioForm.value.dataNascimento),
      cpf: this.funcionarioForm.value.cpf,
      sexo: this.funcionarioForm.value.sexo,
      idperfil: this.funcionarioForm.value.idperfil,
      login: this.funcionarioForm.value.login,
      senha: this.funcionarioForm.value.senha,
      email: this.funcionarioForm.value.email,
      listaTelefone: this.funcionarioForm.value.telefones,
      listaEndereco: this.funcionarioForm.value.endereco,
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
