import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClienteEditComponent } from './components/cliente/cliente-edit/cliente-edit.component';
import { ClienteFormComponent } from './components/cliente/cliente-form/cliente-form.component';
import { ClienteListComponent } from './components/cliente/cliente-list/cliente-list.component';
import { ClienteViewComponent } from './components/cliente/cliente-view/cliente-view.component';
import { FornecedorEditComponent } from './components/fornecedor/fornecedor-edit/fornecedor-edit.component';
import { FornecedorEscolhidoComponent } from './components/fornecedor/fornecedor-escolhido/fornecedor-escolhido.component';
import { FornecedorComponent } from './components/fornecedor/fornecedor-form/fornecedor-form.component';
import { FornecedorListComponent } from './components/fornecedor/fornecedor-list/fornecedor-list.component';
import { FuncionarioListComponent } from './components/funcionario/funcionario-list/funcionario-list.component';
import { HomeComponent } from './components/home/home.component';
import { ProdutoEditComponent } from './components/produto/produto-edit/produto-edit.component';
import { ProdutoFormComponent } from './components/produto/produto-form/produto-form/produto-form.component';
import { ProdutoListComponent } from './components/produto/produto-list/produto-list.component';
import { AuthResolver } from './components/session-token/resolver/session-token.resolver';
import { SessionTokenComponent } from './components/session-token/session-token.component';
import { FuncionarioEditComponent } from './components/funcionario/funcionario-edit/funcionario-edit.component';
import { FuncionarioFormComponent } from './components/funcionario/funcionario-form/funcionario-form.component';
import { FuncionarioViewComponent } from './components/funcionario/funcionario-view/funcionario-view.component';
import { LoteFormComponent } from './components/lote/lote-form/lote-form.component';
import { LoteListComponent } from './components/lote/lote-list-idProduto/lote-list-idProduto.component';
import { ProdutoEscolhidoComponent } from './components/produto/produto-escolhido/produto-escolhido.component';
import { ProdutoGarantiaComponent } from './components/produto/produto-garantia/produto-garantia.component';


export const routes: Routes = [
    { path:'clientes/new', component: ClienteFormComponent, title:'Cadastrar novo cliente' },
    { path:'clientes/edit/:id', component: ClienteEditComponent, title:'Editar cliente' },
    { path:'clientes', component: ClienteListComponent, title:'Listar clientes'},
    { path:'clientes/:id', component: ClienteViewComponent, title:'Ver cliente'},
    { path:'funcionarios/new', component: FuncionarioFormComponent, title:'Cadastrar novo funcionario' },
    { path:'funcionarios/edit/:id', component: FuncionarioEditComponent, title:'Editar funcionario' },
    { path:'funcionarios', component: FuncionarioListComponent, title:'Listar funcionario'},
    { path:'funcionarios/:id', component: FuncionarioViewComponent, title:'Ver funcionario'},
    { path: 'loginF', component: SessionTokenComponent },
    { path: 'home', component: HomeComponent, resolve: { isAuthenticated: AuthResolver } },
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'fornecedores', component: FornecedorListComponent, title: 'Lista de Fornecedores'},
    { path: 'fornecedores/new', component: FornecedorComponent, title: 'Formulario de Fornecedores'},
    { path: 'fornecedores/edit/:id', component: FornecedorEditComponent, title: 'Edição de Dados de Fornecedores' },
    {path: 'fornecedores/:id', component: FornecedorEscolhidoComponent, title: 'Fornecedor Escolhido'},
    {path: 'produtos', component: ProdutoListComponent, title: 'Lista de Produtos'},
    { path: 'produtos/new/:tipoProduto', component: ProdutoFormComponent, title: 'Formulario de Cadastro de Produtos'},
    {path: 'produtos/edit/:id', component: ProdutoEditComponent, title: 'Edicao de Produtos'},
    {path: 'lotes/new', component: LoteFormComponent, title: 'Insercao de Lotes' },
    {path: 'lotes/:id', component: LoteListComponent, title: 'Lista de Lotes por Produto'},
    {path: 'produtos/:id', component: ProdutoEscolhidoComponent, title: 'Produto Escolhido'},
    {path: 'produtos/garantia/:id', component: ProdutoGarantiaComponent, title: 'Fornecedor de Produto Defeituoso'},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    declarations: [HomeComponent], // Declare o HomeComponent aqui
    exports: [RouterModule]
  })
  export class AppRoutingModule { }
