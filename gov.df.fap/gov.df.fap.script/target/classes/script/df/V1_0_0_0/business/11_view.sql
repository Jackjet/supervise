i integer;
begin
execute immediate ' create or replace view sys_role_right_group as
select A.ROLE_ID, B.RIGHT_GROUP_ID, A.USER_ID, B.SET_YEAR, B.RG_CODE
 from SYS_USER_ROLE_RULE A, SYS_RIGHT_GROUP B where A.RULE_ID = B.RULE_ID';

execute immediate ' create or replace view sys_user as
select su."USER_ID",
 su."USER_CODE",
 su."USER_NAME",
 su."PASSWORD",
 su."ORG_TYPE",
 su."ORG_CODE",
 su."LEVEL_NUM",
 su."IS_LEAF",
 su."GENDER",
 su."TELEPHONE",
 su."MOBILE",
 su."ENABLED",
 su."HEADSHIP_CODE",
 su."BIRTHDAY",
 su."ADDRESS",
 su."EMAIL",
 su."USER_TYPE",
 su."IS_AUDIT",
 su."AUDIT_DATE",
 su."AUDIT_USER",
 su."NICKNAME",
 su."LAST_VER",
 su."BELONG_ORG",
 su."BELONG_TYPE",
 su."SECURITY_LEVEL",
 su."INIT_PASSWORD",
 su.set_year,
 su.is_edit_pwd,
 su.is_three_security,
 su.is_ever_locked,
 su.locked_date,
 sur.RG_CODE
 from sys_usermanage su left join sys_user_region sur
 on su.user_id=sur.user_id';

execute immediate ' create or replace view sys_wf_right_group as
select a.wf_id,b.right_group_id from sys_wf_rule a,sys_right_group b
 where a.rule_id=b.rule_id';

execute immediate ' create or replace view vm_sys_data_right_detail as
select
 a.user_id,
 a.rule_id,
 a.role_id,
 c.right_group_id,
 c.right_group_describe,
 t.ele_code,
 t.ele_value as chr_code
 from sys_user_role_rule a, sys_right_group c, sys_right_group_detail t
 where a.rule_id = c.rule_id
 and c.right_group_id = t.right_group_id
 and c.right_group_describe in (''001'', ''002'')
 and t.ele_value <> ''#''';
 
 execute immediate ' create or replace view vm_sys_data_right_type as
select a.user_id,
 a.rule_id,
 a.role_id,
 c.right_group_id,
 c.right_group_describe,
 t.right_type,
 t.ele_code
 from sys_user_role_rule a, sys_right_group c, sys_right_group_type t
 where a.rule_id = c.rule_id
 and c.right_group_id = t.right_group_id';

execute immediate ' create or replace view vw_budget_enterprise as
select SET_YEAR,
 CHR_ID,
 CHR_CODE,
 DISP_CODE,
 CHR_NAME,
 LEVEL_NUM,
 IS_LEAF,
 ENABLED,
 CREATE_DATE,
 CREATE_USER,
 LATEST_OP_DATE,
 IS_DELETED,
 LATEST_OP_USER,
 LAST_VER,
 CHR_CODE1,
 CHR_CODE2,
 CHR_CODE3,
 CHR_CODE4,
 CHR_CODE5,
 RG_CODE,
 MAIN_CODE,
 UNION_CODE,
 EN_PROPERTY,
 SORT_PROPERTY,
 EN_CHARGE,
 FINANCE_CHARGE,
 CLERK,
 DISTRICT_number,
 TELEPHONE,
 EXTENSION_number,
 ADDRESS,
 MANAGE_LEVEL,
 RELATION,
 PARENT_ID,
 CHR_ID1,
 CHR_ID2,
 CHR_ID3,
 CHR_ID4,
 CHR_ID5,
 CHR_CODE6,
 CHR_CODE7,
 CHR_CODE8,
 CHR_CODE9,
 CHR_ID6,
 CHR_ID7,
 CHR_ID8,
 CHR_ID9,
 START_DATE,
 END_DATE,
 ''AGENCY'' AS ORG_TYPE,
 IS_REFORM,
 SECRETDEGREE,
 ISBUDGET,
 LULOAD_MOD,
 EN_CF_MOD,
 EXPFUNC_ID,
 MB_ID
 from ELE_ENTERPRISE
 where ENABLED = 1';

execute immediate ' create or replace view VW_CLEARBANKACCOUNT AS
SELECT T.SET_YEAR,
 T.CHR_ID,
 T.CHR_CODE,
 T.DISP_CODE,
 T.CHR_NAME,
 T.LEVEL_NUM,
 T.IS_LEAF,
 T.ENABLED,
 T.CREATE_DATE,
 T.CREATE_USER,
 T.LATEST_OP_DATE,
 T.IS_DELETED,
 T.LATEST_OP_USER,
 T.LAST_VER,
 T.IS_DEFAULT,
 T.REMARK,
 T.CHR_CODE1,
 T.CHR_CODE2,
 T.CHR_CODE3,
 T.CHR_CODE4,
 T.CHR_CODE5,
 T.RG_CODE,
 T.ACCOUNT_NO,
 T.ACCOUNT_NAME,
 (SELECT CHR_ID
 FROM ELE_BANK
 WHERE CHR_CODE = T.BANK_CODE
 AND SET_YEAR = T.SET_YEAR
 AND CLEARFLAG = 1 AND RG_CODE = t.RG_CODE) AS BANK_ID,
 T.BANK_CODE,
 (SELECT CHR_NAME
 FROM ELE_BANK
 WHERE CHR_CODE = T.BANK_CODE
 AND SET_YEAR = T.SET_YEAR
 AND CLEARFLAG = 1 AND RG_CODE = t.RG_CODE) AS BANK_NAME,
 T.ACCOUNT_TYPE,
 T.OWNER_CODE,
 ''财政'' AS OWNER_NAME,
 T.PARENT_ID,
 T.CHR_ID1,
 T.CHR_ID2,
 T.CHR_ID3,
 T.CHR_ID4,
 T.CHR_ID5,
 T.START_DATE,
 T.STOP_DATE
 FROM ELE_ACCOUNT T
 WHERE T.ACCOUNT_TYPE = ''21''
 OR T.ACCOUNT_TYPE = ''22''';

execute immediate ' create or replace view vw_ele_bis_belong as
select bis.chr_id,bis.chr_code,bis.chr_name,belong.MB_ID,belong.AGENCY_ID,belong.AGENCYEXP_ID,belong.chr_code_sup,bis.set_year,bis.RG_CODE from ELE_BUDGET_ITEM_SUMMARY bis left join ELE_BUDGET_ITEM_SUMMARY_BELONG belong on bis.chr_id=belong.bis_id and bis.set_year=belong.year';

execute immediate ' create or replace view vw_ele_opuser as
select user_id as chr_id,
 USER_CODE as chr_code,
 USER_CODE as disp_code,
 USER_Name as chr_name,
 1 as level_num,
 0 as is_leaf,
 enabled,
 null as create_date,
 null as create_user,
 null as latest_op_date,
 0 as is_deleted,
 null as latest_op_user,
 null as last_ver,
 USER_CODE as chr_code1,
 null as chr_code2,
 null as chr_code3,
 null as chr_code4,
 null as chr_code5,
 RG_CODE,
 null as parent_id, user_id as chr_id1,null as chr_id2, null as chr_id3,null as
 chr_id4,null as chr_id5,
 decode(set_year,null,to_char(sysdate,''yyyy''),set_year) set_year
 from sys_user';
/*execute immediate ' create or replace view vw_en_budget_item_summary as
select a.chr_id,
 a.chr_code,
 a.chr_name,
 a.parent_id,
 1 level_num,
 0 is_leaf,
 a.chr_code1,
 a.chr_code2,
 a.chr_code3,
 a.chr_code4,
 a.chr_code5,
 a.RG_CODE,
 a.set_year,
 a.enabled
 from ele_enterprise a
 where exists
 (select chr_code
 from ele_enterprise en
 where exists
 (select 1
 from ele_budget_item_summary bis
 where en.chr_id = bis.AGENCY_ID)
 and a.chr_code = substr(en.chr_code, 1, length(a.chr_code)))
 union all
 select chr_id,
 chr_code,
 chr_name,
 decode(bis.parent_id, '''', bis.AGENCY_ID, bis.parent_id) parent_id,
 level_num,
 is_leaf,
 chr_code1,
 chr_code2,
 chr_code3,
 chr_code4,
 chr_code5,
 RG_CODE,
 set_year,
 enabled
 from ele_budget_item_summary bis';
 */

execute immediate ' create or replace view vw_gl_account as
select "SET_YEAR",
 "CHR_ID" as account_id,
 "CHR_CODE" as account_code,
 "DISP_CODE",
 "CHR_NAME" as account_name,
 "LEVEL_NUM",
 "IS_LEAF",
 "ENABLED",
 "CREATE_DATE",
 "CREATE_USER",
 "LATEST_OP_DATE",
 "IS_DELETED",
 "LATEST_OP_USER",
 "LAST_VER",
 "CHR_CODE1",
 "CHR_CODE2",
 "CHR_CODE3",
 "CHR_CODE4",
 "CHR_CODE5",
 "RG_CODE",
 "IS_DEBIT" as balance_side,
 "SUBJECT_TYPE",
 "PARENT_ID",
 "CHR_ID1",
 "CHR_ID2",
 "CHR_ID3",
 "CHR_ID4",
 "CHR_ID5",
 "ST_ID",
 "SUBJECT_KIND",
 "COA_ID",
 "HINT_CODE",
 "TABLE_NAME",
 "BALANCE_PERIOD_TYPE",
 "MONTHDETAIL_TABLE_NAME"
 from ele_accountant_subject e
 where st_id=(select chr_id
 from ele_book_set b
 where b.set_type=0
 and b.set_year=e.set_year
 and b.RG_CODE=e.RG_CODE)';

execute immediate ' create or replace view VW_GL_BALANCE AS
SELECT
 RG_CODE,
 BALANCE_ID,
 ACCOUNT_ID as SUM_TYPE_ID,
 SUM_ID,
 CREATE_DATE,
 CREATE_USER,
 LATEST_OP_DATE,
 LATEST_OP_USER,
 SET_YEAR,
 SET_MONTH,
 AVI_MONEY,
 USE_MONEY,
 MINUS_MONEY,
 AVING_MONEY,
 LAST_VER,
 FROMCTRLID,
 REMARK,
 BAL_VERSION,
 RCID,
 CCID
 FROM GL_BALANCE';

