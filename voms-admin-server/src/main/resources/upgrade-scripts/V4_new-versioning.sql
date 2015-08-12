create table audit_event (event_id bigint not null auto_increment, principal varchar(255) not null, event_timestamp datetime not null, event_type varchar(255) not null, primary key (event_id)) ENGINE=InnoDB
create table audit_event_data (event_id bigint not null, name varchar(255) not null, value varchar(255) not null, primary key (event_id, name, value)) ENGINE=InnoDB
alter table audit_event_data add index FK8FEF1913C6FD77C9 (event_id), add constraint FK8FEF1913C6FD77C9 foreign key (event_id) references audit_event (event_id)
alter table req add column explanation varchar(512)
alter table req add column user_message varchar(512)
alter table sign_aup_task add column last_notification_time datetime
alter table usr add column orgdb_id bigint
create index aed_value_idx on audit_event_data(value)
create index aed_name_idx on audit_event_data(name)