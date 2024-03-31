import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { PlacaMae } from '../../../../models/Produto.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-placaMae-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './placaMae-form.component.html',
  styleUrls: ['./placaMae-form.component.css']
})
export class PlacaMaeFormComponent {
  @Input() placaMae: PlacaMae = new PlacaMae();
  placaMaeForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.placaMaeForm = this.createPlacaMaeForm();
  }

  private createPlacaMaeForm(): FormGroup {
    return this.formBuilder.group({
      // Campos específicos de PlacaMae
      cpu: [this.placaMae.cpu],
      chipset: [this.placaMae.chipset],
      memoria: [this.placaMae.memoria],
      bios: [this.placaMae.bios],
      grafico: [this.placaMae.grafico],
      lan: [this.placaMae.lan],
      slots: [this.placaMae.slots],
      armazenamento: [this.placaMae.armazenamento],
    });
  }
}