execute immediate ' create or replace view vw_gl_bus_voucher_detail as
select a.vou_id, a.vou_type_id, a.ctrlid fromctrlid, a.ctrl_type fromctrltype, b.ctrlid toctrlid, b.ctrl_type toctrltype from
 (select * from gl_balance_trace where ctrl_side=1) a
 left join
 (select * from gl_balance_trace where ctrl_side=0) b
 on a.vou_id=b.vou_id and a.vou_type_id=b.vou_type_id';

execute immediate ' create or replace view VW_GL_CCIDS AS
SELECT
 LATEST_OP_DATE      ,
 LATEST_OP_USER      ,
 COA_ID              ,
 AS_ID               ,
 AGENCY_ID               ,
 FUNDTYPE_ID               ,
 EXPFUNC_ID               ,
 AGENCYEXP_ID               ,
 EXPECO_ID              ,
 PAYTYPE_ID               ,
 BGTTYPE_ID               ,
 PAYKIND_ID               ,
 MB_ID               ,
 FILE_ID             ,
 SETMODE_ID               ,
 IN_BS_ID            ,
 IN_BIS_ID           ,
 CB_ID               ,
 PB_ID               ,
 IB_ID               ,
 BAC_ID              ,
 BAP_ID              ,
 BAI_ID              ,
 BGTDIR_ID               ,
 BP_ID               ,
 BGTSOURCE_ID               ,
 HOLD1_ID            ,
 HOLD2_ID            ,
 HOLD3_ID            ,
 HOLD4_ID            ,
 HOLD5_ID            ,
 HOLD6_ID            ,
 HOLD7_ID            ,
 HOLD8_ID            ,
 HOLD9_ID            ,
 HOLD10_ID           ,
 HOLD11_ID           ,
 HOLD12_ID           ,
 HOLD13_ID           ,
 HOLD14_ID           ,
 HOLD15_ID           ,
 HOLD16_ID           ,
 HOLD17_ID           ,
 HOLD18_ID           ,
 HOLD19_ID           ,
 HOLD20_ID           ,
 HOLD21_ID           ,
 HOLD22_ID           ,
 HOLD23_ID           ,
 HOLD24_ID           ,
 HOLD25_ID           ,
 HOLD26_ID           ,
 HOLD27_ID           ,
 HOLD28_ID           ,
 HOLD29_ID           ,
 HOLD30_ID           ,
 SET_YEAR            ,
 BIS_ID              ,
 INPM_ID             ,
 ST_ID               ,
 CT_ID               ,
 SM_ID               ,
 OPUSER_ID           ,
 EDITOR_ID           ,
 ZFCGBS_ID               ,
 FM_ID               ,
 AS_CODE             ,
 AS_NAME             ,
 AGENCY_CODE             ,
 AGENCY_NAME             ,
 FUNDTYPE_CODE             ,
 FUNDTYPE_NAME             ,
 EXPFUNC_CODE             ,
 EXPFUNC_NAME             ,
 AGENCYEXP_CODE             ,
 AGENCYEXP_NAME             ,
 EXPECO_CODE            ,
 EXPECO_NAME            ,
 PAYTYPE_CODE             ,
 PAYTYPE_NAME             ,
 BGTTYPE_CODE             ,
 BGTTYPE_NAME             ,
 PAYKIND_CODE             ,
 PAYKIND_NAME             ,
 MB_CODE             ,
 MB_NAME             ,
 FILE_CODE           ,
 FILE_NAME           ,
 SETMODE_CODE             ,
 SETMODE_NAME             ,
 IN_BS_CODE          ,
 IN_BS_NAME          ,
 IN_BIS_CODE         ,
 IN_BIS_NAME         ,
 CB_CODE             ,
 CB_NAME             ,
 PB_CODE             ,
 PB_NAME             ,
 IB_CODE             ,
 IB_NAME             ,
 BAC_CODE            ,
 BAC_NAME            ,
 BAP_CODE            ,
 BAP_NAME            ,
 BAI_CODE            ,
 BAI_NAME            ,
 BGTDIR_CODE             ,
 BGTDIR_NAME             ,
 BP_CODE             ,
 BP_NAME             ,
 BGTSOURCE_CODE             ,
 BGTSOURCE_NAME             ,
 HOLD1_CODE          ,
 HOLD1_NAME          ,
 HOLD2_CODE          ,
 HOLD2_NAME          ,
 HOLD3_CODE          ,
 HOLD3_NAME          ,
 HOLD4_CODE          ,
 HOLD4_NAME          ,
 HOLD5_CODE          ,
 HOLD5_NAME          ,
 HOLD6_CODE          ,
 HOLD6_NAME          ,
 HOLD7_CODE          ,
 HOLD7_NAME          ,
 HOLD8_CODE          ,
 HOLD8_NAME          ,
 HOLD9_CODE          ,
 HOLD9_NAME          ,
 HOLD10_CODE         ,
 HOLD10_NAME         ,
 HOLD11_CODE         ,
 HOLD11_NAME         ,
 HOLD12_CODE         ,
 HOLD12_NAME         ,
 HOLD13_CODE         ,
 HOLD13_NAME         ,
 HOLD14_CODE         ,
 HOLD14_NAME         ,
 HOLD15_CODE         ,
 HOLD15_NAME         ,
 HOLD16_CODE         ,
 HOLD16_NAME         ,
 HOLD17_CODE         ,
 HOLD17_NAME         ,
 HOLD18_CODE         ,
 HOLD18_NAME         ,
 HOLD19_CODE         ,
 HOLD19_NAME         ,
 HOLD20_CODE         ,
 HOLD20_NAME         ,
 HOLD21_CODE         ,
 HOLD21_NAME         ,
 HOLD22_CODE         ,
 HOLD22_NAME         ,
 HOLD23_CODE         ,
 HOLD23_NAME         ,
 HOLD24_CODE         ,
 HOLD24_NAME         ,
 HOLD25_CODE         ,
 HOLD25_NAME         ,
 HOLD26_CODE         ,
 HOLD26_NAME         ,
 HOLD27_CODE         ,
 HOLD27_NAME         ,
 HOLD28_CODE         ,
 HOLD28_NAME         ,
 HOLD29_CODE         ,
 HOLD29_NAME         ,
 HOLD30_CODE         ,
 HOLD30_NAME         ,
 BIS_CODE            ,
 BIS_NAME            ,
 INPM_CODE           ,
 INPM_NAME           ,
 ST_CODE             ,
 ST_NAME             ,
 CT_CODE             ,
 CT_NAME             ,
 SM_CODE             ,
 SM_NAME             ,
 OPUSER_CODE         ,
 OPUSER_NAME         ,
 EDITOR_CODE         ,
 EDITOR_NAME         ,
 ZFCGBS_CODE             ,
 ZFCGBS_NAME             ,
 FM_CODE             ,
 FM_NAME             ,
 PAY_ACCOUNT_NO      ,
 PAY_ACCOUNT_NAME    ,
 PAY_ACCOUNT_BANK    ,
 CLEAR_ACCOUNT_NO    ,
 CLEAR_ACCOUNT_NAME  ,
 CLEAR_ACCOUNT_BANK  ,
 INCOME_ACCOUNT_NO   ,
 INCOME_ACCOUNT_NAME ,
 INCOME_ACCOUNT_BANK ,
 PF_ID               ,
 PF_CODE             ,
 PF_NAME             ,
 GROUPID_ID          ,
 GROUPID_CODE        ,
 GROUPID_NAME        ,
 USEEN_ID            ,
 USEEN_CODE          ,
 USEEN_NAME          ,
 PROV_ID             ,
 PROV_CODE           ,
 PROV_NAME           ,
 GP_AGENCY_ID           ,
 GP_AGENCY_CODE         ,
 GP_AGENCY_NAME         ,
 MODE_ID             ,
 MODE_CODE           ,
 MODE_NAME           ,
 FASN_ID             ,
 FASN_CODE           ,
 FASN_NAME           ,
 DIR_ID              ,
 DIR_CODE            ,
 DIR_NAME            ,
 GPPLAN_ID           ,
 GPPLAN_CODE         ,
 GPPLAN_NAME         ,
 GPBARN_ID           ,
 GPBARN_CODE         ,
 GPBARN_NAME         ,
 WA_ID               ,
 WA_CODE             ,
 WA_NAME             ,
 FF_ID               ,
 FF_NAME             ,
 FF_CODE             ,
 IEN_ID              ,
 IEN_NAME            ,
 IEN_CODE            ,
 GZBS_ID           ,
 GZBS_NAME         ,
 GZBS_CODE         ,
 BC_ID               ,
 BC_CODE             ,
 BC_NAME             ,
 BSO_ID              ,
 BSO_CODE            ,
 BSO_NAME            ,
 AE_ID               ,
 AE_CODE             ,
 AE_NAME             ,
 CCID                ,
 BUDGET_VOU_ID       ,
 BUDGET_VOU_ID AS BUDGET_VOU_CODE     ,
 BUDGET_VOU_NAME,
 RG_CODE
 FROM GL_CCIDS';

