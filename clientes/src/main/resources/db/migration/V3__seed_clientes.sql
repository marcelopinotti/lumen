-- Seed de clientes iniciais
INSERT INTO cliente (nome, cpf, idade) VALUES
    ('Marcelo', '12345678900', 27),
    ('Luiz', '12345678901', 27)
ON CONFLICT (cpf) DO NOTHING;
