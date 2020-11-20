alter table purchaselot drop constraint purchase_fk;
alter table purchaselot add constraint Purchase_fk foreign key (PurchaseId) references Purchase (PurchaseId) On delete CASCADE ;
