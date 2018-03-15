insert into sp_user values(seq_user.nextval, 'money boy', 'swag', '1 fly dude', 0);
insert into sp_user values(seq_user.nextval, 'phauli', 'pmm', 'lol', 0);
insert into sp_user values(seq_user.nextval, 'samsi', 'swag', 'shesh', 0);
insert into sp_user values(seq_user.nextval, 'gabi', 'lohl', 'lfc <3', 0);
insert into sp_user values(seq_user.nextval, 'simsiboi', 'swag', 'skrr', 0);
insert into sp_user values(seq_user.nextval, 'martino@email.com', 'lol', 'halo i bims, 1 spasd', 1);
insert into sp_user values(seq_user.nextval, 'cracky', 'moni123', 'lali bringt die milch zum grinsen', 1);
insert into sp_user values(seq_user.nextval, 'nignag', 'ilovescissors', 'candiman/swaggerman/mcniggo/nignag/cafelattetyp', 0);
insert into sp_user values(seq_user.nextval, 'christian christian christian', 'c++4life', 'hackerman, c++ developer', 0);
insert into sp_user values(seq_user.nextval, 'jugo der kein deutsch kann', 'chelsea', 'blue is the color', 0);
insert into sp_user values(seq_user.nextval, 'cora deborah rapgod', 'rapstar123', 'there is no better mc than me', 0);
insert into sp_user values(seq_user.nextval, 'jugo der deutsch kann', 'krajic99', 'my parents dont know how to spell christian lol', 0);
insert into sp_user values(seq_user.nextval, 'valone', 'milfs<3', 'milfhunter', 0);
insert into sp_user values(seq_user.nextval, 'flo_mikula', 'burr', 'schule -> unnötig', 0);
insert into sp_user values(seq_user.nextval, 'blaschke', 'iloveasians', 'i love asian women', 0);
insert into sp_user values(seq_user.nextval, 'judth', 'eishockey', '17 | eishockey | vsv', 0);
insert into sp_user values(seq_user.nextval, 'guenther', 'gmuend', 'always tired', 0);
insert into sp_user values(seq_user.nextval, 'klassen_mama', 'angie123', 'lol', 0);
insert into sp_user values(seq_user.nextval, 'meli_b', 'fuckschool', 'a cigarette a day keeps the doctor away', 0);
insert into sp_user values(seq_user.nextval, 'moni', 'cracky123', 'so rauschig war i noch nie', 0);

insert into sp_plan values(seq_plan.nextval, 2, 'low-carb');
insert into sp_plan values(seq_plan.nextval, 2, 'swagtransformation');
insert into sp_plan values(seq_plan.nextval, 3, 'dbi-pro-transformation');

insert into sp_dailyworkout values(seq_dailyworkout.nextval, 2, 'legs');
insert into sp_dailyworkout values(seq_dailyworkout.nextval, 2, 'chest');

insert into sp_workout values(seq_workout.nextval, 2, 'simple leg training');
insert into sp_workout values(seq_workout.nextval, 2, 'advanced leg training');
insert into sp_workout values(seq_workout.nextval, 2,'simple chest training');
insert into sp_workout values(seq_workout.nextval, 2,'advanced chest training');

insert into sp_exercise values(seq_exercise.nextval, 2, 'leg press', 'press the weight with your legs up and slowly down lol');
insert into sp_exercise values(seq_exercise.nextval, 2, 'treadmill', 'run on the treadmill with the simple settings');
insert into sp_exercise values(seq_exercise.nextval, 2, 'squats', 'simple squats');
insert into sp_exercise values(seq_exercise.nextval, 2, 'leg curls', 'push the weight up and slowly down while laying on your belly');
insert into sp_exercise values(seq_exercise.nextval, 2, 'bench press', 'press the weight  up and slowly down lol');
insert into sp_exercise values(seq_exercise.nextval, 2, 'butterfly', 'push the weight from the outside to the inside');
insert into sp_exercise values(seq_exercise.nextval, 2, 'push ups', 'simple push ups');
insert into sp_exercise values(seq_exercise.nextval, 2, 'dips', 'simple dips');

insert into sp_containspd values(2, 2);
insert into sp_containspd values(2, 3);

