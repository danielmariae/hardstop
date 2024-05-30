import { ListaEndereco } from "./endereco.model";
import { ItemDaVenda } from "./itemDaVenda";
import { FormaDePagamento } from "./formaDePagamento";

export class Pedido{
    id!: number;
    endereco!: ListaEndereco;
    formaDePagamento!: FormaDePagamento;
    itemDaVendaDTO!: ItemDaVenda[];
}