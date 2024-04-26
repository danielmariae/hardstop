import { Produto } from "./produto.model";
import { Fornecedor } from "./fornecedor.model";
import { Status } from "./loteStatus.model";

export class LoteRecebeClass {
    id!: number;
    lote!: string;
    dataHoraChegadaLote!: string;
    dataHoraAtivacaoLote!: string;
    dataHoraUltimoVendido!: string;
    fornecedor!: Fornecedor;
    produto!: Produto;
    quantidadeUnidades!: number;
    quantidadeNaoConvencional!: number;
    unidadeDeMedida!: string;
    custoCompra!: number;
    valorVenda!: number;
    garantiaMeses!: number;
    statusDoLote!: Status;
}