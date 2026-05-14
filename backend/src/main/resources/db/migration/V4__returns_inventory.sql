create table if not exists inventory_movements (
    id bigserial primary key,
    product_id bigint not null,
    variant_id bigint,
    type varchar(20) not null,
    quantity integer not null,
    reference_type varchar(30),
    reference_id bigint,
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_inventory_movements_product foreign key (product_id) references products (id),
    constraint fk_inventory_movements_variant foreign key (variant_id) references product_variants (id)
);

create table if not exists return_requests (
    id bigserial primary key,
    order_id bigint not null unique,
    status varchar(20) not null,
    reason varchar(500),
    note varchar(500),
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_return_requests_order foreign key (order_id) references orders (id)
);

alter table cart_items add column if not exists reserved_until timestamp;
