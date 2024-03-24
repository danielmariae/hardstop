import { Endereco } from "./endereco.model";
import { Telefone } from "./telefone.model";

export class Fornecedor {
    id!: number;
    nomeFantasia!: string;
    cnpj!: string;
    endSite!: string;
    endereco!: Endereco[];
    telefone!: Telefone[];
}