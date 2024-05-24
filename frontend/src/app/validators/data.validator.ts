import { AbstractControl, ValidatorFn, ValidationErrors } from '@angular/forms';

export function dataValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const data = control.value;
    if (data.length !== 8) {
      return { 'dataInvalida': { value: control.value } };
    }

    const dia = parseInt(data.substr(0, 2), 10);
    const mes = parseInt(data.substr(2, 2), 10) - 1; // Subtrai 1 aqui, pois os meses no JavaScript começam em 0
    const ano = parseInt(data.substr(4, 4), 10);

    const dataObj = new Date(ano, mes, dia);

    // Verifica se os valores de dia, mês e ano correspondem ao objeto Date criado
    // Isso é necessário porque o objeto Date ainda será criado mesmo com valores inválidos, como o dia 30 de fevereiro
    if (dataObj.getDate() !== dia || dataObj.getMonth() !== mes || dataObj.getFullYear() !== ano) {
      return { 'dataInvalida': { value: control.value } };
    }

    return null;
  };
}
