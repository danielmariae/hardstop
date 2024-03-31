// processador-form.service.ts
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class ProcessadorFormService {
  private processadorFormValue: any = {}; // Armazenar os valores do formulário do componente de processador

  constructor() {}

  // Atualiza os valores do formulário do componente de processador
  updateProcessadorFormValue(formValue: any) {
    this.processadorFormValue = formValue;
  }

  // Obtém os valores do formulário do componente de processador
  getProcessadorFormValue() {
    return this.processadorFormValue;
  }
}
