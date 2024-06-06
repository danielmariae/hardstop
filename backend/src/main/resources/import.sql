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

insert into endereco (nome, logradouro, numerolote, bairro, complemento, cep, localidade, uf, pais) values('Katia de Ruim', 'Barão de Cotegipe', '15', 'Sul', 'Perto da igreja Assembléia', '64532-098', 'Rialma', 'GO', 'Brasil');
insert into endereco (nome, logradouro, numerolote, bairro, complemento, cep, localidade, uf, pais) values('Flávio de Boa', 'Palmares da Cruz', '88', 'Norte', 'Perto do supermercado Real', '38432-422', 'Ponte Alta', 'TO', 'Brasil');
insert into endereco (nome, logradouro, numerolote, bairro, complemento, cep, localidade, uf, pais) values('.Net.Com', 'Aida Aguiar',  '18', 'Santa Efigênia', 'Proximo Açai', '77532-043', 'Sao Paulo', 'SP', 'Brasil');
insert into endereco (nome, logradouro, numerolote, bairro, complemento, cep, localidade, uf, pais) values('Arruda de Alencar', 'Martins Pontes', '55', 'Butantã', 'Sul', '73828-382', 'Sao Paulo', 'SP', 'Brasil');
insert into endereco (nome, logradouro, numerolote, bairro, complemento, cep, localidade, uf, pais) values('Marta de Cesare', 'dos Potiguares', '567', 'Anhangabaú', 'Centro', '70987-213', 'Sao Paulo', 'SP', 'Brasil');
insert into endereco (nome, logradouro, numerolote, bairro, complemento, cep, localidade, uf, pais) values('Mãe da Katia de Ruim', 'Sermão de mãe', '58', 'Oeste', 'Ao lado da igreja do Véu', '73245-787', 'Araraquara', 'SP', 'Brasil');
insert into endereco (nome, logradouro, numerolote, bairro, cep, localidade, uf, pais) values('Leandro Armações Ilimitadas Ltda', 'rua dos Guaytacazes', '1000', 'Santa Efigênia', '77226-654', 'Sao Paulo', 'SP', 'Brasil');
insert into endereco (nome, logradouro, numerolote, bairro, complemento, cep, localidade, uf, pais) values('.Hardware.Com', 'Gal Costa',  '374', 'das Acacias', 's/c', '76093-120', 'Brasilia', 'DF', 'Brasil');

-- ## Clientes ##
-- senha 123VaA_
insert into cliente (nome, datanascimento, perfil, cpf, sexo, login, senha, email) values('Katia de Ruim', '2010-08-08', 0, '23576472720', 'F', 'katiaflavia', 'GP4AIWFnDS2Iekw2MoCX2+/8mpxp6gIHk0WfwgyG4Je02Y0SPaFe5tJ30CiCl032fCnGYexOnR0XT09J2E9R/w==', 'katiaflavia@gmail.com');
-- senha 1a2B3!
insert into cliente (nome, datanascimento, perfil, cpf, sexo, login, senha, email) values('Flavio de Boa', '2007-10-02', 0, '20275068536', 'M', 'flavio1234', 'pTOqkVzjpPAXcO0Iau1rmMLSxHsi/CSmyX7a+k1h9oDcYjh2k2s+GQfTRhK9JECbQFkezUT2unji0ww04h7KDQ==', 'flavio1234@gmail.com');
-- senha 123_@!
insert into cliente (nome, datanascimento, perfil, cpf, sexo, login, senha, email) values('Pedro Aires Damasceno', '1990-01-02', 0, '82139301201', 'M', 'pedroaires', 'SYb6CZac0GdFVBKIammxf0u/RY6UTWQD+KowVTMbFeZv0R+4AAnJVbrA75h/V5YgJHCxlOyBEiPae5OM+srDvQ==', 'pedroaires@gmail.com');
-- senha @Joao123
insert into cliente (nome, datanascimento, perfil, cpf, sexo, login, senha, email) values('João da Costa e Silva', '1991-10-03', 0, '77829300192', 'M', 'joaosilva', 'Xz+ooO8FcCXmnots7ZIFRRG1wPv9Z+IsFfU6CO/9WhpKHPyhl7b5X2uSQWbslorxNvyS1E4dQ/8kLkuCI5T9mg==', 'joaosilva@gmail.com');
-- senha Mari4@!
insert into cliente (nome, datanascimento, perfil, cpf, sexo, login, senha, email) values('Maria Teresa dos Andes Cunha', '1996-07-03', 0, '92388200782', 'F', 'mariateresa', '/0e7YJzsGmFPHs2xx0ePcwGrhElEhQuVfWX/ZrR4bVzu5gg34KL52ACNOIDkvHHirzvKU/qrrPr/GlvAUFrIJw==', 'mariateresa@gmail.com');
-- senha V1rgil!o
insert into cliente (nome, datanascimento, perfil, cpf, sexo, login, senha, email) values('Virgílio Dantas Caires', '1995-11-24', 0, '15782390201', 'M', 'virgiliodantas', 'zStMuq3/Jv+kaNweRwlkmyn4WIGpygop3+DR8YfLSpWuPsaDtxjUSAZboweiBXHkpKRIVLLPVR39vZDsoOeUYA==', 'virgiliodantas@gmail.com');
-- senha Du!c3_
insert into cliente (nome, datanascimento, perfil, cpf, sexo, login, senha, email) values('Dulce dos Reis Feitosa', '1989-05-21', 0, '92138210910', 'F', 'dulcefeitosa', 'IPLokYYVX1KPSIuIx/9qlzSrOJlNn7Yd0CBqr8Hr/Mdp0G65sd5cogxRC6zNLJXWyIaYPYscrnS47odc8pOq1g==', 'dulcefeitosa@gmail.com');
-- senha Cam4n7_
insert into cliente (nome, datanascimento, perfil, cpf, sexo, login, senha, email) values('Felipe Aires Camantelli', '2001-07-13', 0, '18238390145', 'M', 'felipecamantelli', '+f9i/RwxjTdjGPGQskQjp5VV4rkSKJ00qFooyjN2c09BcppRoHXzvYoWlR9gGkPKBLNku12bVpIy1WqlhMhj4w==', 'felipecamantelli@gmail.com');

