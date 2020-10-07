CREATE TABLE
    if not exists MilitaryPosition
(
    PositionID   SERIAL,
    PositionName varchar(150),
    MilitaryUnit varchar(100),
    constraint Position_pk primary key (PositionID)
);

ALTER TABLE Users
    add constraint PositionId_fk foreign key (PositionId) references MilitaryPosition (PositionId);
