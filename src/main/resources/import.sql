-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;



/* CREATE OR REPLACE PROCEDURE atualizar_quantidade_produto(IN produtoid bigint DEFAULT 0, IN quantidadesubtrair integer DEFAULT 0)
LANGUAGE 'plpgsql'
AS $$
BEGIN
    IF (SELECT quantidade - quantidadesubtrair FROM produto WHERE id = produtoid) >= 0 THEN
        UPDATE produto SET quantidade = quantidade - quantidadesubtrair WHERE id = produtoid;
    ELSE
        RAISE EXCEPTION 'A quantidade resultante é negativa';
    END IF;
END;
$$; */

insert into empresa (nomeReal, nomeFantasia, nomeResponsavel, cnpj, cpf, chavePixAleatoria, email, nomeBanco, codigoBanco, numeroConta, numeroAgencia, isEmpresa) values('Leandro Armações Ilimitadas Ltda', 'LAILPALMAS', 'Leandro Guimarães Garcia', '81.478.733/0001-38', '73522652363', 'a87a052f-64f7-4496-9399-8f898c416f28', 'leocid25@gmail.com', 'Banco do Brasil', '001', '124911-8', '1867-8', 'true');

insert into endereco (nome, logradouro, numero, lote, bairro, complemento, cep, localidade, uf, pais) values('Katia de Ruim', 'Barão de Cotegipe', '15', 's/l', 'Sul', 'Perto da igreja Assembléia', '64532098', 'Rialma', 'GO', 'Brasil');

insert into endereco (nome, logradouro, numero, lote, bairro, complemento, cep, localidade, uf, pais) values('Flávio de Boa', 'Palmares da Cruz', '88', 's/l', 'Norte', 'Perto do supermercado Real', '38432422', 'Ponte Alta', 'TO', 'Brasil');

insert into endereco (nome, logradouro, numero, lote, bairro, complemento, cep, localidade, uf, pais) values('.Net.Com', 'Aida Aguiar', 's/n', '18', 'Santa Efigênia', 'Centro', '77532043', 'Sao Paulo', 'SP', 'Brasil');

insert into endereco (nome, logradouro, numero, lote, bairro, complemento, cep, localidade, uf, pais) values('Arruda de Alencar', 'Martins Pontes', 's/n', '55', 'Butantã', 'Sul', '73828382', 'Sao Paulo', 'SP', 'Brasil');

insert into endereco (nome, logradouro, numero, lote, bairro, complemento, cep, localidade, uf, pais) values('Marta de Cesare', 'dos Potiguares', '567', 's/l', 'Anhangabaú', 'Centro', '70987213', 'Sao Paulo', 'SP', 'Brasil');

insert into endereco (nome, logradouro, numero, lote, bairro, complemento, cep, localidade, uf, pais) values('Mãe da Katia de Ruim', 'Sermão de mãe', 's/n', '58', 'Oeste', 'Ao lado da igreja do Véu', '73245787', 'Araraquara', 'SP', 'Brasil');

insert into endereco (nome, logradouro, numero, lote, bairro, cep, localidade, uf, pais) values('Leandro Armações Ilimitadas Ltda', 'rua dos Guaytacazes', '1000', 's/l', 'Santa Efigênia', '77226654', 'Sao Paulo', 'SP', 'Brasil');

-- senha 123VaA_
insert into cliente (nome, datanascimento, cpf, sexo, login, senha, email, perfil) values('Katia de Ruim', '2010-08-08', '23576472720', 'F', 'katiaflavia', 'GP4AIWFnDS2Iekw2MoCX2+/8mpxp6gIHk0WfwgyG4Je02Y0SPaFe5tJ30CiCl032fCnGYexOnR0XT09J2E9R/w==', 'katiaflavia@gmail.com', '0');

-- senha 1a2B3!
insert into cliente (nome, datanascimento, cpf, sexo, login, senha, email, perfil) values('Flavio de Boa', '2007-10-02', '20275068536', 'M', 'flavio1234', 'pTOqkVzjpPAXcO0Iau1rmMLSxHsi/CSmyX7a+k1h9oDcYjh2k2s+GQfTRhK9JECbQFkezUT2unji0ww04h7KDQ==', 'flavio1234@gmail.com', '0');

-- senha bVgH_-2
insert into funcionario (nome, datanascimento, cpf, sexo, login, senha, email, endereco_id, perfil) values('Arruda de Alencar', '2000-09-12', '48202498783', 'M', 'arrudaalencar', '1dFLdP3297AHwCVMOu5ScNEXaWxFW2TO7uj60UQY+PktBgoem0kVhF8b3FqvZDXn2eyOCHQab6aCK0oYGgm/2w==', 'arrudaalencar@gmail.com', 4, '1');

-- senha GfT12-
insert into funcionario (nome, datanascimento, cpf, sexo, login, senha, email, endereco_id, perfil) values('Marta de Cesare', '2001-11-30', '19452476141', 'F', 'martacesare', '5xUVJJKuIYyPjJxOW+bpINIeH2hof2GslHdDXYZDJiG15gET9agGaJ+g1GMr0KgGG0/4yaBDlkp0D3YU7Whgwg==', 'martacesare@gmail.com', 5, '2');