-- ## Funcionarios ##
-- senha bVgH_-2
insert into funcionario (nome, datanascimento, cpf, sexo, endereco_id, login, senha, perfil, email) values('Arruda de Alencar', '2000-09-12', '48202498783', 'M', 4, 'arrudaalencar', '1dFLdP3297AHwCVMOu5ScNEXaWxFW2TO7uj60UQY+PktBgoem0kVhF8b3FqvZDXn2eyOCHQab6aCK0oYGgm/2w==', 1, 'arrudaalencar@gmail.com');
-- senha GfT12-
insert into funcionario (nome, datanascimento, cpf, sexo, endereco_id, login, senha, perfil, email) values('Marta de Cesare', '2001-11-30', '19452476141', 'F', 5, 'martacesare', '5xUVJJKuIYyPjJxOW+bpINIeH2hof2GslHdDXYZDJiG15gET9agGaJ+g1GMr0KgGG0/4yaBDlkp0D3YU7Whgwg==', 2,  'martacesare@gmail.com');
--insert into funcionario (nome, datanascimento, cpf, sexo, endereco_id, login, senha, perfil, email) values('Pedro Dantas Limacedo', '2002-05-18', '19452476141', 'F', 5, 'martacesare', '5xUVJJKuIYyPjJxOW+bpINIeH2hof2GslHdDXYZDJiG15gET9agGaJ+g1GMr0KgGG0/4yaBDlkp0D3YU7Whgwg==', 2,  'martacesare@gmail.com');


insert into fornecedor (nomeFantasia, cnpj, endSite) values('.Net.Com', '20229087654321', 'www.net.com.br');
insert into fornecedor (nomeFantasia, cnpj, endSite) values('.Hardware.Com', '20197352940123', 'www.hardware.com.br');

insert into telefone (tipoTelefone, ddd, numerotelefone) values(2, '62', '987450019');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(1, '62', '33249087');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(2, '63', '997843201');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(2, '11', '978530098');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(2, '11', '964531234');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(1, '11', '337409321');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(3, '11', '988885432');
insert into telefone (tipoTelefone, ddd, numerotelefone) values(2, '61', '922704321');

-- CLIENTES
insert into cliente_endereco (id_cliente, id_endereco) values(1,1);
insert into cliente_endereco (id_cliente, id_endereco) values(1,6);
insert into cliente_telefone (id_cliente, id_telefone) values(1,1);
insert into cliente_endereco (id_cliente, id_endereco) values(2,2);
insert into cliente_telefone (id_cliente, id_telefone) values(2,3);

