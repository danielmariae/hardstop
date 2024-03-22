import { Routes } from '@angular/router';
import { FornecedorListComponent } from './components/fornecedor/fornecedor-list/fornecedor-list.component';
// import { fornecedorResolver } from './components/fornecedor/resolver/fornecedor-resolver';

export const routes: Routes = [
    {path: 'fornecedores', component: FornecedorListComponent, title: 'Lista de Fornecedores'},
    //{path: 'fornecedores/edit/:id', component: FornecedorFormComponent, resolve: {fornecedor: fornecedorResolver}},
];
