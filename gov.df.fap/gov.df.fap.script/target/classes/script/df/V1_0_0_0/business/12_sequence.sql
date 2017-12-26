i integer;
begin
  select count(1) into i from user_sequences t where t.sequence_name='F_NUMBERID';
  if i=0 then
  execute immediate 'create sequence F_NUMBERID
minvalue 1
maxvalue 999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='MSG_SEQ_LOG_ID';
  if i=0 then
  execute immediate 'create sequence MSG_SEQ_LOG_ID
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='MSG_SEQ_MSG_ENTITY_ID';
  if i=0 then
  execute immediate 'create sequence MSG_SEQ_MSG_ENTITY_ID
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='MSG_SEQ_RULE_ID';
  if i=0 then
  execute immediate 'create sequence MSG_SEQ_RULE_ID
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='MSG_SEQ_SERIALIZE_OBJ_ID';
  if i=0 then
  execute immediate 'create sequence MSG_SEQ_SERIALIZE_OBJ_ID
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='MSG_SEQ_TASK_ID';
if i=0 then
execute immediate 'create sequence MSG_SEQ_TASK_ID
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='MSG_SEQ_USER_GROUP_ID';
  if i=0 then
  execute immediate 'create sequence MSG_SEQ_USER_GROUP_ID
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_BIS_ID';
  if i=0 then
  execute immediate 'create sequence SEQ_BIS_ID
minvalue 1
maxvalue 999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_CCID_ORDER';
  if i=0 then
  execute immediate 'create sequence SEQ_CCID_ORDER
minvalue 1
maxvalue 99999999
start with 46013
increment by 1
cache 5
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_ELE_CHR_ID';
  if i=0 then
  execute immediate 'create sequence SEQ_ELE_CHR_ID
minvalue 1
maxvalue 99999999
start with 1
increment by 1
cache 5
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_GL_BUSVOU_ACCTMDL';
  if i=0 then
  execute immediate 'create sequence SEQ_GL_BUSVOU_ACCTMDL
minvalue 1
maxvalue 9999999999
start with 520
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_GL_BUSVOU_TYPE';
  if i=0 then
  execute immediate 'create sequence SEQ_GL_BUSVOU_TYPE
minvalue 1
maxvalue 99999999
start with 1383
increment by 1
cache 20
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_GL_COA';
if i=0 then
execute immediate'create sequence SEQ_GL_COA
minvalue 1
maxvalue 99999999
start with 212
increment by 1
cache 20
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_GL_PATCH_VOUCHER_NO';
  if i=0 then
  execute immediate 'create sequence SEQ_GL_PATCH_VOUCHER_NO
minvalue 1
maxvalue 99999999
start with 391
increment by 1
cache 5
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_NODE';
  if i=0 then
  execute immediate 'create sequence SEQ_NODE
minvalue 1
maxvalue 9999999999999999999999999999
start with 4351
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_OTHER_ID';
  if i=0 then
  execute immediate 'create sequence SEQ_OTHER_ID
minvalue 1
maxvalue 99999999
start with 1200
increment by 1
cache 5
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_RCID';
  if i=0 then
  execute immediate 'create sequence SEQ_RCID
minvalue 1
maxvalue 99999999
start with 16
increment by 1
cache 5
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_SERVICE_PACKAGE_NO';
  if i=0 then
  execute immediate 'create sequence SEQ_SERVICE_PACKAGE_NO
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_SYS_AUTOTASK';
  if i=0 then
  execute immediate 'create sequence SEQ_SYS_AUTOTASK
minvalue 0
maxvalue 999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_SYS_FRAME_ID';
  if i=0 then
  execute immediate 'create sequence SEQ_SYS_FRAME_ID
minvalue 1
maxvalue 99999999
start with 9051
increment by 1
cache 5
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_SYS_MESSAGE_ATTACHMENT';
  if i=0 then
  execute immediate 'create sequence SEQ_SYS_MESSAGE_ATTACHMENT
minvalue 1
maxvalue 999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_SYS_SYNC_REC_ID';
  if i=0 then
  execute immediate 'create sequence SEQ_SYS_SYNC_REC_ID
minvalue 1
maxvalue 999999999
start with 1
increment by 1
cache 5';  
 end if;
select count(1) into i from user_sequences t where t.sequence_name='SEQ_SYS_WEBSERVICE';
  if i=0 then
  execute immediate 'create sequence SEQ_SYS_WEBSERVICE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_SYS_WF_ID';
  if i=0 then
  execute immediate 'create sequence SEQ_SYS_WF_ID
minvalue 1
maxvalue 99999999
start with 151001
increment by 1
cache 5
cycle';  
 end if;

select count(1) into i from user_sequences t where t.sequence_name='SEQ_SYS_WF_TASK_ID';
  if i=0 then
  execute immediate 'create sequence SEQ_SYS_WF_TASK_ID
minvalue 1
maxvalue 99999999
start with 111
increment by 1
cache 5
cycle';  
 end if;