-- FUNCIONÁRIOS 
insert into funcionario_telefone (id_funcionario, id_telefone) values(1,4);
insert into funcionario_telefone (id_funcionario, id_telefone) values(2,5);

insert into empresa_endereco (id_empresa, id_endereco) values(1,7);
insert into empresa_telefone (id_empresa, id_telefone) values(1,2);

insert into fornecedor_endereco (id_fornecedor, id_endereco) values(1,3);
insert into fornecedor_telefone (id_fornecedor, id_telefone) values(1,6);   
insert into fornecedor_telefone (id_fornecedor, id_telefone) values(1,7);

insert into fornecedor_endereco (id_fornecedor, id_endereco) values(2,8);
insert into fornecedor_telefone (id_fornecedor, id_telefone) values(2,8);

insert into classificacao (nome) values('Processadores');
insert into classificacao (nome) values('Placas Mãe');

insert into produto (nome, modelo, descricao, codigoBarras, marca, altura, largura, comprimento, peso, valorVenda, quantidadeUnidades, classificacao_id, version, nomeImagem, imagemprincipal) values('Processador Intel Core i7 11 gen', 'XYZ-FGH12', 'Processador Intel Core i7 11 gen', 'PICI711gen07102023', 'Intel', 0.5, 0.5, 0.5, 0.25, 1000.50, 10, 1, 1, '{"3a2c2c2e-7769-4610-8963-43f532517c9a.jpeg", "9e8c6c9e-0f0c-45e3-8bac-d35716579300.jpeg"}', '3a2c2c2e-7769-4610-8963-43f532517c9a.jpeg');
insert into processador (produto_id, bloqueado, canaismemoria) values (1, 'não', '256');
insert into produto (nome, modelo, descricao, codigoBarras, marca, altura, largura, comprimento, peso, valorVenda, quantidadeUnidades, classificacao_id, version, nomeImagem, imagemprincipal) values('Processador Intel Core i5 12 gen', 'CVBN-KLo34','Processador Intel Core i5 12 gen', 'PICI512gen07102023', 'Intel', 0.5, 0.5, 0.5, 0.25, 1200.0, 5, 1, 1, '{"3a2c2c2e-7769-4610-8963-43f532517c9a.jpeg", "9e8c6c9e-0f0c-45e3-8bac-d35716579300.jpeg"}', '3a2c2c2e-7769-4610-8963-43f532517c9a.jpeg');
insert into processador (produto_id, bloqueado, canaismemoria) values (2, 'não', '512');
insert into produto (nome, modelo, descricao, codigoBarras, marca, altura, largura, comprimento, peso, valorVenda, quantidadeUnidades, classificacao_id, version, nomeImagem, imagemprincipal) values('Processador AMD Ryzen7 11 gen', 'ZEN5-100R', 'Processador AMD Ryzen7 11 gen', 'PAMDX711gen07102023', 'AMD', 0.5, 0.5, 0.5, 0.25, 1000.0, 4, 1, 1, '{"amd_ryzen1.jpg", "amd_ryzen2.jpg"}', 'amd_ryzen2.jpg');
insert into processador (produto_id, bloqueado, canaismemoria) values (3, 'não', '512');
insert into produto (nome, modelo, descricao, codigoBarras, marca, altura, largura, comprimento, peso, valorVenda, quantidadeUnidades, classificacao_id, version, nomeImagem, imagemprincipal) values('Placa Mãe Asus', 'AAA-CGH04', 'Placa mãe ASUS para Inte Core a partir da 13 gen', 'PMA13gen30032024', 'ASUS', 0.5, 0.5, 0.5, 0.25, 1500.0, 31, 2, 1, '{"placa_mae1.jpg", "placa_mae2.jpg"}', 'placa_mae2.jpg');
insert into placamae (produto_id, armazenamento) values (4, '32 MB');



