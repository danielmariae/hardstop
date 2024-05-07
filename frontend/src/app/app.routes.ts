import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClienteEditComponent } from './components/painel-adm/cliente/cliente-edit/cliente-edit.component';
import { ClienteFormComponent } from './components/painel-adm/cliente/cliente-form/cliente-form.component';
import { ClienteListComponent } from './components/painel-adm/cliente/cliente-list/cliente-list.component';
import { ClienteViewComponent } from './components/painel-adm/cliente/cliente-view/cliente-view.component';
import { FornecedorEditComponent } from './components/painel-adm/fornecedor/fornecedor-edit/fornecedor-edit.component';
import { FornecedorComponent } from './components/painel-adm/fornecedor/fornecedor-form/fornecedor-form.component';
import { FornecedorListComponent } from './components/painel-adm/fornecedor/fornecedor-list/fornecedor-list.component';
import { FornecedorViewComponent } from './components/painel-adm/fornecedor/fornecedor-view/fornecedor-view.component';
import { FuncionarioEditComponent } from './components/painel-adm/funcionario/funcionario-edit/funcionario-edit.component';
import { FuncionarioFormComponent } from './components/painel-adm/funcionario/funcionario-form/funcionario-form.component';
import { FuncionarioListComponent } from './components/painel-adm/funcionario/funcionario-list/funcionario-list.component';
import { FuncionarioViewComponent } from './components/painel-adm/funcionario/funcionario-view/funcionario-view.component';
import { HomeComponent } from './components/home/home.component';
import { LoteFormComponent } from './components/painel-adm/lote/lote-form/lote-form.component';
import { LoteListComponent } from './components/painel-adm/lote/lote-list-idProduto/lote-list-idProduto.component';
import { ProdutoEditComponent } from './components/painel-adm/produto/produto-edit/produto-edit.component';
import { ProdutoFormComponent } from './components/painel-adm/produto/produto-form/produto-form/produto-form.component';
import { ProdutoGarantiaComponent } from './components/painel-adm/produto/produto-garantia/produto-garantia.component';
import { ProdutoListComponent } from './components/painel-adm/produto/produto-list/produto-list.component';
import { AuthResolver } from './components/session-token/resolver/session-token.resolver';
import { SessionTokenComponent } from './components/session-token/session-token.component';
import { ProdutoViewComponent } from './components/painel-adm/produto/produto-view/produto-view/produto-view.component';
import { ProdutoEscolhidoComponent } from './components/painel-adm/produto/produto-escolhido/produto-escolhido.component';


export const routes: Routes = [
    { path:'adm/clientes/new', component: ClienteFormComponent, title:'Cadastrar novo cliente' },
    { path:'adm/clientes/edit/:id', component: ClienteEditComponent, title:'Editar cliente' },
    { path:'adm/clientes', component: ClienteListComponent, title:'Listar clientes'},
    { path:'adm/clientes/:id', component: ClienteViewComponent, title:'Ver cliente'},
    { path:'adm/funcionarios/new', component: FuncionarioFormComponent, title:'Cadastrar novo funcionario' },
    { path:'adm/funcionarios/edit/:id', component: FuncionarioEditComponent, title:'Editar funcionario' },
    { path:'adm/funcionarios', component: FuncionarioListComponent, title:'Listar funcionario'},
    { path:'adm/funcionarios/:id', component: FuncionarioViewComponent, title:'Ver funcionario'},
    { path: 'loginF', component: SessionTokenComponent },
    { path: 'home', component: HomeComponent, resolve: { isAuthenticated: AuthResolver } },
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'adm/fornecedores', component: FornecedorListComponent, title: 'Lista de Fornecedores'},
    { path: 'adm/fornecedores/new', component: FornecedorComponent, title: 'Formulario de Fornecedores'},
    { path: 'adm/fornecedores/edit/:id', component: FornecedorEditComponent, title: 'Edição de Dados de Fornecedores' },
    {path: 'adm/fornecedores/:id', component: FornecedorViewComponent, title: 'Ver fornecedor'},
    {path: 'adm/produtos', component: ProdutoListComponent, title: 'Lista de Produtos'},
    { path: 'adm/produtos/new/:tipoProduto', component: ProdutoFormComponent, title: 'Formulario de Cadastro de Produtos'},
    {path: 'adm/produtos/edit/:id', component: ProdutoEditComponent, title: 'Edicao de Produtos'},
    {path: 'adm/lotes/new', component: LoteFormComponent, title: 'Insercao de Lotes' },
    {path: 'adm/lotes/:id', component: LoteListComponent, title: 'Lista de Lotes por Produto'},
    {path: 'adm/produtos/:id', component: ProdutoViewComponent, title: 'Produto Escolhido'},
    {path: 'adm/produtos/escolhido/:id', component: ProdutoEscolhidoComponent, title: 'Produto Escolhido'},
    {path: 'adm/produtos/garantia/:id', component: ProdutoGarantiaComponent, title: 'Fornecedor de Produto Defeituoso'},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    declarations: [HomeComponent], // Declare o HomeComponent aqui
    exports: [RouterModule]
  })
  export class AppRoutingModule { }
