create table if not exists users
(
    id serial not null
        constraint users_pk
            primary key,
    login varchar(15) not null,
    password varchar(18) not null,
    deleted boolean default false not null,
    email varchar(30) default 'not added'::character varying not null,
    created timestamp default CURRENT_TIMESTAMP not null,
    changed timestamp default CURRENT_TIMESTAMP not null,
    role varchar(20) default 'REGULAR_USER'::character varying not null
);

alter table users owner to postgres;

create unique index if not exists users_id_uindex
    on users (id);

create unique index if not exists users_login_uindex
    on users (login);

create table if not exists products
(
    id serial not null
        constraint menu_pk
            primary key,
    name varchar(100) not null,
    available boolean default true not null,
    price integer,
    quantity integer default 0 not null
);

alter table products owner to postgres;

create unique index if not exists menu_name_uindex
    on products (name);

create table if not exists employees
(
    id serial not null
        constraint employees_pk
            primary key,
    full_name varchar(50) not null,
    birth_date date default ('1980-01-01'::date + (floor((random() * (('2002-01-01'::date - '1980-01-01'::date))::double precision)))::integer) not null,
    registration varchar(50) not null,
    hiring_date date default CURRENT_DATE not null,
    position varchar(20) default 'EMPLOYEE'::character varying not null,
    contract_expiration date default (CURRENT_DATE + 365),
    payroll integer not null,
    fired boolean default false not null,
    phone_number varchar(17)
);

alter table employees owner to postgres;

create unique index if not exists employees_id_uindex
    on employees (id);

create table if not exists cars
(
    id serial not null
        constraint cars_pk
            primary key,
    model varchar(40) not null,
    color varchar(20) not null,
    license_plate_number varchar(15) default 'not defined'::character varying not null,
    vin_number varchar(17) default substr(md5((random())::text), 0, 17) not null,
    driver_id integer,
    available boolean default true not null,
    deleted boolean default false not null
);

alter table cars owner to postgres;

create unique index if not exists cars_id_uindex
    on cars (id);

create unique index if not exists cars_vin_number_uindex
    on cars (vin_number);

create table if not exists drivers
(
    id serial not null
        constraint drivers_pk_2
            primary key,
    employee_id integer not null
        constraint drivers_employees_id_fk
            references employees (id),
    car_id integer
        constraint drivers_cars_id_fk
            references cars,
    driver_license_id bigint default floor((random() * (1000000)::double precision)) not null,
    driver_license_expiration_date date default CURRENT_DATE not null,
    available boolean default true not null
);

alter table drivers owner to postgres;

create table if not exists order_info
(
    id serial not null
        constraint orders_pk
            primary key,
    customer_id integer not null
        constraint customer_orders_users_id_fk
            references users (id),
    order_line varchar(255) not null,
    creation_time timestamp default CURRENT_TIMESTAMP not null,
    courier_id integer not null
        constraint customer_orders_drivers_id_fk
            references drivers,
    status varchar(32) default 'IN_PROGRESS'::character varying not null,
    total_price integer not null,
    delivery_cost integer default 100 not null
);

alter table order_info owner to postgres;

create unique index if not exists orders_id_uindex
    on order_info (id);

alter table cars
    add constraint cars_drivers_id_fk
        foreign key (driver_id) references drivers (id);

create unique index if not exists drivers_id_uindex_2
    on drivers (id);

