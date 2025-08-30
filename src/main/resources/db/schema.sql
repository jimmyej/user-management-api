drop table if exists phones cascade;
drop table if exists users cascade;

create table phones (
    id uuid not null,
    user_id uuid,
    city_code varchar(50),
    country_code varchar(50),
    number varchar(50),
    primary key
);

create table users (
    activated boolean not null,
    created timestamp(6) not null,
    last_login timestamp(6) not null,
    modified timestamp(6) not null,
    id uuid not null,
    name varchar(50),
    password varchar(64),
    token varchar(64),
    email varchar(100) unique,
    primary key (id)
);

alter table if exists phones add constraint FKmg6d77tgqfen7n1g763nvsqe3 foreign key (user_id) references users;