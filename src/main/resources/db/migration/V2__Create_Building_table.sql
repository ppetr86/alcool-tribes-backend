create table buildings (
    id bigint not null auto_increment PRIMARY KEY ,
    type int not null,
    level int not null ,
    hp int not null ,
    started_at bigint not null ,
    finished_at bigint not null
);