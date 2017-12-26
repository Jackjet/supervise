i integer;
begin
select count(*) into i from user_tables where table_name='AP_ARTICLE';
 if i=0 then
 execute immediate '
create table AP_ARTICLE
(
  id             VARCHAR2(32) not null,
  title          VARCHAR2(200),
  content        CLOB,
  creator        VARCHAR2(32),
  create_time    DATE default SYSDATE,
  type           VARCHAR2(32),
  attatch_blobid VARCHAR2(32),
  img_url        VARCHAR2(100),
  valid_date     DATE default SYSDATE,
  expire_date    DATE,
  sno            VARCHAR2(32),
  author         VARCHAR2(32),
  wrote_time     DATE default SYSDATE,
  mendor         VARCHAR2(32),
  mend_time      DATE default SYSDATE,
  href           VARCHAR2(200),
  review         VARCHAR2(1024)
)';
 end if;
select count(*) into i from user_tables where table_name='AP_ARTICLE_PORTLET';
 if i=0 then
 execute immediate '
create table AP_ARTICLE_PORTLET
(
  pg_plet_id     NUMBER not null,
  article_id     VARCHAR2(32) not null,
  portlet_id     VARCHAR2(32),
  pub_time       DATE default sysdate,
  check_status   CHAR(1) default ''0'',
  visit_capacity NUMBER default 0 not null
)
';
 end if;
select count(*) into i from user_tables where table_name='AP_ARTICLE_ATTACH';
 if i=0 then
 execute immediate '
create table AP_ARTICLE_ATTACH
(
  attach_id      VARCHAR2(30) not null,
  article_id     VARCHAR2(50),
  attach_name    VARCHAR2(100),
  attach_content BLOB
)';
 end if;
select count(*) into i from user_tables where table_name='ZX_BQ_DWZXL';
 if i=0 then
 execute immediate '
create table ZX_BQ_DWZXL
(
  fiscal   VARCHAR2(255),
  fis_perd VARCHAR2(255),
  dw       VARCHAR2(255),
  dw_jc    VARCHAR2(255),
  ys       NUMBER,
  byzc     NUMBER,
  byzx     NUMBER,
  syzc     NUMBER,
  syzx     NUMBER
)';
 end if;
select count(*) into i from user_tables where table_name='AP_PAGE_PORTLET';
 if i=0 then
 execute immediate '
create table AP_PAGE_PORTLET
(
  id                 NUMBER(16) not null,
  page_id            VARCHAR2(100) not null,
  layout_id          VARCHAR2(32) not null,
  reg_id             VARCHAR2(32) not null,
  row_no             NUMBER(16),
  col_no             NUMBER(16),
  row_span           NUMBER(16),
  col_span           NUMBER(16),
  col_ratio          NUMBER(16,2),
  portlet_id         VARCHAR2(32) not null,
  title              VARCHAR2(200),
  title_style        VARCHAR2(400),
  title_font_size    NUMBER,
  title_font_color   VARCHAR2(32),
  title_bg_color     VARCHAR2(32),
  title_bg_img       VARCHAR2(200),
  title_font         VARCHAR2(32),
  content_style      VARCHAR2(400),
  content_font_size  NUMBER,
  content_font_color VARCHAR2(32),
  content_bg_color   VARCHAR2(32),
  content_bg_img     VARCHAR2(200),
  content_font       VARCHAR2(32),
  record_size        NUMBER,
  border             NUMBER,
  ord_index          NUMBER default 0 not null,
  rowno              NUMBER,
  portlet_height     NUMBER default 0,
  tab_index          NUMBER default 0,
  tab_sign           VARCHAR2(32)
)';
 end if;
select count(*) into i from user_tables where table_name='AP_PAGE_TAB_PORTLET';
 if i=0 then
 execute immediate '
create table AP_PAGE_TAB_PORTLET
(
  id             NUMBER not null,
  page_id        VARCHAR2(100),
  tab_sign       VARCHAR2(32) not null,
  portlet_id     VARCHAR2(32) not null,
  title          VARCHAR2(200),
  title_style    VARCHAR2(400),
  content_style  VARCHAR2(400),
  content_bg_img VARCHAR2(200),
  ord_index      NUMBER not null,
  portlet_height NUMBER default ''0'',
  record_size    NUMBER,
  border         NUMBER
)';
 end if;
select count(*) into i from user_tables where table_name='AP_VISIT_CAPACITY';
 if i=0 then
 execute immediate '
create table AP_VISIT_CAPACITY
(
  guid          NUMBER not null,
  pg_plet_id    NUMBER,
  article_title VARCHAR2(200),
  user_id       VARCHAR2(30),
  user_ip       VARCHAR2(15),
  visit_date    DATE default SYSDATE,
  article_id    VARCHAR2(32)
)';
end if;
execute immediate ' CREATE OR REPLACE VIEW V_AP_ARTICLE_PUB AS
SELECT AAP."PG_PLET_ID",AAP."ARTICLE_ID",AAP."PORTLET_ID",AAP."PUB_TIME",AAP."CHECK_STATUS",AAP."VISIT_CAPACITY", AA.CREATOR AS USER_ID, AA.TITLE AS ARTICLE_TITLE, AA.VALID_DATE,
 AA.EXPIRE_DATE, AA.IMG_URL, AA.HREF, AA.REVIEW, AA.CONTENT,
 APP.TITLE AS PORTLET_NAME, APP.PAGE_ID
 FROM AP_ARTICLE_PORTLET AAP, AP_ARTICLE AA, AP_PAGE_PORTLET APP
 WHERE AAP.ARTICLE_ID = AA.ID AND AAP.PG_PLET_ID = APP.ID
 UNION ALL
 SELECT AAP."PG_PLET_ID",AAP."ARTICLE_ID",AAP."PORTLET_ID",AAP."PUB_TIME",AAP."CHECK_STATUS",AAP."VISIT_CAPACITY", AA.CREATOR AS USER_ID, AA.TITLE AS ARTICLE_TITLE, AA.VALID_DATE,
 AA.EXPIRE_DATE, AA.IMG_URL, AA.HREF, AA.REVIEW, AA.CONTENT,
 APTP.TITLE AS PORTLET_NAME, APTP.PAGE_ID
 FROM AP_ARTICLE_PORTLET AAP, AP_ARTICLE AA, AP_PAGE_TAB_PORTLET APTP
 WHERE AAP.ARTICLE_ID = AA.ID AND AAP.PG_PLET_ID = APTP.ID';
-- Create/Recreate primary, unique and foreign key constraints 
execute immediate ' alter table AP_VISIT_CAPACITY
  add constraint PK_AP_VISIT_CAPACITY primary key (GUID)';



