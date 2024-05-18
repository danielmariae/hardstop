import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { HeaderHomeComponent } from "../header-home/header-home.component";
import { CommonModule } from '@angular/common';
import { FooterHomeComponent } from "../footer-home/footer-home.component";

@Component({
    selector: 'app-home-template',
    standalone: true,
    templateUrl: './home-template.component.html',
    styleUrl: './home-template.component.css',
    imports: [RouterOutlet, HeaderHomeComponent, FooterHomeComponent, CommonModule]
})
export class HomeTemplateComponent {}

