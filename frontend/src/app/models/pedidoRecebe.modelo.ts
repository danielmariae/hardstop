import { ListaEndereco } from "./endereco.model";
import { ItemDaVenda } from "./itemDaVenda";
import { FormaDePagamento } from "./formaDePagamento";
import { StatusDoPedido } from "./statusDoPedido.model";

export class PedidoRecebe{
    id!: number | null;
    codigoDeRastreamento!: string | null;
    endereco!: ListaEndereco;
    formaDePagamento!: FormaDePagamento;
    itemDaVenda!: ItemDaVenda[];
    statusDoPedido!: StatusDoPedido[];
}