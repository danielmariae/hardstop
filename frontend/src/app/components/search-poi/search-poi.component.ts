import { Component } from '@angular/core';
import { HerePoiService } from '../../services/here-poi.service';
import { CommonModule } from '@angular/common';
import { NgModel } from '@angular/forms';

@Component({
  selector: 'app-search-poi',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './search-poi.component.html',
  styleUrls: ['./search-poi.component.css'],
})
export class SearchPoiComponent {
  public pois: any[] = [];
  public searchCategory: string = '';
  public searchName: string = '';

  constructor(private herePoiService: HerePoiService) {}

  // Método para pesquisar POIs com base em categorias
  searchByCategory(): void {
    const latitude = -23.5505; // Exemplo de coordenadas (São Paulo)
    const longitude = -46.6333;

    this.herePoiService.searchPoiByCategory(latitude, longitude, this.searchCategory)
      .subscribe((response: any) => {
        this.pois = response.items; // Retorna a lista de POIs
        console.log('POIs encontrados:', this.pois);
      });
  }

  // Método para pesquisar POIs com base no nome
  searchByName(): void {
    const latitude = -23.5505;
    const longitude = -46.6333;

    this.herePoiService.searchPoiByName(latitude, longitude, this.searchName)
      .subscribe((response: any) => {
        this.pois = response.items;
        console.log('POIs encontrados:', this.pois);
      });
  }
}
