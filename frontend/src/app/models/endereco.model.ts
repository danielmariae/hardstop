type UnidadeFederativa = {}


export class ListaEndereco{
    id!: number;
    nome!: string;
    logradouro!: string;
    numeroLote!: string;
    bairro!: string;
    complemento!: string;
    cep!: string;
    localidade!: string;
    uf!: UnidadeFederativa;
    pais!: string;
    erro!: boolean;
}