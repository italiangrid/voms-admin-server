/* Fix AUDIT log table */
alter table audit_event_data modify value varchar(512)
/* Drop not null constraint from usr.end_time */
alter table usr modify column end_time datetime;
/* Create notification persistence tables */
create table notification (id bigint not null auto_increment, creation_time datetime not null, handler_id varchar(255), message text not null, messageType varchar(512) not null, status varchar(255) not null, subject text not null, primary key (id)) ENGINE=InnoDB
create table notification_delivery (id bigint not null auto_increment, delivery_timestamp datetime not null, error_message text, handler_id varchar(255) not null, status varchar(255) not null, notification_id bigint not null, primary key (id)) ENGINE=InnoDB
create table notification_recipients (notification_id bigint not null, element varchar(255)) ENGINE=InnoDB
create index notification_msg_type_idx on notification (messageType)
create index notification_status_idx on notification (status)
create index nd_status_idx on notification_delivery (status)
create index nd_handler_id_idx on notification_delivery (handler_id)
alter table notification_delivery add index FK9FC39CC89FD1FCA6 (notification_id), add constraint FK9FC39CC89FD1FCA6 foreign key (notification_id) references notification (id)
alter table notification_recipients add index FK685E6D4E9FD1FCA6 (notification_id), add constraint FK685E6D4E9FD1FCA6 foreign key (notification_id) references notification (id)
