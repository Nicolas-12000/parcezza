create table carts (
    id bigserial primary key,
    user_id bigint not null unique,
    constraint fk_carts_user foreign key (user_id) references users (id)
);

create table cart_items (
    id bigserial primary key,
    cart_id bigint not null,
    product_id bigint not null,
    variant_id bigint,
    quantity integer not null,
    unit_price numeric(12, 2) not null,
    currency varchar(10) not null,
    line_total numeric(12, 2) not null,
    constraint fk_cart_items_cart foreign key (cart_id) references carts (id),
    constraint fk_cart_items_product foreign key (product_id) references products (id),
    constraint fk_cart_items_variant foreign key (variant_id) references product_variants (id)
);

create table orders (
    id bigserial primary key,
    user_id bigint not null,
    shipping_address_id bigint,
    status varchar(20) not null,
    total_amount numeric(12, 2) not null,
    currency varchar(10) not null,
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_orders_user foreign key (user_id) references users (id),
    constraint fk_orders_address foreign key (shipping_address_id) references addresses (id)
);

create table order_items (
    id bigserial primary key,
    order_id bigint not null,
    product_id bigint not null,
    variant_id bigint,
    quantity integer not null,
    unit_price numeric(12, 2) not null,
    currency varchar(10) not null,
    line_total numeric(12, 2) not null,
    constraint fk_order_items_order foreign key (order_id) references orders (id),
    constraint fk_order_items_product foreign key (product_id) references products (id),
    constraint fk_order_items_variant foreign key (variant_id) references product_variants (id)
);

create table payments (
    id bigserial primary key,
    order_id bigint not null,
    status varchar(20) not null,
    provider varchar(50),
    provider_ref varchar(120),
    amount numeric(12, 2) not null,
    currency varchar(10) not null,
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_payments_order foreign key (order_id) references orders (id)
);
