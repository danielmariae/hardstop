Clientes:
http://localhost:8080/auth/cliente
katiaflavia     123VaA_  USER
flavio1234      1a2B3!   USER

Funcionarios:
http://localhost:8080/auth/funcionario
arrudaalencar  bVgH_-2  FUNC
martacesare    GfT12-   ADMIN


// Inserir Cliente - Cliente Resource - POST /clientes
// Os tipos possíveis de telefone são: FIXO_RESIDENCIAL (0), FIXO_COMERCIAL (1), CELULAR_PARTICULAR (2), CELULAR_COMERCIAL (3), TELEFONE_OUTRA_PESSOA (4)

{
  "nome": "Jair Figueiras",
  "dataNascimento": "09/10/2010",
  "cpf": "816.211.203-08",
  "sexo": "M",
  "login": "jair1234",
  "senha": "!123Qw",
  "email": "jair123@gmail.com",
  "listaEndereco": [
    {
      "nome": "Jair Figueiras",
      "rua": "Barão de Cotegipe",
      "numero": "57",
      "lote": "s/l",
      "bairro": "'",
      "complemento": "Próximo a praça do Ancião",
      "cep": {
        "prefixo": 0,
        "sufixo": 0,
        "cep": "51269652"
      },
      "localidade": "Rio de Janeiro",
      "uf": "RJ",
      "pais": "Brasil"
    }
  ],
  "listaTelefone": [
    {
      "tipo": 1,
      "ddd": "89",
      "numeroTelefone": "997850123"
    }
  ]
}


// Inserir ou alterar endereço já existente
// - Funcionario Logado Resource - PATCH /funcionariologado/patch/endereco/
// - Cliente Logado Resource - PATCH /clientelogado/patch/endereco/
// Caso o id pertença ao usuário, o sistema irá alterar os dados do endereço e manter o mesmo id
// Caso o id não pertença ao usuário, o sistema adicionará um novo endereço àquele usuário 


[
  {
    "id": 1,
    "nome": "Katia Flávia",
    "logradouro": "Rua dos Borges Alemães",
    "numero": "27",
    "lote": "s/l",
    "bairro": "Genebra",
    "complemento": "Próximo ao Carrefur",
    "cep": {
      "prefixo": 0,
      "sufixo": 0,
      "cep": "65432-078"
    },
    "localidade": "Rialma",
    "uf": "GO",
    "pais": "Brasil"
  }
]






// Inserir ou alterar telefone já existente 
// - Funcionario Logado Resource - PATCH /funcionariologado/patch/telefone/
// - Cliente Logado Resource - PATCH /clientelogado/patch/telefone/
// Caso o id pertença ao usuário, o sistema irá alterar o tipo, ddd e númeroTelefone e manter o id
// Caso o id não pertença ao usuário, o sistema adicionará um novo número de telefone àquele usuário. 


[
  {
    "id": 15,
    "tipo": 2,
    "ddd": "013",
    "numeroTelefone": "98567-1111"
  }
]



// Inserir pedido
// - Cliente Logado Resource - POST - /clientelogado/insert/pedido
// Modalidade 0 representa Cartão de Credito; modalidade 1 representa Boleto bancário; modalidade 2 representa PIX.
// Ao escolher a modalidade 0 precisará preencher numeroCartao, mesValidade, anoValidade, codSeguranca
// Ao escolher a modalidade 1 precisará preencher diasVencimento
// Ao escolher a modalidade 2 não precisará preencher nada
// O item da venda precisa existir em estoque (só existem o produto com idProduto = 1, produto com idProduto = 2  e produto com idProduto = 3 em estoque)
// O idEndereco precisa pertencer ao cliente
// Todo pedido inicia com status AGUARDANDO_PAGAMENTO (0)

{
  "formaDePagamento": {
    "modalidade": 1,
    "numeroCartao": "string",
    "mesValidade": 0,
    "anoValidade": 0,
    "codSeguranca": 0,
    "diasVencimento": 7
  },
  "itemDaVenda": [
    {
      "preco": 1000,
      "quantidade": 2,
      "idProduto": 1
    }
  ],
  "idEndereco": 1
}

