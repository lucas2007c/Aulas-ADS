CREATE DATABASE IF NOT EXISTS banco_digital;
USE banco_digital;

CREATE TABLE IF NOT EXISTS contas (
    numero INT PRIMARY KEY,
    titular VARCHAR(100) NOT NULL,
    saldo DECIMAL(10,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS transferencias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    conta_origem INT NOT NULL,
    conta_destino INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    tarifa DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    modalidade_tarifa VARCHAR(30) NOT NULL,
    data_hora DATETIME NOT NULL,
    CONSTRAINT fk_transf_origem FOREIGN KEY (conta_origem) REFERENCES contas(numero),
    CONSTRAINT fk_transf_destino FOREIGN KEY (conta_destino) REFERENCES contas(numero)
);

INSERT INTO contas (numero, titular, saldo) VALUES
(1001, 'Ana', 1000.00),
(1002, 'Bruno', 500.00),
(1003, 'Carla', 2000.00)
ON DUPLICATE KEY UPDATE titular=VALUES(titular);
