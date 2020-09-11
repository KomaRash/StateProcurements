create table if not exists Purchase
(   PurchaseId     SERIAL primary key ,
    Description varchar(1024),
    DateOfPurchase date,
    PositionId SERIAL,
    ProcedureName     varchar(255) Not Null,
    constraint Position_fk foreign key (PositionId) references MilitaryPosition(PositionId)
);
create table if not exists PurchaseLot(
    PurchaseLotId SERIAL primary key ,
    PurchaseId SERIAL,
    ProductId SERIAL,
    Deadline date,
    LotAmount float,
    LotName varchar(255) not null,
    constraint OKRB_fk foreign key(ProductId) references okrb(productid),
    constraint Purchase_fk foreign key(PurchaseId) references Purchase(PurchaseId)
);
