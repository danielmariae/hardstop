import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ClienteService } from '../../../services/cliente.service';
import { NavigationService } from '../../../services/navigation.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { SenhaUpdate } from '../../../models/senhaUpdate.model';
import { validarSenhaUpdate } from '../../../validators/update-senha.validator';

@Component({
  selector: 'app-cliente-update-senha',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './cliente-update-senha.component.html',
  styleUrl: './cliente-update-senha.component.css',
})
export class ClienteUpdateSenhaComponent
{
  errorMessage: string | null = null;
  errorDetails: any | null = null;

  senha: SenhaUpdate;
  senhaForm: FormGroup;
  updateIsSucess: boolean;

  constructor(
    private clienteService: ClienteService,
    private navigationService: NavigationService,
    private formBuilder: FormBuilder
  ){
    this.senha = new SenhaUpdate();
    this.senhaForm = formBuilder.group({
      senhaAntiga: [''],
      senhaAtual: this.formBuilder.control('', {
        validators: [
          Validators.required,
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[-[!“#$%&'()*+,-./:;<=>?@[\]^_`{|}~]+).{6,10}$/)
        ],
        nonNullable: true,
      }),
    });
    this.updateIsSucess = false;
  }

  editarDados(): void{
    this.navigationService.navigateTo("user/edit");
  }

  salvarAlteracoes(): void{
    if(this.errorDetails !== null)
      this.errorDetails = null;

    if(this.updateIsSucess === true)
      this.updateIsSucess = false;

    if(this.senhaForm.invalid){
      console.error('Formulário invalido. Por favor, corrigir.')
      // return;
    }

    const novaSenha: SenhaUpdate = {
      senhaAntiga: this.senhaForm.value.senhaAntiga,
      senhaAtual: this.senhaForm.value.senhaAtual
    };

    console.log(novaSenha);

    this.clienteService.updateThisSenha(novaSenha).subscribe({


      next: (response) => {
        console.log("Senha atualizada com sucesso!");
        this.updateIsSucess = true;
      },
      error: (error) => {
        console.error('Erro: ', error);
        this.errorMessage = error.error.errorMessage;
        this.errorDetails = error;
      }
    })
  }
}