insert into lote (lote, fornecedor_id, dataHoraChegadaLote, produto_id, custoCompra, valorVenda, quantidadeUnidades, statusDoLote, dataHoraAtivacaoLote) values('f1_p1_lt15', 1, '2023-10-01 16:11:26', 1, 500.0, 1000.0, 100, 1, '2023-10-01 16:11:26');
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, produto_id, custoCompra, valorVenda, quantidadeUnidades, statusDoLote, dataHoraAtivacaoLote) values('f1_p1_lt15', 1, '2023-10-02 15:14:01', 2, 600.0, 1200.0, 50, 1, '2023-10-02 15:14:01');
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, produto_id, custoCompra, valorVenda, quantidadeUnidades, statusDoLote, dataHoraAtivacaoLote) values('f1_p1_lt15', 1, '2023-10-03 09:47:50', 3, 500.0, 1000.0, 20, 1, '2023-10-03 09:47:50');
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, dataHoraUltimoVendido, produto_id, custoCompra, valorVenda, quantidadeUnidades, statusDoLote, dataHoraAtivacaoLote) values('f2_p2_lt14', 2, '2022-01-01 16:11:26', '2023-09-30 16:11:26', 1, 700.0, 1400.0, 50, 0, '2022-01-01 16:11:26');
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, dataHoraUltimoVendido, produto_id, custoCompra, valorVenda, quantidadeUnidades, statusDoLote, dataHoraAtivacaoLote) values('f2_p2_lt14', 2, '2022-01-01 16:11:26', '2023-10-01 16:11:26', 2, 800.0, 1600.0, 50, 0, '2022-01-01 16:11:26');
insert into lote (lote, fornecedor_id, dataHoraChegadaLote, produto_id, custoCompra, valorVenda, quantidadeUnidades, statusDoLote, dataHoraAtivacaoLote) values('f1_p1_lt14', 2, '2022-01-01 16:11:26', 4, 750.0, 1500.0, 31, 1, '2022-01-01 16:11:26');
insert into lote (lote, fornecedor_id, produto_id, custoCompra, valorVenda, quantidadeUnidades, statusDoLote, dataHoraChegadaLote) values('f1_p1_lt15', 2, 1, 500.0, 1000.0, 5, 2, '2024-03-04 09:23:55');
insert into lote (lote, fornecedor_id, produto_id, custoCompra, valorVenda, quantidadeUnidades, statusDoLote, dataHoraChegadaLote) values('f1_p1_lt15', 2, 2, 600.0, 1200.0, 15, 2, '2024-02-10 13:24:16');

update produto set loteAtual_id = 1 where id = 1;
update produto set loteAtual_id = 2 where id = 2;
update produto set loteAtual_id = 3 where id = 3;
update produto set loteAtual_id = 6 where id = 4;

-- Pedido 1
insert into itemDaVenda (preco, quantidadeUnidades, produto_id) values(900.0, 3, 1);
insert into itemDaVenda (preco, quantidadeUnidades, produto_id) values(1200.0, 1, 2);
insert into itemDaVenda (preco, quantidadeUnidades, produto_id) values(1000.0, 2, 3);
insert into itemDaVenda (preco, quantidadeUnidades, produto_id) values(1500.0, 10, 4);

insert into formaDePagamento (modalidade, valorPago) values(0, 20900);

insert into cartaodecredito (anovalidade, codseguranca, mesvalidade, datahorapagamento, numerocartao, formaPagamento_id) values(24, 237, 3, '2023-10-01 16:11:26', '345690872106', 1);
insert into pedido (id_formaDePagamento, id_endereco, id_cliente) values(1, 1, 1);

insert into statusDoPedido (dataHora, status, id_pedido) values('2023-10-01 16:11:26', 0, 1);
insert into statusDoPedido (dataHora, status, id_pedido) values('2023-10-04 20:23:35', 1, 1);
/* insert into pedido_statusDoPedido (id_pedido, id_statusDoPedido) values(1,1);
insert into pedido_statusDoPedido (id_pedido, id_statusDoPedido) values(1,2); */

insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,1);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,2);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,3);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(1,4);

-- Pedido 2
insert into itemDaVenda (preco, quantidadeUnidades, produto_id) values(1000.0, 1, 3);
insert into itemDaVenda (preco, quantidadeUnidades, produto_id) values(1500.0, 2, 4);

insert into formaDePagamento (modalidade, valorPago) values(2, 4000);

insert into pix (formaPagamento_id) values(2);

insert into pedido (id_formaDePagamento, id_endereco, id_cliente) values(2, 1, 1);

insert into statusDoPedido (dataHora, status, id_pedido) values('2023-10-01 16:11:26', 0, 2);
-- insert into statusDoPedido (dataHora, status, id_pedido) values('2023-10-04 20:23:35', 1, 2);
insert into statusDoPedido (dataHora, status, id_pedido) values('2023-10-05 09:10:40', 2, 2);

insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(2,5);
insert into pedido_itemDaVenda (id_pedido, id_itemDaVenda) values(2,6);



insert into lista_de_desejos (id_cliente, id_produto) values(2,1);