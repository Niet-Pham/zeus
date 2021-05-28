-- auto-generated definition
create table receive_notification
(
    receive_notification_id int auto_increment
        primary key,
    account_id              int      null,
    send_notification_id    int      null,
    status                  int(1)   null,
    status_bell             int(1)   null,
    update_by               int      null,
    update_date             datetime null,
    create_by               int      null,
    create_date             datetime null
);

create table send_notification
(
    send_notification_id int auto_increment
        primary key,
    account_id           int          null,
    content              varchar(200) null,
    status               int(1)       null,
    update_by            int          null,
    update_date          datetime     null,
    create_by            int          null,
    create_date          datetime     null
);
