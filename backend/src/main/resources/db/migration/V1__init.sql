create table roles (
    id bigserial primary key,
    role_name varchar(100) not null unique
);

create table users (
    id bigserial primary key,
    email varchar(255) not null unique,
    full_name varchar(200) not null,
    enabled boolean not null default true,
    password_hash varchar(255) not null,
    created_at timestamp,
    updated_at timestamp
);

create table user_roles (
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_user_roles_user foreign key (user_id) references users (id),
    constraint fk_user_roles_role foreign key (role_id) references roles (id)
);

create table addresses (
    id bigserial primary key,
    line1 varchar(255),
    line2 varchar(255),
    postal_code varchar(50),
    administrative_area varchar(100),
    administrative_area_code varchar(50),
    country varchar(100),
    is_primary boolean not null default false,
    user_id bigint,
    constraint fk_addresses_user foreign key (user_id) references users (id)
);

create table sellers (
    id bigserial primary key,
    owner_id bigint not null,
    company_name varchar(255) not null,
    contact_email varchar(255),
    tax_id varchar(100),
    status varchar(20) not null,
    logo_url varchar(500),
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_sellers_owner foreign key (owner_id) references users (id)
);

create table products (
    id bigserial primary key,
    sku varchar(100) not null unique,
    name varchar(255) not null,
    description text,
    base_price numeric(12, 2),
    currency varchar(10),
    active boolean not null default true,
    stock integer not null default 0,
    seller_id bigint,
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_products_seller foreign key (seller_id) references sellers (id)
);

create table product_variants (
    id bigserial primary key,
    product_id bigint,
    sku varchar(100) unique,
    price_override numeric(12, 2),
    stock integer not null default 0,
    constraint fk_variants_product foreign key (product_id) references products (id)
);

create table variant_attributes (
    id bigserial primary key,
    variant_id bigint,
    name varchar(100) not null,
    value varchar(100) not null,
    constraint fk_variant_attributes_variant foreign key (variant_id) references product_variants (id)
);

create table catalogs (
    id bigserial primary key,
    name varchar(255),
    slug varchar(255) unique
);

create table catalog_products (
    catalog_id bigint not null,
    product_id bigint not null,
    primary key (catalog_id, product_id),
    constraint fk_catalog_products_catalog foreign key (catalog_id) references catalogs (id),
    constraint fk_catalog_products_product foreign key (product_id) references products (id)
);

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
    reserved_until timestamp,
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
    card_last4 varchar(4),
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_payments_order foreign key (order_id) references orders (id)
);

create table shipments (
    id bigserial primary key,
    order_id bigint not null unique,
    status varchar(20) not null,
    tracking_code varchar(100),
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_shipments_order foreign key (order_id) references orders (id)
);

create table inventory_movements (
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

create table return_requests (
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
