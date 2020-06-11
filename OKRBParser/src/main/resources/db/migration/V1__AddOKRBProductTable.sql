create  table if not exists OKRB(
                  Section varchar(2) Not Null,
                  Class varchar(2) Not Null,
                  Subcategories varchar(2) Not Null,
                  Groupings varchar(3) Not Null,
                  Name varchar(255) Not Null);
Alter table OKRB add constraint OKRB_PK primary key(Section,Class,Subcategories,Groupings)
