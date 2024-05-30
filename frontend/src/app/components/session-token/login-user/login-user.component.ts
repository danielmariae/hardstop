import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NavigationService } from '../../../services/navigation.service';
import { SessionTokenService } from '../../../services/session-token.service';
import { NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login-user',
  standalone: true,
  imports: [NgIf, ReactiveFormsModule, RouterModule],
  templateUrl: './login-user.component.html',
  styleUrl: './login-user.component.css'
})
export class LoginUserComponent {
  errorMessage: string = '';

  loginClienteForm: FormGroup = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
});


  constructor(
    private formBuilder: FormBuilder,
    private sessionTokenService: SessionTokenService,
    private httpClient: HttpClient,
    private navigationService: NavigationService,  
  ) {}
  
  loginC() {

    if (!this.loginClienteForm) {
        return;
    }

    if (this.loginClienteForm.invalid) {
      return;
    }

    const username = this.loginClienteForm.get('username')?.value; // Use o operador de navegação segura (?) para acessar os valores com segurança
    const password = this.loginClienteForm.get('password')?.value; // Use o operador de navegação segura (?) para acessar os valores com segurança

  if (!username || !password) {
    return;
  }

  console.log("TESTE");
  this.sessionTokenService.authenticateUserC(username, password)
  .subscribe({
       next: (response: any) => {
        this.navigationService.navigateTo('/home');
      },
         error: (error) => {
           console.log('Erro:', error);
           console.log(username + ' ' + password);
           this.errorMessage = 'Usuário ou senha inválidos.';
         }
        });

  // this.sessionTokenService.authenticateUserC(username, password)
  // .subscribe({
  //   next: (response: any) => {
  //     const token = response.headers.get('Authorization') ?? '';
  //     if(token) {
  //       console.log('55555'+ ' ' + token);
  //       this.sessionTokenService.saveSessionToken(token);
  //       const usuarioLogado = response.body;
  //       console.log(usuarioLogado);
  //       this.loginClienteForm.reset();
  //       this.errorMessage = '';
  //       // Redirecionar o usuário para a página de perfil, por exemplo
  //       this.navigationService.navigateTo('/home');
  //     }
      
      
  //   },
  //   error: (error) => {
  //     console.log('Erro:', error);
  //     console.log(username + ' ' + password);
  //     this.errorMessage = 'Usuário ou senha inválidos.';
  //   }
  // });
   }

  cadastrarCliente(): void{
    this.navigationService.navigateTo('new/user');
  }
}
