-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

insert into cliente (nome, datanascimento, cpf, sexo, login, senha, email) values('Katia', '2010-08-08', '45390821398', 'F', 'katiaflavia', '123VaA_', 'katiaflavia@gmail.com');

insert into telefone (tipoTelefone, ddd, numerotelefone) values(2, '63', '987450019');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(0, '63', '33249087');

insert into endereco (nome, rua, numero, lote, bairro, complemento, cep, municipio, estado, pais) values('Katia', 'Barão de Cotegipe', '15', 's/l', 'Sul', 'Perto da igreja', '64532098', 'Rialma', 'Goiás', 'Brasil');

insert into cliente_endereco (id_cliente, id_endereco) values(1,1);
insert into cliente_telefone (id_cliente, id_telefone) values(1,1);
insert into cliente_telefone (id_cliente, id_telefone) values(1,2);

insert into statusDoPedido (dataHora, status) values('2023-10-01 16:11:26', 0);
insert into statusDoPedido (dataHora, status) values('2023-10-04 20:23:35', 1);
insert into itemDaVenda (preco, quantidade) values(50.3, 3);
insert into itemDaVenda (preco, quantidade) values(20.0, 1);
insert into itemDaVenda (preco, quantidade) values(15.43, 2);
insert into formaDePagamento (nome) values('Marta');
insert into pedido (codigoDeRastreamento, id_formaDePagamento, id_endereco) values('Der34ewww', 1, 1);

insert into cliente_pedido (id_cliente, id_pedido) values(1,1);
insert into pedido_statusDoPedido (id_pedido, id_statusDoPedido) values(1,1);
insert into pedido_statusDoPedido (id_pedido, id_statusDoPedido) values(1,2);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,1);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,2);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,3);