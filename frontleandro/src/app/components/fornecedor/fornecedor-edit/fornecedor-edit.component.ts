import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FornecedorService } from '../../../services/fornecedor.service';
import { Fornecedor } from '../../../models/fornecedor.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';


@Component({
  selector: 'app-fornecedor-edit',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './fornecedor-edit.component.html',
  styleUrls: ['./fornecedor-edit.component.css']
})
export class FornecedorEditComponent implements OnInit {
  fornecedor: Fornecedor;
  fornecedorForm: FormGroup;
  tiposTelefone: any[];
  uf: any[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fornecedorService: FornecedorService,
    private formBuilder: FormBuilder
  ) {
    this.tiposTelefone = [];
    this.uf = [];
    this.fornecedor = new Fornecedor(); // Inicialização no construtor
    this.fornecedorForm = this.formBuilder.group({
      nomeFantasia: [''],
      cnpj: [''],
      endSite: [''],
      telefones: this.formBuilder.array([]),
      enderecos: this.formBuilder.array([]),

    });
    
  }

  ngOnInit(): void {

    this.fornecedorService.getTipoTelefone().subscribe(data => {
      this.tiposTelefone = data;
    });

    this.fornecedorService.getUF().subscribe(data => {
      this.uf = data;
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    console.log(idParam);
    const fornecedorId = idParam ? +idParam : null;
    if (fornecedorId !== null) {
        this.fornecedorService.findById(fornecedorId).subscribe(fornecedor => {
          this.fornecedor = fornecedor;

          // Inserindo valores nos campos de nome, cnpj e endSite
          this.fornecedorForm.patchValue({
            nomeFantasia: fornecedor.nomeFantasia,
            cnpj: fornecedor.cnpj,
            endSite: fornecedor.endSite
          });

          // Populando o FormArray com os dados existentes de fornecedor.listaTelefone
          fornecedor.listaTelefone.forEach(telefone => {
            this.adicionarTelefone(telefone);
          });

          // Populando o FormArray com os dados existentes de fornecedor.listaEndereco
          fornecedor.listaEndereco.forEach(endereco => {
            this.adicionarEndereco(endereco);
          });

          console.log(this.fornecedor);
        });
      } else {
        console.error('O parâmetro "id" não foi fornecido ou é inválido.');
      }
  }

get telefones(): FormArray {
  return this.fornecedorForm.get('telefones') as FormArray;
}

adicionarTelefone(telefone?: any): void {
  const telefoneFormGroup = this.formBuilder.group({
    ddd: [telefone ? telefone.ddd : ''],
    numeroTelefone: [telefone ? telefone.numeroTelefone : ''],
    tipo: [telefone ? telefone.tipo : '']
  });

  this.telefones.push(telefoneFormGroup);
}

removerTelefone(index: number): void {
  this.telefones.removeAt(index);
}

get enderecos(): FormArray {
  return this.fornecedorForm.get('enderecos') as FormArray;
}

adicionarEndereco(endereco?: any): void {
  const enderecoFormGroup = this.formBuilder.group({
    nome: [endereco && endereco.nome ? endereco.nome : ''],
    logradouro: [endereco && endereco.logradouro ? endereco.logradouro : ''],
    numeroLote: [endereco && endereco.numeroLote ? endereco.numeroLote : ''],
    bairro: [endereco && endereco.bairro ? endereco.bairro : ''],
    complemento: [endereco && endereco.complemento ? endereco.complemento : ''],
    cep: this.formBuilder.group({
      prefixo: [endereco && endereco.cep && endereco.cep.prefixo ? endereco.cep.prefixo : ''],
      sufixo: [endereco && endereco.cep && endereco.cep.sufixo ? endereco.cep.sufixo : ''],
      cep: [endereco && endereco.cep && endereco.cep.cep ? endereco.cep.cep : '']
    }),
    localidade: [endereco && endereco.localidade ? endereco.localidade : ''],
    uf: [endereco && endereco.uf ? endereco.uf : ''],
    pais: [endereco && endereco.pais ? endereco.pais : ''],
  });

  this.enderecos.push(enderecoFormGroup);
}

removerEndereco(index: number): void {
  this.enderecos.removeAt(index);
}

  cancelarEdicao(): void {
    // Redireciona o usuário para outra rota
    this.router.navigate(['fornecedores']);
  }


  salvarAlteracoes(): void {

    const idParam = Number(this.route.snapshot.paramMap.get('id'));
    console.log(idParam);
    const novoFornecedor: Fornecedor = {
      id: idParam, // Pode definir como necessário
      nomeFantasia: this.fornecedorForm.value.nomeFantasia,
      cnpj: this.fornecedorForm.value.cnpj,
      endSite: this.fornecedorForm.value.endSite,
      listaTelefone: this.fornecedorForm.value.telefones,
      listaEndereco: this.fornecedorForm.value.enderecos
    };
    
    // Lógica para salvar as alterações do fornecedor
    this.fornecedorService.update(novoFornecedor).subscribe({
      next: (response) => {
        console.log(novoFornecedor);
        this.fornecedorService.notificarFornecedorInserido(); // Notificar outros componentes
      },
      error: (error) => {
       // Este callback é executado quando ocorre um erro durante a emissão do valor
       console.error('Erro:', error);
       // Aqui você pode lidar com o erro de acordo com sua lógica de negócio
       // Por exemplo, exibir uma mensagem de erro para o usuário
       window.alert(error);
      }
    });
  }
}
