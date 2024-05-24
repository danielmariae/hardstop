import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { Cliente } from '../../../models/cliente.model';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormArray, Validators, FormControl, ValidatorFn } from '@angular/forms';
import { ClienteService } from '../../../services/cliente.service';
import { NavigationService } from '../../../services/navigation.service';
import { cpfValidator } from '../../../validators/cpf.validator';
import { idadeValidator } from '../../../validators/idade.validator';
import { dataValidator } from '../../../validators/data.validator';
import { HttpClient } from '@angular/common/http';
import { NgxViacepService } from '@brunoc/ngx-viacep';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { formatarDataNascimento } from '../../../utils/date-converter';

@Component({
  selector: 'app-create-user-account',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgxMaskDirective],
  templateUrl: './create-user-account.component.html',
  styleUrl: './create-user-account.component.css',
  providers: [provideNgxMask()]
})
export class CreateUserAccountComponent {
  errorMessage: string | null = null;
  errorDetails: any | null = null; // Objeto JSON para armazenar os detalhes do erro
  clienteForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];

  constructor(
    private formBuilder: FormBuilder, 
    private clienteService: ClienteService,
    private router: Router, 
    private activatedRoute: ActivatedRoute, 
    private navigationService: NavigationService,
    private http: HttpClient, 
    private viaCep: NgxViacepService) {
    this.tiposTelefone = [];
    this.uf = [];
    // Inicializar clienteForm no construtor
    this.clienteForm = formBuilder.group({
      id: [null],
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
      telefones: this.formBuilder.array([]),
      enderecos: this.formBuilder.array([]),
    });
    //    console.log(this.clienteForm.get('dataNascimento'))
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
      id: [null],
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

  // No método criarEnderecoFormGroup():

  criarEnderecoFormGroup(): FormGroup {
    const enderecoFormGroup = this.formBuilder.group({
      id: [null],
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
    });
  
    // Adicionar um observador para o campo de CEP
    enderecoFormGroup.get('cep')?.valueChanges.pipe(
      debounceTime(300), // Aguarda 300ms após a última mudança no campo
      distinctUntilChanged() // Garante que a busca só será feita se o valor do campo for alterado
    ).subscribe((cep: string | null) => {
      if (cep !== null) {
        this.atualizarEndereco(cep, enderecoFormGroup);
      }
    });
  
    return enderecoFormGroup;
  }

  // Método atualizarEndereco atualizado para receber apenas o CEP e o FormGroup:
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
      

  cancelarInsercao(): void {
    // Redireciona o usuário para a rota anterior
    this.navigationService.navigateTo("login/user");
  }

  salvarCliente(): void {
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

    
    // Chamando o serviço para inserir o cliente
    this.clienteService.insert(novoCliente).subscribe({
      next: (response) => {
      console.log('Cliente inserido com sucesso:', response);
        this.clienteService.notificarClienteInserido(); // Notificar outros componentes
        this.navigationService.navigateTo('login/user')
      },
      error: (error) => {
        // Este callback é executado quando ocorre um erro durante a emissão do valor
        console.error('Erro:', error);
        //window.alert(error)
        this.errorMessage = error.error.errorMessage;
        this.errorDetails = error;
      }
    });

  }
}
