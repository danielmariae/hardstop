import { Component } from '@angular/core';
import { HeaderUserComponent } from "./header-user/header-user.component";
import { RouterOutlet } from '@angular/router';
import { SessionTokenService } from '../../../services/session-token.service';

@Component({
    selector: 'app-user-template',
    standalone: true,
    templateUrl: './user-template.component.html',
    styleUrl: './user-template.component.css',
    imports: [HeaderUserComponent, RouterOutlet]
})
export class UserTemplateComponent {

}
