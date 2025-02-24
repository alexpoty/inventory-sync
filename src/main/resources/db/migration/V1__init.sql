CREATE TABLE warehouses
(
    id       BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name     TEXT NOT NULL,
    location TEXT NOT NULL
);

CREATE TABLE products
(
    id            BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name          TEXT NOT NULL,
    description   TEXT,
    price         NUMERIC(10, 2),
    quantity      INT  NOT NULL,
    warehouses_id BIGINT REFERENCES warehouses (id),
    created_at    TIMESTAMP with time zone DEFAULT NOW(),
    updated_at    TIMESTAMP with time zone DEFAULT NOW()
);

