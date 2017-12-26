i integer;
begin
select count(*) into i from user_tables where table_name='sys_element_bak
';
 if i=0 then
 execute immediate '


create table sys_element_bak
(
  chr_id         VARCHAR2(38) not null,
  set_year       NUMBER(4) not null,
  ele_source     VARCHAR2(42) not null,
  ele_code       VARCHAR2(42),
  ele_name       VARCHAR2(60),
  enabled        NUMBER(1) default 1 not null,
  dispmode       NUMBER(2) default 0,
  ref_mode       NUMBER(2) default 0,
  is_rightfilter NUMBER(1) default 0,
  max_level      NUMBER(2),
  code_rule      VARCHAR2(60),
  level_name     VARCHAR2(200),
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  latest_op_user VARCHAR2(42),
  is_deleted     NUMBER default 0 not null,
  is_nolevel     NUMBER default 0 not null,
  is_local       NUMBER(1) default 0,
  is_system      NUMBER(1) default 0,
  ele_type       NUMBER(1),
  is_view        NUMBER(1) default 0,
  czgb_code      VARCHAR2(42),
  last_ver       VARCHAR2(30),
  disp_order     NUMBER(9),
  sys_id         VARCHAR2(42),
  is_operate     NUMBER(1),
  rg_code        VARCHAR2(42) not null
)';
execute immediate 'alter table sys_element add old_ele_source varchar(255)';
execute immediate 'alter table sys_element add old_ele_code varchar(255)';
execute immediate 'alter table sys_element add old_ele_name varchar(255)';
execute immediate 'alter table sys_element add is_change char(1) default ''0''';
end if;
