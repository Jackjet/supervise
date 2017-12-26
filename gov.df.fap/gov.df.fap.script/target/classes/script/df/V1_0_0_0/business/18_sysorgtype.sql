i integer;
begin
select count(*) into i from user_tables where table_name='SYS_USER_ORGTYPE';
 if i=0 then
 execute immediate '
create table SYS_USER_ORGTYPE
(
  user_id  VARCHAR2(40) not null,
  org_type VARCHAR2(40) not null,
  set_year VARCHAR2(4),
  last_ver VARCHAR2(30)
)';
  end if;

