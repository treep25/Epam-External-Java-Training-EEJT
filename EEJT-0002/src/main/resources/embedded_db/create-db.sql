create table tag
(
    id   bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(123) not null,
    constraint tag_pk
        primary key (id)
);

create table gift_certificate
(
    id               bigint PRIMARY KEY AUTO_INCREMENT,
    name             varchar(123) not null,
    description      varchar(123) not null,
    price            int,
    duration         int,
    create_date      timestamp,
    last_update_date timestamp,
    constraint gift_certificate_pk
        primary key (id)
);

create table gift_certificate_tag
(
    id                  bigint PRIMARY KEY AUTO_INCREMENT,
    gift_certificate_id bigint not null,
    tag_id              bigint not null,
    constraint gift_certificate_tag_pk
        primary key (id)
);



