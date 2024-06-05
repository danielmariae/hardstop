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
import { SessionTokenComponent } from './components/session-token/session-token.component';
import { ProdutoViewComponent } from './components/painel-adm/produto/produto-view/produto-view/produto-view.component';
import { ProdutoEscolhidoComponent } from './components/painel-adm/produto/produto-escolhido/produto-escolhido.component';
import { ClienteThisEditComponent } from './components/painel-cliente/cliente-this-edit/cliente-this-edit.component';
import { ClienteUpdateSenhaComponent } from './components/painel-cliente/cliente-update-senha/cliente-update-senha.component';
import { ProdutoHomeListComponent } from './components/home/produto-home-list/produto-home-list.component';
import { UserTemplateComponent } from './components/template/user-template/user-template.component';
import { HomeTemplateComponent } from './components/template/home-template/template-home/home-template.component';
import { AdminTemplateComponent } from './components/template/admin-template/template-admin/admin-template.component';
import { LoginAdmComponent } from './components/session-token/login-adm/login-adm.component';
import { LoginUserComponent } from './components/session-token/login-user/login-user.component';
import { CreateUserAccountComponent } from './components/session-token/create-user-account/create-user-account.component';
import { CarrinhoComponent } from './components/carrinho/carrinho.component';
import { PagamentoComponent } from './components/pagamento/pagamento.component';
import { PedidoUserListComponent } from './components/pedido/pedido-user-list/pedido-user-list.component';
import { FavoritosComponent } from './components/favoritos/favoritos.component';
import { PedidoUserViewComponent } from './components/pedido/pedido-user-view/pedido-user-view.component';
import { PedidoFuncListComponent } from './components/pedido/pedido-func-list/pedido-func-list.component';


export const routes: Routes = [

    {path: 'func/pedidos', component: PedidoFuncListComponent, title: 'Realizar Pagamento'},
    {path: 'carrinho', component: CarrinhoComponent, title: 'Visualizar carrinho'},
    {path: 'pagamento', component: PagamentoComponent, title: 'Realizar Pagamento'},
    {path:'', pathMatch: 'full', redirectTo:'home'},
    {
        path: 'user',
        component: UserTemplateComponent,
        title: 'Painel do cliente',
        children: [
            {path: '', pathMatch: 'full', redirectTo: 'edit'},
            {path: 'edit', component: ClienteThisEditComponent, title: 'Edite seu perfil'},
            {path: 'senha', component: ClienteUpdateSenhaComponent, title: 'Atualizar senha'},
            {
                path: 'pedidos', 
                children: [
                    {path: '', component: PedidoUserListComponent, title: 'Ver pedidos'},
                    {path: ':id', component: PedidoUserViewComponent, title:'Ver um pedido'}
                ]
            },
            {path: 'favoritos', component: FavoritosComponent, title: 'Visualizar favoritos'},        
        ],
    },
    { 
        path: 'home',
        component: HomeTemplateComponent,
        children: [
            {path: '', component: HomeComponent},
            {path: 'buscador/:nome', component: ProdutoHomeListComponent}
        ]
    },
    {
        path: 'adm',
        component: AdminTemplateComponent,
        title: 'Painel de Administrador',
        children:[
            { 
                path:'clientes',
                children:[
                    { path: '', component: ClienteListComponent, title:'Listar clientes'},
                    { path:'new', component: ClienteFormComponent, title:'Cadastrar novo cliente' },
                    { path:'edit/:id', component: ClienteEditComponent, title:'Editar cliente' },
                    { path:':id', component: ClienteViewComponent, title:'Ver cliente'},
                ]
            },
            {
                path:'funcionarios',
                children:[
                    { path:'new', component: FuncionarioFormComponent, title:'Cadastrar novo funcionario' },
                    { path:'edit/:id', component: FuncionarioEditComponent, title:'Editar funcionario' },
                    { path:'', component: FuncionarioListComponent, title:'Listar funcionario'},
                    { path:':id', component: FuncionarioViewComponent, title:'Ver funcionario'},                        
                ]
            },
            {
                path:'fornecedores',
                children:[
                    { path: '', component: FornecedorListComponent, title: 'Lista de Fornecedores'},
                    { path: 'new', component: FornecedorComponent, title: 'Formulario de Fornecedores'},
                    { path: 'edit/:id', component: FornecedorEditComponent, title: 'Edição de Dados de Fornecedores' },
                    { path: ':id', component: FornecedorViewComponent, title: 'Ver fornecedor'},
                
                ]
            },
            {
                path:'produtos',
                children:[
                    {path: '', component: ProdutoListComponent, title: 'Lista de Produtos'},
                    {path: 'new/:tipoProduto', component: ProdutoFormComponent, title: 'Formulario de Cadastro de Produtos'},
                    {path: 'edit/:id', component: ProdutoEditComponent, title: 'Edicao de Produtos'},
                    {path: ':id', component: ProdutoViewComponent, title: 'Produto Escolhido'},
                    {path: 'escolhido/:id', component: ProdutoEscolhidoComponent, title: 'Produto Escolhido'},
                    {path: 'garantia/:id', component: ProdutoGarantiaComponent, title: 'Fornecedor de Produto Defeituoso'},
                
                ]
            },
            {
                path:'lotes',
                children:[
                    {path: 'new', component: LoteFormComponent, title: 'Insercao de Lotes' },
                    {path: 'produto/:id', component: LoteListComponent, title: 'Lista de Lotes por Produto'},                
                ]
            } 

        ]
    },
    { 
        path: 'login', 
        children:[
            {path: '', component: SessionTokenComponent, title: 'Login dos dois'},
            {path: 'adm', component: LoginAdmComponent, title: 'Acesso ao Painel Administrativo'},
            {path: 'user', component: LoginUserComponent, title: 'Bem vindo ao painel de cliente!'},
        ]
    },
    {
        path: 'new',
        children:[
            {path: 'user', component: CreateUserAccountComponent, title: 'Cadastre no melhor site de hardware!'},
            // {path: 'adm', component: }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }
