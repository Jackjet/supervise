i integer;
begin
select count(*) into i from user_tables where table_name='AP_LINK_PORTLET';
 if i=0 then
 execute immediate '
create table AP_LINK_PORTLET
(
  pg_plet_id  VARCHAR2(64) not null,
  link_guid   VARCHAR2(128) not null,
  menu_id     VARCHAR2(128),
  emp_code    VARCHAR2(64) default ''*'' not null,
  role_id     VARCHAR2(64) default ''*'' not null,
  co_code     VARCHAR2(64) default ''*'' not null,
  pub_user    VARCHAR2(64) default ''sa'' not null,
  pub_time    CHAR(19) default TO_CHAR(SYSDATE,''YYYY-MM-DD HH24:MI:SS'') not null
)';
 end if;
execute immediate 'alter table AP_LINK_PORTLET
  add constraint PK_AP_LINK_PORTLET_K primary key (PG_PLET_ID, link_guid, EMP_CODE, ROLE_ID, CO_CODE)
  using index';
execute immediate 'comment on column AP_LINK_PORTLET.pg_plet_id is ''频道ID''';
execute immediate 'comment on column AP_LINK_PORTLET.link_guid is ''AP_LINK表的GUID''';
execute immediate 'comment on column AP_LINK_PORTLET.role_id is ''授权给角色，*星号代表所有角色''';
execute immediate 'comment on column AP_LINK_PORTLET.emp_code is ''授权给人，*星号代表任何人''';
execute immediate 'comment on column AP_LINK_PORTLET.co_code is ''授权给单位，*星号代表所有单位''';
