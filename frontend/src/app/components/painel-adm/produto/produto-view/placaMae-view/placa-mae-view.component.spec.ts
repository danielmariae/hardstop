import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlacaMaeViewComponent } from './placa-mae-view.component';

describe('PlacaMaeViewComponent', () => {
  let component: PlacaMaeViewComponent;
  let fixture: ComponentFixture<PlacaMaeViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlacaMaeViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PlacaMaeViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
