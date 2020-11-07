create table documents
(
    documentid   serial       not null
        constraint documents_pkey
            primary key,
    documentname varchar(255) not null,
    documentbody bytea        not null,
    purchaseid   integer      not null
        constraint fk
            references purchase
);

