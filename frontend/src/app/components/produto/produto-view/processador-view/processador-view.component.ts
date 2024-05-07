import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Processador } from '../../../../models/produto.model';
import { CommonModule } from '@angular/common';
import { ProdutoService } from '../../../../services/produto.service';

@Component({
  selector: 'app-processador-view',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './processador-view.component.html',
  styleUrls: ['./processador-view.component.css']
})
export class ProcessadorViewComponent implements OnInit {
  @Input() processador: Processador = new Processador();
  processadorForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private produtoService: ProdutoService) {
  }

  ngOnInit(): void {
    // Inicializa o formulário do componente de processador
    if (this.processador === null) {
      console.log("TESTE");
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
    } else {
      this.processadorForm = this.formBuilder.group({
        soquete: [this.processador.soquete],
        pistas: [this.processador.pistas],
        bloqueado: [this.processador.bloqueado],
        compatibilidadeChipset: [this.processador.compatibilidadeChipset],
        canaisMemoria: [this.processador.canaisMemoria],
        capacidadeMaxMemoria: [this.processador.capacidadeMaxMemoria],
        pontenciaBase: [this.processador.pontenciaBase],
        potenciaMaxima: [this.processador.potenciaMaxima],
        frequenciaBase: [this.processador.frequenciaBase],
        frequenciaMaxima: [this.processador.frequenciaMaxima],
        tamanhoCacheL3: [this.processador.tamanhoCacheL3],
        tamanhoCacheL2: [this.processador.tamanhoCacheL2],
        numNucleosFisicos: [this.processador.numNucleosFisicos],
        numThreads: [this.processador.numThreads],
        velMaxMemoria: [this.processador.velMaxMemoria],
        conteudoEmbalagem: [this.processador.conteudoEmbalagem],
      });
    }
    // Assine as mudanças no formulário de processador e atualize os valores no serviço compartilhado
    this.processadorForm.valueChanges.subscribe(value => {
      this.produtoService.updateProdutoFormValue(value);
    });
  }



}
