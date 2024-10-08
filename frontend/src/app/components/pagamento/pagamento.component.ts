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
import { NavigationService } from "../../services/navigation.service";
import { getFormattedCurrency } from "../../utils/formatValues";

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
                private carrinhoService: CarrinhoService,
                private navigationService: NavigationService
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
      console.log("INICIANDO PAGAMENTO E FINALIZAÇÃO DO PEDIDO!")
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
      this.itensDaVenda = this.carrinhoItens.map(item => ({
        idProduto: item.id,
        nome: item.nome,
        quantidadeUnidades: item.quantidade,
        quantidadeNaoConvencional: null,
        unidadeDeMedida: null,
        status: null,
        preco: item.preco
      }));
    
      for (let i = 0; i < this.itensDaVenda.length; i++) {
        console.log("ID DO PRODUTO: ", this.itensDaVenda[i].idProduto);
        console.log(this.itensDaVenda[i].nome);
        console.log(this.itensDaVenda[i].quantidadeUnidades);
        console.log(this.itensDaVenda[i].preco);
      }
    }
    
    
    salvarPedido(formaDePagamento: FormaDePagamento): void {
      if (this.clienteLogado && this.clienteLogado.id !== undefined) {
        if (this.enderecoEscolhido && this.enderecoEscolhido.id !== undefined) {
          const novoPedido: Pedido = {
            id: null,
            idCliente: this.clienteLogado.id,
            codigoDeRastreamento: null,
            idEndereco: this.enderecoEscolhido.id,
            statusDoPedido: null,
            itemDaVenda: this.itensDaVenda,
            formaDePagamento: formaDePagamento
          };
    
          // Log do objeto antes de fazer a chamada para a API
          console.log('Objeto Pedido antes de enviar para API:', JSON.stringify(novoPedido));
    
          this.pedidoService.insert(novoPedido).subscribe({
            next: (response) => {
              console.log('Resultado:', response);
              this.carrinhoService.removerTudo();
              this.navigationService.navigateTo('/user/pedidos');
            },
            error: (error) => {
              console.error('Erro ao inserir novo pedido:', error);
            }
          });
    
        } else {
          console.error('Endereço Escolhido sem id definido.');
        }
      } else {
        console.error('Cliente Logado sem id definido.');
      }
    }
    
    calcularTotal(): number {
      let total = 0;
    for (const item of this.carrinhoItens) {
      total += item.quantidade * item.preco;
    }
    return total;
    }
    
    formatValues(valor: number): String {
      return getFormattedCurrency(valor);
      }
  }
  