insert into fornecedor (nomeFantasia, cnpj, endSite) values('.Net.Com', '2022/34539382', 'www.netcom.br');

insert into telefone (tipoTelefone, ddd, numerotelefone) values(2, '62', '987450019');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(0, '62', '33249087');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(0, '63', '997843201');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(1, '11', '978530098');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(1, '11', '964531234');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(1, '11', '988885432');

insert into cliente_endereco (id_cliente, id_endereco) values(1,1);
insert into cliente_endereco (id_cliente, id_endereco) values(1,6);
insert into cliente_telefone (id_cliente, id_telefone) values(1,1);
insert into cliente_telefone (id_cliente, id_telefone) values(1,2);

insert into cliente_endereco (id_cliente, id_endereco) values(2,2);
insert into cliente_telefone (id_cliente, id_telefone) values(2,3);

insert into empresa_endereco (id_empresa, id_endereco) values(1,7);

insert into funcionario_telefone (id_funcionario, id_telefone) values(1,4);
insert into funcionario_telefone (id_funcionario, id_telefone) values(2,5);

insert into fornecedor_endereco (id_fornecedor, id_endereco) values(1,3);
insert into fornecedor_telefone (id_fornecedor, id_telefone) values(1,6);

insert into statusDoPedido (dataHora, status) values('2023-10-01 16:11:26', 0);
insert into statusDoPedido (dataHora, status) values('2023-10-04 20:23:35', 1);
insert into classificacao (nome) values('Processadores');

insert into produto (nome, descricao, codigoBarras, marca, altura, largura, comprimento, peso, valorVenda, quantidade, classificacao_id, version) values('Processador Intel Core i7 11 gen', 'Processador Intel Core i7 11 gen', 'PICI711gen07102023', 'Intel', 0.5, 0.5, 0.5, 0.25, 1000.0, 10, 1, 1);
insert into produto (nome, descricao, codigoBarras, marca, altura, largura, comprimento, peso, valorVenda, quantidade, classificacao_id, version) values('Processador Intel Core i5 12 gen', 'Processador Intel Core i5 12 gen', 'PICI512gen07102023', 'Intel', 0.5, 0.5, 0.5, 0.25, 1200.0, 5, 1, 1);
insert into produto (nome, descricao, codigoBarras, marca, altura, largura, comprimento, peso, valorVenda, quantidade, classificacao_id, version) values('Processador AMD Xeon7 11 gen', 'Processador AMD Xeon7 11 gen', 'PAMDX711gen07102023', 'AMD', 0.5, 0.5, 0.5, 0.25, 1000.0, 4, 1, 1);

insert into fornecedor (nomeFantasia, cnpj, endSite) values('.Hardware.Com', '2020/376937201', 'www.hardware.com.br');
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, produto_id, custoCompra, valorVenda, quantidade) values('f1_p1_lt15', 1, '2023-10-01 16:11:26', 1, 500.0, 1000.0, 100);
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, produto_id, custoCompra, valorVenda, quantidade) values('f1_p1_lt15', 1, '2023-10-02 15:14:01', 2, 600.0, 1200.0, 50);
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, produto_id, custoCompra, valorVenda, quantidade) values('f1_p1_lt15', 1, '2023-10-03 09:47:50', 3, 500.0, 1000.0, 20);
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, dataHoraUltimoVendido, produto_id, custoCompra, valorVenda, quantidade) values('f2_p2_lt14', 2, '2022-01-01 16:11:26', '2023-09-30 16:11:26', 1, 700.0, 1400.0, 50);
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, dataHoraUltimoVendido, produto_id, custoCompra, valorVenda, quantidade) values('f2_p2_lt14', 2, '2022-01-01 16:11:26', '2023-10-01 16:11:26', 2, 800.0, 1600.0, 50);

update produto set loteAtual_id = 1 where id = 1;
update produto set loteAtual_id = 2 where id = 2;
update produto set loteAtual_id = 3 where id = 3;

insert into itemDaVenda (preco, quantidade, produto_id) values(900.0, 3, 1);
insert into itemDaVenda (preco, quantidade, produto_id) values(1200.0, 1, 2);
insert into itemDaVenda (preco, quantidade, produto_id) values(1000.0, 2, 3);

insert into formaDePagamento (forma_de_pagamento, modalidade, valorPago) values('CARTAO_DE_CREDITO',0, 5900);
insert into cartaodecredito (id, anovalidade, codseguranca, mesvalidade, datahorapagamento, numerocartao) values(1, 24, 237, 3, '2023-10-01 16:11:26', '345690872106');
insert into pedido (id_formaDePagamento, id_endereco, id_cliente) values(1, 1, 1);


insert into pedido_statusDoPedido (id_pedido, id_statusDoPedido) values(1,1);
insert into pedido_statusDoPedido (id_pedido, id_statusDoPedido) values(1,2);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,1);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,2);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,3);
insert into lista_de_desejos (id_cliente, id_produto) values(2,1);