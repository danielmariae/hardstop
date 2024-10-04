import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPoiComponent } from './search-poi.component';

describe('SearchPoiComponent', () => {
  let component: SearchPoiComponent;
  let fixture: ComponentFixture<SearchPoiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchPoiComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SearchPoiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
