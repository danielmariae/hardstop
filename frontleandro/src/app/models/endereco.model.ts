import { CEP } from "./cep.model";


type UnidadeFederativa = {}
export class ListaEndereco{
    id!: number;
    nome!: string;
    logradouro!: string;
    numeroLote!: string;
    bairro!: string;
    complemento!: string;
    cep!: CEP;
    localidade!: string;
    uf!: UnidadeFederativa;
    pais!: string;
}