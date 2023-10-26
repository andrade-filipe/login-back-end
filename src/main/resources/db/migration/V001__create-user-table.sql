CREATE TABLE user(
    user_id varchar(36) UNIQUE NOT NULL,
    username varchar(256) UNIQUE not null,
    name varchar(256) not null,
    email varchar(256) UNIQUE not null,
    password varchar(72) not null,
    user_role varchar(24) not null,
    gender varchar(24) not null,
    birth_date datetime not null,

    primary key(user_id)
);