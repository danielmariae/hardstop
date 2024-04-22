import { ListaEndereco } from "./endereco.model";
import { ListaTelefone } from "./telefone.model";

export class Fornecedor {
    id!: number;
    nomeFantasia!: string;
    cnpj!: string;
    endSite!: string;
    listaEndereco!: ListaEndereco[];
    listaTelefone!: ListaTelefone[];
}