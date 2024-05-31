import { ListaEndereco } from "./endereco.model";
import { ItemDaVenda } from "./itemDaVenda";
import { FormaDePagamento } from "./formaDePagamento";
import { StatusDoPedido } from "./statusDoPedido.model";

export class Pedido{
    id!: number | null;
    idCliente!: number | null;
    codigoDeRastreamento!: string | null;
    idEndereco!: number;
    formaDePagamento!: FormaDePagamento;
    itemDaVenda!: ItemDaVenda[];
    statusDoPedido!: StatusDoPedido | null;
}