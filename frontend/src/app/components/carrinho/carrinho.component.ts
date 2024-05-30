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
  cliente: Cliente;
  clienteForm: FormGroup;
  clienteLogado: Cliente | null = null;
  private subscription = new Subscription();
  selectedAddress: number = -1; // Initialize to null

  constructor(private carrinhoService: CarrinhoService,
              private clienteService: ClienteService,
              private formBuilder: FormBuilder,
              private sessionTokenService: SessionTokenService,
              private viaCep: NgxViacepService,

  ) {
    this.cliente = new Cliente();
    this.clienteForm = formBuilder.group({
      enderecos: this.formBuilder.array([]),
  });
  }

  ngOnInit(): void {
    this.carrinhoService.carrinho$.subscribe( itens => {
      this.carrinhoItens = itens;
    });

    this.subscription.add(this.sessionTokenService.getClienteLogado().subscribe(
      cliente => this.clienteLogado = cliente
    ));

    // Populando o FormArray com os dados existentes de cliente.listaEndereco
    if(this.cliente.listaEndereco)
    this.cliente.listaEndereco.forEach(endereco => {
      this.adicionarEndereco(endereco);
    });

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
    if (this.selectedAddress !== -1) {
      return this.enderecos.at(this.selectedAddress);
    }
    return null; // Handle cases where no address is selected
  }

}