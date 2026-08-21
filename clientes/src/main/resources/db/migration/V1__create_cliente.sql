-- v1 - criação da tabela cliente

CREATE TABLE cliente
(
    id    BIGINT PRIMARY KEY,
    nome  VARCHAR(255) NOT NULL,
    cpf   VARCHAR(11)  NOT NULL UNIQUE,
    idade INT          NOT NULL
);