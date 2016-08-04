alter table audit_event_data modify value varchar(512)
create index ae_type_idx on audit_event (event_timestamp, event_type)
create index ae_principal_idx on audit_event (principal, event_timestamp)