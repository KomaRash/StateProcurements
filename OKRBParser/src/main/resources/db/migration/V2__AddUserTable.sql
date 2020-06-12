CREATE TABLE if not exists Users
(
    UserId         INTEGER      not null auto_increment,
    UserName       varchar(20)  not null,
    UserSurname    varchar(45)  not null,
    UserFatherName varchar(40)  not null default ' ',
    userEmail      varchar(255) not null,
    userPassword   varchar(255) not null,
    MilitaryRank   varchar(30)  not null,
    constraint Users_pk primary key (UserId)
);
#enum('ст. л-т','к-н','м-р','п-п/к','п-п/к')