execute immediate ' create or replace view vw_gl_patch_voucher as
select a."VOU_ID",a."VOU_NO",a."VOU_MONEY",a."SET_MONTH",a."IS_END",a."BILLTYPE_ID",a."BILLTYPE_CODE",a."CREATE_DATE",a."CREATE_USER",a."LAST_VER",a."REMARK",a."RCID",a."RG_CODE",a."FROMCTRLID",a."TOCTRLID",
 t."LATEST_OP_DATE",t."LATEST_OP_USER",t."COA_ID",t."AS_ID",t."AGENCY_ID",t."FUNDTYPE_ID",t."EXPFUNC_ID",t."AGENCYEXP_ID",t."EXPECO_ID",t."PAYTYPE_ID",t."BGTTYPE_ID",t."PAYKIND_ID",t."MB_ID",t."FILE_ID",t."SETMODE_ID",t."IN_BS_ID",t."IN_BIS_ID",t."CB_ID",t."PB_ID",t."IB_ID",t."BAC_ID",t."BAP_ID",t."BAI_ID",t."BGTDIR_ID",t."BP_ID",t."BGTSOURCE_ID",t."HOLD1_ID",t."HOLD2_ID",t."HOLD3_ID",t."HOLD4_ID",t."HOLD5_ID",t."HOLD6_ID",t."HOLD7_ID",t."HOLD8_ID",t."HOLD9_ID",t."HOLD10_ID",t."HOLD11_ID",t."HOLD12_ID",t."HOLD13_ID",t."HOLD14_ID",t."HOLD15_ID",t."HOLD16_ID",t."HOLD17_ID",t."HOLD18_ID",t."HOLD19_ID",t."HOLD20_ID",t."HOLD21_ID",t."HOLD22_ID",t."HOLD23_ID",t."HOLD24_ID",t."HOLD25_ID",t."HOLD26_ID",t."HOLD27_ID",t."HOLD28_ID",t."HOLD29_ID",t."HOLD30_ID",t."SET_YEAR",t."BIS_ID",t."INPM_ID",t."ST_ID",t."CT_ID",t."SM_ID",t."OPUSER_ID",t."EDITOR_ID",t."ZFCGBS_ID",t."FM_ID",t."AS_CODE",t."AS_NAME",t."AGENCY_CODE",t."AGENCY_NAME",t."FUNDTYPE_CODE",t."FUNDTYPE_NAME",t."EXPFUNC_CODE",t."EXPFUNC_NAME",t."AGENCYEXP_CODE",t."AGENCYEXP_NAME",t."EXPECO_CODE",t."EXPECO_NAME",t."PAYTYPE_CODE",t."PAYTYPE_NAME",t."BGTTYPE_CODE",t."BGTTYPE_NAME",t."PAYKIND_CODE",t."PAYKIND_NAME",t."MB_CODE",t."MB_NAME",t."FILE_CODE",t."FILE_NAME",t."SETMODE_CODE",t."SETMODE_NAME",t."IN_BS_CODE",t."IN_BS_NAME",t."IN_BIS_CODE",t."IN_BIS_NAME",t."CB_CODE",t."CB_NAME",t."PB_CODE",t."PB_NAME",t."IB_CODE",t."IB_NAME",t."BAC_CODE",t."BAC_NAME",t."BAP_CODE",t."BAP_NAME",t."BAI_CODE",t."BAI_NAME",t."BGTDIR_CODE",t."BGTDIR_NAME",t."BP_CODE",t."BP_NAME",t."BGTSOURCE_CODE",t."BGTSOURCE_NAME",t."HOLD1_CODE",t."HOLD1_NAME",t."HOLD2_CODE",t."HOLD2_NAME",t."HOLD3_CODE",t."HOLD3_NAME",t."HOLD4_CODE",t."HOLD4_NAME",t."HOLD5_CODE",t."HOLD5_NAME",t."HOLD6_CODE",t."HOLD6_NAME",t."HOLD7_CODE",t."HOLD7_NAME",t."HOLD8_CODE",t."HOLD8_NAME",t."HOLD9_CODE",t."HOLD9_NAME",t."HOLD10_CODE",t."HOLD10_NAME",t."HOLD11_CODE",t."HOLD11_NAME",t."HOLD12_CODE",t."HOLD12_NAME",t."HOLD13_CODE",t."HOLD13_NAME",t."HOLD14_CODE",t."HOLD14_NAME",t."HOLD15_CODE",t."HOLD15_NAME",t."HOLD16_CODE",t."HOLD16_NAME",t."HOLD17_CODE",t."HOLD17_NAME",t."HOLD18_CODE",t."HOLD18_NAME",t."HOLD19_CODE",t."HOLD19_NAME",t."HOLD20_CODE",t."HOLD20_NAME",t."HOLD21_CODE",t."HOLD21_NAME",t."HOLD22_CODE",t."HOLD22_NAME",t."HOLD23_CODE",t."HOLD23_NAME",t."HOLD24_CODE",t."HOLD24_NAME",t."HOLD25_CODE",t."HOLD25_NAME",t."HOLD26_CODE",t."HOLD26_NAME",t."HOLD27_CODE",t."HOLD27_NAME",t."HOLD28_CODE",t."HOLD28_NAME",t."HOLD29_CODE",t."HOLD29_NAME",t."HOLD30_CODE",t."HOLD30_NAME",t."BIS_CODE",t."BIS_NAME",t."INPM_CODE",t."INPM_NAME",t."ST_CODE",t."ST_NAME",t."CT_CODE",t."CT_NAME",t."SM_CODE",t."SM_NAME",t."OPUSER_CODE",t."OPUSER_NAME",t."EDITOR_CODE",t."EDITOR_NAME",t."ZFCGBS_CODE",t."ZFCGBS_NAME",t."FM_CODE",t."FM_NAME",t."PAY_ACCOUNT_NO",t."PAY_ACCOUNT_NAME",t."PAY_ACCOUNT_BANK",t."CLEAR_ACCOUNT_NO",t."CLEAR_ACCOUNT_NAME",t."CLEAR_ACCOUNT_BANK",t."INCOME_ACCOUNT_NO",t."INCOME_ACCOUNT_NAME",t."INCOME_ACCOUNT_BANK",t."PF_ID",t."PF_CODE",t."PF_NAME",t."GROUPID_ID",t."GROUPID_CODE",t."GROUPID_NAME",t."USEEN_ID",t."USEEN_CODE",t."USEEN_NAME",t."PROV_ID",t."PROV_CODE",t."PROV_NAME",t."GP_AGENCY_ID",t."GP_AGENCY_CODE",t."GP_AGENCY_NAME",t."MODE_ID",t."MODE_CODE",t."MODE_NAME",t."FASN_ID",t."FASN_CODE",t."FASN_NAME",t."DIR_ID",t."DIR_CODE",t."DIR_NAME",t."GPPLAN_ID",t."GPPLAN_CODE",t."GPPLAN_NAME",t."GPBARN_ID",t."GPBARN_CODE",t."GPBARN_NAME",t."WA_ID",t."WA_CODE",t."WA_NAME",t."FF_ID",t."FF_NAME",t."FF_CODE",t."IEN_ID",t."IEN_NAME",t."IEN_CODE",t."GZBS_ID",t."GZBS_NAME",t."GZBS_CODE",t."BC_ID",t."BC_CODE",t."BC_NAME",t."BSO_ID",t."BSO_CODE",t."BSO_NAME",t."AE_ID",t."AE_CODE",t."AE_NAME",t."CCID",t."BUDGET_VOU_ID",t."BUDGET_VOU_CODE",t."BUDGET_VOU_NAME",t."MD5"
 from gl_patch_voucher a,gl_ccids t
 where a.ccid=t.ccid';

execute immediate ' create or replace view VW_INCOMEBANKACCOUNT AS
SELECT T.SET_YEAR,T.CHR_ID,T.CHR_CODE,T.DISP_CODE,T.CHR_NAME,T.LEVEL_NUM,T.IS_LEAF,
 T.ENABLED,T.CREATE_DATE,T.CREATE_USER,T.LATEST_OP_DATE,T.IS_DELETED,T.LATEST_OP_USER,T.LAST_VER,
 T.CHR_CODE1,T.CHR_CODE2,T.CHR_CODE3,T.CHR_CODE4,T.CHR_CODE5,T.RG_CODE,T.ACCOUNT_NO,T.ACCOUNT_NAME,
 (SELECT CHR_ID FROM ELE_BANK WHERE CHR_CODE = T.BANK_CODE AND SET_YEAR=T.SET_YEAR AND RG_CODE = T.RG_CODE AND INCOMEFLAG=1) AS BANK_ID,
 T.BANK_CODE,(SELECT CHR_NAME FROM ELE_BANK WHERE CHR_CODE = T.BANK_CODE AND SET_YEAR=T.SET_YEAR AND RG_CODE = T.RG_CODE AND INCOMEFLAG=1) AS BANK_NAME,
 T.ACCOUNT_TYPE,T.OWNER_CODE,NULL AS OWNER_NAME,
 T.PARENT_ID,T.CHR_ID1,T.CHR_ID2,T.CHR_ID3,T.CHR_ID4,T.CHR_ID5,T.START_DATE,T.STOP_DATE FROM ELE_ACCOUNT T
 WHERE T.ACCOUNT_TYPE LIKE ''4%''';

execute immediate ' create or replace view VW_PAYBANK AS
SELECT "SET_YEAR",
 "CHR_ID",
 "CHR_CODE",
 "DISP_CODE",
 "CHR_NAME",
 "LEVEL_NUM",
 "IS_LEAF",
 "ENABLED",
 "CREATE_DATE",
 "CREATE_USER",
 "LATEST_OP_DATE",
 "IS_DELETED",
 "LATEST_OP_USER",
 "LAST_VER",
 "CHR_CODE1",
 "CHR_CODE2",
 "CHR_CODE3",
 "CHR_CODE4",
 "CHR_CODE5",
 "RG_CODE",
 "BANK_CHARGE",
 "FINANCE_CHARGE",
 "DISTRICT_NUMBER",
 "TELEPHONE",
 "EXTENSION_NUMBER",
 "ADDRESS",
 "PARENT_ID",
 "CHR_ID1",
 "CHR_ID2",
 "CHR_ID3",
 "CHR_ID4",
 "CHR_ID5",
 "AGENTFLAG",
 "CLEARFLAG",
 "INCOMEFLAG",
 "SALARYFLAG",
 "EXBANK_CODE",
 "EXBANK_NAME",
 "EXBANK_PBNO",
 "ISBN",
 "CITY_CODE",
 "BANK_SIGN",
 "BANK_NO",
 "ISBN_C",
 "PROVINCE"
 FROM ELE_BANK
 WHERE AGENTFLAG = 1';

execute immediate ' create or replace view VW_PAYBANKACCOUNT AS
SELECT T.SET_YEAR,T.CHR_ID,T.CHR_CODE,T.DISP_CODE,T.CHR_NAME,T.LEVEL_NUM,T.IS_LEAF,T.Is_Default,t.remark,
 T.ENABLED,T.CREATE_DATE,T.CREATE_USER,T.LATEST_OP_DATE,T.IS_DELETED,T.LATEST_OP_USER,T.LAST_VER,
 T.CHR_CODE1,T.CHR_CODE2,T.CHR_CODE3,T.CHR_CODE4,T.CHR_CODE5,T.RG_CODE,T.ACCOUNT_NO,T.ACCOUNT_NAME,
 (SELECT CHR_ID FROM ELE_BANK WHERE CHR_CODE = T.BANK_CODE AND SET_YEAR=T.SET_YEAR AND RG_CODE = T.RG_CODE AND CLEARFLAG=1) AS BANK_ID,
 T.BANK_CODE,(SELECT CHR_NAME FROM ELE_BANK WHERE CHR_CODE = T.BANK_CODE AND SET_YEAR=T.SET_YEAR AND RG_CODE = T.RG_CODE AND CLEARFLAG=1) AS BANK_NAME,
 T.ACCOUNT_TYPE,T.OWNER_CODE,(SELECT CHR_NAME FROM ELE_BANK WHERE CHR_CODE = T.OWNER_CODE AND RG_CODE = T.RG_CODE AND AGENTFLAG=1) AS OWNER_NAME,
 T.PARENT_ID,T.CHR_ID1,T.CHR_ID2,T.CHR_ID3,T.CHR_ID4,T.CHR_ID5,T.START_DATE,T.STOP_DATE  FROM ELE_ACCOUNT T
 WHERE T.ACCOUNT_TYPE LIKE ''3%''';

