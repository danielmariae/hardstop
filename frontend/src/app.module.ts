import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app/app-routing/app-routing.module';
import { AppComponent };
import { bootstrapApplication } from '@angular/platform-browser';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    MatButtonModule,
    MatIconModule,
    // ... other imports
    MatToolbarModule // Add MatToolbarModule here (if not already present)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }