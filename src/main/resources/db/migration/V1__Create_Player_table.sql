create table kingdoms(
    id bigint not null auto_increment PRIMARY KEY ,
    kingdomname varchar(100) not null
);


create table players (
    id bigint not null auto_increment PRIMARY KEY ,
    username varchar(100) not null,
    password varchar (100) not null,
    email varchar(100) not null ,
    avatar varchar(100) not null ,
    points int not null ,
    kingdom_id bigint not null,

    FOREIGN KEY (kingdom_id) REFERENCES kingdoms(id)
);

