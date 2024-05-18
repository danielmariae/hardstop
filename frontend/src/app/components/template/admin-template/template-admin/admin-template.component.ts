import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderAdminComponent } from "../header-admin/header-admin.component";

@Component({
    selector: 'app-admin-template',
    standalone: true,
    templateUrl: './admin-template.component.html',
    styleUrl: './admin-template.component.css',
    imports: [RouterOutlet, HeaderAdminComponent]
})
export class AdminTemplateComponent {

}
