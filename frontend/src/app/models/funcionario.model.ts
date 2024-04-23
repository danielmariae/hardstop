import { ListaEndereco } from "./endereco.model";
import { ListaTelefone } from "./telefone.model";

export class Funcionario {
    id!: number;
    nome!: string;
    dataNascimento!: string;
    cpf!: string;
    sexo!: string;
    idperfil!: number;
    email!: string;
    login!: string;
    senha!: string;
    listaEndereco!: ListaEndereco;
    listaTelefone!: ListaTelefone[];
}
