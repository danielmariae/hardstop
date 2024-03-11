import { Endereco } from "./endereco.model";
import { Perfil } from "./perfil.model";

export class Cliente{
    id!: number;
    nome!: string;
    dataNascimento!: string;
    cpf!: string;
    sexo!: string;
    login!: string;
    senha!: string;
    email!: string;
    perfil!: Perfil; 
    endereco!: Endereco;
}