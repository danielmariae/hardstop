// import { CEP } from "cep-promise";

type cep = {

}
export class Endereco{
    nome!: string;
    logradouro!: string;
    numeroLote!: string;
    bairro!: string;
    complemento!: string;
    // cep!: CEP;
    cep!: cep;
    localidade!: string;
    uf!: string;
    pais!: string;
}