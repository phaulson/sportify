create table sp_user
(
    idUser integer Primary Key,
    username varchar2(20),
    password varchar2(20),
    biographie varchar2(500),
    isPro number(1,0)
);
create table sp_post
(
    idPost integer Primary Key,
    idCreator integer,
    caption varchar2(500),
    likes integer,
    timestamp date,
    constraint fk_post_user foreign key(idCreator) references sp_user(idUser)
);
create table sp_comment
(
    idComment integer primary key,
    idCreator integer,
    idPost integer,
    content varchar2(100),
    timestamp date,
    constraint fk_comment_user foreign key(idCreator) references sp_user(idUser),
    constraint fk_comment_post foreign key(idPost) references sp_post(idPost)
);
create table sp_location
(
    idLocation integer primary key,
    idUser integer,
    name varchar2(50),
    lat number,
    lng number,
    type varchar2(20) check(type in ('EVENT', 'PUBLIC_PLACE', 'GYM', 'OTHER')),
    startdatetime date,
    enddatetime date,
    constraint fk_location_user foreign key(idUser) references sp_user(idUser),
    constraint ck_location check(startdatetime < enddatetime and 
      ((startdatetime is null and enddatetime is null and type not like 'EVENT') or
      (startdatetime is not null and enddatetime is not null and type like 'EVENT')))
);
create table sp_follows 
(
    idFollower integer,
    idOl integer,
    constraint pk_follows primary key (idFollower, idOl),
    constraint fk_follows_follower foreign key(idFollower) references sp_user(idUser),
    constraint fk_follows_ol foreign key(idOl) references sp_user(idUser),
    constraint ck_follows check (idFollower != idOl)
);
create table sp_exercise (
    idExercise integer primary key,
    name varchar2(50),
    description varchar2(500)
);
create table sp_workout (
    idWorkout integer primary key,
    name varchar2(50)
);
create table sp_dailyWorkout (
    idDailyWorkout integer primary key,
    name varchar2(50)
);
create table sp_plan (
    idPlan integer primary key,
    idCreator integer,
    name varchar2(50),
    constraint fk_plan_creator foreign key(idCreator) references sp_user(idUser)
);
create table sp_subscription (
    idPlan integer,
    idUser integer,
    constraint pk_subscription primary key (idUser, idPlan),
    constraint fk_subscription_user foreign key(idUser) references sp_user(idUser),
    constraint fk_subscription_plan foreign key(idPlan) references sp_plan(idPlan)
);
create table sp_containsPD (
    idPlan integer,
    idDailyWorkout integer,
    constraint pk_containsPD primary key (idDailyWorkout, idPlan),
    constraint fk_containsPD_dailyWorkout foreign key(idDailyWorkout) references sp_dailyWorkout(idDailyWorkout),
    constraint fk_containsPD_plan foreign key(idPlan) references sp_plan(idPlan)
);
create table sp_containsDW (
    idDailyWorkout integer,
    idWorkout integer,
    constraint pk_containsDW primary key (idDailyWorkout, idWorkout),
    constraint fk_containsDW_dailyWorkout foreign key(idDailyWorkout) references sp_dailyWorkout(idDailyWorkout),
    constraint fk_containsDW_workout foreign key(idWorkout) references sp_workout(idWorkout)
);
create table sp_containsWE (
    idWorkout integer,
    idExercise integer,
    constraint pk_containsWE primary key (idExercise, idWorkout),
    constraint fk_containsWE_exercise foreign key(idExercise) references sp_exercise(idExercise),
    constraint fk_containsWE_workout foreign key(idWorkout) references sp_workout(idWorkout)
);

create sequence seq_user;
create sequence seq_post;
create sequence seq_comment;
create sequence seq_location;
create sequence seq_exercise;
create sequence seq_dailyworkout;
create sequence seq_workout;
create sequence seq_plan;

ALTER TABLE sp_user
add constraint uq_username unique(username);
