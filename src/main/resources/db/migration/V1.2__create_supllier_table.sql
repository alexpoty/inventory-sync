CREATE TABLE suppliers
(
    id           BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name         TEXT NOT NULL,
    contact_info TEXT
);