OU

{
  "formaDePagamento": {
    "modalidade": 0,
    "numeroCartao": "345690872106",
    "mesValidade": 3,
    "anoValidade": 24,
    "codSeguranca": 237,
    "diasVencimento": 0
  },
  "itemDaVenda": [
    {
      "preco": 1000,
      "quantidade": 1,
      "idProduto": 1
    }
  ],
  "idEndereco": 1
}

// Alterar endereço do pedido
// - Cliente Logado Resource - GET - /clientelogado/pedido/patch/endereco
// Pedidos com status: AGUARDANDO_PAGAMENTO (0), PAGO (2), SEPARADO_DO_ESTOQUE (3) podem ter seus endereços alterados
// Pedidos com status: PAGAMENTO_NÃO_AUTORIZADO (1), ENTREGUE_A_TRANSPORTADORA (4) e ENTREGUE (5) não podem ter seus endereços alterados
// O endereço do pedido só pode ser alterado para algum outro endereço dentro da lista de endereços do mesmo cliente
// Precisa informar o id do pedido e em seguida o id do endereço para onde o pedido deverá ser enviado

{
  "idPedido": 1,
  "idEndereco": 6
}


// Deletar pedido
// - Cliente Logado Resource - DELETE - /clientelogado/delete/pedido
// Pedidos com status do tipo: AGUARDANDO_PAGAMENTO ou PAGAMENTO_NÃO_AUTORIZADO podem ser deletados
// Pedidos com status do tipo: PAGO, SEPARADO_DO_ESTOQUE, ENTREGUE_A_TRANSPORTADORA e ENTREGUE não podem ser deletados
// Precisa informar o id do pedido
// O id do pedido informado precisa pertencer ao cliente



// Inserir na lista de desejos do Cliente
// - Cliente Logado Resource - POST - /clientelogado/insert/desejos
// Precisa informar o id do produto a ser inserido na lista de desejos do cliente
// o id do produto precisa existir no banco de dados




// Deletar desejos
// - Cliente Logado Resource - DELETE - /clientelogado/delete/desejos
// Precisa informar o id do produto
// O id do produto informado precisa pertencer à lista de desejos do cliente, caso contrário o método não fará nada


// Alterar Senha
// - Cliente Logado Resource - PATCH - /clientelogado/patch/senha
// Precisa informar a senha atual
// Precisa informar a nova senha
// A nova senha precisa obedecer o padrão pré-estabelecido no sistema (mínimo de 6 caracteres, 1 letra minúscula, 1 letra maiúscula, 1 número, 1 caractere especial) 




// Inserir Funcionario - Funcionario Logado Resource - POST /funcionariologado/insert/funcionario

{
  "nome": "Joseph Klimber",
  "dataNascimento": "05.08.2012",
  "cpf": "247.543.012-34",
  "sexo": "M",
  "login": "joseph",
  "senha": "Joseph12_",
  "email": "josephklimber@gmail.com",
  "endereco": {
    "nome": "Joseph Klimber",
    "logradouro": "Rua Abel de Meneses",
    "numero": "26",
    "lote": "s/l",
    "bairro": "Assombracao",
    "complemento": "Próximo a igreja Calistophocles",
    "cep": {
      "prefixo": 0,
      "sufixo": 0,
      "cep": "647890-21"
    },
    "localidade": "Ananindeua",
    "uf": "PA",
    "pais": "Brasil"
  },
  "idperfil": 1,
  "listaTelefone": [
    {
      "tipo": 0,
      "ddd": "94",
      "numeroTelefone": "942678309"
    }
  ]
}


// Mudança de Endereço para Funcionário - Funcionario Logado Resource - PATCH - /funcionariologado/patch/endereco/funcionario

{
  "logradouro": "Rua Alcolumbre D'Agostini",
  "numero": "s/n",
  "lote": "25",
  "bairro": "Porto Feliz",
  "complemento": "Próximo a igreja do Rosário Rosa",
  "cep": {
    "prefixo": 0,
    "sufixo": 0,
    "cep": "32145678"
  },
  "localidade": "São Paulo",
  "uf": "SP",
  "pais": "Brasil"
}
