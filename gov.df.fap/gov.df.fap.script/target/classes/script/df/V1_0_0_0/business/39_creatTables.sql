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
execute immediate 'comment on column AP_LINK_PORTLET.pg_plet_id is ''Ƶ��ID''';
execute immediate 'comment on column AP_LINK_PORTLET.link_guid is ''AP_LINK���GUID''';
execute immediate 'comment on column AP_LINK_PORTLET.role_id is ''��Ȩ����ɫ��*�ǺŴ������н�ɫ''';
execute immediate 'comment on column AP_LINK_PORTLET.emp_code is ''��Ȩ���ˣ�*�ǺŴ����κ���''';
execute immediate 'comment on column AP_LINK_PORTLET.co_code is ''��Ȩ����λ��*�ǺŴ������е�λ''';
