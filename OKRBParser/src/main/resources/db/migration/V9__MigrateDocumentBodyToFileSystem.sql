alter table documents
    drop documentbody;
alter table purchaselot
    add column
        purchaseLotStatus varchar(255)
            default 'CreatedLot';
