alter table m drop foreign key fk_m_cap;
alter table m drop column cid;
drop table capabilities;
drop table tag_mapping;
drop table tags;
drop table memb_req;
drop table seqnumber;
create table managers (id bigint not null auto_increment, description varchar(255) not null, email_address varchar(255) not null, name varchar(255) not null unique, primary key (id)) type=InnoDB
create table managers_groups (manager_id bigint not null, group_id bigint not null, primary key (manager_id, group_id)) type=InnoDB
create table periodic_notifications (id bigint not null auto_increment, lastNotificationTime datetime not null, notificationType varchar(255) not null unique, primary key (id)) type=InnoDB
alter table req add column approver_ca varchar(255)
alter table req add column approver_dn varchar(255)
alter table managers_groups add index FK8F5E11CDFCFA8B04 (group_id), add constraint FK8F5E11CDFCFA8B04 foreign key (group_id) references groups (gid)
alter table managers_groups add index FK8F5E11CDABC81A12 (manager_id), add constraint FK8F5E11CDABC81A12 foreign key (manager_id) references managers (id)