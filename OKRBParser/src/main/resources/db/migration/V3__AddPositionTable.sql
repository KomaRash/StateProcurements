CREATE TABLE
    if not exists MilitaryPosition
(
    PositionID   SERIAL,
    PositionName varchar(150),
    MilitaryUnit varchar(100),
    UserId       INTEGER,
    constraint Position_pk primary key (PositionID)
);
ALTER TABLE MilitaryPosition
    add constraint UserId_fk foreign key (UserId) references Users (UserId);
