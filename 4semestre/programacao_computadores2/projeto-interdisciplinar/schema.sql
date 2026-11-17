CREATE DATABASE IF NOT EXISTS concessionaria_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE concessionaria_db;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(64) NOT NULL,
    tipo_acesso VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS veiculos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano_modelo INT NOT NULL,
    cor VARCHAR(30) NOT NULL,
    placa VARCHAR(10) NOT NULL UNIQUE,
    quilometragem INT NOT NULL,
    tipo_veiculo VARCHAR(10) NOT NULL,
    preco_aquisicao DECIMAL(12, 2) NOT NULL,
    data_entrada DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    quantidade_portas INT NULL,
    cilindradas INT NULL
);

INSERT IGNORE INTO usuarios (id, nome, usuario, senha, tipo_acesso)
VALUES (1, 'Administrador do Sistema', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMINISTRADOR');
