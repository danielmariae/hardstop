import { Component, OnInit } from "@angular/core";
import { HeaderHomeComponent } from "../template/home-template/header-home/header-home.component";
import { PedidoService } from "../../services/pedido.service";
import { FormBuilder, FormGroup, ReactiveFormsModule, FormControl, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { Cliente } from "../../models/cliente.model";
import { Subscription } from "rxjs";
import { SessionTokenComponent } from "../session-token/session-token.component";
import { SessionTokenService } from "../../services/session-token.service";
import { LocalStorageService } from "../../services/local-storage.service";
import { ListaEndereco } from "../../models/endereco.model";
import { CarrinhoService } from "../../services/carrinho.service";
import { ItemCarrinho } from "../../models/itemcarrinho.model";
import { Pedido } from "../../models/pedido.model";
import { FormaDePagamento } from "../../models/formaDePagamento";
import { ItemDaVenda } from "../../models/itemDaVenda";

@Component({
    selector: 'app-pagamento',
    standalone: true,
    imports: [HeaderHomeComponent, ReactiveFormsModule, CommonModule],
    templateUrl: './pagamento.component.html',
    styleUrl: './pagamento.component.css'
  })
  export class PagamentoComponent implements OnInit {
    clienteLogado: Cliente | null = null;
    valorFinal: number | null = null;
    enderecoEscolhido: ListaEndereco | null = null;
    carrinhoItens: ItemCarrinho[] = [];
    itensDaVenda: ItemDaVenda[] = [];
    private subscription = new Subscription();
        

    tiposPagamentoForm: FormGroup;
    tiposPagamento: any[];

    metodoPagamento: number | null;
    chavePix: string;
    numeroCartao: string;
    mesValidade: string;
    anoValidade: string;
    codSeguranca: string;
    diasVencimento: number | null;

    numeroCartaoControl: FormControl;
    mesValidadeControl: FormControl;
    anoValidadeControl: FormControl;
    codSegurancaControl: FormControl;
    diasVencimentoControl: FormControl;

    pagamentoFormGroup: FormGroup;


    constructor(private pedidoService: PedidoService,
                private formBuilder: FormBuilder,
                private sessionTokenService: SessionTokenService,
                private localStorageService: LocalStorageService,
                private carrinhoService: CarrinhoService
    ) {
      this.tiposPagamentoForm = formBuilder.group({
        metodoEscolhido: [null]
      })
      this.tiposPagamento = [];

      this.metodoPagamento = null;
      this.chavePix = ''; // String vazia
      this.numeroCartao = ''; // String vazia
      this.mesValidade = ''; // String vazia
      this.anoValidade = ''; // String vazia
      this.codSeguranca = ''; // String vazia
      this.diasVencimento = null;

      this.numeroCartaoControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]{16}$/),
      ]);
      this.mesValidadeControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]{2}$/),
        Validators.min(1),
        Validators.max(12),
      ]);
      this.anoValidadeControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]{4}$/),
        Validators.min(2024),
      ]);
      this.codSegurancaControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]{3}$/),
      ]);
      this.diasVencimentoControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]+$/),
        Validators.min(1),
      ]);
    
      this.pagamentoFormGroup = this.formBuilder.group({
        numeroCartao: this.numeroCartaoControl,
        mesValidade: this.mesValidadeControl,
        anoValidade: this.anoValidadeControl,
        codSeguranca: this.codSegurancaControl,
        diasVencimento: this.diasVencimentoControl,
      });
    }


    ngOnInit(): void {
      this.clienteLogado = JSON.parse(localStorage.getItem('clienteLogado') || 'null');
      if (!this.clienteLogado) {
        this.obterClienteLogado();
      }

      this.valorFinal = this.localStorageService.getItem('valorFinal');
      this.enderecoEscolhido = this.localStorageService.getItem('enderecoEscolhido');

      this.carrinhoService.carrinho$.subscribe( itens => {
        this.carrinhoItens = itens;
      });

      console.log(this.valorFinal);
      console.log(this.enderecoEscolhido)
      console.log(this.carrinhoItens);
      console.log(this.clienteLogado);

      this.pedidoService.getModalidadePagamento().subscribe(data => {
        this.tiposPagamento = data;
      });
      this.tiposPagamentoForm.get('metodoEscolhido')?.valueChanges.subscribe(value => {
        this.onPagamentoChange(value);
      });

      //this.criarFormularios();
    }

    obterClienteLogado() {
      this.subscription.add(this.sessionTokenService.getClienteLogado().subscribe(
      cliente => this.clienteLogado = cliente
    ));
    console.log(this.clienteLogado);
    }

    onPagamentoChange(value: any) {
      //console.log('Valor selecionado:', value);
      this.metodoPagamento = Number(value);
      console.log(this.metodoPagamento);
      // Aqui você pode adicionar lógica adicional para manipular o valor selecionado
    }


    criarFormularios() {
      this.numeroCartaoControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]{16}$/),
      ]);
      this.mesValidadeControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]{2}$/),
        Validators.min(1),
        Validators.max(12),
      ]);
      this.anoValidadeControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]{4}$/),
        Validators.min(2024),
      ]);
      this.codSegurancaControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]{3}$/),
      ]);
      this.diasVencimentoControl = new FormControl('', [
        Validators.required,
        Validators.pattern(/^[0-9]+$/),
        Validators.min(1),
      ]);
  
      this.pagamentoFormGroup = this.formBuilder.group({
        numeroCartao: this.numeroCartaoControl,
        mesValidade: this.mesValidadeControl,
        anoValidade: this.anoValidadeControl,
        codSeguranca: this.codSegurancaControl,
        diasVencimento: this.diasVencimentoControl,
      });
    }
  
    efetuarPagamento() {
      this.converterItemCarrinho();
      if(this.metodoPagamento === 2) { // Pagamento por Pix
        const formaDePagamento: FormaDePagamento = {
          id: null,
          modalidade: this.metodoPagamento,
          numeroCartao: null,
          mesValidade: null,
          anoValidade: null,
          codSeguranca: null,
          diasVencimento: null,
        }

        this.salvarPedido(formaDePagamento);

      } else if(this.metodoPagamento === 1) { // Pagamento por Boleto
        const formaDePagamento: FormaDePagamento = {
          id: null,
          modalidade: this.metodoPagamento,
          numeroCartao: null,
          mesValidade: null,
          anoValidade: null,
          codSeguranca: null,
          diasVencimento: this.diasVencimentoControl.value,
        }
        this.salvarPedido(formaDePagamento);

      } else if(this.metodoPagamento === 0) { // Pagamento por Cartão de Crédito
        const formaDePagamento: FormaDePagamento = {
          id: null,
          modalidade: this.metodoPagamento,
          numeroCartao: this.numeroCartaoControl.value,
          mesValidade: this.mesValidadeControl.value,
          anoValidade: this.anoValidadeControl.value,
          codSeguranca: this.codSegurancaControl.value,
          diasVencimento: null,
        }
        this.salvarPedido(formaDePagamento);

      }
      console.log('Pagamento efetuado!');
    }

    converterItemCarrinho(): void {
      this.itensDaVenda = this.carrinhoItens.map(() => ({ idProduto: null, nome: null, quantidadeUnidades: null,  quantidadeNaoConvencional: null, unidadeDeMedida: null, preco: null }));
      for (let i = 0; i < this.carrinhoItens.length; i++) {
        this.itensDaVenda[i].idProduto = this.carrinhoItens[i].id;
        console.log(this.itensDaVenda[i].idProduto);
        this.itensDaVenda[i].nome = this.carrinhoItens[i].nome;
        console.log(this.itensDaVenda[i].nome);
        this.itensDaVenda[i].quantidadeUnidades = this.carrinhoItens[i].quantidade;
        console.log(this.itensDaVenda[i].quantidadeUnidades);
        this.itensDaVenda[i].preco = this.carrinhoItens[i].preco;
        console.log(this.itensDaVenda[i].preco);
      }
    }

    salvarPedido(formaDePagamento: FormaDePagamento): void {

      if (this.clienteLogado && this.clienteLogado.id !== undefined) {
      if(this.enderecoEscolhido && this.enderecoEscolhido.id !== undefined) {
      const novoPedido: Pedido = {
        id: null,
        idCliente: this.clienteLogado.id,
        codigoDeRastreamento: null,
        idEndereco: this.enderecoEscolhido.id,
        statusDoPedido: null,
        itemDaVenda: this.itensDaVenda,
        formaDePagamento: formaDePagamento
      };

      this.pedidoService.insert(novoPedido).subscribe({
        next: (response) => {
        console.log('Resultado:', response);
        },
        error: (error) => {
          // Este callback é executado quando ocorre um erro durante a emissão do valor
          console.error('Erro ao inserir novo pedido:', error);
          //window.alert(error)
        }
    });

       } else {
         console.error('Endereço Escolhido sem id definido.');
       }
      } else {
        console.error('Cliente Logado sem id definido.');
      }
    }

  }
  