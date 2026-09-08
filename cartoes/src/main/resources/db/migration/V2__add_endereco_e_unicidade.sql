ALTER TABLE cliente_cartao ADD COLUMN IF NOT EXISTS endereco_de_entrega VARCHAR(255);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_cliente_cartao_cpf_cartao'
    ) THEN
        ALTER TABLE cliente_cartao
            ADD CONSTRAINT uk_cliente_cartao_cpf_cartao UNIQUE (cpf, id_cartao);
    END IF;
END $$;
