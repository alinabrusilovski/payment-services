GRANT ALL PRIVILEGES ON DATABASE payment_service TO payment_service;

CREATE TABLE payer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    second_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    email VARCHAR(255),
    phone VARCHAR(50)
);

ALTER TABLE public.payer
    OWNER to payment_service;

CREATE TABLE invoice (
id SERIAL PRIMARY KEY,
system_id INTEGER,
payer_id INTEGER REFERENCES payer(id)  NOT NULL,
invoice_description TEXT
);

ALTER TABLE public.invoice
    OWNER to payment_service;

CREATE TABLE invoice_position (
id SERIAL PRIMARY KEY,
invoice_id INTEGER REFERENCES invoice(id) NOT NULL,
invoice_position_description TEXT,
amount DECIMAL(10, 2) NOT NULL
);

ALTER TABLE public.invoice_position
    OWNER to payment_service;

ALTER TABLE "payer"
ALTER COLUMN birth_date SET NOT NULL;

ALTER TABLE "invoice"
ALTER COLUMN invoice_description TYPE VARCHAR(255),
ALTER COLUMN invoice_description SET NOT NULL;

ALTER TABLE invoice_position
ALTER COLUMN invoice_position_description TYPE VARCHAR(255);

ALTER TABLE invoice_position
ALTER COLUMN invoice_position_description SET NOT NULL;

ALTER TABLE users
ADD COLUMN refresh_token VARCHAR(1000) UNIQUE,
ADD COLUMN refresh_token_expired TIMESTAMP;
CREATE INDEX idx_refresh_token ON users(refresh_token);
