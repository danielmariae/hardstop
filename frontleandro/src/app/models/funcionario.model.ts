import { ListaEndereco } from "./endereco.model";
import { ListaTelefone } from "./telefone.model";

export class Funcionario {
    id!: number;
    nome!: string;
    dataNascimento!: string;
    cpf!: string;
    sexo!: string;
    login!: string;
    senha!: string;
    email!: string;
    listaEndereco!: ListaEndereco;
    listaTelefone!: ListaTelefone[];
    perfil!: number;
}
