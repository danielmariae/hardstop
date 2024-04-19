import { AbstractControl, ValidatorFn, ValidationErrors } from '@angular/forms';
function calcularIdade(dataNascimento: string): number {
  // Remova os caracteres '/' ou '-' da string de data
  const dataLimpa = dataNascimento.replace(/[/\-]/g, '');
  
  // Extraia dia, mês e ano da data limpa
  const diaNascimento = parseInt(dataLimpa.substr(0, 2), 10);
  const mesNascimento = parseInt(dataLimpa.substr(2, 2), 10);
  const anoNascimento = parseInt(dataLimpa.substr(4, 4), 10);

  // Obtenha a data atual
  const hoje = new Date();

  // Calcule a idade
  let idade = hoje.getFullYear() - anoNascimento;
  const mesAtual = hoje.getMonth() + 1;
  const diaAtual = hoje.getDate();
  
  // Verifique se o aniversário deste ano já passou
  if (mesAtual < mesNascimento || (mesAtual === mesNascimento && diaAtual < diaNascimento)) {
    idade--;
  }
 
  return idade;
}

export function idadeValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const idade = calcularIdade(control.value);
    return idade >= 18 ? null : { 'idadeInvalida': { value: control.value } };
  };
}
