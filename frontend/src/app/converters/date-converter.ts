export function formatarDataNascimento(dataNascimento: string): string {
    // Verifica se a data de nascimento está no formato ddmmyyyy
    if (dataNascimento && dataNascimento.length === 8) {
      // Extrai o dia, mês e ano da data de nascimento
      const dia = dataNascimento.substring(0, 2);
      const mes = dataNascimento.substring(2, 4);
      const ano = dataNascimento.substring(4, 8);

      // Retorna a data de nascimento formatada
      return `${dia}/${mes}/${ano}`;
    } else {
      return dataNascimento; // Retorna a data de nascimento original se não estiver no formato ddmmyyyy
    }
  }
