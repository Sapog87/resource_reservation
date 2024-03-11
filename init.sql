create sequence reservations_seq
    increment by 1;

alter sequence reservations_seq owner to postgres;

create sequence resources_seq
    increment by 1;

alter sequence resources_seq owner to postgres;

create sequence roles_seq
    increment by 1;

alter sequence roles_seq owner to postgres;

create sequence users_seq
    increment by 1;

alter sequence users_seq owner to postgres;

create table resources
(
    id   bigint       not null
        primary key,
    name varchar(255) not null
        unique
);

alter table resources
    owner to postgres;

create index idxl85pqajoc7v2drqv3tj3rcmpq
    on resources (name);

create table roles
(
    id   bigint not null
        primary key,
    name varchar(255)
);

alter table roles
    owner to postgres;

create table users
(
    id       bigint       not null
        primary key,
    name     varchar(255) not null
        unique,
    password varchar(255) not null
);

alter table users
    owner to postgres;

create table reservations
(
    id                bigint not null
        primary key,
    reservation_end   timestamp(6),
    reservation_start timestamp(6),
    resource_id       bigint
        constraint fkcuu9fjca4p9mplpssqwaopdea
            references resources,
    user_id           bigint
        constraint fkb5g9io5h54iwl2inkno50ppln
            references users
);

alter table reservations
    owner to postgres;

create index idx3g1j96g94xpk3lpxl2qbl985x
    on users (name);

create table users_authorities
(
    authorities_id bigint not null
        constraint fk40fukc61kvbvpc2rhv01q1g2l
            references roles,
    users_id       bigint not null
        constraint fk2cmfwo8tbjcpmltse0rh5ir0t
            references users,
    primary key (authorities_id, users_id)
);

alter table users_authorities
    owner to postgres;

INSERT INTO public.roles (id, name) VALUES (nextval('roles_seq'),'USER');
INSERT INTO public.roles (id, name) VALUES (nextval('roles_seq'),'ADMIN');
INSERT INTO public.users (id, name, password) VALUES (nextval('users_seq'),'admin', '$2a$10$EL6aw5KxLwZlMFA7itFVlOh3h0mAMY443en/yKu77pqrlIurNOYDu');
INSERT INTO public.users_authorities (authorities_id, users_id)
SELECT 
	(SELECT id FROM public.roles WHERE name = 'ADMIN') AS authorities_id,
   	(SELECT id FROM public.users WHERE name = 'admin') AS users_id;






