insert into sp_containsdw values(2, 3);
insert into sp_containsdw values(2, 2);
insert into sp_containsdw values(3, 4);
insert into sp_containsdw values(3, 5);

insert into sp_containswe values(2, 3);
insert into sp_containswe values(2, 4);
insert into sp_containswe values(3, 2);
insert into sp_containswe values(3, 5);
insert into sp_containswe values(4, 8);
insert into sp_containswe values(4, 9);
insert into sp_containswe values(5, 6);
insert into sp_containswe values(5, 7);

insert into sp_location values(seq_location.nextval, 7, 'injoy', 46.7172501,14.1012608, 'GYM', null, null);
insert into sp_location values(seq_location.nextval, 7, 'tennis training', 46.728492,14.0933622, 'EVENT', to_date('9.9.2012', 'dd.mm.yyyy'), to_date('10.10.2012', 'dd.mm.yyyy'));
insert into sp_location values(seq_location.nextval, 7, 'docs', 46.7253889, 14.0946264, 'GYM', null, null);
insert into sp_location values(seq_location.nextval, 4, 'leichtathletikzentrum feldkirchen', 46.727613,14.0928803, 'PUBLIC_PLACE', null, null);
insert into sp_location values(seq_location.nextval, 5, 'vordehnen praxis', 46.7231166,14.0935648, 'EVENT', to_date('16.3.2018', 'dd.mm.yyyy'), to_date('17.3.2018', 'dd.mm.yyyy'));
insert into sp_location values(seq_location.nextval, 5, 'vordehnen theorie', 46.7231166,14.0935648, 'EVENT', to_date('19.3.2018', 'dd.mm.yyyy'), to_date('20.3.2018', 'dd.mm.yyyy'));

insert into sp_post values(seq_post.nextVal, 5, 'fuck manU, they are shit', systimestamp);
insert into sp_post values(seq_post.nextVal, 7, 'does wanking count as training?', systimestamp);
insert into sp_post values(seq_post.nextVal, 5, 'manU is still shit lol', systimestamp);
insert into sp_post values(seq_post.nextVal, 5, 'lfc 4 life', systimestamp);
insert into sp_post values(seq_post.nextVal, 5, 'fuck man city, tey are op', systimestamp);
insert into sp_post values(seq_post.nextVal, 5, 'sieg heil', systimestamp);
insert into sp_post values(seq_post.nextVal, 5, 'tom ford belt an money boy der geldmann', systimestamp);
insert into sp_post values(seq_post.nextVal, 7, 'i wank about 7 times a day. when does my arm grow?', systimestamp);
insert into sp_post values(seq_post.nextVal, 11, 'chelsea fc - they have history', systimestamp);
insert into sp_post values(seq_post.nextVal, 11, 'fucking uefalona', systimestamp);

insert into sp_comment values(seq_comment.nextVal, 7, 2, 'what is a manU?', systimestamp);
insert into sp_comment values(seq_comment.nextVal, 2, 2, 'sheesh', systimestamp);
insert into sp_comment values(seq_comment.nextVal, 2, 3, 'swag', systimestamp);
insert into sp_comment values(seq_comment.nextVal, 8, 3, 'yes it does', systimestamp);
insert into sp_comment values(seq_comment.nextVal, 4, 3, 'skurr', systimestamp);

insert into sp_follow values(3,2);
insert into sp_follow values(4,2);
insert into sp_follow values(5,2);
insert into sp_follow values(6,2);
insert into sp_follow values(7,2);
insert into sp_follow values(5,3);
insert into sp_follow values(4,3);
insert into sp_follow values(2,6);
insert into sp_follow values(3,5);
insert into sp_follow values(3,7);

insert into sp_subscription values(2, 3);
insert into sp_subscription values(2, 4);
insert into sp_subscription values(3, 5);

insert into sp_like values(2,2);
insert into sp_like values(2,3);
insert into sp_like values(2,4);
insert into sp_like values(2,5);
insert into sp_like values(2,6);
insert into sp_like values(2,7);
insert into sp_like values(2,8);
insert into sp_like values(3,3);
insert into sp_like values(4,2);
insert into sp_like values(4,5);
insert into sp_like values(4,4);
