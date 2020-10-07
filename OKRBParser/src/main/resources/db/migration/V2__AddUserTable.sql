CREATE TABLE if not exists Users
(
    UserId         Serial,
    UserName       varchar(20)  not null,
    UserSurname    varchar(45)  not null,
    UserFatherName varchar(40)  not null default ' ',
    userEmail      varchar(255) not null,
    userPassword   varchar(255) not null,
    MilitaryRank   varchar(30)  not null,
    PositionId     integer,
    constraint Users_pk primary key (UserId)
);
