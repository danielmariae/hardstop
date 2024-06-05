import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderAdminComponent } from "../header-admin/header-admin.component";
import { SidebarAdminComponent } from "../sidebar-admin/sidebar-admin.component";
import { FooterAdminComponent } from "../footer-admin/footer-admin.component";
import { SidebarService } from '../../../../services/sidebar.service';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-admin-template',
    standalone: true,
    templateUrl: './admin-template.component.html',
    styleUrl: './admin-template.component.css',
    imports: [RouterOutlet, HeaderAdminComponent, SidebarAdminComponent, FooterAdminComponent, CommonModule]
})
export class AdminTemplateComponent {
    sidebarVisible: boolean = false;

    constructor(private sidebarService: SidebarService){}

    ngOnInit() {
        this.sidebarService.sidebarVisible$.subscribe(visible => {
          this.sidebarVisible = visible;
        });
    }
}