execute immediate ' create or replace view VW_PAYEE_BANK AS
SELECT "SET_YEAR",
 "CHR_ID",
 "CHR_CODE",
 "DISP_CODE",
 "CHR_NAME",
 "LEVEL_NUM",
 "IS_LEAF",
 "ENABLED",
 "CREATE_DATE",
 "CREATE_USER",
 "LATEST_OP_DATE",
 "IS_DELETED",
 "LATEST_OP_USER",
 "LAST_VER",
 "CHR_CODE1",
 "CHR_CODE2",
 "CHR_CODE3",
 "CHR_CODE4",
 "CHR_CODE5",
 "RG_CODE",
 "BANK_CHARGE",
 "FINANCE_CHARGE",
 "DISTRICT_NUMBER",
 "TELEPHONE",
 "EXTENSION_NUMBER",
 "ADDRESS",
 "PARENT_ID",
 "CHR_ID1",
 "CHR_ID2",
 "CHR_ID3",
 "CHR_ID4",
 "CHR_ID5",
 "AGENTFLAG",
 "CLEARFLAG",
 "INCOMEFLAG",
 "SALARYFLAG",
 "EXBANK_CODE",
 "EXBANK_NAME",
 "EXBANK_PBNO",
 "PROVINCE",
 "BANK_SIGN",
 "ISBN",
 "CITY_CODE",
 "ISBN_C"
 FROM ELE_BANK
 WHERE clearflag=0 and incomeflag=0 and salaryflag=0';

