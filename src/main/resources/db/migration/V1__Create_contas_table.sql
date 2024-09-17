CREATE TABLE conta (
                       id BIGSERIAL PRIMARY KEY,
                       data_vencimento DATE NOT NULL,
                       data_pagamento DATE,
                       valor DECIMAL(10, 2) NOT NULL,
                       descricao VARCHAR(255),
                       situacao VARCHAR(20) DEFAULT 'PENDENTE'
);
