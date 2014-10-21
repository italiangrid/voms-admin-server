alter table m drop column cid cascade constraints
drop table capabilities cascade constraints
drop table tag_mapping cascade constraints
drop table tags cascade constraints
drop table memb_req cascade constraints
drop table seqnumber cascade constraints
create table managers (id number(19,0) not null, description varchar2(255 char) not null, email_address varchar2(255 char) not null, name varchar2(255 char) not null unique, primary key (id))
create table managers_groups (manager_id number(19,0) not null, group_id number(19,0) not null, primary key (manager_id, group_id))
create table periodic_notifications (id number(19,0) not null, lastNotificationTime timestamp not null, notificationType varchar2(255 char) not null unique, primary key (id))
alter table req add approver_ca varchar2(255 char)
alter table req add approver_dn varchar2(255 char)
alter table managers_groups add constraint FK8F5E11CDFCFA8B04 foreign key (group_id) references groups
alter table managers_groups add constraint FK8F5E11CDABC81A12 foreign key (manager_id) references managers
create sequence VOMS_GRPMAN_SEQ
create sequence VOMS_PER_NOT_SEQ
drop sequence VOMS_TAG_SEQ
drop sequence VOMS_TAG_MAP_SEQ