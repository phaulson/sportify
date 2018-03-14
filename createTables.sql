drop table sp_user cascade constraints;
drop table sp_post cascade constraints;
drop table sp_comment cascade constraints;
drop table sp_location cascade constraints;
drop table sp_follows cascade constraints;
drop table sp_exercise cascade constraints;
drop table sp_workout cascade constraints;
drop table sp_dailyWorkout cascade constraints;
drop table sp_plan cascade constraints;
drop table sp_subscription cascade constraints;
drop table sp_containsPD cascade constraints;
drop table sp_containsDW cascade constraints;
drop table sp_containsWE cascade constraints;
drop table sp_likes cascade constraints;

drop sequence seq_user;
drop sequence seq_post;
drop sequence seq_comment;
drop sequence seq_location;
drop sequence seq_exercise;
drop sequence seq_dailyworkout;
drop sequence seq_workout;
drop sequence seq_plan;

create table sp_user
(
    idUser integer Primary Key,
    username varchar2(20) unique,
    password varchar2(20),
    biographie varchar2(500),
    isPro number(1,0)
);
create table sp_post
(
    idPost integer Primary Key,
    idCreator integer,
    caption varchar2(500),
    timestamp timestamp,
    constraint fk_post_user foreign key(idCreator) references sp_user(idUser)
);
create table sp_comment
(
    idComment integer primary key,
    idCreator integer,
    idPost integer,
    content varchar2(100),
    timestamp timestamp,
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
    starttimestamptime timestamp,
    endtimestamptime timestamp,
    constraint fk_location_user foreign key(idUser) references sp_user(idUser),
    constraint ck_location check(starttimestamptime < endtimestamptime and 
      ((starttimestamptime is null and endtimestamptime is null and type not like 'EVENT') or
      (starttimestamptime is not null and endtimestamptime is not null and type like 'EVENT')))
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
    idCreator integer,
    name varchar2(50),
    description varchar2(500),
    constraint fk_exercise_creator foreign key(idCreator) references sp_user(idUser)
);
create table sp_workout (
    idWorkout integer primary key,
    idCreator integer,
    name varchar2(50),
    constraint fk_workout_creator foreign key(idCreator) references sp_user(idUser)
);
create table sp_dailyWorkout (
    idDailyWorkout integer primary key,
    idCreator integer,
    name varchar2(50),
    constraint fk_dailyworkout_creator foreign key(idCreator) references sp_user(idUser)
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
create table sp_likes
(
  idPost number,
  idUser number,
  constraint fk_likes_post foreign key(idPost) references sp_post(idPost),
  constraint fk_likes_user foreign key(idUser) references sp_user(idUser),
  constraint pk_likes primary key(idpost, iduser)
);

create or replace view sp_revPost as
select * from sp_post order by idPost desc;

create sequence seq_user;
create sequence seq_post;
create sequence seq_comment;
create sequence seq_location;
create sequence seq_exercise;
create sequence seq_dailyworkout;
create sequence seq_workout;
create sequence seq_plan;

commit;
