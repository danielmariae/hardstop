import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Processador } from '../../../../models/Produto.model';
import { CommonModule } from '@angular/common';
import { ProcessadorFormService } from './processador-form-service.component';

@Component({
  selector: 'app-processador-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './processador-form.component.html',
  styleUrls: ['./processador-form.component.css']
})
export class ProcessadorFormComponent  implements OnInit {
  @Input() processador: Processador = new Processador();
  processadorForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private processadorFormService: ProcessadorFormService) {
    // this.processadorForm = this.createProcessadorForm();
  }

  // private createProcessadorForm(): FormGroup {
  //   return this.formBuilder.group({
  //     // Campos específicos de Processador
  //     soquete: [this.processador.soquete],
  //     pistas: [this.processador.pistas],
  //     bloqueado: [this.processador.bloqueado],
  //     compatibilidadeChipset: [this.processador.compatibilidadeChipset],
  //     canaisMemoria: [this.processador.canaisMemoria],
  //     capacidadeMaxMemoria: [this.processador.capacidadeMaxMemoria],
  //     pontenciaBase: [this.processador.pontenciaBase],
  //     potenciaMaxima: [this.processador.potenciaMaxima],
  //     frequenciaBase: [this.processador.frequenciaBase],
  //     frequenciaMaxima: [this.processador.frequenciaMaxima],
  //     tamanhoCacheL3: [this.processador.tamanhoCacheL3],
  //     tamanhoCacheL2: [this.processador.tamanhoCacheL2],
  //     numNucleosFisicos: [this.processador.numNucleosFisicos],
  //     numThreads: [this.processador.numThreads],
  //     velMaxMemoria: [this.processador.velMaxMemoria],
  //     conteudoEmbalagem: [this.processador.conteudoEmbalagem],
  //   });
  // }

  ngOnInit(): void {
    // Inicializa o formulário do componente de processador
    this.processadorForm = this.formBuilder.group({
      produto_id: [null],
      soquete: [''],
      pistas: [''],
      bloqueado: [''],
      compatibilidadeChipset: [''],
      canaisMemoria: [''],
      capacidadeMaxMemoria: [''],
      pontenciaBase: [''],
      potenciaMaxima: [''],
      frequenciaBase: [''],
      frequenciaMaxima: [''],
      tamanhoCacheL3: [''],
      tamanhoCacheL2: [''],
      numNucleosFisicos: [''],
      numThreads: [''],
      velMaxMemoria: [''],
      conteudoEmbalagem: ['']
    });

    // Assine as mudanças no formulário de processador e atualize os valores no serviço compartilhado
    this.processadorForm.valueChanges.subscribe(value => {
      this.processadorFormService.updateProcessadorFormValue(value);
    });
  }



}
