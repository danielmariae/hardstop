import { NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SessionTokenService } from '../../../services/session-token.service';
import { NavigationService } from '../../../services/navigation.service';

@Component({
  selector: 'app-login-adm',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, RouterModule],
  templateUrl: './login-adm.component.html',
  styleUrl: './login-adm.component.css'
})
export class LoginAdmComponent implements OnInit {
  errorMessage: string = '';
  loginForm: FormGroup = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });
  
  constructor(
    private formBuilder: FormBuilder,
    private sessionTokenService: SessionTokenService,
    private navigationService: NavigationService
  ){}

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
    next: (response: any) => {
     this.navigationService.navigateTo('/adm/clientes');
   },
      error: (error) => {
        console.log('Erro:', error);
        console.log(username + ' ' + password);
        this.errorMessage = 'Usuário ou senha inválidos.';
      }
     });
  }
}
