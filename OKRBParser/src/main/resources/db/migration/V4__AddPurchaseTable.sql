create table if not exists OKRB
(   productId     SERIAL,
    Section       varchar(2)   Not Null,
    Class         varchar(2)   Not Null,
    Subcategories varchar(2)   Not Null,
    Groupings     varchar(3)   Not Null,
    Name          varchar(2048) Not Null);

