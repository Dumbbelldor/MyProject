create table if not exists users
(
    id serial not null
        constraint m_users_pk
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

create unique index if not exists m_users_id_uindex
    on users (id);

create unique index if not exists m_users_login_uindex
    on users (login);

create table if not exists products
(
    id serial not null
        constraint m_menu_pk
            primary key,
    name varchar(100) not null,
    available boolean default true not null,
    price integer
);

alter table products owner to postgres;

create unique index if not exists m_menu_name_uindex
    on products (name);

create table if not exists employees
(
    id serial not null
        constraint m_employees_pk
            primary key,
    full_name varchar(50) not null,
    birth_date date default ('1980-01-01'::date + (floor((random() * (('2002-01-01'::date - '1980-01-01'::date))::double precision)))::integer) not null,
    registration varchar(50) not null,
    hiring_date date default CURRENT_DATE not null,
    position varchar(20) default 'employee'::character varying not null,
    contract_expiration date default (CURRENT_DATE + 365),
    payroll integer not null,
    fired boolean default false not null,
    phone_number integer
);

alter table employees owner to postgres;

create unique index if not exists m_employees_id_uindex
    on employees (id);

create table if not exists cars
(
    id serial not null
        constraint m_cars_pk
            primary key,
    model varchar(20) not null,
    color varchar(20) not null,
    license_plate_number varchar(15) default 'not defined'::character varying not null,
    vin_number varchar(17) default md5((random())::text) not null,
    driver_id integer,
    available boolean default true not null,
    deleted boolean default false not null
);

alter table cars owner to postgres;

create unique index if not exists m_cars_id_uindex
    on cars (id);

create unique index if not exists m_cars_vin_number_uindex
    on cars (vin_number);

create table if not exists drivers
(
    id serial not null
        constraint m_drivers_pk_2
            primary key,
    employee_id integer not null
        constraint m_drivers_m_employees_id_fk
            references employees,
    car_id integer
        constraint m_drivers_m_cars_id_fk
            references cars,
    driver_license_id bigint default floor((random() * (1000000)::double precision)) not null,
    driver_license_expiration_date date default CURRENT_DATE not null,
    available boolean default true not null
);

alter table drivers owner to postgres;

create table if not exists order_info
(
    id serial not null
        constraint m_orders_pk
            primary key,
    customer_id integer not null
        constraint m_customer_orders_m_users_id_fk
            references users,
    order_line varchar(255) not null,
    creation_time timestamp default CURRENT_TIMESTAMP not null,
    courier_id integer not null
        constraint m_customer_orders_m_drivers_id_fk
            references drivers,
    status varchar(32) default 'IN_PROGRESS'::character varying not null,
    total_price integer not null
);

alter table order_info owner to postgres;

create unique index if not exists m_orders_id_uindex
    on order_info (id);

alter table cars
    add constraint m_cars_m_drivers_id_fk
        foreign key (driver_id) references drivers;

create unique index if not exists m_drivers_id_uindex_2
    on drivers (id);

create table if not exists hibernate_sequences
(
    sequence_name varchar(255) not null
        constraint hibernate_sequences_pkey
            primary key,
    next_val bigint
);

alter table hibernate_sequences owner to postgres;

