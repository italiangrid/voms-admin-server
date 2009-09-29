create table aup (id bigint not null auto_increment, name varchar(255) not null unique, description varchar(255), reacceptancePeriod integer not null, primary key (id)) type=InnoDB
create table aup_acc_record (id bigint not null auto_increment, aup_version_id bigint not null, usr_id bigint not null, last_acceptance_date datetime not null, primary key (id), unique (aup_version_id, usr_id)) type=InnoDB
create table aup_version (id bigint not null auto_increment, aup_id bigint not null, version varchar(255) not null, url varchar(255), text varchar(255), creationTime datetime not null, lastForcedReacceptanceTime datetime, active bit not null, primary key (id), unique (aup_id, version)) type=InnoDB
alter table ca add column subject_string varchar(255) unique
alter table ca add column description varchar(255)
alter table ca add column creation_time datetime
create table certificate (id bigint not null auto_increment, creation_time datetime not null, subject_string varchar(255) not null, suspended bit not null, suspended_reason varchar(255), suspension_reason_code varchar(255), ca_id smallint not null, usr_id bigint not null, primary key (id), unique (ca_id, subject_string)) type=InnoDB
create table certificate_request (certificate tinyblob, certificateIssuer varchar(255) not null, certificateSubject varchar(255) not null, request_id bigint not null, primary key (request_id)) type=InnoDB
create table group_membership_req (groupName varchar(255) not null, request_id bigint not null, primary key (request_id)) type=InnoDB
alter table groups add column description varchar(255)
alter table groups add column restricted bit
create table membership_rem_req (reason varchar(255) not null, request_id bigint not null, primary key (request_id)) type=InnoDB
create table personal_info (id bigint not null auto_increment, value varchar(255), visible bit, personal_info_type_id bigint not null, primary key (id)) type=InnoDB
create table personal_info_type (id bigint not null auto_increment, description varchar(255), type varchar(255) not null unique, primary key (id)) type=InnoDB
create table req (request_id bigint not null auto_increment, completionDate datetime, creationDate datetime, expirationDate datetime, status varchar(255) not null, requester_info_id bigint not null, primary key (request_id), unique (requester_info_id)) type=InnoDB
create table requester_info (id bigint not null auto_increment, address varchar(255), certificateIssuer varchar(255) not null, certificateSubject varchar(255) not null, emailAddress varchar(255) not null, institution varchar(255), name varchar(255), phoneNumber varchar(255), surname varchar(255), voMember bit, primary key (id)) type=InnoDB
create table requester_personal_info (requester_id bigint not null, pi_value varchar(255), pi_key varchar(255), primary key (requester_id, pi_key)) type=InnoDB
create table role_membership_req (groupName varchar(255), roleName varchar(255), request_id bigint not null, primary key (request_id)) type=InnoDB
create table sign_aup_task (task_id bigint not null, aup_id bigint not null, primary key (task_id)) type=InnoDB
create table tag_mapping (mapping_id bigint not null auto_increment, tag_id bigint not null, gid bigint not null, rid bigint, admin_id bigint not null, primary key (mapping_id), unique (tag_id, gid, rid, admin_id)) type=InnoDB
create table tags (id bigint not null auto_increment, name varchar(255) not null unique, implicit bit not null, permissions integer not null, permissionsOnPath integer, primary key (id)) type=InnoDB
create table task (task_id bigint not null auto_increment, completionDate datetime, creationDate datetime, expiryDate datetime, status varchar(255) not null, admin_id bigint, task_type_id bigint not null, usr_id bigint, primary key (task_id)) type=InnoDB
create table task_log_record (id bigint not null auto_increment, adminDn varchar(255), creation_time datetime not null, event varchar(255) not null, userDn varchar(255), task_id bigint not null, primary key (id)) type=InnoDB
create table task_type (id bigint not null auto_increment, description varchar(255), name varchar(255) not null unique, primary key (id)) type=InnoDB
create table user_request_task (task_id bigint not null, req_id bigint not null, primary key (task_id)) type=InnoDB
alter table usr add column address varchar(255)
alter table usr add column creation_time datetime
alter table usr add column email_address varchar(255)
alter table usr add column end_time datetime
alter table usr add column institution varchar(255)
alter table usr add column name varchar(255)
alter table usr add column phone_number varchar(255)
alter table usr add column surname varchar(255)
alter table usr add column suspended bit
alter table usr add column suspension_reason varchar(255)
alter table usr add column suspension_reason_code varchar(255)
alter table version add column admin_version varchar(255)
create table vo_membership_req (confirmId varchar(255) not null, request_id bigint not null, primary key (request_id)) type=InnoDB
alter table aup_acc_record add index FKB1979B325208DD8 (usr_id), add constraint FKB1979B325208DD8 foreign key (usr_id) references usr (userid)
alter table aup_acc_record add index FKB1979B32A8A54F89 (aup_version_id), add constraint FKB1979B32A8A54F89 foreign key (aup_version_id) references aup_version (id)
alter table aup_version add index fk_aup_version_aup (aup_id), add constraint fk_aup_version_aup foreign key (aup_id) references aup (id) on delete cascade
alter table certificate add index FK745F41975208DD8 (usr_id), add constraint FK745F41975208DD8 foreign key (usr_id) references usr (userid)
alter table certificate add index FK745F4197C537E901 (ca_id), add constraint FK745F4197C537E901 foreign key (ca_id) references ca (cid)
alter table certificate_request add index FK47CA53E7AD152A33 (request_id), add constraint FK47CA53E7AD152A33 foreign key (request_id) references req (request_id)
alter table group_membership_req add index FKBD145E75AD152A33 (request_id), add constraint FKBD145E75AD152A33 foreign key (request_id) references req (request_id)
alter table membership_rem_req add index FK1877BC10AD152A33 (request_id), add constraint FK1877BC10AD152A33 foreign key (request_id) references req (request_id)
alter table personal_info add index FK229FDF4DAE536B0B (personal_info_type_id), add constraint FK229FDF4DAE536B0B foreign key (personal_info_type_id) references personal_info_type (id)
alter table req add index FK1B89E8516AAEC (requester_info_id), add constraint FK1B89E8516AAEC foreign key (requester_info_id) references requester_info (id)
alter table requester_personal_info add index FK7E3D7FCA9698DB21 (requester_id), add constraint FK7E3D7FCA9698DB21 foreign key (requester_id) references requester_info (id)
alter table role_membership_req add index FK3B9C79EAD152A33 (request_id), add constraint FK3B9C79EAD152A33 foreign key (request_id) references req (request_id)
alter table sign_aup_task add index FK7FCB416AFA2EA7DB (task_id), add constraint FK7FCB416AFA2EA7DB foreign key (task_id) references task (task_id)
alter table sign_aup_task add index FK7FCB416AE482CB72 (aup_id), add constraint FK7FCB416AE482CB72 foreign key (aup_id) references aup (id)
alter table tag_mapping add index fk_tag_mapping_roles (rid), add constraint fk_tag_mapping_roles foreign key (rid) references roles (rid) on delete cascade
alter table tag_mapping add index fk_tag_mapping_admins (admin_id), add constraint fk_tag_mapping_admins foreign key (admin_id) references admins (adminid) on delete cascade
alter table tag_mapping add index fk_tag_mapping_groups (gid), add constraint fk_tag_mapping_groups foreign key (gid) references groups (gid) on delete cascade
alter table tag_mapping add index fk_tag_mapping_tag (tag_id), add constraint fk_tag_mapping_tag foreign key (tag_id) references tags (id) on delete cascade
alter table task add index FK363585FFC02DA6 (task_type_id), add constraint FK363585FFC02DA6 foreign key (task_type_id) references task_type (id)
alter table task add index FK3635855208DD8 (usr_id), add constraint FK3635855208DD8 foreign key (usr_id) references usr (userid)
alter table task add index FK3635856C2379D3 (admin_id), add constraint FK3635856C2379D3 foreign key (admin_id) references admins (adminid)
alter table task_log_record add index FK77673CA6FA2EA7DB (task_id), add constraint FK77673CA6FA2EA7DB foreign key (task_id) references task (task_id)
alter table user_request_task add index FKACB7D29FA2EA7DB (task_id), add constraint FKACB7D29FA2EA7DB foreign key (task_id) references task (task_id)
alter table user_request_task add index FKACB7D29732B75C4 (req_id), add constraint FKACB7D29732B75C4 foreign key (req_id) references req (request_id)
alter table usr add index FK1C594BF9754C3 (ca), add constraint FK1C594BF9754C3 foreign key (ca) references ca (cid)
alter table vo_membership_req add index FK28EE8AFBAD152A33 (request_id), add constraint FK28EE8AFBAD152A33 foreign key (request_id) references req (request_id)