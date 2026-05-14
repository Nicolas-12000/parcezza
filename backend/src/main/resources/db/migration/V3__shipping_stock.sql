alter table products add column if not exists stock integer not null default 0;
alter table payments add column if not exists card_last4 varchar(4);

create table if not exists shipments (
    id bigserial primary key,
    order_id bigint not null unique,
    status varchar(20) not null,
    tracking_code varchar(100),
    created_by varchar(255),
    created_at timestamp,
    updated_at timestamp,
    constraint fk_shipments_order foreign key (order_id) references orders (id)
);
