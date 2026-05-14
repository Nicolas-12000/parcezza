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
