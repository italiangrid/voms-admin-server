--
-- Copyright (c) Members of the EGEE Collaboration. 2006-2009.
-- See http://www.eu-egee.org/partners/ for details on the copyright holders.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
-- Authors:
-- 	Andrea Ceccanti (INFN)
--
create table aup (id number(19,0) not null, name varchar2(255 char) not null unique, description varchar2(255 char), reacceptancePeriod number(10,0) not null, primary key (id))
create table aup_acc_record (id number(19,0) not null, aup_version_id number(19,0) not null, usr_id number(19,0) not null, last_acceptance_date timestamp not null, primary key (id), unique (aup_version_id, usr_id))
create table aup_version (id number(19,0) not null, aup_id number(19,0) not null, version varchar2(255 char) not null, url varchar2(255 char), text varchar2(255 char), creationTime timestamp not null, lastForcedReacceptanceTime timestamp, active number(1,0) not null, primary key (id), unique (aup_id, version))
alter table ca add subject_string varchar2(255 char) unique
alter table ca add description varchar2(255 char)
alter table ca add creation_time timestamp
create table certificate (id number(19,0) not null, creation_time timestamp not null, subject_string varchar2(255 char) not null, suspended number(1,0) not null, suspended_reason varchar2(255 char), suspension_reason_code varchar2(255 char), ca_id number(5,0) not null, usr_id number(19,0) not null, primary key (id), unique (ca_id, subject_string))
create table certificate_request (certificate raw(255), certificateIssuer varchar2(255 char) not null, certificateSubject varchar2(255 char) not null, request_id number(19,0) not null, primary key (request_id))
create table group_membership_req (groupName varchar2(255 char) not null, request_id number(19,0) not null, primary key (request_id))
alter table groups add description varchar2(255 char)
alter table groups add restricted number(1,0)
create table membership_rem_req (reason varchar2(255 char) not null, request_id number(19,0) not null, primary key (request_id))
create table personal_info (id number(19,0) not null, value varchar2(255 char), visible number(1,0), personal_info_type_id number(19,0) not null, primary key (id))
create table personal_info_type (id number(19,0) not null, description varchar2(255 char), type varchar2(255 char) not null unique, primary key (id))
create table req (request_id number(19,0) not null, completionDate timestamp, creationDate timestamp, expirationDate timestamp, status varchar2(255 char) not null, requester_info_id number(19,0) not null, primary key (request_id), unique (requester_info_id))
create table requester_info (id number(19,0) not null, address varchar2(255 char), certificateIssuer varchar2(255 char) not null, certificateSubject varchar2(255 char) not null, emailAddress varchar2(255 char) not null, institution varchar2(255 char), name varchar2(255 char), phoneNumber varchar2(255 char), surname varchar2(255 char), voMember number(1,0), primary key (id))
create table requester_personal_info (requester_id number(19,0) not null, pi_value varchar2(255 char), pi_key varchar2(255 char), primary key (requester_id, pi_key))
create table role_membership_req (groupName varchar2(255 char), roleName varchar2(255 char), request_id number(19,0) not null, primary key (request_id))
create table sign_aup_task (task_id number(19,0) not null, aup_id number(19,0) not null, primary key (task_id))
create table tag_mapping (mapping_id number(19,0) not null, tag_id number(19,0) not null, gid number(19,0) not null, rid number(19,0), admin_id number(19,0) not null, primary key (mapping_id), unique (tag_id, gid, rid, admin_id))
create table tags (id number(19,0) not null, name varchar2(255 char) not null unique, implicit number(1,0) not null, permissions number(10,0) not null, permissionsOnPath number(10,0), primary key (id))
create table task (task_id number(19,0) not null, completionDate timestamp, creationDate timestamp, expiryDate timestamp, status varchar2(255 char) not null, admin_id number(19,0), task_type_id number(19,0) not null, usr_id number(19,0), primary key (task_id))
create table task_log_record (id number(19,0) not null, adminDn varchar2(255 char), creation_time timestamp not null, event varchar2(255 char) not null, userDn varchar2(255 char), task_id number(19,0) not null, primary key (id))
create table task_type (id number(19,0) not null, description varchar2(255 char), name varchar2(255 char) not null unique, primary key (id))
create table user_request_task (task_id number(19,0) not null, req_id number(19,0) not null, primary key (task_id))
alter table usr add address varchar2(255 char)
alter table usr add creation_time timestamp
alter table usr add email_address varchar2(255 char)
alter table usr add end_time timestamp
alter table usr add institution varchar2(255 char)
alter table usr add name varchar2(255 char)
alter table usr add phone_number varchar2(255 char)
alter table usr add surname varchar2(255 char)
alter table usr add suspended number(1,0)
alter table usr add suspension_reason varchar2(255 char)
alter table usr add suspension_reason_code varchar2(255 char)
alter table version add admin_version varchar2(255 char)
create table vo_membership_req (confirmId varchar2(255 char) not null, request_id number(19,0) not null, primary key (request_id))
alter table aup_acc_record add constraint FKB1979B325208DD8 foreign key (usr_id) references usr
alter table aup_acc_record add constraint FKB1979B32A8A54F89 foreign key (aup_version_id) references aup_version
alter table aup_version add constraint fk_aup_version_aup foreign key (aup_id) references aup on delete cascade
alter table certificate add constraint FK745F41975208DD8 foreign key (usr_id) references usr
alter table certificate add constraint FK745F4197C537E901 foreign key (ca_id) references ca
alter table certificate_request add constraint FK47CA53E7AD152A33 foreign key (request_id) references req
alter table group_membership_req add constraint FKBD145E75AD152A33 foreign key (request_id) references req
alter table membership_rem_req add constraint FK1877BC10AD152A33 foreign key (request_id) references req
alter table personal_info add constraint FK229FDF4DAE536B0B foreign key (personal_info_type_id) references personal_info_type
alter table req add constraint FK1B89E8516AAEC foreign key (requester_info_id) references requester_info
alter table requester_personal_info add constraint FK7E3D7FCA9698DB21 foreign key (requester_id) references requester_info
alter table role_membership_req add constraint FK3B9C79EAD152A33 foreign key (request_id) references req
alter table sign_aup_task add constraint FK7FCB416AFA2EA7DB foreign key (task_id) references task
alter table sign_aup_task add constraint FK7FCB416AE482CB72 foreign key (aup_id) references aup
alter table tag_mapping add constraint fk_tag_mapping_roles foreign key (rid) references roles on delete cascade
alter table tag_mapping add constraint fk_tag_mapping_admins foreign key (admin_id) references admins on delete cascade
alter table tag_mapping add constraint fk_tag_mapping_groups foreign key (gid) references groups on delete cascade
alter table tag_mapping add constraint fk_tag_mapping_tag foreign key (tag_id) references tags on delete cascade
alter table task add constraint FK363585FFC02DA6 foreign key (task_type_id) references task_type
alter table task add constraint FK3635855208DD8 foreign key (usr_id) references usr
alter table task add constraint FK3635856C2379D3 foreign key (admin_id) references admins
alter table task_log_record add constraint FK77673CA6FA2EA7DB foreign key (task_id) references task
alter table user_request_task add constraint FKACB7D29FA2EA7DB foreign key (task_id) references task
alter table user_request_task add constraint FKACB7D29732B75C4 foreign key (req_id) references req
alter table vo_membership_req add constraint FK28EE8AFBAD152A33 foreign key (request_id) references req