import { Component, OnInit, signal } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { HeaderHomeComponent } from "../header-home/header-home.component";
import { CommonModule, NgFor } from '@angular/common';
import { FooterHomeComponent } from "../footer-home/footer-home.component";
import { Consulta } from '../../../../models/consulta.model';
import { MatCard, MatCardActions, MatCardContent, MatCardFooter, MatCardTitle } from '@angular/material/card';
import { MatButton } from '@angular/material/button';


@Component({
    selector: 'app-home-template',
    standalone: true,
    imports: [RouterOutlet, HeaderHomeComponent, FooterHomeComponent, CommonModule],
    templateUrl: './home-template.component.html',
    styleUrl: './home-template.component.css',
})
export class HomeTemplateComponent {

}

