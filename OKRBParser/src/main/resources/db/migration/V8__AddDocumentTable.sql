create table documents
(
    documentid   serial       not null
        constraint documents_pkey
            primary key,
    documentname varchar(255) not null,
    documentbody bytea        not null,
    extensions varchar(255) not null,
    descriptions varchar(255) default '',
    documentLink varchar(255) not null,
    purchaseid   integer      not null
        constraint fk
            references purchase
);

