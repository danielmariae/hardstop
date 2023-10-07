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
insert into classificacao (nome) values('Processadores');

insert into produto (nome, descricao, codigoBarras, marca, altura, largura, comprimento, peso, custoCompra, valorVenda, quantidade, classificacao_id) values('Processador Intel Core i7 11 gen', 'Processador Intel Core i7 11 gen', 'PICI711gen07102023', 'Intel', 0.5, 0.5, 0.5, 0.25, 500.0, 1000.0, 10, 1);

insert into produto (nome, descricao, codigoBarras, marca, altura, largura, comprimento, peso, custoCompra, valorVenda, quantidade, classificacao_id) values('Processador Intel Core i5 12 gen', 'Processador Intel Core i5 12 gen', 'PICI512gen07102023', 'Intel', 0.5, 0.5, 0.5, 0.25, 600.0, 1200.0, 5, 1);

insert into produto (nome, descricao, codigoBarras, marca, altura, largura, comprimento, peso, custoCompra, valorVenda, quantidade, classificacao_id) values('Processador AMD Xeon7 11 gen', 'Processador AMD Xeon7 11 gen', 'PAMDX711gen07102023', 'AMD', 0.5, 0.5, 0.5, 0.25, 500.0, 1000.0, 4, 1);


insert into fornecedor (nomeFantasia, cnpj, endSite) values('.Net.Com', '2022/34539382', 'www.netcom.br');
insert into lote (lote, fornecedor_id) values('f1_p1_lt15', 1);



insert into itemDaVenda (preco, quantidade, produto_id) values(900.0, 3, 1);
insert into itemDaVenda (preco, quantidade, produto_id) values(1200.0, 1, 2);
insert into itemDaVenda (preco, quantidade, produto_id) values(1000.0, 2, 3);
insert into formaDePagamento (nome) values('Cartão de Crédito');
insert into pedido (codigoDeRastreamento, id_formaDePagamento, id_endereco) values('Der34ewww', 1, 1);

insert into cliente_pedido (id_cliente, id_pedido) values(1,1);
insert into produto_lote (id_produto, id_lote) values(1,1);
insert into pedido_statusDoPedido (id_pedido, id_statusDoPedido) values(1,1);
insert into pedido_statusDoPedido (id_pedido, id_statusDoPedido) values(1,2);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,1);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,2);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,3);