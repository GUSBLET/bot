create table if not exists lecture_replacement
(
    id                          bigserial primary key,
    telegram_id               bigint,
    class                       varchar(20) not null,
    current_lecture             varchar(50) not null,
    current_teacher_lecture     varchar(50) not null,
    replacement_lecture         varchar(50) not null,

);


