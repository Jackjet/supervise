i integer;
begin
select count(*) into i from user_tables where table_name='BIS_CONFIG_BM';
 if i=0 then
 execute immediate '
create table BIS_CONFIG_BM
(
  AGENCY_ID    VARCHAR2(38) not null,
  RG_CODE  VARCHAR2(42) not null,
  set_year NUMBER(4) not null

  )';
  end if;
  
execute immediate 'comment on table BIS_CONFIG_BM
  is ''��Ŀ����ʽ���Ź���洢��''';
execute immediate 'comment on column BIS_CONFIG_BM.AGENCY_ID
  is ''����ID''';
execute immediate 'comment on column BIS_CONFIG_BM.RG_CODE
  is ''����''';
execute immediate 'comment on column BIS_CONFIG_BM.set_year
  is ''���''';
execute immediate 'alter table BIS_CONFIG_BM
  add constraint BIS_CONFIG_BM_PK primary key (AGENCY_ID)';



  
  
select count(*) into i from user_tables where table_name='BIS_CONFIG_PARAMETER';
 if i=0 then
 execute immediate '
create table BIS_CONFIG_PARAMETER
(
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42) not null,
  chr_name       VARCHAR2(60),
  chr_value      VARCHAR2(3999),
  chr_desc       VARCHAR2(200),
  sys_id         VARCHAR2(42),
  is_visible     NUMBER(1) default 0 not null,
  is_edit        NUMBER(1) default 0 not null,
  field_valueset VARCHAR2(200),
  field_disptype NUMBER(2) default 0,
  group_name     VARCHAR2(60),
  set_id         VARCHAR2(38),
  last_ver       VARCHAR2(30),
  RG_CODE        VARCHAR2(42) not null,
  set_year       NUMBER(4) not null
)';
end if;

execute immediate 'comment on table BIS_CONFIG_PARAMETER
  is ''��Ŀ����ʽ���ñ�''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.chr_id
  is ''Ψһ��ʶ����������Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.chr_code
  is ''��������''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.chr_name
  is ''��������''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.chr_value
  is ''����ֵ''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.chr_desc
  is ''����˵��''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.sys_id
  is ''����������������ϵͳ
 ���ձ�ʾ������ϵͳ���ã�
 ''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.is_visible
  is ''�Ƿ������û�����(1����,0������)''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.is_edit
  is ''�Ƿ������û��༭''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.field_valueset
  is ''�������ֶε�ȡֵ��Χ����/�ֿ�
 ��1/2/3/4/5/6/7/8��''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.field_disptype
  is ''������¼�ֶ���ʾ����''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.group_name
  is ''���ÿ����ϵͳ�������ڽ��й���''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.set_id
  is ''����ID''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.RG_CODE
  is ''����''';
execute immediate 'comment on column BIS_CONFIG_PARAMETER.set_year
  is ''���''';
execute immediate 'create index BIS_CONFIG_PARAMETER_N1 on BIS_CONFIG_PARAMETER (SYS_ID)';
execute immediate 'create index BIS_CONFIG_PARAMETER_N2 on BIS_CONFIG_PARAMETER (CHR_CODE)';
  
execute immediate 'alter table BIS_CONFIG_PARAMETER
  add constraint BIS_CONFIG_PARAMETER_PK primary key (CHR_ID, RG_CODE, SET_YEAR)';
   
select count(*) into i from user_tables where table_name='BIS_DETAIL_CONFIG';
 if i=0 then
 execute immediate '
create table BIS_DETAIL_CONFIG
(
  node_id    VARCHAR2(38) not null,
  node_code  VARCHAR2(42),
  node_name  VARCHAR2(80),
  group_code VARCHAR2(38),
  order_num  NUMBER(3),
  sys_id     VARCHAR2(42),
  class_name VARCHAR2(200),
  param      VARCHAR2(2000),
  node_desc  VARCHAR2(300),
  is_valid   NUMBER(1) not null,
  set_year   NUMBER(4) not null,
  RG_CODE    VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on table BIS_DETAIL_CONFIG
  is ''��Ŀ��ϸ��ѯ���ñ�''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.node_id
  is ''��Ŀ��ϸ��ѯ������ϸҳ�������ID��������''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.node_code
  is ''��Ŀ��ϸ��ѯ������ϸҳ���������''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.node_name
  is ''��Ŀ��ϸ��ѯ������ϸҳ����������''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.group_code
  is ''��Ŀ��ϸ��ѯ������ϸҳ��ı���''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.order_num
  is ''����''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.sys_id
  is ''��ϵͳ����''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.class_name
  is ''��Ŀ��ϸ��ѯ����ʵ������''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.param
  is ''��Ŀ��ϸ��ѯ����ʵ������ʹ�ò���''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.node_desc
  is ''��ϸ˵��''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.is_valid
  is ''�Ƿ����ã�0.δ���ã�1.���ã�''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.set_year
  is ''���''';
execute immediate 'comment on column BIS_DETAIL_CONFIG.RG_CODE
  is ''����''';
execute immediate 'create unique index UK_BIS_DETAIL_CONFIG on BIS_DETAIL_CONFIG (NODE_CODE, SET_YEAR, RG_CODE)';
  
execute immediate 'alter table BIS_DETAIL_CONFIG
  add constraint PK_BIS_DETAIL_CONFIG_ID primary key (NODE_ID, SET_YEAR, RG_CODE)';

  
  
select count(*) into i from user_tables where table_name='CCID_TRANS_TEMP_61';
 if i=0 then
 execute immediate '
  
create table CCID_TRANS_TEMP_61
(
  old_ccid NUMBER,
  new_ccid NUMBER
)'
;
end if;


select count(*) into i from user_tables where table_name='DB_BUG_SQL';
 if i=0 then
 execute immediate '

create table DB_BUG_SQL
(
  sys_id         VARCHAR2(42) not null,
  app_version    VARCHAR2(60) not null,
  sql_id         NUMBER(8) not null,
  iscommit       NUMBER(1) default 0,
  remark         VARCHAR2(400),
  latest_op_date VARCHAR2(30),
  is_now         NUMBER(1)
)'
;
end if;

execute immediate 'comment on column DB_BUG_SQL.sys_id
  is ''�洢��ϵͳID''';
execute immediate 'comment on column DB_BUG_SQL.app_version
  is ''�洢����汾''';
execute immediate 'comment on column DB_BUG_SQL.iscommit
  is ''�Ƿ��Ѿ������ݿ���ִ��''';
execute immediate 'comment on column DB_BUG_SQL.remark
  is ''��ע''';
execute immediate 'comment on column DB_BUG_SQL.latest_op_date
  is ''������ʱ��''';
execute immediate 'comment on column DB_BUG_SQL.is_now
  is ''�ǲ�������ʹ�õİ汾(1��,0��)''';
execute immediate 'alter table DB_BUG_SQL
  add constraint PRI_BUG_DB_SQL primary key (SYS_ID, APP_VERSION, SQL_ID)';

  
  
  
select count(*) into i from user_tables where table_name='DB_SUIT';
 if i=0 then
 execute immediate '
create table DB_SUIT
(
  app_version    VARCHAR2(400) not null,
  iscommit       NUMBER(1) default 0,
  remark         VARCHAR2(400),
  latest_op_date VARCHAR2(30),
  is_now         NUMBER(1),
  suit_ver       VARCHAR2(100)
)'
;
end if;

execute immediate 'comment on column DB_SUIT.app_version
  is ''�׼���װ��������ϵͳ''';
execute immediate 'comment on column DB_SUIT.remark
  is ''��ע''';
execute immediate 'comment on column DB_SUIT.latest_op_date
  is ''������ʱ��''';
execute immediate 'comment on column DB_SUIT.is_now
  is ''�Ƿ��ǵ�ǰ�׼�1����,0:��''';
execute immediate 'comment on column DB_SUIT.suit_ver
  is ''�׼��汾''';

  
  
  
select count(*) into i from user_tables where table_name='ELE_ACCOUNT
';
 if i=0 then
 execute immediate '
create table ELE_ACCOUNT
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  account_no     VARCHAR2(42) not null,
  account_name   VARCHAR2(60),
  bank_code      VARCHAR2(42) not null,
  account_type   NUMBER(4) not null,
  owner_code     VARCHAR2(42),
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  start_date     VARCHAR2(38),
  stop_date      VARCHAR2(38),
  is_default     NUMBER(1),
  remark         VARCHAR2(200)
)'
;
end if;

execute immediate 'comment on column ELE_ACCOUNT.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_ACCOUNT.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_ACCOUNT.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_ACCOUNT.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_ACCOUNT.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_ACCOUNT.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_ACCOUNT.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_ACCOUNT.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_ACCOUNT.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_ACCOUNT.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_ACCOUNT.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_ACCOUNT.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNT.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNT.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNT.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNT.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNT.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_ACCOUNT.account_no
  is ''�˻���Ӧ�˺�''';
execute immediate 'comment on column ELE_ACCOUNT.account_name
  is ''�˻�����''';
execute immediate 'comment on column ELE_ACCOUNT.bank_code
  is ''�˻���Ӧ������Ψһ����''';
execute immediate 'comment on column ELE_ACCOUNT.account_type
  is ''11ֱ��֧��������˻� 12��Ȩ֧��������˻� 13��λʵ���տ��˻� 21���������˻� 22ʵ�������˻�
 31�������л����˻� 41�����˻� 51�������˻�''';
execute immediate 'comment on column ELE_ACCOUNT.owner_code
  is ''�궨���˻���������
 ֱ��֧��������˻��������˻��������˻�д��0��ʾ��������Ȩ֧��������˻��������˻�д�뵥λ�����ʾ��Ӧ��λ�����������˻�д��鼯�б����ʾ��Ӧ���С�''';
execute immediate 'comment on column ELE_ACCOUNT.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_ACCOUNT.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNT.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNT.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNT.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNT.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNT.start_date
  is ''�˻���������''';
execute immediate 'comment on column ELE_ACCOUNT.stop_date
  is ''�˻�ͣ������''';
execute immediate 'create index ELE_ACCOUNT_N1 on ELE_ACCOUNT (SET_YEAR, RG_CODE)
  ';
execute immediate 'create index ELE_ACCOUNT_N2 on ELE_ACCOUNT (CHR_CODE)
  ';
  
select count(*) into i from user_tables where table_name='ELE_ACCOUNTANT_SUBJECT
';
 if i=0 then
 execute immediate '
create table ELE_ACCOUNTANT_SUBJECT
(
  set_year               NUMBER(4) not null,
  chr_id                 VARCHAR2(38) not null,
  chr_code               VARCHAR2(42),
  disp_code              VARCHAR2(42) not null,
  chr_name               VARCHAR2(60) not null,
  level_num              NUMBER(2) default 0 not null,
  is_leaf                NUMBER(1) default 0 not null,
  enabled                NUMBER(1) default 1 not null,
  create_date            VARCHAR2(30),
  create_user            VARCHAR2(42),
  latest_op_date         VARCHAR2(30) not null,
  is_deleted             NUMBER default 0 not null,
  latest_op_user         VARCHAR2(42),
  last_ver               VARCHAR2(30),
  chr_code1              VARCHAR2(42),
  chr_code2              VARCHAR2(42),
  chr_code3              VARCHAR2(42),
  chr_code4              VARCHAR2(42),
  chr_code5              VARCHAR2(42),
  RG_CODE                VARCHAR2(42) not null,
  is_debit               NUMBER(1),
  subject_type           NUMBER(1) default 0,
  parent_id              VARCHAR2(38),
  chr_id1                VARCHAR2(38),
  chr_id2                VARCHAR2(38),
  chr_id3                VARCHAR2(38),
  chr_id4                VARCHAR2(38),
  chr_id5                VARCHAR2(38),
  st_id                  VARCHAR2(38),
  subject_kind           NUMBER(1),
  coa_id                 VARCHAR2(38),
  hint_code              VARCHAR2(42),
  help_code              VARCHAR2(42),
  analy_code             VARCHAR2(42),
  is_from_moudle         NUMBER(1),
  remark                 VARCHAR2(60),
  ele_code3              VARCHAR2(42) default ''AS'' not null,
  ele_code               VARCHAR2(42) default ''AS'' not null,
  foreign_mon            VARCHAR2(30),
  unit_num               VARCHAR2(30),
  is_spread              NUMBER(1) default 0,
  table_name             VARCHAR2(30),
  balance_period_type    NUMBER(1),
  monthdetail_table_name VARCHAR2(30),
  bal_table              VARCHAR2(42) default ''glv_'',
  asgroup_id             NUMBER(10),
  ctrl_type              NUMBER(1)
)'
;
end if;

execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.is_debit
  is ''��Ŀ����Ƿ��ڽ跽''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.subject_type
  is ''��Ŀ����''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.st_id
  is ''����ID''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.subject_kind
  is ''��Ŀ����,��Ŀ���ʣ�0��һ���Ŀ��1���̶��ʲ���2��������3���ռ��ʣ�''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.coa_id
  is ''��Ŀ�ṹCOA''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.hint_code
  is ''������''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.help_code
  is ''��������''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.analy_code
  is ''ͳ����''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.is_from_moudle
  is ''�Ƿ��ģ���Ŀ�����ģ���·���Ŀ''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.remark
  is ''��ע''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.ele_code3
  is ''Ҫ���ֶΣ�����ǹ��ܷ���д��BS���������д��IN_BS''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.ele_code
  is ''Ҫ���ֶΣ�����ǹ��ܷ���д��BS���������д��IN_BS''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.foreign_mon
  is ''��ұ���''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.unit_num
  is ''������λ''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.is_spread
  is ''�Ƿ��Զ�չ���׸���������Ϣ''';
execute immediate 'comment on column ELE_ACCOUNTANT_SUBJECT.monthdetail_table_name
  is ''�·���ϸ�ı�''';
execute immediate 'create index ELE_ACCOUNTANT_SUBJECT_N1 on ELE_ACCOUNTANT_SUBJECT (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_ACCOUNTANT_SUBJECT_N2 on ELE_ACCOUNTANT_SUBJECT (CHR_CODE)';
  
execute immediate 'alter table ELE_ACCOUNTANT_SUBJECT
  add constraint ELE_ACCOUNTANT_SUBJECT_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';

  select count(*) into i from user_tables where table_name='ELE_ACCREDIT
';
 if i=0 then
 execute immediate '
create table ELE_ACCREDIT
(
  SET_YEAR       NUMBER(4) not null,
  CHR_ID         VARCHAR2(38) not null,
  CHR_CODE       VARCHAR2(42),
  DISP_CODE      VARCHAR2(42) not null,
  CHR_NAME       VARCHAR2(60) not null,
  LEVEL_NUM      NUMBER(2) default 0 not null,
  IS_LEAF        NUMBER(1) default 0 not null,
  ENABLED        NUMBER(1) default 1 not null,
  CREATE_DATE    VARCHAR2(30),
  CREATE_USER    VARCHAR2(42),
  LATEST_OP_DATE VARCHAR2(30) not null,
  IS_DELETED     NUMBER default 0 not null,
  LATEST_OP_USER VARCHAR2(42),
  LAST_VER       VARCHAR2(30),
  CHR_CODE1      VARCHAR2(42),
  CHR_CODE2      VARCHAR2(42),
  CHR_CODE3      VARCHAR2(42),
  CHR_CODE4      VARCHAR2(42),
  CHR_CODE5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  PARENT_ID      VARCHAR2(38),
  CHR_ID1        VARCHAR2(38),
  CHR_ID2        VARCHAR2(38),
  CHR_ID3        VARCHAR2(38),
  CHR_ID4        VARCHAR2(38),
  CHR_ID5        VARCHAR2(38)
)'
;
end if;

-- Add comments to the columns 
execute immediate 'comment on column ELE_ACCREDIT.SET_YEAR
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_ID
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_CODE
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_ACCREDIT.DISP_CODE
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_NAME
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_ACCREDIT.LEVEL_NUM
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_ACCREDIT.IS_LEAF
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_ACCREDIT.ENABLED
  is ''�궨��Ҫ���Ƿ�����''';
execute immediate 'comment on column ELE_ACCREDIT.CREATE_DATE
  is ''����ʱ�� YYYY-MM-DD HH:MM:SS''';
execute immediate 'comment on column ELE_ACCREDIT.CREATE_USER
  is ''�����û�''';
execute immediate 'comment on column ELE_ACCREDIT.LATEST_OP_DATE
  is ''����޸�ʱ�䣺 YYYY-MM-DD HH:MM:SS''';
execute immediate 'comment on column ELE_ACCREDIT.IS_DELETED
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_ACCREDIT.LATEST_OP_USER
  is ''����޸��û�''';
execute immediate 'comment on column ELE_ACCREDIT.LAST_VER
  is ''���汾�� YYYY-MM-DD HH:MM:SS''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_CODE1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_CODE2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_CODE3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_CODE4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_CODE5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ACCREDIT.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_ACCREDIT.PARENT_ID
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_ID1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_ID2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_ID3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_ID4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ACCREDIT.CHR_ID5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
-- Create/Recreate primary, unique and foreign key constraints 
execute immediate 'alter table ELE_ACCREDIT
  add constraint ELE_ACCREDIT_PK primary key (CHR_ID)
  using index ';
-- Create/Recreate indexes 
execute immediate 'create index ELE_ACCREDIT_N1 on ELE_ACCREDIT (SET_YEAR)
  ';
execute immediate 'create index ELE_ACCREDIT_N2 on ELE_ACCREDIT (CHR_CODE)
  ';  
  
select count(*) into i from user_tables where table_name='ELE_BANK
';
 if i=0 then
 execute immediate '
  
create table ELE_BANK
(
  set_year         NUMBER(4) not null,
  chr_id           VARCHAR2(38) not null,
  chr_code         VARCHAR2(42),
  disp_code        VARCHAR2(42) not null,
  chr_name         VARCHAR2(60) not null,
  level_num        NUMBER(2) default 0 not null,
  is_leaf          NUMBER(1) default 0 not null,
  enabled          NUMBER(1) default 1 not null,
  create_date      VARCHAR2(30),
  create_user      VARCHAR2(42),
  latest_op_date   VARCHAR2(30) not null,
  is_deleted       NUMBER default 0 not null,
  latest_op_user   VARCHAR2(42),
  last_ver         VARCHAR2(30),
  chr_code1        VARCHAR2(42),
  chr_code2        VARCHAR2(42),
  chr_code3        VARCHAR2(42),
  chr_code4        VARCHAR2(42),
  chr_code5        VARCHAR2(42),
  RG_CODE          VARCHAR2(42) not null,
  bank_charge      VARCHAR2(20),
  finance_charge   VARCHAR2(20),
  district_number  VARCHAR2(4),
  telephone        VARCHAR2(10),
  extension_number VARCHAR2(10),
  address          VARCHAR2(200),
  parent_id        VARCHAR2(38),
  chr_id1          VARCHAR2(38),
  chr_id2          VARCHAR2(38),
  chr_id3          VARCHAR2(38),
  chr_id4          VARCHAR2(38),
  chr_id5          VARCHAR2(38),
  agentflag        NUMBER(1) default 0,
  clearflag        NUMBER(1) default 0,
  incomeflag       NUMBER(1) default 0,
  salaryflag       NUMBER(1) default 0,
  exbank_code      VARCHAR2(50),
  exbank_name      VARCHAR2(100),
  exbank_pbno      VARCHAR2(3),
  bank_sign        VARCHAR2(20),
  isbn             VARCHAR2(50),
  city_code        VARCHAR2(42),
  province         VARCHAR2(42),
  bank_no          VARCHAR2(42),
  isbn_c           VARCHAR2(50)
)'
;
end if;

execute immediate 'comment on column ELE_BANK.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BANK.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BANK.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BANK.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BANK.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BANK.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BANK.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BANK.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BANK.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BANK.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BANK.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BANK.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BANK.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BANK.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BANK.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BANK.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BANK.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BANK.bank_charge
  is ''���и�����''';
execute immediate 'comment on column ELE_BANK.finance_charge
  is ''ҵ������''';
execute immediate 'comment on column ELE_BANK.district_number
  is ''���е绰����''';
execute immediate 'comment on column ELE_BANK.telephone
  is ''���е绰''';
execute immediate 'comment on column ELE_BANK.extension_number
  is ''���зֻ���''';
execute immediate 'comment on column ELE_BANK.address
  is ''���е�ַ''';
execute immediate 'comment on column ELE_BANK.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BANK.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BANK.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BANK.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BANK.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BANK.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BANK.agentflag
  is ''�Ƿ��������(1 �� 0 ��)''';
execute immediate 'comment on column ELE_BANK.clearflag
  is ''�Ƿ���������(1 �� 0 ��)''';
execute immediate 'comment on column ELE_BANK.incomeflag
  is ''�Ƿ���������(1 �� 0 ��)''';
execute immediate 'comment on column ELE_BANK.salaryflag
  is ''�Ƿ��ʴ�������(1 �� 0 ��)''';
execute immediate 'comment on column ELE_BANK.exbank_code
  is ''�����к�(�����з�֧�л�����)''';
execute immediate 'comment on column ELE_BANK.exbank_name
  is ''����������(������֧������)''';
execute immediate 'comment on column ELE_BANK.exbank_pbno
  is ''���б��,�����������У���ֵΪ00''';
execute immediate 'comment on column ELE_BANK.bank_sign
  is ''���м��(���������е�������Ʒ)''';
execute immediate 'comment on column ELE_BANK.isbn
  is ''���к�''';
execute immediate 'comment on column ELE_BANK.city_code
  is ''��������(������������������)''';
execute immediate 'comment on column ELE_BANK.province
  is ''����ʡ��(��������������ʡ��)''';
execute immediate 'comment on column ELE_BANK.isbn_c
  is ''�����к�''';
execute immediate 'create index ELE_BANK_N1 on ELE_BANK (SET_YEAR)
  ';
execute immediate 'create index ELE_BANK_N2 on ELE_BANK (CHR_CODE)
  ';
execute immediate 'alter table ELE_BANK
  add constraint ELE_BANK_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
select count(*) into i from user_tables where table_name='ELE_BOOK_SET
';
 if i=0 then
 execute immediate '
  
create table ELE_BOOK_SET
(
  set_year         NUMBER(4) not null,
  chr_id           VARCHAR2(38) not null,
  chr_code         VARCHAR2(42),
  disp_code        VARCHAR2(42) not null,
  chr_name         VARCHAR2(60) not null,
  level_num        NUMBER(2) default 0 not null,
  is_leaf          NUMBER(1) default 0 not null,
  enabled          NUMBER(1) default 1 not null,
  create_date      VARCHAR2(30),
  create_user      VARCHAR2(42),
  latest_op_date   VARCHAR2(30) not null,
  is_deleted       NUMBER default 0 not null,
  latest_op_user   VARCHAR2(42),
  last_ver         VARCHAR2(30),
  chr_code1        VARCHAR2(42),
  chr_code2        VARCHAR2(42),
  chr_code3        VARCHAR2(42),
  chr_code4        VARCHAR2(42),
  chr_code5        VARCHAR2(42),
  RG_CODE          VARCHAR2(42) not null,
  start_date       VARCHAR2(30) not null,
  set_type         NUMBER(2) not null,
  parent_id        VARCHAR2(38),
  chr_id1          VARCHAR2(38),
  chr_id2          VARCHAR2(38),
  chr_id3          VARCHAR2(38),
  chr_id4          VARCHAR2(38),
  chr_id5          VARCHAR2(38),
  manager_name     VARCHAR2(42),
  current_month    NUMBER(2) default 1 not null,
  is_initialized   NUMBER(1) default 0 not null,
  is_year_finished NUMBER(1) default 0 not null,
  coa_id           VARCHAR2(38),
  AGENCY_ID            VARCHAR2(38),
  rule_id          NUMBER,
  table_flag       VARCHAR2(30) default ''GL_''
)'
;
end if;

execute immediate 'comment on column ELE_BOOK_SET.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BOOK_SET.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BOOK_SET.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BOOK_SET.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BOOK_SET.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BOOK_SET.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BOOK_SET.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BOOK_SET.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BOOK_SET.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BOOK_SET.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BOOK_SET.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BOOK_SET.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BOOK_SET.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BOOK_SET.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BOOK_SET.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BOOK_SET.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BOOK_SET.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BOOK_SET.start_date
  is ''�����漰��������''';
execute immediate 'comment on column ELE_BOOK_SET.set_type
  is ''��������0��ָ����1��Ԥ������2��Ԥ������ 3���籣������''';
execute immediate 'comment on column ELE_BOOK_SET.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BOOK_SET.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BOOK_SET.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BOOK_SET.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BOOK_SET.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BOOK_SET.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BOOK_SET.manager_name
  is ''��������''';
execute immediate 'comment on column ELE_BOOK_SET.current_month
  is ''��ǰ����·�''';
execute immediate 'comment on column ELE_BOOK_SET.is_initialized
  is ''�Ƿ��ʼ��ȷ��''';
execute immediate 'comment on column ELE_BOOK_SET.is_year_finished
  is ''�Ƿ����''';
execute immediate 'comment on column ELE_BOOK_SET.coa_id
  is ''����COA''';
execute immediate 'comment on column ELE_BOOK_SET.AGENCY_ID
  is ''Ԥ�㵥λ''';
execute immediate 'comment on column ELE_BOOK_SET.table_flag
  is ''������ʹ�õ����������Ƶ�ǰ׺''';
execute immediate 'create index ELE_BOOK_SET_N1 on ELE_BOOK_SET (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_BOOK_SET_N2 on ELE_BOOK_SET (CHR_CODE)';
  
execute immediate 'alter table ELE_BOOK_SET add constraint ELE_BOOK_SET_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
   
   
   
select count(*) into i from user_tables where table_name='ELE_BUDGET_CATEGORY
';
 if i=0 then
 execute immediate '
   
create table ELE_BUDGET_CATEGORY
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_CATEGORY.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_CATEGORY.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_CATEGORY.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_CATEGORY.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_CATEGORY_N1 on ELE_BUDGET_CATEGORY (SET_YEAR)';
  
execute immediate 'create index ELE_BUDGET_CATEGORY_N2 on ELE_BUDGET_CATEGORY (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_CATEGORY
  add constraint ELE_BUDGET_CATEGORY_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_ITEM
';
 if i=0 then
 execute immediate '

create table ELE_BUDGET_ITEM
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_ITEM.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_ITEM.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_ITEM.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_ITEM.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_ITEM.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_ITEM.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_ITEM.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_ITEM.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_ITEM.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_ITEM.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_ITEM_N1 on ELE_BUDGET_ITEM (SET_YEAR)';
  
execute immediate 'create index ELE_BUDGET_ITEM_N2 on ELE_BUDGET_ITEM (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_ITEM
  add constraint ELE_BUDGET_ITEM_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_ITEM_SUMMARY
';
 if i=0 then
 execute immediate '
  
create table ELE_BUDGET_ITEM_SUMMARY
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(2000) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  start_date     VARCHAR2(30),
  end_date       VARCHAR2(30),
  public_sign    NUMBER(1) default 0,
  sys_id         VARCHAR2(38),
  remark         VARCHAR2(250)
)'
;
end if;

execute immediate 'comment on table ELE_BUDGET_ITEM_SUMMARY
  is ''��Ŀ����''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY.public_sign
  is ''�Ƿ�����Ŀ''';
execute immediate 'create index ELE_BUDGET_ITEM_SUMMARY_N1 on ELE_BUDGET_ITEM_SUMMARY (SET_YEAR, RG_CODE)
 ' ;
execute immediate 'create index ELE_BUDGET_ITEM_SUMMARY_N2 on ELE_BUDGET_ITEM_SUMMARY (CHR_CODE)
  ';
execute immediate 'alter table ELE_BUDGET_ITEM_SUMMARY
  add constraint ELE_BUDGET_ITEM_SUMMARY_PK primary key (CHR_ID, RG_CODE, SET_YEAR)';
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_ITEM_SUMMARY_BELONG
';
 if i=0 then
 execute immediate '
  
create table ELE_BUDGET_ITEM_SUMMARY_BELONG
(
  bis_id       VARCHAR2(38) not null,
  year         NUMBER(4) not null,
  MB_ID VARCHAR2(38),
  AGENCY_ID        VARCHAR2(38),
  agencyexp_id VARCHAR2(38),
  chr_code_sup VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on table ELE_BUDGET_ITEM_SUMMARY_BELONG
  is ''������Ϣ������''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_BELONG.bis_id
  is ''��ĿID''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_BELONG.year
  is ''���''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_BELONG.MB_ID
  is ''ҵ����''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_BELONG.AGENCY_ID
  is ''Ԥ�㵥λ''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_BELONG.agencyexp_id
  is ''��Ŀ����''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_BELONG.chr_code_sup
  is ''������Ŀ����''';
execute immediate 'create index INDEX_BIS_BELONG on ELE_BUDGET_ITEM_SUMMARY_BELONG (BIS_ID)';



select count(*) into i from user_tables where table_name='ELE_BUDGET_ITEM_SUMMARY_MONEY
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_ITEM_SUMMARY_MONEY
(
  bis_id               VARCHAR2(38) not null,
  year                 NUMBER(4) not null,
  total_money          NUMBER(16,2) default 0,
  money                NUMBER(16,2) default 0,
  adjust_money         NUMBER(16,2) default 0,
  budget_money         NUMBER(16,2) default 0,
  budget_balance_money NUMBER(16,2) default 0,
  plan_money           NUMBER(16,2) default 0,
  pay_money            NUMBER(16,2) default 0
)'
;
end if;

execute immediate 'comment on table ELE_BUDGET_ITEM_SUMMARY_MONEY
  is ''�ʽ���Ϣ������''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.bis_id
  is ''��ĿID''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.year
  is ''���''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.total_money
  is ''��Ŀ������''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.money
  is ''���Ž��''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.adjust_money
  is ''�������''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.budget_money
  is ''ָ����''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.budget_balance_money
  is ''ָ�������''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.plan_money
  is ''�ƻ����''';
execute immediate 'comment on column ELE_BUDGET_ITEM_SUMMARY_MONEY.pay_money
  is ''֧�����''';
execute immediate 'alter table ELE_BUDGET_ITEM_SUMMARY_MONEY
  add constraint PK_ELE_BIS_MONEY primary key (BIS_ID, YEAR)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_KIND
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_KIND
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_KIND.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_KIND.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_KIND.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_KIND.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_KIND.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_KIND.create_user
  is ''�����û�''';
execute immediate 'comment on column ELE_BUDGET_KIND.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_KIND.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_KIND.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_KIND.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_KIND.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_KIND.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_KIND_N1 on ELE_BUDGET_KIND (SET_YEAR)';
  
execute immediate 'create index ELE_BUDGET_KIND_N2 on ELE_BUDGET_KIND (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_KIND
  add constraint ELE_BUDGET_KIND_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_LABEL
';
 if i=0 then
 execute immediate '

create table ELE_BUDGET_LABEL
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_LABEL.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_LABEL.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_LABEL.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_LABEL.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_LABEL.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_LABEL.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_LABEL.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_LABEL.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_LABEL.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_LABEL.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_LABEL.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_LABEL.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_LABEL_N1 on ELE_BUDGET_LABEL (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_BUDGET_LABEL_N2 on ELE_BUDGET_LABEL (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_LABEL
  add constraint ELE_BUDGET_LABEL_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_ORIGIN
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_ORIGIN
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_ORIGIN.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_ORIGIN.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_ORIGIN.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ORIGIN.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_ORIGIN_N1 on ELE_BUDGET_ORIGIN (SET_YEAR)';
  
execute immediate 'create index ELE_BUDGET_ORIGIN_N2 on ELE_BUDGET_ORIGIN (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_ORIGIN
  add constraint ELE_BUDGET_ORIGIN_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_PURPOSE
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_PURPOSE
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_PURPOSE.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_code
  is ''Ϊ�ۺϲ�ѯ�����ı���''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.disp_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_PURPOSE.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_PURPOSE.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_PURPOSE.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_PURPOSE_N1 on ELE_BUDGET_PURPOSE (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_BUDGET_PURPOSE_N2 on ELE_BUDGET_PURPOSE (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_PURPOSE
  add constraint ELE_BUDGET_PURPOSE_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  

select count(*) into i from user_tables where table_name='ELE_BUDGET_SUBJECT
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_SUBJECT
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(200) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  type_id        NUMBER(2) default 22,
  subitem_type   NUMBER(3)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_SUBJECT.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_SUBJECT_N1 on ELE_BUDGET_SUBJECT (SET_YEAR, RG_CODE)
  ';
execute immediate 'create index ELE_BUDGET_SUBJECT_N2 on ELE_BUDGET_SUBJECT (CHR_CODE)
  ';
execute immediate 'alter table ELE_BUDGET_SUBJECT
  add constraint ELE_BUDGET_SUBJECT_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_SUBJECT_INCOME
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_SUBJECT_INCOME
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  type_id        NUMBER(2) default 24,
  subitem_type   NUMBER(2) default 0
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_INCOME.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_SUBJECT_INCOME_N1 on ELE_BUDGET_SUBJECT_INCOME (SET_YEAR, RG_CODE)
  ';
execute immediate 'create index ELE_BUDGET_SUBJECT_INCOME_N2 on ELE_BUDGET_SUBJECT_INCOME (CHR_CODE)
  ';
execute immediate 'alter table ELE_BUDGET_SUBJECT_INCOME
  add constraint ELE_BUDGET_SUBJECT_INCOME_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';

  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_SUBJECT_ITEM
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_SUBJECT_ITEM
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  pay_type_id    NUMBER(2) default 23,
  subitem_type   NUMBER(2) default 0,
  is_sichar      NUMBER(2) default 1,
  projectused    NUMBER(2) default 1,
  type_id        NUMBER(2) default 23
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_ITEM.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_SUBJECT_ITEM_N1 on ELE_BUDGET_SUBJECT_ITEM (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_BUDGET_SUBJECT_ITEM_N2 on ELE_BUDGET_SUBJECT_ITEM (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_SUBJECT_ITEM
  add constraint ELE_BUDGET_SUBJECT_ITEM_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_SUBJECT_OLD
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_SUBJECT_OLD
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUBJECT_OLD.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_SUBJECT_OLD_N1 on ELE_BUDGET_SUBJECT_OLD (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_BUDGET_SUBJECT_OLD_N2 on ELE_BUDGET_SUBJECT_OLD (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_SUBJECT_OLD
  add constraint ELE_BUDGET_SUBJECT_OLD_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_SUMMARY
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_SUMMARY
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(200) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_SUMMARY.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_SUMMARY_N2 on ELE_BUDGET_SUMMARY (CHR_CODE)
  ';
execute immediate 'alter table ELE_BUDGET_SUMMARY
  add constraint ELE_BUDGET_SUMMARY_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';

  
  
select count(*) into i from user_tables where table_name='ELE_BUDGET_TARGET
';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_TARGET
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_BUDGET_TARGET.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_TARGET.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_TARGET.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_TARGET.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_TARGET.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_BUDGET_TARGET.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_BUDGET_TARGET.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_TARGET.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_BUDGET_TARGET.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_TARGET.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_TARGET.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_TARGET.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_BUDGET_TARGET_N1 on ELE_BUDGET_TARGET (SET_YEAR)';
  
execute immediate 'create index ELE_BUDGET_TARGET_N2 on ELE_BUDGET_TARGET (CHR_CODE)';
  
execute immediate 'alter table ELE_BUDGET_TARGET
  add constraint ELE_BUDGET_TARGET_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_CLEAR_INTERFACE_DATE
';
 if i=0 then
 execute immediate '
create table ELE_CLEAR_INTERFACE_DATE
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_CLEAR_INTERFACE_DATE.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_CLEAR_INTERFACE_DATE_N1 on ELE_CLEAR_INTERFACE_DATE (SET_YEAR)';
  
execute immediate 'create index ELE_CLEAR_INTERFACE_DATE_N2 on ELE_CLEAR_INTERFACE_DATE (CHR_CODE)';



select count(*) into i from user_tables where table_name='ELE_ENTERPRISE
';
 if i=0 then
 execute immediate '
create table ELE_ENTERPRISE
(
  set_year            NUMBER(4) not null,
  chr_id              VARCHAR2(38) not null,
  chr_code            VARCHAR2(42),
  disp_code           VARCHAR2(42) not null,
  chr_name            VARCHAR2(100) not null,
  level_num           NUMBER(2) default 0 not null,
  is_leaf             NUMBER(1) default 0 not null,
  enabled             NUMBER(1) default 1 not null,
  create_date         VARCHAR2(30),
  create_user         VARCHAR2(42),
  latest_op_date      VARCHAR2(30) not null,
  is_deleted          NUMBER default 0 not null,
  latest_op_user      VARCHAR2(42),
  last_ver            VARCHAR2(30),
  chr_code1           VARCHAR2(42),
  chr_code2           VARCHAR2(42),
  chr_code3           VARCHAR2(42),
  chr_code4           VARCHAR2(42),
  chr_code5           VARCHAR2(42),
  RG_CODE             VARCHAR2(42) not null,
  main_code           VARCHAR2(42),
  union_code          VARCHAR2(42),
  en_property         VARCHAR2(60),
  sort_property       VARCHAR2(60),
  en_charge           VARCHAR2(20),
  finance_charge      VARCHAR2(20),
  clerk               VARCHAR2(20),
  district_number     VARCHAR2(4),
  telephone           VARCHAR2(10),
  extension_number    VARCHAR2(10),
  address             VARCHAR2(200),
  manage_level        VARCHAR2(60),
  relation            VARCHAR2(60),
  parent_id           VARCHAR2(38),
  chr_id1             VARCHAR2(38),
  chr_id2             VARCHAR2(38),
  chr_id3             VARCHAR2(38),
  chr_id4             VARCHAR2(38),
  chr_id5             VARCHAR2(38),
  chr_code6           VARCHAR2(42),
  chr_code7           VARCHAR2(42),
  chr_code8           VARCHAR2(42),
  chr_code9           VARCHAR2(42),
  chr_id6             VARCHAR2(38),
  chr_id7             VARCHAR2(38),
  chr_id8             VARCHAR2(38),
  chr_id9             VARCHAR2(38),
  is_reform           NUMBER(1) default 1,
  start_date          VARCHAR2(30),
  end_date            VARCHAR2(30),
  secretdegree        NUMBER(2) default 3,
  isbudget            NUMBER(2) default 0,
  luload_mod          NUMBER(2) default 0,
  en_cf_mod           NUMBER(2) default 0,
  expfunc_id          VARCHAR2(38),
  reserve_amt         NUMBER(16,2) default 0,
  reserve_start       NUMBER(16,2) default 0,
  reserve_used        NUMBER(16,2) default 0,
  MB_ID        VARCHAR2(38),
  en_type             NUMBER(1),
  div_kind            VARCHAR2(20),
  div_fmkind          VARCHAR2(40),
  is_legalinc         NUMBER(2),
  enabled_ebank_date  VARCHAR2(10),
  ebank_enterprise_no VARCHAR2(30),
  enabled_ebank       NUMBER(1) default 0
)'
;
end if;

execute immediate 'comment on column ELE_ENTERPRISE.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_ENTERPRISE.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_ENTERPRISE.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_ENTERPRISE.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_ENTERPRISE.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_ENTERPRISE.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_ENTERPRISE.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_ENTERPRISE.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_ENTERPRISE.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_ENTERPRISE.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_ENTERPRISE.main_code
  is ''��ʾ��λΨһ������''';
execute immediate 'comment on column ELE_ENTERPRISE.union_code
  is ''���������ල�ֺ˷�''';
execute immediate 'comment on column ELE_ENTERPRISE.en_property
  is ''��λ�������ʺ���''';
execute immediate 'comment on column ELE_ENTERPRISE.sort_property
  is ''���ŷ��ຬ��''';
execute immediate 'comment on column ELE_ENTERPRISE.en_charge
  is ''��λ������''';
execute immediate 'comment on column ELE_ENTERPRISE.finance_charge
  is ''��������''';
execute immediate 'comment on column ELE_ENTERPRISE.clerk
  is ''��Ҫ����Ա''';
execute immediate 'comment on column ELE_ENTERPRISE.district_number
  is ''��λ�绰����''';
execute immediate 'comment on column ELE_ENTERPRISE.telephone
  is ''��λ�绰''';
execute immediate 'comment on column ELE_ENTERPRISE.extension_number
  is ''��λ��Ӧ�ֻ���''';
execute immediate 'comment on column ELE_ENTERPRISE.address
  is ''��λ��ַ''';
execute immediate 'comment on column ELE_ENTERPRISE.manage_level
  is ''Ԥ������κ���''';
execute immediate 'comment on column ELE_ENTERPRISE.relation
  is ''������ϵ����''';
execute immediate 'comment on column ELE_ENTERPRISE.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ENTERPRISE.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_ENTERPRISE.is_reform
  is ''�궨�Ƿ�ĸﵥλ1�� 0��''';
execute immediate 'comment on column ELE_ENTERPRISE.reserve_amt
  is ''��λ�ı��ý���''';
execute immediate 'comment on column ELE_ENTERPRISE.reserve_start
  is ''���õı��ý���''';
execute immediate 'comment on column ELE_ENTERPRISE.reserve_used
  is ''ʹ�õı��ý���''';
execute immediate 'comment on column ELE_ENTERPRISE.en_type
  is ''1#���һ���+2#��ҵ��λ+3#������֯''';
execute immediate 'comment on column ELE_ENTERPRISE.div_kind
  is ''��λ����''';
execute immediate 'comment on column ELE_ENTERPRISE.div_fmkind
  is ''Ԥ�����ʽ''';
execute immediate 'comment on column ELE_ENTERPRISE.is_legalinc
  is ''�Ƿ񷨶�������λ''';
execute immediate 'comment on column ELE_ENTERPRISE.enabled_ebank_date
  is ''��������ʱ��''';
execute immediate 'comment on column ELE_ENTERPRISE.ebank_enterprise_no
  is ''�����ͻ���''';
execute immediate 'comment on column ELE_ENTERPRISE.enabled_ebank
  is ''�Ƿ���������''';
execute immediate 'create index ELE_ENTERPRISE_N1 on ELE_ENTERPRISE (SET_YEAR, RG_CODE)
 ' ;
execute immediate 'create index ELE_ENTERPRISE_N2 on ELE_ENTERPRISE (CHR_CODE)
 ' ;
execute immediate 'alter table ELE_ENTERPRISE
  add constraint ELE_ENTERPRISE_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_FILE_NO
';
 if i=0 then
 execute immediate '
create table ELE_FILE_NO
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  file_year      NUMBER(4),
  file_date      VARCHAR2(30),
  title          VARCHAR2(2000),
  memo           VARCHAR2(2000),
  file_pre       VARCHAR2(100),
  file_num       VARCHAR2(10)
)'
;
end if;

execute immediate 'comment on column ELE_FILE_NO.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_FILE_NO.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_FILE_NO.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_FILE_NO.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_FILE_NO.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_FILE_NO.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_FILE_NO.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_FILE_NO.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_FILE_NO.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_FILE_NO.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_FILE_NO.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_FILE_NO.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FILE_NO.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FILE_NO.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FILE_NO.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FILE_NO.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FILE_NO.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_FILE_NO.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_FILE_NO.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FILE_NO.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FILE_NO.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FILE_NO.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FILE_NO.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FILE_NO.file_year
  is ''�ĺ����''';

execute immediate 'comment on column ELE_FILE_NO.title
  is ''����''';
execute immediate 'comment on column ELE_FILE_NO.memo
  is ''˵��''';
execute immediate 'comment on column ELE_FILE_NO.file_pre
  is ''�ĺ�ǰ׺''';
execute immediate 'comment on column ELE_FILE_NO.file_num
  is ''�����''';
execute immediate 'create index ELE_FILE_NO_N1 on ELE_FILE_NO (SET_YEAR, RG_CODE)';
execute immediate 'create index ELE_FILE_NO_N2 on ELE_FILE_NO (CHR_CODE)';
execute immediate 'alter table ELE_FILE_NO
  add constraint ELE_FILE_NO_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_FOREIGN_MONEY
';
 if i=0 then
 execute immediate '
create table ELE_FOREIGN_MONEY
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  parent_id      VARCHAR2(38),
  RG_CODE        VARCHAR2(42) not null,
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_FOREIGN_MONEY.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_FOREIGN_MONEY.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_FOREIGN_MONEY.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_FOREIGN_MONEY.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_FOREIGN_MONEY_N1 on ELE_FOREIGN_MONEY (SET_YEAR)';
  
execute immediate 'create index ELE_FOREIGN_MONEY_N2 on ELE_FOREIGN_MONEY (CHR_CODE)';
  
execute immediate 'alter table ELE_FOREIGN_MONEY
  add constraint ELE_FOREIGN_MONEY_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';

  
  
select count(*) into i from user_tables where table_name='ELE_GOV_BUY
';
 if i=0 then
 execute immediate '
create table ELE_GOV_BUY
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_GOV_BUY.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_GOV_BUY.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_GOV_BUY.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_GOV_BUY.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_GOV_BUY.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_GOV_BUY.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_GOV_BUY.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_GOV_BUY.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_GOV_BUY.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_GOV_BUY.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_GOV_BUY.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_GOV_BUY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GOV_BUY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GOV_BUY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GOV_BUY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GOV_BUY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GOV_BUY.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_GOV_BUY.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_GOV_BUY.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GOV_BUY.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GOV_BUY.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GOV_BUY.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GOV_BUY.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_GOV_BUY_N1 on ELE_GOV_BUY (SET_YEAR)';
  
execute immediate 'create index ELE_GOV_BUY_N2 on ELE_GOV_BUY (CHR_CODE)';
  
execute immediate 'alter table ELE_GOV_BUY
  add constraint ELE_GOV_BUY_PK primary key (CHR_ID)';

  
  
select count(*) into i from user_tables where table_name='ELE_GP_AGENCY
';
 if i=0 then
 execute immediate '
create table ELE_GP_AGENCY
(
  chr_id                VARCHAR2(38) not null,
  chr_code              VARCHAR2(42),
  disp_code             VARCHAR2(42) not null,
  chr_name              VARCHAR2(60) not null,
  level_num             NUMBER(2) default 0 not null,
  is_leaf               NUMBER(1) default 0 not null,
  enabled               NUMBER(1) default 1 not null,
  create_date           VARCHAR2(30),
  create_user           VARCHAR2(42),
  latest_op_date        VARCHAR2(30) not null,
  is_deleted            NUMBER default 0 not null,
  latest_op_user        VARCHAR2(42),
  last_ver              VARCHAR2(30),
  chr_code1             VARCHAR2(42),
  chr_code2             VARCHAR2(42),
  chr_code3             VARCHAR2(42),
  chr_code4             VARCHAR2(42),
  chr_code5             VARCHAR2(42),
  RG_CODE               VARCHAR2(42) not null,
  parent_id             VARCHAR2(38),
  chr_id1               VARCHAR2(38),
  chr_id2               VARCHAR2(38),
  chr_id3               VARCHAR2(38),
  chr_id4               VARCHAR2(38),
  chr_id5               VARCHAR2(38),
  postalcode            VARCHAR2(10),
  address               VARCHAR2(80),
  email                 VARCHAR2(30),
  homepage              VARCHAR2(40),
  art_person            VARCHAR2(20),
  art_per_card          VARCHAR2(30),
  register_date         VARCHAR2(30),
  operation_scope       VARCHAR2(200),
  registered_fund       NUMBER(16,2),
  principal_idcred      VARCHAR2(30),
  linkman               VARCHAR2(20),
  phone                 VARCHAR2(20),
  fax                   VARCHAR2(20),
  company_intro         VARCHAR2(200),
  company_qualification VARCHAR2(100),
  set_year              NUMBER(4) not null
)'
;
end if;

execute immediate 'comment on table ELE_GP_AGENCY
  is ''�����ɹ�������-�����ɹ��н���������''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ�����룬
 �����ɹ��н��������������''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_GP_AGENCY.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ���������������''';
execute immediate 'comment on column ELE_GP_AGENCY.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_GP_AGENCY.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_GP_AGENCY.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_GP_AGENCY.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_GP_AGENCY.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_GP_AGENCY.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_GP_AGENCY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_AGENCY.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_GP_AGENCY.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_AGENCY.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_AGENCY.postalcode
  is ''��������''';
execute immediate 'comment on column ELE_GP_AGENCY.address
  is ''��ַ''';
execute immediate 'comment on column ELE_GP_AGENCY.email
  is ''�ʼ���ַ''';
execute immediate 'comment on column ELE_GP_AGENCY.homepage
  is ''��վ��ַ''';
execute immediate 'comment on column ELE_GP_AGENCY.art_person
  is ''���˴���''';
execute immediate 'comment on column ELE_GP_AGENCY.art_per_card
  is ''���˴������֤����''';
execute immediate 'comment on column ELE_GP_AGENCY.register_date
  is ''��������''';
execute immediate 'comment on column ELE_GP_AGENCY.operation_scope
  is ''ҵ��Χ''';
execute immediate 'comment on column ELE_GP_AGENCY.registered_fund
  is ''ע���ʽ�''';
execute immediate 'comment on column ELE_GP_AGENCY.principal_idcred
  is ''���������֤����''';
execute immediate 'comment on column ELE_GP_AGENCY.linkman
  is ''��ϵ��''';
execute immediate 'comment on column ELE_GP_AGENCY.phone
  is ''�绰''';
execute immediate 'comment on column ELE_GP_AGENCY.fax
  is ''����''';
execute immediate 'comment on column ELE_GP_AGENCY.company_intro
  is ''��˾����''';
execute immediate 'comment on column ELE_GP_AGENCY.company_qualification
  is ''��˾����''';
execute immediate 'comment on column ELE_GP_AGENCY.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'alter table ELE_GP_AGENCY
  add constraint PAYTYPE_GP_AGENCY primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_GP_BARN
';
 if i=0 then
 execute immediate '
create table ELE_GP_BARN
(
  chr_id          VARCHAR2(38) not null,
  chr_code        VARCHAR2(42),
  disp_code       VARCHAR2(42) not null,
  chr_name        VARCHAR2(60) not null,
  level_num       NUMBER(2) default 0 not null,
  is_leaf         NUMBER(1) default 0 not null,
  enabled         NUMBER(1) default 1 not null,
  create_date     VARCHAR2(30),
  create_user     VARCHAR2(42),
  latest_op_date  VARCHAR2(30) not null,
  is_deleted      NUMBER default 0 not null,
  latest_op_user  VARCHAR2(42),
  last_ver        VARCHAR2(30),
  chr_code1       VARCHAR2(42),
  chr_code2       VARCHAR2(42),
  chr_code3       VARCHAR2(42),
  chr_code4       VARCHAR2(42),
  chr_code5       VARCHAR2(42),
  RG_CODE         VARCHAR2(42) not null,
  parent_id       VARCHAR2(38),
  chr_id1         VARCHAR2(38),
  chr_id2         VARCHAR2(38),
  chr_id3         VARCHAR2(38),
  chr_id4         VARCHAR2(38),
  chr_id5         VARCHAR2(38),
  set_year        NUMBER(4) not null,
  prov_id         VARCHAR2(38),
  GP_agency_id       VARCHAR2(38),
  dir_id          VARCHAR2(38),
  fasn_id         VARCHAR2(38),
  mode_id         VARCHAR2(38),
  carspec_id      VARCHAR2(38),
  is_energysav    NUMBER(1),
  barn_money      NUMBER(16,2),
  barn_in_money   NUMBER(16,2),
  barn_out_money  NUMBER(16,2),
  barn_self_money NUMBER(16,2),
  price           NUMBER(16,2),
  amount          NUMBER(16),
  unite           VARCHAR2(20),
  spectype        VARCHAR2(1000),
  barn_date       VARCHAR2(20),
  is_three        NUMBER(1) default 0,
  barn_term       VARCHAR2(20),
  item_id         VARCHAR2(38),
  gpplan_id       VARCHAR2(38),
  brand_id        VARCHAR2(38),
  entype          NUMBER(1) default 1,
  MB_ID    VARCHAR2(38),
  AGENCY_ID           VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on table ELE_GP_BARN
  is ''�����ɹ���Ŀ��''';
execute immediate 'comment on column ELE_GP_BARN.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ�����룬֧����ʽID''';
execute immediate 'comment on column ELE_GP_BARN.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_GP_BARN.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_GP_BARN.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_GP_BARN.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_GP_BARN.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_GP_BARN.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_GP_BARN.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_GP_BARN.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_GP_BARN.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_GP_BARN.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_BARN.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_BARN.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_BARN.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_BARN.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_BARN.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_GP_BARN.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_GP_BARN.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_BARN.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_BARN.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_BARN.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_BARN.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_BARN.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_GP_BARN.prov_id
  is ''�б깩Ӧ��ID''';
execute immediate 'comment on column ELE_GP_BARN.GP_agency_id
  is ''�����������''';
execute immediate 'comment on column ELE_GP_BARN.carspec_id
  is ''��������''';
execute immediate 'comment on column ELE_GP_BARN.is_energysav
  is ''�Ƿ����''';
execute immediate 'comment on column ELE_GP_BARN.barn_money
  is ''��ͬ���''';
execute immediate 'comment on column ELE_GP_BARN.amount
  is ''����''';
execute immediate 'comment on column ELE_GP_BARN.barn_term
  is ''��Լʱ��''';
execute immediate 'comment on column ELE_GP_BARN.item_id
  is ''�ɹ���Ŀ��ϸID''';
execute immediate 'comment on column ELE_GP_BARN.gpplan_id
  is ''�ɹ���ĿID''';
execute immediate 'comment on column ELE_GP_BARN.brand_id
  is ''�ɹ�Ʒ��ID''';
execute immediate 'comment on column ELE_GP_BARN.entype
  is ''��λ����''';
execute immediate 'alter table ELE_GP_BARN
  add constraint PK_ELE_GP_BARN primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_GP_DIRECTORY
';
 if i=0 then
 execute immediate '
create table ELE_GP_DIRECTORY
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  check_symbol   NUMBER(1) default 0,
  price          NUMBER(16,2),
  amount         VARCHAR2(10),
  remark         VARCHAR2(100),
  dirtype        NUMBER(1),
  unit           VARCHAR2(10),
  varl_type      NUMBER(1),
  chr_code6      VARCHAR2(42),
  chr_id6        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on table ELE_GP_DIRECTORY
  is ''�����ɹ�������-�����ɹ�Ŀ¼��
 ELE_GP_DIRECTORY''';
execute immediate 'comment on column ELE_GP_DIRECTORY.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ�����룬
 �����ɹ�Ŀ¼���������''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_GP_DIRECTORY.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_GP_DIRECTORY.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_GP_DIRECTORY.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_GP_DIRECTORY.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_GP_DIRECTORY.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_GP_DIRECTORY.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_GP_DIRECTORY.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_GP_DIRECTORY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_DIRECTORY.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_GP_DIRECTORY.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_DIRECTORY.check_symbol
  is ''��˱�־----��Ҫҵ���һ�ֳ���˵�������Ʒ�ı�־��
 Ĭ��ֵ0����1����
 ''';
execute immediate 'comment on column ELE_GP_DIRECTORY.price
  is ''�ο�����''';
execute immediate 'comment on column ELE_GP_DIRECTORY.amount
  is ''�ο�����''';
execute immediate 'comment on column ELE_GP_DIRECTORY.dirtype
  is ''Ŀ¼���� 1 Э�� 2 ���� 3 ����''';
execute immediate 'comment on column ELE_GP_DIRECTORY.unit
  is ''�ο�������λ''';
execute immediate 'comment on column ELE_GP_DIRECTORY.varl_type
  is ''ƷĿ���� 1���� 2���� 3����''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_code6
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_DIRECTORY.chr_id6
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index IDX_GP_D_1 on ELE_GP_DIRECTORY (CHR_CODE)
  ';
execute immediate 'alter table ELE_GP_DIRECTORY
  add constraint PK_GP_DIRECTORY primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_GP_PLAN
';
 if i=0 then
 execute immediate '
create table ELE_GP_PLAN
(
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  set_year       NUMBER(4) not null
)'
;
end if;

execute immediate 'comment on table ELE_GP_PLAN
  is ''�����ɹ���Ŀ��''';
execute immediate 'comment on column ELE_GP_PLAN.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ�����룬֧����ʽID''';
execute immediate 'comment on column ELE_GP_PLAN.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_GP_PLAN.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_GP_PLAN.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_GP_PLAN.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_GP_PLAN.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_GP_PLAN.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_GP_PLAN.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_GP_PLAN.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_GP_PLAN.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_GP_PLAN.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_PLAN.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_PLAN.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_PLAN.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_PLAN.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_GP_PLAN.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_GP_PLAN.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_GP_PLAN.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_PLAN.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_PLAN.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_PLAN.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_PLAN.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_GP_PLAN.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'alter table ELE_GP_PLAN
  add constraint PK_ELE_GP_PLAN primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_HOLD1
';
 if i=0 then
 execute immediate '
create table ELE_HOLD1
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) not null,
  is_leaf        NUMBER(1) not null,
  enabled        NUMBER(1) not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_HOLD1.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD1.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD1.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD1.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD1.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD1.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD1.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD1.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD1.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD1.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_HOLD1_N2 on ELE_HOLD1 (CHR_CODE)';
execute immediate 'alter table ELE_HOLD1
  add constraint ELE_HOLD1_PAYTYPE primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_HOLD2
';
 if i=0 then
 execute immediate '
create table ELE_HOLD2
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) not null,
  is_leaf        NUMBER(1) not null,
  enabled        NUMBER(1) not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_HOLD2.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD2.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD2.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD2.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD2.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD2.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD2.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD2.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD2.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD2.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_HOLD2_N2 on ELE_HOLD2 (CHR_CODE)
  ';
execute immediate 'alter table ELE_HOLD2
  add constraint ELE_HOLD2_PAYTYPE primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_HOLD3
';
 if i=0 then
 execute immediate '
create table ELE_HOLD3
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) not null,
  is_leaf        NUMBER(1) not null,
  enabled        NUMBER(1) not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_HOLD3.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD3.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD3.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD3.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD3.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_HOLD3.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD3.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD3.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD3.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_HOLD3.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_HOLD3_N2 on ELE_HOLD3 (CHR_CODE)
 ' ;
execute immediate 'alter table ELE_HOLD3
  add constraint ELE_HOLD3_PAYTYPE primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_INCOMEPAYMANAGE
';
 if i=0 then
 execute immediate '
create table ELE_INCOMEPAYMANAGE
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_INCOMEPAYMANAGE.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_INCOMEPAYMANAGE.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_INCOMEPAYMANAGE.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_INCOMEPAYMANAGE.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_INCOMEPAYMANAGE_N1 on ELE_INCOMEPAYMANAGE (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_INCOMEPAYMANAGE_N2 on ELE_INCOMEPAYMANAGE (CHR_CODE)';
  
execute immediate 'alter table ELE_INCOMEPAYMANAGE
  add constraint ELE_INCOMEPAYMANAGE_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_INCOME_ITEM
';
 if i=0 then
 execute immediate '
create table ELE_INCOME_ITEM
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_INCOME_ITEM.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_INCOME_ITEM.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_INCOME_ITEM.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_INCOME_ITEM.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_INCOME_ITEM.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_INCOME_ITEM.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_INCOME_ITEM.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_INCOME_ITEM.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_INCOME_ITEM.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_INCOME_ITEM.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_INCOME_ITEM.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_INCOME_ITEM.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_INCOME_ITEM_N1 on ELE_INCOME_ITEM (SET_YEAR)
  ';
execute immediate 'create index ELE_INCOME_ITEM_N2 on ELE_INCOME_ITEM (CHR_CODE)
 ' ;
execute immediate 'alter table ELE_INCOME_ITEM
  add constraint ELE_INCOME_ITEM_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_MANAGE_BRANCH
';
 if i=0 then
 execute immediate '
create table ELE_MANAGE_BRANCH
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  abbreviation   VARCHAR2(10)
)'
;
end if;

execute immediate 'comment on column ELE_MANAGE_BRANCH.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_MANAGE_BRANCH.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_MANAGE_BRANCH.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MANAGE_BRANCH.abbreviation
  is ''���Ҽ��''';
execute immediate 'create index ELE_MANAGE_BRANCH_N1 on ELE_MANAGE_BRANCH (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_MANAGE_BRANCH_N2 on ELE_MANAGE_BRANCH (CHR_CODE)';
  
execute immediate 'alter table ELE_MANAGE_BRANCH
  add constraint ELE_MANAGE_BRANCH_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_MONEY_KIND
';
 if i=0 then
 execute immediate '
create table ELE_MONEY_KIND
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_MONEY_KIND.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_MONEY_KIND.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_MONEY_KIND.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_MONEY_KIND.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_MONEY_KIND.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_MONEY_KIND.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_MONEY_KIND.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_MONEY_KIND.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_MONEY_KIND.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_MONEY_KIND.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_MONEY_KIND.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_MONEY_KIND.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_MONEY_KIND_N1 on ELE_MONEY_KIND (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_MONEY_KIND_N2 on ELE_MONEY_KIND (CHR_CODE)';
  
execute immediate 'alter table ELE_MONEY_KIND
  add constraint ELE_MONEY_KIND_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_PAYEE_ACCOUNT
';
 if i=0 then
 execute immediate '
create table ELE_PAYEE_ACCOUNT
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  account_no     VARCHAR2(42),
  account_name   VARCHAR2(60),
  payee_name     VARCHAR2(200),
  bank_code      VARCHAR2(42),
  bank_name      VARCHAR2(60),
  account_type   NUMBER(4) not null,
  owner_code     VARCHAR2(42),
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  start_date     VARCHAR2(38),
  stop_date      VARCHAR2(38),
  is_default     NUMBER(1),
  remark         VARCHAR2(200),
  bankflag       VARCHAR2(38) default ''000'',
  company        VARCHAR2(255),
  contact        VARCHAR2(200),
  phone          VARCHAR2(40),
  is_corporation NUMBER(1) default 0,
  payee_rg_code  VARCHAR2(42),
  is_public      NUMBER(1) default 0,
  st_id          VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_PAYEE_ACCOUNT.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_code
  is ''Ϊ�ۺϲ�ѯ�����ı���''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.disp_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_PAYEE_ACCOUNT.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_PAYEE_ACCOUNT.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.account_no
  is ''�˻���Ӧ�˺�''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.account_name
  is ''�˻�����''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.payee_name
  is ''�տ�������''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.bank_code
  is ''�˻���Ӧ�����б���''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.bank_name
  is ''�˻���Ӧ����������''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.account_type
  is ''�˻�����
 6x���տ����˻�''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.owner_code
  is ''�궨���˻���������
 д���տ����˻���Ӧ�Ļ�������''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.start_date
  is ''�˻���������''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.stop_date
  is ''�˻�ͣ������''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.remark
  is ''��ע''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.bankflag
  is ''�տ����б�''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.company
  is ''�տ��˵�λ����''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.contact
  is ''��λ������''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.phone
  is ''��ϵ�绰''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.is_corporation
  is ''�Ƿ���ҵ�˻�''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.is_public
  is ''�Ƿ񹫹��տ���''';
execute immediate 'comment on column ELE_PAYEE_ACCOUNT.st_id
  is ''����ID''';
execute immediate 'create index ELE_PAYEE_ACCOUNT_N1 on ELE_PAYEE_ACCOUNT (SET_YEAR)
 ' ;
execute immediate 'create index ELE_PAYEE_ACCOUNT_N2 on ELE_PAYEE_ACCOUNT (CHR_CODE)
 ' ;
execute immediate 'create index ELE_PAYEE_ACCOUNT_N3 on ELE_PAYEE_ACCOUNT (OWNER_CODE)
  ';
execute immediate 'alter table ELE_PAYEE_ACCOUNT
  add constraint ELE_PAYEE_ACCOUNT_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_PAYOFF_KIND
';
 if i=0 then
 execute immediate '
create table ELE_PAYOFF_KIND
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_PAYOFF_KIND.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_PAYOFF_KIND.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_PAYOFF_KIND.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_PAYOFF_KIND.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_PAYOFF_KIND.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_PAYOFF_KIND.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_PAYOFF_KIND.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_PAYOFF_KIND.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_PAYOFF_KIND.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAYOFF_KIND.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_PAYOFF_KIND.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAYOFF_KIND.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_PAYOFF_KIND_N1 on ELE_PAYOFF_KIND (SET_YEAR)';
  
execute immediate 'create index ELE_PAYOFF_KIND_N2 on ELE_PAYOFF_KIND (CHR_CODE)';
  
execute immediate 'alter table ELE_PAYOFF_KIND
  add constraint ELE_PAYOFF_KIND_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_PAY_FASHION
';
 if i=0 then
 execute immediate '
create table ELE_PAY_FASHION
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_PAY_FASHION.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_PAY_FASHION.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_PAY_FASHION.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_PAY_FASHION.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_PAY_FASHION.enabled
  is ''�궨��Ҫ���Ƿ�����''';
execute immediate 'comment on column ELE_PAY_FASHION.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_PAY_FASHION.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_PAY_FASHION.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_PAY_FASHION.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_FASHION.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_PAY_FASHION.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_FASHION.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_PAY_FASHION_N1 on ELE_PAY_FASHION (SET_YEAR, RG_CODE)';
  
execute immediate 'create index ELE_PAY_FASHION_N2 on ELE_PAY_FASHION (CHR_CODE)';
  
execute immediate 'alter table ELE_PAY_FASHION
  add constraint ELE_PAY_FASHION_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_PAY_MODE
';
 if i=0 then
 execute immediate '
create table ELE_PAY_MODE
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_PAY_MODE.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_PAY_MODE.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_PAY_MODE.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_PAY_MODE.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_PAY_MODE.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_PAY_MODE.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_PAY_MODE.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_PAY_MODE.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_PAY_MODE.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_PAY_MODE.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_PAY_MODE.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_PAY_MODE.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_MODE.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_MODE.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_MODE.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_MODE.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_MODE.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_PAY_MODE.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_PAY_MODE.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_MODE.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_MODE.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_MODE.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_MODE.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_PAY_MODE_N1 on ELE_PAY_MODE (SET_YEAR)';
  
execute immediate 'create index ELE_PAY_MODE_N2 on ELE_PAY_MODE (CHR_CODE)';
  
execute immediate 'alter table ELE_PAY_MODE
  add constraint ELE_PAY_MODE_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_PAY_SUMMARY
';
 if i=0 then
 execute immediate '
create table ELE_PAY_SUMMARY
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(200) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_PAY_SUMMARY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_PAY_SUMMARY.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'create index ELE_PAY_SUMMARY_N2 on ELE_PAY_SUMMARY (CHR_CODE)
  ';
execute immediate 'alter table ELE_PAY_SUMMARY
  add constraint ELE_PAY_SUMMARY_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_REGION
';
 if i=0 then
 execute immediate '
create table ELE_REGION
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38),
  is_top         NUMBER(1) default 0 not null,
  is_valid       NUMBER(1) default 0 not null
)'
;
end if;

execute immediate 'comment on column ELE_REGION.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_REGION.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_REGION.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_REGION.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_REGION.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_REGION.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_REGION.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_REGION.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_REGION.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_REGION.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_REGION.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_REGION.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_REGION.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_REGION.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_REGION.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_REGION.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_REGION.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_REGION.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_REGION.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_REGION.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_REGION.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_REGION.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_REGION.is_top
  is ''�Ƿ������ö�������,0:δ���� 1:������''';
execute immediate 'comment on column ELE_REGION.is_valid
  is ''�Ƿ���������,0:δ���� 1:������''';
execute immediate 'create index ELE_REGION_N1 on ELE_REGION (SET_YEAR)';
execute immediate 'create index ELE_REGION_N2 on ELE_REGION (CHR_CODE)';
execute immediate 'alter table ELE_REGION
  add constraint ELE_REGION_PK primary key (CHR_ID, SET_YEAR)';

  
  
select count(*) into i from user_tables where table_name='ELE_SALARYTAG
';
 if i=0 then
 execute immediate '
create table ELE_SALARYTAG
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(60) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_SALARYTAG.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_SALARYTAG.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_SALARYTAG.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_SALARYTAG.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_SALARYTAG.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_SALARYTAG.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_SALARYTAG.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_SALARYTAG.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_SALARYTAG.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_SALARYTAG.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_SALARYTAG.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_SALARYTAG.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_SALARYTAG.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_SALARYTAG.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_SALARYTAG.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_SALARYTAG.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_SALARYTAG.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_SALARYTAG.parent_id
  is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_SALARYTAG.chr_id1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SALARYTAG.chr_id2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SALARYTAG.chr_id3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SALARYTAG.chr_id4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SALARYTAG.chr_id5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'alter table ELE_SALARYTAG
  add constraint ELE_SALARYTAG_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='ELE_SUMMARY
';
 if i=0 then
 execute immediate '
create table ELE_SUMMARY
(
  set_year       NUMBER(4) not null,
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(42) not null,
  chr_name       VARCHAR2(300) not null,
  level_num      NUMBER(2) default 0 not null,
  is_leaf        NUMBER(1) default 0 not null,
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  is_deleted     NUMBER default 0 not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  chr_code1      VARCHAR2(42),
  chr_code2      VARCHAR2(42),
  chr_code3      VARCHAR2(42),
  chr_code4      VARCHAR2(42),
  chr_code5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  parent_id      VARCHAR2(38),
  chr_id1        VARCHAR2(38),
  chr_id2        VARCHAR2(38),
  chr_id3        VARCHAR2(38),
  chr_id4        VARCHAR2(38),
  chr_id5        VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column ELE_SUMMARY.set_year
  is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_SUMMARY.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_SUMMARY.chr_code
  is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_SUMMARY.disp_code
  is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_SUMMARY.chr_name
  is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_SUMMARY.level_num
  is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_SUMMARY.is_leaf
  is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_SUMMARY.enabled
  is ''�궨��Ҫ���Ƿ�����''';

execute immediate 'comment on column ELE_SUMMARY.create_user
  is ''�����û�''';

execute immediate 'comment on column ELE_SUMMARY.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_SUMMARY.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column ELE_SUMMARY.chr_code1
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SUMMARY.chr_code2
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SUMMARY.chr_code3
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SUMMARY.chr_code4
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SUMMARY.chr_code5
  is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_SUMMARY.RG_CODE
  is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_SUMMARY.parent_id
  is ''��¼�丸��ID''';
execute immediate 'create index ELE_SUMMARY_N1 on ELE_SUMMARY (SET_YEAR, RG_CODE)
 ' ;
execute immediate 'create index ELE_SUMMARY_N2 on ELE_SUMMARY (CHR_CODE)
 ' ;
execute immediate 'alter table ELE_SUMMARY
  add constraint ELE_SUMMARY_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
  
select count(*) into i from user_tables where table_name='GL_BALANCE
';
 if i=0 then
 execute immediate '
create table GL_BALANCE
(
  account_id     VARCHAR2(38) not null,
  sum_id         VARCHAR2(38),
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  latest_op_user VARCHAR2(42),
  set_year       NUMBER(4) not null,
  set_month      NUMBER(2) not null,
  avi_money      NUMBER(16,2) default 0,
  use_money      NUMBER(16,2) default 0,
  minus_money    NUMBER(16,2) default 0,
  aving_money    NUMBER(16,2) default 0,
  last_ver       VARCHAR2(30),
  fromctrlid     VARCHAR2(38),
  remark         VARCHAR2(200),
  bal_version    NUMBER(1) default 1,
  rcid           NUMBER,
  ccid           NUMBER,
  RG_CODE        VARCHAR2(42),
  balance_id     NUMBER,
  AGENCY_ID          VARCHAR2(42),
  MB_ID   VARCHAR2(42)
)'
;
end if;

execute immediate 'comment on column GL_BALANCE.account_id
  is ''��������ID''';
execute immediate 'comment on column GL_BALANCE.sum_id
  is ''���ID''';

execute immediate 'comment on column GL_BALANCE.create_user
  is ''�����û�''';

execute immediate 'comment on column GL_BALANCE.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column GL_BALANCE.set_year
  is ''ҵ�����''';
execute immediate 'comment on column GL_BALANCE.set_month
  is ''��ǰ�·�''';
execute immediate 'comment on column GL_BALANCE.avi_money
  is ''��Ч���''';
execute immediate 'comment on column GL_BALANCE.use_money
  is ''��������''';
execute immediate 'comment on column GL_BALANCE.minus_money
  is ''��;�������''';
execute immediate 'comment on column GL_BALANCE.aving_money
  is ''��;�������''';

execute immediate 'comment on column GL_BALANCE.fromctrlid
  is ''�ϼ����ƶ��ID''';
execute immediate 'comment on column GL_BALANCE.remark
  is ''��ע''';
execute immediate 'comment on column GL_BALANCE.bal_version
  is ''ҵ�����ݰ汾��0��������� 1��ʽָ��''';
execute immediate 'create index GL_BALANCE_CCID_PRO on GL_BALANCE (CCID)
 ' ;
execute immediate 'create index GL_BALANCE_N1_PRO on GL_BALANCE (ACCOUNT_ID, CCID, RCID)
 ' ;
execute immediate 'create index GL_BALANCE_PRO_RCID on GL_BALANCE (RCID)
 ' ;
execute immediate 'create unique index IDX_GL_BALANCE_ACEG on GL_BALANCE (ACCOUNT_ID, CCID, SET_YEAR, RG_CODE)
 ' ;
execute immediate 'create unique index IDX_GL_BALANCE_N1 on GL_BALANCE (AGENCY_ID, ACCOUNT_ID, CCID, RG_CODE, SET_YEAR)
 ' ;
execute immediate 'create unique index IDX_GL_BALANCE_N2 on GL_BALANCE (MB_ID, ACCOUNT_ID, CCID, RG_CODE, SET_YEAR)
' ;
execute immediate 'create index IDX_GL_BALANCE_SM on GL_BALANCE (SUM_ID, SET_MONTH)
  ';
execute immediate 'create index IDX_GL_BALANCE_SUM_ID on GL_BALANCE (SUM_ID)
 ' ;
 
 
 
select count(*) into i from user_tables where table_name='GL_BALANCE_BUDGET_FILE_TRANS
';
 if i=0 then
 execute immediate '
create table GL_BALANCE_BUDGET_FILE_TRANS
(
  s_sum_id       NVARCHAR2(38) not null,
  t_sum_id       NVARCHAR2(38) not null,
  latest_op_date NVARCHAR2(30)
)'
;
end if;

execute immediate 'comment on table GL_BALANCE_BUDGET_FILE_TRANS
  is ''���ˢ��ת����''';
execute immediate 'comment on column GL_BALANCE_BUDGET_FILE_TRANS.s_sum_id
  is ''ԭʼ���id''';
execute immediate 'comment on column GL_BALANCE_BUDGET_FILE_TRANS.t_sum_id
  is ''ת������id''';
execute immediate 'comment on column GL_BALANCE_BUDGET_FILE_TRANS.latest_op_date
  is ''������ʱ��''';

  
  
  
select count(*) into i from user_tables where table_name='GL_BALANCE_CACHE
';
 if i=0 then
 execute immediate '
create global temporary table GL_BALANCE_CACHE
(
  balance_id     NUMBER,
  sum_id         VARCHAR2(38),
  set_year       NUMBER(4),
  RG_CODE        VARCHAR2(42),
  account_id     VARCHAR2(38),
  ccid           NUMBER,
  set_month      NUMBER(2),
  avi_money      NUMBER(16,2) default 0,
  use_money      NUMBER(16,2) default 0,
  minus_money    NUMBER(16,2) default 0,
  aving_money    NUMBER(16,2) default 0,
  remark         VARCHAR2(200),
  bal_version    NUMBER(1),
  is_enforce     NUMBER(1),
  create_date    VARCHAR2(30),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30),
  index_         NUMBER,
  rcid           NUMBER,
  fromctrlid     VARCHAR2(38),
  AGENCY_ID          VARCHAR2(42),
  MB_ID   VARCHAR2(42)
)';
end if;

execute immediate 'create index IDX_BALANCE_CACHE_BALANCE_ID on GL_BALANCE_CACHE (BALANCE_ID)';
execute immediate 'create index IDX_BALANCE_CACHE_SUM_ID on GL_BALANCE_CACHE (SUM_ID)';
execute immediate 'create index IDX_GL_BALANCE_CACHE_ACEG on GL_BALANCE_CACHE (ACCOUNT_ID, CCID, SET_YEAR, RG_CODE)';
execute immediate 'create index IDX_GL_BALANCE_CACHE_SM on GL_BALANCE_CACHE (SUM_ID, SET_MONTH)';



select count(*) into i from user_tables where table_name='GL_BALANCE_MONTH_DETAIL
';
 if i=0 then
 execute immediate '
create table GL_BALANCE_MONTH_DETAIL
(
  account_id     VARCHAR2(38) not null,
  sum_id         VARCHAR2(38),
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  latest_op_user VARCHAR2(42),
  set_year       NUMBER(4) not null,
  set_month      NUMBER(2) not null,
  avi_money      NUMBER(16,2) default 0,
  use_money      NUMBER(16,2) default 0,
  minus_money    NUMBER(16,2) default 0,
  aving_money    NUMBER(16,2) default 0,
  last_ver       VARCHAR2(30),
  fromctrlid     VARCHAR2(38),
  remark         VARCHAR2(200),
  bal_version    NUMBER(1),
  ccid           NUMBER,
  rcid           NUMBER,
  RG_CODE        VARCHAR2(42),
  balance_id     NUMBER not null,
  AGENCY_ID          VARCHAR2(42),
  MB_ID   VARCHAR2(42)
)'
;
end if;

execute immediate 'create index IDX_GL_BALANCE_ACCOUNT_ID on GL_BALANCE_MONTH_DETAIL (ACCOUNT_ID)
 ' ;
execute immediate 'create index IDX_GL_BALANCE_DETAIL_ACEG on GL_BALANCE_MONTH_DETAIL (ACCOUNT_ID, CCID, SET_YEAR, RG_CODE)
  ';
execute immediate 'create index IDX_GL_BALANCE_DETAIL_SM on GL_BALANCE_MONTH_DETAIL (SUM_ID, SET_MONTH)
 ' ;
execute immediate 'create index IDX_GL_BALANCE_FROMCTRLID on GL_BALANCE_MONTH_DETAIL (FROMCTRLID)
  ';
execute immediate 'create index IDX_GL_BALANCE_MD_CCID on GL_BALANCE_MONTH_DETAIL (CCID)
 ' ;
execute immediate 'create index IDX_GL_BALANCE_MD_SUM_ID on GL_BALANCE_MONTH_DETAIL (SUM_ID)
'  ;
execute immediate 'alter table GL_BALANCE_MONTH_DETAIL
  add constraint GL_BALANCE_DETAIL_PAYTYPE primary key (BALANCE_ID)';
  
  
  
  
select count(*) into i from user_tables where table_name='GL_BALANCE_TRACE
';
 if i=0 then
 execute immediate '
create table GL_BALANCE_TRACE
(
  vou_id         VARCHAR2(38),
  set_year       NUMBER(4),
  RG_CODE        VARCHAR2(42),
  vou_type_id    NUMBER,
  ctrlid         NUMBER,
  ctrl_type      VARCHAR2(38),
  ctrl_side      NUMBER(1),
  is_primary     NUMBER(1),
  create_date    VARCHAR2(30),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30)
)'
;
end if;

execute immediate 'create index IDX_VOU on GL_BALANCE_TRACE (VOU_ID, RG_CODE, SET_YEAR, VOU_TYPE_ID)';



select count(*) into i from user_tables where table_name='GL_BALANCE_YEAR_BEGIN_TMP
';
 if i=0 then
 execute immediate '
create table GL_BALANCE_YEAR_BEGIN_TMP
(
  ctrlid        VARCHAR2(38),
  file_id       VARCHAR2(38),
  batch         NUMBER(2),
  budget_vou_id VARCHAR2(38),
  fundtype_id   VARCHAR2(38),
  AGENCY_ID         VARCHAR2(38),
  expfunc_id    VARCHAR2(38),
  agencyexp_id  VARCHAR2(38),
  expeco_id     VARCHAR2(38),
  paytype_id    VARCHAR2(38),
  BGTTYPE_ID         VARCHAR2(38),
  paykind_id    VARCHAR2(38),
  MB_ID  VARCHAR2(38),
  setmode_id    VARCHAR2(38),
  ib_id         VARCHAR2(38),
  bgtdir_id     VARCHAR2(38),
  bp_id         VARCHAR2(38),
  BGTSOURCE_ID  VARCHAR2(38),
  hold1_id      VARCHAR2(38),
  hold2_id      VARCHAR2(38),
  hold3_id      VARCHAR2(38),
  hold4_id      VARCHAR2(38),
  hold5_id      VARCHAR2(38),
  hold6_id      VARCHAR2(38),
  hold7_id      VARCHAR2(38),
  hold8_id      VARCHAR2(38),
  hold9_id      VARCHAR2(38),
  hold10_id     VARCHAR2(38),
  hold11_id     VARCHAR2(38),
  hold12_id     VARCHAR2(38),
  hold13_id     VARCHAR2(38),
  hold14_id     VARCHAR2(38),
  hold15_id     VARCHAR2(38),
  hold16_id     VARCHAR2(38),
  hold17_id     VARCHAR2(38),
  hold18_id     VARCHAR2(38),
  hold19_id     VARCHAR2(38),
  hold20_id     VARCHAR2(38),
  hold21_id     VARCHAR2(38),
  hold22_id     VARCHAR2(38),
  hold23_id     VARCHAR2(38),
  hold24_id     VARCHAR2(38),
  hold25_id     VARCHAR2(38),
  hold26_id     VARCHAR2(38),
  hold27_id     VARCHAR2(38),
  hold28_id     VARCHAR2(38),
  sm_id         VARCHAR2(38),
  hold29_id     VARCHAR2(38),
  hold30_id     VARCHAR2(38)
)'
;
end if;

execute immediate 'create index GL_BALANCE_YEAR_BEGIN_INDEX on GL_BALANCE_YEAR_BEGIN_TMP (CTRLID)
  ';
  
  
  
select count(*) into i from user_tables where table_name='GL_BATCH_CCID_HIS
';
 if i=0 then
 execute immediate '
create table GL_BATCH_CCID_HIS
(
  old_ccid VARCHAR2(20),
  new_ccid VARCHAR2(20)
)'
;
end if;

execute immediate 'create index GL_BATCH_CCID_HIS_IDX1 on GL_BATCH_CCID_HIS (OLD_CCID)
 ' ;
execute immediate 'create index GL_BATCH_CCID_HIS_IDX2 on GL_BATCH_CCID_HIS (NEW_CCID)
 ' ;
 
 
 
select count(*) into i from user_tables where table_name='GL_BATCH_CCID_TABLE
';
 if i=0 then
 execute immediate '
create table GL_BATCH_CCID_TABLE
(
  table_name VARCHAR2(40),
  remark     VARCHAR2(200),
  view_code  VARCHAR2(40)
)'
;
end if;

execute immediate 'comment on column GL_BATCH_CCID_TABLE.table_name
  is ''ʹ�õ���CCID�ı���''';
execute immediate 'comment on column GL_BATCH_CCID_TABLE.remark
  is ''������ע''';
  
  
  
select count(*) into i from user_tables where table_name='GL_BUSVOU_ACCTMDL
';
 if i=0 then
 execute immediate '
create table GL_BUSVOU_ACCTMDL
(
  acctmdl_id        NUMBER not null,
  set_year          NUMBER(4) not null,
  RG_CODE           VARCHAR2(42) not null,
  vou_type_id       VARCHAR2(38),
  account_id        VARCHAR2(38) not null,
  entry_side        NUMBER(1),
  is_primary_source NUMBER(1),
  is_primary_target NUMBER(1),
  ctrl_level        NUMBER(1),
  rule_id           VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column GL_BUSVOU_ACCTMDL.ctrl_level
  is ''0 ������ 1 ������� 2 �ϸ����''';
execute immediate 'alter table GL_BUSVOU_ACCTMDL
  add constraint GL_BUSVOU_ACCTMDL_PAYTYPE primary key (ACCTMDL_ID, SET_YEAR, RG_CODE, ACCOUNT_ID)';
  
  
  
select count(*) into i from user_tables where table_name='GL_BUSVOU_TYPE
';
 if i=0 then
 execute immediate '
create table GL_BUSVOU_TYPE
(
  vou_type_id        VARCHAR2(38) not null,
  vou_type_code      VARCHAR2(42) not null,
  vou_type_name      VARCHAR2(42),
  latest_op_date     VARCHAR2(30) not null,
  latest_op_user     VARCHAR2(42),
  is_manual          NUMBER(1) default 1,
  last_ver           VARCHAR2(30),
  set_year           NUMBER(4) not null,
  ds_id              VARCHAR2(38),
  cs_id              VARCHAR2(38),
  st_id              VARCHAR2(38),
  datasource_setting VARCHAR2(300),
  RG_CODE            VARCHAR2(42),
  enabled            NUMBER(1)
)'
;
end if;


execute immediate 'comment on column GL_BUSVOU_TYPE.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column GL_BUSVOU_TYPE.set_year
  is ''ҵ�����''';
execute immediate 'comment on column GL_BUSVOU_TYPE.st_id
  is ''����ID''';
execute immediate 'alter table GL_BUSVOU_TYPE
  add constraint GL_BUSVOU_TYPE_PAYTYPE primary key (VOU_TYPE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='GL_BUSVOU_TYPE_BYTE
';
 if i=0 then
 execute immediate '
create table GL_BUSVOU_TYPE_BYTE
(
  bus_vou_type_byte BLOB,
  set_year          NUMBER(6),
  RG_CODE           VARCHAR2(42)
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_CCIDS
';
 if i=0 then
 execute immediate '
create table GL_CCIDS
(
  latest_op_date      VARCHAR2(30) not null,
  latest_op_user      VARCHAR2(42),
  coa_id              VARCHAR2(38),
  as_id               VARCHAR2(38),
  AGENCY_ID               VARCHAR2(38),
  fundtype_id         VARCHAR2(38),
  expfunc_id          VARCHAR2(38),
  agencyexp_id        VARCHAR2(38),
  expeco_id           VARCHAR2(38),
  paytype_id          VARCHAR2(38),
  BGTTYPE_ID               VARCHAR2(38),
  paykind_id          VARCHAR2(38),
  MB_ID        VARCHAR2(38),
  file_id             VARCHAR2(38),
  setmode_id          VARCHAR2(38),
  in_bs_id            VARCHAR2(38),
  in_bis_id           VARCHAR2(38),
  cb_id               VARCHAR2(38),
  pb_id               VARCHAR2(38),
  ib_id               VARCHAR2(38),
  bac_id              VARCHAR2(38),
  bap_id              VARCHAR2(38),
  bai_id              VARCHAR2(38),
  bgtdir_id           VARCHAR2(38),
  bp_id               VARCHAR2(38),
  BGTSOURCE_ID        VARCHAR2(38),
  hold1_id            VARCHAR2(38),
  hold2_id            VARCHAR2(38),
  hold3_id            VARCHAR2(38),
  hold4_id            VARCHAR2(38),
  hold5_id            VARCHAR2(38),
  hold6_id            VARCHAR2(38),
  hold7_id            VARCHAR2(38),
  hold8_id            VARCHAR2(38),
  hold9_id            VARCHAR2(38),
  hold10_id           VARCHAR2(38),
  hold11_id           VARCHAR2(38),
  hold12_id           VARCHAR2(38),
  hold13_id           VARCHAR2(38),
  hold14_id           VARCHAR2(38),
  hold15_id           VARCHAR2(38),
  hold16_id           VARCHAR2(38),
  hold17_id           VARCHAR2(38),
  hold18_id           VARCHAR2(38),
  hold19_id           VARCHAR2(38),
  hold20_id           VARCHAR2(38),
  hold21_id           VARCHAR2(38),
  hold22_id           VARCHAR2(38),
  hold23_id           VARCHAR2(38),
  hold24_id           VARCHAR2(38),
  hold25_id           VARCHAR2(38),
  hold26_id           VARCHAR2(38),
  hold27_id           VARCHAR2(38),
  hold28_id           VARCHAR2(38),
  hold29_id           VARCHAR2(38),
  hold30_id           VARCHAR2(38),
  set_year            NUMBER(4) not null,
  bis_id              VARCHAR2(38),
  inpm_id             VARCHAR2(38),
  st_id               VARCHAR2(38),
  ct_id               VARCHAR2(38),
  sm_id               VARCHAR2(38),
  opuser_id           VARCHAR2(38),
  editor_id           VARCHAR2(38),
  ZFCGBS_ID               VARCHAR2(38),
  fm_id               VARCHAR2(38),
  as_code             VARCHAR2(42),
  as_name             VARCHAR2(60),
  AGENCY_CODE             VARCHAR2(42),
  AGENCY_NAME             VARCHAR2(100),
  fundtype_code       VARCHAR2(42),
  fundtype_name       VARCHAR2(60),
  expfunc_code        VARCHAR2(42),
  expfunc_name        VARCHAR2(60),
  agencyexp_code      VARCHAR2(42),
  agencyexp_name      VARCHAR2(60),
  expeco_code         VARCHAR2(42),
  expeco_name         VARCHAR2(60),
  paytype_code        VARCHAR2(42),
  paytype_name        VARCHAR2(60),
  BGTTYPE_CODE             VARCHAR2(42),
  BGTTYPE_NAME             VARCHAR2(60),
  paykind_code        VARCHAR2(42),
  paykind_name        VARCHAR2(60),
  MB_CODE      VARCHAR2(42),
  MB_NAME      VARCHAR2(60),
  file_code           VARCHAR2(42),
  file_name           VARCHAR2(200),
  setmode_code        VARCHAR2(42),
  setmode_name        VARCHAR2(60),
  in_bs_code          VARCHAR2(42),
  in_bs_name          VARCHAR2(60),
  in_bis_code         VARCHAR2(42),
  in_bis_name         VARCHAR2(200),
  cb_code             VARCHAR2(42),
  cb_name             VARCHAR2(60),
  pb_code             VARCHAR2(42),
  pb_name             VARCHAR2(60),
  ib_code             VARCHAR2(42),
  ib_name             VARCHAR2(60),
  bac_code            VARCHAR2(42),
  bac_name            VARCHAR2(60),
  bap_code            VARCHAR2(42),
  bap_name            VARCHAR2(60),
  bai_code            VARCHAR2(42),
  bai_name            VARCHAR2(60),
  bgtdir_code         VARCHAR2(42),
  bgtdir_name         VARCHAR2(60),
  bp_code             VARCHAR2(42),
  bp_name             VARCHAR2(60),
  BGTSOURCE_CODE      VARCHAR2(42),
  BGTSOURCE_NAME      VARCHAR2(60),
  hold1_code          VARCHAR2(42),
  hold1_name          VARCHAR2(60),
  hold2_code          VARCHAR2(42),
  hold2_name          VARCHAR2(60),
  hold3_code          VARCHAR2(42),
  hold3_name          VARCHAR2(60),
  hold4_code          VARCHAR2(42),
  hold4_name          VARCHAR2(60),
  hold5_code          VARCHAR2(42),
  hold5_name          VARCHAR2(60),
  hold6_code          VARCHAR2(42),
  hold6_name          VARCHAR2(60),
  hold7_code          VARCHAR2(42),
  hold7_name          VARCHAR2(60),
  hold8_code          VARCHAR2(42),
  hold8_name          VARCHAR2(60),
  hold9_code          VARCHAR2(42),
  hold9_name          VARCHAR2(60),
  hold10_code         VARCHAR2(42),
  hold10_name         VARCHAR2(60),
  hold11_code         VARCHAR2(42),
  hold11_name         VARCHAR2(60),
  hold12_code         VARCHAR2(42),
  hold12_name         VARCHAR2(60),
  hold13_code         VARCHAR2(42),
  hold13_name         VARCHAR2(60),
  hold14_code         VARCHAR2(42),
  hold14_name         VARCHAR2(60),
  hold15_code         VARCHAR2(42),
  hold15_name         VARCHAR2(60),
  hold16_code         VARCHAR2(42),
  hold16_name         VARCHAR2(60),
  hold17_code         VARCHAR2(42),
  hold17_name         VARCHAR2(60),
  hold18_code         VARCHAR2(42),
  hold18_name         VARCHAR2(60),
  hold19_code         VARCHAR2(42),
  hold19_name         VARCHAR2(60),
  hold20_code         VARCHAR2(42),
  hold20_name         VARCHAR2(60),
  hold21_code         VARCHAR2(42),
  hold21_name         VARCHAR2(60),
  hold22_code         VARCHAR2(42),
  hold22_name         VARCHAR2(60),
  hold23_code         VARCHAR2(42),
  hold23_name         VARCHAR2(60),
  hold24_code         VARCHAR2(42),
  hold24_name         VARCHAR2(60),
  hold25_code         VARCHAR2(42),
  hold25_name         VARCHAR2(60),
  hold26_code         VARCHAR2(42),
  hold26_name         VARCHAR2(60),
  hold27_code         VARCHAR2(42),
  hold27_name         VARCHAR2(60),
  hold28_code         VARCHAR2(42),
  hold28_name         VARCHAR2(60),
  hold29_code         VARCHAR2(42),
  hold29_name         VARCHAR2(60),
  hold30_code         VARCHAR2(42),
  hold30_name         VARCHAR2(60),
  bis_code            VARCHAR2(42),
  bis_name            VARCHAR2(2000),
  inpm_code           VARCHAR2(42),
  inpm_name           VARCHAR2(60),
  st_code             VARCHAR2(42),
  st_name             VARCHAR2(60),
  ct_code             VARCHAR2(42),
  ct_name             VARCHAR2(60),
  sm_code             VARCHAR2(42),
  sm_name             VARCHAR2(300),
  opuser_code         VARCHAR2(42),
  opuser_name         VARCHAR2(60),
  editor_code         VARCHAR2(42),
  editor_name         VARCHAR2(60),
  ZFCGBS_CODE             VARCHAR2(42),
  ZFCGBS_NAME             VARCHAR2(60),
  fm_code             VARCHAR2(42),
  fm_name             VARCHAR2(60),
  pay_account_no      VARCHAR2(60),
  pay_account_name    VARCHAR2(60),
  pay_account_bank    VARCHAR2(60),
  clear_account_no    VARCHAR2(60),
  clear_account_name  VARCHAR2(60),
  clear_account_bank  VARCHAR2(60),
  income_account_no   VARCHAR2(60),
  income_account_name VARCHAR2(60),
  income_account_bank VARCHAR2(60),
  pf_id               VARCHAR2(38),
  pf_code             VARCHAR2(42),
  pf_name             VARCHAR2(60),
  groupid_id          VARCHAR2(38),
  groupid_code        VARCHAR2(42),
  groupid_name        VARCHAR2(60),
  useen_id            VARCHAR2(38),
  useen_code          VARCHAR2(42),
  useen_name          VARCHAR2(60),
  prov_id             VARCHAR2(38),
  prov_code           VARCHAR2(42),
  prov_name           VARCHAR2(60),
  GP_agency_id           VARCHAR2(38),
  GP_agency_code         VARCHAR2(42),
  GP_agency_name         VARCHAR2(60),
  mode_id             VARCHAR2(38),
  mode_code           VARCHAR2(42),
  mode_name           VARCHAR2(60),
  fasn_id             VARCHAR2(38),
  fasn_code           VARCHAR2(42),
  fasn_name           VARCHAR2(60),
  dir_id              VARCHAR2(38),
  dir_code            VARCHAR2(42),
  dir_name            VARCHAR2(60),
  gpplan_id           VARCHAR2(38),
  gpplan_code         VARCHAR2(42),
  gpplan_name         VARCHAR2(60),
  gpbarn_id           VARCHAR2(38),
  gpbarn_code         VARCHAR2(42),
  gpbarn_name         VARCHAR2(60),
  wa_id               VARCHAR2(38),
  wa_code             VARCHAR2(42),
  wa_name             VARCHAR2(60),
  ff_id               VARCHAR2(38),
  ff_name             VARCHAR2(60),
  ff_code             VARCHAR2(42),
  ien_id              VARCHAR2(38),
  ien_name            VARCHAR2(60),
  ien_code            VARCHAR2(42),
  GZBS_ID           VARCHAR2(38),
  GZBS_NAME         VARCHAR2(60),
  GZBS_CODE         VARCHAR2(42),
  bc_id               VARCHAR2(38),
  bc_code             VARCHAR2(42),
  bc_name             VARCHAR2(60),
  bso_id              VARCHAR2(38),
  bso_code            VARCHAR2(42),
  bso_name            VARCHAR2(60),
  ae_id               VARCHAR2(38),
  ae_code             VARCHAR2(42),
  ae_name             VARCHAR2(60),
  ccid                NUMBER not null,
  budget_vou_id       VARCHAR2(38),
  budget_vou_code     VARCHAR2(42),
  budget_vou_name     VARCHAR2(60),
  md5                 VARCHAR2(32),
  gp_vou_id           VARCHAR2(38),
  gp_vou_code         VARCHAR2(42),
  gp_vou_name         VARCHAR2(60),
  RG_CODE             VARCHAR2(42) not null
)'
;
end if;


execute immediate 'comment on column GL_CCIDS.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column GL_CCIDS.coa_id
  is ''��������ʹ���''';
execute immediate 'comment on column GL_CCIDS.as_id
  is ''��ƿ�Ŀ''';
execute immediate 'comment on column GL_CCIDS.AGENCY_ID
  is ''Ԥ�㵥λ''';
execute immediate 'comment on column GL_CCIDS.fundtype_id
  is ''�ʽ����ʴ���''';
execute immediate 'comment on column GL_CCIDS.expfunc_id
  is ''Ԥ��֧����Ŀ''';
execute immediate 'comment on column GL_CCIDS.agencyexp_id
  is ''Ԥ��֧����Ŀ''';
execute immediate 'comment on column GL_CCIDS.expeco_id
  is ''֧��Ŀ����Ŀ''';
execute immediate 'comment on column GL_CCIDS.paytype_id
  is ''֧����ʽ����''';
execute immediate 'comment on column GL_CCIDS.BGTTYPE_ID
  is ''Ԥ�㵥λ������Դ����''';
execute immediate 'comment on column GL_CCIDS.paykind_id
  is ''ָ������''';
execute immediate 'comment on column GL_CCIDS.MB_ID
  is ''ҵ����''';
execute immediate 'comment on column GL_CCIDS.file_id
  is ''�ĺ�''';
execute immediate 'comment on column GL_CCIDS.setmode_id
  is ''���㷽Ԥ��''';
execute immediate 'comment on column GL_CCIDS.in_bs_id
  is ''�����Ŀʽ''';
execute immediate 'comment on column GL_CCIDS.in_bis_id
  is ''Ԥ��������Ŀ''';
execute immediate 'comment on column GL_CCIDS.hold21_id
  is ''�ɹ���ͬ''';
execute immediate 'comment on column GL_CCIDS.set_year
  is ''ҵ�����''';
execute immediate 'comment on column GL_CCIDS.bis_id
  is ''Ԥ����Ŀ''';
execute immediate 'comment on column GL_CCIDS.inpm_id
  is ''��֧����''';
execute immediate 'comment on column GL_CCIDS.st_id
  is ''����''';
execute immediate 'comment on column GL_CCIDS.ct_id
  is ''ҵ������''';
execute immediate 'comment on column GL_CCIDS.sm_id
  is ''��;''';
execute immediate 'comment on column GL_CCIDS.opuser_id
  is ''�����û�''';
execute immediate 'comment on column GL_CCIDS.editor_id
  is ''���ƻ���''';
execute immediate 'comment on column GL_CCIDS.fm_id
  is ''���''';
execute immediate 'comment on column GL_CCIDS.pf_id
  is ''֧����������''';
execute immediate 'comment on column GL_CCIDS.groupid_id
  is ''�����''';
execute immediate 'comment on column GL_CCIDS.useen_id
  is ''ʹ�õ�λ''';
execute immediate 'comment on column GL_CCIDS.prov_id
  is ''��Ӧ��''';
execute immediate 'comment on column GL_CCIDS.GP_agency_id
  is ''�ɹ��н�''';
execute immediate 'comment on column GL_CCIDS.mode_id
  is ''�ɹ�ģʽ''';
execute immediate 'comment on column GL_CCIDS.fasn_id
  is ''�ɹ���ʽ''';
execute immediate 'comment on column GL_CCIDS.dir_id
  is ''�ɹ�Ŀ¼''';
execute immediate 'comment on column GL_CCIDS.gpplan_id
  is ''�ɹ��ƻ�''';
execute immediate 'comment on column GL_CCIDS.gpbarn_id
  is ''�ɹ���ͬ''';
execute immediate 'comment on column GL_CCIDS.wa_id
  is ''ָ�����''';
execute immediate 'comment on column GL_CCIDS.bc_id
  is ''ָ�����''';
execute immediate 'comment on column GL_CCIDS.bso_id
  is ''Ԥ���ĿID''';
execute immediate 'comment on column GL_CCIDS.bso_code
  is ''Ԥ���Ŀ����''';
execute immediate 'comment on column GL_CCIDS.bso_name
  is ''Ԥ���Ŀ����''';
execute immediate 'create index GL_CCIDS_EN on GL_CCIDS (AGENCY_ID)
 ' ;
execute immediate 'create index IDX_GL_CCIDS_BI on GL_CCIDS (AGENCYEXP_ID)
  ';
execute immediate 'create index IDX_GL_CCIDS_BL on GL_CCIDS (BGTSOURCE_ID)
 ' ;
execute immediate 'create index IDX_GL_CCIDS_BP on GL_CCIDS (BP_ID)
 ' ;
execute immediate 'create index IDX_GL_CCIDS_BS on GL_CCIDS (EXPFUNC_ID)
 ' ;
execute immediate 'create index IDX_GL_CCIDS_BSI on GL_CCIDS (EXPECO_ID)
 ' ;
execute immediate 'create index IDX_GL_CCIDS_COA on GL_CCIDS (COA_ID)
 ' ;
execute immediate 'create index IDX_GL_CCIDS_FININTORG on GL_CCIDS (MB_ID)
 ' ;
execute immediate 'create index IDX_GL_CCIDS_MK on GL_CCIDS (FUNDTYPE_ID)
 ' ;
execute immediate 'create index IDX_GL_CCIDS_PAYTYPE on GL_CCIDS (PAYTYPE_ID)
 ' ;
execute immediate 'create unique index IDX_GL_CCID_MD5 on GL_CCIDS (MD5)
 ' ;
execute immediate 'create index IDZ_GL_CCIDS_GPPLAN_ID on GL_CCIDS (GPPLAN_ID)
 ' ;
execute immediate 'alter table GL_CCIDS
  add constraint GL_CCIDS_PRO_PAYTYPE primary key (CCID)';
  
  
  
select count(*) into i from user_tables where table_name='GL_CCIDS_CACHE
';
 if i=0 then
 execute immediate '
create global temporary table GL_CCIDS_CACHE
(
  latest_op_date      VARCHAR2(30) not null,
  latest_op_user      VARCHAR2(42),
  coa_id              VARCHAR2(38),
  as_id               VARCHAR2(38),
  AGENCY_ID               VARCHAR2(38),
  fundtype_id         VARCHAR2(38),
  expfunc_id          VARCHAR2(38),
  agencyexp_id        VARCHAR2(38),
  expeco_id           VARCHAR2(38),
  paytype_id          VARCHAR2(38),
  BGTTYPE_ID               VARCHAR2(38),
  paykind_id          VARCHAR2(38),
  MB_ID        VARCHAR2(38),
  file_id             VARCHAR2(38),
  setmode_id          VARCHAR2(38),
  in_bs_id            VARCHAR2(38),
  in_bis_id           VARCHAR2(38),
  cb_id               VARCHAR2(38),
  pb_id               VARCHAR2(38),
  ib_id               VARCHAR2(38),
  bac_id              VARCHAR2(38),
  bap_id              VARCHAR2(38),
  bai_id              VARCHAR2(38),
  bgtdir_id           VARCHAR2(38),
  bp_id               VARCHAR2(38),
  BGTSOURCE_ID        VARCHAR2(38),
  hold1_id            VARCHAR2(38),
  hold2_id            VARCHAR2(38),
  hold3_id            VARCHAR2(38),
  hold4_id            VARCHAR2(38),
  hold5_id            VARCHAR2(38),
  hold6_id            VARCHAR2(38),
  hold7_id            VARCHAR2(38),
  hold8_id            VARCHAR2(38),
  hold9_id            VARCHAR2(38),
  hold10_id           VARCHAR2(38),
  hold11_id           VARCHAR2(38),
  hold12_id           VARCHAR2(38),
  hold13_id           VARCHAR2(38),
  hold14_id           VARCHAR2(38),
  hold15_id           VARCHAR2(38),
  hold16_id           VARCHAR2(38),
  hold17_id           VARCHAR2(38),
  hold18_id           VARCHAR2(38),
  hold19_id           VARCHAR2(38),
  hold20_id           VARCHAR2(38),
  hold21_id           VARCHAR2(38),
  hold22_id           VARCHAR2(38),
  hold23_id           VARCHAR2(38),
  hold24_id           VARCHAR2(38),
  hold25_id           VARCHAR2(38),
  hold26_id           VARCHAR2(38),
  hold27_id           VARCHAR2(38),
  hold28_id           VARCHAR2(38),
  hold29_id           VARCHAR2(38),
  hold30_id           VARCHAR2(38),
  set_year            NUMBER(4) not null,
  bis_id              VARCHAR2(38),
  inpm_id             VARCHAR2(38),
  st_id               VARCHAR2(38),
  ct_id               VARCHAR2(38),
  sm_id               VARCHAR2(38),
  opuser_id           VARCHAR2(38),
  editor_id           VARCHAR2(38),
  ZFCGBS_ID               VARCHAR2(38),
  fm_id               VARCHAR2(38),
  as_code             VARCHAR2(42),
  as_name             VARCHAR2(60),
  AGENCY_CODE             VARCHAR2(42),
  AGENCY_NAME             VARCHAR2(100),
  fundtype_code       VARCHAR2(42),
  fundtype_name       VARCHAR2(60),
  expfunc_code        VARCHAR2(42),
  expfunc_name        VARCHAR2(60),
  agencyexp_code      VARCHAR2(42),
  agencyexp_name      VARCHAR2(60),
  expeco_code         VARCHAR2(42),
  expeco_name         VARCHAR2(60),
  paytype_code        VARCHAR2(42),
  paytype_name        VARCHAR2(60),
  BGTTYPE_CODE             VARCHAR2(42),
  BGTTYPE_NAME             VARCHAR2(60),
  paykind_code             VARCHAR2(42),
  paykind_name        VARCHAR2(60),
  MB_CODE      VARCHAR2(42),
  MB_NAME      VARCHAR2(60),
  file_code           VARCHAR2(42),
  file_name           VARCHAR2(200),
  setmode_code        VARCHAR2(42),
  setmode_name        VARCHAR2(60),
  in_bs_code          VARCHAR2(42),
  in_bs_name          VARCHAR2(60),
  in_bis_code         VARCHAR2(42),
  in_bis_name         VARCHAR2(200),
  cb_code             VARCHAR2(42),
  cb_name             VARCHAR2(60),
  pb_code             VARCHAR2(42),
  pb_name             VARCHAR2(60),
  ib_code             VARCHAR2(42),
  ib_name             VARCHAR2(60),
  bac_code            VARCHAR2(42),
  bac_name            VARCHAR2(60),
  bap_code            VARCHAR2(42),
  bap_name            VARCHAR2(60),
  bai_code            VARCHAR2(42),
  bai_name            VARCHAR2(60),
  bgtdir_code         VARCHAR2(42),
  bgtdir_name         VARCHAR2(60),
  bp_code             VARCHAR2(42),
  bp_name             VARCHAR2(60),
  BGTSOURCE_CODE      VARCHAR2(42),
  BGTSOURCE_NAME      VARCHAR2(60),
  hold1_code          VARCHAR2(42),
  hold1_name          VARCHAR2(60),
  hold2_code          VARCHAR2(42),
  hold2_name          VARCHAR2(60),
  hold3_code          VARCHAR2(42),
  hold3_name          VARCHAR2(60),
  hold4_code          VARCHAR2(42),
  hold4_name          VARCHAR2(60),
  hold5_code          VARCHAR2(42),
  hold5_name          VARCHAR2(60),
  hold6_code          VARCHAR2(42),
  hold6_name          VARCHAR2(60),
  hold7_code          VARCHAR2(42),
  hold7_name          VARCHAR2(60),
  hold8_code          VARCHAR2(42),
  hold8_name          VARCHAR2(60),
  hold9_code          VARCHAR2(42),
  hold9_name          VARCHAR2(60),
  hold10_code         VARCHAR2(42),
  hold10_name         VARCHAR2(60),
  hold11_code         VARCHAR2(42),
  hold11_name         VARCHAR2(60),
  hold12_code         VARCHAR2(42),
  hold12_name         VARCHAR2(60),
  hold13_code         VARCHAR2(42),
  hold13_name         VARCHAR2(60),
  hold14_code         VARCHAR2(42),
  hold14_name         VARCHAR2(60),
  hold15_code         VARCHAR2(42),
  hold15_name         VARCHAR2(60),
  hold16_code         VARCHAR2(42),
  hold16_name         VARCHAR2(60),
  hold17_code         VARCHAR2(42),
  hold17_name         VARCHAR2(60),
  hold18_code         VARCHAR2(42),
  hold18_name         VARCHAR2(60),
  hold19_code         VARCHAR2(42),
  hold19_name         VARCHAR2(60),
  hold20_code         VARCHAR2(42),
  hold20_name         VARCHAR2(60),
  hold21_code         VARCHAR2(42),
  hold21_name         VARCHAR2(60),
  hold22_code         VARCHAR2(42),
  hold22_name         VARCHAR2(60),
  hold23_code         VARCHAR2(42),
  hold23_name         VARCHAR2(60),
  hold24_code         VARCHAR2(42),
  hold24_name         VARCHAR2(60),
  hold25_code         VARCHAR2(42),
  hold25_name         VARCHAR2(60),
  hold26_code         VARCHAR2(42),
  hold26_name         VARCHAR2(60),
  hold27_code         VARCHAR2(42),
  hold27_name         VARCHAR2(60),
  hold28_code         VARCHAR2(42),
  hold28_name         VARCHAR2(60),
  hold29_code         VARCHAR2(42),
  hold29_name         VARCHAR2(60),
  hold30_code         VARCHAR2(42),
  hold30_name         VARCHAR2(60),
  bis_code            VARCHAR2(42),
  bis_name            VARCHAR2(2000),
  inpm_code           VARCHAR2(42),
  inpm_name           VARCHAR2(60),
  st_code             VARCHAR2(42),
  st_name             VARCHAR2(60),
  ct_code             VARCHAR2(42),
  ct_name             VARCHAR2(60),
  sm_code             VARCHAR2(42),
  sm_name             VARCHAR2(300),
  opuser_code         VARCHAR2(42),
  opuser_name         VARCHAR2(60),
  editor_code         VARCHAR2(42),
  editor_name         VARCHAR2(60),
  ZFCGBS_CODE             VARCHAR2(42),
  ZFCGBS_NAME             VARCHAR2(60),
  fm_code             VARCHAR2(42),
  fm_name             VARCHAR2(60),
  pay_account_no      VARCHAR2(60),
  pay_account_name    VARCHAR2(60),
  pay_account_bank    VARCHAR2(60),
  clear_account_no    VARCHAR2(60),
  clear_account_name  VARCHAR2(60),
  clear_account_bank  VARCHAR2(60),
  income_account_no   VARCHAR2(60),
  income_account_name VARCHAR2(60),
  income_account_bank VARCHAR2(60),
  pf_id               VARCHAR2(38),
  pf_code             VARCHAR2(42),
  pf_name             VARCHAR2(60),
  groupid_id          VARCHAR2(38),
  groupid_code        VARCHAR2(42),
  groupid_name        VARCHAR2(60),
  useen_id            VARCHAR2(38),
  useen_code          VARCHAR2(42),
  useen_name          VARCHAR2(60),
  prov_id             VARCHAR2(38),
  prov_code           VARCHAR2(42),
  prov_name           VARCHAR2(60),
  GP_agency_id           VARCHAR2(38),
  GP_agency_code         VARCHAR2(42),
  GP_agency_name         VARCHAR2(60),
  mode_id             VARCHAR2(38),
  mode_code           VARCHAR2(42),
  mode_name           VARCHAR2(60),
  fasn_id             VARCHAR2(38),
  fasn_code           VARCHAR2(42),
  fasn_name           VARCHAR2(60),
  dir_id              VARCHAR2(38),
  dir_code            VARCHAR2(42),
  dir_name            VARCHAR2(60),
  gpplan_id           VARCHAR2(38),
  gpplan_code         VARCHAR2(42),
  gpplan_name         VARCHAR2(60),
  gpbarn_id           VARCHAR2(38),
  gpbarn_code         VARCHAR2(42),
  gpbarn_name         VARCHAR2(60),
  wa_id               VARCHAR2(38),
  wa_code             VARCHAR2(42),
  wa_name             VARCHAR2(60),
  ff_id               VARCHAR2(38),
  ff_name             VARCHAR2(60),
  ff_code             VARCHAR2(42),
  ien_id              VARCHAR2(38),
  ien_name            VARCHAR2(60),
  ien_code            VARCHAR2(42),
  GZBS_ID           VARCHAR2(38),
  GZBS_NAME         VARCHAR2(60),
  GZBS_CODE         VARCHAR2(42),
  bc_id               VARCHAR2(38),
  bc_code             VARCHAR2(42),
  bc_name             VARCHAR2(60),
  bso_id              VARCHAR2(38),
  bso_code            VARCHAR2(42),
  bso_name            VARCHAR2(60),
  ae_id               VARCHAR2(38),
  ae_code             VARCHAR2(42),
  ae_name             VARCHAR2(60),
  ccid                NUMBER,
  budget_vou_id       VARCHAR2(38),
  budget_vou_code     VARCHAR2(42),
  budget_vou_name     VARCHAR2(60),
  md5                 VARCHAR2(32),
  gp_vou_id           VARCHAR2(38),
  gp_vou_code         VARCHAR2(42),
  gp_vou_name         VARCHAR2(60),
  RG_CODE             VARCHAR2(42)
)';
end if;

execute immediate 'create index IDX_GL_CCIDS_CACHE_CCID on GL_CCIDS_CACHE (CCID)';



select count(*) into i from user_tables where table_name='GL_CCIDS_OLD_BAK
';
 if i=0 then
 execute immediate '
create table GL_CCIDS_OLD_BAK
(
  latest_op_date      VARCHAR2(30) not null,
  latest_op_user      VARCHAR2(42),
  coa_id              VARCHAR2(38),
  as_id               VARCHAR2(38),
  AGENCY_ID               VARCHAR2(38),
  fundtype_id         VARCHAR2(38),
  expfunc_id          VARCHAR2(38),
  agencyexp_id        VARCHAR2(38),
  expeco_id           VARCHAR2(38),
  paytype_id          VARCHAR2(38),
  BGTTYPE_ID               VARCHAR2(38),
  paykind_id          VARCHAR2(38),
  MB_ID        VARCHAR2(38),
  file_id             VARCHAR2(38),
  setmode_id          VARCHAR2(38),
  in_bs_id            VARCHAR2(38),
  in_bis_id           VARCHAR2(38),
  cb_id               VARCHAR2(38),
  pb_id               VARCHAR2(38),
  ib_id               VARCHAR2(38),
  bac_id              VARCHAR2(38),
  bap_id              VARCHAR2(38),
  bai_id              VARCHAR2(38),
  bgtdir_id           VARCHAR2(38),
  bp_id               VARCHAR2(38),
  BGTSOURCE_ID        VARCHAR2(38),
  hold1_id            VARCHAR2(38),
  hold2_id            VARCHAR2(38),
  hold3_id            VARCHAR2(38),
  hold4_id            VARCHAR2(38),
  hold5_id            VARCHAR2(38),
  hold6_id            VARCHAR2(38),
  hold7_id            VARCHAR2(38),
  hold8_id            VARCHAR2(38),
  hold9_id            VARCHAR2(38),
  hold10_id           VARCHAR2(38),
  hold11_id           VARCHAR2(38),
  hold12_id           VARCHAR2(38),
  hold13_id           VARCHAR2(38),
  hold14_id           VARCHAR2(38),
  hold15_id           VARCHAR2(38),
  hold16_id           VARCHAR2(38),
  hold17_id           VARCHAR2(38),
  hold18_id           VARCHAR2(38),
  hold19_id           VARCHAR2(38),
  hold20_id           VARCHAR2(38),
  hold21_id           VARCHAR2(38),
  hold22_id           VARCHAR2(38),
  hold23_id           VARCHAR2(38),
  hold24_id           VARCHAR2(38),
  hold25_id           VARCHAR2(38),
  hold26_id           VARCHAR2(38),
  hold27_id           VARCHAR2(38),
  hold28_id           VARCHAR2(38),
  hold29_id           VARCHAR2(38),
  hold30_id           VARCHAR2(38),
  set_year            NUMBER(4) not null,
  bis_id              VARCHAR2(38),
  inpm_id             VARCHAR2(38),
  st_id               VARCHAR2(38),
  ct_id               VARCHAR2(38),
  sm_id               VARCHAR2(38),
  opuser_id           VARCHAR2(38),
  editor_id           VARCHAR2(38),
  ZFCGBS_ID               VARCHAR2(38),
  fm_id               VARCHAR2(38),
  as_code             VARCHAR2(42),
  as_name             VARCHAR2(60),
  AGENCY_CODE             VARCHAR2(42),
  AGENCY_NAME             VARCHAR2(60),
  fundtype_code       VARCHAR2(42),
  fundtype_name       VARCHAR2(60),
  expfunc_code        VARCHAR2(42),
  expfunc_name        VARCHAR2(60),
  agencyexp_code      VARCHAR2(42),
  agencyexp_name      VARCHAR2(60),
  expeco_code         VARCHAR2(42),
  expeco_name         VARCHAR2(60),
  paytype_code        VARCHAR2(42),
  paytype_name        VARCHAR2(60),
  BGTTYPE_CODE             VARCHAR2(42),
  BGTTYPE_NAME             VARCHAR2(60),
  paykind_code        VARCHAR2(42),
  paykind_name        VARCHAR2(60),
  MB_CODE      VARCHAR2(42),
  MB_NAME      VARCHAR2(60),
  file_code           VARCHAR2(42),
  file_name           VARCHAR2(200),
  setmode_code        VARCHAR2(42),
  setmode_name        VARCHAR2(60),
  in_bs_code          VARCHAR2(42),
  in_bs_name          VARCHAR2(60),
  in_bis_code         VARCHAR2(42),
  in_bis_name         VARCHAR2(200),
  cb_code             VARCHAR2(42),
  cb_name             VARCHAR2(60),
  pb_code             VARCHAR2(42),
  pb_name             VARCHAR2(60),
  ib_code             VARCHAR2(42),
  ib_name             VARCHAR2(60),
  bac_code            VARCHAR2(42),
  bac_name            VARCHAR2(60),
  bap_code            VARCHAR2(42),
  bap_name            VARCHAR2(60),
  bai_code            VARCHAR2(42),
  bai_name            VARCHAR2(60),
  bgtdir_code         VARCHAR2(42),
  bgtdir_name         VARCHAR2(60),
  bp_code             VARCHAR2(42),
  bp_name             VARCHAR2(60),
  BGTSOURCE_CODE      VARCHAR2(42),
  BGTSOURCE_NAME      VARCHAR2(60),
  hold1_code          VARCHAR2(42),
  hold1_name          VARCHAR2(60),
  hold2_code          VARCHAR2(42),
  hold2_name          VARCHAR2(60),
  hold3_code          VARCHAR2(42),
  hold3_name          VARCHAR2(60),
  hold4_code          VARCHAR2(42),
  hold4_name          VARCHAR2(60),
  hold5_code          VARCHAR2(42),
  hold5_name          VARCHAR2(60),
  hold6_code          VARCHAR2(42),
  hold6_name          VARCHAR2(60),
  hold7_code          VARCHAR2(42),
  hold7_name          VARCHAR2(60),
  hold8_code          VARCHAR2(42),
  hold8_name          VARCHAR2(60),
  hold9_code          VARCHAR2(42),
  hold9_name          VARCHAR2(60),
  hold10_code         VARCHAR2(42),
  hold10_name         VARCHAR2(60),
  hold11_code         VARCHAR2(42),
  hold11_name         VARCHAR2(60),
  hold12_code         VARCHAR2(42),
  hold12_name         VARCHAR2(60),
  hold13_code         VARCHAR2(42),
  hold13_name         VARCHAR2(60),
  hold14_code         VARCHAR2(42),
  hold14_name         VARCHAR2(60),
  hold15_code         VARCHAR2(42),
  hold15_name         VARCHAR2(60),
  hold16_code         VARCHAR2(42),
  hold16_name         VARCHAR2(60),
  hold17_code         VARCHAR2(42),
  hold17_name         VARCHAR2(60),
  hold18_code         VARCHAR2(42),
  hold18_name         VARCHAR2(60),
  hold19_code         VARCHAR2(42),
  hold19_name         VARCHAR2(60),
  hold20_code         VARCHAR2(42),
  hold20_name         VARCHAR2(60),
  hold21_code         VARCHAR2(42),
  hold21_name         VARCHAR2(60),
  hold22_code         VARCHAR2(42),
  hold22_name         VARCHAR2(60),
  hold23_code         VARCHAR2(42),
  hold23_name         VARCHAR2(60),
  hold24_code         VARCHAR2(42),
  hold24_name         VARCHAR2(60),
  hold25_code         VARCHAR2(42),
  hold25_name         VARCHAR2(60),
  hold26_code         VARCHAR2(42),
  hold26_name         VARCHAR2(60),
  hold27_code         VARCHAR2(42),
  hold27_name         VARCHAR2(60),
  hold28_code         VARCHAR2(42),
  hold28_name         VARCHAR2(60),
  hold29_code         VARCHAR2(42),
  hold29_name         VARCHAR2(60),
  hold30_code         VARCHAR2(42),
  hold30_name         VARCHAR2(60),
  bis_code            VARCHAR2(42),
  bis_name            VARCHAR2(2000),
  inpm_code           VARCHAR2(42),
  inpm_name           VARCHAR2(60),
  st_code             VARCHAR2(42),
  st_name             VARCHAR2(60),
  ct_code             VARCHAR2(42),
  ct_name             VARCHAR2(60),
  sm_code             VARCHAR2(42),
  sm_name             VARCHAR2(300),
  opuser_code         VARCHAR2(42),
  opuser_name         VARCHAR2(60),
  editor_code         VARCHAR2(42),
  editor_name         VARCHAR2(60),
  ZFCGBS_CODE             VARCHAR2(42),
  ZFCGBS_NAME             VARCHAR2(60),
  fm_code             VARCHAR2(42),
  fm_name             VARCHAR2(60),
  pay_account_no      VARCHAR2(60),
  pay_account_name    VARCHAR2(60),
  pay_account_bank    VARCHAR2(60),
  clear_account_no    VARCHAR2(60),
  clear_account_name  VARCHAR2(60),
  clear_account_bank  VARCHAR2(60),
  income_account_no   VARCHAR2(60),
  income_account_name VARCHAR2(60),
  income_account_bank VARCHAR2(60),
  pf_id               VARCHAR2(38),
  pf_code             VARCHAR2(42),
  pf_name             VARCHAR2(60),
  groupid_id          VARCHAR2(38),
  groupid_code        VARCHAR2(42),
  groupid_name        VARCHAR2(60),
  useen_id            VARCHAR2(38),
  useen_code          VARCHAR2(42),
  useen_name          VARCHAR2(60),
  prov_id             VARCHAR2(38),
  prov_code           VARCHAR2(42),
  prov_name           VARCHAR2(60),
  GP_agency_id           VARCHAR2(38),
  GP_agency_code         VARCHAR2(42),
  GP_agency_name         VARCHAR2(60),
  mode_id             VARCHAR2(38),
  mode_code           VARCHAR2(42),
  mode_name           VARCHAR2(60),
  fasn_id             VARCHAR2(38),
  fasn_code           VARCHAR2(42),
  fasn_name           VARCHAR2(60),
  dir_id              VARCHAR2(38),
  dir_code            VARCHAR2(42),
  dir_name            VARCHAR2(60),
  gpplan_id           VARCHAR2(38),
  gpplan_code         VARCHAR2(42),
  gpplan_name         VARCHAR2(60),
  gpbarn_id           VARCHAR2(38),
  gpbarn_code         VARCHAR2(42),
  gpbarn_name         VARCHAR2(60),
  wa_id               VARCHAR2(38),
  wa_code             VARCHAR2(42),
  wa_name             VARCHAR2(60),
  ff_id               VARCHAR2(38),
  ff_name             VARCHAR2(60),
  ff_code             VARCHAR2(42),
  ien_id              VARCHAR2(38),
  ien_name            VARCHAR2(60),
  ien_code            VARCHAR2(42),
  GZBS_ID           VARCHAR2(38),
  GZBS_NAME         VARCHAR2(60),
  GZBS_CODE         VARCHAR2(42),
  bc_id               VARCHAR2(38),
  bc_code             VARCHAR2(42),
  bc_name             VARCHAR2(60),
  bso_id              VARCHAR2(38),
  bso_code            VARCHAR2(42),
  bso_name            VARCHAR2(60),
  ae_id               VARCHAR2(38),
  ae_code             VARCHAR2(42),
  ae_name             VARCHAR2(60),
  ccid                NUMBER not null,
  budget_vou_id       VARCHAR2(38),
  budget_vou_code     VARCHAR2(42),
  budget_vou_name     VARCHAR2(60),
  md5                 VARCHAR2(32),
  gp_vou_id           VARCHAR2(38),
  gp_vou_code         VARCHAR2(42),
  gp_vou_name         VARCHAR2(60)
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_CCID_CONFLICT
';
 if i=0 then
 execute immediate '
create table GL_CCID_CONFLICT
(
  ccid          NUMBER not null,
  md5           VARCHAR2(32) not null,
  conflict_type NUMBER(1) not null,
  fixed_value   VARCHAR2(32) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_CCID_TRANS
';
 if i=0 then
 execute immediate '
create table GL_CCID_TRANS
(
  s_ccid   VARCHAR2(38) not null,
  t_ccid   VARCHAR2(38) not null,
  last_ver VARCHAR2(30),
  set_year NUMBER(4) not null,
  t_coa_id NUMBER
)'
;
end if;


execute immediate 'comment on column GL_CCID_TRANS.set_year
  is ''ҵ�����''';
execute immediate 'alter table GL_CCID_TRANS
  add constraint GL_CCID_TRANS_PAYTYPE primary key (S_CCID, T_CCID)';
  
  
  
select count(*) into i from user_tables where table_name='GL_COA
';
 if i=0 then
 execute immediate '
create table GL_COA
(
  coa_id         VARCHAR2(38) not null,
  coa_code       VARCHAR2(42),
  coa_name       VARCHAR2(42),
  coa_desc       VARCHAR2(200),
  enabled        NUMBER(1) default 1 not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  set_year       NUMBER(4) not null,
  RG_CODE        VARCHAR2(42),
  ccids_table    VARCHAR2(42),
  sys_app_name   VARCHAR2(42)
)'
;
end if;

execute immediate 'comment on column GL_COA.coa_id
  is ''COAID''';
execute immediate 'comment on column GL_COA.coa_code
  is ''COA����''';
execute immediate 'comment on column GL_COA.coa_name
  is ''COA����''';
execute immediate 'comment on column GL_COA.coa_desc
  is ''����''';
execute immediate 'comment on column GL_COA.enabled
  is ''����''';

execute immediate 'comment on column GL_COA.create_user
  is ''�����û�''';

execute immediate 'comment on column GL_COA.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column GL_COA.set_year
  is ''ҵ�����''';
execute immediate 'comment on column GL_COA.sys_app_name
  is ''Ӧ�õ���ϵͳ����''';
execute immediate 'alter table GL_COA
  add constraint GL_COA_PAYTYPE primary key (COA_ID)';
  
  
  
select count(*) into i from user_tables where table_name='GL_COA_CASCADE
';
 if i=0 then
 execute immediate '
create table GL_COA_CASCADE
(
  coa_id          NUMBER,
  coa_name        VARCHAR2(60),
  relation_coa_id NUMBER,
  is_up_stream    NUMBER(1),
  is_branch       NUMBER(1),
  RG_CODE         VARCHAR2(42),
  set_year        NUMBER(6)
)'
;
end if;

execute immediate 'comment on column GL_COA_CASCADE.coa_id
  is ''����coa����''';
execute immediate 'comment on column GL_COA_CASCADE.coa_name
  is ''����coa����''';
execute immediate 'comment on column GL_COA_CASCADE.relation_coa_id
  is ''����coa����''';
execute immediate 'comment on column GL_COA_CASCADE.is_up_stream
  is ''�Ƿ�������coa''';
execute immediate 'comment on column GL_COA_CASCADE.is_branch
  is ''����coa�Ƿ��з�֧''';
  
  
  
select count(*) into i from user_tables where table_name='GL_COA_DETAIL
';
 if i=0 then
 execute immediate '
create table GL_COA_DETAIL
(
  coa_id        VARCHAR2(38) not null,
  coa_detail_id VARCHAR2(38) not null,
  ele_code      VARCHAR2(42) not null,
  disp_order    NUMBER(10) default 0,
  level_num     NUMBER(2) default -1,
  last_ver      VARCHAR2(30),
  set_year      NUMBER(4) not null,
  is_mustinput  NUMBER default 1,
  default_value VARCHAR2(38),
  RG_CODE       VARCHAR2(42)
)'
;
end if;

execute immediate 'comment on column GL_COA_DETAIL.coa_id
  is ''COA ID''';
execute immediate 'comment on column GL_COA_DETAIL.coa_detail_id
  is ''COA��ϸID''';
execute immediate 'comment on column GL_COA_DETAIL.ele_code
  is ''Ҫ�ر���''';
execute immediate 'comment on column GL_COA_DETAIL.disp_order
  is ''Ҫ��˳��''';
execute immediate 'comment on column GL_COA_DETAIL.level_num
  is ''����Ҫ��ʹ�õļ��Σ�''';

execute immediate 'comment on column GL_COA_DETAIL.set_year
  is ''ҵ�����''';
execute immediate 'comment on column GL_COA_DETAIL.is_mustinput
  is ''�Ƿ��¼�룺0��������¼�룻1����¼��''';
execute immediate 'create index GL_COA_DETAIL_ID on GL_COA_DETAIL (COA_ID)';
  
execute immediate 'alter table GL_COA_DETAIL
  add constraint GL_COA_DETAIL_PAYTYPE primary key (COA_DETAIL_ID)';
  
  
  
select count(*) into i from user_tables where table_name='GL_COA_DETAIL_CODE
';
 if i=0 then
 execute immediate '
create table GL_COA_DETAIL_CODE
(
  coa_code_id   VARCHAR2(38) not null,
  coa_detail_id VARCHAR2(38) not null,
  level_code    VARCHAR2(42),
  last_ver      VARCHAR2(30),
  set_year      NUMBER(4) not null
)'
;
end if;


execute immediate 'comment on column GL_COA_DETAIL_CODE.set_year
  is ''ҵ�����''';
execute immediate 'alter table GL_COA_DETAIL_CODE
  add constraint GL_COA_DETAIL_CODE_PAYTYPE primary key (COA_CODE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='GL_ERROR_DATA_001
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_001
(
  sum_id             VARCHAR2(38),
  ccid               NUMBER,
  g_avi_money        NUMBER,
  j_avi_money        NUMBER,
  margin_avi_money   NUMBER,
  g_aving_money      NUMBER,
  j_aving_money      NUMBER,
  margin_aving_money NUMBER,
  g_mcinus_money     NUMBER,
  j_minus_money      NUMBER,
  margin_minus_money NUMBER
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_002
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_002
(
  sum_id           VARCHAR2(38),
  g_use_money      NUMBER(16,2),
  j_use_money      NUMBER,
  margin_use_money NUMBER
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_003
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_003
(
  sum_id             VARCHAR2(38),
  ccid               NUMBER,
  g_avi_money        NUMBER,
  j_avi_money        NUMBER,
  margin_avi_money   NUMBER,
  g_aving_money      NUMBER,
  j_aving_money      NUMBER,
  margin_aving_money NUMBER,
  g_minus_money      NUMBER,
  j_minus_money      NUMBER,
  margin_minus_money NUMBER
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_004
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_004
(
  sum_id           VARCHAR2(38),
  g_use_money      NUMBER(16,2),
  j_use_money      NUMBER,
  margin_use_money NUMBER
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_005
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_005
(
  sum_id           VARCHAR2(38),
  g_use_money      NUMBER(16,2),
  j_use_money      NUMBER,
  margin_use_money NUMBER
)'
;
end if;


select count(*) into i from user_tables where table_name='GL_ERROR_DATA_006
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_006
(
  sum_id           VARCHAR2(38),
  g_use_money      NUMBER(16,2),
  p_use_money      NUMBER,
  margin_use_money NUMBER
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_007
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_007
(
  sum_id             VARCHAR2(38),
  g_avi_money        NUMBER(16,2),
  p_avi_money        NUMBER,
  margin_avi_money   NUMBER,
  g_aving_money      NUMBER(16,2),
  p_aving_money      NUMBER,
  margin_aving_money NUMBER,
  g_minus_money      NUMBER(16,2),
  p_minus_money      NUMBER,
  margin_minus_money NUMBER
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_008
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_008
(
  sum_id             VARCHAR2(38),
  g_avi_money        NUMBER(16,2),
  b_avi_money        NUMBER,
  margin_avi_money   NUMBER,
  g_aving_money      NUMBER(16,2),
  b_aving_money      NUMBER,
  margin_aving_money NUMBER,
  g_minus_money      NUMBER(16,2),
  b_minus_money      NUMBER,
  margin_minus_money NUMBER,
  latest_op_date     VARCHAR2(30) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_009
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_009
(
  ele_source   VARCHAR2(64),
  ele_code     VARCHAR2(64),
  ele_name     VARCHAR2(64),
  chr_code     VARCHAR2(64),
  chr_name     VARCHAR2(64),
  repeat_feild VARCHAR2(64)
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_010
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_010
(
  ele_source VARCHAR2(64),
  ele_code   VARCHAR2(64),
  ele_name   VARCHAR2(64),
  chr_code   VARCHAR2(64),
  chr_code1  VARCHAR2(64),
  chr_code2  VARCHAR2(64),
  chr_code3  VARCHAR2(64),
  chr_code4  VARCHAR2(64),
  chr_code5  VARCHAR2(64),
  chr_id     VARCHAR2(64),
  chr_id1    VARCHAR2(64),
  chr_id2    VARCHAR2(64),
  chr_id3    VARCHAR2(64),
  chr_id4    VARCHAR2(64),
  chr_id5    VARCHAR2(64),
  parent_id  VARCHAR2(64),
  chr_name   VARCHAR2(200),
  level_num  NUMBER(2),
  error_type VARCHAR2(64)
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_DATA_011
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_011
(
  ele_rule_code VARCHAR2(42),
  ele_rule_name VARCHAR2(64),
  ele_code      VARCHAR2(64),
  ele_name      VARCHAR2(64),
  ele_source    VARCHAR2(64),
  ele_value     VARCHAR2(64)
)'
;
end if;


select count(*) into i from user_tables where table_name='GL_ERROR_DATA_012
';
 if i=0 then
 execute immediate '
create table GL_ERROR_DATA_012
(
  relation_code VARCHAR2(42),
  ele_code      VARCHAR2(64),
  ele_name      VARCHAR2(64),
  ele_source    VARCHAR2(64),
  pri_ele_value VARCHAR2(64),
  sec_ele_value VARCHAR2(64),
  invalid_feild VARCHAR2(64)
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_ERROR_TYPE
';
 if i=0 then
 execute immediate '
create table GL_ERROR_TYPE
(
  error_type            VARCHAR2(30) not null,
  detail_sql            VARCHAR2(4000),
  query_sql             VARCHAR2(4000),
  query_related_column  VARCHAR2(500),
  class_name            VARCHAR2(200),
  remark                VARCHAR2(256),
  advice                VARCHAR2(1000),
  main_fields           VARCHAR2(3000),
  detail_fields         VARCHAR2(3000),
  detail_related_column VARCHAR2(500),
  procedurestr          VARCHAR2(50)
)'
;
end if;

execute immediate 'alter table GL_ERROR_TYPE
  add constraint PAYTYPE_GL_ERROR_TYPE primary key (ERROR_TYPE)';
  
  
  
select count(*) into i from user_tables where table_name='GL_JOURNAL
';
 if i=0 then
 execute immediate '
create table GL_JOURNAL
(
  journal_id     NUMBER not null,
  set_year       NUMBER(4),
  RG_CODE        VARCHAR2(42),
  vou_id         VARCHAR2(38) not null,
  vou_type_id    NUMBER,
  vou_money      NUMBER(16,2),
  set_month      NUMBER(2),
  ccid           NUMBER,
  rcid           NUMBER,
  is_end         NUMBER(1),
  is_valid       NUMBER(1),
  action_id      NUMBER(1),
  bill_date      VARCHAR2(30),
  remark         VARCHAR2(200),
  bal_version    NUMBER(1),
  create_date    VARCHAR2(30),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30),
  billtype_code  VARCHAR2(42)
)'
;
end if;

execute immediate 'comment on column GL_JOURNAL.set_month
  is ''ҵ���·�''';
execute immediate 'create index IDX_GL_JOURNAL_BILLTYPE_TYPE on GL_JOURNAL (BILLTYPE_CODE)
  ';
execute immediate 'create unique index IDX_GL_JOURNAL_VOU on GL_JOURNAL (VOU_ID, VOU_TYPE_ID, SET_YEAR, RG_CODE)
 ' ;
execute immediate 'create index IDX_GL_JOURNAL_VOUID on GL_JOURNAL (VOU_ID)
 ' ;
execute immediate 'create index IDX_GL_JOURNAL_VOU_TYPE on GL_JOURNAL (VOU_TYPE_ID)
 ' ;
execute immediate 'alter table GL_JOURNAL
  add constraint PK_GL_JOURNAL primary key (JOURNAL_ID, VOU_ID)';
  
  
  
select count(*) into i from user_tables where table_name='GL_JOURNAL_CACHE
';
 if i=0 then
 execute immediate '
create global temporary table GL_JOURNAL_CACHE
(
  journal_id     NUMBER not null,
  set_year       NUMBER(4),
  RG_CODE        VARCHAR2(42),
  vou_id         VARCHAR2(38),
  vou_type_id    NUMBER,
  billtype_code  VARCHAR2(42),
  vou_money      NUMBER(16,2),
  set_month      NUMBER(2),
  ccid           NUMBER,
  rcid           NUMBER,
  is_end         NUMBER(1),
  is_valid       NUMBER(1),
  action_id      NUMBER(1),
  bill_date      VARCHAR2(30),
  remark         VARCHAR2(200),
  bal_version    NUMBER(1),
  create_date    VARCHAR2(30),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30),
  index_         NUMBER,
  ctrl_level     NUMBER(1)
)';
end if;

execute immediate 'create index IDX_GL_JOURNAL_CACHE_VOU on GL_JOURNAL_CACHE (VOU_ID, VOU_TYPE_ID, SET_YEAR, RG_CODE)';



select count(*) into i from user_tables where table_name='GL_JOURNAL_HIS
';
 if i=0 then
 execute immediate '
create table GL_JOURNAL_HIS
(
  journal_id     NUMBER not null,
  action_id      NUMBER(1),
  set_year       NUMBER(4),
  RG_CODE        VARCHAR2(42),
  vou_id         VARCHAR2(38),
  vou_type_id    NUMBER,
  billtype_code  VARCHAR2(42),
  vou_money      NUMBER(16,2),
  set_month      NUMBER(2),
  ccid           NUMBER,
  rcid           NUMBER,
  is_end         NUMBER(1),
  is_valid       NUMBER(1),
  bill_date      VARCHAR2(30),
  remark         VARCHAR2(200),
  bal_version    NUMBER(1),
  create_date    VARCHAR2(30),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30)
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_MANUAL_CCIDS
';
 if i=0 then
 execute immediate '
create table GL_MANUAL_CCIDS
(
  create_date VARCHAR2(30),
  ccid        NUMBER
)'
;
end if;



select count(*) into i from user_tables where table_name='GL_MONTHLY_BALANCE
';
 if i=0 then
 execute immediate '
create table GL_MONTHLY_BALANCE
(
  set_year     NUMBER(4) not null,
  set_month    NUMBER(4) not null,
  closing_time VARCHAR2(40)
)'
;
end if;

execute immediate 'alter table GL_MONTHLY_BALANCE
  add constraint GL_MONTH_BALANCE_PAYTYPE primary key (SET_YEAR, SET_MONTH)';
  
  
  
select count(*) into i from user_tables where table_name='GL_PATCH_VOUCHER
';
 if i=0 then
 execute immediate '
create table GL_PATCH_VOUCHER
(
  set_year       NUMBER(4) not null,
  vou_id         VARCHAR2(38) not null,
  vou_no         VARCHAR2(42),
  vou_money      NUMBER(16,2),
  set_month      NUMBER(2),
  is_end         NUMBER(1),
  billtype_id    VARCHAR2(38),
  billtype_code  VARCHAR2(38),
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  latest_op_user VARCHAR2(42),
  last_ver       VARCHAR2(30),
  remark         VARCHAR2(200),
  ccid           NUMBER,
  rcid           NUMBER,
  RG_CODE        VARCHAR2(42),
  fromctrlid     VARCHAR2(38),
  toctrlid       VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column GL_PATCH_VOUCHER.set_year
  is ''ҵ�����''';
execute immediate 'comment on column GL_PATCH_VOUCHER.vou_id
  is ''ҵ������id''';
execute immediate 'comment on column GL_PATCH_VOUCHER.vou_no
  is ''ҵ������ƾ֤��''';
execute immediate 'comment on column GL_PATCH_VOUCHER.vou_money
  is ''ҵ����''';
execute immediate 'comment on column GL_PATCH_VOUCHER.set_month
  is ''ҵ���·�''';
execute immediate 'comment on column GL_PATCH_VOUCHER.is_end
  is ''�����־''';
execute immediate 'comment on column GL_PATCH_VOUCHER.billtype_id
  is ''����ƾ֤id''';
execute immediate 'comment on column GL_PATCH_VOUCHER.remark
  is ''��ע''';
execute immediate 'comment on column GL_PATCH_VOUCHER.RG_CODE
  is ''�������''';
execute immediate 'create index GL_PATCH_VOUCHER_INDEX1 on GL_PATCH_VOUCHER (BILLTYPE_ID)
 ' ;
execute immediate 'alter table GL_PATCH_VOUCHER
  add constraint GL_PATCH_VOUCHER_PAYTYPE primary key (VOU_ID)';
  
  
  
select count(*) into i from user_tables where table_name='MSG_COMPLETE_TASK
';
 if i=0 then
 execute immediate '
create table MSG_COMPLETE_TASK
(
  id                 NUMBER not null,
  msg_id             NUMBER not null,
  is_valid           CHAR(1) default ''1'',
  RG_CODE            VARCHAR2(42),
  set_year           NUMBER(4),
  status             CHAR(1),
  send_type          CHAR(4) not null,
  from_user_id       VARCHAR2(60),
  from_user_telecode VARCHAR2(100),
  to_user_id         VARCHAR2(60),
  to_user_telecode   VARCHAR2(100),
  msg_title          VARCHAR2(200),
  msg_content        VARCHAR2(2000),
  last_ver           DATE default sysdate,
  upid               NUMBER not null,
  batch_id           NUMBER not null
)'
;
end if;

execute immediate 'comment on table MSG_COMPLETE_TASK
  is ''��Ϣ��������б�''';
execute immediate 'comment on column MSG_COMPLETE_TASK.id
  is ''ID��ˮ��''';
execute immediate 'comment on column MSG_COMPLETE_TASK.msg_id
  is ''��Ϣʵ��ID''';
execute immediate 'comment on column MSG_COMPLETE_TASK.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column MSG_COMPLETE_TASK.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_COMPLETE_TASK.set_year
  is ''���''';
execute immediate 'comment on column MSG_COMPLETE_TASK.status
  is ''��Ϣ״̬��0�����ɴ�����1���ͳɹ�2����ʧ��''';
execute immediate 'comment on column MSG_COMPLETE_TASK.send_type
  is ''��Ϣ���ͷ�ʽ4λ��1��ʾ���ͣ�0��ʾ������ ������Ϊϵͳ�ڷ��͡����ŷ��͡��ֻ�app���͡�΢�ŷ���''';
execute immediate 'comment on column MSG_COMPLETE_TASK.from_user_id
  is ''�����û�ID''';
execute immediate 'comment on column MSG_COMPLETE_TASK.from_user_telecode
  is ''�����û���ͨ�ŵ�ַ���ֻ��š�΢�źŵȱ�Ҫ�����ֶΣ�''';
execute immediate 'comment on column MSG_COMPLETE_TASK.to_user_id
  is ''�����û�ID''';
execute immediate 'comment on column MSG_COMPLETE_TASK.to_user_telecode
  is ''�����û���ͨ�ŵ�ַ���ֻ��š�΢�źŵȱ�Ҫ�����ֶΣ�''';
execute immediate 'comment on column MSG_COMPLETE_TASK.msg_title
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_COMPLETE_TASK.msg_content
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_COMPLETE_TASK.last_ver
  is ''������ʱ��''';
execute immediate 'comment on column MSG_COMPLETE_TASK.upid
  is ''����ID������������£�''';
execute immediate 'comment on column MSG_COMPLETE_TASK.batch_id
  is ''���κ�''';
execute immediate 'create index INDEX_MSG_COMPLETE_TASK_MSGID on MSG_COMPLETE_TASK (MSG_ID)
 ' ;
execute immediate 'create index INDEX_MSG_COMPLETE_TASK_UPID on MSG_COMPLETE_TASK (UPID)
 ' ;
execute immediate 'alter table MSG_COMPLETE_TASK
  add constraint PK_MSG_COMPLETE_TASK primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='MSG_CURRENT_TASK
';
 if i=0 then
 execute immediate '
create table MSG_CURRENT_TASK
(
  id                 NUMBER not null,
  msg_id             NUMBER not null,
  is_valid           CHAR(1) default ''1'',
  RG_CODE            VARCHAR2(42),
  set_year           NUMBER(4),
  status             CHAR(1),
  send_type          CHAR(4) not null,
  from_user_id       VARCHAR2(60),
  from_user_telecode VARCHAR2(100),
  to_user_id         VARCHAR2(60),
  to_user_telecode   VARCHAR2(100),
  msg_title          VARCHAR2(200),
  msg_content        VARCHAR2(2000),
  last_ver           DATE default sysdate,
  upid               NUMBER not null,
  batch_id           NUMBER not null
)'
;
end if;

execute immediate 'comment on table MSG_CURRENT_TASK
  is ''��Ϣ��ǰ�����б�''';
execute immediate 'comment on column MSG_CURRENT_TASK.id
  is ''ID��ˮ��''';
execute immediate 'comment on column MSG_CURRENT_TASK.msg_id
  is ''��Ϣʵ��ID''';
execute immediate 'comment on column MSG_CURRENT_TASK.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column MSG_CURRENT_TASK.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_CURRENT_TASK.set_year
  is ''���''';
execute immediate 'comment on column MSG_CURRENT_TASK.status
  is ''��Ϣ״̬��0�����ɴ�����1���ͳɹ�2����ʧ��''';
execute immediate 'comment on column MSG_CURRENT_TASK.send_type
  is ''��Ϣ���ͷ�ʽ4λ��1��ʾ���ͣ�0��ʾ������ ������Ϊϵͳ�ڷ��͡����ŷ��͡��ֻ�app���͡�΢�ŷ���''';
execute immediate 'comment on column MSG_CURRENT_TASK.from_user_id
  is ''�����û�ID''';
execute immediate 'comment on column MSG_CURRENT_TASK.from_user_telecode
  is ''�����û���ͨ�ŵ�ַ���ֻ��š�΢�źŵȱ�Ҫ�����ֶΣ�''';
execute immediate 'comment on column MSG_CURRENT_TASK.to_user_id
  is ''�����û�ID''';
execute immediate 'comment on column MSG_CURRENT_TASK.to_user_telecode
  is ''�����û���ͨ�ŵ�ַ���ֻ��š�΢�źŵȱ�Ҫ�����ֶΣ�''';
execute immediate 'comment on column MSG_CURRENT_TASK.msg_title
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_CURRENT_TASK.msg_content
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_CURRENT_TASK.last_ver
  is ''������ʱ��''';
execute immediate 'comment on column MSG_CURRENT_TASK.upid
  is ''����ID������������£�''';
execute immediate 'comment on column MSG_CURRENT_TASK.batch_id
  is ''���κ�''';
execute immediate 'create index INDEX_MSG_CURRENT_TASK_MSGID on MSG_CURRENT_TASK (MSG_ID)
 ' ;
execute immediate 'create index INDEX_MSG_CURRENT_TASK_UPID on MSG_CURRENT_TASK (UPID)
  ';
execute immediate 'alter table MSG_CURRENT_TASK
  add constraint PK_MSG_CURRENT_TASK primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='MSG_ENTITY
';
 if i=0 then
 execute immediate '
create table MSG_ENTITY
(
  id                 NUMBER not null,
  is_valid           CHAR(1) default ''1'',
  RG_CODE            VARCHAR2(42),
  set_year           NUMBER(4),
  send_type          CHAR(4) not null,
  from_user_id       VARCHAR2(60),
  from_user_telecode VARCHAR2(100),
  to_user_id         VARCHAR2(60),
  to_user_telecode   VARCHAR2(100),
  title              VARCHAR2(200),
  content            VARCHAR2(2000),
  last_ver           DATE default sysdate,
  upid               NUMBER not null,
  msg_rule_code      VARCHAR2(100),
  parent_id          NUMBER,
  status             CHAR(1),
  create_date        DATE,
  send_date          DATE,
  batch_id           NUMBER not null
)'
;
end if;

execute immediate 'comment on table MSG_ENTITY
  is ''��Ϣ����ʵ��''';
execute immediate 'comment on column MSG_ENTITY.id
  is ''ID��ˮ��''';
execute immediate 'comment on column MSG_ENTITY.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column MSG_ENTITY.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_ENTITY.set_year
  is ''���''';
execute immediate 'comment on column MSG_ENTITY.send_type
  is ''��Ϣ���ͷ�ʽ0ϵͳ�ڷ���1���ŷ���2�ֻ�app����3΢�ŷ���''';
execute immediate 'comment on column MSG_ENTITY.from_user_id
  is ''�����û�ID''';
execute immediate 'comment on column MSG_ENTITY.from_user_telecode
  is ''�����û���ͨ�ŵ�ַ���ֻ��š�΢�źŵȱ�Ҫ�����ֶΣ�''';
execute immediate 'comment on column MSG_ENTITY.to_user_id
  is ''�����û�ID''';
execute immediate 'comment on column MSG_ENTITY.to_user_telecode
  is ''�����û���ͨ�ŵ�ַ���ֻ��š�΢�źŵȱ�Ҫ�����ֶΣ�''';
execute immediate 'comment on column MSG_ENTITY.title
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_ENTITY.content
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_ENTITY.last_ver
  is ''������ʱ��''';
execute immediate 'comment on column MSG_ENTITY.upid
  is ''����ID������������£�''';
execute immediate 'comment on column MSG_ENTITY.msg_rule_code
  is ''��Ϣ������루ʹ����Ϣģ�淢��ʱ��ֵ��''';
execute immediate 'comment on column MSG_ENTITY.parent_id
  is ''����ϢID''';
execute immediate 'comment on column MSG_ENTITY.status
  is ''��Ϣ״̬��0�����ɴ�����1���ͳɹ�2����ʧ��''';
execute immediate 'comment on column MSG_ENTITY.create_date
  is ''��Ϣ����ʱ��''';
execute immediate 'comment on column MSG_ENTITY.send_date
  is ''��Ϣ����ʱ��''';
execute immediate 'comment on column MSG_ENTITY.batch_id
  is ''���κ�''';
execute immediate 'create index INDEX_MSG_ENTITY_UPID on MSG_ENTITY (UPID)
  ';
execute immediate 'alter table MSG_ENTITY
  add constraint PK_MSG_ENTITY primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='MSG_LOG
';
 if i=0 then
 execute immediate '
create table MSG_LOG
(
  id                 NUMBER not null,
  is_valid           CHAR(1) default ''1'',
  RG_CODE            VARCHAR2(42),
  set_year           NUMBER(4),
  create_date        VARCHAR2(30),
  send_date          VARCHAR2(30),
  status             CHAR(1),
  msg_id             NUMBER not null,
  from_user_id       VARCHAR2(60),
  from_user_telecode VARCHAR2(100),
  to_user_id         VARCHAR2(60),
  to_user_telecode   VARCHAR2(100),
  send_type          CHAR(4) not null,
  msg_title          VARCHAR2(200),
  msg_content        VARCHAR2(2000),
  last_ver           DATE default sysdate,
  upid               NUMBER not null,
  batch_id           NUMBER not null
)'
;
end if;

execute immediate 'comment on table MSG_LOG
  is ''��Ϣ��־''';
execute immediate 'comment on column MSG_LOG.id
  is ''ID��ˮ��''';
execute immediate 'comment on column MSG_LOG.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column MSG_LOG.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_LOG.set_year
  is ''���''';
execute immediate 'comment on column MSG_LOG.create_date
  is ''��Ϣ����ʱ��''';
execute immediate 'comment on column MSG_LOG.send_date
  is ''��Ϣ����ʱ��''';
execute immediate 'comment on column MSG_LOG.status
  is ''��Ϣ״̬��0�����ɴ�����1���ͳɹ�2����ʧ��''';
execute immediate 'comment on column MSG_LOG.msg_id
  is ''��Ϣʵ��ID''';
execute immediate 'comment on column MSG_LOG.from_user_id
  is ''�����û�ID''';
execute immediate 'comment on column MSG_LOG.from_user_telecode
  is ''�����û���ͨ�ŵ�ַ���ֻ��š�΢�źŵȱ�Ҫ�����ֶΣ�''';
execute immediate 'comment on column MSG_LOG.to_user_id
  is ''�����û�ID''';
execute immediate 'comment on column MSG_LOG.to_user_telecode
  is ''�����û���ͨ�ŵ�ַ���ֻ��š�΢�źŵȱ�Ҫ�����ֶΣ�''';
execute immediate 'comment on column MSG_LOG.send_type
  is ''��Ϣ���ͷ�ʽ0ϵͳ�ڷ���1���ŷ���2�ֻ�app����3΢�ŷ���''';
execute immediate 'comment on column MSG_LOG.msg_title
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_LOG.msg_content
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_LOG.last_ver
  is ''������ʱ��''';
execute immediate 'comment on column MSG_LOG.upid
  is ''����ID������������£�''';
execute immediate 'comment on column MSG_LOG.batch_id
  is ''���κ�''';
execute immediate 'create index INDEX_MSG_LOG_MSGID on MSG_LOG (MSG_ID)
 ' ;
execute immediate 'create index INDEX_MSG_LOG_UPID on MSG_LOG (UPID)
  ';
execute immediate 'alter table MSG_LOG
  add constraint PK_MSG_LOG primary key (ID)';
  
  
select count(*) into i from user_tables where table_name='MSG_RULES
';
 if i=0 then
 execute immediate '
create table MSG_RULES
(
  id             NUMBER not null,
  is_valid       CHAR(1) default ''1'',
  RG_CODE        VARCHAR2(42),
  set_year       NUMBER(4),
  msg_rule_code  VARCHAR2(100) not null,
  msg_rule_name  VARCHAR2(200) not null,
  invoketype     CHAR(1) not null,
  wf_flow_id     NUMBER,
  wf_flow_code   VARCHAR2(42),
  wf_flow_name   VARCHAR2(100),
  wf_node_id     NUMBER,
  wf_node_code   VARCHAR2(42),
  wf_node_name   VARCHAR2(100),
  wf_action_id   NUMBER not null,
  wf_action_code VARCHAR2(42),
  wf_action_name VARCHAR2(100),
  wf_condition   VARCHAR2(2000),
  send_type      CHAR(4) not null,
  enabled        CHAR(1) default ''1'',
  content_model  VARCHAR2(2000),
  last_ver       DATE default sysdate,
  upid           NUMBER not null,
  content_title  VARCHAR2(200)
)'
;
end if;

execute immediate 'comment on table MSG_RULES
  is ''��Ϣ����''';
execute immediate 'comment on column MSG_RULES.id
  is ''ID��ˮ��''';
execute immediate 'comment on column MSG_RULES.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column MSG_RULES.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_RULES.set_year
  is ''���''';
execute immediate 'comment on column MSG_RULES.msg_rule_code
  is ''�������''';
execute immediate 'comment on column MSG_RULES.msg_rule_name
  is ''��������''';
execute immediate 'comment on column MSG_RULES.invoketype
  is ''��Ϣ���ɷ�ʽ0ҵ��ϵͳ����1�������Զ�����''';
execute immediate 'comment on column MSG_RULES.wf_flow_id
  is ''����������ģ��ID''';
execute immediate 'comment on column MSG_RULES.wf_flow_code
  is ''����������ģ�����''';
execute immediate 'comment on column MSG_RULES.wf_flow_name
  is ''����������ģ������''';
execute immediate 'comment on column MSG_RULES.wf_node_id
  is ''�������ڵ�ID''';
execute immediate 'comment on column MSG_RULES.wf_node_code
  is ''�������ڵ����''';
execute immediate 'comment on column MSG_RULES.wf_node_name
  is ''�������ڵ�����''';
execute immediate 'comment on column MSG_RULES.wf_action_id
  is ''�������ڵ��������ID''';
execute immediate 'comment on column MSG_RULES.wf_action_code
  is ''�������ڵ�������ͱ���''';
execute immediate 'comment on column MSG_RULES.wf_action_name
  is ''�������ڵ������������''';
execute immediate 'comment on column MSG_RULES.wf_condition
  is ''���ݹ�������''';
execute immediate 'comment on column MSG_RULES.send_type
  is ''��Ϣ���ͷ�ʽ4λ���룺��1λϵͳ�ڷ��ͣ���2λ���ŷ��ͣ���3λ�ֻ�app���ͣ���4λ΢�ŷ��ͣ�ÿһλ0��ʾ�����ã�1��ʾ���á�����֧�ֶ��ź��ֻ�app�����Ϊ0110''';
execute immediate 'comment on column MSG_RULES.enabled
  is ''Ӧ��״̬0ͣ��1����''';
execute immediate 'comment on column MSG_RULES.content_model
  is ''��Ϣ����ģ��''';
execute immediate 'comment on column MSG_RULES.last_ver
  is ''������ʱ��''';
execute immediate 'comment on column MSG_RULES.upid
  is ''����ID������������£�''';
execute immediate 'comment on column MSG_RULES.content_title
  is ''��Ϣ����''';
execute immediate 'create index INDEX_MSG_RULES_UPID on MSG_RULES (UPID)
 ' ;
execute immediate 'create index INDEX_MSG_RULE_CODE on MSG_RULES (MSG_RULE_CODE)
  ';
execute immediate 'alter table MSG_RULES
  add constraint PK_MSG_RULE_ID primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='MSG_RULE_CONTENT_FIELDS
';
 if i=0 then
 execute immediate '
create table MSG_RULE_CONTENT_FIELDS
(
  id          NUMBER not null,
  msg_rule_id NUMBER not null,
  is_valid    CHAR(1) default ''1'',
  RG_CODE     VARCHAR2(42),
  set_year    NUMBER(4),
  key_name    VARCHAR2(60),
  value_name  VARCHAR2(60),
  upid        NUMBER not null
)'
;
end if;

execute immediate 'comment on table MSG_RULE_CONTENT_FIELDS
  is ''��Ϣ����ģ���ֶ�''';
execute immediate 'comment on column MSG_RULE_CONTENT_FIELDS.id
  is ''ID��ˮ��''';
execute immediate 'comment on column MSG_RULE_CONTENT_FIELDS.msg_rule_id
  is ''��Ϣ����ID''';
execute immediate 'comment on column MSG_RULE_CONTENT_FIELDS.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column MSG_RULE_CONTENT_FIELDS.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_RULE_CONTENT_FIELDS.set_year
  is ''���''';
execute immediate 'comment on column MSG_RULE_CONTENT_FIELDS.key_name
  is ''ռλ��������''';
execute immediate 'comment on column MSG_RULE_CONTENT_FIELDS.value_name
  is ''ҵ��Ҫ�ر�����''';
execute immediate 'comment on column MSG_RULE_CONTENT_FIELDS.upid
  is ''����ID������������£�''';
execute immediate 'create index INDEX_MSG_RULE_FIELDS_UPID on MSG_RULE_CONTENT_FIELDS (UPID)
  ';
execute immediate 'create index INDEX_MSG_RULE_FIELD_MSGID on MSG_RULE_CONTENT_FIELDS (MSG_RULE_ID)
 ';
execute immediate 'alter table MSG_RULE_CONTENT_FIELDS
  add constraint PK_MSG_RULE_CONTENT_FIELDS_ID primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='MSG_RULE_EN
';
 if i=0 then
 execute immediate '
create table MSG_RULE_EN
(
  id          NUMBER not null,
  msg_rule_id VARCHAR2(50),
  AGENCY_CODE     VARCHAR2(50),
  RG_CODE     VARCHAR2(42),
  set_year    NUMBER(4)
)'
;
end if;

execute immediate 'comment on table MSG_RULE_EN
  is ''��Ϣ�����޶�ʹ�õ�λ��''';
execute immediate 'comment on column MSG_RULE_EN.id
  is ''ID��ˮ��''';
execute immediate 'comment on column MSG_RULE_EN.msg_rule_id
  is ''����id''';
execute immediate 'comment on column MSG_RULE_EN.AGENCY_CODE
  is ''��λcode''';
execute immediate 'comment on column MSG_RULE_EN.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_RULE_EN.set_year
  is ''���''';
execute immediate 'alter table MSG_RULE_EN
  add constraint PK_MSG_RULE_EN primary key (ID)';
 
select count(*) into i from user_tables where table_name='MSG_RULE_RECEIVER
';
 if i=0 then
 execute immediate '
create table MSG_RULE_RECEIVER
(
  id            NUMBER not null,
  msg_rule_id   NUMBER,
  is_valid      CHAR(1) default ''1'',
  RG_CODE       VARCHAR2(42),
  set_year      NUMBER(4),
  is_user       CHAR(1) default ''1'' not null,
  user_id       VARCHAR2(60),
  user_group_id NUMBER,
  upid          NUMBER not null
)'
;
end if;

execute immediate 'comment on table MSG_RULE_RECEIVER
  is ''��Ϣ���������''';
execute immediate 'comment on column MSG_RULE_RECEIVER.id
  is ''ID��ˮ��''';
execute immediate 'comment on column MSG_RULE_RECEIVER.msg_rule_id
  is ''��Ϣ����ID''';
execute immediate 'comment on column MSG_RULE_RECEIVER.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column MSG_RULE_RECEIVER.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_RULE_RECEIVER.set_year
  is ''���''';
execute immediate 'comment on column MSG_RULE_RECEIVER.is_user
  is ''�Ƿ�Ϊ�û�0�û�Ⱥ1�û�''';
execute immediate 'comment on column MSG_RULE_RECEIVER.user_id
  is ''�û�ID''';
execute immediate 'comment on column MSG_RULE_RECEIVER.user_group_id
  is ''�û�ȺID''';
execute immediate 'comment on column MSG_RULE_RECEIVER.upid
  is ''����ID������������£�''';
execute immediate 'create index INDEX_MSG_RULE_RECEIVER_IU on MSG_RULE_RECEIVER (IS_USER, USER_ID, USER_GROUP_ID)
 ' ;
execute immediate 'create index INDEX_MSG_RULE_RECEIVER_RID on MSG_RULE_RECEIVER (MSG_RULE_ID)
  ';
execute immediate 'create index INDEX_MSG_RULE_RECEIVER_UPID on MSG_RULE_RECEIVER (UPID)
 ' ;
execute immediate 'alter table MSG_RULE_RECEIVER
  add constraint PK_MSG_RULE_RECEIVER_ID primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='MSG_SERIALIZE_OBJ
';
 if i=0 then
 execute immediate '
create table MSG_SERIALIZE_OBJ
(
  id       NUMBER not null,
  obj      BLOB,
  RG_CODE  VARCHAR2(42),
  set_year NUMBER(4)
)'
;
end if;

execute immediate 'comment on column MSG_SERIALIZE_OBJ.id
  is ''����''';
execute immediate 'comment on column MSG_SERIALIZE_OBJ.obj
  is ''���л�����''';
execute immediate 'comment on column MSG_SERIALIZE_OBJ.RG_CODE
  is ''����''';
execute immediate 'comment on column MSG_SERIALIZE_OBJ.set_year
  is ''ҵ�����''';
execute immediate 'alter table MSG_SERIALIZE_OBJ
  add constraint PK_MSG_SERIALIZE_OBJ primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='MSG_TASK_LOCK
';
 if i=0 then
 execute immediate '
create table MSG_TASK_LOCK
(
  status CHAR(1)
)'
;
end if;

execute immediate 'comment on table MSG_TASK_LOCK
  is ''��Ϣ�Զ�����������''';
execute immediate 'comment on column MSG_TASK_LOCK.status
  is ''0����̬;1ռ��̬''';

  
  
select count(*) into i from user_tables where table_name='SYS_ACC_RULE
';
 if i=0 then
 execute immediate '
create table SYS_ACC_RULE
(
  set_year  NUMBER(4),
  acc_table VARCHAR2(38),
  chr_id    VARCHAR2(38),
  rule_id   NUMBER,
  acc_id    VARCHAR2(38)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_ACTION
';
 if i=0 then
 execute immediate '
create table SYS_ACTION
(
  action_id   VARCHAR2(38) not null,
  action_code VARCHAR2(42) not null,
  action_name VARCHAR2(60),
  enabled     NUMBER(1) default 1 not null,
  func_name   VARCHAR2(60),
  class_name  VARCHAR2(200),
  param       VARCHAR2(200),
  action_type VARCHAR2(42),
  last_ver    VARCHAR2(30),
  sys_id      VARCHAR2(42)
)'
;
end if;

execute immediate 'comment on column SYS_ACTION.action_id
  is ''����ΨһID''';
execute immediate 'comment on column SYS_ACTION.action_code
  is ''�������''';
execute immediate 'comment on column SYS_ACTION.action_name
  is ''��������''';
execute immediate 'comment on column SYS_ACTION.enabled
  is ''�궨�����Ƿ�����(1����,0������)''';
execute immediate 'comment on column SYS_ACTION.func_name
  is ''������ʾ��Ϣ''';
execute immediate 'comment on column SYS_ACTION.class_name
  is ''��Ӧ������''';
execute immediate 'comment on column SYS_ACTION.param
  is ''���ò���(��a=a;b=b)''';
execute immediate 'comment on column SYS_ACTION.action_type
  is ''����ָ��Ĭ�ϵĲ������ͣ�''';
execute immediate 'comment on column SYS_ACTION.sys_id
  is ''Ӧ��ģ��ID''';
execute immediate 'alter table SYS_ACTION
  add constraint SYS_ACTION_PK primary key (ACTION_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_ACTION_VIEW
';
 if i=0 then
 execute immediate '
create table SYS_ACTION_VIEW
(
  action_id  VARCHAR2(38) not null,
  ui_id      VARCHAR2(38) not null,
  disp_order NUMBER(9),
  set_year   NUMBER(4) not null,
  remark     VARCHAR2(200),
  last_ver   VARCHAR2(20),
  key_value  VARCHAR2(50),
  RG_CODE    VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_ACTION_VIEW.action_id
  is ''����ID''';
execute immediate 'comment on column SYS_ACTION_VIEW.ui_id
  is ''��ͼID''';
execute immediate 'comment on column SYS_ACTION_VIEW.disp_order
  is ''��ʾ˳��''';
execute immediate 'comment on column SYS_ACTION_VIEW.set_year
  is ''���''';
execute immediate 'comment on column SYS_ACTION_VIEW.remark
  is ''����''';
execute immediate 'comment on column SYS_ACTION_VIEW.last_ver
  is ''����ʱ��''';
execute immediate 'comment on column SYS_ACTION_VIEW.key_value
  is ''�ؼ���''';
  
  
  
select count(*) into i from user_tables where table_name='SYS_APP
';
 if i=0 then
 execute immediate '
create table SYS_APP
(
  sys_guid           VARCHAR2(38),
  sys_id             VARCHAR2(42) not null,
  sys_name           VARCHAR2(60),
  sys_desc           VARCHAR2(200),
  enabled            NUMBER(1) default 1 not null,
  version            VARCHAR2(40),
  deploy_ip          VARCHAR2(20),
  deploy_port        VARCHAR2(20),
  deploy_folder      VARCHAR2(40),
  download_servlet   VARCHAR2(40),
  is_local           NUMBER(1) default 1,
  org_type           VARCHAR2(42),
  welcome_img        VARCHAR2(150),
  is_offline         NUMBER(1) default 0,
  application_handle VARCHAR2(100),
  last_ver           VARCHAR2(30),
  is_dstore          NUMBER(1) default (0),
  sys_year           VARCHAR2(4),
  jar_names          VARCHAR2(2000)
)'
;
end if;

execute immediate 'comment on column SYS_APP.sys_id
  is ''�ñ���궨��ǰ����ϵͳ''';
execute immediate 'comment on column SYS_APP.sys_name
  is ''��ϵͳ����''';
execute immediate 'comment on column SYS_APP.sys_desc
  is ''��ϸ��������ϵͳ''';
execute immediate 'comment on column SYS_APP.enabled
  is ''�궨ϵͳ�Ƿ�����(1����,0������)''';
execute immediate 'comment on column SYS_APP.version
  is ''�汾��''';
execute immediate 'comment on column SYS_APP.deploy_ip
  is ''��ϵͳ����·��IP��ַ''';
execute immediate 'comment on column SYS_APP.deploy_port
  is ''��ϵͳ����·���˿ں�''';
execute immediate 'comment on column SYS_APP.deploy_folder
  is ''��ϵͳ�����ļ���''';
execute immediate 'comment on column SYS_APP.download_servlet
  is ''��ϵͳ�ͻ�������servlet��''';
execute immediate 'comment on column SYS_APP.is_local
  is ''�Ƿ񱾵�ϵͳ��ֻ�б�ϵͳ�Ĳ���ά���˵�''';
execute immediate 'comment on column SYS_APP.org_type
  is ''�������ͣ���¼Ҫ�ؼ���''';
execute immediate 'comment on column SYS_APP.welcome_img
  is ''��ϵͳ�Ļ�ӭͼƬ''';
execute immediate 'comment on column SYS_APP.is_offline
  is ''�Ƿ�֧�����ߵ�¼''';
execute immediate 'comment on column SYS_APP.application_handle
  is ''Ӧ��ϵͳ�¼�������''';
execute immediate 'comment on column SYS_APP.is_dstore
  is ''�Ƿ�ֲ�ʽ(1��,0��)''';
execute immediate 'comment on column SYS_APP.jar_names
  is ''Jar�������ԷֺŸ��������һ��jar�������ؼӷֺ�''';
execute immediate 'alter table SYS_APP
  add constraint SYS_APP_PK primary key (SYS_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_AUTOTASK
';
 if i=0 then
 execute immediate '
create table SYS_AUTOTASK
(
  autotask_id       NUMBER(9) not null,
  set_year          NUMBER(4) not null,
  autotask_code     VARCHAR2(42),
  autotask_name     VARCHAR2(60),
  autotask_desc     VARCHAR2(2000),
  autotask_type     NUMBER(1),
  start_date        VARCHAR2(30),
  end_date          VARCHAR2(30),
  taskinterval      NUMBER(9),
  run_times         NUMBER(9),
  enabled           NUMBER(1) default 1,
  autotask_bean     VARCHAR2(500),
  autotask_param    VARCHAR2(500),
  remark            VARCHAR2(200),
  create_user       VARCHAR2(38),
  create_date       VARCHAR2(30),
  last_ver          VARCHAR2(30),
  schedule_crontype NUMBER(1),
  month_of_year     NUMBER(9),
  day_of_week       NUMBER(9),
  day_of_month      NUMBER(9),
  hour_of_day       NUMBER(9),
  minute_of_day     NUMBER(9),
  sys_id            VARCHAR2(42),
  RG_CODE           VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_AUTOTASK.autotask_id
  is ''�Զ�����ID''';
execute immediate 'comment on column SYS_AUTOTASK.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_AUTOTASK.autotask_code
  is ''�Զ��������''';
execute immediate 'comment on column SYS_AUTOTASK.autotask_name
  is ''�Զ���������''';
execute immediate 'comment on column SYS_AUTOTASK.autotask_desc
  is ''�Զ�������ϸ����''';
execute immediate 'comment on column SYS_AUTOTASK.autotask_type
  is ''1��������� 2����������''';
execute immediate 'comment on column SYS_AUTOTASK.start_date
  is ''��ʼ����''';
execute immediate 'comment on column SYS_AUTOTASK.end_date
  is ''��������''';
execute immediate 'comment on column SYS_AUTOTASK.taskinterval
  is ''ʱ�������룩''';
execute immediate 'comment on column SYS_AUTOTASK.run_times
  is ''���д���''';
execute immediate 'comment on column SYS_AUTOTASK.enabled
  is ''�Ƿ�����''';
execute immediate 'comment on column SYS_AUTOTASK.autotask_bean
  is ''�Զ��������з���ʵ��''';
execute immediate 'comment on column SYS_AUTOTASK.autotask_param
  is ''����ʵ�����''';
execute immediate 'comment on column SYS_AUTOTASK.remark
  is ''��ע''';
execute immediate 'comment on column SYS_AUTOTASK.create_user
  is ''�����û�''';
execute immediate 'comment on column SYS_AUTOTASK.create_date
  is ''����ʱ��''';
execute immediate 'comment on column SYS_AUTOTASK.last_ver
  is ''���汾''';
execute immediate 'comment on column SYS_AUTOTASK.schedule_crontype
  is ''0��ÿ�� 1��ÿ�� 2��ÿ�� 3��ÿ��''';
execute immediate 'comment on column SYS_AUTOTASK.month_of_year
  is ''�ڼ���''';
execute immediate 'comment on column SYS_AUTOTASK.day_of_week
  is ''ÿ���ڼ�''';
execute immediate 'comment on column SYS_AUTOTASK.day_of_month
  is ''ÿ�µڼ���''';
execute immediate 'comment on column SYS_AUTOTASK.hour_of_day
  is ''Сʱ''';
execute immediate 'comment on column SYS_AUTOTASK.minute_of_day
  is ''����''';
execute immediate 'comment on column SYS_AUTOTASK.sys_id
  is ''Ӧ��ģ��ID''';
execute immediate 'alter table SYS_AUTOTASK
  add constraint PK_SYS_AUTOTASK primary key (AUTOTASK_ID)';
  
  
  
  
select count(*) into i from user_tables where table_name='SYS_AUTOTASK_MONITOR
';
 if i=0 then
 execute immediate '
create table SYS_AUTOTASK_MONITOR
(
  autotask_id   NUMBER(9) not null,
  set_year      NUMBER(4) not null,
  start_time    VARCHAR2(30),
  last_exe_time VARCHAR2(30),
  totaltime     NUMBER(16),
  total_count   NUMBER(9),
  fail_count    NUMBER(9),
  success_count NUMBER(9),
  task_status   NUMBER(1),
  RG_CODE       VARCHAR2(42),
  token_id      VARCHAR2(42) default SYS_GUID()
)'
;
end if;

execute immediate 'comment on column SYS_AUTOTASK_MONITOR.autotask_id
  is ''SYS_AUTOTASK��AUTOTASK_ID����''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.start_time
  is ''��һ��ִ��ʱ��''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.last_exe_time
  is ''�ϴ�ִ��ʱ��''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.totaltime
  is ''�ϴ�ִ�к�ʱ''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.total_count
  is ''�����ܴ���''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.fail_count
  is ''����ʧ�ܴ���''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.success_count
  is ''���гɹ�����''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.task_status
  is ''����״̬:1:δ���� 2:������ 3:���� 4:ִ����''';
execute immediate 'comment on column SYS_AUTOTASK_MONITOR.token_id
  is ''����ID''';
execute immediate 'alter table SYS_AUTOTASK_MONITOR
  add constraint PK_SYS_AUTOTASK_MONITOR primary key (AUTOTASK_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLNO
';
 if i=0 then
 execute immediate '
create table SYS_BILLNO
(
  billnoruleline_id VARCHAR2(38) not null,
  ele_value         VARCHAR2(4000) not null,
  max_no            NUMBER(9) default 1 not null,
  latest_op_date    VARCHAR2(30),
  latest_op_user    VARCHAR2(38),
  set_year          NUMBER(4) not null,
  last_ver          VARCHAR2(30),
  RG_CODE           VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLNO.billnoruleline_id
  is ''������ID''';
execute immediate 'comment on column SYS_BILLNO.ele_value
  is ''Ҫ��ֵ���''';
execute immediate 'comment on column SYS_BILLNO.max_no
  is ''��ǰ��󵥺�''';
execute immediate 'comment on column SYS_BILLNO.latest_op_date
  is ''������ʱ��''';
execute immediate 'comment on column SYS_BILLNO.latest_op_user
  is ''��������''';
execute immediate 'comment on column SYS_BILLNO.set_year
  is ''ҵ�����''';
execute immediate 'alter table SYS_BILLNO
  add constraint SYS_BILLNO_PK primary key (BILLNORULELINE_ID, ELE_VALUE)';
  
  

select count(*) into i from user_tables where table_name='SYS_BILLNOBREAK
';
 if i=0 then
 execute immediate '
create table SYS_BILLNOBREAK
(
  break_id          VARCHAR2(40) not null,
  billnoruleline_id VARCHAR2(38),
  ele_value         VARCHAR2(4000),
  bill_no           VARCHAR2(128),
  break_no          NUMBER(9),
  breakno_status    CHAR(1),
  last_ver          VARCHAR2(30),
  RG_CODE           VARCHAR2(42) not null,
  set_year          NUMBER(4) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLNOBREAK.breakno_status
  is ''0��δʹ��,1��ռ��,2����ʹ��,3���Ϻ�δʹ��''';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLNORULE
';
 if i=0 then
 execute immediate '
create table SYS_BILLNORULE
(
  billnorule_id   VARCHAR2(38) not null,
  billnorule_code VARCHAR2(42) not null,
  billnorule_name VARCHAR2(60),
  latest_op_date  VARCHAR2(30),
  latest_op_user  VARCHAR2(38),
  set_year        NUMBER(4) not null,
  last_ver        VARCHAR2(30),
  sys_id          VARCHAR2(42),
  RG_CODE         VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLNORULE.billnorule_id
  is '' ��Ź���ID''';
execute immediate 'comment on column SYS_BILLNORULE.billnorule_code
  is ''��Ź���CODE''';
execute immediate 'comment on column SYS_BILLNORULE.billnorule_name
  is ''��Ź�������''';
execute immediate 'comment on column SYS_BILLNORULE.latest_op_date
  is ''������ʱ��''';
execute immediate 'comment on column SYS_BILLNORULE.latest_op_user
  is ''��������''';
execute immediate 'comment on column SYS_BILLNORULE.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_BILLNORULE.sys_id
  is ''Ӧ��ģ��ID''';
execute immediate 'alter table SYS_BILLNORULE
  add constraint SYS_BILLNORULE_PK primary key (BILLNORULE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLNORULEELE
';
 if i=0 then
 execute immediate '
create table SYS_BILLNORULEELE
(
  billnoruleline_id VARCHAR2(38) not null,
  ele_code          VARCHAR2(42) not null,
  level_num         NUMBER(2) default -1,
  set_year          NUMBER(4) not null,
  last_ver          VARCHAR2(30),
  RG_CODE           VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLNORULEELE.billnoruleline_id
  is ''������ID''';
execute immediate 'comment on column SYS_BILLNORULEELE.ele_code
  is ''Ҫ�ؼ��''';
execute immediate 'comment on column SYS_BILLNORULEELE.level_num
  is ''Ҫ�ؼ���''';
execute immediate 'comment on column SYS_BILLNORULEELE.set_year
  is ''ҵ�����''';
execute immediate 'alter table SYS_BILLNORULEELE
  add constraint SYS_BILLNORULEELE_PK primary key (BILLNORULELINE_ID, ELE_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLNORULELINE
';
 if i=0 then
 execute immediate '
create table SYS_BILLNORULELINE
(
  billnorule_id     VARCHAR2(38) not null,
  billnoruleline_id VARCHAR2(38) not null,
  line_no           NUMBER(9),
  line_type         NUMBER(1),
  line_format       VARCHAR2(42),
  init_value        VARCHAR2(60),
  ele_code          VARCHAR2(42),
  level_num         NUMBER(2),
  latest_op_date    VARCHAR2(30),
  latest_op_user    VARCHAR2(38),
  set_year          NUMBER(4) not null,
  last_ver          VARCHAR2(30),
  RG_CODE           VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLNORULELINE.billnorule_id
  is ''��Ź���ID''';
execute immediate 'comment on column SYS_BILLNORULELINE.billnoruleline_id
  is ''��Ź�����ID''';
execute immediate 'comment on column SYS_BILLNORULELINE.line_no
  is ''������˳���''';
execute immediate 'comment on column SYS_BILLNORULELINE.line_type
  is ''����������''';
execute immediate 'comment on column SYS_BILLNORULELINE.line_format
  is ''�������ʽ''';
execute immediate 'comment on column SYS_BILLNORULELINE.init_value
  is ''�������ʼֵ''';
execute immediate 'comment on column SYS_BILLNORULELINE.ele_code
  is ''Ҫ�ؼ��''';
execute immediate 'comment on column SYS_BILLNORULELINE.level_num
  is ''Ҫ�ؼ���''';
execute immediate 'comment on column SYS_BILLNORULELINE.latest_op_date
  is ''������ʱ��''';
execute immediate 'comment on column SYS_BILLNORULELINE.latest_op_user
  is ''��������''';
execute immediate 'comment on column SYS_BILLNORULELINE.set_year
  is ''ҵ�����''';
execute immediate 'alter table SYS_BILLNORULELINE
  add constraint SYS_BILLNORULELINE_PK primary key (BILLNORULELINE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLSUMSET
';
 if i=0 then
 execute immediate '
create table SYS_BILLSUMSET
(
  id          VARCHAR2(38) not null,
  billtype_id VARCHAR2(42),
  ele_code    VARCHAR2(42),
  disp_name   VARCHAR2(42),
  disp_type   NUMBER(1),
  level_num   NUMBER(2),
  level_limit NUMBER(2),
  disp_order  NUMBER(3),
  set_year    NUMBER(4),
  last_ver    VARCHAR2(20),
  RG_CODE     VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLSUMSET.id
  is ''����''';
execute immediate 'comment on column SYS_BILLSUMSET.billtype_id
  is ''��������''';
execute immediate 'comment on column SYS_BILLSUMSET.ele_code
  is ''�ֶ���''';
execute immediate 'comment on column SYS_BILLSUMSET.disp_name
  is ''��ʾ����''';
execute immediate 'comment on column SYS_BILLSUMSET.disp_type
  is ''��ʾ����(-1Ϊ��ɫ��ʾ��ѡ�У�0Ϊ��������ѡ�У�1Ϊ������ʾ��ѡ��)''';
execute immediate 'comment on column SYS_BILLSUMSET.level_num
  is ''Ĭ�ϼ���''';
execute immediate 'comment on column SYS_BILLSUMSET.level_limit
  is ''���η�Χ''';
execute immediate 'comment on column SYS_BILLSUMSET.disp_order
  is ''��ʾ˳��''';
execute immediate 'comment on column SYS_BILLSUMSET.set_year
  is ''ҵ�����''';

execute immediate 'alter table SYS_BILLSUMSET
  add constraint PRI_SYS_BILLSUMSET primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLTYPE
';
 if i=0 then
 execute immediate '
create table SYS_BILLTYPE
(
  billtype_id            VARCHAR2(38) not null,
  billtype_code          VARCHAR2(42) not null,
  billtype_name          VARCHAR2(60) not null,
  is_busincrease         NUMBER(1),
  busvou_type_id         VARCHAR2(38),
  coa_id                 VARCHAR2(38),
  billnorule_id          VARCHAR2(38),
  table_name             VARCHAR2(100),
  billtype_class         NUMBER(1),
  enabled                NUMBER(1) default 1,
  latest_op_date         VARCHAR2(30),
  latest_op_user         VARCHAR2(42),
  set_year               NUMBER(4),
  last_ver               VARCHAR2(30),
  nobudgetbusvou_type_id VARCHAR2(38),
  is_needchecknobudget   NUMBER(1),
  sys_id                 VARCHAR2(42),
  field_name             VARCHAR2(60),
  ui_id                  VARCHAR2(42),
  from_billtype_id       VARCHAR2(42),
  from_ui_id             VARCHAR2(42),
  to_billtype_id         VARCHAR2(42),
  to_ui_id               VARCHAR2(42),
  vou_control_id         VARCHAR2(42),
  RG_CODE                VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLTYPE.billtype_id
  is ''����ƾ֤����ID''';
execute immediate 'comment on column SYS_BILLTYPE.billtype_code
  is ''����ƾ֤����CODE''';
execute immediate 'comment on column SYS_BILLTYPE.billtype_name
  is ''����ƾ֤��������''';
execute immediate 'comment on column SYS_BILLTYPE.is_busincrease
  is ''ҵ����(1��,0��)''';
execute immediate 'comment on column SYS_BILLTYPE.busvou_type_id
  is ''������ID''';
execute immediate 'comment on column SYS_BILLTYPE.coa_id
  is ''��ӦCOA''';
execute immediate 'comment on column SYS_BILLTYPE.billnorule_id
  is ''��Ź���ID''';
execute immediate 'comment on column SYS_BILLTYPE.table_name
  is ''��Ӧ����''';
execute immediate 'comment on column SYS_BILLTYPE.billtype_class
  is ''ƾ֤���ͷ���(0ҵ�� 1 ��ϸ)''';
execute immediate 'comment on column SYS_BILLTYPE.enabled
  is ''�Ƿ�����(1 ���� 0 ������)''';
execute immediate 'comment on column SYS_BILLTYPE.latest_op_date
  is ''����޸�ʱ�䣺''';
execute immediate 'comment on column SYS_BILLTYPE.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_BILLTYPE.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_BILLTYPE.nobudgetbusvou_type_id
  is ''��ָ�꽻����ID''';
execute immediate 'comment on column SYS_BILLTYPE.is_needchecknobudget
  is ''�Ƿ�Ҫ������ָ�����⴦��(O:�� 1:��)''';
execute immediate 'comment on column SYS_BILLTYPE.sys_id
  is ''��Ӧ��ϵͳ''';
execute immediate 'comment on column SYS_BILLTYPE.field_name
  is ''��Ӧ����ֶ�''';
execute immediate 'comment on column SYS_BILLTYPE.ui_id
  is ''Ĭ��¼����ͼ''';
execute immediate 'comment on column SYS_BILLTYPE.from_billtype_id
  is ''��Դ������ϸ����''';
execute immediate 'comment on column SYS_BILLTYPE.from_ui_id
  is ''¼����ͼ����Դ��''';
execute immediate 'comment on column SYS_BILLTYPE.to_billtype_id
  is ''ȥ�򵥾���ϸ����''';
execute immediate 'comment on column SYS_BILLTYPE.to_ui_id
  is ''¼����ͼ��ȥ��''';
execute immediate 'comment on column SYS_BILLTYPE.vou_control_id
  is ''�ο�¼���ȿ�������''';
execute immediate 'alter table SYS_BILLTYPE
  add constraint PK_SYS_BILLTYPE primary key (BILLTYPE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLTYPE_ASSELE
';
 if i=0 then
 execute immediate '
create table SYS_BILLTYPE_ASSELE
(
  assele_id      VARCHAR2(38) not null,
  billtype_id    VARCHAR2(38) not null,
  ele_code       VARCHAR2(42),
  latest_op_user VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  set_year       NUMBER(4) not null,
  level_num      NUMBER(2),
  last_ver       VARCHAR2(30),
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLTYPE_ASSELE.assele_id
  is ''��Ź���ID''';
execute immediate 'comment on column SYS_BILLTYPE_ASSELE.billtype_id
  is ''����ƾ֤����ID''';
execute immediate 'comment on column SYS_BILLTYPE_ASSELE.ele_code
  is ''Ҫ�ش���''';
execute immediate 'comment on column SYS_BILLTYPE_ASSELE.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_BILLTYPE_ASSELE.latest_op_date
  is ''����޸�����''';
execute immediate 'comment on column SYS_BILLTYPE_ASSELE.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_BILLTYPE_ASSELE.level_num
  is ''Ҫ�ؼ���(-2 ���⼶�� -1 �׼� 1~9  ָ������)''';
execute immediate 'alter table SYS_BILLTYPE_ASSELE
  add constraint PK_SYS_BILLTYPE_ASSELE primary key (ASSELE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLTYPE_REPORT
';
 if i=0 then
 execute immediate '
create table SYS_BILLTYPE_REPORT
(
  id            VARCHAR2(38) not null,
  billtype_id   VARCHAR2(42),
  report_file   VARCHAR2(255),
  report_title  VARCHAR2(255),
  arg_list      VARCHAR2(255),
  display_order NUMBER(1),
  para_list     VARCHAR2(255),
  RG_CODE       VARCHAR2(42) not null,
  set_year      NUMBER(4) not null
)'
;
end if;

execute immediate 'alter table SYS_BILLTYPE_REPORT
  add constraint SYS_BILLTYPE_REPORT_PK_PRO7 primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BILLTYPE_VALUESET
';
 if i=0 then
 execute immediate '
create table SYS_BILLTYPE_VALUESET
(
  valueset_id    VARCHAR2(38) not null,
  valueset_type  NUMBER(1) not null,
  field_code     VARCHAR2(60) not null,
  default_value  VARCHAR2(2000),
  billtype_id    VARCHAR2(38),
  ele_rule_id    VARCHAR2(38),
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(38),
  latest_op_date VARCHAR2(30),
  latest_op_user VARCHAR2(38),
  last_ver       VARCHAR2(30) not null,
  set_year       NUMBER(4) not null,
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_BILLTYPE_VALUESET.valueset_id
  is ''��ֵ����ID''';
execute immediate 'comment on column SYS_BILLTYPE_VALUESET.valueset_type
  is ''��ֵ����(0Ĭ��ֵ 1Ҫ�ع��� 2�������)''';
execute immediate 'comment on column SYS_BILLTYPE_VALUESET.field_code
  is ''�ֶ���''';
execute immediate 'comment on column SYS_BILLTYPE_VALUESET.default_value
  is ''Ĭ��ֵ(���ֶζ�ֵ��������ΪĬ��ֵʱ����Ч)''';
execute immediate 'comment on column SYS_BILLTYPE_VALUESET.billtype_id
  is ''����ƾ֤����ID''';
execute immediate 'comment on column SYS_BILLTYPE_VALUESET.ele_rule_id
  is ''Ҫ�ض�ֵ����ID''';

execute immediate 'comment on column SYS_BILLTYPE_VALUESET.create_user
  is ''�����û�''';

execute immediate 'comment on column SYS_BILLTYPE_VALUESET.latest_op_user
  is ''����޸��û�''';

execute immediate 'comment on column SYS_BILLTYPE_VALUESET.set_year
  is ''ҵ�����''';
execute immediate 'alter table SYS_BILLTYPE_VALUESET
  add constraint SYS_BILLTYPE_VALUESET_PK primary key (VALUESET_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_BUTTON
';
 if i=0 then
 execute immediate '
create table SYS_BUTTON
(
  button_id        VARCHAR2(38) not null,
  action_id        VARCHAR2(38) not null,
  module_id        NUMBER,
  display_order    NUMBER(3) not null,
  display_title    VARCHAR2(100) not null,
  tips             VARCHAR2(200),
  group_name       VARCHAR2(38),
  button_icon      VARCHAR2(100),
  group_icon       VARCHAR2(38),
  parent_button_id VARCHAR2(38),
  last_ver         VARCHAR2(30)
)'
;
end if;

execute immediate 'alter table SYS_BUTTON
  add constraint SYS_BUTTON_PK_PRO7 primary key (BUTTON_ID)';
  
  

select count(*) into i from user_tables where table_name='SYS_CHECK_CHRID
';
 if i=0 then
 execute immediate '  
create table SYS_CHECK_CHRID
(
  ele_source VARCHAR2(64),
  ele_code   VARCHAR2(64),
  ele_name   VARCHAR2(64),
  chr_code   VARCHAR2(64),
  chr_code1  VARCHAR2(64),
  chr_code2  VARCHAR2(64),
  chr_code3  VARCHAR2(64),
  chr_code4  VARCHAR2(64),
  chr_code5  VARCHAR2(64),
  chr_id     VARCHAR2(64),
  chr_id1    VARCHAR2(64),
  chr_id2    VARCHAR2(64),
  chr_id3    VARCHAR2(64),
  chr_id4    VARCHAR2(64),
  chr_id5    VARCHAR2(64),
  chr_name   VARCHAR2(200),
  parent_id  VARCHAR2(64),
  level_num  NUMBER(2),
  error_type VARCHAR2(64)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_CHECK_CODE_NAME
';
 if i=0 then
 execute immediate '
create table SYS_CHECK_CODE_NAME
(
  ele_source   VARCHAR2(64),
  ele_code     VARCHAR2(64),
  ele_name     VARCHAR2(64),
  chr_code     VARCHAR2(64),
  chr_name     VARCHAR2(64),
  repeat_feild VARCHAR2(64)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_CHECK_RELATIONS
';
 if i=0 then
 execute immediate '
create table SYS_CHECK_RELATIONS
(
  relation_code VARCHAR2(42),
  ele_code      VARCHAR2(64),
  ele_name      VARCHAR2(64),
  ele_source    VARCHAR2(64),
  pri_ele_value VARCHAR2(64),
  sec_ele_value VARCHAR2(64),
  invalid_feild VARCHAR2(64)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_CHECK_RULE
';
 if i=0 then
 execute immediate '
create table SYS_CHECK_RULE
(
  ele_rule_code VARCHAR2(42),
  ele_rule_name VARCHAR2(64),
  ele_code      VARCHAR2(64),
  ele_name      VARCHAR2(64),
  ele_source    VARCHAR2(64),
  ele_value     VARCHAR2(64)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_DATABASE
';
 if i=0 then
 execute immediate '
create table SYS_DATABASE
(
  chr_id        VARCHAR2(38) not null,
  set_year      NUMBER(4) default 0 not null,
  int_dbtype    NUMBER(2) default 0,
  sys_id        VARCHAR2(42) default 0 not null,
  is_dstore     NUMBER(1) default 0 not null,
  db_name       VARCHAR2(42),
  db_user       VARCHAR2(42) not null,
  db_pwd        VARCHAR2(60),
  db_host       VARCHAR2(42),
  db_port       VARCHAR2(10),
  db_version    VARCHAR2(60),
  max_pool_size NUMBER(2) default 0 not null,
  pool_name     VARCHAR2(60) not null,
  RG_CODE       VARCHAR2(42),
  last_ver      VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_DATABASE.chr_id
  is ''Ψһ��ʶ������Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column SYS_DATABASE.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_DATABASE.int_dbtype
  is ''������''';
execute immediate 'comment on column SYS_DATABASE.sys_id
  is ''��ϵͳ''';
execute immediate 'comment on column SYS_DATABASE.is_dstore
  is ''�Ƿ�ֲ�ʽ�洢������ǣ������ݿ�����Ϊ��(1��,0��)''';
execute immediate 'comment on column SYS_DATABASE.db_name
  is ''���ݿ�����''';
execute immediate 'comment on column SYS_DATABASE.db_user
  is ''��Ӧ���½�û�''';
execute immediate 'comment on column SYS_DATABASE.db_pwd
  is ''�û����룬ʹ��md5����''';
execute immediate 'comment on column SYS_DATABASE.db_host
  is ''�ֲ�ʽ�洢ʱ��Ҫ��д��Ӧ������/IP''';
execute immediate 'comment on column SYS_DATABASE.db_port
  is ''���ݿ�����˿�''';
execute immediate 'comment on column SYS_DATABASE.db_version
  is ''���ݿ�汾�ţ����û�б�����Ĭ��Ϊ���°汾��''';
execute immediate 'comment on column SYS_DATABASE.max_pool_size
  is ''������ӳظ���''';
execute immediate 'comment on column SYS_DATABASE.pool_name
  is ''���ӳ�������Ӧ������ӳ���ļ�Ŀ¼������Ϊpay���������Ϊmappingfiles.pay.*.xml''';
execute immediate 'comment on column SYS_DATABASE.RG_CODE
  is ''��ǰ������''';
execute immediate 'create index SYS_DATABASE_N1 on SYS_DATABASE (SET_YEAR, SYS_ID)
  ';
execute immediate 'alter table SYS_DATABASE
  add constraint SYS_DATABASE_PK primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_ELEMENT
';
 if i=0 then
 execute immediate '
create table SYS_ELEMENT
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
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_ELEMENT.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column SYS_ELEMENT.set_year
  is ''��Ӧҵ�����''';
execute immediate 'comment on column SYS_ELEMENT.ele_source
  is ''��Ƕ�ӦҪ�ر�''';
execute immediate 'comment on column SYS_ELEMENT.ele_code
  is ''��ӦҪ�ر�����Ӧ��Ҫ�ر���''';
execute immediate 'comment on column SYS_ELEMENT.ele_name
  is ''��ӦҪ�ص�������''';
execute immediate 'comment on column SYS_ELEMENT.enabled
  is ''�Ƿ����ö�ӦҪ��''';
execute immediate 'comment on column SYS_ELEMENT.dispmode
  is ''����Ҫ����ʾ�ķ�ʽ��Ĭ��Ϊ0��''';
execute immediate 'comment on column SYS_ELEMENT.ref_mode
  is ''������/������/�б����''';
execute immediate 'comment on column SYS_ELEMENT.is_rightfilter
  is ''���ڿ���Ҫ������Ȩ�ޣ���Ҫ��������Ȩ�޵����ã����ȼ����''';
execute immediate 'comment on column SYS_ELEMENT.max_level
  is ''�趨��ӦҪ�ص���󼶴�''';
execute immediate 'comment on column SYS_ELEMENT.code_rule
  is ''�����ǿ�ƹ淶��null��������,��:3-3-3''';
execute immediate 'comment on column SYS_ELEMENT.level_name
  is ''�÷�б������,������Ҫ������ʾ�գ���/��/��/Ŀ��''';

execute immediate 'comment on column SYS_ELEMENT.create_user
  is ''�����û�''';

execute immediate 'comment on column SYS_ELEMENT.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_ELEMENT.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column SYS_ELEMENT.is_nolevel
  is ''�궨Ҫ���Ƿ񲻴��ڼ���''';
execute immediate 'comment on column SYS_ELEMENT.is_system
  is ''�Ƿ�Ԥ��Ҫ��''';
execute immediate 'comment on column SYS_ELEMENT.ele_type
  is ''Ҫ������    1 ����Ҫ���ࡢ2 ����Ҫ���ࡢ��������
 ''';
execute immediate 'comment on column SYS_ELEMENT.is_view
  is ''�Ƿ��߼�Ҫ��''';
execute immediate 'comment on column SYS_ELEMENT.czgb_code
  is ''�������ұ�׼�룬�ֽ׶δ洢������ͼʹ�õı���''';
execute immediate 'comment on column SYS_ELEMENT.disp_order
  is ''��ʾ˳��''';
execute immediate 'comment on column SYS_ELEMENT.sys_id
  is ''Ӧ��ģ��ID''';
execute immediate 'comment on column SYS_ELEMENT.is_operate
  is ''��Ҫ���Ƿ�����ƽ̨��ά����1��ƽ̨ά����0����ϵͳ����ά��''';
execute immediate 'create index SYS_ELEMENT_N1 on SYS_ELEMENT (SET_YEAR)';
  
execute immediate 'create index SYS_ELEMENT_N2 on SYS_ELEMENT (ELE_CODE)';
  
execute immediate 'alter table SYS_ELEMENT
  add constraint SYS_ELEMENT_PK primary key (CHR_ID)';
  
select count(*) into i from user_tables where table_name='SYS_ELE_CTRL_RULE';
if i=0 then execute immediate '
create table SYS_ELE_CTRL_RULE
(
  ctrl_id        VARCHAR2(38) not null,
  set_year       NUMBER(4),
  RG_CODE        VARCHAR2(42),
  ctrl_ele_code  VARCHAR2(42),
  ctrl_level_num VARCHAR2(4),
  ctrl_rg_code   VARCHAR2(42),
  ctrl_type      VARCHAR2(4),
  ctrl_code      VARCHAR2(38) not null,
  ctrl_name      VARCHAR2(38) not null,
  ctrl_reserve   VARCHAR2(42),
  is_valid       VARCHAR2(4) default ''1'',
  ctrl_rg_name   VARCHAR2(42),
  ctrl_rg_id     VARCHAR2(42)
)'
;
end if;

execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.set_year
  is ''��Ӧҵ�����''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.RG_CODE
  is ''������''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_ele_code
  is ''����Ҫ�ر���''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_level_num
  is ''���Ƽ��Σ�1#һ��+2#����+3#����+4#�ļ�+5#�弶+6#����''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_rg_code
  is ''��������''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_type
  is ''����ģʽ��1.�Կ��������¼�������������Ч��2.���Կ���������Ч''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_code
  is ''Ҫ����Ȩ����''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_name
  is ''Ҫ����Ȩ����''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_reserve
  is ''Ԥ���ֶ�''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.is_valid
  is ''�Ƿ����ã�0#��+1#��''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_rg_name
  is ''������������''';
execute immediate 'comment on column SYS_ELE_CTRL_RULE.ctrl_rg_id
  is ''��������ID''';
execute immediate 'create index CTRL_ELE_CODE_INDEX on SYS_ELE_CTRL_RULE (CTRL_ELE_CODE)
 ' ;
execute immediate 'create index RG_CODE_INDEX on SYS_ELE_CTRL_RULE (RG_CODE)
 ' ;
execute immediate 'create index SET_YEAR_INDEX on SYS_ELE_CTRL_RULE (SET_YEAR)
 ' ;
execute immediate 'alter table SYS_ELE_CTRL_RULE
  add constraint CTRL_ID_KEY primary key (CTRL_ID)
   
  ';
execute immediate 'alter table SYS_ELE_CTRL_RULE
  add constraint CTRL_CODE_KEY unique (CTRL_CODE, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_ELE_RULE
';
 if i=0 then
 execute immediate '
create table SYS_ELE_RULE
(
  ele_rule_id    VARCHAR2(38) not null,
  ele_rule_code  VARCHAR2(42) not null,
  ele_rule_name  VARCHAR2(60) not null,
  ele_code       VARCHAR2(60) not null,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(38),
  latest_op_date VARCHAR2(30),
  latest_op_user VARCHAR2(38),
  last_ver       VARCHAR2(30) not null,
  set_year       NUMBER(4) not null,
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_ELE_RULE.ele_rule_id
  is ''Ҫ�ض�ֵ����ID''';
execute immediate 'comment on column SYS_ELE_RULE.ele_rule_code
  is ''Ҫ�ض�ֵ�������''';
execute immediate 'comment on column SYS_ELE_RULE.ele_rule_name
  is ''Ҫ�ض�ֵ��������''';
execute immediate 'comment on column SYS_ELE_RULE.ele_code
  is ''Ҫ�ؼ��''';

execute immediate 'comment on column SYS_ELE_RULE.create_user
  is ''�����û�''';

execute immediate 'comment on column SYS_ELE_RULE.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_ELE_RULE.last_ver
  is ''���汾''';
execute immediate 'comment on column SYS_ELE_RULE.set_year
  is ''ҵ�����''';
execute immediate 'alter table SYS_ELE_RULE
  add constraint SYS_ELE_RULE_PK primary key (ELE_RULE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_ELE_RULE_DETAIL
';
 if i=0 then
 execute immediate '
create table SYS_ELE_RULE_DETAIL
(
  ele_rule_detail_id VARCHAR2(38) not null,
  ele_value          VARCHAR2(42) not null,
  ele_rule_id        VARCHAR2(38) not null,
  rule_id            NUMBER,
  create_date        VARCHAR2(30),
  create_user        VARCHAR2(38),
  latest_op_date     VARCHAR2(30),
  latest_op_user     VARCHAR2(38),
  last_ver           VARCHAR2(30) not null,
  set_year           NUMBER(4) not null,
  ele_code           VARCHAR2(42),
  ele_name           VARCHAR2(60),
  RG_CODE            VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_ELE_RULE_DETAIL
  add constraint SYS_ELE_RULE_DETAIL_PRO_PK primary key (ELE_RULE_DETAIL_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_ENUMERATE
';
 if i=0 then
 execute immediate '
create table SYS_ENUMERATE
(
  chr_id         VARCHAR2(38) not null,
  field_code     VARCHAR2(38) not null,
  enu_code       VARCHAR2(42),
  enu_name       VARCHAR2(60),
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(38),
  latest_op_date VARCHAR2(30),
  latest_op_user VARCHAR2(38),
  is_deleted     NUMBER(1) default 0,
  last_ver       VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_ENUMERATE.chr_id
  is ''ö�ٱ�����,guid����''';
execute immediate 'comment on column SYS_ENUMERATE.field_code
  is ''�ֶα���''';
execute immediate 'comment on column SYS_ENUMERATE.enu_code
  is ''ö�ٱ���''';
execute immediate 'comment on column SYS_ENUMERATE.enu_name
  is ''ö������''';
execute immediate 'comment on column SYS_ENUMERATE.create_date
  is ''��������''';
execute immediate 'comment on column SYS_ENUMERATE.create_user
  is ''������''';
execute immediate 'comment on column SYS_ENUMERATE.latest_op_date
  is ''����������''';
execute immediate 'comment on column SYS_ENUMERATE.latest_op_user
  is ''�������û�''';
execute immediate 'comment on column SYS_ENUMERATE.is_deleted
  is ''�Ƿ�ɾ��''';
execute immediate 'alter table SYS_ENUMERATE
  add constraint SYS_ENUMERATE_U1 primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_EXPIMP_CONFIG_FIELD
';
 if i=0 then
 execute immediate '
create table SYS_EXPIMP_CONFIG_FIELD
(
  config_table_code VARCHAR2(60),
  config_field_code VARCHAR2(60),
  config_field_name VARCHAR2(60),
  is_must           NUMBER(1),
  is_enumerate      NUMBER(1),
  field_index       NUMBER(4)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_EXPIMP_CONFIG_TABLE
';
 if i=0 then
 execute immediate '
create table SYS_EXPIMP_CONFIG_TABLE
(
  config_table_code VARCHAR2(60),
  config_table_name VARCHAR2(60),
  table_code        VARCHAR2(60),
  config_table_type NUMBER(3),
  where_sql         VARCHAR2(60)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_FIELDMANAGER
';
 if i=0 then
 execute immediate '
create table SYS_FIELDMANAGER
(
  chr_id         VARCHAR2(38) not null,
  field_code     VARCHAR2(42),
  field_name     VARCHAR2(60),
  field_disptype NUMBER(2) default 0,
  default_value  VARCHAR2(42),
  tips           VARCHAR2(100),
  field_valueset VARCHAR2(2000),
  remark         VARCHAR2(200),
  is_system      NUMBER(1) default 0,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  latest_op_user VARCHAR2(42),
  is_deleted     NUMBER default 0 not null,
  source         VARCHAR2(42),
  last_ver       VARCHAR2(30),
  table_code     VARCHAR2(38)
)'
;
end if;

execute immediate 'comment on column SYS_FIELDMANAGER.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column SYS_FIELDMANAGER.field_code
  is ''�ֶ�Ӣ����''';
execute immediate 'comment on column SYS_FIELDMANAGER.field_name
  is ''�ֶ�������''';
execute immediate 'comment on column SYS_FIELDMANAGER.field_disptype
  is ''������¼�ֶ���ʾ����''';
execute immediate 'comment on column SYS_FIELDMANAGER.default_value
  is ''���趨���ֶεĹ̶�Ĭ��ֵ����ҵ������δ�Ը��ֶν����κβ���������ֶα�����Ĭ��ֵ''';
execute immediate 'comment on column SYS_FIELDMANAGER.tips
  is ''����û���һ���������ֶκ��������''';
execute immediate 'comment on column SYS_FIELDMANAGER.field_valueset
  is ''�������ֶε�ȡֵ��Χ����/�ֿ�
 ��code1#name1+code2#name2��
 ''';
execute immediate 'comment on column SYS_FIELDMANAGER.remark
  is ''����Աʹ��''';
execute immediate 'comment on column SYS_FIELDMANAGER.is_system
  is ''�궨���ֶ��Ƿ�ϵͳ�����ֶ�''';

execute immediate 'comment on column SYS_FIELDMANAGER.create_user
  is ''�����û�''';

execute immediate 'comment on column SYS_FIELDMANAGER.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_FIELDMANAGER.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column SYS_FIELDMANAGER.source
  is ''���ΪҪ�����¼Ҫ�ؼ��,�����ö��ֵ���ʾӳ���ֶ���''';
execute immediate 'alter table SYS_FIELDMANAGER
  add constraint SYS_FIELDMANAGER_PK primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_LOG
';
 if i=0 then
 execute immediate '
create table SYS_LOG
(
  log_type       VARCHAR2(3) not null,
  log_typename   VARCHAR2(60),
  is_defalutlog  NUMBER(1) default 1,
  searchlogclass VARCHAR2(100),
  last_ver       VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_LOG.log_type
  is ''��־����:0,�û�����,1���ܲ���,2,������,3,�����־''';
execute immediate 'comment on column SYS_LOG.log_typename
  is ''��־��������''';
execute immediate 'comment on column SYS_LOG.is_defalutlog
  is ''�Ƿ�ʹ��ȱʡ��־ʵ��''';
execute immediate 'comment on column SYS_LOG.searchlogclass
  is ''�Զ���ʵ����־��ѯ��''';
execute immediate 'comment on column SYS_LOG.last_ver
  is ''�汾�ֶ�''';
execute immediate 'alter table SYS_LOG
  add constraint SYS_LOG_PK primary key (LOG_TYPE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_LOGINFO
';
 if i=0 then
 execute immediate '
create table SYS_LOGINFO
(
  log_id           VARCHAR2(38) not null,
  user_id          VARCHAR2(40),
  user_name        VARCHAR2(60),
  user_ip          VARCHAR2(30),
  log_type         VARCHAR2(3),
  log_level        NUMBER(1),
  menu_id          NUMBER,
  module_id        NUMBER,
  action_type_code VARCHAR2(38),
  action_name      VARCHAR2(40),
  sys_id           VARCHAR2(42) default ''001'',
  inspect_info     VARCHAR2(300),
  remark           VARCHAR2(500),
  wf_name          VARCHAR2(60),
  cur_node_name    VARCHAR2(60),
  money            NUMBER(16,2),
  vou_id           VARCHAR2(38),
  oper_time        VARCHAR2(60),
  last_ver         VARCHAR2(30),
  RG_CODE          VARCHAR2(42) not null,
  set_year         NUMBER(4) not null
)'
;
end if;

execute immediate 'alter table SYS_LOGINFO
  add constraint SYS_LOGINFO_PRO3_PK primary key (LOG_ID)';
  
  

select count(*) into i from user_tables where table_name='SYS_LOGINFO_BAK
';
 if i=0 then
 execute immediate '
create table SYS_LOGINFO_BAK
(
  log_id           VARCHAR2(38) not null,
  user_id          VARCHAR2(40),
  user_name        VARCHAR2(60),
  user_ip          VARCHAR2(30),
  log_type         VARCHAR2(3),
  log_level        NUMBER(1),
  menu_id          NUMBER,
  module_id        NUMBER,
  action_type_code VARCHAR2(38),
  action_name      VARCHAR2(40),
  sys_id           VARCHAR2(42),
  inspect_info     VARCHAR2(300),
  remark           VARCHAR2(500),
  wf_name          VARCHAR2(60),
  cur_node_name    VARCHAR2(60),
  money            NUMBER(16,2),
  vou_id           VARCHAR2(38),
  oper_time        VARCHAR2(60),
  last_ver         VARCHAR2(30),
  RG_CODE          VARCHAR2(42) not null,
  set_year         NUMBER(4) not null
)'
;
end if;

execute immediate 'alter table SYS_LOGINFO_BAK
  add constraint SYS_LOGINFO_BAK_PRO3_PK primary key (LOG_ID)';

  
  
select count(*) into i from user_tables where table_name='SYS_MENU
';
 if i=0 then
 execute immediate '
create table SYS_MENU
(
  menu_id                   VARCHAR2(42) not null,
  menu_code                 VARCHAR2(42) not null,
  menu_name                 VARCHAR2(60),
  icon                      VARCHAR2(100),
  enabled                   NUMBER not null,
  level_num                 NUMBER(2) not null,
  is_leaf                   NUMBER not null,
  tips                      VARCHAR2(100),
  disp_order                NUMBER(10),
  user_sys_id               VARCHAR2(42),
  last_ver                  VARCHAR2(30),
  step_shortcut             VARCHAR2(10),
  direct_shortcut_letter    VARCHAR2(10),
  direct_shortcut_function1 NUMBER(1),
  direct_shortcut_function2 NUMBER(1),
  is_offline                NUMBER(1) default 0,
  screentype                VARCHAR2(25),
  parent_id                 VARCHAR2(42),
  menu_parameter            VARCHAR2(225),
  url                       VARCHAR2(225)
)';

end if; 

execute immediate 'comment on column SYS_MENU.user_sys_id is ''�û���ϵͳid''';
execute immediate 'comment on column SYS_MENU.step_shortcut
  is ''׷���ַ��ֶ�''';
execute immediate 'comment on column SYS_MENU.direct_shortcut_letter
  is ''��ݷ�ʽ�ַ�''';
execute immediate 'comment on column SYS_MENU.direct_shortcut_function1
  is ''���̲�����1''';
execute immediate 'comment on column SYS_MENU.direct_shortcut_function2
  is ''���̲�����2''';
execute immediate 'comment on column SYS_MENU.is_offline
  is ''�Ƿ�Ϊ���߲˵� 0 �� 1 �� 2 BOTH ���ǣ��������߹���''';
execute immediate 'comment on column SYS_MENU.screentype
  is ''�˵�����''';
execute immediate 'comment on column SYS_MENU.parent_id
  is ''����id''';
execute immediate 'comment on column SYS_MENU.menu_parameter
  is ''����''';
execute immediate 'comment on column SYS_MENU.url
  is ''��ַ''';
execute immediate 'alter table SYS_MENU
  add constraint SYS_MENU_PK_PRO8 primary key (MENU_ID)
  using index ';

  
select count(*) into i from user_tables where table_name='SYS_MENU_BUTTON
';
 if i=0 then
 execute immediate '
create table SYS_MENU_BUTTON
(
  btn_id   VARCHAR2(38) not null,
  btn_name VARCHAR2(64),
  menu_id  VARCHAR2(38) not null,
  btn_code VARCHAR2(32),
  remark   VARCHAR2(32),
  last_ver DATE
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_MENU_MODULE
';
 if i=0 then
 execute immediate '
create table SYS_MENU_MODULE
(
  menu_id       NUMBER not null,
  module_id     NUMBER not null,
  display_order NUMBER(3) not null,
  display_title VARCHAR2(60) not null,
  last_ver      VARCHAR2(30)
)'
;
end if;

execute immediate 'alter table SYS_MENU_MODULE
  add constraint SYS_MENU_MODULE_PK_PRO7 primary key (MENU_ID, MODULE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_MENU_VIEW
';
 if i=0 then
 execute immediate '
create table SYS_MENU_VIEW
(
  menu_guid   VARCHAR2(38) not null,
  ui_id       VARCHAR2(38) not null,
  disp_order  NUMBER(9) not null,
  remark      VARCHAR2(200),
  last_ver    VARCHAR2(30),
  key_value   VARCHAR2(50),
  RG_CODE     VARCHAR2(42) not null,
  set_year    NUMBER(4),
  menuview_id VARCHAR2(38) not null
)'
;
end if;

execute immediate 'alter table SYS_MENU_VIEW
  add constraint MENU_VIEW_PRIMARY primary key (MENUVIEW_ID)'; 
execute immediate 'alter table SYS_MENU_VIEW
  add constraint MENU_VIEW_UNIQUE unique (UI_ID, MENU_GUID)';

  
  
select count(*) into i from user_tables where table_name='SYS_MESSAGE
';
 if i=0 then
 execute immediate '
create table SYS_MESSAGE
(
  msg_id             VARCHAR2(38) not null,
  msg_title          VARCHAR2(200),
  msg_content        VARCHAR2(3999),
  msg_type_code      NUMBER(1),
  user_id            VARCHAR2(40),
  role_id            NUMBER,
  module_id          NUMBER,
  para_string        VARCHAR2(200),
  send_type          NUMBER(1),
  is_temp            NUMBER(1),
  is_send            NUMBER(1),
  is_receive         NUMBER(1),
  send_num           NUMBER(3),
  last_ver           VARCHAR2(30),
  cancel_relation_id VARCHAR2(38),
  fromuser           VARCHAR2(40),
  sent_time          VARCHAR2(30),
  receive_time       VARCHAR2(30),
  is_relaterole      NUMBER(1),
  attm_id            NVARCHAR2(60)
)'
;
end if;

execute immediate 'alter table SYS_MESSAGE
  add constraint SYS_MESSAGE_PRO3_PK primary key (MSG_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_MESSAGE_ATTACHMENT
';
 if i=0 then
 execute immediate '
create table SYS_MESSAGE_ATTACHMENT
(
  attm_id   NVARCHAR2(60) not null,
  file_name NVARCHAR2(500) not null,
  time_path NVARCHAR2(8) not null
)'
;
end if;

execute immediate 'comment on column SYS_MESSAGE_ATTACHMENT.attm_id
  is ''����ID''';
execute immediate 'comment on column SYS_MESSAGE_ATTACHMENT.file_name
  is ''�����ļ�����''';
execute immediate 'comment on column SYS_MESSAGE_ATTACHMENT.time_path
  is ''������ʱ���·��''';
execute immediate 'alter table SYS_MESSAGE_ATTACHMENT
  add constraint ATTM_ID_PK primary key (ATTM_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_METADATA
';
 if i=0 then
 execute immediate '
create table SYS_METADATA
(
  chr_id         VARCHAR2(38) not null,
  field_code     VARCHAR2(42),
  field_name     VARCHAR2(60),
  field_disptype NUMBER(2) default 0,
  default_value  VARCHAR2(42),
  tips           VARCHAR2(100),
  field_valueset VARCHAR2(2000),
  remark         VARCHAR2(200),
  is_system      NUMBER(1) default 0,
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  latest_op_user VARCHAR2(42),
  is_deleted     NUMBER default 0 not null,
  source         VARCHAR2(42),
  last_ver       VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_METADATA.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column SYS_METADATA.field_code
  is ''�ֶ�Ӣ����''';
execute immediate 'comment on column SYS_METADATA.field_name
  is ''�ֶ�������''';
execute immediate 'comment on column SYS_METADATA.field_disptype
  is ''������¼�ֶ���ʾ����''';
execute immediate 'comment on column SYS_METADATA.default_value
  is ''���趨���ֶεĹ̶�Ĭ��ֵ����ҵ������δ�Ը��ֶν����κβ���������ֶα�����Ĭ��ֵ''';
execute immediate 'comment on column SYS_METADATA.tips
  is ''����û���һ���������ֶκ��������''';
execute immediate 'comment on column SYS_METADATA.field_valueset
  is ''�������ֶε�ȡֵ��Χ����/�ֿ�
 ��code1#name1+code2#name2��
 ''';
execute immediate 'comment on column SYS_METADATA.remark
  is ''����Աʹ��''';
execute immediate 'comment on column SYS_METADATA.is_system
  is ''�궨���ֶ��Ƿ�ϵͳ�����ֶ�''';

execute immediate 'comment on column SYS_METADATA.create_user
  is ''�����û�''';

execute immediate 'comment on column SYS_METADATA.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_METADATA.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column SYS_METADATA.source
  is ''���ΪҪ�����¼Ҫ�ؼ��,�����ö��ֵ���ʾӳ���ֶ���''';
execute immediate 'alter table SYS_METADATA
  add constraint SYS_METADATA_PK primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_MODULE
';
 if i=0 then
 execute immediate '
create table SYS_MODULE
(
  module_id      NUMBER not null,
  module_code    VARCHAR2(42) not null,
  module_name    VARCHAR2(60),
  sys_id         VARCHAR2(42),
  module_type    NUMBER(1) not null,
  enabled        NUMBER(1) not null,
  default_icon   VARCHAR2(100),
  tips           VARCHAR2(100),
  class_name     VARCHAR2(200),
  func_name      VARCHAR2(60),
  param          VARCHAR2(2000),
  bill_type_code VARCHAR2(60),
  last_ver       VARCHAR2(30)
)'
;
end if;

execute immediate 'create index SYS_MODULE_U1_PRO7 on SYS_MODULE (SYS_ID)';
  
execute immediate 'alter table SYS_MODULE
  add constraint SYS_MODULE_PK_PRO7 primary key (MODULE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_MODULE_BAKUP
';
 if i=0 then
 execute immediate '
create table SYS_MODULE_BAKUP
(
  module_id      NUMBER not null,
  module_code    VARCHAR2(42) not null,
  module_name    VARCHAR2(60),
  sys_id         VARCHAR2(42),
  module_type    NUMBER(1) not null,
  enabled        NUMBER(1) not null,
  default_icon   VARCHAR2(100),
  tips           VARCHAR2(100),
  class_name     VARCHAR2(200),
  func_name      VARCHAR2(60),
  param          VARCHAR2(2000),
  bill_type_code VARCHAR2(60),
  last_ver       VARCHAR2(30)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_MODULE_STATUS
';
 if i=0 then
 execute immediate '
create table SYS_MODULE_STATUS
(
  module_id     NUMBER not null,
  status_id     VARCHAR2(42) not null,
  display_order NUMBER(3) not null,
  display_title VARCHAR2(60) not null,
  last_ver      VARCHAR2(30)
)'
;
end if;

execute immediate 'alter table SYS_MODULE_STATUS
  add constraint SYS_MODULE_STATUS_PK_PRO7 primary key (MODULE_ID, STATUS_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_MODULE_STATUS_BUTTON
';
 if i=0 then
 execute immediate '
create table SYS_MODULE_STATUS_BUTTON
(
  module_id NUMBER not null,
  status_id VARCHAR2(38) not null,
  button_id VARCHAR2(38) not null,
  last_ver  VARCHAR2(30)
)'
;
end if;

execute immediate 'alter table SYS_MODULE_STATUS_BUTTON
  add constraint SYS_MODULE_STATUS_PRO7 primary key (MODULE_ID, STATUS_ID, BUTTON_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_MODULE_VIEW
';
 if i=0 then
 execute immediate '
create table SYS_MODULE_VIEW
(
  module_id  NUMBER not null,
  ui_id      VARCHAR2(38) not null,
  disp_order NUMBER(9) not null,
  remark     VARCHAR2(200),
  last_ver   VARCHAR2(30),
  key_value  VARCHAR2(50),
  RG_CODE    VARCHAR2(42) not null,
  set_year   NUMBER(4)
)'
;
end if;

execute immediate 'alter table SYS_MODULE_VIEW
  add constraint SYS_MODULE_VIEW_PK_PRO7 primary key (MODULE_ID, UI_ID, DISP_ORDER)';
  
  
	
select count(*) into i from user_tables where table_name='SYS_NAVIGATE
';
 if i=0 then
 execute immediate '
create table SYS_NAVIGATE
(
  navigate_id        VARCHAR2(38) not null,
  navigate_code      VARCHAR2(42),
  navigate_name      VARCHAR2(60),
  level_num          NUMBER(2) not null,
  is_leaf            NUMBER not null,
  disp_order         NUMBER(10),
  navigate_icon      VARCHAR2(100),
  role_id            NUMBER,
  tips               VARCHAR2(100),
  menu_id            NUMBER,
  parent_navigate_id VARCHAR2(38),
  last_ver           VARCHAR2(30)
)'
;
end if;

execute immediate 'alter table SYS_NAVIGATE
  add constraint SYS_NAVIGATE_PK_PRO8 primary key (NAVIGATE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_ORGTYPE
';
 if i=0 then
 execute immediate '
create table SYS_ORGTYPE
(
  orgtype_code VARCHAR2(42) not null,
  orgtype_name VARCHAR2(60),
  ele_code     VARCHAR2(42),
  last_ver     VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_ORGTYPE.orgtype_code
  is ''�������ͱ���''';
execute immediate 'comment on column SYS_ORGTYPE.orgtype_name
  is ''������������''';
execute immediate 'comment on column SYS_ORGTYPE.ele_code
  is ''��ӦҪ�ر���''';
execute immediate 'alter table SYS_ORGTYPE
  add constraint SYS_ORGTYPE_PK primary key (ORGTYPE_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_PLUGS_STATE
';
 if i=0 then
 execute immediate '
create table SYS_PLUGS_STATE
(
  plugin_id    VARCHAR2(100),
  pro_id       VARCHAR2(10),
  state        VARCHAR2(32),
  last_operate VARCHAR2(32)
)'
;
end if;

execute immediate 'comment on table SYS_PLUGS_STATE
  is ''�������״̬''';
execute immediate 'comment on column SYS_PLUGS_STATE.plugin_id
  is ''���ID''';
execute immediate 'comment on column SYS_PLUGS_STATE.pro_id
  is ''Ʒ��ID''';
execute immediate 'comment on column SYS_PLUGS_STATE.state
  is ''����״̬1 ������0 δ����''';
execute immediate 'comment on column SYS_PLUGS_STATE.last_operate
  is ''������ʱ��''';
  
  
  
select count(*) into i from user_tables where table_name='SYS_PRINT_MODELS
';
 if i=0 then
 execute immediate '
create table SYS_PRINT_MODELS
(
  billtype_report_id VARCHAR2(38) not null,
  billtype_id        VARCHAR2(38) not null,
  report_id          VARCHAR2(42) not null,
  arg_list           VARCHAR2(255),
  para_list          VARCHAR2(255),
  display_order      NUMBER(2),
  enabled            NUMBER(1) default 1,
  is_default         NUMBER(1) default 0,
  create_date        VARCHAR2(30),
  latest_op_date     VARCHAR2(30) not null,
  latest_op_user     VARCHAR2(30) not null,
  set_year           NUMBER(4) not null,
  RG_CODE            VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_PRINT_MODELS.billtype_report_id
  is ''id������''';
execute immediate 'comment on column SYS_PRINT_MODELS.billtype_id
  is ''����ƾ֤����ID''';
execute immediate 'comment on column SYS_PRINT_MODELS.report_id
  is ''����ģ��ID''';
execute immediate 'comment on column SYS_PRINT_MODELS.arg_list
  is ''�������''';
execute immediate 'comment on column SYS_PRINT_MODELS.para_list
  is ''��������''';
execute immediate 'comment on column SYS_PRINT_MODELS.display_order
  is ''��ʾ˳��''';
execute immediate 'comment on column SYS_PRINT_MODELS.enabled
  is ''���ã�1������ 0��������''';
execute immediate 'comment on column SYS_PRINT_MODELS.is_default
  is ''�Ƿ�Ĭ�ϣ�1��Ĭ��0����Ĭ��''';
execute immediate 'comment on column SYS_PRINT_MODELS.create_date
  is ''����ʱ��''';
execute immediate 'comment on column SYS_PRINT_MODELS.latest_op_date
  is ''����޸�ʱ��''';
execute immediate 'comment on column SYS_PRINT_MODELS.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_PRINT_MODELS.set_year
  is ''ҵ�����''';
execute immediate 'alter table SYS_PRINT_MODELS
  add constraint PK_PRINT_MODELS primary key (BILLTYPE_REPORT_ID, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_QUERY_CONDITION
';
 if i=0 then
 execute immediate '
create table SYS_QUERY_CONDITION
(
  condition_name       VARCHAR2(60) not null,
  condition_value      VARCHAR2(1000),
  condition_disp_value VARCHAR2(1000),
  last_ver             VARCHAR2(30),
  user_id              VARCHAR2(40) not null,
  ui_code              VARCHAR2(60),
  is_public            NUMBER(1)
)'
;
end if;

execute immediate 'comment on column SYS_QUERY_CONDITION.condition_name
  is ''��������''';
execute immediate 'comment on column SYS_QUERY_CONDITION.condition_value
  is ''����ֵ''';
execute immediate 'comment on column SYS_QUERY_CONDITION.condition_disp_value
  is ''������ʾֵ''';
execute immediate 'comment on column SYS_QUERY_CONDITION.last_ver
  is ''����ʱ��''';
execute immediate 'comment on column SYS_QUERY_CONDITION.user_id
  is ''�û�Id''';
execute immediate 'comment on column SYS_QUERY_CONDITION.ui_code
  is ''��ͼ���''';
execute immediate 'comment on column SYS_QUERY_CONDITION.is_public
  is ''�Ƿ���''';
  
  
  
select count(*) into i from user_tables where table_name='SYS_REGION_APP
';
 if i=0 then
 execute immediate '
create table SYS_REGION_APP
(
  RG_CODE  VARCHAR2(42) not null,
  sys_id   VARCHAR2(42) not null,
  is_valid NUMBER(1) default 0 not null
)'
;
end if;

execute immediate 'comment on column SYS_REGION_APP.RG_CODE
  is ''������chr_code''';
execute immediate 'comment on column SYS_REGION_APP.sys_id
  is ''Ӧ��ϵͳ��sys_id''';
execute immediate 'comment on column SYS_REGION_APP.is_valid
  is ''�Ƿ�����ϵͳ,0:δ���� 1:������ 2:����ʧ��''';
execute immediate 'alter table SYS_REGION_APP
  add constraint SYS_REGION_APP_PK primary key (RG_CODE, SYS_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RELATIONS
';
 if i=0 then
 execute immediate '
create table SYS_RELATIONS
(
  relation_detail_id VARCHAR2(38) not null,
  relation_id        VARCHAR2(38) not null,
  pri_ele_value      VARCHAR2(42),
  sec_ele_value      VARCHAR2(42),
  set_year           NUMBER(4) not null,
  is_deleted         NUMBER default 0 not null,
  last_ver           VARCHAR2(30),
  RG_CODE            VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_RELATIONS.relation_detail_id
  is ''������ϵ��ϸΨһID''';
execute immediate 'comment on column SYS_RELATIONS.relation_id
  is ''������ϵΨһID''';
execute immediate 'comment on column SYS_RELATIONS.pri_ele_value
  is ''����Ҫ������''';
execute immediate 'comment on column SYS_RELATIONS.sec_ele_value
  is ''������Ҫ������''';
execute immediate 'comment on column SYS_RELATIONS.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_RELATIONS.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'create index IDX_SYS_RELATIONS on SYS_RELATIONS (RELATION_ID, PRI_ELE_VALUE)
 ' ;
execute immediate 'create index SYS_RELATIONS_N1 on SYS_RELATIONS (SET_YEAR)
  ';
execute immediate 'create index SYS_RELATIONS_N2 on SYS_RELATIONS (RELATION_ID)
 ' ;
execute immediate 'alter table SYS_RELATIONS
  add constraint SYS_RELATIONS_PK primary key (RELATION_DETAIL_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RELATION_MANAGER
';
 if i=0 then
 execute immediate '
create table SYS_RELATION_MANAGER
(
  relation_id    VARCHAR2(38) not null,
  relation_code  VARCHAR2(42) not null,
  pri_ele_code   VARCHAR2(42),
  sec_ele_code   VARCHAR2(42),
  set_year       NUMBER(4),
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30) not null,
  latest_op_user VARCHAR2(42),
  is_deleted     NUMBER default 0 not null,
  last_ver       VARCHAR2(30),
  sys_id         VARCHAR2(42),
  relation_type  NUMBER,
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_RELATION_MANAGER.relation_id
  is ''������ϵΨһID''';
execute immediate 'comment on column SYS_RELATION_MANAGER.relation_code
  is ''������ϵ����''';
execute immediate 'comment on column SYS_RELATION_MANAGER.pri_ele_code
  is ''����Ҫ�ر���''';
execute immediate 'comment on column SYS_RELATION_MANAGER.sec_ele_code
  is ''������Ҫ�ر���''';
execute immediate 'comment on column SYS_RELATION_MANAGER.set_year
  is ''ҵ�����''';

execute immediate 'comment on column SYS_RELATION_MANAGER.create_user
  is ''�����û�''';

execute immediate 'comment on column SYS_RELATION_MANAGER.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_RELATION_MANAGER.is_deleted
  is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column SYS_RELATION_MANAGER.sys_id
  is ''Ӧ��ģ��ID''';
execute immediate 'create index SYS_RELATION_MANAGER_N1 on SYS_RELATION_MANAGER (SET_YEAR)
  ';
execute immediate 'alter table SYS_RELATION_MANAGER
  add constraint SYS_RELATION_MANAGER_PK primary key (RELATION_ID)';
  
  

select count(*) into i from user_tables where table_name='SYS_REPORTPARAM
';
 if i=0 then
 execute immediate '
create table SYS_REPORTPARAM
(
  chr_id   VARCHAR2(38) not null,
  chr_name VARCHAR2(38) not null,
  chr_code VARCHAR2(42),
  last_ver VARCHAR2(30)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_REPORT_USER_RIGHTS_INFO
';
 if i=0 then
 execute immediate '
create table SYS_REPORT_USER_RIGHTS_INFO
(
  user_code       VARCHAR2(100),
  user_name       VARCHAR2(100),
  role_code       VARCHAR2(100),
  role_name       VARCHAR2(100),
  menu_rights     VARCHAR2(4000),
  workflow_rights VARCHAR2(4000),
  data_rights     VARCHAR2(4000),
  query_id        VARCHAR2(100)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_RIGHT_COMBINATION
';
 if i=0 then
 execute immediate '
create table SYS_RIGHT_COMBINATION
(
  rcid          NUMBER not null,
  as_id         VARCHAR2(38),
  AGENCY_ID         VARCHAR2(38),
  fundtype_id   VARCHAR2(38),
  expfunc_id    VARCHAR2(38),
  agencyexp_id  VARCHAR2(38),
  expeco_id     VARCHAR2(38),
  paytype_id    VARCHAR2(38),
  BGTTYPE_ID         VARCHAR2(38),
  paykind_id    VARCHAR2(38),
  MB_ID  VARCHAR2(38),
  file_id       VARCHAR2(38),
  setmode_id    VARCHAR2(38),
  in_expfunc_id VARCHAR2(38),
  in_bis_id     VARCHAR2(38),
  cb_id         VARCHAR2(38),
  pb_id         VARCHAR2(38),
  ib_id         VARCHAR2(38),
  bac_id        VARCHAR2(38),
  bap_id        VARCHAR2(38),
  bai_id        VARCHAR2(38),
  bgtdir_id     VARCHAR2(38),
  bp_id         VARCHAR2(38),
  BGTSOURCE_ID  VARCHAR2(38),
  hold1_id      VARCHAR2(38),
  hold2_id      VARCHAR2(38),
  hold3_id      VARCHAR2(38),
  hold4_id      VARCHAR2(38),
  hold5_id      VARCHAR2(38),
  hold6_id      VARCHAR2(38),
  hold7_id      VARCHAR2(38),
  hold8_id      VARCHAR2(38),
  hold9_id      VARCHAR2(38),
  hold10_id     VARCHAR2(38),
  hold11_id     VARCHAR2(38),
  hold12_id     VARCHAR2(38),
  hold13_id     VARCHAR2(38),
  hold14_id     VARCHAR2(38),
  hold15_id     VARCHAR2(38),
  hold16_id     VARCHAR2(38),
  hold17_id     VARCHAR2(38),
  hold18_id     VARCHAR2(38),
  hold19_id     VARCHAR2(38),
  hold20_id     VARCHAR2(38),
  hold21_id     VARCHAR2(38),
  hold22_id     VARCHAR2(38),
  hold23_id     VARCHAR2(38),
  hold24_id     VARCHAR2(38),
  hold25_id     VARCHAR2(38),
  hold26_id     VARCHAR2(38),
  hold27_id     VARCHAR2(38),
  hold28_id     VARCHAR2(38),
  hold29_id     VARCHAR2(38),
  hold30_id     VARCHAR2(38),
  set_year      NUMBER(4) not null,
  inpm_id       VARCHAR2(38),
  bis_id        VARCHAR2(38),
  st_id         VARCHAR2(38),
  ct_id         VARCHAR2(38),
  sm_id         VARCHAR2(38),
  last_ver      VARCHAR2(30),
  ZFCGBS_ID         VARCHAR2(38),
  pub_id        VARCHAR2(38),
  GZBS_ID     VARCHAR2(38),
  update_flag   NUMBER,
  ien_id        VARCHAR2(42),
  tbv_id        VARCHAR2(38),
  RG_CODE       VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index IDX_SYS_RIGHT_COMBINATION_PRO1 on SYS_RIGHT_COMBINATION (SET_YEAR, RCID)
 ' ;
execute immediate 'create index SYS_RIGHT_COMBINATION_PRO1_PK on SYS_RIGHT_COMBINATION (RCID)
 ' ;
execute immediate 'create index SYS_RIGHT_COMBINATION_PRO1_PK1 on SYS_RIGHT_COMBINATION (AGENCY_ID)
  ';
execute immediate 'create index SYS_RIGHT_COMBINATION_PRO1_PK2 on SYS_RIGHT_COMBINATION (FUNDTYPE_ID)
 ' ;
execute immediate 'create index SYS_RIGHT_COMBINATION_PRO1_PK3 on SYS_RIGHT_COMBINATION (EXPFUNC_ID)
 ' ;
execute immediate 'create index SYS_RIGHT_COMBINATION_PRO1_PK4 on SYS_RIGHT_COMBINATION (PAYTYPE_ID)
 ';
execute immediate 'create index SYS_RIGHT_COMBINATION_PRO1_PK5 on SYS_RIGHT_COMBINATION (MB_ID)
 ' ;
execute immediate 'alter table SYS_RIGHT_COMBINATION
  add constraint SYS_RIGHT_COMBINATION_PRO_PK primary key (RCID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RIGHT_GROUP
';
 if i=0 then
 execute immediate '
create table SYS_RIGHT_GROUP
(
  right_group_id       VARCHAR2(38) not null,
  right_group_describe VARCHAR2(10),
  right_type           NUMBER(1),
  rule_id              NUMBER,
  last_ver             VARCHAR2(30),
  set_year             NUMBER(4) not null,
  RG_CODE              VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index SYS_RIGHT_GROUP_N1_PRO on SYS_RIGHT_GROUP (RULE_ID)';
  execute immediate 'alter table SYS_RIGHT_GROUP
  add constraint SYS_RIGHT_GROUP_PRO_PK primary key (RIGHT_GROUP_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RIGHT_GROUP_CACHE
';
 if i=0 then
 execute immediate '
create table SYS_RIGHT_GROUP_CACHE
(
  right_group_id       VARCHAR2(38) not null,
  right_group_describe VARCHAR2(10),
  right_type           NUMBER(1),
  rule_id              NUMBER,
  last_ver             VARCHAR2(30),
  set_year             NUMBER(4) not null,
  RG_CODE              VARCHAR2(42) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_RIGHT_GROUP_DETAIL
';
 if i=0 then
 execute immediate '
create table SYS_RIGHT_GROUP_DETAIL
(
  right_group_id VARCHAR2(38) not null,
  ele_code       VARCHAR2(42) not null,
  ele_value      VARCHAR2(38) not null,
  set_year       NUMBER(4) not null,
  last_ver       VARCHAR2(30),
  ele_name       VARCHAR2(255),
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index SYS_RIGHT_GROUP_DETAIL_N1 on SYS_RIGHT_GROUP_DETAIL (SET_YEAR)';
  
execute immediate 'alter table SYS_RIGHT_GROUP_DETAIL
  add constraint SYS_RIGHT_GROUP_DETAIL_PK primary key (RIGHT_GROUP_ID, ELE_CODE, ELE_VALUE, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RIGHT_GROUP_DETAIL_CACHE
';
 if i=0 then
 execute immediate '
create global temporary table SYS_RIGHT_GROUP_DETAIL_CACHE
(
  right_group_id VARCHAR2(38) not null,
  ele_code       VARCHAR2(42) not null,
  ele_value      VARCHAR2(38) not null,
  set_year       NUMBER(4) not null,
  last_ver       VARCHAR2(30),
  RG_CODE        VARCHAR2(42)
)';
end if;



select count(*) into i from user_tables where table_name='SYS_RIGHT_GROUP_TYPE
';
 if i=0 then
 execute immediate '
create table SYS_RIGHT_GROUP_TYPE
(
  right_group_id VARCHAR2(38) not null,
  ele_code       VARCHAR2(42) not null,
  right_type     NUMBER(1),
  set_year       NUMBER(4) not null,
  last_ver       VARCHAR2(30),
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_RIGHT_GROUP_TYPE
  add constraint SYS_RIGHT_GROUP_TYPE_PK primary key (RIGHT_GROUP_ID, ELE_CODE, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RIGHT_GROUP_TYPE_CACHE
';
 if i=0 then
 execute immediate '
create global temporary table SYS_RIGHT_GROUP_TYPE_CACHE
(
  right_group_id VARCHAR2(38) not null,
  ele_code       VARCHAR2(42) not null,
  right_type     NUMBER(1),
  set_year       NUMBER(4),
  last_ver       VARCHAR2(30),
  RG_CODE        VARCHAR2(42)
)';
end if;



select count(*) into i from user_tables where table_name='SYS_ROLE
';
 if i=0 then
 execute immediate '
create table SYS_ROLE
(
  role_id     VARCHAR2(42) not null,
  role_code   VARCHAR2(42) not null,
  role_name   VARCHAR2(60),
  user_sys_id VARCHAR2(42),
  enabled     NUMBER(1),
  role_type   VARCHAR2(3),
  last_ver    VARCHAR2(30),
  RG_CODE     VARCHAR2(42),
  set_year    NUMBER(4)
)'
;
end if;

execute immediate 'alter table SYS_ROLE
  add constraint SYS_ROLE_PK_PRO5 primary key (ROLE_ID)';
  
  

select count(*) into i from user_tables where table_name='SYS_ROLE_MENU
';
 if i=0 then
 execute immediate '
create table SYS_ROLE_MENU
(
  role_id  VARCHAR2(42) not null,
  menu_id  VARCHAR2(42) not null,
  set_year NUMBER(4) not null,
  last_ver VARCHAR2(30),
  RG_CODE  VARCHAR2(42) not null
)'
;
end if;
execute immediate 'create index SYS_ROLE_MENU_N1_PRO8 on SYS_ROLE_MENU (SET_YEAR)';
  
execute immediate 'alter table SYS_ROLE_MENU
  add constraint SYS_ROLE_MENU_PK_PRO8 primary key (ROLE_ID, MENU_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_ROLE_MENU_BUTTON
';
 if i=0 then
 execute immediate '
create table SYS_ROLE_MENU_BUTTON
(
  role_id   NUMBER not null,
  button_id VARCHAR2(38) not null,
  set_year  NUMBER(4) not null,
  menu_id   NUMBER not null,
  last_ver  VARCHAR2(30),
  RG_CODE   VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index SYS_ROLE_MENU_BUTTON_N1_PRO8 on SYS_ROLE_MENU_BUTTON (SET_YEAR)'
  ;
execute immediate 'alter table SYS_ROLE_MENU_BUTTON
  add constraint SYS_ROLE_MENU_BUTTON_PK_PRO8 primary key (ROLE_ID, BUTTON_ID, MENU_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_ROLE_MENU_MODULE
';
 if i=0 then
 execute immediate '
create table SYS_ROLE_MENU_MODULE
(
  role_id   NUMBER not null,
  menu_id   NUMBER not null,
  module_id NUMBER not null,
  set_year  NUMBER(4) not null,
  last_ver  VARCHAR2(30),
  RG_CODE   VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index SYS_ROLE_MENU_MODULE_PRO7 on SYS_ROLE_MENU_MODULE (SET_YEAR)';
  
execute immediate 'alter table SYS_ROLE_MENU_MODULE
  add constraint SYS_ROLE_MENU_MODULE_PK_PRO7 primary key (MENU_ID, MODULE_ID, ROLE_ID, SET_YEAR, RG_CODE)';
  
  

select count(*) into i from user_tables where table_name='SYS_ROLE_MODULE
';
 if i=0 then
 execute immediate '
create table SYS_ROLE_MODULE
(
  role_id   NUMBER not null,
  module_id NUMBER not null,
  set_year  NUMBER(4) not null,
  last_ver  VARCHAR2(30),
  RG_CODE   VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index SYS_ROLE_MODULE_N1_PRO7 on SYS_ROLE_MODULE (SET_YEAR)';
  
execute immediate 'alter table SYS_ROLE_MODULE
  add constraint SYS_ROLE_MODULE_PK_PRO7 primary key (MODULE_ID, ROLE_ID, SET_YEAR, RG_CODE)';

  
  
select count(*) into i from user_tables where table_name='SYS_ROLE_RULE
';
 if i=0 then
 execute immediate '
create table SYS_ROLE_RULE
(
  role_id  VARCHAR2(38) not null,
  rule_id  VARCHAR2(38) not null,
  last_ver VARCHAR2(30),
  set_year NUMBER(4) not null,
  RG_CODE  VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_ROLE_RULE
  add constraint SYS_ROLE_RULE_PK primary key (ROLE_ID, RULE_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RULE
';
 if i=0 then
 execute immediate '
create table SYS_RULE
(
  rule_id        NUMBER not null,
  rule_code      VARCHAR2(42) not null,
  rule_name      VARCHAR2(60),
  remark         VARCHAR2(200),
  set_year       NUMBER(4) not null,
  enabled        NUMBER(1),
  rule_classify  VARCHAR2(10),
  sys_remark     VARCHAR2(200),
  right_type     NUMBER(1),
  create_user    VARCHAR2(42),
  create_date    VARCHAR2(30),
  latest_op_user VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30),
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_RULE
  add constraint SYS_RULE_PRO_PK primary key (RULE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RULE_CACHE
';
 if i=0 then
 execute immediate '
create table SYS_RULE_CACHE
(
  rule_id        NUMBER,
  rule_code      VARCHAR2(42) not null,
  rule_name      VARCHAR2(60),
  remark         VARCHAR2(200),
  set_year       NUMBER(4) not null,
  enabled        NUMBER(1),
  rule_classify  VARCHAR2(10),
  sys_remark     VARCHAR2(200),
  right_type     NUMBER(1),
  create_user    VARCHAR2(42),
  create_date    VARCHAR2(30),
  latest_op_user VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30),
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_RULE_CROSS_JOIN_ADD
';
 if i=0 then
 execute immediate '
create table SYS_RULE_CROSS_JOIN_ADD
(
  rule_id          NUMBER,
  as_id            VARCHAR2(38),
  AGENCY_ID            VARCHAR2(38),
  bs_id            VARCHAR2(38),
  fundtype_id      VARCHAR2(38),
  expfunc_id       VARCHAR2(38),
  agencyexp_id     VARCHAR2(38),
  expeco_id        VARCHAR2(38),
  paytype_id       VARCHAR2(38),
  in_bs_id         VARCHAR2(38),
  BGTTYPE_ID            VARCHAR2(38),
  paykind_id       VARCHAR2(38),
  MB_ID     VARCHAR2(38),
  file_id          VARCHAR2(38),
  setmode_id       VARCHAR2(38),
  in_expfunc_id    VARCHAR2(38),
  in_bis_id        VARCHAR2(38),
  cb_id            VARCHAR2(38),
  pb_id            VARCHAR2(38),
  ib_id            VARCHAR2(38),
  bac_id           VARCHAR2(38),
  bap_id           VARCHAR2(38),
  bai_id           VARCHAR2(38),
  bgtdir_id        VARCHAR2(38),
  bp_id            VARCHAR2(38),
  BGTSOURCE_ID     VARCHAR2(38),
  hold1_id         VARCHAR2(38),
  hold2_id         VARCHAR2(38),
  hold3_id         VARCHAR2(38),
  hold4_id         VARCHAR2(38),
  hold5_id         VARCHAR2(38),
  hold6_id         VARCHAR2(38),
  hold7_id         VARCHAR2(38),
  hold8_id         VARCHAR2(38),
  hold9_id         VARCHAR2(38),
  hold10_id        VARCHAR2(38),
  hold11_id        VARCHAR2(38),
  hold12_id        VARCHAR2(38),
  hold13_id        VARCHAR2(38),
  hold14_id        VARCHAR2(38),
  hold15_id        VARCHAR2(38),
  hold16_id        VARCHAR2(38),
  hold17_id        VARCHAR2(38),
  hold18_id        VARCHAR2(38),
  hold19_id        VARCHAR2(38),
  hold20_id        VARCHAR2(38),
  hold21_id        VARCHAR2(38),
  hold22_id        VARCHAR2(38),
  hold23_id        VARCHAR2(38),
  hold24_id        VARCHAR2(38),
  hold25_id        VARCHAR2(38),
  hold26_id        VARCHAR2(38),
  hold27_id        VARCHAR2(38),
  hold28_id        VARCHAR2(38),
  hold29_id        VARCHAR2(38),
  hold30_id        VARCHAR2(38),
  bis_id           VARCHAR2(38),
  inpm_id          VARCHAR2(38),
  st_id            VARCHAR2(38),
  ct_id            VARCHAR2(38),
  sm_id            VARCHAR2(38),
  editor_id        VARCHAR2(38),
  ba_id            VARCHAR2(38),
  pa_id            VARCHAR2(38),
  last_ver         VARCHAR2(30),
  bc_id            VARCHAR2(38),
  ZFCGBS_ID            VARCHAR2(38),
  pub_id           VARCHAR2(38),
  GZBS_ID        VARCHAR2(38),
  ien_id           VARCHAR2(42),
  pf_id            VARCHAR2(38),
  payee_account_id VARCHAR2(38),
  agent_account_id VARCHAR2(38),
  tbv_id           VARCHAR2(38)
)';
end if;

execute immediate 'create index SYS_JOIN_ADD_PRO_PK1 on SYS_RULE_CROSS_JOIN_ADD (RULE_ID)
  ';
execute immediate 'create index SYS_JOIN_ADD_PRO_PK2 on SYS_RULE_CROSS_JOIN_ADD (AGENCY_ID)
 ' ;
execute immediate 'create index SYS_JOIN_ADD_PRO_PK3 on SYS_RULE_CROSS_JOIN_ADD (FUNDTYPE_ID)
 ' ;
execute immediate 'create index SYS_JOIN_ADD_PRO_PK4 on SYS_RULE_CROSS_JOIN_ADD (EXPFUNC_ID)
 ' ;
execute immediate 'create index SYS_JOIN_ADD_PRO_PK5 on SYS_RULE_CROSS_JOIN_ADD (PAYTYPE_ID)
  ';
execute immediate 'create index SYS_JOIN_ADD_PRO_PK6 on SYS_RULE_CROSS_JOIN_ADD (MB_ID)
 ' ;
 
 
 
select count(*) into i from user_tables where table_name='SYS_RULE_CROSS_JOIN_CACHE
';
 if i=0 then
 execute immediate '
create table SYS_RULE_CROSS_JOIN_CACHE
(
  rule_id          NUMBER,
  as_id            VARCHAR2(38),
  AGENCY_ID            VARCHAR2(38),
  fundtype_id      VARCHAR2(38),
  expfunc_id       VARCHAR2(38),
  agencyexp_id     VARCHAR2(38),
  expeco_id        VARCHAR2(38),
  paytype_id       VARCHAR2(38),
  BGTTYPE_ID            VARCHAR2(38),
  paykind_id       VARCHAR2(38),
  MB_ID     VARCHAR2(38),
  file_id          VARCHAR2(38),
  setmode_id       VARCHAR2(38),
  in_expfunc_id    VARCHAR2(38),
  in_bis_id        VARCHAR2(38),
  cb_id            VARCHAR2(38),
  pb_id            VARCHAR2(38),
  ib_id            VARCHAR2(38),
  bac_id           VARCHAR2(38),
  bap_id           VARCHAR2(38),
  bai_id           VARCHAR2(38),
  bgtdir_id        VARCHAR2(38),
  bp_id            VARCHAR2(38),
  BGTSOURCE_ID     VARCHAR2(38),
  hold1_id         VARCHAR2(38),
  hold2_id         VARCHAR2(38),
  hold3_id         VARCHAR2(38),
  hold4_id         VARCHAR2(38),
  hold5_id         VARCHAR2(38),
  hold6_id         VARCHAR2(38),
  hold7_id         VARCHAR2(38),
  hold8_id         VARCHAR2(38),
  hold9_id         VARCHAR2(38),
  hold10_id        VARCHAR2(38),
  hold11_id        VARCHAR2(38),
  hold12_id        VARCHAR2(38),
  hold13_id        VARCHAR2(38),
  hold14_id        VARCHAR2(38),
  hold15_id        VARCHAR2(38),
  hold16_id        VARCHAR2(38),
  hold17_id        VARCHAR2(38),
  hold18_id        VARCHAR2(38),
  hold19_id        VARCHAR2(38),
  hold20_id        VARCHAR2(38),
  hold21_id        VARCHAR2(38),
  hold22_id        VARCHAR2(38),
  hold23_id        VARCHAR2(38),
  hold24_id        VARCHAR2(38),
  hold25_id        VARCHAR2(38),
  hold26_id        VARCHAR2(38),
  hold27_id        VARCHAR2(38),
  hold28_id        VARCHAR2(38),
  hold29_id        VARCHAR2(38),
  hold30_id        VARCHAR2(38),
  bis_id           VARCHAR2(38),
  inpm_id          VARCHAR2(38),
  st_id            VARCHAR2(38),
  ct_id            VARCHAR2(38),
  sm_id            VARCHAR2(38),
  editor_id        VARCHAR2(38),
  ba_id            VARCHAR2(38),
  pa_id            VARCHAR2(38),
  last_ver         VARCHAR2(30),
  bc_id            VARCHAR2(38),
  ZFCGBS_ID            VARCHAR2(38),
  pub_id           VARCHAR2(38),
  GZBS_ID        VARCHAR2(38),
  pf_id            VARCHAR2(38),
  payee_account_id VARCHAR2(38),
  agent_account_id VARCHAR2(38),
  tbv_id           VARCHAR2(38)
)'
;
end if;

execute immediate 'create index SYS_PRO_CROSS_JOIN_CACHE_PK1 on SYS_RULE_CROSS_JOIN_CACHE (RULE_ID)
 ' ;
execute immediate 'create index SYS_PRO_CROSS_JOIN_CACHE_PK2 on SYS_RULE_CROSS_JOIN_CACHE (AGENCY_ID)
 ' ;
execute immediate 'create index SYS_PRO_CROSS_JOIN_CACHE_PK3 on SYS_RULE_CROSS_JOIN_CACHE (FUNDTYPE_ID)
 ' ;
execute immediate 'create index SYS_PRO_CROSS_JOIN_CACHE_PK4 on SYS_RULE_CROSS_JOIN_CACHE (EXPFUNC_ID)
 ' ;
execute immediate 'create index SYS_PRO_CROSS_JOIN_CACHE_PK5 on SYS_RULE_CROSS_JOIN_CACHE (PAYTYPE_ID)
 ' ;
execute immediate 'create index SYS_PRO_CROSS_JOIN_CACHE_PK6 on SYS_RULE_CROSS_JOIN_CACHE (MB_ID)
 ' ;

 
 
select count(*) into i from user_tables where table_name='SYS_RULE_CROSS_JOIN_DEL
';
 if i=0 then
 execute immediate '
 create table SYS_RULE_CROSS_JOIN_DEL
(
  rule_id          NUMBER,
  as_id            VARCHAR2(38),
  AGENCY_ID            VARCHAR2(38),
  fundtype_id      VARCHAR2(38),
  expfunc_id       VARCHAR2(38),
  agencyexp_id     VARCHAR2(38),
  expeco_id        VARCHAR2(38),
  paytype_id       VARCHAR2(38),
  BGTTYPE_ID            VARCHAR2(38),
  paykind_id       VARCHAR2(38),
  MB_ID     VARCHAR2(38),
  file_id          VARCHAR2(38),
  setmode_id       VARCHAR2(38),
  in_expfunc_id    VARCHAR2(38),
  in_bis_id        VARCHAR2(38),
  cb_id            VARCHAR2(38),
  pb_id            VARCHAR2(38),
  ib_id            VARCHAR2(38),
  bac_id           VARCHAR2(38),
  bap_id           VARCHAR2(38),
  bai_id           VARCHAR2(38),
  bgtdir_id        VARCHAR2(38),
  bp_id            VARCHAR2(38),
  BGTSOURCE_ID     VARCHAR2(38),
  hold1_id         VARCHAR2(38),
  hold2_id         VARCHAR2(38),
  hold3_id         VARCHAR2(38),
  hold4_id         VARCHAR2(38),
  hold5_id         VARCHAR2(38),
  hold6_id         VARCHAR2(38),
  hold7_id         VARCHAR2(38),
  hold8_id         VARCHAR2(38),
  hold9_id         VARCHAR2(38),
  hold10_id        VARCHAR2(38),
  hold11_id        VARCHAR2(38),
  hold12_id        VARCHAR2(38),
  hold13_id        VARCHAR2(38),
  hold14_id        VARCHAR2(38),
  hold15_id        VARCHAR2(38),
  hold16_id        VARCHAR2(38),
  hold17_id        VARCHAR2(38),
  hold18_id        VARCHAR2(38),
  hold19_id        VARCHAR2(38),
  hold20_id        VARCHAR2(38),
  hold21_id        VARCHAR2(38),
  hold22_id        VARCHAR2(38),
  hold23_id        VARCHAR2(38),
  hold24_id        VARCHAR2(38),
  hold25_id        VARCHAR2(38),
  hold26_id        VARCHAR2(38),
  hold27_id        VARCHAR2(38),
  hold28_id        VARCHAR2(38),
  hold29_id        VARCHAR2(38),
  hold30_id        VARCHAR2(38),
  bis_id           VARCHAR2(38),
  inpm_id          VARCHAR2(38),
  st_id            VARCHAR2(38),
  ct_id            VARCHAR2(38),
  sm_id            VARCHAR2(38),
  editor_id        VARCHAR2(38),
  ba_id            VARCHAR2(38),
  pa_id            VARCHAR2(38),
  last_ver         VARCHAR2(30),
  bc_id            VARCHAR2(38),
  ZFCGBS_ID            VARCHAR2(38),
  pub_id           VARCHAR2(38),
  GZBS_ID        VARCHAR2(38),
  ien_id           VARCHAR2(42),
  pf_id            VARCHAR2(38),
  payee_account_id VARCHAR2(38),
  agent_account_id VARCHAR2(38),
  tbv_id           VARCHAR2(38)
)'
;
end if;
execute immediate 'create index SYS_JOIN_DEL_PK1 on SYS_RULE_CROSS_JOIN_DEL (RULE_ID)
 ' ;
execute immediate 'create index SYS_JOIN_DEL_PK2 on SYS_RULE_CROSS_JOIN_DEL (AGENCY_ID)
 ' ;
execute immediate 'create index SYS_JOIN_DEL_PK3 on SYS_RULE_CROSS_JOIN_DEL (FUNDTYPE_ID)
 ' ;
execute immediate 'create index SYS_JOIN_DEL_PK4 on SYS_RULE_CROSS_JOIN_DEL (EXPFUNC_ID)
 ' ;
execute immediate 'create index SYS_JOIN_DEL_PK5 on SYS_RULE_CROSS_JOIN_DEL (PAYTYPE_ID)
 ' ;
execute immediate 'create index SYS_JOIN_DEL_PK6 on SYS_RULE_CROSS_JOIN_DEL (MB_ID)
 ' ;

 
 
select count(*) into i from user_tables where table_name='SYS_RULE_DATA_CATCH
';
 if i=0 then
 execute immediate '
 create global temporary table SYS_RULE_DATA_CATCH
(
  as_id              VARCHAR2(38),
  AGENCY_ID              VARCHAR2(38),
  fundtype_id        VARCHAR2(38),
  expfunc_id         VARCHAR2(38),
  agencyexp_id       VARCHAR2(38),
  expeco_id          VARCHAR2(38),
  BGTTYPE_ID              VARCHAR2(38),
  paykind_id         VARCHAR2(38),
  MB_ID       VARCHAR2(38),
  file_id            VARCHAR2(38),
  setmode_id         VARCHAR2(38),
  in_bs_id           VARCHAR2(38),
  in_bis_id          VARCHAR2(38),
  cb_id              VARCHAR2(38),
  pb_id              VARCHAR2(38),
  ib_id              VARCHAR2(38),
  bac_id             VARCHAR2(38),
  bap_id             VARCHAR2(38),
  bai_id             VARCHAR2(38),
  bgtdir_id          VARCHAR2(38),
  bp_id              VARCHAR2(38),
  BGTSOURCE_ID       VARCHAR2(38),
  hold1_id           VARCHAR2(38),
  hold2_id           VARCHAR2(38),
  hold3_id           VARCHAR2(38),
  hold4_id           VARCHAR2(38),
  hold5_id           VARCHAR2(38),
  bis_id             VARCHAR2(38),
  inpm_id            VARCHAR2(38),
  ct_id              VARCHAR2(38),
  as_code            VARCHAR2(42),
  as_name            VARCHAR2(60),
  AGENCY_CODE            VARCHAR2(42),
  AGENCY_NAME            VARCHAR2(60),
  fundtype_code      VARCHAR2(42),
  fundtype_name      VARCHAR2(60),
  expfunc_code       VARCHAR2(42),
  expfunc_name       VARCHAR2(60),
  agencyexp_code     VARCHAR2(42),
  agencyexp_name     VARCHAR2(60),
  expeco_code        VARCHAR2(42),
  expeco_name        VARCHAR2(60),
  BGTTYPE_CODE            VARCHAR2(42),
  BGTTYPE_NAME            VARCHAR2(60),
  paykind_code       VARCHAR2(42),
  paykind_name       VARCHAR2(60),
  MB_CODE     VARCHAR2(42),
  MB_NAME     VARCHAR2(60),
  file_code          VARCHAR2(42),
  file_name          VARCHAR2(200),
  setmode_code       VARCHAR2(42),
  setmode_name       VARCHAR2(60),
  in_bs_code         VARCHAR2(42),
  in_bs_name         VARCHAR2(60),
  in_bis_code        VARCHAR2(42),
  in_bis_name        VARCHAR2(60),
  cb_code            VARCHAR2(42),
  cb_name            VARCHAR2(60),
  pb_code            VARCHAR2(42),
  pb_name            VARCHAR2(60),
  ib_code            VARCHAR2(42),
  ib_name            VARCHAR2(60),
  bac_code           VARCHAR2(42),
  bac_name           VARCHAR2(60),
  bap_code           VARCHAR2(42),
  bap_name           VARCHAR2(60),
  bai_code           VARCHAR2(42),
  bai_name           VARCHAR2(60),
  bgtdir_code        VARCHAR2(42),
  bgtdir_name        VARCHAR2(60),
  bp_code            VARCHAR2(42),
  bp_name            VARCHAR2(60),
  BGTSOURCE_CODE     VARCHAR2(42),
  BGTSOURCE_NAME     VARCHAR2(60),
  bis_code           VARCHAR2(42),
  bis_name           VARCHAR2(600),
  inpm_code          VARCHAR2(42),
  inpm_name          VARCHAR2(60),
  ct_code            VARCHAR2(42),
  ct_name            VARCHAR2(60),
  paytype_id         VARCHAR2(38),
  paytype_code       VARCHAR2(42),
  paytype_name       VARCHAR2(60),
  item_index         VARCHAR2(4) not null,
  is_set             NUMBER(2),
  billtype_id        VARCHAR2(38),
  billtype_code      VARCHAR2(42),
  billtype_name      VARCHAR2(200),
  agent_account_name VARCHAR2(60),
  agent_account_code VARCHAR2(38),
  agent_account_id   VARCHAR2(38),
  pf_id              VARCHAR2(38),
  pf_code            VARCHAR2(42),
  pf_name            VARCHAR2(60),
  payee_account_id   VARCHAR2(38),
  payee_account_code VARCHAR2(42),
  payee_account_name VARCHAR2(100),
  tbv_id             VARCHAR2(38),
  GZBS_ID          VARCHAR2(38),
  GZBS_CODE        VARCHAR2(42),
  GZBS_NAME        VARCHAR2(60),
  pa_id              VARCHAR2(38),
  pa_code            VARCHAR2(42),
  pa_name            VARCHAR2(60)
)';
end if;
execute immediate 'alter table SYS_RULE_DATA_CATCH
  add constraint DDDD primary key (ITEM_INDEX)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_RULE_RCID
';
 if i=0 then
 execute immediate '
create table SYS_RULE_RCID
(
  set_year NUMBER(4) not null,
  rule_id  NUMBER not null,
  rcid     NUMBER not null,
  last_ver VARCHAR2(30),
  RG_CODE  VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index SYS_RULE_RCID_PRO_N1 on SYS_RULE_RCID (RULE_ID)
 ' ;
execute immediate 'create index SYS_RULE_RCID_PRO_N2 on SYS_RULE_RCID (RCID)
 ' ;
execute immediate 'alter table SYS_RULE_RCID
  add constraint PRI_SYS_RULE_RCID_PRO primary key (SET_YEAR, RULE_ID, RCID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SERVICEMANAGE
';
 if i=0 then
 execute immediate '
create table SYS_SERVICEMANAGE
(
  service_code           VARCHAR2(42) not null,
  service_jndiname       VARCHAR2(39) not null,
  service_name           VARCHAR2(39),
  service_interfaceclass VARCHAR2(200) not null,
  service_url            VARCHAR2(1024) not null,
  service_wedl           VARCHAR2(4000),
  sys_id                 VARCHAR2(42),
  service_type           VARCHAR2(40)
)'
;
end if;

execute immediate 'comment on column SYS_SERVICEMANAGE.sys_id
  is ''Ӧ��ģ��ID''';
execute immediate 'alter table SYS_SERVICEMANAGE
  add constraint SYS_SERVICEMANAGE_PK primary key (SERVICE_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SESSION
';
 if i=0 then
 execute immediate '
create table SYS_SESSION
(
  session_id    VARCHAR2(38) not null,
  sys_id        VARCHAR2(42) not null,
  user_id       VARCHAR2(40),
  role_id       NUMBER,
  set_year      NUMBER(4) not null,
  user_ip       VARCHAR2(20),
  login_date    VARCHAR2(30),
  logout_date   VARCHAR2(30),
  session_value VARCHAR2(500),
  last_ver      VARCHAR2(30)
)'
;
end if;

execute immediate 'create index IDX_SYS_SESSION_PRO3 on SYS_SESSION (SESSION_VALUE)
 ' ;
execute immediate 'alter table SYS_SESSION
  add constraint SYS_SESSION_PK_PRO3 primary key (SESSION_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SESSION_BAK
';
 if i=0 then
 execute immediate '
create table SYS_SESSION_BAK
(
  session_id    VARCHAR2(38) not null,
  sys_id        VARCHAR2(42) not null,
  user_id       VARCHAR2(40),
  role_id       NUMBER,
  set_year      NUMBER(4) not null,
  user_ip       VARCHAR2(20),
  login_date    VARCHAR2(30),
  logout_date   VARCHAR2(30),
  session_value VARCHAR2(500),
  last_ver      VARCHAR2(30)
)'
;
end if;

execute immediate 'create index IDX_SYS_SESSION_BAK_PRO3 on SYS_SESSION_BAK (SESSION_VALUE)
 ' ;
execute immediate 'alter table SYS_SESSION_BAK
  add constraint SYS_SESSION_BAK_PK_PRO3 primary key (SESSION_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SETYEARMENU
';
 if i=0 then
 execute immediate '
create table SYS_SETYEARMENU
(
  menu_id        VARCHAR2(38) not null,
  normal_enabled NUMBER(1),
  change_enabled NUMBER(1),
  close_enabled  NUMBER(1)
)'
;
end if;

execute immediate 'comment on column SYS_SETYEARMENU.menu_id
  is ''�˵�Ψһ���ţ�����sys_menu��id''';
execute immediate 'comment on column SYS_SETYEARMENU.normal_enabled
  is ''���������ñ�ʶ''';
execute immediate 'comment on column SYS_SETYEARMENU.change_enabled
  is ''���������ñ�ʶ''';
execute immediate 'comment on column SYS_SETYEARMENU.close_enabled
  is ''�ر������ñ�ʶ''';
execute immediate 'alter table SYS_SETYEARMENU
  add constraint SYS_SETYEARMENU_PK primary key (MENU_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_STATUS
';
 if i=0 then
 execute immediate '
create table SYS_STATUS
(
  status_id   VARCHAR2(38) not null,
  status_name VARCHAR2(42) not null,
  status_code VARCHAR2(60) not null,
  last_ver    VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_STATUS.status_id
  is ''״̬ΨһID''';
execute immediate 'comment on column SYS_STATUS.status_name
  is ''״̬����''';
execute immediate 'comment on column SYS_STATUS.status_code
  is ''״̬����''';
execute immediate 'alter table SYS_STATUS
  add constraint SYS_STATUS_PK primary key (STATUS_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_YEAR';
 if i=0 then
 execute immediate '
create table SYS_SYNC_ELE_CONFIG
(
  ele_code       VARCHAR2(42) not null,
  sync_direction NUMBER not null
)'
;
end if;

execute immediate 'comment on column SYS_SYNC_ELE_CONFIG.ele_code
  is ''Ҫ�ؼ��''';
execute immediate 'comment on column SYS_SYNC_ELE_CONFIG.sync_direction
  is ''ͬ������''';
execute immediate 'alter table SYS_SYNC_ELE_CONFIG
  add constraint SYS_SYNC_ELE_CONFIG_PAYTYPE primary key (ELE_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SYNC_FIELD_MAPPING
';
 if i=0 then
 execute immediate '
create table SYS_SYNC_FIELD_MAPPING
(
  ele_code   VARCHAR2(42) not null,
  ele_field  VARCHAR2(42) not null,
  fasp_field VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_SYNC_FIELD_MAPPING.ele_code
  is ''Ҫ�ؼ��''';
execute immediate 'comment on column SYS_SYNC_FIELD_MAPPING.ele_field
  is ''����Ҫ�ر��ֶ���''';
execute immediate 'comment on column SYS_SYNC_FIELD_MAPPING.fasp_field
  is ''�������������ݱ��ֶ���''';
execute immediate 'create index SYS_SYNC_FIELD_MAPPING_N1 on SYS_SYNC_FIELD_MAPPING (ELE_CODE)';
  
execute immediate 'create index SYS_SYNC_FIELD_MAPPING_N2 on SYS_SYNC_FIELD_MAPPING (ELE_FIELD)';
  
execute immediate 'create index SYS_SYNC_FIELD_MAPPING_N3 on SYS_SYNC_FIELD_MAPPING (FASP_FIELD)';



select count(*) into i from user_tables where table_name='SYS_SYNC_GUID_ITEMID
';
 if i=0 then
 execute immediate '
create table SYS_SYNC_GUID_ITEMID
(
  chr_id   VARCHAR2(38),
  itemid   NUMBER(9),
  ele_code VARCHAR2(42)
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_SYNC_RECORD
';
 if i=0 then
 execute immediate '
create table SYS_SYNC_RECORD
(
  record_id      NUMBER not null,
  ele_code       VARCHAR2(42) not null,
  is_sync_all    NUMBER,
  sync_type      NUMBER not null,
  sync_direction NUMBER not null,
  sync_time      VARCHAR2(30) not null,
  is_succeed     NUMBER not null,
  err_msg        VARCHAR2(200)
)'
;
end if;

execute immediate 'comment on column SYS_SYNC_RECORD.record_id
  is ''ȡֵseq_sys_sync_rec_id''';
execute immediate 'comment on column SYS_SYNC_RECORD.ele_code
  is ''Ҫ�ؼ��''';
execute immediate 'comment on column SYS_SYNC_RECORD.is_sync_all
  is ''�Ƿ�ȫ��ͬ��''';
execute immediate 'comment on column SYS_SYNC_RECORD.sync_type
  is ''�Ƿ��ֹ�ͬ��''';
execute immediate 'comment on column SYS_SYNC_RECORD.sync_direction
  is ''ͬ������''';
execute immediate 'comment on column SYS_SYNC_RECORD.sync_time
  is ''ͬ��ʱ��''';
execute immediate 'comment on column SYS_SYNC_RECORD.is_succeed
  is ''ͬ���Ƿ�ɹ�''';
execute immediate 'comment on column SYS_SYNC_RECORD.err_msg
  is ''ͬ�������¼''';
execute immediate 'create index SYS_SYNC_RECORD_N1 on SYS_SYNC_RECORD (ELE_CODE)
  ';
execute immediate 'alter table SYS_SYNC_RECORD
  add constraint SYS_SYNC_RECORD_PAYTYPE primary key (RECORD_ID, ELE_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SYNC_TABLE_MAPPING
';
 if i=0 then
 execute immediate '
create table SYS_SYNC_TABLE_MAPPING
(
  ele_code                VARCHAR2(42) not null,
  ele_table               VARCHAR2(42) not null,
  ele_view                VARCHAR2(42),
  elementcode             VARCHAR2(30),
  fasp_table              VARCHAR2(42) not null,
  fasp_matching_condition VARCHAR2(200)
)'
;
end if;

execute immediate 'comment on column SYS_SYNC_TABLE_MAPPING.ele_code
  is ''Ҫ�ؼ��''';
execute immediate 'comment on column SYS_SYNC_TABLE_MAPPING.ele_table
  is ''����Ҫ�ر���''';
execute immediate 'comment on column SYS_SYNC_TABLE_MAPPING.ele_view
  is ''����Ҫ����ͼ��''';
execute immediate 'comment on column SYS_SYNC_TABLE_MAPPING.elementcode
  is ''�������������ݼ��''';
execute immediate 'comment on column SYS_SYNC_TABLE_MAPPING.fasp_table
  is ''�������������ݱ���''';
execute immediate 'comment on column SYS_SYNC_TABLE_MAPPING.fasp_matching_condition
  is ''������ƥ������''';
execute immediate 'create index SYS_SYNC_TABLE_MAPPING_N1 on SYS_SYNC_TABLE_MAPPING (ELE_TABLE)';
  
execute immediate 'create index SYS_SYNC_TABLE_MAPPING_N2 on SYS_SYNC_TABLE_MAPPING (FASP_TABLE)';
  
execute immediate 'alter table SYS_SYNC_TABLE_MAPPING
  add constraint ELE_CODE primary key (ELE_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SYNMANAGER
';
 if i=0 then
 execute immediate '
create table SYS_SYNMANAGER
(
  guid           VARCHAR2(38) not null,
  table_type     NUMBER(2) not null,
  table_name     VARCHAR2(60),
  syn_user       VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  syn_time       VARCHAR2(30),
  set_year       NUMBER(4) not null,
  last_ver       VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_SYNMANAGER.guid
  is ''Ψһ��ʶ������Ϣ''';
execute immediate 'comment on column SYS_SYNMANAGER.table_type
  is ''�����ͷ���''';
execute immediate 'comment on column SYS_SYNMANAGER.table_name
  is ''��Ӧ����''';
execute immediate 'comment on column SYS_SYNMANAGER.syn_user
  is ''ͬ���û�''';
execute immediate 'comment on column SYS_SYNMANAGER.set_year
  is ''ҵ�����''';
execute immediate 'alter table SYS_SYNMANAGER
  add constraint SYS_SYNMANAGER_PK primary key (GUID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SYN_RIGHT_DETAIL
';
 if i=0 then
 execute immediate '
create table SYS_SYN_RIGHT_DETAIL
(
  right_detail_id VARCHAR2(38),
  right_id        VARCHAR2(38) not null,
  ele_value       VARCHAR2(38),
  set_year        NUMBER(4),
  last_ver        VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_SYN_RIGHT_DETAIL.right_id
  is ''Ȩ��IdΨһ����''';
execute immediate 'comment on column SYS_SYN_RIGHT_DETAIL.ele_value
  is ''��Ҫ��ֵ''';
execute immediate 'comment on column SYS_SYN_RIGHT_DETAIL.set_year
  is ''ҵ�����''';
execute immediate 'create index SYS_SYN_RIGHT_DETAIL_N1 on SYS_SYN_RIGHT_DETAIL (SET_YEAR)
 ' ;
execute immediate 'alter table SYS_SYN_RIGHT_DETAIL
  add constraint SYS_SYN_RIGHT_DETAIL_PK primary key (RIGHT_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_SYN_RIGHT_TYPE
';
 if i=0 then
 execute immediate '
create table SYS_SYN_RIGHT_TYPE
(
  right_id   VARCHAR2(38) not null,
  role_id    NUMBER,
  ele_code   VARCHAR2(42),
  right_type NUMBER(1),
  set_year   NUMBER(4),
  last_ver   VARCHAR2(30)
)'
;
end if;

execute immediate 'create index SYS_SYN_RIGHT_TYPE_N1_PRO5 on SYS_SYN_RIGHT_TYPE (ROLE_ID, SET_YEAR)
 ' ;
execute immediate 'alter table SYS_SYN_RIGHT_TYPE
  add constraint SYS_SYN_RIGHT_TYPE_PK_PRO5 primary key (RIGHT_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_TABLEMANAGER
';
 if i=0 then
 execute immediate '
create table SYS_TABLEMANAGER
(
  chr_id         VARCHAR2(38) not null,
  table_code     VARCHAR2(60) default 0 not null,
  table_name     VARCHAR2(60),
  table_type     NUMBER(3),
  table_desc     VARCHAR2(200),
  is_system      NUMBER(1),
  create_date    VARCHAR2(30),
  create_user    VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  latest_op_user VARCHAR2(42),
  id_column_name VARCHAR2(30),
  last_ver       VARCHAR2(30),
  sys_id         VARCHAR2(42)
)'
;
end if;

execute immediate 'comment on column SYS_TABLEMANAGER.chr_id
  is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column SYS_TABLEMANAGER.table_code
  is ''����''';
execute immediate 'comment on column SYS_TABLEMANAGER.table_name
  is ''��ʾ��''';
execute immediate 'comment on column SYS_TABLEMANAGER.table_type
  is ''�����ͣ�101���������ݱ�201��ҵ����ϸ��202��ҵ�񵥱�203����ȱ�301��ϵͳ��''';
execute immediate 'comment on column SYS_TABLEMANAGER.table_desc
  is ''����''';
execute immediate 'comment on column SYS_TABLEMANAGER.is_system
  is ''�Ƿ�ϵͳ����''';

execute immediate 'comment on column SYS_TABLEMANAGER.create_user
  is ''�����û�''';

execute immediate 'comment on column SYS_TABLEMANAGER.latest_op_user
  is ''����޸��û�''';
execute immediate 'comment on column SYS_TABLEMANAGER.id_column_name
  is ''����ID�ֶε�����''';
execute immediate 'comment on column SYS_TABLEMANAGER.sys_id
  is ''Ӧ��ģ��ID''';
execute immediate 'alter table SYS_TABLEMANAGER
  add constraint SYS_TABLEMANAGER_PK primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_TOOLBAR
';
 if i=0 then
 execute immediate '
create table SYS_TOOLBAR
(
  toolbar_id   VARCHAR2(38) not null,
  toolbar_code VARCHAR2(42) not null,
  toolbar_name VARCHAR2(60),
  toolbar_type NUMBER(2) not null,
  menu_id      NUMBER,
  toolbar_icon VARCHAR2(100),
  tips         VARCHAR2(100),
  last_ver     VARCHAR2(30)
)'
;
end if;

execute immediate 'alter table SYS_TOOLBAR
  add constraint SYS_TOOLBAR_PK_PRO8 primary key (TOOLBAR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_UICONF
';
 if i=0 then
 execute immediate '
create table SYS_UICONF
(
  uiconf_type       VARCHAR2(60) not null,
  uiconf_field      VARCHAR2(60) not null,
  uiconf_field_name VARCHAR2(200),
  uiconf_field_type VARCHAR2(10),
  last_ver          VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_UICONF.uiconf_type
  is ''UI�������''';
execute immediate 'comment on column SYS_UICONF.uiconf_field
  is ''UI��������''';
execute immediate 'comment on column SYS_UICONF.uiconf_field_name
  is ''UI��������������''';
execute immediate 'comment on column SYS_UICONF.uiconf_field_type
  is ''UI�����������''';
execute immediate 'create index IDX_SYS_UICONF_01 on SYS_UICONF (UICONF_FIELD, UICONF_TYPE)';
  
execute immediate 'alter table SYS_UICONF
  add constraint SYS_UICONF_PK primary key (UICONF_TYPE, UICONF_FIELD)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_UICONF_DETAIL
';
 if i=0 then
 execute immediate '
create table SYS_UICONF_DETAIL
(
  uiconf_id    VARCHAR2(38) not null,
  uiconf_field VARCHAR2(60) not null,
  uiconf_value VARCHAR2(3999) not null,
  last_ver     VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_UICONF_DETAIL.uiconf_id
  is ''UI����ID����ΪUI_ID��UI_DETAIL_ID��''';
execute immediate 'comment on column SYS_UICONF_DETAIL.uiconf_field
  is ''UI��������''';
execute immediate 'comment on column SYS_UICONF_DETAIL.uiconf_value
  is ''UI��������ֵ''';
execute immediate 'alter table SYS_UICONF_DETAIL
  add constraint SYS_UICONF_DETAIL_PK primary key (UICONF_ID, UICONF_FIELD)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_UIDETAIL
';
 if i=0 then
 execute immediate '
create table SYS_UIDETAIL
(
  ui_detail_id        VARCHAR2(38) not null,
  ui_id               VARCHAR2(38) not null,
  field_name          VARCHAR2(60) not null,
  disp_mode           VARCHAR2(20),
  is_nessary          NUMBER(1) not null,
  is_enabled          NUMBER(1) not null,
  field_index         NUMBER(10) not null,
  id                  VARCHAR2(60),
  title               VARCHAR2(60),
  cols                NUMBER(3),
  visible             VARCHAR2(10),
  editable            VARCHAR2(10),
  value               VARCHAR2(300),
  ref_model           VARCHAR2(500),
  source              VARCHAR2(60),
  width               NUMBER(3),
  is_must_input       VARCHAR2(10),
  last_ver            VARCHAR2(30),
  enabled             VARCHAR2(10),
  query_relation_sign VARCHAR2(2),
  detail_type         NUMBER(1) default 0,
  parent_id           VARCHAR2(38),
  virtual_column_sql  VARCHAR2(1000),
  is_virtual_column   VARCHAR2(10) default ''false'',
  RG_CODE             VARCHAR2(42) not null,
  set_year            NUMBER(4),
  SUM_FLAG VARCHAR2(10),
  HEADER_LEVEL VARCHAR2(10)
)'
;
end if;

execute immediate 'comment on column SYS_UIDETAIL.HEADER_LEVEL
  is ''�еļ���''';

execute immediate 'comment on column SYS_UIDETAIL.SUM_FLAG
  is ''�Ƿ�ϼ���''';

execute immediate 'comment on column SYS_UIDETAIL.ui_detail_id
  is ''��ϸ���(GUID)''';
execute immediate 'comment on column SYS_UIDETAIL.ui_id
  is ''�ֶ�������''';
execute immediate 'comment on column SYS_UIDETAIL.field_name
  is ''�ֶ�����''';
execute immediate 'comment on column SYS_UIDETAIL.disp_mode
  is ''text���ı���textArea�������ı���checkbox��ѡ���int��������decimal��С����radio����ѡ��combobox����Ͽ�datetime: ���ڿؼ���tree�����Ϳؼ���assistcombobox������¼��������tableassist�������¼�룻treeassist�����͸���¼�룻line���ָ��߿ؼ���button:��ť�ؼ���column���б����''';
execute immediate 'comment on column SYS_UIDETAIL.is_nessary
  is ''�Ƿ��ѡ��,���Ϊ1��ENABLED����Ϊ1''';
execute immediate 'comment on column SYS_UIDETAIL.is_enabled
  is ''�Ƿ�����,ʵʩ''';
execute immediate 'comment on column SYS_UIDETAIL.field_index
  is ''�ֶ�������''';
execute immediate 'comment on column SYS_UIDETAIL.id
  is ''�ؼ���Ψһ��ʶ''';
execute immediate 'comment on column SYS_UIDETAIL.title
  is ''�ؼ��ı�������''';
execute immediate 'comment on column SYS_UIDETAIL.cols
  is ''��ʾ��''';
execute immediate 'comment on column SYS_UIDETAIL.visible
  is ''�ؼ��Ƿ���ʾ���û�''';
execute immediate 'comment on column SYS_UIDETAIL.editable
  is ''�ؼ��Ƿ�ɱ༭,�û�''';
execute immediate 'comment on column SYS_UIDETAIL.value
  is ''�ؼ���Ĭ��ֵ''';
execute immediate 'comment on column SYS_UIDETAIL.ref_model
  is ''ö����,Ҫ�ؼ�ƣ��ؼ��Ĳο�����ģ�ͣ�FIntegerField��FDecimalField��FRadioGroup��FComboBox��FTree��FAssistComboBox����FTreeAssistInput��''';
execute immediate 'comment on column SYS_UIDETAIL.source
  is ''�õ����ݵ�����Դ��FAssistComboBox��FTreeAssistInput��''';
execute immediate 'comment on column SYS_UIDETAIL.width
  is ''���''';
execute immediate 'comment on column SYS_UIDETAIL.is_must_input
  is ''�Ƿ��¼��''';
execute immediate 'comment on column SYS_UIDETAIL.enabled
  is ''�ؼ��Ƿ���������''';
execute immediate 'comment on column SYS_UIDETAIL.query_relation_sign
  is ''��ѯ��ϵ����0�����ڣ�1�������ڣ�2�����ڣ�3�����ڵ��ڣ�4��С�ڣ�5��С�ڵ��ڣ�6��like��7��nolike''';
execute immediate 'comment on column SYS_UIDETAIL.detail_type
  is ''0���ֶ��� 1��������''';
execute immediate 'comment on column SYS_UIDETAIL.parent_id
  is ''�ϼ�������ID''';
execute immediate 'comment on column SYS_UIDETAIL.virtual_column_sql
  is ''�����е�SQL''';
execute immediate 'comment on column SYS_UIDETAIL.is_virtual_column
  is ''�Ƿ�������''';
execute immediate 'create index IDX_SYS_UIDETAIL_01 on SYS_UIDETAIL (DISP_MODE)';
  
execute immediate 'create index IDX_SYS_UIDETAIL_02 on SYS_UIDETAIL (UI_ID)
 ' ;
execute immediate 'create unique index SYS_UIDETAIL1_PK on SYS_UIDETAIL (UI_DETAIL_ID)
 ' ;
execute immediate 'create index SYS_UIDETAIL_RGYEAR on SYS_UIDETAIL (RG_CODE, SET_YEAR)
 ' ;

 
 
select count(*) into i from user_tables where table_name='SYS_UIDETAIL_MULTIVALUE
';
 if i=0 then
 execute immediate '
 create table SYS_UIDETAIL_MULTIVALUE
(
  ui_detail_id VARCHAR2(38) not null,
  value        VARCHAR2(38) not null
)'
;
end if;

execute immediate 'comment on column SYS_UIDETAIL_MULTIVALUE.ui_detail_id
  is ''��ϸ��Id''';
execute immediate 'comment on column SYS_UIDETAIL_MULTIVALUE.value
  is ''��ϸ�е�����һ��Ĭ��ֵ''';

  
  
select count(*) into i from user_tables where table_name='SYS_UIMANAGER
';
 if i=0 then
 execute immediate '
  create table SYS_UIMANAGER
(
  ui_id               VARCHAR2(38) not null,
  ui_code             VARCHAR2(42) not null,
  ui_name             VARCHAR2(150),
  ui_type             VARCHAR2(10),
  ui_source           VARCHAR2(60),
  remark              VARCHAR2(200),
  create_user         VARCHAR2(42),
  create_date         VARCHAR2(30),
  latest_op_user      VARCHAR2(42),
  latest_op_date      VARCHAR2(30),
  columns             NUMBER(3) default 1,
  id                  VARCHAR2(60),
  title               VARCHAR2(60),
  last_ver            VARCHAR2(30),
  query_relation_sign VARCHAR2(1),
  sys_id              VARCHAR2(42),
  RG_CODE             VARCHAR2(42) not null,
  set_year            NUMBER(4)
)'
;
end if;
execute immediate 'comment on column SYS_UIMANAGER.ui_id
  is ''GUID����''';
execute immediate 'comment on column SYS_UIMANAGER.ui_code
  is ''UI����CODE''';
execute immediate 'comment on column SYS_UIMANAGER.ui_name
  is ''UI������������''';
execute immediate 'comment on column SYS_UIMANAGER.ui_type
  is ''001��¼����ͼ��002���б���ͼ��003����ѯ��ͼ��004����ϸ��ʾ��ͼ��005��Toolbar��ͼ��101��·����ͼ''';
execute immediate 'comment on column SYS_UIMANAGER.columns
  is ''��ʾ��''';
execute immediate 'comment on column SYS_UIMANAGER.id
  is ''�ؼ���Ψһ��ʶ''';
execute immediate 'comment on column SYS_UIMANAGER.title
  is ''�ؼ��ı�������''';
execute immediate 'comment on column SYS_UIMANAGER.query_relation_sign
  is ''��ѯ��ϵ����0�����ڣ�1�������ڣ�2�����ڣ�3�����ڵ��ڣ�4��С�ڣ�5��С�ڵ��ڣ�6��like��7��nolike''';
execute immediate 'comment on column SYS_UIMANAGER.sys_id
  is ''Ӧ��ģ��ID''';
execute immediate 'alter table SYS_UIMANAGER
  add constraint SYS_UIMANAGER1_PK primary key (UI_ID)';
   
  
execute immediate 'alter table SYS_UIMANAGER
  add constraint SYS_UIMANAGER_UK unique (UI_CODE, RG_CODE, SET_YEAR)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USERACCREDIT
';
 if i=0 then
 execute immediate '
create table SYS_USERACCREDIT
(
  id            VARCHAR2(38) not null,
  accredit_from VARCHAR2(38) not null,
  accredit_to   VARCHAR2(38) not null,
  start_time    VARCHAR2(30) not null,
  end_time      VARCHAR2(30) not null,
  back_time     VARCHAR2(30),
  state         NUMBER default 0 not null,
  remark        VARCHAR2(200),
  role_id       NUMBER
)'
;
end if;

execute immediate 'comment on column SYS_USERACCREDIT.id
  is ''ÿ�����ݵ�Ψһid''';
execute immediate 'comment on column SYS_USERACCREDIT.accredit_from
  is ''��Ȩ�û���id''';
execute immediate 'comment on column SYS_USERACCREDIT.accredit_to
  is ''����Ȩ���û�id''';

execute immediate 'comment on column SYS_USERACCREDIT.state
  is ''0��δ���� 1���ѻ���''';
execute immediate 'comment on column SYS_USERACCREDIT.remark
  is ''��ע''';
execute immediate 'alter table SYS_USERACCREDIT
  add constraint SYS_USERACCREDIT_PK primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USERMANAGE
';
 if i=0 then
 execute immediate '
create table SYS_USERMANAGE
(
  user_id           VARCHAR2(40) not null,
  user_code         VARCHAR2(42) not null,
  user_name         VARCHAR2(100),
  password          VARCHAR2(60),
  org_type          VARCHAR2(42) not null,
  org_code          VARCHAR2(42),
  level_num         NUMBER(2) not null,
  is_leaf           NUMBER(1),
  gender            NUMBER(1),
  telephone         VARCHAR2(60),
  mobile            VARCHAR2(60),
  enabled           NUMBER(1) not null,
  headship_code     VARCHAR2(42),
  birthday          VARCHAR2(10),
  address           VARCHAR2(60),
  email             VARCHAR2(60),
  user_type         NUMBER(3) not null,
  is_audit          NUMBER(1) not null,
  audit_date        VARCHAR2(30),
  audit_user        VARCHAR2(42),
  nickname          VARCHAR2(42),
  last_ver          VARCHAR2(30),
  MB_ID      VARCHAR2(38),
  belong_org        VARCHAR2(38),
  belong_type       VARCHAR2(38),
  security_level    NUMBER(1),
  init_password     NUMBER(1) default 0,
  gp_org            VARCHAR2(38),
  payment_password  VARCHAR2(38) default ''c4ca4238a0b923820dcc509a6f75849b'',
  title_tech        VARCHAR2(20),
  identity_card     VARCHAR2(30),
  rtxuin            VARCHAR2(20),
  photo             VARCHAR2(50),
  photo_blobid      VARCHAR2(50),
  ca_serial         VARCHAR2(60),
  co_name           VARCHAR2(100),
  emp_index         NUMBER,
  set_year          NUMBER(4),
  is_edit_pwd       NUMBER(1) default (0),
  is_three_security NUMBER(1) default (1),
  is_ever_locked    NUMBER(1) default (0),
  locked_date       VARCHAR2(30),
  imsi              VARCHAR2(64),
  imei              VARCHAR2(64),
  weixin            VARCHAR2(32),
  is_blacklist      CHAR(1) default ''0'',
  rg_code           VARCHAR2(6)
)'
;
end if;

execute immediate 'comment on column SYS_USERMANAGE.is_edit_pwd
  is ''�Ƿ��޸Ĺ�����''';
execute immediate 'comment on column SYS_USERMANAGE.is_three_security
  is ''�Ƿ�����������ȫ''';
execute immediate 'comment on column SYS_USERMANAGE.is_ever_locked
  is ''�Ƿ���������''';
execute immediate 'comment on column SYS_USERMANAGE.locked_date
  is ''�Զ���������''';
execute immediate 'comment on column SYS_USERMANAGE.imsi
  is ''�����ƶ��û�ʶ����(��SIM����)''';
execute immediate 'comment on column SYS_USERMANAGE.imei
  is ''�ƶ��豸���������(���ƶ��豸��)''';
execute immediate 'comment on column SYS_USERMANAGE.weixin
  is ''΢�ź�''';
execute immediate 'comment on column SYS_USERMANAGE.is_blacklist
  is ''�Ƿ��������0����1����''';
execute immediate 'create index SYS_USER_USERCODE_PRO4 on SYS_USERMANAGE (USER_CODE)';
  
execute immediate 'alter table SYS_USERMANAGE
  add constraint SYS_USER_PK_PRO3 primary key (USER_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USERPARA
';
 if i=0 then
 execute immediate '
create table SYS_USERPARA
(
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42) not null,
  chr_name       VARCHAR2(60),
  chr_value      VARCHAR2(3999),
  chr_desc       VARCHAR2(200),
  sys_id         VARCHAR2(42),
  is_visible     NUMBER(1) default 0 not null,
  is_edit        NUMBER(1) default 0 not null,
  field_valueset VARCHAR2(200),
  field_disptype NUMBER(2) default 0,
  group_name     VARCHAR2(60),
  set_id         VARCHAR2(38),
  last_ver       VARCHAR2(30),
  RG_CODE        VARCHAR2(42) not null,
  set_year       NUMBER(4) not null
)'
;
end if;

execute immediate 'comment on column SYS_USERPARA.chr_id
  is ''Ψһ��ʶ����������Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column SYS_USERPARA.chr_code
  is ''��������''';
execute immediate 'comment on column SYS_USERPARA.chr_name
  is ''��������''';
execute immediate 'comment on column SYS_USERPARA.chr_value
  is ''����ֵ''';
execute immediate 'comment on column SYS_USERPARA.chr_desc
  is ''����˵��''';
execute immediate 'comment on column SYS_USERPARA.sys_id
  is ''����������������ϵͳ
 ���ձ�ʾ������ϵͳ���ã�
 ''';
execute immediate 'comment on column SYS_USERPARA.is_visible
  is ''�Ƿ������û�����(1����,0������)''';
execute immediate 'comment on column SYS_USERPARA.is_edit
  is ''�Ƿ������û��༭''';
execute immediate 'comment on column SYS_USERPARA.field_valueset
  is ''�������ֶε�ȡֵ��Χ����/�ֿ�
 ��1/2/3/4/5/6/7/8��''';
execute immediate 'comment on column SYS_USERPARA.field_disptype
  is ''������¼�ֶ���ʾ����''';
execute immediate 'comment on column SYS_USERPARA.group_name
  is ''���ÿ����ϵͳ�������ڽ��й���''';
execute immediate 'comment on column SYS_USERPARA.set_id
  is ''����ID''';
execute immediate 'comment on column SYS_USERPARA.RG_CODE
  is ''����''';
execute immediate 'comment on column SYS_USERPARA.set_year
  is ''���''';
execute immediate 'create index SYS_USERPARA_N1 on SYS_USERPARA (SYS_ID)';
  
execute immediate 'create index SYS_USERPARA_N2 on SYS_USERPARA (CHR_CODE)';
  
execute immediate 'alter table SYS_USERPARA
  add constraint SYS_USERPARA_PK primary key (CHR_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_BOOKSET
';
 if i=0 then
 execute immediate '
create table SYS_USER_BOOKSET
(
  user_id    VARCHAR2(38) not null,
  bookset_id VARCHAR2(38) not null,
  is_default NUMBER(1) default 0
)'
;
end if;

execute immediate 'comment on column SYS_USER_BOOKSET.user_id
  is ''��Ȩ���û�id''';
execute immediate 'comment on column SYS_USER_BOOKSET.bookset_id
  is ''���׵�chr_id''';
execute immediate 'alter table SYS_USER_BOOKSET
  add constraint SYS_USER_BOOKSET_PK primary key (USER_ID, BOOKSET_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_BOOKSET_ROLE
';
 if i=0 then
 execute immediate '
create table SYS_USER_BOOKSET_ROLE
(
  user_id    VARCHAR2(40) not null,
  bookset_id VARCHAR2(38) not null,
  role_id    NUMBER not null
)'
;
end if;

execute immediate 'comment on column SYS_USER_BOOKSET_ROLE.user_id
  is ''��Ȩ���û�id''';
execute immediate 'comment on column SYS_USER_BOOKSET_ROLE.bookset_id
  is ''���׵�chr_id''';
execute immediate 'comment on column SYS_USER_BOOKSET_ROLE.role_id
  is ''��ɫ��role_id''';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_CERTIFICATE
';
 if i=0 then
 execute immediate '
create table SYS_USER_CERTIFICATE
(
  user_id       VARCHAR2(38) not null,
  user_code     VARCHAR2(42) not null,
  create_date   VARCHAR2(20),
  last_op_date  VARCHAR2(20),
  is_delete     CHAR(1) default 2,
  cert_refno    VARCHAR2(64),
  cert_authcode VARCHAR2(64),
  cert_sn       VARCHAR2(64),
  cert_dn       VARCHAR2(64),
  cert_notafter VARCHAR2(64)
)'
;
end if;

execute immediate 'comment on column SYS_USER_CERTIFICATE.user_id
  is ''�û�ID''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.user_code
  is ''�û�����''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.create_date
  is ''֤����������''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.last_op_date
  is ''֤������������''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.is_delete
  is ''ͣ�ñ�ʶ��0�����á�1��ͣ��''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.cert_refno
  is ''֤��ο���(�Ű�CA)''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.cert_authcode
  is ''֤����Ȩ��(�Ű�CA)''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.cert_sn
  is ''֤�����к�(�Ű�CA)''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.cert_dn
  is ''֤������(�Ű�CA)''';
execute immediate 'comment on column SYS_USER_CERTIFICATE.cert_notafter
  is ''֤���ֹ��Ч����(CA)''';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_GROUP
';
 if i=0 then
 execute immediate '
create table SYS_USER_GROUP
(
  id       NUMBER not null,
  is_valid CHAR(1) default ''1'',
  RG_CODE  VARCHAR2(42),
  set_year NUMBER(4),
  name     VARCHAR2(200),
  last_ver DATE default SYSDATE,
  upid     NUMBER not null
)'
;
end if;

execute immediate 'comment on table SYS_USER_GROUP
  is ''�û�Ⱥ''';
execute immediate 'comment on column SYS_USER_GROUP.id
  is ''�û�ȺID''';
execute immediate 'comment on column SYS_USER_GROUP.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column SYS_USER_GROUP.RG_CODE
  is ''����''';
execute immediate 'comment on column SYS_USER_GROUP.set_year
  is ''���''';
execute immediate 'comment on column SYS_USER_GROUP.name
  is''�û�Ⱥ����''';
execute immediate 'comment on column SYS_USER_GROUP.last_ver
  is ''������ʱ��''';
execute immediate 'comment on column SYS_USER_GROUP.upid
  is ''����ID������������£�''';
execute immediate 'create index INDEX_SYS_USER_GROUP_UPID on SYS_USER_GROUP (UPID)
 ' ;
execute immediate 'create index INDEX_USER_GROUP_NAME on SYS_USER_GROUP (NAME)
 ' ;
execute immediate 'alter table SYS_USER_GROUP
  add constraint PK_USER_GROUP_ID primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_GROUP_RELATION
';
 if i=0 then
 execute immediate '
create table SYS_USER_GROUP_RELATION
(
  id            NUMBER not null,
  is_valid      CHAR(1) default ''1'',
  RG_CODE       VARCHAR2(42),
  set_year      NUMBER(4),
  user_id       VARCHAR2(60) not null,
  user_group_id NUMBER not null,
  last_ver      DATE default SYSDATE,
  upid          NUMBER not null
)'
;
end if;

execute immediate 'comment on table SYS_USER_GROUP_RELATION
  is ''�û����û�Ⱥ��ϵ��''';
execute immediate 'comment on column SYS_USER_GROUP_RELATION.id
  is ''ID��ˮ��''';
execute immediate 'comment on column SYS_USER_GROUP_RELATION.is_valid
  is ''�Ƿ���Ч0��Ч1��Ч''';
execute immediate 'comment on column SYS_USER_GROUP_RELATION.RG_CODE
  is ''����''';
execute immediate 'comment on column SYS_USER_GROUP_RELATION.set_year
  is ''���''';
execute immediate 'comment on column SYS_USER_GROUP_RELATION.user_id
  is ''�û�id''';
execute immediate 'comment on column SYS_USER_GROUP_RELATION.user_group_id
  is ''�û�ȺID''';
execute immediate 'comment on column SYS_USER_GROUP_RELATION.last_ver
  is ''������ʱ��''';
execute immediate 'comment on column SYS_USER_GROUP_RELATION.upid
  is ''����ID������������£�''';
execute immediate 'create index INDEX_SYS_USER_GROUP_REL_UPID on SYS_USER_GROUP_RELATION (UPID)
 ' ;
execute immediate 'create index INDEX_USER_GROUP_RELATION_IDS on SYS_USER_GROUP_RELATION (ID, USER_ID, USER_GROUP_ID)
  ';
execute immediate 'alter table SYS_USER_GROUP_RELATION
  add constraint PK_USER_GROUP_RELATION_ID primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_ORG
';
 if i=0 then
 execute immediate '
create table SYS_USER_ORG
(
  user_id  VARCHAR2(40) not null,
  org_id   VARCHAR2(38) not null,
  last_ver VARCHAR2(30),
  set_year VARCHAR2(4)
)'
;
end if;

execute immediate 'alter table SYS_USER_ORG
  add constraint SYS_USER_ORG_PK_PRO3 primary key (USER_ID, ORG_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_REGION
';
 if i=0 then
 execute immediate '
create table SYS_USER_REGION
(
  user_id    VARCHAR2(40) not null,
  RG_CODE    VARCHAR2(42) not null,
  is_default NUMBER(1) default 0
)'
;
end if;

execute immediate 'comment on column SYS_USER_REGION.user_id
  is ''�û�id''';
execute immediate 'comment on column SYS_USER_REGION.RG_CODE
  is ''������chr_id''';
execute immediate 'alter table SYS_USER_REGION
  add constraint SYS_USER_REGION_PK primary key (USER_ID, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_ROLE_RULE
';
 if i=0 then
 execute immediate '
create table SYS_USER_ROLE_RULE
(
  user_id    VARCHAR2(40) not null,
  role_id    varchar2(100) not null,
  rule_id    NUMBER,
  is_defined NUMBER(1) not null,
  set_year   NUMBER(4) not null,
  last_ver   VARCHAR2(30),
  RG_CODE    VARCHAR2(42) not null,
  role_name varchar2(100)
)'
;
end if;

execute immediate 'create index SYS_USER_ROLE_RULE_PRO3_N1 on SYS_USER_ROLE_RULE (SET_YEAR)';
  
execute immediate 'create index SYS_USER_ROLE_RULE_PRO3_PK1 on SYS_USER_ROLE_RULE (USER_ID)';
  
execute immediate 'create index SYS_USER_ROLE_RULE_PRO3_PK2 on SYS_USER_ROLE_RULE (ROLE_ID)';
  
execute immediate 'create index SYS_USER_ROLE_RULE_PRO3_PK3 on SYS_USER_ROLE_RULE (RULE_ID)';
  
execute immediate 'alter table SYS_USER_ROLE_RULE
  add constraint SYS_USER_ROLE_RULE_PRO3_PK primary key (USER_ID, ROLE_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_RULE
';
 if i=0 then
 execute immediate '
create table SYS_USER_RULE
(
  user_id  VARCHAR2(40) not null,
  rule_id  NUMBER,
  set_year NUMBER(4) not null,
  last_ver VARCHAR2(30),
  RG_CODE  VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index SYS_USER_RULE_PRO3_N1 on SYS_USER_RULE (SET_YEAR)';
  
execute immediate 'alter table SYS_USER_RULE
  add constraint SYS_USER_RULE_PRO3_PK primary key (USER_ID, SET_YEAR, RG_CODE)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_SYS
';
 if i=0 then
 execute immediate '
create table SYS_USER_SYS
(
  user_sys_id     VARCHAR2(42) not null,
  user_sys_name   VARCHAR2(60) not null,
  user_sys_desc   VARCHAR2(200) not null,
  enabled         NUMBER(1) default 1 not null,
  welcome_img     VARCHAR2(150),
  is_desktop_link NUMBER(1) default 0 not null,
  login_img       VARCHAR2(150)
)'
;
end if;

execute immediate 'comment on column SYS_USER_SYS.user_sys_id
  is ''�ñ���궨��ǰ���û���ϵͳ��3λ���֣���9��ͷ''';
execute immediate 'comment on column SYS_USER_SYS.user_sys_name
  is ''�û���ϵͳ����''';
execute immediate 'comment on column SYS_USER_SYS.user_sys_desc
  is ''��ϸ��������ϵͳ''';
execute immediate 'comment on column SYS_USER_SYS.enabled
  is ''�궨ϵͳ�Ƿ����ã�1����,0�����ã�Ĭ��1''';
execute immediate 'comment on column SYS_USER_SYS.welcome_img
  is ''�û���ϵͳ�Ļ�ӭͼƬ''';
execute immediate 'comment on column SYS_USER_SYS.is_desktop_link
  is ''�궨���û���ϵͳ�Ƿ�֧�������ݷ�ʽ��1֧��,0��֧�֡�Ĭ��0''';
execute immediate 'comment on column SYS_USER_SYS.login_img
  is ''�û���ϵͳ�ĵ�¼ͼƬ''';
execute immediate 'alter table SYS_USER_SYS
  add constraint SYS_USER_SYS_U1 primary key (USER_SYS_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_USER_SYS_APP
';
 if i=0 then
 execute immediate '
create table SYS_USER_SYS_APP
(
  sys_id      VARCHAR2(42) not null,
  user_sys_id VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_USER_SYS_APP.sys_id
  is ''3λ���֣���1��8��ͷ''';
execute immediate 'comment on column SYS_USER_SYS_APP.user_sys_id
  is ''3λ���֣���9��ͷ''';
execute immediate 'alter table SYS_USER_SYS_APP
  add constraint SYS_USER_SYS_APP_U1 primary key (SYS_ID, USER_SYS_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_ACCOUNT
';
 if i=0 then
 execute immediate '
create table SYS_WF_ACCOUNT
(
  id                 VARCHAR2(38) not null,
  module_id          NUMBER,
  module_dispname    VARCHAR2(100),
  chr_id             VARCHAR2(38),
  chr_dispname       VARCHAR2(100),
  billtype_id        VARCHAR2(38),
  billtype_dispname  VARCHAR2(100),
  verify_set_mode    VARCHAR2(3),
  value_set_verify   VARCHAR2(100),
  value_set_evaluate VARCHAR2(100),
  set_year           NUMBER(4) not null,
  RG_CODE            VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_WF_ACCOUNT
  add constraint PRO7_SYS_WF_ACCOUNT primary key (ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_CACHE_TASKS
';
 if i=0 then
 execute immediate '
create global temporary table SYS_WF_CACHE_TASKS
(
  task_id          NUMBER(20) not null,
  wf_id            NUMBER,
  wf_table_name    VARCHAR2(38) not null,
  entity_id        VARCHAR2(42) not null,
  current_node_id  NUMBER,
  next_node_id     NUMBER,
  action_type_code VARCHAR2(38) not null,
  is_undo          NUMBER(1) not null,
  create_user      VARCHAR2(100),
  create_date      VARCHAR2(30),
  undo_user        VARCHAR2(100),
  undo_date        VARCHAR2(30),
  operation_name   VARCHAR2(42),
  operation_date   VARCHAR2(60),
  operation_remark VARCHAR2(400),
  init_money       NUMBER(16,2),
  result_money     NUMBER(16,2),
  remark           VARCHAR2(3999),
  tolly_flag       NUMBER(1) not null,
  bill_type_code   VARCHAR2(42),
  bill_id          VARCHAR2(38),
  rcid             NUMBER,
  ccid             NUMBER,
  last_ver         VARCHAR2(30),
  send_msg_date    VARCHAR2(30),
  auto_audit_date  VARCHAR2(30),
  set_month        NUMBER(2),
  update_flag      NUMBER(1),
  create_user_id   VARCHAR2(40),
  set_year         NUMBER(4) not null,
  RG_CODE          VARCHAR2(42) not null
)';
end if;

execute immediate 'comment on table SYS_WF_CACHE_TASKS
  is ''������cache���������̵ļ����Ż�''';
execute immediate 'create index SYS_WF_CACHE_TASKS_ID on SYS_WF_CACHE_TASKS (ENTITY_ID)';
execute immediate 'alter table SYS_WF_CACHE_TASKS
  add constraint SYS_WF_CACHE_TASKS_PK primary key (TASK_ID)';
  
  
  
  
select count(*) into i from user_tables where table_name='SYS_YEAR';
 if i=0 then
 execute immediate '
create table SYS_WF_COMPLETE_ITEM
(
  entity_id    VARCHAR2(42),
  bill_id      VARCHAR2(38),
  node_id      NUMBER,
  status_code  VARCHAR2(42),
  rcid         NUMBER,
  ccid         NUMBER,
  set_year     NUMBER(4) not null,
  RG_CODE      VARCHAR2(42) not null,
  MB_ID VARCHAR2(42),
  AGENCY_ID        VARCHAR2(42),
  pb_id        VARCHAR2(42),
  cb_id        VARCHAR2(42),
  ib_id        VARCHAR2(42),
  GP_agency_id    VARCHAR2(42)
)'
;
end if;

execute immediate 'create index INDEX_COM_AGENCY_ID on SYS_WF_COMPLETE_ITEM (GP_AGENCY_ID)
 ' ;
execute immediate 'create index INDEX_COM_ALL on SYS_WF_COMPLETE_ITEM (NODE_ID, SET_YEAR, RG_CODE, STATUS_CODE, CCID)
 ' ;
execute immediate 'create index INDEX_COM_BILL_ID on SYS_WF_COMPLETE_ITEM (BILL_ID)
'  ;
execute immediate 'create index INDEX_COM_CB_ID on SYS_WF_COMPLETE_ITEM (CB_ID)
 ' ;
execute immediate 'create index INDEX_COM_CCID on SYS_WF_COMPLETE_ITEM (CCID)
 ' ;
execute immediate 'create index INDEX_COM_ENTITY_ID on SYS_WF_COMPLETE_ITEM (ENTITY_ID)
 ' ;
execute immediate 'create index INDEX_COM_EN_ID on SYS_WF_COMPLETE_ITEM (AGENCY_ID)
 ' ;
execute immediate 'create index INDEX_COM_IB_ID on SYS_WF_COMPLETE_ITEM (IB_ID)
 ' ;
execute immediate 'create index INDEX_COM_MB_ID on SYS_WF_COMPLETE_ITEM (MB_ID)
 ' ;
execute immediate 'create index INDEX_COM_NODE_ID on SYS_WF_COMPLETE_ITEM (NODE_ID)
 ' ;
execute immediate 'create index INDEX_COM_PB_ID on SYS_WF_COMPLETE_ITEM (PB_ID)
 ' ;
execute immediate 'create index INDEX_COM_RCID on SYS_WF_COMPLETE_ITEM (RCID)
 ' ;
execute immediate 'create index INDEX_COM_STATUS_CODE on SYS_WF_COMPLETE_ITEM (STATUS_CODE)
 ' ;
 
 
 
select count(*) into i from user_tables where table_name='SYS_WF_COMPLETE_TASKS
';
 if i=0 then
 execute immediate '
create table SYS_WF_COMPLETE_TASKS
(
  task_id          NUMBER(20) not null,
  wf_id            NUMBER,
  wf_table_name    VARCHAR2(38) not null,
  entity_id        VARCHAR2(42) not null,
  current_node_id  NUMBER,
  next_node_id     NUMBER,
  action_type_code VARCHAR2(38) not null,
  is_undo          NUMBER(1) not null,
  create_user      VARCHAR2(100),
  create_date      VARCHAR2(30),
  undo_user        VARCHAR2(100),
  undo_date        VARCHAR2(30),
  operation_name   VARCHAR2(42),
  operation_date   VARCHAR2(60),
  operation_remark VARCHAR2(400),
  init_money       NUMBER(16,2),
  result_money     NUMBER(16,2),
  remark           VARCHAR2(3999),
  tolly_flag       NUMBER(1) not null,
  bill_type_code   VARCHAR2(42),
  bill_id          VARCHAR2(38),
  rcid             NUMBER,
  ccid             NUMBER,
  last_ver         VARCHAR2(30),
  send_msg_date    VARCHAR2(30),
  auto_audit_date  VARCHAR2(30),
  set_month        NUMBER(2),
  update_flag      NUMBER(1),
  create_user_id   VARCHAR2(40),
  set_year         NUMBER(4) not null,
  RG_CODE          VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_WF_COMPLETE_TASKS.update_flag
  is ''0����ʾ�²�������� �� 1�� ��ʾԭ��������''';
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK0 on SYS_WF_COMPLETE_TASKS (DECODE(ACTION_TYPE_CODE,''NEXT'',NEXT_NODE_ID,''BACK'',NEXT_NODE_ID,''INPUT'',NEXT_NODE_ID,CURRENT_NODE_ID))
 ' ;
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK1 on SYS_WF_COMPLETE_TASKS (WF_ID)
 ' ;
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK2 on SYS_WF_COMPLETE_TASKS (ENTITY_ID)
 ' ;
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK3 on SYS_WF_COMPLETE_TASKS (WF_TABLE_NAME)
 ' ;
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK4 on SYS_WF_COMPLETE_TASKS (CURRENT_NODE_ID)
 ' ;
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK5 on SYS_WF_COMPLETE_TASKS (NEXT_NODE_ID)
 ' ;
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK6 on SYS_WF_COMPLETE_TASKS (RCID)
 ' ;
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK7 on SYS_WF_COMPLETE_TASKS (CCID)
 ' ;
execute immediate 'create index SYS_WF_COMPLETE_TASKS_PRO2_PK8 on SYS_WF_COMPLETE_TASKS (UPDATE_FLAG)
 ' ;
execute immediate 'alter table SYS_WF_COMPLETE_TASKS
  add constraint SYS_WF_COMP_TASKS_PRO2_PK primary key (TASK_ID, SET_YEAR)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_CONDITIONS
';
 if i=0 then
 execute immediate '
create table SYS_WF_CONDITIONS
(
  condition_id   VARCHAR2(38) not null,
  condition_code VARCHAR2(42) not null,
  condition_name VARCHAR2(100),
  expression     VARCHAR2(2000),
  remark         VARCHAR2(200),
  create_user    VARCHAR2(42),
  create_date    VARCHAR2(30),
  latest_op_user VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30),
  bsh_expression VARCHAR2(4000),
  condition_type VARCHAR2(10),
  set_year       NUMBER(4) not null,
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_WF_CONDITIONS_TEMP
';
 if i=0 then
 execute immediate '
create table SYS_WF_CONDITIONS_TEMP
(
  condition_id   VARCHAR2(38) not null,
  condition_code VARCHAR2(42) not null,
  condition_name VARCHAR2(100),
  expression     VARCHAR2(2000),
  remark         VARCHAR2(200),
  create_user    VARCHAR2(42),
  create_date    VARCHAR2(30),
  latest_op_user VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  last_ver       VARCHAR2(30),
  set_year       NUMBER(4) not null,
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.condition_id
  is ''������Ψһ��ʶ''';
execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.condition_code
  is ''������CODE''';
execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.condition_name
  is ''����������''';
execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.expression
  is ''�����ı��ʽ''';
execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.remark
  is ''˵��''';
execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.create_user
  is ''������''';
execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.create_date
  is ''����ʱ��''';
execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.latest_op_user
  is ''����޸���''';
execute immediate 'comment on column SYS_WF_CONDITIONS_TEMP.latest_op_date
  is ''����޸�ʱ��''';
execute immediate 'alter table SYS_WF_CONDITIONS_TEMP
  add constraint SYS_WF_CONDITIONS_PK primary key (CONDITION_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_CONDITION_LINES
';
 if i=0 then
 execute immediate '
create table SYS_WF_CONDITION_LINES
(
  line_id             VARCHAR2(38) not null,
  condition_id        VARCHAR2(38) not null,
  operator            VARCHAR2(10),
  logic_operator      VARCHAR2(20),
  create_user         VARCHAR2(42),
  create_date         VARCHAR2(30),
  latest_op_user      VARCHAR2(42),
  latest_op_date      VARCHAR2(30),
  left_pare           VARCHAR2(31),
  right_pare          VARCHAR2(31),
  last_ver            VARCHAR2(30),
  left_paraid         VARCHAR2(38),
  right_paraid        VARCHAR2(38),
  line_sort           NUMBER(10),
  set_year            NUMBER(4) not null,
  RG_CODE             VARCHAR2(42) not null,
  para_type           VARCHAR2(2),
  left_paravaluetype  VARCHAR2(20),
  right_paravaluetype VARCHAR2(20),
  left_paraname       VARCHAR2(60),
  right_paraname      VARCHAR2(60)
)'
;
end if;

execute immediate 'comment on column SYS_WF_CONDITION_LINES.para_type
  is ''Ҫ�����:1 Ҫ�� 2 ��Ҫ��''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES.left_paravaluetype
  is ''���������:1 ���� 2 :�ַ��� 3:������ 4:С������''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES.right_paravaluetype
  is ''�Ҳ�������:1 ���� 2 :�ַ��� 3:������ 4:С������''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES.left_paraname
  is ''���������''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES.right_paraname
  is ''�Ҳ�������''';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_CONDITION_LINES_TEMP
';
 if i=0 then
 execute immediate '
create table SYS_WF_CONDITION_LINES_TEMP
(
  line_id              NUMBER(3) not null,
  condition_id         VARCHAR2(38) not null,
  condition_table_name VARCHAR2(38),
  column_name          VARCHAR2(38),
  operator             VARCHAR2(10),
  constant             VARCHAR2(60),
  logic_operator       VARCHAR2(20),
  create_user          VARCHAR2(42),
  create_date          VARCHAR2(30),
  latest_op_user       VARCHAR2(42),
  latest_op_date       VARCHAR2(30),
  left_pare            VARCHAR2(3),
  right_pare           VARCHAR2(3),
  column_type          VARCHAR2(10),
  last_ver             VARCHAR2(30),
  set_year             NUMBER(4) not null,
  RG_CODE              VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.line_id
  is ''�����е�Ψһ��ʶ''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.condition_id
  is ''������Ψһ��ʶ''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.condition_table_name
  is ''������''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.column_name
  is ''�ֶ�����''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.operator
  is ''��ϵ�����''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.constant
  is ''����''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.logic_operator
  is ''�߼������''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.create_user
  is ''������''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.create_date
  is ''����ʱ��''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.latest_op_user
  is ''����޸���''';
execute immediate 'comment on column SYS_WF_CONDITION_LINES_TEMP.latest_op_date
  is ''����޸�ʱ��''';
execute immediate 'alter table SYS_WF_CONDITION_LINES_TEMP
  add constraint SYS_WF_CONDITION_LINES_PK primary key (LINE_ID, CONDITION_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_CONDITION_PARAS
';
 if i=0 then
 execute immediate '
create table SYS_WF_CONDITION_PARAS
(
  para_id        VARCHAR2(38) not null,
  condition_id   VARCHAR2(38),
  para_desc      VARCHAR2(200),
  para_type      NUMBER(1),
  para_name      VARCHAR2(200),
  para_remark    VARCHAR2(200),
  para_valuetype VARCHAR2(38),
  fun_paras      VARCHAR2(200),
  is_shared      NUMBER(1),
  fun_id         VARCHAR2(38),
  set_year       NUMBER(4) not null,
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_WF_CURRENT_ITEM
';
 if i=0 then
 execute immediate '
create table SYS_WF_CURRENT_ITEM
(
  entity_id    VARCHAR2(42),
  bill_id      VARCHAR2(38),
  node_id      NUMBER,
  status_code  VARCHAR2(42),
  rcid         NUMBER,
  ccid         NUMBER,
  set_year     NUMBER(4) not null,
  RG_CODE      VARCHAR2(42) not null,
  MB_ID VARCHAR2(42),
  AGENCY_ID        VARCHAR2(42),
  pb_id        VARCHAR2(42),
  cb_id        VARCHAR2(42),
  ib_id        VARCHAR2(42),
  GP_agency_id    VARCHAR2(42)
)'
;
end if;

execute immediate 'create index IDX_WF_CURR_ORG_YEAR on SYS_WF_CURRENT_ITEM (RG_CODE, SET_YEAR)
 ' ;
execute immediate 'create index INDEX_CUR_AGENCY_ID on SYS_WF_CURRENT_ITEM (GP_AGENCY_ID)
  ';
execute immediate 'create index INDEX_CUR_BILL_ID on SYS_WF_CURRENT_ITEM (BILL_ID)
 ' ;
execute immediate 'create index INDEX_CUR_CB_ID on SYS_WF_CURRENT_ITEM (CB_ID)
 ' ;
execute immediate 'create index INDEX_CUR_CCID on SYS_WF_CURRENT_ITEM (CCID)
 ' ;
execute immediate 'create index INDEX_CUR_ENTITY_ID on SYS_WF_CURRENT_ITEM (ENTITY_ID)
 ' ;
execute immediate 'create index INDEX_CUR_EN_ID on SYS_WF_CURRENT_ITEM (AGENCY_ID)
 ' ;
execute immediate 'create index INDEX_CUR_IB_ID on SYS_WF_CURRENT_ITEM (IB_ID)
'  ;
execute immediate 'create index INDEX_CUR_MB_ID on SYS_WF_CURRENT_ITEM (MB_ID)
 ' ;
execute immediate 'create index INDEX_CUR_NODE_ID on SYS_WF_CURRENT_ITEM (NODE_ID)
 ' ;
execute immediate 'create index INDEX_CUR_PB_ID on SYS_WF_CURRENT_ITEM (PB_ID)
 ' ;
execute immediate 'create index INDEX_CUR_RCID on SYS_WF_CURRENT_ITEM (RCID)
 ' ;
execute immediate 'create index INDEX_CUR_STATUS_CODE on SYS_WF_CURRENT_ITEM (STATUS_CODE)
 ' ;
 
 
 
select count(*) into i from user_tables where table_name='SYS_WF_CURRENT_TASKS
';
 if i=0 then
 execute immediate '
create table SYS_WF_CURRENT_TASKS
(
  task_id          NUMBER(20) not null,
  wf_id            NUMBER,
  wf_table_name    VARCHAR2(38) not null,
  entity_id        VARCHAR2(42) not null,
  current_node_id  NUMBER,
  next_node_id     NUMBER,
  action_type_code VARCHAR2(38) not null,
  is_undo          NUMBER(1) not null,
  create_user      VARCHAR2(100),
  create_date      VARCHAR2(30),
  undo_user        VARCHAR2(100),
  undo_date        VARCHAR2(30),
  operation_name   VARCHAR2(42),
  operation_date   VARCHAR2(60),
  operation_remark VARCHAR2(400),
  init_money       NUMBER(16,2),
  result_money     NUMBER(16,2),
  remark           VARCHAR2(3999),
  tolly_flag       NUMBER(1) not null,
  bill_type_code   VARCHAR2(42),
  bill_id          VARCHAR2(38),
  rcid             NUMBER,
  ccid             NUMBER,
  last_ver         VARCHAR2(30),
  send_msg_date    VARCHAR2(30),
  auto_audit_date  VARCHAR2(30),
  set_month        NUMBER(2),
  update_flag      NUMBER(1),
  create_user_id   VARCHAR2(40),
  set_year         NUMBER(4) not null,
  RG_CODE          VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_WF_CURRENT_TASKS.update_flag
  is ''0����ʾ�²�������� �� 1�� ��ʾԭ��������''';
execute immediate 'create index SYS_WF_CURRENT_TASKS_PRO2_PK2 on SYS_WF_CURRENT_TASKS (ENTITY_ID)
  ';
execute immediate 'create index SYS_WF_CURRENT_TASKS_PRO2_PK3 on SYS_WF_CURRENT_TASKS (WF_TABLE_NAME)
  ';
execute immediate 'create index SYS_WF_CURRENT_TASKS_PRO2_PK4 on SYS_WF_CURRENT_TASKS (CURRENT_NODE_ID)
 ' ;
execute immediate 'create index SYS_WF_CURRENT_TASKS_PRO2_PK5 on SYS_WF_CURRENT_TASKS (NEXT_NODE_ID)
 ' ;
execute immediate 'create index SYS_WF_CURRENT_TASKS_PRO2_PK6 on SYS_WF_CURRENT_TASKS (RCID)
  ';
execute immediate 'create index SYS_WF_CURRENT_TASKS_PRO2_PK7 on SYS_WF_CURRENT_TASKS (CCID)
 ' ;
execute immediate 'create index SYS_WF_CURRENT_TASKS_PRO2_PK8 on SYS_WF_CURRENT_TASKS (UPDATE_FLAG)
 ' ;
execute immediate 'alter table SYS_WF_CURRENT_TASKS
  add constraint SYS_WF_CURR_TASKS_PRO2_PK primary key (TASK_ID, SET_YEAR)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_END_TASKS
';
 if i=0 then
 execute immediate '
create table SYS_WF_END_TASKS
(
  task_id          NUMBER(20) not null,
  wf_id            NUMBER,
  wf_table_name    VARCHAR2(38) not null,
  entity_id        VARCHAR2(42) not null,
  current_node_id  NUMBER,
  next_node_id     NUMBER,
  action_type_code VARCHAR2(38) not null,
  is_undo          NUMBER(1) not null,
  create_user      VARCHAR2(100),
  create_date      VARCHAR2(30),
  undo_user        VARCHAR2(100),
  undo_date        VARCHAR2(30),
  operation_name   VARCHAR2(42),
  operation_date   VARCHAR2(60),
  operation_remark VARCHAR2(400),
  init_money       NUMBER(16,2),
  result_money     NUMBER(16,2),
  remark           VARCHAR2(3999),
  tolly_flag       NUMBER(1) not null,
  bill_type_code   VARCHAR2(42),
  bill_id          VARCHAR2(38),
  rcid             NUMBER,
  ccid             NUMBER,
  last_ver         VARCHAR2(30),
  send_msg_date    VARCHAR2(30),
  auto_audit_date  VARCHAR2(30),
  set_month        NUMBER(2),
  update_flag      NUMBER(1),
  create_user_id   VARCHAR2(40),
  set_year         NUMBER(4) not null,
  RG_CODE          VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_WF_END_TASKS.update_flag
  is ''0����ʾ�²�������� �� 1�� ��ʾԭ��������''';
execute immediate 'create index SYS_WF_END_TASKS_PRO2_PK2 on SYS_WF_END_TASKS (ENTITY_ID)
  ';
execute immediate 'create index SYS_WF_END_TASKS_PRO2_PK3 on SYS_WF_END_TASKS (WF_TABLE_NAME)
 ' ;
execute immediate 'create index SYS_WF_END_TASKS_PRO2_PK4 on SYS_WF_END_TASKS (CURRENT_NODE_ID)
 ' ;
execute immediate 'create index SYS_WF_END_TASKS_PRO2_PK5 on SYS_WF_END_TASKS (NEXT_NODE_ID)
 ' ;
execute immediate 'create index SYS_WF_END_TASKS_PRO2_PK6 on SYS_WF_END_TASKS (RCID)
 ' ;
execute immediate 'create index SYS_WF_END_TASKS_PRO2_PK7 on SYS_WF_END_TASKS (CCID)
 ' ;
execute immediate 'create index SYS_WF_END_TASKS_PRO2_PK8 on SYS_WF_END_TASKS (UPDATE_FLAG)
  ';
execute immediate 'alter table SYS_WF_END_TASKS
  add constraint SYS_WF_END_TASKS_PRO2_PK primary key (TASK_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_FLOWS
';
 if i=0 then
 execute immediate '
create table SYS_WF_FLOWS
(
  wf_id          NUMBER not null,
  wf_code        VARCHAR2(42) not null,
  wf_name        VARCHAR2(100),
  wf_table_name  VARCHAR2(38) not null,
  id_column_name VARCHAR2(30) not null,
  condition_id   VARCHAR2(38),
  remark         VARCHAR2(200),
  enabled        NUMBER(1),
  create_user    VARCHAR2(42),
  create_date    VARCHAR2(30),
  latest_op_user VARCHAR2(42),
  latest_op_date VARCHAR2(30),
  set_year       NUMBER(4) not null,
  last_ver       VARCHAR2(30),
  coa_id         VARCHAR2(38),
  right_ccid     VARCHAR2(1),
  right_rcid     VARCHAR2(1),
  RG_CODE        VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_WF_FLOWS
  add constraint SYS_WF_FLOWS_PK_PRO9 primary key (WF_ID)'; 
execute immediate 'alter table SYS_WF_FLOWS
  add constraint SYS_WF_FLOWS_PK1_PRO9 unique (WF_CODE, RG_CODE, SET_YEAR)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_FLOWS_BYTE
';
 if i=0 then
 execute immediate '
create table SYS_WF_FLOWS_BYTE
(
  wf_id    NUMBER,
  wf_byte  BLOB,
  last_ver VARCHAR2(30),
  set_year NUMBER(4) not null,
  RG_CODE  VARCHAR2(42) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_WF_FLOWS_BYTE_CACHE
';
 if i=0 then
 execute immediate '
create table SYS_WF_FLOWS_BYTE_CACHE
(
  wf_id    NUMBER,
  wf_byte  BLOB,
  last_ver VARCHAR2(30),
  set_year NUMBER(4) not null,
  RG_CODE  VARCHAR2(42) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_WF_FUNCTION
';
 if i=0 then
 execute immediate '
create table SYS_WF_FUNCTION
(
  fun_id        VARCHAR2(38) not null,
  fun_classname VARCHAR2(100),
  fun_method    VARCHAR2(200),
  fun_name      VARCHAR2(200),
  fun_remark    VARCHAR2(200),
  fun_valuetype VARCHAR2(38),
  sys_id        VARCHAR2(38),
  set_year      NUMBER(4) not null,
  RG_CODE       VARCHAR2(42) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_WF_FUNCTION_PARAS
';
 if i=0 then
 execute immediate '
create table SYS_WF_FUNCTION_PARAS
(
  fun_id       VARCHAR2(38),
  fun_paraid   NUMBER(10) not null,
  fun_paraname VARCHAR2(38),
  fun_paratype VARCHAR2(38),
  fun_parasort NUMBER(1),
  set_year     NUMBER(4) not null,
  RG_CODE      VARCHAR2(42) not null
)'
;
end if;



select count(*) into i from user_tables where table_name='SYS_WF_MODULE_NODE
';
 if i=0 then
 execute immediate '
create table SYS_WF_MODULE_NODE
(
  node_id   NUMBER,
  module_id VARCHAR2(42),
  last_ver  VARCHAR2(30),
  set_year  NUMBER(4) not null,
  RG_CODE   VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index IDX_WF_MODULE_NODE_ORG_YEAR on SYS_WF_MODULE_NODE (RG_CODE, SET_YEAR)
  ';
execute immediate 'create index SYS_WF_MODULE_NODE_PRO7_PK on SYS_WF_MODULE_NODE (NODE_ID, MODULE_ID)
 ' ;

 
select count(*) into i from user_tables where table_name='SYS_WF_NODES
';
 if i=0 then
 execute immediate '
 create table SYS_WF_NODES
(
  wf_id                NUMBER,
  node_code            VARCHAR2(42),
  node_name            VARCHAR2(100),
  node_type            VARCHAR2(10),
  remark               VARCHAR2(200),
  create_user          VARCHAR2(42),
  create_date          VARCHAR2(30),
  latest_op_user       VARCHAR2(42),
  latest_op_date       VARCHAR2(30),
  node_id              NUMBER not null,
  wf_table_name        VARCHAR2(38),
  gather_flag          NUMBER(1) not null,
  id_column_name       VARCHAR2(38),
  outer_table_name     VARCHAR2(38),
  outer_column_name    VARCHAR2(38),
  relation_column_name VARCHAR2(38),
  last_ver             VARCHAR2(30),
  send_msg_flag        NUMBER(1),
  send_msg_time        NUMBER(3),
  auto_audit_flag      NUMBER(1),
  auto_audit_time      NUMBER(3),
  send_msg_time_unit   NUMBER(1),
  auto_audit_time_unit NUMBER(1),
  set_year             NUMBER(4) not null,
  RG_CODE              VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index IDX_SYS_WF_NODES_NODE_PRO9 on SYS_WF_NODES (GATHER_FLAG)
 ' ;
execute immediate 'create index IDX_SYS_WF_NODES_WF_PRO9 on SYS_WF_NODES (WF_ID)
 ' ;
execute immediate 'create index IDX_SYS_WF_NODES_WF_TABLE_PRO9 on SYS_WF_NODES (WF_TABLE_NAME)
 ' ;
execute immediate 'alter table SYS_WF_NODES
  add constraint SYS_WF_NODES_PRO9_PK primary key (NODE_ID)';
  
  
select count(*) into i from user_tables where table_name='SYS_WF_NODE_ACTION_INSPECT
';
 if i=0 then
 execute immediate '
create table SYS_WF_NODE_ACTION_INSPECT
(
  node_id          NUMBER not null,
  action_type_code VARCHAR2(38) not null,
  inspect_rule_id  VARCHAR2(38) not null,
  last_ver         VARCHAR2(30),
  set_year         NUMBER(4) not null,
  RG_CODE          VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_WF_NODE_ACTION_INSPECT
  add constraint ACTION_INSPECT_PRO_PK unique (NODE_ID, ACTION_TYPE_CODE, INSPECT_RULE_ID, RG_CODE, SET_YEAR)';

  
select count(*) into i from user_tables where table_name='SYS_WF_NODE_CONDITIONS
';
 if i=0 then
 execute immediate '
  create table SYS_WF_NODE_CONDITIONS
(
  wf_id        NUMBER,
  node_id      NUMBER,
  next_node_id NUMBER,
  condition_id VARCHAR2(38) not null,
  routing_type VARCHAR2(10) not null,
  last_ver     VARCHAR2(30),
  set_year     NUMBER(4) not null,
  RG_CODE      VARCHAR2(42) not null,
  LINE_RULE_ID VARCHAR2(42) 
)'
;
end if;

execute immediate 'create index SYS_WF_NODE_CONDITIONS_PRO9_PK on SYS_WF_NODE_CONDITIONS (WF_ID, NODE_ID, CONDITION_ID, NEXT_NODE_ID, ROUTING_TYPE)';



select count(*) into i from user_tables where table_name='SYS_WF_NODE_RELATIONS
';
 if i=0 then
 execute immediate '
create table SYS_WF_NODE_RELATIONS
(
  wf_id         NUMBER,
  node_id       NUMBER,
  next_node_id  NUMBER,
  relation_type VARCHAR2(10) not null,
  last_ver      VARCHAR2(30),
  set_year      NUMBER(4) not null,
  RG_CODE       VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index SYS_WF_NODE_RELATIONS_PRO9_PK on SYS_WF_NODE_RELATIONS (WF_ID, NODE_ID, NEXT_NODE_ID, RELATION_TYPE)
 ' ;
 
 
 
select count(*) into i from user_tables where table_name='SYS_WF_NODE_TOLLY_ACTION_TYPE
';
 if i=0 then
 execute immediate '
create table SYS_WF_NODE_TOLLY_ACTION_TYPE
(
  node_id          NUMBER,
  action_type_code VARCHAR2(42) not null,
  tolly_flag       NUMBER(1) not null,
  last_ver         VARCHAR2(30),
  set_year         NUMBER(4) not null,
  RG_CODE          VARCHAR2(42) not null
)'
;
end if;

execute immediate 'create index TOLLY_ACTION_TYPE_PRO_PK on SYS_WF_NODE_TOLLY_ACTION_TYPE (NODE_ID, ACTION_TYPE_CODE)
  ';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_ROLE_NODE
';
 if i=0 then
 execute immediate '
create table SYS_WF_ROLE_NODE
(
  node_id  NUMBER not null,
  role_id  VARCHAR2(42) not null,
  last_ver VARCHAR2(30),
  set_year NUMBER(4) not null,
  RG_CODE  VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_WF_ROLE_NODE
  add constraint SYS_WF_ROLE_NODE_PRO5_PK unique (NODE_ID, ROLE_ID, RG_CODE, SET_YEAR)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_RULE
';
 if i=0 then
 execute immediate '
create table SYS_WF_RULE
(
  wf_id    NUMBER not null,
  rule_id  NUMBER not null,
  last_ver VARCHAR2(30),
  set_year NUMBER(4) not null,
  RG_CODE  VARCHAR2(42) not null
)'
;
end if;

execute immediate 'alter table SYS_WF_RULE
  add constraint SYS_WF_RULE_PRO_PK unique (WF_ID, RULE_ID, RG_CODE, SET_YEAR)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_TASK_INSPECT_RULE
';
 if i=0 then
 execute immediate '
create table SYS_WF_TASK_INSPECT_RULE
(
  rule_id              NUMBER not null,
  rule_name            VARCHAR2(100),
  wf_id                NUMBER,
  node_id              NUMBER,
  status_code          VARCHAR2(20),
  delay_work_days      NUMBER,
  enabled              NUMBER,
  is_sysmessage_notice NUMBER,
  is_sms_notice        NUMBER,
  set_year             NUMBER(4) not null,
  RG_CODE              VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.rule_id
  is ''����ID''';
execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.rule_name
  is ''��������''';
execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.wf_id
  is ''������ID''';
execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.node_id
  is ''�ڵ�ID''';
execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.status_code
  is ''����״̬''';
execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.delay_work_days
  is ''�ӳٹ�����''';
execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.enabled
  is ''�Ƿ�����''';
execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.is_sysmessage_notice
  is ''�Ƿ�ͨ��ϵͳ��Ϣ֪ͨ''';
execute immediate 'comment on column SYS_WF_TASK_INSPECT_RULE.is_sms_notice
  is ''�Ƿ�ͨ������֪ͨ''';
execute immediate 'alter table SYS_WF_TASK_INSPECT_RULE
  add constraint RULE_ID primary key (RULE_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WF_TASK_ROUTING
';
 if i=0 then
 execute immediate '
create table SYS_WF_TASK_ROUTING
(
  task_id      NUMBER(20) not null,
  next_task_id NUMBER(20) not null,
  last_ver     VARCHAR2(30),
  set_year     NUMBER(4) not null,
  RG_CODE      VARCHAR2(42) not null
)'
;
end if;

execute immediate 'comment on column SYS_WF_TASK_ROUTING.task_id
  is ''��ǰ�����Ψһ��ʶ''';
execute immediate 'comment on column SYS_WF_TASK_ROUTING.next_task_id
  is ''��һ�����Ψһ��ʶ''';
execute immediate 'create index SYS_WF_TASK_ROUTING_01 on SYS_WF_TASK_ROUTING (NEXT_TASK_ID)
 ' ;
execute immediate 'alter table SYS_WF_TASK_ROUTING
  add constraint SYS_WF_TASK_ROUTING_PK primary key (TASK_ID, NEXT_TASK_ID)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_WORK_DAY
';
 if i=0 then
 execute immediate '
create table SYS_WORK_DAY
(
  set_year   NUMBER(4) default 0 not null,
  date_month VARCHAR2(20) default 0 not null,
  date_day   VARCHAR2(2) default 0 not null,
  week       NUMBER default 1,
  date_type  NUMBER default 0,
  china_date VARCHAR2(20),
  last_ver   VARCHAR2(30)
)'
;
end if;

execute immediate 'comment on column SYS_WORK_DAY.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_WORK_DAY.date_month
  is ''�·�''';
execute immediate 'comment on column SYS_WORK_DAY.date_day
  is ''���ڣ���¼��''';
execute immediate 'comment on column SYS_WORK_DAY.week
  is ''���ڹ������ڵı�ʶ��1/����һ��2/���ڶ���3/��������4/�����ģ�5/�����壻6/��������7/�����졣''';
execute immediate 'comment on column SYS_WORK_DAY.date_type
  is ''�Ƿ�Ϊ������0/�����գ�1/��Ϣ�ա�''';
execute immediate 'comment on column SYS_WORK_DAY.china_date
  is ''ũ�����ڣ������ֶ�''';
execute immediate 'alter table SYS_WORK_DAY
  add constraint PRI_WORK_DAY primary key (SET_YEAR, DATE_MONTH, DATE_DAY)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_YEAR
';
 if i=0 then
 execute immediate '
create table SYS_YEAR
(
  set_year    NUMBER(4) not null,
  year_name   VARCHAR2(42),
  enabled     NUMBER(1) default 1 not null,
  init_flag   NUMBER(1) default 1 not null,
  start_date  VARCHAR2(20) default 2006-01-01,
  end_date    VARCHAR2(20) default 2006-12-31,
  last_ver    VARCHAR2(30),
  year_status NUMBER(1) default 1
)'
;
end if;

execute immediate 'comment on column SYS_YEAR.set_year
  is ''ҵ�����''';
execute immediate 'comment on column SYS_YEAR.year_name
  is ''ҵ�������''';
execute immediate 'comment on column SYS_YEAR.enabled
  is ''�궨ҵ������Ƿ�����(1����,0������)''';
execute immediate 'comment on column SYS_YEAR.init_flag
  is ''�궨��ҵ������Ƿ��ʼ��(1��ʼ�����,0δ���г�ʼ��)''';
execute immediate 'comment on column SYS_YEAR.start_date
  is ''��ʼ����''';
execute immediate 'comment on column SYS_YEAR.end_date
  is ''��ֹ����''';
execute immediate 'comment on column SYS_YEAR.year_status
  is ''1�������� 2�������� 3���ر���''';
execute immediate 'alter table SYS_YEAR
  add constraint SYS_YEAR_PK primary key (SET_YEAR)';
  
  
  
select count(*) into i from user_tables where table_name='SYS_YEAR_OP_CUR_STATUS
';
 if i=0 then
 execute immediate '
create table SYS_YEAR_OP_CUR_STATUS
(
  op_id     NUMBER(5) not null,
  op_name   VARCHAR2(38),
  op_status NUMBER(5) default 0,
  RG_CODE   VARCHAR2(42) not null,
  set_year  NUMBER(4) not null
)'
;
end if;

execute immediate 'comment on column SYS_YEAR_OP_CUR_STATUS.op_id
  is ''���������˵����
 1��һ����ʼ����������⣻
 2: һ����ʼ��ִ�нű���
 3�������飨��;�ƻ���֧����
 4���������
 5���ƻ����ࡢ��Ч
 6���ɹ����ࡢ��Ч
 7�����ʽ���
 8��ָ����ࡢ��Ч
 ''';
execute immediate 'comment on column SYS_YEAR_OP_CUR_STATUS.op_name
  is ''����������˵����
 һ����ʼ����������⣻
 һ����ʼ��ִ�нű���
 �����飨��;�ƻ���֧����
 �������
 �ƻ����ࡢ��Ч
 �ɹ����ࡢ��Ч
 ���ʽ���
 ָ����ࡢ��Ч
 ''';
execute immediate 'comment on column SYS_YEAR_OP_CUR_STATUS.op_status
  is ''0��δ��ʼ��
 1����ɣ�
 ''';
execute immediate 'alter table SYS_YEAR_OP_CUR_STATUS
  add constraint SYS_YEAR_OP_CUR_STATUS_KEY primary key (OP_ID, RG_CODE, SET_YEAR)';

select count(*) into i from user_tables where table_name='ELE_BUDGET_ADJUST';
 if i=0 then
 execute immediate '
create table ELE_BUDGET_ADJUST
(
  SET_YEAR       NUMBER(4) not null,
  CHR_ID         VARCHAR2(38) not null,
  CHR_CODE       VARCHAR2(42),
  DISP_CODE      VARCHAR2(42) not null,
  CHR_NAME       VARCHAR2(60) not null,
  LEVEL_NUM      NUMBER(2) default 0 not null,
  IS_LEAF        NUMBER(1) default 0 not null,
  ENABLED        NUMBER(1) default 1 not null,
  CREATE_DATE    VARCHAR2(30),
  CREATE_USER    VARCHAR2(42),
  LATEST_OP_DATE VARCHAR2(30) not null,
  IS_DELETED     NUMBER default 0 not null,
  LATEST_OP_USER VARCHAR2(42),
  LAST_VER       VARCHAR2(30),
  CHR_CODE1      VARCHAR2(42),
  CHR_CODE2      VARCHAR2(42),
  CHR_CODE3      VARCHAR2(42),
  CHR_CODE4      VARCHAR2(42),
  CHR_CODE5      VARCHAR2(42),
  RG_CODE        VARCHAR2(42) not null,
  PARENT_ID      VARCHAR2(38),
  CHR_ID1        VARCHAR2(38),
  CHR_ID2        VARCHAR2(38),
  CHR_ID3        VARCHAR2(38),
  CHR_ID4        VARCHAR2(38),
  CHR_ID5        VARCHAR2(38)
)'
;
end if;
execute immediate 'comment on column ELE_BUDGET_ADJUST.SET_YEAR is ''��¼��ǰҵ�����''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_ID is ''Ψһ��ʶ����Ҫ����Ϣ��ʹ��Guid����ʵ��Ψһ������''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_CODE is ''������ʾ����ӡ��Ҫ�����ı�����ʾ''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.DISP_CODE is ''Ϊ��ϵͳԤ���ֶ�''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_NAME is ''������ʾ����ӡ��Ҫ������������ʾ''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.LEVEL_NUM is ''��ʾ��ǰ��Ҫ�صļ���''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.IS_LEAF is ''�궨�Ƿ�׼�Ҷ�ڵ�''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.ENABLED is ''�궨��Ҫ���Ƿ�����''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CREATE_DATE is ''����ʱ�� YYYY-MM-DD HH:MM:SS''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CREATE_USER is ''�����û�''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.LATEST_OP_DATE is ''����޸�ʱ�䣺 YYYY-MM-DD HH:MM:SS''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.IS_DELETED is ''�궨�Ƿ�ɾ��''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.LATEST_OP_USER is ''����޸��û�''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.LAST_VER is ''���汾�� YYYY-MM-DD HH:MM:SS''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_CODE1 is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_CODE2 is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_CODE3 is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_CODE4 is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_CODE5 is ''���ڲ�ѯͳ�����٣���¼������CODE''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.RG_CODE is ''������ά����Ҫ�����ݣ�ʵ�ֶ����򡢶����ͬʱ������''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.PARENT_ID is ''��¼�丸��ID''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_ID1 is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_ID2 is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_ID3 is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_ID4 is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'comment on column ELE_BUDGET_ADJUST.CHR_ID5 is ''���ڲ�ѯͳ�����٣���¼������ID''';
execute immediate 'alter table ELE_BUDGET_ADJUST add constraint ELE_BUDGET_ADJUST_PK primary key (CHR_ID, SET_YEAR, RG_CODE)';
execute immediate 'create index ELE_BUDGET_ADJUST_N1 on ELE_BUDGET_ADJUST (SET_YEAR)';
execute immediate 'create index ELE_BUDGET_ADJUST_N2 on ELE_BUDGET_ADJUST (CHR_CODE)';


select count(*) into i from user_tables where table_name='SYS_USER_COMMON_MENU';
 if i=0 then
 execute immediate '
create table SYS_USER_COMMON_MENU
(
  user_id  VARCHAR2(42),
  roleguid VARCHAR2(42),
  menuguid VARCHAR2(42),
  img_src  VARCHAR2(60),
  set_year NUMBER,
  rg_code  VARCHAR2(60),
  morder   NUMBER
)';
end if;
execute immediate 'comment on column SYS_USER_COMMON_MENU.user_id
  is ''�û�ID''';
execute immediate 'comment on column SYS_USER_COMMON_MENU.roleguid
  is ''��ɫ''';
execute immediate 'comment on column SYS_USER_COMMON_MENU.menuguid
  is ''�˵�ID''';
execute immediate 'comment on column SYS_USER_COMMON_MENU.img_src
  is ''ͼƬ��Դ��ַ''';
execute immediate 'comment on column SYS_USER_COMMON_MENU.set_year
  is ''���''';
execute immediate 'comment on column SYS_USER_COMMON_MENU.rg_code
  is ''����''';
execute immediate 'comment on column SYS_USER_COMMON_MENU.morder
  is ''��ʾ˳��''';

select count(*) into i from user_tables where table_name='REPORTCY_MANAGER';
 if i=0 then
 execute immediate '
create table REPORTCY_MANAGER
(
  report_id   VARCHAR2(38) not null,
  report_code VARCHAR2(100),
  report_name VARCHAR2(500),
  report_type VARCHAR2(100),
  report_sort VARCHAR2(100),
  create_date VARCHAR2(20),
  modify_date VARCHAR2(20) default to_char(sysdate,''yyyy-mm-dd hh24:mi:ss''),
  enable      NUMBER(1) default 1,
  rg_code     VARCHAR2(42) not null
)';
end if;
execute immediate 'comment on table REPORTCY_MANAGER
  is ''�°汨���ߵı���ע���.''';
execute immediate 'comment on column REPORTCY_MANAGER.report_id
  is ''����,����Ωһid''';
execute immediate 'comment on column REPORTCY_MANAGER.report_code
  is ''�����ֶ�.��ʱ����''';
execute immediate 'comment on column REPORTCY_MANAGER.report_name
  is ''������������''';
execute immediate 'comment on column REPORTCY_MANAGER.report_type
  is ''��������,�ɱ���������Զ�����,�������޸�.''';
execute immediate 'comment on column REPORTCY_MANAGER.report_sort
  is ''�������,�����Զ���.''';
execute immediate 'comment on column REPORTCY_MANAGER.create_date
  is ''����������''';
execute immediate 'comment on column REPORTCY_MANAGER.modify_date
  is ''�����޸�����''';
execute immediate 'comment on column REPORTCY_MANAGER.enable
  is ''�Ƿ�����:0������,1����''';

execute immediate 'alter table REPORTCY_MANAGER
  add constraint PK_REPORT_MANAGER primary key (REPORT_ID, RG_CODE)';
execute immediate 'alter index PK_REPORT_MANAGER nologging';


 
select count(*) into i from user_tables where table_name='TEMP_DATE
';
 if i=0 then
 execute immediate '
create table TEMP_DATE
(
  day NUMBER
)'
;
end if;

select count(*) into i from user_tables where table_name='ELE_ACCOUNT_TYPE';
 if i=0 then
 execute immediate '
create table ELE_ACCOUNT_TYPE
(
  chr_id         VARCHAR2(38) not null,
  chr_code       VARCHAR2(42),
  disp_code      VARCHAR2(38),
  chr_name       VARCHAR2(38),
  owner_code     NUMBER(1),
  owner_field    VARCHAR2(42),
  bank_field     VARCHAR2(42),
  ele_source     VARCHAR2(42),
  level_num      NUMBER(2),
  is_leaf        NUMBER(1),
  parent_id      VARCHAR2(38),
  owner_type_id  VARCHAR2(38),
  bank_type_id   VARCHAR2(38),
  phiscal_source VARCHAR2(38),
  ele_code       VARCHAR2(20)
)';
end if;
-- Add comments to the columns 
execute immediate 'comment on column ELE_ACCOUNT_TYPE.chr_id
  is ''����''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.chr_code
  is ''����''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.disp_code
  is ''��ʾ����''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.chr_name
  is ''��ʾ����''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.owner_code
  is ''��������''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.owner_field
  is ''�����ֶ�''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.bank_field
  is ''���������ֶ�''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.ele_source
  is ''����������Դ''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.parent_id
  is ''���ڵ�id''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.owner_type_id
  is ''��������id''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.bank_type_id
  is ''��������id''';
execute immediate 'comment on column ELE_ACCOUNT_TYPE.phiscal_source
  is ''��Ӧ�����������Դ''';
-- Create/Recreate primary, unique and foreign key constraints 
execute immediate 'alter table ELE_ACCOUNT_TYPE
  add constraint PRI_ELE_ACCOUNT_TYPE primary key (CHR_ID)';
execute immediate 'alter index PRI_ELE_ACCOUNT_TYPE nologging';


select count(*) into i from user_tables where table_name='SYS_WF_RULE_MONITOR
';
 if i=0 then
 execute immediate '
create table SYS_WF_RULE_MONITOR
(
  LINE_RULE_ID      VARCHAR2(38) not null,
  CLASS_NAME     VARCHAR2(100) not null,
  PARA_NAME VARCHAR2(100) not null,
  LAST_VER          VARCHAR2(30),
  SET_YEAR          NUMBER(4) not null,
  RG_CODE           VARCHAR2(42) not null
)'
;
end if;
-- Add comments to the columns 
execute immediate 'comment on column SYS_WF_RULE_MONITOR.LINE_RULE_ID
  is ''��ת��''';
execute immediate 'comment on column SYS_WF_RULE_MONITOR.CLASS_NAME
  is ''������''';
execute immediate 'comment on column SYS_WF_RULE_MONITOR.PARA_NAME
  is ''����''';
  
-- Create table
select count(*) into i from user_tables where table_name='SYS_WF_LINE_RULE
';
 if i=0 then
 execute immediate '
create table SYS_WF_LINE_RULE
(
  LINE_RULE_ID VARCHAR2(38) not null,
  RULE_ID      VARCHAR2(20) not null,
  RULE_ORDER   NUMBER(2) not null,
  LAST_VER     VARCHAR2(30),
  SET_YEAR     NUMBER(4) not null,
  RG_CODE      VARCHAR2(42) not null
)'
;
end if;
-- Add comments to the columns 
execute immediate 'comment on column SYS_WF_LINE_RULE.LINE_RULE_ID
  is ''��ת��''';
execute immediate 'comment on column SYS_WF_LINE_RULE.RULE_ID
  is ''����ID''';
execute immediate 'comment on column SYS_WF_LINE_RULE.RULE_ORDER
  is ''�������''';  
  
-- Create table
select count(*) into i from user_tables where table_name='SYS_WF_EXTEND_RULE
';
 if i=0 then
 execute immediate '
create table SYS_WF_EXTEND_RULE
(
  SYS_TYPE         VARCHAR2(20) not null,
  RULE_TYPE        NUMBER(2) not null,
  RULE_ID          VARCHAR2(20) not null,
  RULE_NAME        VARCHAR2(200) not null,
  RULE_CONTENT     VARCHAR2(2000) not null,
  RULE_FIELD       VARCHAR2(20),
  RULE_EXTEND_FLAG NUMBER(2),
  IS_ENABLED       NUMBER(1) default 1,
  SET_YEAR         NUMBER(4),
  RG_CODE          VARCHAR2(20)
)'
;
end if;
-- Add comments to the columns 
execute immediate 'comment on column SYS_WF_EXTEND_RULE.SYS_TYPE
  is ''ϵͳ��ʶ,FB:Ԥ�����PRJ:��Ŀ�⣬BAS����̬��Ա''';
execute immediate 'comment on column SYS_WF_EXTEND_RULE.RULE_TYPE
  is ''��������,1��������2��ִ��SQL��3����˹�ʽID��4���������ͣ�5�����������6���༭������7���������''';
execute immediate 'comment on column SYS_WF_EXTEND_RULE.RULE_ID
  is ''�����ʶ,��ˮ�ţ���0001��ʼ''';
execute immediate 'comment on column SYS_WF_EXTEND_RULE.RULE_NAME
  is ''��������''';
execute immediate 'comment on column SYS_WF_EXTEND_RULE.RULE_CONTENT
  is ''��������''';
execute immediate 'comment on column SYS_WF_EXTEND_RULE.RULE_FIELD
  is ''����Ŀ����,��������ԴԤ��ϵͳ''';
execute immediate 'comment on column SYS_WF_EXTEND_RULE.RULE_EXTEND_FLAG
  is ''��չ�����ʶ,��������ԴԤ��ϵͳ''';
execute immediate 'comment on column SYS_WF_EXTEND_RULE.IS_ENABLED
  is ''���ñ�ʶ��0�������ã�1������''';  

-- Create table
select count(*) into i from user_tables where table_name='SYS_ATTACH_FILE
';
 if i=0 then
 execute immediate '
create table SYS_ATTACH_FILE
(
  ATTACH_ID   VARCHAR2(60) not null,
  ATTACH_CODE VARCHAR2(60) not null,
  ATTACH_NAME VARCHAR2(255),
  ATTACH_TYPE VARCHAR2(20),
  ATTACH_PATH VARCHAR2(255),
  STATUS      CHAR(1) default 0 not null,
  APPID       VARCHAR2(60),
  SET_YEAR        CHAR(4),
  RG_CODE     VARCHAR2(60),
  REMARK      VARCHAR2(255),
  CREATE_BY   VARCHAR2(60),
  CREATE_TIME VARCHAR2(30),
  UPDATE_BY   VARCHAR2(60),
  UPDATE_TIME VARCHAR2(30),
  EXT1        VARCHAR2(255),
  EXT2        VARCHAR2(255),
  EXT3        VARCHAR2(255)
)';
end if;
execute immediate 'comment on column SYS_ATTACH_FILE.ATTACH_ID
  is ''����ID������''';
execute immediate 'comment on column SYS_ATTACH_FILE.ATTACH_CODE
  is ''�������''';
execute immediate 'comment on column SYS_ATTACH_FILE.ATTACH_NAME
  is ''��������''';
execute immediate 'comment on column SYS_ATTACH_FILE.ATTACH_TYPE
  is ''�������ͣ�doc��pdf''';
execute immediate 'comment on column SYS_ATTACH_FILE.ATTACH_PATH
  is ''����·��''';
execute immediate 'comment on column SYS_ATTACH_FILE.STATUS
  is ''����״̬��0��Ĭ�ϣ�1��ɾ��''';
execute immediate 'comment on column SYS_ATTACH_FILE.APPID
  is ''��ϵͳID''';
execute immediate 'comment on column SYS_ATTACH_FILE.SET_YEAR
  is ''���''';
execute immediate 'comment on column SYS_ATTACH_FILE.RG_CODE
  is ''����''';
execute immediate 'comment on column SYS_ATTACH_FILE.REMARK
  is ''����˵��''';
execute immediate 'comment on column SYS_ATTACH_FILE.CREATE_BY
  is ''������ID''';
execute immediate 'comment on column SYS_ATTACH_FILE.CREATE_TIME
  is ''����ʱ�䣬��ʽ��yyyy-MM-dd HH:mm:ss''';
execute immediate 'comment on column SYS_ATTACH_FILE.UPDATE_BY
  is ''������ID''';
execute immediate 'comment on column SYS_ATTACH_FILE.UPDATE_TIME
  is ''����ʱ�䣬��ʽ��yyyy-MM-dd HH:mm:ss''';
execute immediate 'comment on column SYS_ATTACH_FILE.EXT1
  is ''Ԥ���ֶ�''';
execute immediate 'comment on column SYS_ATTACH_FILE.EXT2
  is ''Ԥ���ֶ�''';
execute immediate 'comment on column SYS_ATTACH_FILE.EXT3
  is ''Ԥ���ֶ�''';
-- Create/Recreate primary, unique and foreign key constraints 
execute immediate 'alter table SYS_ATTACH_FILE
  add primary key (ATTACH_ID)';

select count(*) into i from user_tables where table_name='SYS_ATTACH_DB
';
 if i=0 then
 execute immediate '
create table SYS_ATTACH_DB
(
  ATTACH_ID   VARCHAR2(60) not null,
  ATTACH_CODE VARCHAR2(60) not null,
  ATTACH_NAME VARCHAR2(255),
  ATTACH_TYPE VARCHAR2(20),
  ATTACH_OB   BLOB,
  STATUS      CHAR(1) default 0,
  APPID       VARCHAR2(60),
  SET_YEAR        CHAR(4),
  RG_CODE     VARCHAR2(60),
  REMARK      VARCHAR2(255),
  CREATE_BY   VARCHAR2(60),
  CREATE_TIME VARCHAR2(30),
  UPDATE_BY   VARCHAR2(60),
  UPDATE_TIME VARCHAR2(30),
  EXT1        VARCHAR2(255),
  EXT2        VARCHAR2(255),
  EXT3        VARCHAR2(255)
)';
end if;

-- Add comments to the columns 
execute immediate 'comment on column SYS_ATTACH_DB.ATTACH_ID
  is ''����ID������ID''';
execute immediate 'comment on column SYS_ATTACH_DB.ATTACH_CODE
  is ''�������''';
execute immediate 'comment on column SYS_ATTACH_DB.ATTACH_NAME
  is ''��������''';
execute immediate 'comment on column SYS_ATTACH_DB.ATTACH_TYPE
  is ''��������''';
execute immediate 'comment on column SYS_ATTACH_DB.ATTACH_OB
  is ''����''';
execute immediate 'comment on column SYS_ATTACH_DB.STATUS
  is ''����״̬��0:Ĭ�ϣ�1:ɾ��''';
execute immediate 'comment on column SYS_ATTACH_DB.APPID
  is ''��ϵͳID''';
execute immediate 'comment on column SYS_ATTACH_DB.SET_YEAR
  is ''���''';
execute immediate 'comment on column SYS_ATTACH_DB.RG_CODE
  is ''����''';
execute immediate 'comment on column SYS_ATTACH_DB.REMARK
  is ''����˵��''';
execute immediate 'comment on column SYS_ATTACH_DB.CREATE_BY
  is ''������''';
execute immediate 'comment on column SYS_ATTACH_DB.CREATE_TIME
  is ''����ʱ��''';
execute immediate 'comment on column SYS_ATTACH_DB.UPDATE_BY
  is ''������''';
execute immediate 'comment on column SYS_ATTACH_DB.UPDATE_TIME
  is ''����ʱ��''';
execute immediate 'comment on column SYS_ATTACH_DB.EXT1
  is ''Ԥ���ֶ�''';
execute immediate 'comment on column SYS_ATTACH_DB.EXT2
  is ''Ԥ���ֶ�''';
execute immediate 'comment on column SYS_ATTACH_DB.EXT3
  is ''Ԥ���ֶ�''';
-- Create/Recreate primary, unique and foreign key constraints 
execute immediate 'alter table SYS_ATTACH_DB
  add primary key (ATTACH_ID)';

select count(*) into i from user_tables where table_name='SYS_CATEGORY
';
 if i=0 then
 execute immediate '
create table SYS_CATEGORY
(
  CATEGORY_ID   VARCHAR2(60) not null,
  CATEGORY_CODE VARCHAR2(60) not null,
  CATEGORY_NAME VARCHAR2(255),
  APPID         VARCHAR2(60),
  SET_YEAR          CHAR(4),
  RG_CODE       VARCHAR2(60),
  REMARK        VARCHAR2(255),
  CREATE_BY     VARCHAR2(60),
  CREATE_TIME   VARCHAR2(30),
  UPDATE_BY     VARCHAR2(60),
  UPDATE_TIME   VARCHAR2(30),
  EXT1          VARCHAR2(255),
  EXT2          VARCHAR2(255),
  EXT3          VARCHAR2(255)
)';
end if;

-- Add comments to the columns 
execute immediate 'comment on column SYS_CATEGORY.CATEGORY_ID
  is ''����ID������ID''';
execute immediate 'comment on column SYS_CATEGORY.CATEGORY_CODE
  is ''������''';
execute immediate 'comment on column SYS_CATEGORY.CATEGORY_NAME
  is ''��������''';
execute immediate 'comment on column SYS_CATEGORY.SET_YEAR
  is ''���''';
execute immediate 'comment on column SYS_CATEGORY.RG_CODE
  is ''����''';
execute immediate 'comment on column SYS_CATEGORY.REMARK
  is ''����˵��''';
execute immediate 'comment on column SYS_CATEGORY.CREATE_BY
  is ''c������''';
execute immediate 'comment on column SYS_CATEGORY.CREATE_TIME
  is ''����ʱ��''';
execute immediate 'comment on column SYS_CATEGORY.UPDATE_BY
  is ''������''';
execute immediate 'comment on column SYS_CATEGORY.UPDATE_TIME
  is ''����ʱ��''';
execute immediate 'comment on column SYS_CATEGORY.EXT1
  is ''Ԥ���ֶ�''';
execute immediate 'comment on column SYS_CATEGORY.EXT2
  is ''Ԥ���ֶ�''';
execute immediate 'comment on column SYS_CATEGORY.EXT3
  is ''Ԥ���ֶ�''';
-- Create/Recreate primary, unique and foreign key constraints 
execute immediate 'alter table SYS_CATEGORY
  add primary key (CATEGORY_ID)';

select count(*) into i from user_tables where table_name='SYS_ATTACH_CATEGORY
';
 if i=0 then
 execute immediate '
create table SYS_ATTACH_CATEGORY
(
  ID          VARCHAR2(60) not null,
  ATTACH_ID   VARCHAR2(60) not null,
  CATEGORY_ID VARCHAR2(60) not null
)';
end if;
execute immediate 'comment on column SYS_ATTACH_CATEGORY.ID
  is ''����ID''';
execute immediate 'comment on column SYS_ATTACH_CATEGORY.ATTACH_ID
  is ''����ID''';
execute immediate 'comment on column SYS_ATTACH_CATEGORY.CATEGORY_ID
  is ''����ID''';
-- Create/Recreate primary, unique and foreign key constraints 
execute immediate 'alter table SYS_ATTACH_CATEGORY
  add primary key (ID)
  using index';


select count(*) into i from user_tables where table_name='SYS_WF_RULE_NODE
';
 if i=0 then
 execute immediate 'create table SYS_WF_RULE_NODE
(
  NODE_ID  NUMBER,
  RULE_ID  VARCHAR2(20),
  LAST_VER VARCHAR2(30),
  SET_YEAR NUMBER(4) not null,
  RG_CODE  VARCHAR2(42) not null
)';
end if;
-- Add comments to the columns 
execute immediate 'comment on column SYS_WF_RULE_NODE.NODE_ID
  is ''�ڵ�id''';
execute immediate 'comment on column SYS_WF_RULE_NODE.RULE_ID
  is ''����id''';
-- Create/Recreate indexes 
execute immediate 'create index SYS_WF_RULE_NODE_PRO7_PK on SYS_WF_RULE_NODE (NODE_ID, RULE_ID,SET_YEAR, RG_CODE)';

select count(*) into i from user_tables where table_name='SYS_BILLNO_DELETED';
if i=0 then
 execute immediate 'create table SYS_BILLNO_DELETED
(
  id            VARCHAR2(38) not null,
  billtype_code VARCHAR2(38) not null,
  bill_no       VARCHAR2(42) not null,
  set_year      NUMBER(4),
  rg_code       VARCHAR2(42)
)';
end if;
execute immediate 'comment on column SYS_BILLNO_DELETED.id  is ''ID''';
execute immediate 'comment on column SYS_BILLNO_DELETED.billtype_code  is ''�������ͱ���''';
execute immediate 'comment on column SYS_BILLNO_DELETED.bill_no  is ''ƾ֤���''';
execute immediate 'comment on column SYS_BILLNO_DELETED.set_year  is ''���''';
execute immediate 'comment on column SYS_BILLNO_DELETED.rg_code  is ''����''';
