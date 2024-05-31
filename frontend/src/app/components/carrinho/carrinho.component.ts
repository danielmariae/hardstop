import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ItemCarrinho } from '../../models/itemcarrinho.model';
import { CarrinhoService } from '../../services/carrinho.service';
import { HeaderHomeComponent } from '../template/home-template/header-home/header-home.component';
import { Cliente } from '../../models/cliente.model';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClienteService } from '../../services/cliente.service';
import { Subscription, debounceTime, distinctUntilChanged } from 'rxjs';
import { SessionTokenService } from '../../services/session-token.service';
import { NgxViacepService } from '@brunoc/ngx-viacep';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-carrinho',
  standalone: true,
  imports: [NgFor, NgIf, HeaderHomeComponent, FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './carrinho.component.html',
  styleUrl: './carrinho.component.css'
})
export class CarrinhoComponent implements OnInit {

  carrinhoItens: ItemCarrinho[] = [];
  clienteForm: FormGroup;
  clienteLogado: Cliente | null = null;
  enderecoFormGroup: FormGroup;
  private subscription = new Subscription();
  enderecos: FormArray;
  selectedAddress: number | null;
  showNewAddressForm = false; // usado para esconder o formulário para novo endereço
  addressesAdded = false; // usado para desabilitar o botão Adicionar endereço após seu uso

  constructor(private carrinhoService: CarrinhoService,
              private clienteService: ClienteService,
              private formBuilder: FormBuilder,
              private sessionTokenService: SessionTokenService,
              private viaCep: NgxViacepService,

  ) {
   
    this.selectedAddress = null;

    this.clienteForm = this.formBuilder.group({
      enderecos: this.formBuilder.array([]),
  });
  this.enderecos = this.clienteForm.get('enderecos') as FormArray;

  this.enderecoFormGroup = this.formBuilder.group({
    id: [null],
    nome: ['', Validators.required],
    cep: [''],
    logradouro: [''],
    numeroLote: [''],
    bairro: [''],
    complemento: [''],
    localidade: [''],
    uf: [''],
    pais: [''],
    cepInvalido: [false],
  });

  this.setupCepObserver();

  }

  ngOnInit(): void {
    this.carrinhoService.carrinho$.subscribe( itens => {
      this.carrinhoItens = itens;
    });

    this.clienteLogado = JSON.parse(localStorage.getItem('clienteLogado') || 'null');
    if (!this.clienteLogado) {
      this.obterClienteLogado();
    }
    console.log(this.clienteLogado);

     // Carregar endereços pré-existentes
     this.carregarEnderecos();

  }

  obterClienteLogado() {
    this.subscription.add(this.sessionTokenService.getClienteLogado().subscribe(
    cliente => this.clienteLogado = cliente
  ));
  }

  carregarEnderecos(): void {

    // Populando o FormArray com os dados existentes de cliente.listaEndereco
    // if(this.cliente.listaEndereco)
    //   this.cliente.listaEndereco.forEach(endereco => {
    //     this.adicionarEndereco(endereco);
    //   });

    if(this.clienteLogado) {
    console.log(this.clienteLogado.listaEndereco);
    
         this.clienteLogado.listaEndereco.forEach(endereco => {
           this.enderecos.push(this.formBuilder.group(endereco));
         });
        }
  }

  removerItem(item: ItemCarrinho): void {
    this.carrinhoService.remover(item);
  }
  
  finalizarCompra(): void {

  }

  diminuirQuantidade(item: ItemCarrinho) {
    if (item.quantidade > 1) {
      item.quantidade--;
      this.calcularTotal(); // Atualizar o total do carrinho
    }
  }
  
  aumentarQuantidade(item: ItemCarrinho) {
    console.log(item.quantidadeLimite);
    if(item.quantidade < item.quantidadeLimite) {
    item.quantidade++;
    this.calcularTotal(); // Atualizar o total do carrinho
    }
  }

  calcularTotal(): number {
    let total = 0;
  for (const item of this.carrinhoItens) {
    total += item.quantidade * item.preco;
  }
  return total;
  }

  // get enderecos(): FormArray {
  //   return this.clienteForm.get('enderecos') as FormArray;
  // }
  
  setupCepObserver(): void {
    this.enderecoFormGroup.get('cep')?.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe((cep: string | null) => {
      if (cep !== null) {
        this.atualizarEndereco(cep, this.enderecoFormGroup);
      }
    });
  }

  adicionarEndereco(endereco?: any): void {
    this.selectedAddress = null; // Deseleciona qualquer seleção que tenha sido feita para algum endereço pre-existente.
    this.addressesAdded = true; // Habilita o Disable do botão Adicionar endereço
    if (this.showNewAddressForm) {
      // Se o formulário já está sendo exibido, não faz nada.
      return;
    }

    if (endereco) {
      this.enderecoFormGroup.patchValue({
        nome: endereco.nome || '',
        logradouro: endereco.logradouro || '',
        numeroLote: endereco.numeroLote || '',
        bairro: endereco.bairro || '',
        complemento: endereco.complemento || '',
        cep: endereco.cep || '',
        localidade: endereco.localidade || '',
        uf: endereco.uf || '',
        pais: endereco.pais || '',
      });
    } else {
      this.enderecoFormGroup.reset();
    }

    this.showNewAddressForm = true;
  }

  salvarEndereco(): void {
    this.enderecos.push(this.formBuilder.group(this.enderecoFormGroup.value));
    this.showNewAddressForm = false;
    this.addressesAdded = true;
  }

  cancelarEndereco(): void {
    this.showNewAddressForm = false;
    this.enderecoFormGroup.reset();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }



  // adicionarEndereco(endereco?: any): void {
 
  //   this.enderecoFormGroup = this.formBuilder.group({
  //     nome: [endereco && endereco.nome ? endereco.nome : '', Validators.required],
  //     logradouro: [endereco && endereco.logradouro ? endereco.logradouro : ''],
  //     numeroLote: [endereco && endereco.numeroLote ? endereco.numeroLote : ''],
  //     bairro: [endereco && endereco.bairro ? endereco.bairro : ''],
  //     complemento: [endereco && endereco.complemento ? endereco.complemento : ''],
  //     cep: [endereco && endereco.cep ? endereco.cep : ''],
  //     localidade: [endereco && endereco.localidade ? endereco.localidade : ''],
  //     uf: [endereco && endereco.uf ? endereco.uf : ''],
  //     pais: [endereco && endereco.pais ? endereco.pais : ''],
  //     cepInvalido: [false],
  //   });
  
  //     // Adicionar um observador para o campo de CEP dentro do FormGroup
  //     this.enderecoFormGroup.get('cep')?.valueChanges.pipe(
  //       debounceTime(300),
  //       distinctUntilChanged()
  //     ).subscribe((cep: string | null) => {
  //       if (cep !== null) {
  //         this.atualizarEndereco(cep, this.enderecoFormGroup);
  //       }
  //     });
    
  //   this.enderecos.push(this.enderecoFormGroup);
  //   this.addressesAdded = true;
  //   this.showNewAddressForm = true; // Mostra o formulário
    
  // }
  
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
  
  removerEndereco(index: number): void {
    this.enderecos.removeAt(index);
  }

  getSelectedAddress() {
    if (this.selectedAddress !== null) {
      console.log(this.selectedAddress);
      const selectedEndereco = this.enderecos.controls[this.selectedAddress].value;
      console.log("Selected Endereço:", selectedEndereco);
      return this.enderecos.at(this.selectedAddress);
    }
    return null; // Casos onde nenhum endereço é selecionado
  }

}