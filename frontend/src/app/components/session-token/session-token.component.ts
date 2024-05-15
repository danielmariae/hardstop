import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { SessionTokenService } from '../../services/session-token.service';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NgIf } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSelectModule } from '@angular/material/select';
import { HttpClient } from '@angular/common/http';
import { NavigationService } from '../../services/navigation.service';


@Component({
  selector: 'app-session-token',
  standalone: true,
  imports: [NgIf, ReactiveFormsModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatCardModule, MatToolbarModule, RouterModule, MatSelectModule],

  templateUrl: './session-token.component.html',
  styleUrls: ['./session-token.component.css']
})
export class SessionTokenComponent implements OnInit {
    loginClienteForm: FormGroup = this.formBuilder.group({
        username: ['', Validators.required],
        password: ['', Validators.required]
    });
    loginForm: FormGroup = this.formBuilder.group({
        username: ['', Validators.required],
        password: ['', Validators.required]
    });
  errorMessage: string = '';

  constructor(
            private formBuilder: FormBuilder,
            private sessionTokenService: SessionTokenService,
            private httpClient: HttpClient,
            private navigationService: NavigationService,  
          ) {}

  ngOnInit(): void {
  }

  loginF() {

    if (!this.loginForm) {
        return;
    }

    if (this.loginForm.invalid) {
      return;
    }

    const username = this.loginForm.get('username')?.value; // Use o operador de navegação segura (?) para acessar os valores com segurança
    const password = this.loginForm.get('password')?.value; // Use o operador de navegação segura (?) para acessar os valores com segurança

  if (!username || !password) {
    return;
  }

  this.sessionTokenService.authenticateUserF(username, password)
  .subscribe({
    next: (response) => {
      const token = response.token;
      //console.log('12334'+ ' ' + token);
      this.sessionTokenService.saveSessionToken(token);
      this.loginForm.reset();
      this.errorMessage = '';
      this.navigationService.navigateTo('/adm/clientes');
    },
    error: (error) => {
      console.log('Erro:', error);
      console.log(username + ' ' + password);
      this.errorMessage = 'Usuário ou senha inválidos.';
    }
  });
  }

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

  this.sessionTokenService.authenticateUserC(username, password)
  .subscribe({
    next: (response) => {
      const token = response.token;
      //console.log('55555'+ ' ' + token);
      this.sessionTokenService.saveSessionToken(token);
      this.loginForm.reset();
      this.errorMessage = '';
      // Redirecionar o usuário para a página de perfil, por exemplo
      this.navigationService.navigateTo('/cliente/edit');
      
    },
    error: (error) => {
      console.log('Erro:', error);
      console.log(username + ' ' + password);
      this.errorMessage = 'Usuário ou senha inválidos.';
    }
  });

  }
}













// import { Component } from '@angular/core';
// import { SessionTokenService } from '../../services/session-token.service';

// @Component({
//   selector: 'app-session-token',
//   templateUrl: './session-token.component.html',
//   styleUrls: ['./session-token.component.css']
// })
// export class SessionTokenComponent {
//   username: string = '';
//   password: string = '';
//   errorMessage: string = '';

//   constructor(private sessionTokenService: SessionTokenService) { }

//   login() {
//     this.sessionTokenService.authenticateUser(this.username, this.password)
//       .subscribe(
//         (token) => {
//           Se a autenticação for bem-sucedida, salve o token de sessão
//           this.sessionTokenService.saveSessionToken(token);
//           Limpe os campos de entrada e mensagens de erro
//           this.username = '';
//           this.password = '';
//           this.errorMessage = '';
//           Redirecione o usuário para a página de perfil, por exemplo
//           this.router.navigate(['/profile']);
//         },
//         (error) => {
//           Se ocorrer um erro durante a autenticação, exiba uma mensagem de erro
//           this.errorMessage = 'Usuário ou senha inválidos.';
//         }
//       );
//   }
// }