execute immediate ' create or replace view VW_PAYMENTACCOUNT AS
SELECT T."SET_YEAR",T."CHR_ID",T."CHR_CODE",T."DISP_CODE",T."CHR_NAME",T."LEVEL_NUM",T."IS_LEAF",T."ENABLED",T."CREATE_DATE",T."CREATE_USER",T."LATEST_OP_DATE",T."IS_DELETED",T."LATEST_OP_USER",T."LAST_VER",T."CHR_CODE1",T."CHR_CODE2",T."CHR_CODE3",T."CHR_CODE4",T."CHR_CODE5",T."RG_CODE",T."ACCOUNT_NO",T."ACCOUNT_NAME",T."BANK_CODE",T."ACCOUNT_TYPE",T."OWNER_CODE",T."PARENT_ID",T."CHR_ID1",T."CHR_ID2",T."CHR_ID3",T."CHR_ID4",T."CHR_ID5",T."START_DATE",T."STOP_DATE",T."IS_DEFAULT",T."REMARK",
 (SELECT CHR_ID
 FROM ELE_BANK
 WHERE CHR_CODE = T.BANK_CODE and RG_CODE = t.RG_CODE
 AND SET_YEAR = T.SET_YEAR
 AND RG_CODE = T.RG_CODE
 AND AGENTFLAG = 1) AS BANK_ID,
 (SELECT CHR_NAME
 FROM ELE_BANK
 WHERE CHR_CODE = T.BANK_CODE and RG_CODE = t.RG_CODE
 AND SET_YEAR = T.SET_YEAR
 AND RG_CODE = T.RG_CODE
 AND AGENTFLAG = 1) AS BANK_NAME,
 DECODE(T.OWNER_CODE,
 ''000'',
 ''财政'',
 (SELECT CHR_NAME
 FROM ELE_ENTERPRISE
 WHERE CHR_CODE = T.OWNER_CODE and t.RG_CODE=RG_CODE and t.set_year = set_year)) AS OWNER_NAME
 FROM ELE_ACCOUNT T
 WHERE T.ACCOUNT_TYPE LIKE ''1%''';

 execute immediate ' create or replace view VW_SALARYBANK AS
SELECT "SET_YEAR",
 "CHR_ID",
 "CHR_CODE",
 "DISP_CODE",
 "CHR_NAME",
 "LEVEL_NUM",
 "IS_LEAF",
 "ENABLED",
 "CREATE_DATE",
 "CREATE_USER",
 "LATEST_OP_DATE",
 "IS_DELETED",
 "LATEST_OP_USER",
 "LAST_VER",
 "CHR_CODE1",
 "CHR_CODE2",
 "CHR_CODE3",
 "CHR_CODE4",
 "CHR_CODE5",
 "RG_CODE",
 "BANK_CHARGE",
 "FINANCE_CHARGE",
 "DISTRICT_NUMBER",
 "TELEPHONE",
 "EXTENSION_NUMBER",
 "ADDRESS",
 "PARENT_ID",
 "CHR_ID1",
 "CHR_ID2",
 "CHR_ID3",
 "CHR_ID4",
 "CHR_ID5",
 "AGENTFLAG",
 "CLEARFLAG",
 "INCOMEFLAG",
 "SALARYFLAG"
 FROM ELE_BANK
 WHERE SALARYFLAG = 1 and ENABLED = 1';

execute immediate ' create or replace view vw_sys_button as
select b.button_id,--按钮id，内码
 to_char(b.module_id) module_id,--功能id，内码
 b.display_order,--按钮排序码，数字排列，如1,2,3
 b.display_title --按钮显示名
 from sys_button b
';

execute immediate ' create or replace view vw_sys_element as
( select se.ele_code as chr_id, se.ele_code as chr_code, se.ele_source as ele_source, se.ele_name as chr_name, null as parent_id, 1 as enabled, se.set_year,  se.RG_CODE, null as is_leaf, null as chr_code1, null as chr_code2, null as chr_code3, null as chr_code4, null as chr_code5, null as level_num, se.is_operate as is_operate from sys_element se)';

execute immediate ' create or replace view vw_sys_enumerate as
select chr_id as chr_id,
 field_code as field_code,
enu_name as chr_name,
enu_code as chr_code,
 1 as level_num,
 0 as is_leaf,
 0 as RG_CODE,
 0 as enabled,
 is_deleted as is_deleted,
 create_date as create_date,
 create_user as create_user,
 latest_op_date as lastest_op_date,
 latest_op_user as last_op_user,
 last_ver as last_ver,
 field_code as chr_code0,
 null as chr_code1,
 null as chr_id1
 from sys_enumerate';

execute immediate ' create or replace view vw_sys_menu as
select to_char(menu.menu_id) menu_id,--菜单id，内码
 menu.menu_code,--菜单编码，界面显示，菜单排序显示和此编码有关
 menu.menu_name,--菜单名称
 menu.enabled,--菜单是否可用，1为是，0为否
 menu.user_sys_id, --用户子系统
 menu.icon,
 menu.is_offline,
 menu.level_num,
 menu.is_leaf, --是否叶子节点，解决切换风格时报错的问题
 (select max(sp.state) from sys_menu_module smm, sys_module sm,sys_plugs_state sp where
 smm.module_id = sm.module_id
 and sm.sys_id = sp.pro_id
 and smm.menu_id = menu.menu_id) isstart
 from Sys_Menu menu
';

execute immediate ' create or replace view vw_sys_menu_module as
select to_char(m.menu_id) menu_id,--菜单id，内码
 to_char(m.module_id) module_id,--功能id，内码
 m.display_order,--功能排序码
 m.display_title --功能显示名
 from sys_menu_module m
';

execute immediate ' create or replace view vw_sys_metadata_ui_relation as
select distinct Lower(t.field_code) as field_name,
 t.field_name as title,
 decode(a.ref_mode,
 0,
 ''treeassist'',
 1,
 ''assistcombobox'',
 2,
 ''tableassist'') as disp_mode,
 t.default_value as value,
 null as ref_model,
 t.field_valueset as source,
 decode(t.field_disptype, 6, ''false'', ''true'') as visible,
 1 as is_element
 from sys_metadata t, sys_element a
 where t.field_disptype = 1
 and t.field_valueset = a.ele_code
 union
 select distinct Lower(t.field_code) as field_name,
 t.field_name as title,
 decode(t.field_disptype,
 0,
 ''text'',
 2,
 ''combobox'',
 3,
 ''decimal'',
 4,
 ''datetime'',
 5,
 ''datetime'',
 7,
 ''textarea'',
 ''text'') as disp_mode,
 t.default_value as value,
 decode(t.source,null,t.field_valueset,
 (select b.field_valueset from sys_metadata b where b.field_code=t.source))
 as ref_model,
 null as source,
 decode(t.field_disptype, 6, ''false'', ''true'') as visible,
 0 as is_element
 from sys_metadata t where t.field_disptype<>1';

execute immediate ' create or replace view vw_sys_module as
select to_char(m.module_id) module_id,--功能id，内码
 m.module_code,--功能编码，界面显示，功能排序显示和此编码有关
 m.module_name,--功能名称
 m.enabled --功能是否可用，1为是，0为否
 from sys_module m
';

execute immediate ' create or replace view vw_sys_orgs as
select chr_id,chr_code,chr_name,(select ORGTYPE_CODE from SYS_ORGTYPE where ELE_CODE=''RG'' and rownum=1 ) as ORG_TYPE,set_year,null as RG_CODE,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5  from ele_region UNION ALL select chr_id,chr_code,chr_name,''002'' as ORG_TYPE,set_year,null as RG_CODE,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5  from ELE_ENTERPRISE UNION ALL select chr_id,chr_code,chr_name,''003'' as ORG_TYPE,set_year,null as RG_CODE,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5  from ELE_BANK UNION ALL select chr_id,chr_code,chr_name,''004'' as ORG_TYPE,set_year,null as RG_CODE,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5  from ELE_BANK UNION ALL select chr_id,chr_code,chr_name,''005'' as ORG_TYPE,set_year,null as RG_CODE,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5  from ELE_BANK';

execute immediate ' create or replace view vw_sys_status as
select status_id as chr_id,
 substr(to_char(sysdate,''YYYYMMDD''),1,4) as set_year,
 status_name as chr_name,
 status_code as chr_code,
 1 as level_num,
 0 as is_leaf,
 0 as is_deleted,
 0 as RG_CODE,
 0 as enabled,
 last_ver as last_ver
 from sys_status';

execute immediate ' create or replace view vw_sys_useraccredit as
select ua.id,
 umfrom.user_id as accredit_from_id,
 umfrom.user_code as accredit_from_code,
 umfrom.user_name as accredit_from_name,
 umto.user_id as accredit_to_id,
 umto.user_code as accredit_to_code,
 umto.user_name as accredit_to_name,
 ua.role_id,
 r.role_name,
 ua.start_time,
 ua.end_time,
 ua.back_time,
 ua.state,
 ua.remark
 from sys_useraccredit ua,
 sys_usermanage   umfrom,
 sys_usermanage   umto,
 sys_role         r
 where ua.accredit_from = umfrom.user_id
 and ua.accredit_to = umto.user_id
 and ua.role_id = r.role_id';

execute immediate ' create or replace view vw_sys_wf_complete_item as
select SC.BILL_ID, STATUS_CODE,
 case SC.STATUS_CODE
 when ''101'' then ''已挂起''
 when ''102'' then ''已删除''
 when ''103'' then ''已作废''
 else SC.STATUS_CODE
 end as STATUS_NAME,
 SC.NODE_ID, NODE_NAME, ENTITY_ID, RCID, CCID, SC.SET_YEAR, SC.RG_CODE,sc.MB_ID,sc.AGENCY_ID,sc.pb_id,sc.cb_id,sc.ib_id,sc.GP_agency_id
 from SYS_WF_COMPLETE_ITEM SC, SYS_WF_NODES SN
 where SC.NODE_ID = SN.NODE_ID and SC.SET_YEAR = SN.SET_YEAR and SC.RG_CODE = SN.RG_CODE and SC.STATUS_CODE <> 001';

execute immediate ' create or replace view vw_sys_wf_complete_tasks as
select distinct BILL_ID, ENTITY_ID, decode(ACTION_TYPE_CODE, ''NEXT'', NEXT_NODE_ID, ''BACK'', NEXT_NODE_ID,
 ''INPUT'', NEXT_NODE_ID, CURRENT_NODE_ID) NODE_ID, ''008'' as STATUS_CODE, ''曾经办'' as STATUS_NAME, RCID,
 CCID, SET_YEAR, RG_CODE from SYS_WF_COMPLETE_TASKS';

execute immediate ' create or replace view vw_sys_wf_current_end_tasks as
select TASK_ID, WF_ID, WF_TABLE_NAME, ENTITY_ID, CURRENT_NODE_ID, NEXT_NODE_ID, ACTION_TYPE_CODE, IS_UNDO,
 CREATE_USER, CREATE_DATE, UNDO_USER, UNDO_DATE, OPERATION_NAME, OPERATION_DATE, OPERATION_REMARK,
 INIT_MONEY, RESULT_MONEY, REMARK, TOLLY_FLAG, BILL_TYPE_CODE, BILL_ID, RCID, CCID, LAST_VER, SEND_MSG_DATE,
 AUTO_AUDIT_DATE, SET_MONTH, SET_YEAR, create_user_id,RG_CODE from SYS_WF_CURRENT_TASKS
 union all
 select TASK_ID, WF_ID, WF_TABLE_NAME, ENTITY_ID, CURRENT_NODE_ID, NEXT_NODE_ID, ACTION_TYPE_CODE, IS_UNDO,
 CREATE_USER, CREATE_DATE, UNDO_USER, UNDO_DATE, OPERATION_NAME, OPERATION_DATE, OPERATION_REMARK,
 INIT_MONEY, RESULT_MONEY, REMARK, TOLLY_FLAG, BILL_TYPE_CODE, BILL_ID, RCID, CCID, LAST_VER, SEND_MSG_DATE,
 AUTO_AUDIT_DATE, SET_MONTH, SET_YEAR, create_user_id,RG_CODE from SYS_WF_END_TASKS';

execute immediate ' create or replace view vw_sys_wf_current_item as
select C.BILL_ID, STATUS_CODE,
 case C.STATUS_CODE
 when ''001'' then ''未确认''
 when ''004'' then ''被退回''
 else C.STATUS_CODE
 end as STATUS_NAME,
 C.NODE_ID,
 (select node_name from SYS_WF_NODES n where n.node_id = c.node_id
 and n.set_year = c.set_year and n.RG_CODE = c.RG_CODE)node_name,
 ENTITY_ID, RCID, CCID, C.SET_YEAR, C.RG_CODE,c.MB_ID,c.AGENCY_ID,c.pb_id,c.cb_id,c.ib_id,c.GP_agency_id from SYS_WF_CURRENT_ITEM C';

execute immediate 'create or replace view vw_sys_wf_log as
select T.TASK_ID, T.WF_ID, T.WF_TABLE_NAME, T.ENTITY_ID, T.CURRENT_NODE_ID as NODE_ID, T.ACTION_TYPE_CODE,
 0 as IS_UNDO, T.CREATE_USER, T.CREATE_DATE, T.OPERATION_NAME, T.OPERATION_DATE, T.OPERATION_REMARK,
 T.RESULT_MONEY, T.BILL_TYPE_CODE, T.BILL_ID, (select A.WF_NAME from SYS_WF_FLOWS A where A.WF_ID = T.WF_ID)
 as WF_NAME, (select B.NODE_NAME from SYS_WF_NODES B
 where B.NODE_ID = T.CURRENT_NODE_ID and B.SET_YEAR = T.SET_YEAR and B.RG_CODE = T.RG_CODE) as NODE_NAME,
 (select C.ENU_NAME from SYS_ENUMERATE C where C.FIELD_CODE = ''ACTION_TYPE'' and C.ENU_CODE = T.ACTION_TYPE_CODE)
 as ACTION_TYPE_NAME, T.SET_YEAR, T.RG_CODE from SYS_WF_CURRENT_TASKS T
 union all
 select T.TASK_ID, T.WF_ID, T.WF_TABLE_NAME, T.ENTITY_ID, T.CURRENT_NODE_ID as NODE_ID, T.ACTION_TYPE_CODE,
 T.IS_UNDO, T.UNDO_USER as CREATE_USER, T.UNDO_DATE as CREATE_DATE, ''撤销'' as OPERATION_NAME,
 '''' as OPERATION_DATE, ''撤销'' as OPERATION_REMARK, T.RESULT_MONEY, T.BILL_TYPE_CODE, T.BILL_ID,
 (select A.WF_NAME from SYS_WF_FLOWS A where A.WF_ID = T.WF_ID) as WF_NAME,
 (select B.NODE_NAME from SYS_WF_NODES B
 where B.NODE_ID = T.CURRENT_NODE_ID and B.SET_YEAR = T.SET_YEAR and B.RG_CODE = T.RG_CODE) as NODE_NAME,
 (select C.ENU_NAME from SYS_ENUMERATE C where C.FIELD_CODE = ''ACTION_TYPE'' and C.ENU_CODE = T.ACTION_TYPE_CODE)
 as ACTION_TYPE_NAME, T.SET_YEAR, T.RG_CODE from SYS_WF_CURRENT_TASKS T where T.IS_UNDO = 1
 union all
 select T.TASK_ID, T.WF_ID, T.WF_TABLE_NAME, T.ENTITY_ID, T.CURRENT_NODE_ID as NODE_ID, T.ACTION_TYPE_CODE,
 0 as IS_UNDO, T.CREATE_USER, T.CREATE_DATE, T.OPERATION_NAME, T.OPERATION_DATE, T.OPERATION_REMARK,
 T.RESULT_MONEY, T.BILL_TYPE_CODE, T.BILL_ID, (select A.WF_NAME from SYS_WF_FLOWS A where A.WF_ID = T.WF_ID)
 as WF_NAME, (select B.NODE_NAME from SYS_WF_NODES B
 where B.NODE_ID = T.CURRENT_NODE_ID and B.SET_YEAR = T.SET_YEAR and B.RG_CODE = T.RG_CODE) as NODE_NAME,
 (select C.ENU_NAME from SYS_ENUMERATE C where C.FIELD_CODE = ''ACTION_TYPE'' and C.ENU_CODE = T.ACTION_TYPE_CODE)
 as ACTION_TYPE_NAME, T.SET_YEAR, T.RG_CODE from SYS_WF_COMPLETE_TASKS T
 union all
 select T.TASK_ID, T.WF_ID, T.WF_TABLE_NAME, T.ENTITY_ID, T.CURRENT_NODE_ID as NODE_ID, T.ACTION_TYPE_CODE,
 T.IS_UNDO, T.UNDO_USER as CREATE_USER, T.UNDO_DATE as CREATE_DATE, ''撤销'' as OPERATION_NAME,
 '''' as OPERATION_DATE, ''撤销'' as OPERATION_REMARK, T.RESULT_MONEY, T.BILL_TYPE_CODE, T.BILL_ID,
 (select A.WF_NAME from SYS_WF_FLOWS A where A.WF_ID = T.WF_ID) as WF_NAME,
 (select B.NODE_NAME from SYS_WF_NODES B
 where B.NODE_ID = T.CURRENT_NODE_ID and B.SET_YEAR = T.SET_YEAR and B.RG_CODE = T.RG_CODE) as NODE_NAME,
 (select C.ENU_NAME from SYS_ENUMERATE C where C.FIELD_CODE = ''ACTION_TYPE'' and C.ENU_CODE = T.ACTION_TYPE_CODE)
 as ACTION_TYPE_NAME, T.SET_YEAR, T.RG_CODE from SYS_WF_COMPLETE_TASKS T where T.IS_UNDO = 1';

execute immediate ' create or replace view vw_sys_wf_node_inspect as
select A.MODULE_ID, B.ROLE_ID, C.NODE_ID, C.WF_ID, D.ACTION_TYPE_CODE, D.INSPECT_RULE_ID, A.SET_YEAR, A.RG_CODE
 from SYS_WF_MODULE_NODE A, SYS_WF_ROLE_NODE B, SYS_WF_NODES C, SYS_WF_NODE_ACTION_INSPECT D
 where A.NODE_ID = B.NODE_ID and B.NODE_ID = C.NODE_ID and C.NODE_ID = D.NODE_ID(+)
 and A.SET_YEAR = B.SET_YEAR and B.SET_YEAR = C.SET_YEAR and C.SET_YEAR = D.SET_YEAR(+)
 and A.RG_CODE = B.RG_CODE and B.RG_CODE = C.RG_CODE and C.RG_CODE = D.RG_CODE(+)';

execute immediate ' create or replace view vw_sys_wf_node_relation as
select A.MODULE_ID, B.ROLE_ID, C.NODE_ID, C.WF_ID, C.GATHER_FLAG, D.TOLLY_FLAG,
 D.ACTION_TYPE_CODE, A.SET_YEAR, A.RG_CODE
 from SYS_WF_MODULE_NODE A, SYS_WF_ROLE_NODE B, SYS_WF_NODES C, SYS_WF_NODE_TOLLY_ACTION_TYPE D
 where A.NODE_ID = B.NODE_ID and B.NODE_ID = C.NODE_ID and C.NODE_ID = D.NODE_ID(+)
 and A.SET_YEAR = B.SET_YEAR and B.SET_YEAR = C.SET_YEAR and C.SET_YEAR = D.SET_YEAR(+)
 and A.RG_CODE = B.RG_CODE and B.RG_CODE = C.RG_CODE and C.RG_CODE = D.RG_CODE(+)';

execute immediate ' create or replace view vw_sys_wf_stride_001_and_004 as
select  ''001'' as STATUS_CODE,
 case W.STATUS_CODE
 when ''001'' then ''已确认''
 when ''004'' then ''被退回''
 end as STATUS_NAME,
 C.NODE_ID, C.NODE_NAME, null as TASK_ID, W.ENTITY_ID, W.RCID, W.CCID, null as CREATE_DATE,
 null as SEND_MSG_DATE, null as AUTO_AUDIT_DATE, C.SET_YEAR, C.RG_CODE from SYS_WF_NODES C, SYS_WF_CURRENT_ITEM W
 where C.NODE_ID = W.NODE_ID and C.SET_YEAR = W.SET_YEAR and C.RG_CODE = W.RG_CODE
 and (W.STATUS_CODE = ''001'' or W.STATUS_CODE = ''004'')';

execute immediate ' create or replace view vw_sys_wf_stride_002 as
select D.STATUS_CODE, ''已送审'' as STATUS_NAME, D.NODE_ID, N.NODE_NAME, null as TASK_ID, D.ENTITY_ID,
 D.RCID, D.CCID, null as CREATE_DATE, null as SEND_MSG_DATE, null as AUTO_AUDIT_DATE, N.SET_YEAR, N.RG_CODE
 from SYS_WF_NODES N, SYS_WF_COMPLETE_ITEM D
 where D.NODE_ID = N.NODE_ID and D.SET_YEAR = N.SET_YEAR and D.RG_CODE = N.RG_CODE and D.STATUS_CODE = ''002''';

