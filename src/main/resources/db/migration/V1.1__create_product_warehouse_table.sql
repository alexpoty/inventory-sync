CREATE TABLE product_warehouse(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    product_id BIGINT REFERENCES products (id),
    warehouse_id BIGINT REFERENCES warehouses (id),
    quantity INT NOT NULL
);