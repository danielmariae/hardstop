import { CEP } from "cep-promise";

export class Endereco{
    nome!: string;
    logradouro!: string;
    numeroLote!: string;
    bairro!: string;
    complemento!: string;
    cep!: CEP;
    localidade!: string;
    uf!: string;
    pais!: string;
}