execute immediate ' create or replace view vw_sys_wf_stride_008 as
select distinct BILL_ID,
 ENTITY_ID,
 decode(ACTION_TYPE_CODE,
 ''NEXT'',
 NEXT_NODE_ID,
 ''BACK'',
 NEXT_NODE_ID,
 ''INPUT'',
 NEXT_NODE_ID,
 CURRENT_NODE_ID) NODE_ID,
 ''008'' as STATUS_CODE,
 ''曾经办'' as STATUS_NAME,
 sw.RCID,
 sw.CCID,
 sw.SET_YEAR,
 sw.RG_CODE,
 nvl(gl.AGENCY_ID, ''#'') AGENCY_ID,
 nvl(gl.MB_ID, ''#'') MB_ID,
 nvl(gl.pb_id, ''#'') pb_id,
 nvl(gl.cb_id, ''#'') cb_id,
 nvl(gl.gP_agency_id, ''#'') GP_agency_id,
 nvl(gl.ib_id, ''#'') ib_id
 from gl_ccids gl, SYS_WF_COMPLETE_TASKS sw
 where gl.ccid = sw.ccid
 and not exists (select 1 from sys_wf_current_tasks st where sw.entity_id = st.entity_id and st.action_type_code = ''DELETE'')';

execute immediate ' create or replace view vw_sys_wf_stride_101 as
select ''101'' as STATUS_CODE, ''已挂起'' as STATUS_NAME, C.NODE_ID, C.NODE_NAME, D.ENTITY_ID, D.RCID, D.CCID,
 C.SET_YEAR, C.RG_CODE from SYS_WF_NODES C, SYS_WF_CURRENT_ITEM D
 where C.NODE_ID = D.NODE_ID and C.SET_YEAR = D.SET_YEAR and C.RG_CODE = D.RG_CODE and D.STATUS_CODE = ''101''';
 
 execute immediate ' create or replace view vw_sys_wf_stride_102 as
select ''102'' as STATUS_CODE, ''已删除'' as STATUS_NAME, C.NODE_ID, C.NODE_NAME, D.ENTITY_ID, D.RCID, D.CCID,
 C.SET_YEAR, C.RG_CODE from SYS_WF_NODES C, SYS_WF_CURRENT_ITEM D
 where C.NODE_ID = D.NODE_ID and C.SET_YEAR = D.SET_YEAR and C.RG_CODE = D.RG_CODE and D.STATUS_CODE = ''102''';

execute immediate ' create or replace view vw_sys_wf_stride_103 as
select STATUS_CODE, ''已作废'' as STATUS_NAME, C.NODE_ID, C.NODE_NAME, D.ENTITY_ID, D.RCID, D.CCID,
 C.SET_YEAR, C.RG_CODE from SYS_WF_NODES C, SYS_WF_CURRENT_ITEM D
 where C.NODE_ID = D.NODE_ID and C.SET_YEAR = D.SET_YEAR and C.RG_CODE = D.RG_CODE and D.STATUS_CODE = ''103''';

execute immediate ' create or replace view vw_sys_wf_stride_all as
select C.BILL_ID, STATUS_CODE,
 case C.STATUS_CODE
 when ''001'' then ''未确认''
 when ''004'' then ''被退回''
 else C.STATUS_CODE
 end as STATUS_NAME,
 C.NODE_ID, NODE_NAME, ENTITY_ID, RCID, CCID, 1 MARK, N.WF_TABLE_NAME, C.SET_YEAR, C.RG_CODE
 from SYS_WF_CURRENT_ITEM C, SYS_WF_NODES N
 where C.NODE_ID = N.NODE_ID and C.SET_YEAR = N.SET_YEAR and C.RG_CODE = N.RG_CODE
 union all
 select SC.BILL_ID, STATUS_CODE,
 case SC.STATUS_CODE
 when ''101'' then ''已挂起''
 when ''102'' then ''已删除''
 when ''103'' then ''已作废''
 else SC.STATUS_CODE
 end as STATUS_NAME,
 SC.NODE_ID, NODE_NAME, ENTITY_ID, RCID, CCID, 2 MARK, SN.WF_TABLE_NAME, SC.SET_YEAR, SC.RG_CODE
 from SYS_WF_COMPLETE_ITEM SC, SYS_WF_NODES SN
 where SC.NODE_ID = SN.NODE_ID and SC.SET_YEAR = SN.SET_YEAR and SC.RG_CODE = SN.RG_CODE
 and SC.STATUS_CODE <> ''002'' and SC.STATUS_CODE <> ''003''
 union all
 select SC.BILL_ID, STATUS_CODE,
 case SC.STATUS_CODE
 when ''101'' then ''已挂起''
 when ''102'' then ''已删除''
 when ''103'' then ''已作废''
 when ''002'' then ''已确认''
 else SC.STATUS_CODE
 end as STATUS_NAME,
 SN.NODE_ID, NODE_NAME, ENTITY_ID, RCID, CCID, 3 MARK, SN.WF_TABLE_NAME, SC.SET_YEAR, SC.RG_CODE
 from SYS_WF_COMPLETE_ITEM SC, SYS_WF_NODES SN, SYS_WF_NODE_CONDITIONS NC2
 where SC.NODE_ID = NC2.NODE_ID and SN.NODE_ID = NC2.NEXT_NODE_ID
 and SC.SET_YEAR = NC2.SET_YEAR and SN.SET_YEAR = NC2.SET_YEAR
 and SC.RG_CODE = NC2.RG_CODE and SN.RG_CODE = NC2.RG_CODE
 and NC2.ROUTING_TYPE = ''001'' and SC.STATUS_CODE = ''002'' and exists
 (select 1 from SYS_WF_NODES WN, SYS_WF_NODE_CONDITIONS NC
 where NC.NEXT_NODE_ID = WN.NODE_ID and SC.NODE_ID = NC.NODE_ID
 and NC.SET_YEAR = WN.SET_YEAR and SC.SET_YEAR = NC.SET_YEAR
 and NC.RG_CODE = WN.RG_CODE and SC.RG_CODE = NC.RG_CODE
 and WN.NODE_TYPE = ''003'' and NC.ROUTING_TYPE = ''001'') and not exists
 (select 1 from SYS_WF_CURRENT_ITEM CI, SYS_WF_NODES SWN where CI.ENTITY_ID = SC.ENTITY_ID
 and CI.NODE_ID = SWN.NODE_ID and CI.SET_YEAR = SWN.SET_YEAR and CI.RG_CODE = SWN.RG_CODE
 and SWN.WF_TABLE_NAME = SN.WF_TABLE_NAME)';

execute immediate ' create or replace view vw_sys_wf_stride_all_all as
select C.BILL_ID, STATUS_CODE,
 case C.STATUS_CODE
 when ''001'' then ''未确认''
 when ''004'' then ''被退回''
 else C.STATUS_CODE
 end as STATUS_NAME,
 C.NODE_ID, NODE_NAME, ENTITY_ID, RCID, CCID, C.SET_YEAR, C.RG_CODE from SYS_WF_CURRENT_ITEM C, SYS_WF_NODES N
 where C.NODE_ID = N.NODE_ID and C.SET_YEAR = N.SET_YEAR and C.RG_CODE = N.RG_CODE
 union all
 select SC.BILL_ID, STATUS_CODE,
 case SC.STATUS_CODE
 when ''101'' then ''已挂起''
 when ''102'' then ''已删除''
 when ''103'' then ''已作废''
 else SC.STATUS_CODE
 end as STATUS_NAME,
 SC.NODE_ID, NODE_NAME, ENTITY_ID, RCID, CCID, SC.SET_YEAR, SC.RG_CODE from SYS_WF_COMPLETE_ITEM SC, SYS_WF_NODES SN
 where SC.NODE_ID = SN.NODE_ID and SC.SET_YEAR = SN.SET_YEAR and SC.RG_CODE = SN.RG_CODE and SC.STATUS_CODE <> 001';

execute immediate ' create or replace view vw_sys_wf_tasks_999_tb as
select ''999'' as STATUS_CODE, RO.ROLE_ID, MO.MODULE_ID, TASK.ENTITY_ID, TASK.RCID, TASK.CCID,
 null as OUTER_TABLE_NAME, decode(TASK.BILL_ID, null, TASK.ENTITY_ID, TASK.BILL_ID) as BILL_ID,
 TASK.SET_YEAR, TASK.RG_CODE from (select * from SYS_WF_CURRENT_TASKS
 union all
 select * from SYS_WF_END_TASKS
 union all
 select * from SYS_WF_COMPLETE_TASKS) TASK, SYS_WF_MODULE_NODE MO, SYS_WF_ROLE_NODE RO
 where MO.NODE_ID = RO.NODE_ID and TASK.CURRENT_NODE_ID = MO.NODE_ID
 and MO.SET_YEAR = RO.SET_YEAR and TASK.SET_YEAR = MO.SET_YEAR
 and MO.RG_CODE = RO.RG_CODE and TASK.RG_CODE = MO.RG_CODE';

execute immediate ' 
create or replace view VW_YEAR_INIT_EXP_TABLES AS
SELECT TABLE_NAME
 FROM USER_TABLES U
 WHERE  U.table_name NOT LIKE ''TEMP''
 AND  U.table_name NOT LIKE ''%CACHE''
 AND  U.table_name NOT LIKE ''%BAK''
 AND  U.TEMPORARY = ''N''
 AND  U.table_name  NOT IN
 (''GL_REMAIN'',
 ''GL_VOUCHER_DETAIL'',
 ''GL_VOUCHER'',
 ''GL_VOU_BALANCE'',
 ''GL_VOU_BILL_RELATION'',
 ''GL_VOU_BILLDETAIL_RELATION'',
 ''GL_INCOME_INTERFACE'',
 ''GL_PAY_INTERFACE'',
 ''GL_PRINT_VOUCHER'',
 ''GL_MONEY_RELATION'',
 ''GLV_BALANCE'',
 ''GL_PAY_NOBUDGET'',
 ''GL_ACCBOOK_DIRECTORY'',
 ''ELE_ASRELATION_DEFINE'',
 ''GLV_BALANCE_TRACE'',
 ''GL_BALANCE'',
 ''GL_BALANCE_MONTH_DETAIL'',
 ''GL_BALANCE_BUDGET_FILE_TRANS'',
 ''GL_BALANCE_TRACE'',
 ''GL_BALANCE_CACHE'',
 ''GL_BALANCE_YEAR_BEGIN_TMP'',
 ''GL_JOURNAL'',
 ''GL_JOURNAL_CACHE'',
 ''GL_JOURNAL_HIS'',
 ''GL_CCID_CONFLICT'',
 ''GL_CCIDS'',
 ''GL_CCID_TRANS'',
 ''GL_MONTHLY_BALANCE'',
 ''GL_ERROR_TYPE'',
 ''GL_ERROR_DATA_001'',
 ''GL_ERROR_DATA_002'',
 ''GL_ERROR_DATA_003'',
 ''GL_ERROR_DATA_004'',
 ''GL_ERROR_DATA_005'',
 ''GL_ERROR_DATA_006'',
 ''GL_ERROR_DATA_007'',
 ''GL_ERROR_DATA_008'',
 ''BUDGET_CONTROL_REPLACE'',
 ''BUDGET_CARRYFORWARD_VOUCHER'',
 ''BUDGET_CARRYFORWARD_LOG'',
 ''BUDGET_CONTROL'',
 ''BUDGET_CONTROL_BILL'',
 ''BUDGET_AUTOPAY_CACHE'',
 ''BUDGET_GRANT'',
 ''BUDGET_INTERFACE_ELE_TEMP'',
 ''BUDGET_INTERFACE_TEMP'',
 ''BUDGET_VOUCHER'',
 ''BUDGET_VOUCHER_BILL'',
 ''BUDGET_VOUCHER_CACHE'',
 ''ELE_BUDGET_SUMMARY'',
 ''BUDGET_ERR_LOG'',
 ''BUDGET_PRINT_TYPE'',
 ''BUDGET_QUERY_TREE'',
 ''BUDGET_REFORM_PK'',
 ''BUDGET_WORKFLOW_TYPE'',
 ''BUDGET_VOUCHER_MODIFY_LOG'',
 ''BUDGET_MODIFY_INTERFACE'',
 ''CONTROL_REPLACE_LOG'',
 ''BUDGET_CARRYFORWARD_TEMP'',
 ''BUDGET_EXPORTINFO'',
 ''BUDGET_USEABLE_VOUCHER'',
 ''BUDGET_USEABLE_CACHE'',
 ''BUDGET_DOING'',
 ''PLAN_DETAIL_DETAIL'',
 ''PLAN_DETAIL'',
 ''PAY_DETAIL'',
 ''PAY'',
 ''BUDGET_VOUCHER_MATCHING_RULE'',
 ''BUDGET_VOUCHER_USE'',
 ''PAY_VOUCHER'',
 ''PAY_EN_VOUCHER'',
 ''PAY_EN_VOUCHER_HIS'',
 ''PAY_EN_BILL'',
 ''PAY_EN_BILL_HIS'',
 ''PAY_ACC_BILL'',
 ''PAY_AGENT_BILL'',
 ''PAY_CLEAR_BILL'',
 ''PAY_EN_BILL'',
 ''PAY_DAY_BILL'',
 ''PAY_VOUCHER_BILL'',
 ''PAY_REAL_BUDGET_MATCH'',
 ''PAY_CONSIGN_SETTING'',
 ''PAY_BILL_INTERFACE'',
 ''PAY_VOUCHER_INTERFACE'',
 ''PAY_SALARY_INTERFACE'',
 ''PAY_SALARY_DETAIL_INTERFACE'',
 ''PAY_SALARY_DATA'',
 ''PAY_CONSIGN_SETTING'',
 ''PAY_EN_VOUCHER_INTERFACE'',
 ''PAY_INCOME_BALANCE'',
 ''PAY_INCOME_VOUCHER'',
 ''PAY_INCOME_VOUCHER'',
 ''PAY_PAYEE_GROUP'',
 ''PAY_PAYEE_GROUP_DETAIL'',
 ''PAY_CARD_DETAIL'',
 ''PLAN_VOUCHER'',
 ''PLAN_AGENT_BILL'',
 ''PLAN_AGENTEN_BILL'',
 ''PLAN_CHECKED_BILL'',
 ''PLAN_CLEAR_BILL'',
 ''PLAN_EN_BILL'',
 ''PLAN_EN_BILL_HIS'',
 ''PLAN_GOV_BILL'',
 ''PLAN_VOUCHER_INTERFACE'',
 ''PLAN_BILL_INTERFACE'',
 ''PLAN_GOV_SZ_TRANS'',
 ''PLAN_GOV_BILL_COMPACT'',
 ''PLAN_DIVIDE_RELATION'',
 ''PLAN_DIVIDE_RELATION_CACHE'',
 ''INSPECT_BANK_FLOW'',
 ''INSPECT_BANK_FLOW_RESULT'',
 ''INSPECT_ERROR_INSTANCE'',
 ''INSPECT_ERROR_INSTANCE_TASK'',
 ''INSPECT_ERROR_LICENSE'',
 ''INSPECT_EXCHANGE_DETAIL'',
 ''INSPECT_FLOW_LOG'',
 ''INSPECT_FLOW_RESULT'',
 ''INSPECT_FLOW_RESULT_CACHE'',
 ''INSPECT_GWK_RESULT'',
 ''INSPECT_INTERFACE_LOG'',
 ''INSPECT_MSG_RECORD'',
 ''INSPECT_MSG_ROLE_USER'',
 ''INSPECT_PAY_CARD_DETAIL'',
 ''INSPECT_PAY_NOBUDGET'',
 ''INSPECT_TODO'',
 ''INSPECT_VW_PAY_VOUCHER'',
 ''ACCBANK_INTERFACE'',
 ''ACC_ACCOUNTAPPLY'',
 ''ACC_ACCOUNT_CHECK'',
 ''ACC_ACCOUNT_HISTORY'',
 ''ACC_ELE_BANK'',
 ''ACC_ENTERPRISE_CHANGE'',
 ''ACC_EN_BELONGS_ACCOUNT'',
 ''ACC_EN_BELONGS_COPY_ACCOUNT'',
 ''ACC_UNIT_ACCOUNT'',
 ''ACC_UNIT_ACCOUNT_HISTORY'',
 ''ACC_UNIT_ACCOUNT_HISTORY_BILL'',
 ''CM_ACCOUNT_BALANCE'',
 ''CM_EXCHANGE_BILL'',
 ''CM_EXCHANGE_DETAIL'',
 ''CM_EXCHANGE_LOG'',
 ''CM_FIXED_DEPOSIT_BILL'',
 ''CM_FIXED_DEPOSIT_DETAIL'',
 ''CM_FIXED_DEPOSIT_HISTORY'',
 ''ACC_ACCOUNTSUPERVISE'',
 ''ACC_ACCOUNTSUPERVISEDISPOSAL'',
 ''SYS_WF_COMPLETE_ITEM'',
 ''SYS_WF_COMPLETE_TASKS'',
 ''SYS_WF_CURRENT_ITEM'',
 ''SYS_WF_CURRENT_TASKS'',
 ''SYS_WF_TASK_ROUTING'',
 ''AS_GROUP'',
 ''AS_IP'',
 ''AS_USER_SESSION'',
 ''AS_PLUGINS_INFO'',
 ''AS_POSITION'',
 ''AS_PRINT_JASPERTEMP'',
 ''AS_WF_ACTION_RMD_HISTORY'',
 ''FR_DATASET'',
 ''NT_LOG_RECORD'',
 ''PCIS_ADJUSTINFO'',
 ''WF_TEMPLATE'',
 ''AS_WF_ACTION_RMD_HISTORY'',
 ''FR_DATASET'',
 ''NT_LOG_RECORD'',
 ''PCIS_ADJUSTINFO'',
 ''WF_TEMPLATE'',
 ''ZC_EB_SIGNUP_PACK'',
 ''AS_FILE'',
 ''BUDGET_GOVDOC_FILE_TEMPLATE'',
 ''FI_FR_DATASET'',
 ''FI_FR_ARCHIVE'',
 ''FI_FR_TEMPLET'',
 ''FR_TEMPLET'',
 ''GL_BUSVOU_TYPE_BYTE'',
 ''GL_CONFIG_MANAGE'',
 ''PAFB_S_PRINTINFO'',
 ''PL_CONFIGURE_BATCH'',
 ''SYS_WF_FLOWS_BYTE'',
 ''ZC_USER_PREFERENCES'',
 ''FB_U_QR_SQLLINES'',
 ''FB_U_SQL'',
 ''FI_ATTACH'',
 ''ELE_INCOME_ITEM'',
 ''ELE_INCOME_ITEM_IMP'',
 ''FR_ARCHIVE'',
 ''ZC_EB_EVAL_REPORT'',
 ''ZC_EB_EVAL_REPORT'',
 ''ZC_EB_EVAL_REPORT'',
 ''REPORT_CONTENT'',--以下表都含有大字段或LONG类型的字段
 ''BUDGET_OB'',
 ''FB_P_AFFIX'',
 ''GL_RIGHT_MANAGE'',
 ''AP_FILE'',
 ''AP_JFORUM_MODERATION_LOG'',
 ''AP_JFORUM_MODERATION_LOG'',
 ''AP_JFORUM_POSTS_TEXT'',
 ''AP_JFORUM_PRIVMSGS_TEXT'',
 ''AP_MESSAGE_BOARD'',
 ''AP_MESSAGE_BOARD'',
 ''AP_PAGE_TEMP'',
 ''AP_ARTICLE'',
 ''AP_ARTICLE_ATTACH'',
 ''PLAN_FUNC_CONFIG'',
 ''FB_BAS_R_SQLLINES'',
 ''FB_BAS_S_PRINTINFO'',
 ''SYS_WF_FLOWS_BYTE_CACHE'',
 ''UNTAX_FILEBYMANAGE'',
 ''UNTAX_FILEBYMANAGE_OB'',
 ''UNTAX_INCITEM'',
 ''PA_SF_APPRAISE_AFFIAX'',
 ''PA_SF_APPRAISE_DESTINCT_AFFIAX'',
 ''PA_T_AFFIX'',
 ''PAFB_U_QR_GROUPREPORT'',
 ''PAFB_U_QR_SQLLINES'',
 ''FB_S_IMPORT_TABLE'',
 ''FB_S_OFFLINE_UPDATE'',
 ''FB_S_OFFLINE_UPDATE'',
 ''FB_S_PRINTINFO'',
 ''PAFB_TASK'',
 ''PAFB_T_AFFIX'',
 ''PA_PROJECT_DETAIL_AFFIAX'',
 ''AP_PORTLET'',
 ''AP_PORTLET_LABEL'',
 ''AS_CUSTOM'',
 ''AS_INFO'',
 ''AS_MESSAGE'',
 ''AS_MESSAGE_FILE'',
 ''AS_SETUP'',
 ''UNTAX_ITEM_HIS'',
 ''FB_P_DLIB_PERFORMANCE_AFFIX'',
 ''FB_S_BLOBFILE'',
 ''FB_U_DIV_DOC'',
 ''FB_BUDGET_FILE_DIV'',
 ''FB_DS_PRINT_REPORT'',
 ''PCIS_RETURNMESSAGE'',
 ''PCIS_SENDMESSAGE'',
 ''FB_P_MODEL_AFFIX'',
 ''FB_U_QR_GROUPREPORT'',
 ''PA_PRO_ZJ_FILE'',
 ''FB_POLICY_INFO'',
 ''FB_POLICY_LIB_ACCOUNT'',
 ''FB_POLICY_LIB_INFO'',
 ''FB_U_PAYOUT_AFFIX'',
 ''BUDGET_GOVDOC_FILE'',
 ''PAFB_FU_AFFIX'',
 ''PAFB_QC_REPORT'',
 ''PAFB_QC_REPORT'',
 ''PAFB_QC_REPORT'',
 ''PAFB_QC_REPORT'',
 ''FB_BAS_B_AFFIX'',
 ''V_AP_ARTICLE_PUB'',
 ''UNTAX_ITEM'',
 ''VW_BUDGET_GOVDOC_FILE_ATTA'',
 ''VW_BUDGET_GOVDOC_FILE'')
';
execute immediate ' create or replace view gl_sum_type as
select account_id   as sum_type_id,
 account_code as sum_type_code,
 account_name as sum_type_name,
 coa_id       as coa_id,
 set_year     as set_year,
 RG_CODE
 from vw_gl_account';
 
 execute immediate ' create or replace view vm_sys_user_org as
select b.user_id,
 b.user_code,
 c.chr_code as org_code,
 b.org_type
 from sys_user_org a, sys_usermanage b,vw_sys_orgs c
 where a.user_id = b.user_id and a.org_id=c.chr_id';

 execute immediate ' CREATE OR REPLACE VIEW VW_GL_BALANCE_DETAIL AS
SELECT
 RG_CODE,
 BALANCE_ID,
 ACCOUNT_ID as SUM_TYPE_ID,
 SUM_ID,
 CREATE_DATE,
 CREATE_USER,
 LATEST_OP_DATE,
 LATEST_OP_USER,
 SET_YEAR,
 SET_MONTH,
 AVI_MONEY,
 USE_MONEY,
 MINUS_MONEY,
 AVING_MONEY,
 LAST_VER,
 FROMCTRLID,
 REMARK,
 BAL_VERSION,
 RCID,
 CCID
 FROM GL_BALANCE_MONTH_DETAIL';

execute immediate 'CREATE OR REPLACE VIEW VW_TRACE_BALANCE_MONEYS AS
SELECT BAL.SUM_ID,
 sum(BAL.CANUSE_MONEY) AS CANUSE_MONEY,
 sum(BAL.AVI_MONEY) AS AVI_MONEY,
 sum(BAL.USE_MONEY) AS USE_MONEY,
 sum(BAL.MINUS_MONEY) AS MINUS_MONEY,
 sum(BAL.AVING_MONEY) AS AVING_MONEY,
 max(BAL.FROMCTRLID) AS fromctrlid,
 max(BAL.BAL_VERSION) AS bal_version,
 max(C.ccid) AS ccid,
 max(C.COA_ID) AS COA_ID,
 max(C.AGENCYEXP_ID) as AGENCYEXP_ID,
 max(C.AGENCYEXP_CODE) as AGENCYEXP_CODE,
 max(C.AGENCYEXP_NAME) as AGENCYEXP_NAME,
 max(C.BIS_ID) AS BIS_ID,
 max(C.BIS_CODE) AS BIS_CODE,
 max(C.BIS_NAME) AS BIS_NAME,
 max(C.BGTSOURCE_ID) as BGTSOURCE_ID,
 max(C.BGTSOURCE_CODE) as BGTSOURCE_CODE,
 max(C.BGTSOURCE_NAME) as BGTSOURCE_NAME,
 max(C.BGTTYPE_ID) as BGTTYPE_ID,
 max(C.BGTTYPE_CODE) as BGTTYPE_CODE,
 max(C.BGTTYPE_NAME) as BGTTYPE_NAME,
 max(C.BP_ID) AS BP_ID,
 max(C.BP_CODE) AS BP_CODE,
 max(C.BP_NAME) AS BP_NAME,
 max(C.EXPFUNC_ID) as EXPFUNC_ID,
 max(C.EXPFUNC_CODE) as EXPFUNC_CODE,
 max(C.EXPFUNC_NAME) as EXPFUNC_NAME,
 max(C.BGTDIR_ID) as BGTDIR_ID,
 max(C.BGTDIR_CODE) as BGTDIR_CODE,
 max(C.BGTDIR_NAME) as BGTDIR_NAME,
 max(C.BUDGET_VOU_ID) AS BUDGET_VOU_ID,
 max(C.BUDGET_VOU_CODE) AS BUDGET_VOU_CODE,
 max(C.BUDGET_VOU_NAME) AS BUDGET_VOU_NAME,
 max(C.AGENCY_ID) as GP_AGENCY_ID,
 max(C.AGENCY_CODE) as GP_AGENCY_CODE,
 max(C.AGENCY_NAME) as GP_AGENCY_NAME,
 max(C.FILE_ID) AS FILE_ID,
 max(C.FILE_CODE) AS FILE_CODE,
 max(C.FILE_NAME) AS FILE_NAME,
 max(C.ZFCGBS_ID) as ZFCGBS_ID,
 max(C.ZFCGBS_CODE) as ZFCGBS_CODE,
 max(C.ZFCGBS_NAME) as ZFCGBS_NAME,
 max(C.MB_ID) AS MB_ID,
 max(C.MB_CODE) AS MB_CODE,
 max(C.MB_NAME) AS MB_NAME,
 max(C.FUNDTYPE_ID) as FUNDTYPE_ID,
 max(C.FUNDTYPE_CODE) as FUNDTYPE_CODE,
 max(C.FUNDTYPE_NAME) as FUNDTYPE_NAME,
 max(C.PAYTYPE_ID) as PAYTYPE_ID,
 max(C.PAYTYPE_CODE) as PAYTYPE_CODE,
 max(C.PAYTYPE_NAME) as PAYTYPE_NAME,
 max(C.GZBS_ID) as GZBS_ID,
 max(C.GZBS_CODE) as GZBS_CODE,
 max(C.GZBS_NAME) as GZBS_NAME,
 max(C.SM_ID) AS SM_ID,
 max(C.SM_CODE) AS SM_CODE,
 max(C.SM_NAME) AS SM_NAME,
 C.RG_CODE,
 C.SET_YEAR
 FROM (SELECT B.SUM_ID AS SUM_ID,
 NVL((B.AVI_MONEY - B.USE_MONEY - B.MINUS_MONEY), 0) AS CANUSE_MONEY,
 NVL(B.AVI_MONEY, 0) AS AVI_MONEY,
 NVL(B.USE_MONEY, 0) AS USE_MONEY,
 NVL(B.MINUS_MONEY, 0) AS MINUS_MONEY,
 NVL(B.AVING_MONEY, 0) AS AVING_MONEY,
 B.CCID AS CCID,
 B.SET_MONTH AS SET_MONTH,
 B.FROMCTRLID AS FROMCTRLID,
 B.RCID AS RCID,
 B.BAL_VERSION AS BAL_VERSION,
 B.RG_CODE,
 B.SET_YEAR
 FROM Gl_Balance_Month_Detail B
 WHERE B.SET_YEAR = to_char(SYSDATE, ''yyyy'')
 AND B.BAL_VERSION <> -1) BAL,
 GL_CCIDS C
 WHERE BAL.CCID = C.CCID
 and c.rg_code = bal.rg_code
 and c.set_year = bal.set_year
 GROUP BY BAL.SUM_ID, C.RG_CODE, C.SET_YEAR';

 execute immediate ' create or replace view vw_pay_query_balance_budget as
select b.SUM_ID ctrl_id,
 b.RG_CODE,
 b.SET_MONTH,
 b.SET_YEAR,
 b.AVI_MONEY,
 b.USE_MONEY,
 b.MINUS_MONEY,
 b.AVING_MONEY,
 b.BAL_VERSION,
 b.CCID,
 b.RCID,
 b.FROMCTRLID balance_ctrl_id,
 cc.AGENCY_CODE,
 cc.AGENCY_NAME,
 cc.FUNDTYPE_CODE,
 cc.FUNDTYPE_name,
 cc.PAYTYPE_code,
 cc.PAYTYPE_name,
 cc.EXPFUNC_CODE,
 cc.EXPFUNC_NAME,
 cc.EXPECO_CODE,
 cc.EXPECO_name,
 cc.MB_CODE,
 cc.MB_NAME,
 cc.file_code,
 cc.file_name,
 cc.bis_code,
 cc.bis_name,
 cc.AGENCYEXP_code,
 cc.AGENCYEXP_name,
 cc.BGTTYPE_CODE,
 cc.BGTTYPE_NAME,
 cc.bp_code,
 cc.bp_name,
 cc.BGTSOURCE_CODE,
 cc.BGTSOURCE_NAME,
 cc.sm_name,
 cc.BGTDIR_code,
 cc.BGTDIR_name
 from vw_gl_balance b
 inner join gl_ccids cc on cc.ccid = b.ccid
 where b.SUM_TYPE_ID = (select sum_type_id
 from gl_sum_type st
 where st.sum_type_code = 635
 and st.RG_CODE = cc.RG_CODE
 and st.set_year = cc.set_year)
 and b.AVI_MONEY <> 0';





