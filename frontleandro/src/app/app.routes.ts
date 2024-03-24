import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FornecedorListComponent } from './components/fornecedor/fornecedor-list/fornecedor-list.component';
import { SessionTokenComponent } from './components/session-token/session-token.component';
import { HomeComponent } from './components/home/home.component';
import { AuthResolver } from './components/session-token/resolver/session-token.resolver';
// import { fornecedorResolver } from './components/fornecedor/resolver/fornecedor-resolver';

export const routes: Routes = [
    { path: 'loginF', component: SessionTokenComponent },
    { path: 'home', component: HomeComponent, resolve: { isAuthenticated: AuthResolver } },
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'fornecedores', component: FornecedorListComponent, title: 'Lista de Fornecedores'},
    //{path: 'fornecedores/edit/:id', component: FornecedorFormComponent, resolve: {fornecedor: fornecedorResolver}},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    declarations: [HomeComponent], // Declare o HomeComponent aqui
    exports: [RouterModule]
  })
  export class AppRoutingModule { }
