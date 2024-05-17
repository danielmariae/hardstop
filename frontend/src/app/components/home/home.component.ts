import { Component } from '@angular/core';
import { SessionTokenService } from '../../services/session-token.service';
import { FormBuilder, FormControl } from '@angular/forms';
import { NavigationService } from '../../services/navigation.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  buscadorForm: FormControl;

  constructor(
    private sessionTokenService: SessionTokenService,
    private formBuilder: FormBuilder,
    private navigationService: NavigationService
  ){
    this.buscadorForm = formBuilder.control('');
  }

  buscarProduto(): void{
    if(this.buscadorForm.value !== null){
      console.log("Buscando por: ", this.buscadorForm.value);
      this.navigationService.navigateTo('home/buscador/'+this.buscadorForm.value);  
    }else{
      this.navigationService.navigateTo('home/buscador/%');
    }
  }
}
