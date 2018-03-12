create table user
(
    idUser integer Primary Key,
    username varchar2(20),
    password varchar2(20),
    biographie varchar2(500),
    isPro number(1,0)
);
create table post
(
    idPost integer Primary Key,
    idCreator integer,
    caption varchar2(500),
    likes integer,
    timestamp date,
    constraint fk_post_user foreign key(idCreator) references user(idUser)
);
create table comment
(
    idComment integer primary key,
    idCreator integer,
    idPost integer,
    content varchar2(100),
    timestamp date,
    constraint fk_comment_user foreign key(idCreator) references User(idUser),
    constraint fk_comment_post foreign key(idPost) references Post(idPost)
);
create table location
(
    idLocation integer primary key,
    idPage integer,
    name varchar2(50),
    lat double,
    lng double,
    type varchar2(20) check(type in ('EVENT', 'PUBLIC_PLACE', 'GYM', 'OTHER')),
    startdatetime date,
    enddatetime date,
    constraint fk_location_page foreign key(idPost) references Post(idPost),
    constraint ck_location check(startdatetime < enddatetime and 
      ((startdatetime is null and enddatetime is null and type not like 'EVENT') or
      (startdatetime is not null and enddatetime is not null and type = 'EVENT')))
);
create table follows 
(
    idFollower integer,
    idOl integer,
    constraint pk_follows primary key (idFollower, idOl),
    constraint fk_follows_follower foreign key(idFollower) references user(idUser),
    constraint fk_follows_ol foreign key(idOl) references user(idUser),
    constraint ck_follows check (idFollower != idOl)
);
create table exercise (
    idExercise integer primary key,
    name varchar2(50),
    description varchar2(500)
);
create table workout (
    idWorkout integer primary key,
    name varchar2(50)
);
create table dailyWorkout (
    idDailyWorkout integer primary key,
    name varchar2(50)
);
create table plan (
    idPlan integer primary key,
    idCreator integer,
    name varchar2(50),
    constraint fk_plan_creator foreign key(idCreator) references user(idUser)
);
create table subscription (
    idPlan integer,
    idUser integer,
    constraint pk_subscription primary key (idUser, idPlan),
    constraint fk_subscription_user foreign key(idUser) references user(idUser),
    constraint fk_subscription_plan foreign key(idPlan) references plan(idPlan)
);
create table containsPD (
    idPlan integer,
    idDailyWorkout integer,
    constraint pk_containsPD primary key (idDailyWorkout, idPlan),
    constraint fk_containsPD_dailyWorkout foreign key(idDailyWorkout) references dailyWorkout(idDailyWorkout),
    constraint fk_containsPD_plan foreign key(idPlan) references plan(idPlan)
);
create table containsDW (
    idDailyWorkout integer,
    idWorkout integer,
    constraint pk_containsDW primary key (idDailyWorkout, idWorkout),
    constraint fk_containsDW_dailyWorkout foreign key(idDailyWorkout) references dailyWorkout(idDailyWorkout),
    constraint fk_containsDW_workout foreign key(idWorkout) references workout(idWorkout)
);
create table containsWE (
    idWorkout integer,
    idExercise integer,
    constraint pk_containsWE primary key (idExercise, idWorkout),
    constraint fk_containsWE_exercise foreign key(idExercise) references exercise(idExercise),
    constraint fk_containsWE_workout foreign key(idWorkout) references workout(idWorkout)
);