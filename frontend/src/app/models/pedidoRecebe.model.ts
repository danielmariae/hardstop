import { ListaEndereco } from "./endereco.model";
import { ItemDaVenda } from "./itemDaVenda";
import { FormaDePagamento } from "./formaDePagamento";
import { StatusDoPedido } from "./statusDoPedido.model";
import { ItemDaVendaRecebe } from "./itemRecebe";
import { FormaRecebe } from "./formaRecebe.model";

export class PedidoRecebe{
    id!: number | null;
    codigoDeRastreamento!: string | null;
    endereco!: ListaEndereco;
    formaDePagamento!: FormaRecebe;
    itemDaVenda!: ItemDaVendaRecebe[];
    statusDoPedido!: StatusDoPedido[];
}