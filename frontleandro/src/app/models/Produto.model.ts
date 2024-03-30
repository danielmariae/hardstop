export class Produto {
    id!: number;
    nome!: string;
    modelo!: string;
    marca!: string;
    descricao!: string;
    codigoBarras!: string;
    altura!: number;
    largura!: number;
    comprimento!: number;
    peso!: number;
    valorVenda!: number;
    quantidade!: number;
    nomeImagem!: string[];
}

export class PlacaMae extends Produto {
    produto_id!: number;
    cpu!: string;
    chipset!: string;
    memoria!: string;
    bios!: string;
    grafico!: string;
    lan!: string;
    slots!: string;
    armazenamento!: string;
    garantiaMeses!: string;
}

export class Processador extends Produto {
    produto_id!: number;
    soquete!: string;
    pistas!: string;
    bloqueado!: string;
    compatibilidadeChipset!: string;
    canaisMemoria!: string;
    capacidadeMaxMemoria!: string;
    pontenciaBase!: string;
    potenciaMaxima!: string;
    frequenciaBase!: string;
    frequenciaMaxima!: string;
    tamanhoCacheL3!: string;
    tamanhoCacheL2!: string;
    numNucleosFisicos!: string;
    numThreads!: string;
    velMaxMemoria!: string;
    conteudoEmbalagem!: string;
    garantiaMeses!: string;

}