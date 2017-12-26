i integer;
begin
select count(*) into i from user_tables where table_name='AP_LINK';
 if i=0 then
 execute immediate '
create table AP_LINK
(
  guid         VARCHAR2(32) default sys_guid() not null,
  link_title   VARCHAR2(128),
  link_url     VARCHAR2(512),
  menu_id      VARCHAR2(256),
  link_img     VARCHAR2(256),
  ord_index    NUMBER(4) default 0,
  link_type    char(1) default 0,
  create_user    VARCHAR2(64) default ''sa'' not null,
  create_time    CHAR(19) default TO_CHAR(SYSDATE,''YYYY-MM-DD HH24:MI:SS'') not null
)';
 end if;
execute immediate 'alter table AP_LINK add constraint PK_AP_LINK primary key (GUID) using index';
execute immediate 'comment on column AP_LINK.link_type is ''0-图片链接；1-文字链接；2-图片和文字链接''';
execute immediate 'comment on column AP_LINK.link_img is ''示例：/download/html/gdps_60.png''';
execute immediate 'comment on column AP_LINK.link_url is ''示例：http://www.baidu.com''';
