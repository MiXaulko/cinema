create table role
(
    id   bigint auto_increment primary key,
    name varchar(10)
);

create table category
(
    id       bigint auto_increment primary key,
    name     varchar(64) not null,
    discount float       not null
);

create table user
(
    id             bigint auto_increment primary key,
    login          varchar(64) not null,
    fk_role_id     bigint,
    fk_category_id bigint,
    foreign key (fk_role_id) references role (id),
    foreign key (fk_category_id) references category (id)
);

create table seance
(
    id                 bigint auto_increment primary key,
    name               varchar(64) not null,
    start_time         timestamp   not null,
    price              int         not null,
    minimum_profit     int         not null,
    enabled_privileges boolean     not null,
    number_of_rows     int         not null,
    number_of_columns  int         not null
);

create table seat_reservation
(
    id           bigint auto_increment primary key,
    real_price   float not null,
    row_         int   not null,
    column_      int   not null,
    fk_user_id   bigint,
    fk_seance_id bigint,
    foreign key (fk_user_id) references user (id),
    foreign key (fk_seance_id) references seance (id)
);

insert into role (name)
values ('admin'),
       ('client');

insert into category (name, discount)
values ('common', 0),
       ('social-1', 20),
       ('social-2', 50),
       ('social-3', 100);

insert into user (login, fk_role_id, fk_category_id)
values ('admin', 1, 1),
       ('user-1', 2, 1)