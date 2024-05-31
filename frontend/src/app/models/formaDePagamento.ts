export class FormaDePagamento {
    id!: number | null;
    modalidade!: number;
    numeroCartao!: string | null;
    mesValidade!: number | null;
    anoValidade!: number | null;
    codSeguranca!: number | null;
    diasVencimento!: number | null;
}