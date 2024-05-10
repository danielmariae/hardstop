import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { PlacaMae } from '../../../../../models/produto.model';
import { CommonModule } from '@angular/common';
import { ProdutoService } from '../../../../../services/produto.service';

@Component({
  selector: 'app-placaMae-view',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './placa-mae-view.component.html',
  styleUrls: ['./placa-mae-view.component.css']
})
export class PlacaMaeViewComponent {
  errorMessage: string | null = null;
  errorDetails: any | null = null;
  @Input() placaMae: PlacaMae = new PlacaMae();
  placaMaeForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private produtoService: ProdutoService) {
  }

  ngOnInit(): void {
    // Inicializa o formulário do componente de placa mãe
    if(this.placaMae == null) {
    this.placaMaeForm = this.formBuilder.group({
      produto_id: [null],
      cpu: [''],
      chipset: [''],
      memoria: [''],
      bios: [''],
      grafico: [''],
      lan: [''],
      slots: [''],
      armazenamento: [''],
    });
  } else {
    this.placaMaeForm = this.formBuilder.group({
      cpu: [this.placaMae.cpu],
      chipset: [this.placaMae.chipset],
      memoria: [this.placaMae.memoria],
      bios: [this.placaMae.bios],
      grafico: [this.placaMae.grafico],
      lan: [this.placaMae.lan],
      slots: [this.placaMae.slots],
      armazenamento: [this.placaMae.armazenamento]
    });
  }
    // Assinando as mudanças no formulário de placaMae e atualize os valores no serviço compartilhado
    this.placaMaeForm.valueChanges.subscribe(value => {
      this.produtoService.updateProdutoFormValue(value);
    });
  }
}
