create table if not exists OKRB
(   productId     INTEGER      not null auto_increment,
    Section       varchar(2)   Not Null,
    Class         varchar(2)   Not Null,
    Subcategories varchar(2)   Not Null,
    Groupings     varchar(3)   Not Null,
    Name          varchar(255) Not Null,
    constraint OKRB_pk primary key (productId)
);

