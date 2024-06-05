import { Component, OnInit } from '@angular/core';
import { HeaderUserComponent } from "./header-user/header-user.component";
import { RouterOutlet } from '@angular/router';
import { SessionTokenService } from '../../../services/session-token.service';
import { SidebarUserComponent } from "./sidebar-user/sidebar-user.component";
import { SidebarService } from '../../../services/sidebar.service';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-user-template',
    standalone: true,
    templateUrl: './user-template.component.html',
    styleUrl: './user-template.component.css',
    imports: [HeaderUserComponent, RouterOutlet, SidebarUserComponent, CommonModule]
})
export class UserTemplateComponent implements OnInit {
    sidebarVisible: boolean = false;

    constructor(private sidebarService: SidebarService){}

    ngOnInit() {
        this.sidebarService.sidebarVisible$.subscribe(visible => {
          this.sidebarVisible = visible;
        });
    }
}
