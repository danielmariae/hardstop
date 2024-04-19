import { Produto } from "./produto.model";
import { Fornecedor } from "./fornecedor.model";
import { Status } from "./loteStatus.model";

export class LoteEnvia {
    id!: number;
    lote!: string;
    dataHoraChegadaLote!: string | null;
    fornecedor!: Fornecedor;
    produto!: Produto;
    quantidadeUnidades!: number;
    quantidadeNaoConvencional!: number;
    unidadeDeMedida!: string;
    custoCompra!: number;
    valorVenda!: number;
    garantiaMeses!: number;
    status!: Status;
}