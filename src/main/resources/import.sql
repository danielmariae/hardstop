-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

insert into cliente (nome, datanascimento, cpf, sexo, login, senha, email) values('Katia', '2010-08-08', '45390821398', 'F', 'katiaflavia', '123456', 'katiaflavia@gmail.com');

insert into telefone (tipoTelefone, ddd, numerotelefone) values(2, '63', '987450019');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(0, '63', '33249087');

insert into endereco (nome, rua, numero, lote, bairro, complemento, cep, municipio, estado, pais) values('Katia', 'Barão de Cotegipe', '15', 's/l', 'Sul', 'Perto da igreja', '64532098', 'Rialma', 'Goiás', 'Brasil');

insert into cliente_endereco (id_cliente, id_endereco) values(1,1);
insert into cliente_telefone (id_cliente, id_telefone) values(1,1);
insert into cliente_telefone (id_cliente, id_telefone) values(1,2);