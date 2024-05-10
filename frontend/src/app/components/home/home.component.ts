import { Component } from '@angular/core';
import { SessionTokenService } from '../../services/session-token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(
    private sessionTokenService: SessionTokenService
  ){}


}
