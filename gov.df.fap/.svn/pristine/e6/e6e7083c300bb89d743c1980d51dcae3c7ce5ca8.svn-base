begin
execute immediate 'CREATE OR REPLACE PROCEDURE AUTOTASKMANAGER (srcRgCode in varchar2,
 destRgCode in varchar2,
 srcSetYear in varchar2,
 destSetYear in varchar2) as
 begin
 declare
 taskCount number;
 tempSql   varchar2(1000);
 begin
 execute immediate ''DELETE FROM SYS_AUTOTASK WHERE SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''DELETE FROM SYS_AUTOTASK_MONITOR WHERE SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 SELECT COUNT(*) INTO taskCount FROM user_tables WHERE table_name = ''SYS_TASK_TEMP_TAB'';
 IF taskCount > 0 THEN
 EXECUTE IMMEDIATE ''Drop table SYS_TASK_TEMP_TAB'';
 END IF;
 EXECUTE IMMEDIATE ''create table SYS_TASK_TEMP_TAB(old_task_id number,new_task_id number)'';
 tempSql := ''insert into SYS_TASK_TEMP_TAB(old_task_id,new_task_id)
 select autotask_id,SEQ_SYS_FRAME_ID.nextval
 from SYS_AUTOTASK sa
 where sa.RG_CODE ='''''' || srcrgcode || ''''''
 and sa.set_year ='' || srcSetYear;
 EXECUTE IMMEDIATE tempSql;
 commit;
 tempSql := ''INSERT INTO SYS_AUTOTASK (AUTOTASK_ID,AUTOTASK_CODE,AUTOTASK_NAME,AUTOTASK_DESC
 ,AUTOTASK_TYPE,START_DATE,END_DATE,TASKINTERVAL,RUN_TIMES,ENABLED,AUTOTASK_BEAN,AUTOTASK_PARAM,REMARK
 ,CREATE_USER,CREATE_DATE,LAST_VER,SCHEDULE_CRONTYPE,MONTH_OF_YEAR,DAY_OF_WEEK,DAY_OF_MONTH,HOUR_OF_DAY
 ,MINUTE_OF_DAY,SYS_ID,RG_CODE,SET_YEAR) SELECT (select new_task_id from SYS_TASK_TEMP_TAB sttb where sttb.old_task_id=sa.autotask_id) AS AUTOTASK_ID,AUTOTASK_CODE,
 AUTOTASK_NAME,AUTOTASK_DESC
 ,AUTOTASK_TYPE,START_DATE,END_DATE,TASKINTERVAL,RUN_TIMES,ENABLED,AUTOTASK_BEAN,AUTOTASK_PARAM,REMARK
 ,CREATE_USER,CREATE_DATE,LAST_VER,SCHEDULE_CRONTYPE,MONTH_OF_YEAR,DAY_OF_WEEK,DAY_OF_MONTH,HOUR_OF_DAY
 ,MINUTE_OF_DAY,SYS_ID,''''''||destRgCode||'''''' AS RG_CODE,''''''||destSetYear||'''''' AS SET_YEAR FROM SYS_AUTOTASK SA WHERE SA.RG_CODE=''''''||srcRgCode
 ||'''''' AND SA.SET_YEAR=''||srcSetYear;
 execute immediate tempSql;
 commit;
 tempSql :=''INSERT INTO SYS_AUTOTASK_MONITOR (AUTOTASK_ID,START_TIME,LAST_EXE_TIME,TOTALTIME,TOTAL_COUNT,FAIL_COUNT,SUCCESS_COUNT
 ,TASK_STATUS,RG_CODE,SET_YEAR) SELECT (select new_task_id from SYS_TASK_TEMP_TAB sttb where sttb.old_task_id=sam.autotask_id) as AUTOTASK_ID,START_TIME,LAST_EXE_TIME,TOTALTIME,TOTAL_COUNT,FAIL_COUNT,SUCCESS_COUNT
 ,TASK_STATUS,''''''||destRgCode||'''''' AS RG_CODE,''''''||destSetYear||'''''' AS SET_YEAR
 FROM SYS_AUTOTASK_MONITOR SAM WHERE SAM.RG_CODE=''''''||srcRgCode||'''''' AND SAM.SET_YEAR=''||srcSetYear;
 execute immediate tempSql;
 commit;
 tempSql :=''drop table SYS_TASK_TEMP_TAB'';
 execute immediate tempSql;
 commit;
 end;
 end autoTaskManager;';

execute immediate 'CREATE OR REPLACE PROCEDURE basicdatamanager(srcrgcode   IN VARCHAR2,
 destrgcode  IN VARCHAR2,
 srcsetyear  IN VARCHAR2,
 destsetyear IN VARCHAR2) AS
 BEGIN
 DECLARE
 TYPE lccursor IS REF CURSOR;
 TYPE varchar2array IS TABLE OF VARCHAR2(42) INDEX BY BINARY_INTEGER;
 array_tablename    varchar2array;
 tablename          VARCHAR2(42);
 other_table_cursor lccursor; --游标，初始化新年度时 用于拷贝基础要素表数据
 other_tablename    VARCHAR2(42);
 is_need_copy       BOOLEAN;
 BEGIN
 is_need_copy := TRUE;
 array_tablename(1) := ''ELE_BUDGET_SUBJECT_INCOME'';
 array_tablename(2) := ''ELE_MONEY_KIND'';
 array_tablename(3) := ''ELE_BUDGET_CATEGORY'';
 array_tablename(4) := ''ELE_BUDGET_PURPOSE'';
 array_tablename(5) := ''ELE_BUDGET_LABEL'';
 array_tablename(6) := ''ELE_BUDGET_SUBJECT_OLD'';
 array_tablename(7) := ''ELE_PAY_FASHION'';
 array_tablename(8) := ''ELE_SALARYTAG'';
 array_tablename(9) := ''ELE_GOV_BUY'';
 array_tablename(10) := ''ELE_BUDGET_SUBJECT_ITEM'';
 array_tablename(11) := ''ELE_INCOMEPAYMANAGE'';
 array_tablename(12) := ''ELE_SUMMARY'';
 array_tablename(13) := ''ELE_BUDGET_SUBJECT'';
 array_tablename(14) := ''ELE_MANAGE_BRANCH'';
 array_tablename(15) := ''ELE_PAYOFF_KIND'';
 array_tablename(16) := ''ELE_BUDGET_KIND'';
 array_tablename(17) := ''ELE_BUDGET_ITEM'';
 array_tablename(18) := ''ELE_BUDGET_ORIGIN'';
 array_tablename(19) := ''ELE_BUDGET_TARGET'';
 array_tablename(20) := ''ELE_BOOK_SET'';
 array_tablename(21) := ''ELE_ACCOUNTANT_SUBJECT'';
 array_tablename(22) := ''ELE_FOREIGN_MONEY'';
 DECLARE
 varhastable NUMBER;
 varcount    NUMBER := 22;
 BEGIN
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ELE_PAY_FILL_MODE''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ELE_PAY_FILL_MODE'';
 END IF;
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ELE_PAY_ADJUST''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ELE_PAY_ADJUST'';
 END IF;
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ELE_PAY_MODE''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ELE_PAY_MODE'';
 END IF;
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ELE_PLAN_ADJUST''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ELE_PLAN_ADJUST'';
 END IF;
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ELE_CLEAR_INTERFACE_DATE''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ELE_CLEAR_INTERFACE_DATE'';
 END IF;
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ELE_PAY_SUMMARY''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ELE_PAY_SUMMARY'';
 END IF;
 IF srcrgcode = destrgcode AND srcsetyear <> destsetyear THEN
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ELE_ACCOUNT''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ELE_ACCOUNT'';
 END IF;
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ELE_BANK''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ELE_BANK'';
 END IF;
 EXECUTE IMMEDIATE ''select count(*) from USER_TABLES where TABLE_NAME = ''''ACC_ELE_ACCOUNT''''''
 INTO varhastable;
 IF varhastable > 0 THEN
 varcount := varcount + 1;
 array_tablename(varcount) := ''ACC_ELE_ACCOUNT'';
 END IF;
 OPEN other_table_cursor FOR
 SELECT ele_source
 FROM sys_element
 WHERE RG_CODE = srcrgcode
 AND set_year = srcsetyear
 AND ele_source NOT IN
 (''ELE_INCOME_ITEM'', ''ELE_PERTYPE'', ''ELE_INSPECT_KEYWORD'', ''ACC_ACCOUNT_USE'',''ACC_ACCOUNT_TYPE'',''ACC_BANK_CITY'',
 ''ACC_ELE_ENTERPRISE_BELONGS_EN'', ''ACC_SPECIAL_BASIS'',
 ''SAL_BATCH'', ''SAL_POSSALGR'', ''ELE_INSPECT_NSKIND'', ''SAL_ENTYPE'',
 ''ELE_SALTYPE'', ''SAL_SALARYGR'', ''ELE_INSPECT_BAKIND'', ''SAL_BZLX'', ''SAL_PTPOSSALGR'', ''ACC_ACCOUNT_KIND'',
 ''ELE_INSPECT_GRADE'', ''ELE_REGION'', ''SAL_DEPARTMENT'', ''ELE_BUDGET_SUMMARY'', ''UNTAX_BILLNAME'', ''UNTAX_BILLPRICE'',
 ''UNTAX_BILLPRINTERY'', ''ELE_BILLKIND'', ''ELE_INCOME_ENTERPRISE'', ''UNTAX_IENUSERMANAGE'',''ELE_BUDGET_ITEM_SUMMARY'')
 AND EXISTS (SELECT 1 FROM user_tables WHERE ele_source = table_name)
 AND EXISTS (SELECT 1
 FROM user_tab_columns c
 WHERE c.column_name = ''CHR_ID''
 AND c.table_name = ele_source)
 AND EXISTS (SELECT 1
 FROM user_tab_columns c
 WHERE c.column_name = ''CHR_CODE''
 AND c.table_name = ele_source)
 AND EXISTS (SELECT 1
 FROM user_tab_columns c
 WHERE c.column_name = ''SET_YEAR''
 AND c.table_name = ele_source)
 AND EXISTS (SELECT 1
 FROM user_tab_columns c
 WHERE c.column_name = ''RG_CODE''
 AND c.table_name = ele_source)
 AND NOT EXISTS (SELECT 1
 FROM user_tab_columns c
 WHERE c.column_name LIKE ''%ACCOUNT%''
 AND c.table_name = ele_source)
 AND EXISTS (SELECT 1
 FROM user_tab_columns c
 WHERE c.column_name = ''DISP_CODE''
 AND c.table_name = ele_source);
 FETCH other_table_cursor
 INTO other_tablename;
 WHILE other_table_cursor%FOUND LOOP
 FOR i IN 1 .. array_tablename.count LOOP
 IF array_tablename(i) = other_tablename THEN
 is_need_copy := FALSE;
 END IF;
 END LOOP;
 IF is_need_copy = TRUE THEN
 varcount := varcount + 1;
 array_tablename(varcount) := other_tablename;
 END IF;
 is_need_copy := TRUE;
 FETCH other_table_cursor
 INTO other_tablename;
 END LOOP;
 CLOSE other_table_cursor;
 END IF;
 END;
 FOR i IN 1 .. array_tablename.count LOOP
 tablename := array_tablename(i);
 BEGIN
 EXECUTE IMMEDIATE ''delete from '' || tablename || '' where SET_YEAR = '' || destsetyear ||
 '' and RG_CODE = '''''' || destrgcode || '''''''';
 EXCEPTION
 WHEN OTHERS THEN
 dbms_output.put_line(tablename);
 END;
 DECLARE
 varhascolumn NUMBER;
 BEGIN
 EXECUTE IMMEDIATE ''select count(*) from COLS where TABLE_NAME = '''''' || tablename ||
 '''''' and COLUMN_NAME = ''''OLD_CHR_ID''''''
 INTO varhascolumn;
 IF varhascolumn > 0 THEN
 EXECUTE IMMEDIATE ''alter table '' || tablename || '' drop column OLD_CHR_ID'';
 END IF;
 EXECUTE IMMEDIATE ''alter table '' || tablename || '' add OLD_CHR_ID varchar2(38)'';
 END;
 EXECUTE IMMEDIATE ''insert into '' || tablename ||
 '' VALUE (SET_YEAR, CHR_ID, CHR_CODE,
 DISP_CODE, CHR_NAME, LEVEL_NUM, IS_LEAF, ENABLED, CREATE_DATE, CREATE_USER, LATEST_OP_DATE,
 IS_DELETED, LATEST_OP_USER, LAST_VER, CHR_CODE1, CHR_CODE2, CHR_CODE3, CHR_CODE4,
 CHR_CODE5, RG_CODE, PARENT_ID, CHR_ID1, CHR_ID2, CHR_ID3, CHR_ID4, CHR_ID5, '' ||
 CASE tablename
 WHEN ''ELE_BUDGET_SUBJECT_INCOME'' THEN
 ''TYPE_ID, SUBITEM_TYPE, ''
 WHEN ''ELE_BUDGET_SUBJECT_ITEM'' THEN
 ''PAY_TYPE_ID, SUBITEM_TYPE, IS_SICHAR,
 PROJECTUSED, TYPE_ID, ''
 WHEN ''ELE_BUDGET_SUBJECT'' THEN
 ''TYPE_ID, SUBITEM_TYPE, ''
 WHEN ''ELE_MANAGE_BRANCH'' THEN
 ''ABBREVIATION, ''
 WHEN ''ELE_FILE_NO'' THEN
 ''FILE_YEAR, FILE_DATE, TITLE, MEMO, FILE_PRE, FILE_NUM, ''
 WHEN ''ELE_BOOK_SET'' THEN
 ''START_DATE, SET_TYPE, MANAGER_NAME, CURRENT_MONTH,
 IS_INITIALIZED, IS_YEAR_FINISHED, COA_ID, AGENCY_ID, RULE_ID, TABLE_FLAG, ''
 WHEN ''ELE_ACCOUNTANT_SUBJECT'' THEN
 ''IS_DEBIT, SUBJECT_TYPE, ST_ID, SUBJECT_KIND,
 COA_ID, HINT_CODE, HELP_CODE, ANALY_CODE, IS_FROM_MOUDLE, REMARK, ELE_CODE3,
 ELE_CODE, FOREIGN_MON, UNIT_NUM, IS_SPREAD, TABLE_NAME, BALANCE_PERIOD_TYPE,
 MONTHDETAIL_TABLE_NAME, BAL_TABLE, ''
 WHEN ''ELE_ACCOUNT'' THEN
 '' ACCOUNT_NO,ACCOUNT_NAME,BANK_CODE,ACCOUNT_TYPE,OWNER_CODE,IS_DEFAULT,REMARK, ''
 WHEN ''ACC_ELE_ACCOUNT'' THEN
 '' ACCOUNT_NO,ACCOUNT_NAME,BANK_CODE,ACCOUNT_TYPE,OWNER_CODE, ''
 WHEN ''ELE_PAYEE_ACCOUNT'' THEN
 '' OWNER_CODE,ACCOUNT_NO,ACCOUNT_NAME,PAYEE_NAME,BANK_CODE,BANK_NAME,ACCOUNT_TYPE,BANKFLAG,IS_CORPORATION,IS_PUBLIC,REMARK, ''
 WHEN ''ELE_ENTERPRISE'' THEN
 '' START_DATE,END_DATE,EN_TYPE,ENABLED_EBANK_DATE,EBANK_ENTERPRISE_NO,ENABLED_EBANK,MAIN_CODE,UNION_CODE,IS_REFORM,SECRETDEGREE,ISBUDGET,LULOAD_MOD,EN_CF_MOD, ''
 WHEN ''ELE_BANK'' THEN
 '' INCOMEFLAG,AGENTFLAG,PROVINCE,CLEARFLAG,BANK_SIGN,ISBN,ISBN_C,CITY_CODE, ''
 ELSE
 ''''
 END || '' OLD_CHR_ID) select '' || destsetyear || '' SET_YEAR, NEWID CHR_ID, CHR_CODE,
 DISP_CODE, CHR_NAME, LEVEL_NUM, IS_LEAF, ENABLED, CREATE_DATE, CREATE_USER, LATEST_OP_DATE,
 IS_DELETED, LATEST_OP_USER, LAST_VER, CHR_CODE1, CHR_CODE2, CHR_CODE3, CHR_CODE4,
 CHR_CODE5, '''''' || destrgcode || '''''' RG_CODE, PARENT_ID, CHR_ID1, CHR_ID2, CHR_ID3,
 CHR_ID4, CHR_ID5, '' || CASE tablename
 WHEN ''ELE_BUDGET_SUBJECT_INCOME'' THEN
 ''TYPE_ID, SUBITEM_TYPE, ''
 WHEN ''ELE_BUDGET_SUBJECT_ITEM'' THEN
 ''PAY_TYPE_ID, SUBITEM_TYPE, IS_SICHAR,
 PROJECTUSED, TYPE_ID, ''
 WHEN ''ELE_BUDGET_SUBJECT'' THEN
 ''TYPE_ID, SUBITEM_TYPE, ''
 WHEN ''ELE_MANAGE_BRANCH'' THEN
 ''ABBREVIATION, ''
 WHEN ''ELE_FILE_NO'' THEN
 ''FILE_YEAR, FILE_DATE, TITLE, MEMO, FILE_PRE, FILE_NUM, ''
 WHEN ''ELE_BOOK_SET'' THEN
 ''START_DATE, SET_TYPE, MANAGER_NAME, CURRENT_MONTH,
 IS_INITIALIZED, IS_YEAR_FINISHED, COA_ID, AGENCY_ID, RULE_ID, TABLE_FLAG, ''
 WHEN ''ELE_ACCOUNTANT_SUBJECT'' THEN
 ''IS_DEBIT, SUBJECT_TYPE, ST_ID, SUBJECT_KIND,
 COA_ID, HINT_CODE, HELP_CODE, ANALY_CODE, IS_FROM_MOUDLE, REMARK, ELE_CODE3,
 ELE_CODE, FOREIGN_MON, UNIT_NUM, IS_SPREAD, TABLE_NAME, BALANCE_PERIOD_TYPE,
 MONTHDETAIL_TABLE_NAME, BAL_TABLE, ''
 WHEN ''ELE_ACCOUNT'' THEN
 '' ACCOUNT_NO,ACCOUNT_NAME,BANK_CODE,ACCOUNT_TYPE,OWNER_CODE,IS_DEFAULT,REMARK, ''
 WHEN ''ACC_ELE_ACCOUNT'' THEN
 '' ACCOUNT_NO,ACCOUNT_NAME,BANK_CODE,ACCOUNT_TYPE,OWNER_CODE, ''
 WHEN ''ELE_PAYEE_ACCOUNT'' THEN
 '' OWNER_CODE,ACCOUNT_NO,ACCOUNT_NAME,PAYEE_NAME,BANK_CODE,BANK_NAME,ACCOUNT_TYPE,BANKFLAG,IS_CORPORATION,IS_PUBLIC,REMARK, ''
 WHEN ''ELE_ENTERPRISE'' THEN
 '' START_DATE,END_DATE,EN_TYPE,ENABLED_EBANK_DATE,EBANK_ENTERPRISE_NO,ENABLED_EBANK,MAIN_CODE,UNION_CODE,IS_REFORM,SECRETDEGREE,ISBUDGET,LULOAD_MOD,EN_CF_MOD, ''
 WHEN ''ELE_BANK'' THEN
 '' INCOMEFLAG,AGENTFLAG,PROVINCE,CLEARFLAG,BANK_SIGN,ISBN,ISBN_C,CITY_CODE, ''
 ELSE
 ''''
 END || ''CHR_ID OLD_CHR_ID from '' || tablename || '' where SET_YEAR = '' ||
 srcsetyear || '' and RG_CODE = '' || srcrgcode;
 IF tablename = ''ELE_PAY_FILL_MODE'' THEN
 EXECUTE IMMEDIATE ''update ELE_PAY_FILL_MODE set CHR_ID = CHR_CODE
 where CHR_CODE in (''''ZFXT'''', ''''ZFXT-BZFS'''') and SET_YEAR = '' ||
 destsetyear || '' and RG_CODE ='''''' || destrgcode || '''''''';
 END IF;
 COMMIT;
 DECLARE
 TYPE refcursor IS REF CURSOR;
 cursor_parentid refcursor;
 varparentid     VARCHAR2(38);
 varnewchrid     VARCHAR2(38);
 BEGIN
 OPEN cursor_parentid FOR ''select distinct PARENT_ID from '' || tablename || '' where PARENT_ID is not null and SET_YEAR = '' || destsetyear || '' and RG_CODE = '''''' || destrgcode||'''''''';
 FETCH cursor_parentid
 INTO varparentid;
 WHILE cursor_parentid% FOUND LOOP
 DECLARE
 BEGIN
 EXECUTE IMMEDIATE ''select CHR_ID from '' || tablename || '' where OLD_CHR_ID = '''''' ||
 varparentid || ''''''''
 INTO varnewchrid;
 EXCEPTION
 WHEN OTHERS THEN
 varnewchrid := NULL;
 END;
 IF varnewchrid IS NOT NULL THEN
 EXECUTE IMMEDIATE ''update '' || tablename || '' set PARENT_ID = '''''' || varnewchrid ||
 '''''' where PARENT_ID = '''''' || varparentid || '''''' and SET_YEAR = '' ||
 destsetyear || '' and RG_CODE = '''''' || destrgcode || '''''''';
 END IF;
 FETCH cursor_parentid
 INTO varparentid;
 END LOOP;
 CLOSE cursor_parentid;
 COMMIT;
 END;
 DECLARE
 TYPE refcursor IS REF CURSOR;
 cursor_chrid1 refcursor;
 varchrid1     VARCHAR2(38);
 varnewchrid   VARCHAR2(38);
 BEGIN
 OPEN cursor_chrid1 FOR ''select distinct CHR_ID1 from '' || tablename || '' where CHR_ID1 is not null and SET_YEAR = '' || destsetyear || '' and RG_CODE = '''''' || destrgcode ||'''''''';
 FETCH cursor_chrid1
 INTO varchrid1;
 WHILE cursor_chrid1% FOUND LOOP
 DECLARE
 BEGIN
 EXECUTE IMMEDIATE ''select CHR_ID from '' || tablename || '' where OLD_CHR_ID = '''''' ||
 varchrid1 || ''''''''
 INTO varnewchrid;
 EXCEPTION
 WHEN OTHERS THEN
 varnewchrid := NULL;
 END;
 IF varnewchrid IS NOT NULL THEN
 EXECUTE IMMEDIATE ''update '' || tablename || '' set CHR_ID1 = '''''' || varnewchrid ||
 '''''' where CHR_ID1 = '''''' || varchrid1 || '''''' and SET_YEAR = '' ||
 destsetyear || '' and RG_CODE = '''''' || destrgcode || '''''''';
 END IF;
 FETCH cursor_chrid1
 INTO varchrid1;
 END LOOP;
 CLOSE cursor_chrid1;
 COMMIT;
 END;
 DECLARE
 TYPE refcursor IS REF CURSOR;
 cursor_chrid2 refcursor;
 varchrid2     VARCHAR2(38);
 varnewchrid   VARCHAR2(38);
 BEGIN
 OPEN cursor_chrid2 FOR ''select distinct CHR_ID2 from '' || tablename || '' where CHR_ID2 is not null and SET_YEAR = '' || destsetyear || '' and RG_CODE = '''''' || destrgcode || '''''''';
 FETCH cursor_chrid2
 INTO varchrid2;
 WHILE cursor_chrid2% FOUND LOOP
 DECLARE
 BEGIN
 EXECUTE IMMEDIATE ''select CHR_ID from '' || tablename || '' where OLD_CHR_ID = '''''' ||
 varchrid2 || ''''''''
 INTO varnewchrid;
 EXCEPTION
 WHEN OTHERS THEN
 varnewchrid := NULL;
 END;
 IF varnewchrid IS NOT NULL THEN
 EXECUTE IMMEDIATE ''update '' || tablename || '' set CHR_ID2 = '''''' || varnewchrid ||
 '''''' where CHR_ID2 = '''''' || varchrid2 || '''''' and SET_YEAR = '' ||
 destsetyear || '' and RG_CODE = '''''' || destrgcode || '''''''';
 END IF;
 FETCH cursor_chrid2
 INTO varchrid2;
 END LOOP;
 CLOSE cursor_chrid2;
 COMMIT;
 END;
 DECLARE
 TYPE refcursor IS REF CURSOR;
 cursor_chrid3 refcursor;
 varchrid3     VARCHAR2(38);
 varnewchrid   VARCHAR2(38);
 BEGIN
 OPEN cursor_chrid3 FOR ''select distinct CHR_ID3 from '' || tablename || '' where CHR_ID3 is not null and SET_YEAR = '' || destsetyear || '' and RG_CODE = '''''' || destrgcode ||'''''''';
 FETCH cursor_chrid3
 INTO varchrid3;
 WHILE cursor_chrid3% FOUND LOOP
 DECLARE
 BEGIN
 EXECUTE IMMEDIATE ''select CHR_ID from '' || tablename || '' where OLD_CHR_ID = '''''' ||
 varchrid3 || ''''''''
 INTO varnewchrid;
 EXCEPTION
 WHEN OTHERS THEN
 varnewchrid := NULL;
 END;
 IF varnewchrid IS NOT NULL THEN
 EXECUTE IMMEDIATE ''update '' || tablename || '' set CHR_ID3 = '''''' || varnewchrid ||
 '''''' where CHR_ID3 = '''''' || varchrid3 || '''''' and SET_YEAR = '' ||
 destsetyear || '' and RG_CODE = '''''' || destrgcode ||'''''''';
 END IF;
 FETCH cursor_chrid3
 INTO varchrid3;
 END LOOP;
 CLOSE cursor_chrid3;
 COMMIT;
 END;
 DECLARE
 TYPE refcursor IS REF CURSOR;
 cursor_chrid4 refcursor;
 varchrid4     VARCHAR2(38);
 varnewchrid   VARCHAR2(38);
 BEGIN
 OPEN cursor_chrid4 FOR ''select distinct CHR_ID4 from '' || tablename || '' where CHR_ID4 is not null and SET_YEAR = '' || destsetyear || '' and RG_CODE = '''''' || destrgcode||'''''''';
 FETCH cursor_chrid4
 INTO varchrid4;
 WHILE cursor_chrid4% FOUND LOOP
 DECLARE
 BEGIN
 EXECUTE IMMEDIATE ''select CHR_ID from '' || tablename || '' where OLD_CHR_ID = '''''' ||
 varchrid4 || ''''''''
 INTO varnewchrid;
 EXCEPTION
 WHEN OTHERS THEN
 varnewchrid := NULL;
 END;
 IF varnewchrid IS NOT NULL THEN
 EXECUTE IMMEDIATE ''update '' || tablename || '' set CHR_ID4 = '''''' || varnewchrid ||
 '''''' where CHR_ID4 = '''''' || varchrid4 || '''''' and SET_YEAR = '' ||
 destsetyear || '' and RG_CODE = '''''' || destrgcode||'''''''';
 END IF;
 FETCH cursor_chrid4
 INTO varchrid4;
 END LOOP;
 CLOSE cursor_chrid4;
 COMMIT;
 END;
 DECLARE
 TYPE refcursor IS REF CURSOR;
 cursor_chrid5 refcursor;
 varchrid5     VARCHAR2(38);
 varnewchrid   VARCHAR2(38);
 BEGIN
 OPEN cursor_chrid5 FOR ''select distinct CHR_ID5 from '' || tablename || '' where CHR_ID5 is not null and SET_YEAR = '' || destsetyear || '' and RG_CODE = '''''' || destrgcode||'''''''';
 FETCH cursor_chrid5
 INTO varchrid5;
 WHILE cursor_chrid5% FOUND LOOP
 DECLARE
 BEGIN
 EXECUTE IMMEDIATE ''select CHR_ID from '' || tablename || '' where OLD_CHR_ID = '''''' ||
 varchrid5 || ''''''''
 INTO varnewchrid;
 EXCEPTION
 WHEN OTHERS THEN
 varnewchrid := NULL;
 END;
 IF varnewchrid IS NOT NULL THEN
 EXECUTE IMMEDIATE ''update '' || tablename || '' set CHR_ID5 = '''''' || varnewchrid ||
 '''''' where CHR_ID5 = '''''' || varchrid5 || '''''' and SET_YEAR = '' ||
 destsetyear || '' and RG_CODE = '''''' || destrgcode ||'''''''';
 END IF;
 FETCH cursor_chrid5
 INTO varchrid5;
 END LOOP;
 CLOSE cursor_chrid5;
 COMMIT;
 END;
 IF tablename = ''ELE_ACCOUNTANT_SUBJECT'' THEN
 DECLARE
 TYPE refcursor IS REF CURSOR;
 cursor_stid refcursor;
 varstid     VARCHAR2(38);
 varnewchrid VARCHAR2(38);
 BEGIN
 OPEN cursor_stid FOR ''select distinct ST_ID from '' || tablename || '' where ST_ID is not null and SET_YEAR = '' || destsetyear || '' and RG_CODE = '''''' || destrgcode||'''''''';
 FETCH cursor_stid
 INTO varstid;
 WHILE cursor_stid% FOUND LOOP
 BEGIN
 EXECUTE IMMEDIATE ''select CHR_ID from ELE_BOOK_SET where OLD_CHR_ID = '''''' || varstid || ''''''''
 INTO varnewchrid;
 EXCEPTION
 WHEN OTHERS THEN
 varnewchrid := NULL;
 END;
 EXECUTE IMMEDIATE ''update '' || tablename || '' set ST_ID = '''''' || varnewchrid ||
 '''''' where ST_ID = '''''' || varstid || '''''' and SET_YEAR = '' ||
 destsetyear || '' and RG_CODE = '''''' || destrgcode ||'''''''';
 FETCH cursor_stid
 INTO varstid;
 END LOOP;
 CLOSE cursor_stid;
 COMMIT;
 END;
 END IF;
 END LOOP;
 FOR i IN 1 .. array_tablename.count LOOP
 tablename := array_tablename(i);
 EXECUTE IMMEDIATE ''alter table '' || tablename || '' drop column OLD_CHR_ID'';
 COMMIT;
 END LOOP;
 COMMIT;
 END;
 END basicdatamanager;';
execute immediate 'create or replace procedure batch_create_rule_cross is
 type lcCursors is ref cursor;
 str     varchar2(1000);
 rule_id varchar2(38);
 str1    varchar2(38);
 lsCursor lcCursors; --结果集游标
 begin
 str := ''select a.rule_id from sys_role_rule a,sys_rule b where a.rule_id=b.rule_id and b.right_type=1'';
 open lsCursor for str;
 loop
 fetch lsCursor
 into rule_id;
 if (lsCursor%notfound) then
 exit;
 else
 str1 := create_rule_cross(rule_id);
 commit;
 end if;
 end loop;
 close lsCursor;
 commit;
 end batch_create_rule_cross;';

execute immediate 'create or replace procedure billtypeManage(srcRgCode   in varchar2,
 destRgCode  in varchar2,
 srcSetYear  in varchar2,
 destSetYear in varchar2) as
 begin
 delete from sys_billtype_report where RG_CODE=destRgCode
 AND SET_YEAR=destSetYear;
 delete from sys_print_models where RG_CODE=destRgCode
 AND SET_YEAR=destSetYear;
 commit;
 insert into SYS_BILLTYPE_REPORT value
 (ID,
 BILLTYPE_ID,
 REPORT_FILE,
 REPORT_TITLE,
 ARG_LIST,
 DISPLAY_ORDER,
 PARA_LIST,
 RG_CODE,
 SET_YEAR)
 select newid,
 BILLTYPE_ID,
 REPORT_FILE,
 REPORT_TITLE,
 ARG_LIST,
 DISPLAY_ORDER,
 PARA_LIST,
 destRgCode,
 destSetYear
 from SYS_BILLTYPE_REPORT
 where RG_CODE = srcRgCode
 and set_year = srcSetYear;
 insert into SYS_PRINT_MODELS
 value(BILLTYPE_REPORT_ID,
 BILLTYPE_ID,
 REPORT_ID,
 ARG_LIST,
 PARA_LIST,
 DISPLAY_ORDER,
 ENABLED,
 IS_DEFAULT,
 CREATE_DATE,
 LATEST_OP_DATE,
 LATEST_OP_USER,
 SET_YEAR,
 RG_CODE)
 select newid,
 billtype_id,
 REPORT_ID,
 ARG_LIST,
 PARA_LIST,
 DISPLAY_ORDER,
 ENABLED,
 IS_DEFAULT,
 CREATE_DATE,
 LATEST_OP_DATE,
 LATEST_OP_USER,
 destSetYear,
 destRgCode
 from SYS_PRINT_MODELS
 where RG_CODE=srcRgCode
 and set_year=srcSetYear ;
 update SYS_PRINT_MODELS spm set billtype_id=(select billtype_id from sys_billtype where billtype_code =(select billtype_code from sys_billtype sb where sb.billtype_id = spm.billtype_id )
 and RG_CODE=destRgCode
 and set_year=destSetYear)
 where RG_CODE=destRgCode
 and set_year=destSetYear
 and exists (select 1 from sys_billtype sbt where sbt.billtype_id =spm.billtype_id );
 commit;
 end billtypeManage;';

execute immediate 'CREATE OR REPLACE PROCEDURE copy_ele_payee_account(srcrgcode   IN VARCHAR2,
 destrgcode  IN VARCHAR2,
 srcsetyear  IN VARCHAR2,
 destsetyear IN VARCHAR2) AS
 tablenum NUMBER;
 BEGIN
 DELETE FROM ele_payee_account WHERE set_year = destsetyear AND RG_CODE = destrgcode;
 SELECT COUNT(1) INTO tablenum FROM user_tab_cols WHERE table_name = ''COMPARE_ID_TMP'';
 IF tablenum = 0 THEN
 EXECUTE IMMEDIATE ''create table compare_id_tmp (old_id varchar(42),new_id varchar(42))'';
 EXECUTE IMMEDIATE ''create index IDX_COMPARE_NEW_ID on COMPARE_ID_TMP (NEW_ID)'';
 EXECUTE IMMEDIATE ''create index IDX_COMPARE_OLD_ID on COMPARE_ID_TMP (OLD_ID)'';
 ELSE
 EXECUTE IMMEDIATE ''delete from compare_id_tmp'';
 END IF;
 SELECT COUNT(1) INTO tablenum FROM user_tab_cols WHERE table_name = ''ELE_PAYEE_ACCOUNT_TMP'';
 IF tablenum = 0 THEN
 EXECUTE IMMEDIATE ''create table ele_payee_account_tmp as select * from ele_payee_account where RG_CODE='' ||
 srcrgcode || ''and set_year='' || srcsetyear;
 ELSE
 EXECUTE IMMEDIATE ''delete from ele_payee_account_tmp'';
 EXECUTE IMMEDIATE ''insert into ele_payee_account_tmp  select * from ele_payee_account where RG_CODE='' ||
 srcrgcode || ''and set_year='' || srcsetyear;
 END IF;
 EXECUTE IMMEDIATE ''insert into compare_id_tmp
 select DISTINCT chr_id, newid
 FROM ele_payee_account
 WHERE set_year = ''||srcsetyear||''
 AND RG_CODE = ''''''||srcrgcode||'''''''';
 COMMIT;
 EXECUTE IMMEDIATE ''update ele_payee_account_tmp a set a.chr_id =
 (select new_id from compare_id_tmp b where b.old_id = a.chr_id),
 a.parent_id = (select new_id from compare_id_tmp b where b.old_id = a.parent_id),
 a.chr_id1 = (select new_id from compare_id_tmp b where b.old_id = a.chr_id1),
 a.chr_id2 = (select new_id from compare_id_tmp b where b.old_id = a.chr_id2),
 a.chr_id3 = (select new_id from compare_id_tmp b where b.old_id = a.chr_id3),
 a.chr_id4 = (select new_id from compare_id_tmp b where b.old_id = a.chr_id4),
 a.chr_id5 = (select new_id from compare_id_tmp b where b.old_id = a.chr_id5)'';
 commit;
 EXECUTE IMMEDIATE ''update ele_payee_account_tmp set set_year = '' || destsetyear;
 EXECUTE IMMEDIATE ''INSERT INTO ele_payee_account SELECT * FROM ele_payee_account_tmp'';
 COMMIT;
 EXECUTE IMMEDIATE ''drop table compare_id_tmp '';
 EXECUTE IMMEDIATE ''drop table ele_payee_account_tmp '';
 END;';

execute immediate 'create or replace procedure create_batch_rcid(in_table_name In Varchar2,
 in_condition  in varchar2,
 in_rg_code IN VARCHAR2,year in varchar2) Is
 v_ele_col             varchar2(2000);
 v_ele_col_2           varchar2(2000);
 v_ele_par_combination varchar2(2000);
 v_ele_par_rule        varchar2(2000);
 v_ele_par_rule_2      varchar2(2000);
 v_insert_combination  varchar2(2000);
 v_insert_rule         varchar2(2000);
 v_update_table        varchar2(2000);
 v_field               number;
 v_ele_exists          boolean;
 v_rowcount            number;
 Begin
 v_ele_exists := true;
 for loop_cur in (select t.ele_code || ''_id'' as ele_id
 from sys_element t
 where t.is_rightfilter = 1 AND t.RG_CODE = UPPER(in_rg_code) and t.set_year=year) loop
 v_ele_col             := v_ele_col || ''  '' || loop_cur.ele_id || '' ,'';
 v_ele_col_2           := v_ele_col_2 || '' nvl(a.'' || loop_cur.ele_id ||
 '', ''''#''''),'';
 v_ele_par_combination := v_ele_par_combination || '' and a.'' ||
 loop_cur.ele_id || ''=b.'' || loop_cur.ele_id;
 v_ele_par_rule        := v_ele_par_rule || '' and (a.'' ||
 loop_cur.ele_id || '' is null or b.'' ||
 loop_cur.ele_id || '' = ''''#'''' or a.'' ||
 loop_cur.ele_id || ''=b.'' || loop_cur.ele_id || '')'';
 v_ele_par_rule_2      := v_ele_par_rule_2 || '' and ((a.'' ||
 loop_cur.ele_id || '' is null and b.'' ||
 loop_cur.ele_id || '' = ''''#'''') or a.'' ||
 loop_cur.ele_id || ''=b.'' || loop_cur.ele_id || '')'';
 if(v_ele_exists)then
 Select count(1)
 into v_field
 from user_tab_cols
 where table_name = upper(in_table_name)
 and column_name = upper(loop_cur.ele_id);
 If (v_field < 1) then
 v_ele_exists := false;
 End if;
 end if;
 end loop;
 if (v_ele_exists) then
 v_insert_combination := ''insert into sys_right_combination(RG_CODE,set_year, rcid,'' ||
 v_ele_col || ''update_flag)'' ||
 '' select a.RG_CODE,a.set_year, seq_rcid.nextval,'' ||
 v_ele_col_2 || ''0 from '' || in_table_name ||
 '' a where 1=1 and a.RG_CODE = ''|| in_rg_code ||'' and a.set_year= ''|| year || '' '' || in_condition ||
 '' and not exists '' ||
 '' (select 1 from sys_right_combination '' ||
 '' b where 1=1 and b.RG_CODE = ''|| in_rg_code ||'' and b.set_year= ''|| year || '' ''|| v_ele_par_rule_2 || '' ) '';
 else
 v_insert_combination := ''insert into sys_right_combination(RG_CODE,set_year, rcid,'' ||
 v_ele_col || ''update_flag)'' ||
 '' select c.RG_CODE,a.set_year, seq_rcid.nextval,'' ||
 v_ele_col_2 || ''0 from '' || in_table_name ||
 '' c, gl_ccids a where a.ccid = c.ccid
 and a.RG_CODE = ''|| in_rg_code ||'' and a.set_year= ''|| year || '' '' ||
 in_condition || '' and not exists '' ||
 '' (select 1 from sys_right_combination '' ||
 '' b where 1=1 and b.RG_CODE = ''|| in_rg_code ||'' and b.set_year= ''|| year || '' '' || v_ele_par_rule_2 || '' ) '';
 end if;
 dbms_output.put_line(v_insert_combination);
 execute immediate v_insert_combination;
 v_rowcount := SQL%ROWCOUNT;
 if (v_rowcount > 0) then
 v_insert_rule := '' insert into sys_rule_rcid(RG_CODE,set_year,rule_id,rcid,last_ver) '' ||
 '' select b.RG_CODE,b.set_year,c.rule_id,b.rcid,sysdate from '' ||
 '' sys_rule c, sys_right_combination b where c.rule_classify = ''''006'''''' ||
 '' and b.update_flag = 0 and exists (select 1 from sys_rule_cross_join_add a '' ||
 '' where a.rule_id = c.rule_id '' || v_ele_par_rule || '')'' ||
 '' and not exists (select 1 from sys_rule_cross_join_del a where a.rule_id = c.rule_id '' ||
 v_ele_par_rule || '') '' ||
 '' and not exists (select 1 from sys_rule_rcid a where a.set_year = b.set_year'' ||
 '' and a.rule_id = c.rule_id and a.rcid = b.rcid) '';
 execute immediate v_insert_rule;
 execute immediate ''update sys_right_combination set update_flag=1 where update_flag=0'';
 end if;
 if (v_ele_exists) then
 v_update_table := '' update '' || in_table_name ||
 '' a set a.rcid =(select b.rcid from sys_right_combination b where 1=1 '' ||
 v_ele_par_rule_2 || '' and rownum<2) where 1=1 '' ||
 in_condition;
 else
 v_update_table := '' update '' || in_table_name ||
 '' c set c.rcid =(select b.rcid from sys_right_combination b, gl_ccids a where a.ccid = c.ccid and 1=1 '' ||
 v_ele_par_rule_2 || '' and rownum<2) where 1=1 '' ||
 in_condition;
 end if;
 execute immediate v_update_table;
 End create_batch_rcid;';

execute immediate 'create or replace procedure dropColumn
 as
 begin
 
 execute immediate ''alter table SYS_WF_CONDITIONS drop column OLD_CONDITION_ID'';
 execute immediate ''alter table SYS_WF_FLOWS drop column OLD_WF_ID'';
 execute immediate ''alter table SYS_WF_NODES drop column OLD_NODE_ID'';
 commit;
 end;';

execute immediate 'CREATE OR REPLACE PROCEDURE elementmanage(srcrgcode   IN VARCHAR2,
 destrgcode  IN VARCHAR2,
 srcsetyear  IN VARCHAR2,
 destsetyear IN VARCHAR2) AS
 BEGIN
 DELETE FROM sys_element WHERE set_year = destsetyear AND RG_CODE = destrgcode;
 INSERT INTO sys_element
 (SELECT newid chr_id, destsetyear set_year, ele_source, ele_code, ele_name, enabled, dispmode, ref_mode, is_rightfilter, max_level, code_rule, level_name, create_date, create_user, latest_op_date, latest_op_user, is_deleted, is_nolevel, is_local, is_system, ele_type, is_view, czgb_code, last_ver, disp_order, sys_id, is_operate, destrgcode RG_CODE
 FROM sys_element
 WHERE set_year = srcsetyear
 AND RG_CODE = srcrgcode);
 IF srcrgcode = destrgcode AND srcsetyear<>destsetyear THEN
 copy_ele_payee_account(srcrgcode, destrgcode, srcsetyear, destsetyear);
 END IF;
 END elementmanage;';
 
execute immediate 'CREATE OR REPLACE PROCEDURE fapgl_copy_configure(srcyear  IN VARCHAR2,
 desyear  IN VARCHAR2,
 old_code IN VARCHAR2,
 new_code IN VARCHAR2) IS
 BEGIN
 DECLARE
 vnewid    VARCHAR(100);
 tablename VARCHAR(42);
 BEGIN
 DELETE gl_coa_detail
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 DELETE gl_coa
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 DELETE gl_coa_cascade WHERE RG_CODE = new_code and set_year=desyear;
 DELETE gl_busvou_acctmdl
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 DELETE gl_busvou_type
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 DELETE sys_billnorule
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 DELETE sys_billnoruleline
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 DELETE sys_billnoruleele
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 DELETE sys_billtype_assele
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 DELETE sys_billtype
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 INSERT INTO gl_coa
 SELECT seq_gl_coa.NEXTVAL AS coa_id, coa_code, coa_name, coa_desc, enabled, create_date, create_user, latest_op_date, latest_op_user, last_ver, desyear, new_code, ccids_table, sys_app_name
 FROM gl_coa
 WHERE RG_CODE = old_code
 AND set_year = srcyear;
 INSERT INTO gl_coa_detail
 SELECT (SELECT coa_id
 FROM gl_coa out_table
 WHERE EXISTS (SELECT 1
 FROM gl_coa tmp
 WHERE tmp.coa_code = out_table.coa_code
 AND tmp.coa_id = in_table.coa_id)
 AND out_table.RG_CODE = new_code
 AND out_table.set_year = desyear) AS coa_id, newid() AS coa_detail_id, ele_code, disp_order, level_num, last_ver, desyear, is_mustinput, default_value, new_code
 FROM gl_coa_detail in_table
 WHERE coa_id IN (SELECT coa_id
 FROM gl_coa
 WHERE RG_CODE = old_code
 AND set_year = srcyear)
 AND set_year = srcyear
 AND RG_CODE = old_code;
 DECLARE
 elesource VARCHAR2(100);
 eleid     VARCHAR2(40);
 sql1      VARCHAR2(500);
 BEGIN
 FOR v_temp IN (SELECT coa_detail_id, ele_code, default_value
 FROM gl_coa_detail
 WHERE default_value IS NOT NULL
 AND RG_CODE = new_code
 AND set_year = desyear) LOOP
 SELECT ele_source
 INTO elesource
 FROM sys_element
 WHERE ele_code = v_temp.ele_code
 AND RG_CODE = new_code
 AND set_year = desyear;
 sql1 := ''select chr_id from '' || elesource || '' where chr_code= (select chr_code from '' ||
 elesource || '' where chr_id='''''' || v_temp.default_value || '''''') and RG_CODE='' ||
 new_code || '' and set_year='' || desyear;
 BEGIN
 EXECUTE IMMEDIATE sql1
 INTO eleid;
 UPDATE gl_coa_detail
 SET default_value = eleid
 WHERE coa_detail_id = v_temp.coa_detail_id;
 EXCEPTION
 WHEN no_data_found THEN
 UPDATE gl_coa_detail SET default_value = '''' WHERE coa_detail_id = v_temp.coa_detail_id;
 END;
 END LOOP;
 END;
 IF srcyear = desyear THEN
 INSERT INTO gl_coa_cascade
 (coa_id, coa_name, relation_coa_id, is_up_stream, is_branch, RG_CODE, set_year)
 SELECT (SELECT coa_id
 FROM gl_coa
 WHERE coa_code = (SELECT coa_code FROM gl_coa t WHERE t.coa_id = a.coa_id)
 AND RG_CODE = new_code), a.coa_name, (SELECT coa_id
 FROM gl_coa
 WHERE coa_code =
 (SELECT coa_code
 FROM gl_coa t
 WHERE t.coa_id = a.relation_coa_id)
 AND RG_CODE = new_code), a.is_up_stream, a.is_branch, new_code, desyear
 FROM gl_coa_cascade a
 WHERE a.RG_CODE = old_code;
 END IF;
 INSERT INTO gl_busvou_type
 SELECT seq_gl_busvou_type.NEXTVAL AS vou_type_id, vou_type_code, vou_type_name, latest_op_date, latest_op_user, is_manual, last_ver, desyear AS set_year, ds_id, cs_id, st_id, datasource_setting, new_code AS RG_CODE, enabled
 FROM gl_busvou_type
 WHERE RG_CODE = old_code
 AND set_year = srcyear;
 INSERT INTO gl_busvou_acctmdl
 SELECT seq_gl_busvou_acctmdl.NEXTVAL AS acctmdl_id, desyear AS set_year, new_code AS RG_CODE, (SELECT vou_type_id
 FROM gl_busvou_type out_table
 WHERE EXISTS
 (SELECT 1
 FROM gl_busvou_type tmp
 WHERE tmp.vou_type_code =
 out_table.vou_type_code
 AND tmp.vou_type_id =
 in_table.vou_type_id)
 AND out_table.RG_CODE =
 new_code
 AND out_table.set_year =
 desyear) AS vou_type_id, (SELECT account_id
 FROM vw_gl_account ac
 WHERE account_code =
 (SELECT account_code
 FROM vw_gl_account tmp_ac
 WHERE in_table.account_id = tmp_ac.account_id
 AND tmp_ac.RG_CODE = old_code
 AND tmp_ac.set_year = srcyear)
 AND RG_CODE = new_code
 AND set_year = desyear), entry_side, is_primary_source, is_primary_target, ctrl_level, decode(in_table.rule_id,
 ''0'',
 in_table.rule_id,
 (SELECT rule_id
 FROM sys_rule ac
 WHERE rule_code =
 (SELECT rule_code
 FROM sys_rule tmp_ac
 WHERE in_table.rule_id =
 tmp_ac.rule_id
 AND tmp_ac.RG_CODE =
 old_code
 AND tmp_ac.set_year =
 srcyear)
 AND RG_CODE =
 new_code
 AND set_year =
 desyear))
 FROM gl_busvou_acctmdl in_table
 WHERE RG_CODE = old_code
 AND set_year = srcyear;
 INSERT INTO sys_billnorule
 SELECT newid() AS billnorule_id, billnorule_code, billnorule_name, latest_op_date, latest_op_user, desyear AS set_year, last_ver, sys_id, new_code AS RG_CODE
 FROM sys_billnorule
 WHERE RG_CODE = old_code
 AND set_year = srcyear;
 INSERT INTO sys_billnoruleline
 SELECT nvl((SELECT DISTINCT billnorule_id
 FROM sys_billnorule a
 WHERE (a.billnorule_code, a.billnorule_name) =
 (SELECT billnorule_code, billnorule_name
 FROM sys_billnorule tmp
 WHERE tmp.RG_CODE = old_code
 AND tmp.set_year = srcyear
 AND tmp.billnorule_id = in_table.billnorule_id)
 AND a.RG_CODE = new_code
 AND a.set_year = desyear),''1'') billnorule_id, newid() billnoruleline_id, line_no, line_type, line_format, init_value, ele_code, level_num, latest_op_date, latest_op_user, desyear, last_ver, new_code
 FROM sys_billnoruleline in_table
 WHERE RG_CODE = old_code
 AND set_year = srcyear;
 INSERT INTO sys_billnoruleele
 SELECT (SELECT destable.billnoruleline_id
 FROM sys_billnoruleline destable
 WHERE EXISTS (SELECT 1
 FROM sys_billnoruleline oldtable, sys_billnorule oldrule, sys_billnorule newrule
 WHERE oldrule.billnorule_code = newrule.billnorule_code
 AND newrule.billnorule_id = destable.billnorule_id
 AND oldrule.billnorule_id = oldtable.billnorule_id
 AND oldtable.billnoruleline_id = in_table.billnoruleline_id
 AND oldtable.line_no = destable.line_no)
 AND destable.RG_CODE = new_code
 AND destable.set_year = desyear) AS billnoruleline_id, ele_code, level_num, desyear AS set_year, last_ver, new_code AS RG_CODE
 FROM sys_billnoruleele in_table
 WHERE RG_CODE = old_code
 AND set_year = srcyear;
 INSERT INTO sys_billtype
 SELECT newid() AS billtype_id, billtype_code, billtype_name, is_busincrease, (SELECT vou_type_id
 FROM gl_busvou_type out_table
 WHERE EXISTS
 (SELECT 1
 FROM gl_busvou_type tmp
 WHERE tmp.vou_type_code =
 out_table.vou_type_code
 AND tmp.vou_type_id =
 in_table.busvou_type_id)
 AND out_table.RG_CODE =
 new_code
 AND out_table.set_year =
 desyear) AS busvou_type_id, (SELECT coa_id
 FROM gl_coa out_table
 WHERE EXISTS
 (SELECT 1
 FROM gl_coa tmp
 WHERE tmp.coa_code = out_table.coa_code
 AND tmp.coa_id = in_table.coa_id)
 AND out_table.RG_CODE = new_code
 AND out_table.set_year = desyear) AS coa_id, (SELECT DISTINCT billnorule_id
 FROM sys_billnorule a
 WHERE (a.billnorule_code,
 a.billnorule_name) =
 (SELECT billnorule_code, billnorule_name
 FROM sys_billnorule tmp
 WHERE tmp.RG_CODE = old_code
 AND tmp.set_year = srcyear
 AND tmp.billnorule_id =
 in_table.billnorule_id)
 AND a.RG_CODE = new_code
 AND a.set_year = desyear) AS billnorule_id, table_name, billtype_class, enabled, latest_op_date, latest_op_user, desyear AS set_year, last_ver, nobudgetbusvou_type_id, is_needchecknobudget, sys_id, field_name, ui_id, from_billtype_id, from_ui_id, to_billtype_id, to_ui_id, vou_control_id, new_code AS RG_CODE
 FROM sys_billtype in_table
 WHERE RG_CODE = old_code
 AND set_year = srcyear;
 INSERT INTO sys_billtype_assele
 (assele_id, billtype_id, ele_code, latest_op_user, latest_op_date, set_year, level_num, last_ver, RG_CODE)
 SELECT newid(), t.billtype_id, t.ele_code, t.latest_op_user, SYSDATE, desyear AS set_year, t.level_num, last_ver, new_code
 FROM sys_billtype_assele t
 WHERE RG_CODE = old_code
 AND set_year = srcyear;
 UPDATE sys_billtype_assele a
 SET billtype_id = (SELECT billtype_id
 FROM sys_billtype b
 WHERE b.billtype_code =
 (SELECT billtype_code
 FROM sys_billtype c
 WHERE a.billtype_id = c.billtype_id)
 AND b.RG_CODE = new_code
 AND b.set_year = desyear)
 WHERE RG_CODE = new_code
 AND set_year = desyear;
 UPDATE ele_book_set a
 SET a.coa_id = (SELECT coa_id
 FROM gl_coa coa
 WHERE coa.coa_code = (SELECT coa_code
 FROM gl_coa tmp_coa
 WHERE tmp_coa.coa_id = a.coa_id
 AND tmp_coa.RG_CODE = old_code
 AND tmp_coa.set_year = srcyear)
 AND coa.RG_CODE = new_code
 AND coa.set_year = desyear)
 WHERE a.RG_CODE = new_code
 AND a.set_year = desyear;
 UPDATE ele_accountant_subject a
 SET a.coa_id = (SELECT coa_id
 FROM gl_coa coa
 WHERE coa.coa_code = (SELECT coa_code
 FROM gl_coa tmp_coa
 WHERE tmp_coa.coa_id = a.coa_id
 AND tmp_coa.RG_CODE = old_code
 AND tmp_coa.set_year = srcyear)
 AND coa.RG_CODE = new_code
 AND coa.set_year = desyear)
 WHERE a.RG_CODE = new_code
 AND a.set_year = desyear;
 billtypemanage(old_code, new_code, srcyear, desyear); --执行sys_billtype_report, sys_print_models复制的存储过程
 DELETE FROM sys_billtype_valueset b
 WHERE b.set_year = desyear
 AND b.RG_CODE = new_code;
 FOR column_all IN (SELECT *
 FROM sys_billtype_valueset b
 WHERE b.RG_CODE = old_code
 AND b.set_year = srcyear) LOOP
 SELECT newid INTO vnewid FROM dual;
 INSERT INTO sys_billtype_valueset
 VALUES
 (vnewid, column_all.valueset_type, column_all.field_code, column_all.default_value, column_all.billtype_id, column_all.ele_rule_id, column_all.create_date, column_all.create_user, column_all.latest_op_date, column_all.latest_op_user, column_all.last_ver, desyear, new_code);
 UPDATE sys_billtype_valueset v
 SET ele_rule_id = (SELECT ele_rule_id
 FROM sys_ele_rule r
 WHERE EXISTS (SELECT 1
 FROM sys_ele_rule r1
 WHERE r1.ele_rule_id = v.ele_rule_id
 AND r1.ele_rule_code = r.ele_rule_code)
 AND r.set_year = desyear
 AND r.RG_CODE = new_code)
 WHERE valueset_id = vnewid;
 UPDATE sys_billtype_valueset v
 SET billtype_id = (SELECT billtype_id
 FROM sys_billtype r
 WHERE EXISTS (SELECT 1
 FROM sys_billtype r1
 WHERE r1.billtype_id = v.billtype_id
 AND r1.billtype_code = r.billtype_code)
 AND r.set_year = desyear
 AND r.RG_CODE = new_code)
 WHERE valueset_id = vnewid;
 IF (column_all.default_value IS NOT NULL) THEN
 SELECT ele_source
 INTO tablename
 FROM sys_element e
 WHERE e.RG_CODE = new_code
 AND e.set_year = desyear;
 EXECUTE IMMEDIATE ''update  sys_billtype_valueset v
 set default_value = (select chr_id
 from '' || tablename || '' t
 where exists
 (select 1
 from '' || tablename || '' t1
 where t1.chr_id = v.default_value
 and t1.chr_code = t.chr_code)
 and t.set_year = '''''' || desyear || ''''''
 and t.RG_CODE = '''''' || new_code ||
 '''''') where v.valueset_id = '' || vnewid;
 END IF;
 COMMIT;
 END LOOP;
 END;
 END fapgl_copy_configure;';

execute immediate 'create or replace procedure GL_CLOSE_MONTH_END is
 set_year          varchar2(10);
 RG_CODE           varchar2(10);
 is_continue       number := 1;
 set_month         number(4) := 0;
 set_month_current number(4);
 type t_cur is ref cursor;
 month_cur   t_cur;
 month_data  gl_monthly_balance.set_month%type;
 account_cur t_cur;
 type account_type is record(
 account_id             varchar2(38),
 balance_period_type    number(1),
 table_name             varchar2(30),
 monthdetail_table_name varchar2(30));
 account_data       account_type;
 insert_balance_sql varchar2(1000);
 update_money_sql   varchar2(2000);
 update_month_sql   varchar2(100);
 insert_month_sql   varchar2(100);
 str_sql varchar2(1000);
 begin
 select to_char(sysdate, ''yyyy'') into set_year from dual;
 begin
 str_sql:= ''select chr_value from sys_userpara where chr_code = ''''App_Curr_Region'''' and set_year = ''||set_year;
 execute immediate str_sql into RG_CODE;
 exception
 when no_data_found then
 is_continue := 0;
 dbms_output.put_line(''sys_userpara 表中 App_Curr_Region 区划未维护,月结失败！'');
 end;
 if (is_continue = 1) then
 open month_cur for ''select nvl(max(set_month),''''0'''') set_month from gl_monthly_balance where set_year= :set_year''
 using set_year;
 loop
 fetch month_cur
 into month_data;
 exit when month_cur%notfound;
 set_month := month_data;
 end loop;
 select to_number(to_char(sysdate, ''mm''))
 into set_month_current
 from dual;
 if (set_month_current < set_month + 1) then
 is_continue := 0;
 dbms_output.put_line(set_month_current || ''月份，已月结！'');
 end if;
 if (set_month_current = set_month + 1) then
 execute immediate ''delete gl_monthly_balance where set_month='' ||set_month;
 end if;
 end if;
 if (is_continue = 1) then
 open account_cur for ''select account_id,balance_period_type,table_name,monthdetail_table_name from vw_gl_account where set_year = '''''' || set_year || '''''' and BALANCE_PERIOD_TYPE = ''''1'''''';
 loop
 fetch account_cur
 into account_data;
 exit when account_cur%notfound;
 insert_balance_sql := ''insert into '' || account_data.TABLE_NAME ||
 ''(account_id,sum_id,create_date,latest_op_date,set_year,set_month,avi_money,use_money,minus_money,aving_money,last_ver,fromctrlid,bal_version,rcid,ccid,RG_CODE,balance_id,AGENCY_ID,MB_ID) (select max(account_id), sum_id, min(create_date), max(latest_op_date), max(set_year), :set_month_current, 0, 0, 0, 0, max(last_ver), max(fromctrlid), max(bal_version), max(rcid), max(ccid), max(RG_CODE), max(balance_id),max(AGENCY_ID),max(MB_ID) from '' ||
 account_data.MONTHDETAIL_TABLE_NAME ||
 '' d where d.account_id = :account_id and d.set_month <= :set_month_current and not exists (select 1 from '' ||
 account_data.TABLE_NAME ||
 '' b where b.account_id = :account_id and b.sum_id = d.sum_id and b.RG_CODE=d.RG_CODE) group by sum_id)'';
 execute immediate insert_balance_sql
 using set_month_current, account_data.account_id, set_month_current, account_data.account_id;
 update_money_sql := ''update '' || account_data.TABLE_NAME ||
 '' b set (b.avi_money, b.use_money, b.minus_money, b.aving_money) = (select nvl(sum(m.avi_money), 0), nvl(sum(m.use_money), 0), nvl(sum(m.minus_money), 0), nvl(sum(m.aving_money), 0) from '' ||
 account_data.MONTHDETAIL_TABLE_NAME ||
 '' m where b.sum_id = m.sum_id and b.set_year = m.set_year and b.RG_CODE = m.RG_CODE and m.set_year = :set_year and m.account_id =:account_id and m.set_month <= :set_month_current) where b.account_id = :account_id and exists (select 1 from '' ||
 account_data.MONTHDETAIL_TABLE_NAME ||
 '' d where d.sum_id = b.sum_id and d.set_month <= :set_month_current)'';
 execute immediate update_money_sql
 using set_year, account_data.account_id, set_month_current, account_data.account_id, set_month_current;
 update_month_sql := ''update gl_balance set set_month=:set_month_current where account_id=:account_id'';
 execute immediate update_month_sql
 using set_month_current, account_data.account_id;
 end loop;
 insert_month_sql := ''insert into gl_monthly_balance values(:set_year,:set_month,to_char(sysdate,''''yyyy-mm-dd hh24:mi:ss''''))'';
 execute immediate insert_month_sql
 using set_year, set_month_current - 1;
 commit;
 end if;
 end GL_CLOSE_MONTH_END;';

execute immediate 'CREATE OR REPLACE PROCEDURE groupmanage(srcrgcode   IN VARCHAR2,
 destrgcode  IN VARCHAR2,
 srcsetyear  IN VARCHAR2,
 destsetyear IN VARCHAR2) IS
 BEGIN
 DELETE FROM sys_relation_manager WHERE RG_CODE = destrgcode AND set_year = destsetyear;
 DELETE FROM sys_relations WHERE RG_CODE = destrgcode AND set_year = destsetyear;
 INSERT INTO sys_relation_manager
 (relation_id, relation_code, pri_ele_code, sec_ele_code, set_year, create_date, create_user, latest_op_date, latest_op_user, is_deleted, last_ver, sys_id, relation_type, RG_CODE)
 SELECT newid, relation_code, pri_ele_code, sec_ele_code, destsetyear, to_char(SYSDATE, ''yyyy-MM-dd hh24:mi:ss''), create_user, to_char(SYSDATE, ''yyyy-MM-dd hh24:mi:ss''), latest_op_user, is_deleted, last_ver, sys_id, relation_type, destrgcode
 FROM sys_relation_manager
 WHERE RG_CODE = srcrgcode
 AND set_year = srcsetyear;
 INSERT INTO sys_relations
 (relation_detail_id, relation_id, pri_ele_value, sec_ele_value, set_year, is_deleted, last_ver, RG_CODE)
 SELECT newid, relation_id, pri_ele_value, sec_ele_value, destsetyear, is_deleted, last_ver, destrgcode
 FROM sys_relations
 WHERE RG_CODE = srcrgcode
 AND set_year = srcsetyear;
 FOR var IN (SELECT t.relation_id,t.relation_code FROM sys_relation_manager t WHERE t.RG_CODE = destrgcode AND t.set_year = destsetyear) LOOP
 UPDATE sys_relations a
 SET a.relation_id = var.relation_id
 WHERE a.set_year = destsetyear
 AND a.RG_CODE = destrgcode
 AND a.relation_id = (SELECT relation_id FROM sys_relation_manager WHERE relation_code = var.relation_code AND set_year = srcsetyear AND RG_CODE =srcrgcode );
 END LOOP;
 DELETE FROM sys_year_op_cur_status
 WHERE RG_CODE = destrgcode
 AND set_year = destsetyear;
 INSERT INTO sys_year_op_cur_status
 (op_id, op_name, op_status, RG_CODE, set_year)
 SELECT op_id, op_name, ''0'', destrgcode, destsetyear
 FROM sys_year_op_cur_status
 WHERE RG_CODE = srcrgcode
 AND set_year = srcsetyear;
 COMMIT;
 END groupmanage;';

execute immediate 'create or replace procedure PF_ADDFIELDMANAGERDATA is
 V_CODE VARCHAR2(40);
 V_SQL VARCHAR2(1000);
 V_TMP VARCHAR2(4000);
 V_SQL2 VARCHAR2(200);
 V_SQL3 VARCHAR2(100);
 V_SQL4 VARCHAR2(300);
 V_COL VARCHAR2(40);
 V_NEWID VARCHAR2(60);
 V_FIELDCODE VARCHAR2(60);
 SET_YEAR VARCHAR2(100);
 TABLE_CODE  VARCHAR2(100);
 FIELD_CODE  VARCHAR2(100);
 FIELD_NAME  VARCHAR2(100);
 FIELD_DISPTYPE  VARCHAR2(100);
 DEFAULT_VALUE  VARCHAR2(100);
 TIPS  VARCHAR2(100);
 FIELD_VALUESET  VARCHAR2(1000);
 REMARK  VARCHAR2(1000);
 IS_SYSTEM  number(2) DEFAULT 0;
 CREATE_DATE  VARCHAR2(100);
 CREATE_USER  VARCHAR2(100);
 LATEST_OP_DATE  VARCHAR2(100);
 LATEST_OP_USER  VARCHAR2(100);
 IS_DELETED  number(2) DEFAULT 0;
 SOURCE1  VARCHAR2(100);
 LAST_VER  VARCHAR2(100);
 TYPE REFCURSOR IS REF CURSOR;
 ACURSOR REFCURSOR;
 BCURSOR REFCURSOR;
 CCURSOR REFCURSOR;
 DCURSOR REFCURSOR;
 V_FLAG number;
 begin
 FOR T_CURSOR IN(SELECT T.TABLE_NAME FROM USER_TABLES T WHERE T.table_name LIKE ''SYS_%'') LOOP
 V_CODE := T_CURSOR.TABLE_NAME;
 V_SQL := ''SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME=''''''||V_CODE||'''''''';
 OPEN ACURSOR FOR V_SQL;
 LOOP
 V_SQL3 := ''SELECT NEWID FROM DUAL'';
 OPEN CCURSOR FOR V_SQL3;
 LOOP
 FETCH CCURSOR INTO V_NEWID;
 IF(CCURSOR%NOTFOUND)THEN
 EXIT;
 END IF;
 END LOOP;
 CLOSE CCURSOR;
 FETCH ACURSOR INTO V_COL;
 IF(ACURSOR%NOTFOUND)THEN
 EXIT;
 ELSE
 V_SQL2 := ''SELECT FIELD_CODE FROM SYS_METADATA WHERE FIELD_CODE =''''''||V_COL||'''''''';
 OPEN BCURSOR FOR V_SQL2;
 LOOP
 FETCH BCURSOR INTO V_FIELDCODE;
 IF(BCURSOR%NOTFOUND)THEN
 V_TMP := ''INSERT INTO SYS_FIELDMANAGER(CHR_ID,SET_YEAR,TABLE_CODE,FIELD_CODE,FIELD_NAME,''
 ||''FIELD_DISPTYPE,DEFAULT_VALUE,TIPS,FIELD_VALUESET,REMARK,IS_SYSTEM,CREATE_DATE,''
 ||''CREATE_USER,LATEST_OP_DATE,LATEST_OP_USER,IS_DELETED,SOURCE,LAST_VER) ''
 ||''VALUES(''''''||V_NEWID||'''''',''''''||TO_CHAR(SYSDATE,''YYYY'')||'''''',''''''||V_CODE||'''''',''''''||V_COL||'''''','''''''',''''0'''','''''''','''''''','''''''','''''''',0,''''''||TO_CHAR(SYSDATE,''YYYY-MM-DD HH:MM:SS'')||'''''','''''''',''''''||TO_CHAR(SYSDATE,''YYYY-MM-DD HH:MM:SS'')
 ||'''''','''''''',0,'''''''',''''''||TO_CHAR(SYSDATE,''YYYY-MM-DD HH:MM:SS'')||'''''')'';
 EXECUTE IMMEDIATE V_TMP;
 EXECUTE IMMEDIATE ''COMMIT'';
 EXIT;
 ELSE
 V_SQL4 := ''SELECT SET_YEAR,FIELD_CODE,FIELD_NAME,FIELD_DISPTYPE,DEFAULT_VALUE,TIPS,FIELD_VALUESET,REMARK,IS_SYSTEM,CREATE_DATE,''
 ||''CREATE_USER,LATEST_OP_DATE,LATEST_OP_USER,IS_DELETED,SOURCE,LAST_VER FROM SYS_METADATA ''
 ||''WHERE FIELD_CODE=''''''||V_FIELDCODE||'''''''';
 OPEN DCURSOR FOR V_SQL4;
 LOOP
 FETCH DCURSOR INTO SET_YEAR,FIELD_CODE,FIELD_NAME,
 FIELD_DISPTYPE,DEFAULT_VALUE,TIPS,FIELD_VALUESET,REMARK,IS_SYSTEM,CREATE_DATE,
 CREATE_USER,LATEST_OP_DATE,LATEST_OP_USER,IS_DELETED,SOURCE1,LAST_VER ;
 IF(DCURSOR%NOTFOUND)THEN
 EXIT;
 ELSE
 V_TMP := ''INSERT INTO SYS_FIELDMANAGER(CHR_ID,SET_YEAR,TABLE_CODE,FIELD_CODE,FIELD_NAME,''
 ||''FIELD_DISPTYPE,DEFAULT_VALUE,TIPS,FIELD_VALUESET,REMARK,IS_SYSTEM,CREATE_DATE,''
 ||''CREATE_USER,LATEST_OP_DATE,LATEST_OP_USER,IS_DELETED,SOURCE,LAST_VER) ''
 ||''VALUES(''''''||V_NEWID||'''''',''''''||SET_YEAR||'''''',''''''||V_CODE||'''''',''''''||FIELD_CODE||'''''',''''''||FIELD_NAME
 ||'''''',''''''||FIELD_DISPTYPE||'''''',''''''||DEFAULT_VALUE||'''''',''''''||TIPS||'''''',''''''||FIELD_VALUESET
 ||'''''',''''''||REMARK||'''''',''||IS_SYSTEM||'',''''''||CREATE_DATE||'''''',''''''||CREATE_USER||'''''',''''''||LATEST_OP_DATE
 ||'''''',''''''||LATEST_OP_USER||'''''',''||IS_DELETED||'',''''''||SOURCE1||'''''',''''''||LAST_VER||'''''')'';
 EXECUTE IMMEDIATE V_TMP;
 EXECUTE IMMEDIATE ''COMMIT'';
 EXIT;
 END IF;
 END LOOP;
 CLOSE DCURSOR;
 EXIT;
 END IF;
 END LOOP;
 CLOSE BCURSOR;
 END IF;
 END LOOP;
 CLOSE ACURSOR;
 END LOOP;
 end PF_ADDFIELDMANAGERDATA;';

execute immediate 'create or replace procedure PF_ADDLASTVER is
 V_CODE VARCHAR2(40);
 V_SQL VARCHAR2(1000);
 V_TMP VARCHAR2(4000);
 V_COL VARCHAR2(40);
 TYPE REFCURSOR IS REF CURSOR;
 ACURSOR REFCURSOR;
 V_FLAG number;
 begin
 FOR T_CURSOR IN(SELECT T.TABLE_NAME FROM USER_TABLES T WHERE T.table_name LIKE ''SYS_%'') LOOP
 V_CODE := T_CURSOR.TABLE_NAME;
 V_SQL := ''SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME=''''''||V_CODE||'''''''';
 V_FLAG := 0;
 OPEN ACURSOR FOR V_SQL;
 LOOP
 FETCH ACURSOR INTO V_COL;
 IF(ACURSOR%NOTFOUND)THEN
 EXIT;
 ELSE
 IF(V_COL = ''LAST_VER'') THEN
 V_FLAG := 1;
 EXIT;
 END IF;
 END IF;
 END LOOP;
 CLOSE ACURSOR;
 V_TMP := ''ALTER TABLE ''||V_CODE||'' DROP COLUMN LAST_VER;'';
 IF V_FLAG = 0 THEN
 EXECUTE IMMEDIATE ''ALTER TABLE ''||V_CODE||'' ADD LAST_VER VARCHAR2(30)'';
 END IF;
 END LOOP;
 end PF_ADDLASTVER;';

execute immediate 'create or replace procedure PF_ADDLASTVERDATA is
 V_CODE VARCHAR2(40);
 V_SQL VARCHAR2(1000);
 V_TMP VARCHAR2(4000);
 V_COL VARCHAR2(40);
 TYPE REFCURSOR IS REF CURSOR;
 ACURSOR REFCURSOR;
 V_FLAG number;
 begin
 FOR T_CURSOR IN(SELECT T.TABLE_NAME FROM USER_TABLES T WHERE T.table_name LIKE ''SYS_%'') LOOP
 V_CODE := T_CURSOR.TABLE_NAME;
 V_SQL := ''SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME=''''''||V_CODE||'''''''';
 V_FLAG := 0;
 OPEN ACURSOR FOR V_SQL;
 LOOP
 FETCH ACURSOR INTO V_COL;
 IF(ACURSOR%NOTFOUND)THEN
 EXIT;
 ELSE
 IF(V_COL = ''LAST_VER'') THEN
 V_FLAG := 1;
 EXIT;
 END IF;
 END IF;
 END LOOP;
 CLOSE ACURSOR;
 V_TMP := ''UPDATE  ''||V_CODE||'' SET LAST_VER='' || ''''''2006-08-29 16:45:00'''''';
 IF V_FLAG = 1 THEN
 EXECUTE IMMEDIATE V_TMP;
 END IF;
 END LOOP;
 end PF_ADDLASTVERDATA;';

execute immediate 'create or replace procedure PF_ADDTIMESTAMP is
 V_CODE VARCHAR2(40);
 V_SQL VARCHAR2(1000);
 V_TMP VARCHAR2(4000);
 V_COL VARCHAR2(40);
 TYPE REFCURSOR IS REF CURSOR;
 ACURSOR REFCURSOR;
 V_FLAG number;
 begin
 FOR T_CURSOR IN(SELECT T.TABLE_NAME FROM USER_TABLES T WHERE T.TABLE_NAME=''SYS_YEAR'') LOOP
 V_CODE := T_CURSOR.TABLE_NAME;
 V_SQL := ''SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME=''''''||V_CODE||'''''''';
 V_FLAG := 0;
 OPEN ACURSOR FOR V_SQL;
 LOOP
 FETCH ACURSOR INTO V_COL;
 IF(ACURSOR%NOTFOUND)THEN
 EXIT;
 ELSE
 IF(V_COL = ''LAST_VER'') THEN
 V_FLAG := 1;
 EXIT;
 END IF;
 END IF;
 END LOOP;
 CLOSE ACURSOR;
 V_TMP := ''ALTER TABLE ''||V_CODE||'' DROP COLUMN LAST_VER;'';
 IF V_FLAG = 1 THEN
 EXECUTE IMMEDIATE ''ALTER TABLE ''||V_CODE||'' DROP COLUMN LAST_VER'';
 EXECUTE IMMEDIATE ''ALTER TABLE ''||V_CODE||'' ADD LAST_VER DATE DEFAULT SYSDATE'';
 ELSE
 EXECUTE IMMEDIATE ''ALTER TABLE ''||V_CODE||'' ADD LAST_VER DATE DEFAULT SYSDATE'';
 END IF;
 END LOOP;
 end PF_ADDTIMESTAMP;';

execute immediate 'create or replace procedure pf_build_userRightsInfo_rg(query_id in varchar2,
 RG_CODE  in varchar2) is
 cursor userRole_cur IS
 select t1.user_code, t1.user_name, t3.role_code, t3.role_name
 from sys_usermanage t1, sys_user_role_rule t2, sys_role t3
 where t1.user_id = t2.user_id
 and t2.role_id = t3.role_id --and t1.user_code = ''006198'' and t3.role_code = ''001004''
 order by t1.user_code, t3.role_code;
 userRole_rec userRole_cur%ROWTYPE;
 cursor userRoleMenu_cur is
 select t1.user_code,
 t1.user_name,
 t3.role_code,
 t3.role_name,
 t5.menu_code,
 t5.menu_name,
 t2.RG_CODE
 from sys_usermanage     t1,
 sys_user_role_rule t2,
 sys_role           t3,
 sys_role_menu      t4,
 sys_menu           t5
 where t1.user_id = t2.user_id
 and t2.role_id = t3.role_id
 and t2.role_id = t4.role_id
 and t5.menu_id = t4.menu_id
 and t2.RG_CODE = t4.RG_CODE
 order by t1.user_code, t3.role_code, t5.menu_code;
 userRoleMenu_rec userRoleMenu_cur%ROWTYPE;
 cursor userRoleWorkflow_cur is
 select t1.user_code,
 t1.user_name,
 t3.role_code,
 t3.role_name,
 t6.wf_name,
 t5.node_name
 from sys_usermanage     t1,
 sys_user_role_rule t2,
 sys_role           t3,
 sys_wf_role_node   t4,
 sys_wf_nodes       t5,
 sys_wf_flows       t6
 where t1.user_id = t2.user_id
 and t2.role_id = t3.role_id
 and t2.role_id = t4.role_id
 and t4.node_id = t5.node_id
 and t5.wf_id = t6.wf_id
 order by t1.user_code, t3.role_code, t6.wf_code;
 userRoleWorkflow_rec userRoleWorkflow_cur%ROWTYPE;
 cursor userRoleDataRight_cur is
 select t1.user_code,
 t1.user_name,
 t3.role_code,
 t3.role_name,
 t6.ele_name  element_name,
 t5.ele_value ele_code,
 t5.ele_name  ele_name
 from sys_usermanage         t1,
 sys_user_role_rule     t2,
 sys_role               t3,
 sys_right_group        t4,
 sys_right_group_detail t5,
 sys_element            t6
 where t1.user_id = t2.user_id
 and t2.role_id = t3.role_id
 and t2.rule_id = t4.rule_id
 and t4.right_group_id = t5.right_group_id
 and t6.ele_code = t5.ele_code
 order by t1.user_code,
 t3.role_code,
 t6.ele_name,
 t5.ele_value,
 t5.ele_name;
 userRoleDataRight_rec userRoleDataRight_cur%ROWTYPE;
 userRoleMenu_str      varchar2(4000);
 userRoleWorkflow_str  varchar2(4000);
 userRoleDataRight_str varchar2(4000);
 temp_str              varchar2(100);
 lastWfName            varchar2(100);
 lastElementName       varchar2(100);
 row_count             number;
 sqlstr                varchar2(30000);
 begin
 execute immediate ''delete from SYS_REPORT_USER_RIGHTS_INFO where query_id = '''''' ||
 query_id || '''''''';
 commit;
 OPEN userRole_cur;
 OPen userRoleMenu_cur;
 Open userRoleWorkflow_cur;
 Open userRoleDataRight_cur;
 row_count := 0;
 fetch userRole_cur
 into userRole_rec;
 fetch userRoleMenu_cur
 into userRoleMenu_rec;
 fetch userRoleWorkflow_cur
 into userRoleWorkflow_rec;
 fetch userRoleDataRight_cur
 into userRoleDataRight_rec;
 lastWfName      := '' ''; --userRoleWorkflow_rec.Wf_Name;
 lastElementName := '' ''; -- userRoleDataRight_rec.Element_Name;
 LOOP
 exit when userRole_cur%notfound;
 userRoleMenu_str := '''';
 row_count        := 0;
 LOOP
 exit when userRoleMenu_cur%notfound;
 row_count := row_count + 1;
 if (userRole_rec.User_Code = userRoleMenu_rec.User_Code) and
 (userRole_rec.Role_Code = userRoleMenu_rec.Role_Code) then
 if (row_count <= 100) and (userRoleMenu_rec.RG_CODE = RG_CODE) then
 if (userRoleMenu_str is null) then
 userRoleMenu_str := userRoleMenu_rec.Menu_Code || '' '' ||
 userRoleMenu_rec.Menu_name;
 else
 userRoleMenu_str := userRoleMenu_str || ''\n'' ||
 userRoleMenu_rec.Menu_Code || '' '' ||
 userRoleMenu_rec.Menu_name;
 end if;
 end if;
 fetch userRoleMenu_cur
 into userRoleMenu_rec;
 else
 if (row_count > 100) and (userRoleMenu_rec.RG_CODE = RG_CODE) then
 userRoleMenu_str := userRoleMenu_str || ''\n等等'';
 end if;
 exit;
 end if;
 end loop;
 userRoleWorkflow_str := null;
 row_count            := 0;
 LOOP
 exit when userRoleWorkflow_cur%notfound;
 row_count := row_count + 1;
 if (userRole_rec.User_Code = userRoleWorkflow_rec.User_Code) and
 (userRole_rec.Role_Code = userRoleWorkflow_rec.Role_Code) then
 if (row_count <= 100) then
 if (lastWfName != userRoleWorkflow_rec.Wf_Name) then
 if (userRoleWorkflow_str is null) then
 userRoleWorkflow_str := userRoleWorkflow_rec.Wf_Name || '': '' ||
 ''\n    '' ||
 userRoleWorkflow_rec.Node_Name;
 else
 userRoleWorkflow_str := userRoleWorkflow_str || ''\n'' ||
 userRoleWorkflow_rec.Wf_Name || '': '' ||
 ''\n    '' ||
 userRoleWorkflow_rec.Node_Name;
 end if;
 lastWfName := userRoleWorkflow_rec.Wf_Name;
 else
 userRoleWorkflow_str := userRoleWorkflow_str || ''\n    '' ||
 userRoleWorkflow_rec.Node_Name;
 end if;
 end if;
 fetch userRoleWorkflow_cur
 into userRoleWorkflow_rec;
 else
 lastWfName := '' '';
 if (row_count > 100) then
 userRoleWorkflow_str := userRoleWorkflow_str || '' 等等'';
 end if;
 exit;
 end if;
 end loop;
 userRoledataright_str := null;
 row_count             := 0;
 LOOP
 exit when userRoledataright_cur%notfound;
 row_count := row_count + 1;
 if (userRole_rec.User_Code = userRoleDataRight_rec.User_Code) and
 (userRole_rec.Role_Code = userRoleDataRight_rec.Role_Code) then
 if (row_count <= 100) then
 if userRoleDataRight_rec.Ele_Code = ''#'' then
 temp_str := ''全部权限'';
 else
 temp_str := userRoleDataRight_rec.Ele_Code;
 end if;
 if (lastElementName != userRoleDataRight_rec.Element_Name) then
 if (userRoleDataRight_str is null) then
 userRoledataright_str := userRoleDataRight_rec.Element_Name ||
 '':\n    '' || temp_str || '' '' ||
 userRoleDataRight_rec.Ele_Name;
 else
 userRoledataright_str := userRoledataright_str || ''\n'' ||
 userRoleDataRight_rec.Element_Name ||
 '':\n    '' || temp_str || '' '' ||
 userRoleDataRight_rec.Ele_Name;
 end if;
 lastElementName := userRoleDataRight_rec.Element_Name;
 else
 userRoledataright_str := userRoledataright_str || ''\n    '' ||
 temp_str || '' '' ||
 userRoleDataRight_rec.Ele_Name;
 end if;
 end if;
 fetch userRoledataright_cur
 into userRoledataright_rec;
 else
 lastElementName := '' '';
 if (row_count > 100) then
 userRoledataright_str := userRoledataright_str || '' 等等'';
 end if;
 exit;
 end if;
 end loop;
 sqlstr := ''insert into SYS_REPORT_USER_RIGHTS_INFO (user_code, user_name, role_code, role_name, menu_rights, workflow_rights, data_rights, query_id) '' ||
 ''values('''''' || userRole_rec.User_Code || '''''', '''''' ||
 userRole_rec.User_Name || '''''', '''''' || userRole_rec.Role_Code ||
 '''''', '''''' || userRole_rec.Role_Name || '''''', '''''' ||
 userRoleMenu_str || '''''', '''''' || userRoleWorkflow_str ||
 '''''', '''''' || userRoleDataRight_str || '''''', '''''' || query_id ||
 '''''')'';
 execute immediate sqlstr;
 commit;
 fetch userRole_cur
 into userRole_rec;
 end Loop;
 CLOSE userRole_cur;
 end pf_build_userRightsInfo_rg;';

execute immediate 'create or replace procedure pf_build_userRightsInfo_table(query_id varchar2) is
 cursor userRole_cur IS
 select t1.user_code, t1.user_name, t3.role_code, t3.role_name
 from sys_usermanage t1, sys_user_role_rule t2, sys_role t3
 where t1.user_id = t2.user_id
 and t2.role_id = t3.role_id --and t1.user_code = ''006198'' and t3.role_code = ''001004''
 order by t1.user_code, t3.role_code;
 userRole_rec userRole_cur%ROWTYPE;
 cursor userRoleMenu_cur is
 select t1.user_code,
 t1.user_name,
 t3.role_code,
 t3.role_name,
 t5.menu_code,
 t5.menu_name
 from sys_usermanage     t1,
 sys_user_role_rule t2,
 sys_role           t3,
 sys_role_menu      t4,
 sys_menu           t5
 where t1.user_id = t2.user_id
 and t2.role_id = t3.role_id
 and t2.role_id = t4.role_id
 and t5.menu_id = t4.menu_id --and t1.user_code = ''006198'' and t3.role_code = ''001004''
 order by t1.user_code, t3.role_code, t5.menu_code;
 userRoleMenu_rec userRoleMenu_cur%ROWTYPE;
 cursor userRoleWorkflow_cur is
 select t1.user_code,
 t1.user_name,
 t3.role_code,
 t3.role_name,
 t6.wf_name,
 t5.node_name
 from sys_usermanage     t1,
 sys_user_role_rule t2,
 sys_role           t3,
 sys_wf_role_node   t4,
 sys_wf_nodes       t5,
 sys_wf_flows       t6
 where t1.user_id = t2.user_id
 and t2.role_id = t3.role_id
 and t2.role_id = t4.role_id
 and t4.node_id = t5.node_id
 and t5.wf_id = t6.wf_id
 order by t1.user_code, t3.role_code, t6.wf_code;
 userRoleWorkflow_rec userRoleWorkflow_cur%ROWTYPE;
 cursor userRoleDataRight_cur is
 select t1.user_code,
 t1.user_name,
 t3.role_code,
 t3.role_name,
 t6.ele_name element_name,
 t5.ele_value ele_code,
 t5.ele_name ele_name
 from sys_usermanage         t1,
 sys_user_role_rule     t2,
 sys_role               t3,
 sys_right_group        t4,
 sys_right_group_detail t5,
 sys_element            t6
 where t1.user_id = t2.user_id
 and t2.role_id = t3.role_id
 and t2.rule_id = t4.rule_id
 and t4.right_group_id = t5.right_group_id
 and t6.ele_code = t5.ele_code
 order by t1.user_code,
 t3.role_code,
 t6.ele_name,
 t5.ele_value,
 t5.ele_name;
 userRoleDataRight_rec userRoleDataRight_cur%ROWTYPE;
 userRoleMenu_str      varchar2(4000);
 userRoleWorkflow_str  varchar2(4000);
 userRoleDataRight_str varchar2(4000);
 temp_str varchar2(100);
 lastWfName      varchar2(100);
 lastElementName varchar2(100);
 row_count number;
 sqlstr varchar2(30000);
 begin
 execute immediate ''delete from SYS_REPORT_USER_RIGHTS_INFO where query_id = '''''' ||
 query_id || '''''''';
 commit;
 OPEN userRole_cur;
 OPen userRoleMenu_cur;
 Open userRoleWorkflow_cur;
 Open userRoleDataRight_cur;
 row_count := 0;
 fetch userRole_cur
 into userRole_rec;
 fetch userRoleMenu_cur
 into userRoleMenu_rec;
 fetch userRoleWorkflow_cur
 into userRoleWorkflow_rec;
 fetch userRoleDataRight_cur
 into userRoleDataRight_rec;
 lastWfName      := '' ''; --userRoleWorkflow_rec.Wf_Name;
 lastElementName := '' ''; -- userRoleDataRight_rec.Element_Name;
 LOOP
 exit when userRole_cur%notfound;
 userRoleMenu_str := '''';
 row_count := 0;
 LOOP
 exit when userRoleMenu_cur%notfound;
 row_count := row_count + 1;
 if (userRole_rec.User_Code = userRoleMenu_rec.User_Code) and
 (userRole_rec.Role_Code = userRoleMenu_rec.Role_Code) then
 if (row_count <= 100) then
 if (userRoleMenu_str is null) then
 userRoleMenu_str := userRoleMenu_rec.Menu_Code || '' '' ||
 userRoleMenu_rec.Menu_name;
 else
 userRoleMenu_str := userRoleMenu_str || ''\n'' ||
 userRoleMenu_rec.Menu_Code || '' '' ||
 userRoleMenu_rec.Menu_name;
 end if;
 end if;
 fetch userRoleMenu_cur
 into userRoleMenu_rec;
 else
 if (row_count > 100) then
 userRoleMenu_str := userRoleMenu_str || ''\n等等'';
 end if;
 exit;
 end if;
 end loop;
 userRoleWorkflow_str := null;
 row_count := 0;
 LOOP
 exit when userRoleWorkflow_cur%notfound;
 row_count := row_count + 1;
 if (userRole_rec.User_Code = userRoleWorkflow_rec.User_Code) and
 (userRole_rec.Role_Code = userRoleWorkflow_rec.Role_Code) then
 if (row_count <= 100) then
 if (lastWfName != userRoleWorkflow_rec.Wf_Name) then
 if (userRoleWorkflow_str is null) then
 userRoleWorkflow_str := userRoleWorkflow_rec.Wf_Name || '': '' ||
 ''\n    '' ||
 userRoleWorkflow_rec.Node_Name;
 else
 userRoleWorkflow_str := userRoleWorkflow_str || ''\n'' ||
 userRoleWorkflow_rec.Wf_Name || '': '' ||
 ''\n    '' ||
 userRoleWorkflow_rec.Node_Name;
 end if;
 lastWfName := userRoleWorkflow_rec.Wf_Name;
 else
 userRoleWorkflow_str := userRoleWorkflow_str || ''\n    '' ||
 userRoleWorkflow_rec.Node_Name;
 end if;
 end if;
 fetch userRoleWorkflow_cur
 into userRoleWorkflow_rec;
 else
 lastWfName := '' '';
 if (row_count > 100) then
 userRoleWorkflow_str := userRoleWorkflow_str || '' 等等'';
 end if;
 exit;
 end if;
 end loop;
 userRoledataright_str := null;
 row_count := 0;
 LOOP
 exit when userRoledataright_cur%notfound;
 row_count := row_count + 1;
 if (userRole_rec.User_Code = userRoleDataRight_rec.User_Code) and
 (userRole_rec.Role_Code = userRoleDataRight_rec.Role_Code) then
 if (row_count <= 100) then
 if userRoleDataRight_rec.Ele_Code = ''#'' then
 temp_str := ''全部权限'';
 else
 temp_str := userRoleDataRight_rec.Ele_Code;
 end if;
 if (lastElementName != userRoleDataRight_rec.Element_Name) then
 if (userRoleDataRight_str is null) then
 userRoledataright_str := userRoleDataRight_rec.Element_Name ||
 '':\n    '' || temp_str || '' '' ||
 userRoleDataRight_rec.Ele_Name;
 else
 userRoledataright_str := userRoledataright_str || ''\n'' ||
 userRoleDataRight_rec.Element_Name ||
 '':\n    '' || temp_str || '' '' ||
 userRoleDataRight_rec.Ele_Name;
 end if;
 lastElementName := userRoleDataRight_rec.Element_Name;
 else
 userRoledataright_str := userRoledataright_str || ''\n    '' ||
 temp_str || '' '' ||
 userRoleDataRight_rec.Ele_Name;
 end if;
 end if;
 fetch userRoledataright_cur
 into userRoledataright_rec;
 else
 lastElementName := '' '';
 if (row_count > 100) then
 userRoledataright_str := userRoledataright_str || '' 等等'';
 end if;
 exit;
 end if;
 end loop;
 sqlstr := ''insert into SYS_REPORT_USER_RIGHTS_INFO (user_code, user_name, role_code, role_name, menu_rights, workflow_rights, data_rights, query_id) '' ||
 ''values('''''' || userRole_rec.User_Code || '''''', '''''' ||
 userRole_rec.User_Name || '''''', '''''' || userRole_rec.Role_Code ||
 '''''', '''''' || userRole_rec.Role_Name || '''''', '''''' ||
 userRoleMenu_str || '''''', '''''' || userRoleWorkflow_str ||
 '''''', '''''' || userRoleDataRight_str || '''''', '''''' || query_id ||
 '''''')'';
 execute immediate sqlstr;
 commit;
 fetch userRole_cur
 into userRole_rec;
 end Loop;
 CLOSE userRole_cur;
 end pf_build_userRightsInfo_table;';

execute immediate 'create or replace procedure pf_build_vw_rightgroup Authid Current_User is
 cursor eleType_cur IS
 select distinct se.ele_code as ele_code, se.ele_source as ele_source
 from sys_element se
 where se.is_rightfilter = ''1'' and se.RG_CODE=RG_CODE;
 eleType_rec eleType_cur%ROWTYPE;
 sqlstr      varchar2(32767);
 begin
 OPEN eleType_cur;
 fetch eleType_cur
 into eleType_rec;
 sqlstr := ''create or replace view sys_right_group_info as'';
 sqlstr := sqlstr || '' select distinct  "CHR_ID","CHR_CODE","CHR_NAME","PARENT_ID","RULE_ID","ENABLE" ,"RG_CODE","SET_YEAR" from ('';
 sqlstr := sqlstr || '' select ''''001'''' as chr_id ,'''''''' as chr_code,''''主范围'''' as chr_name,'''''''' as parent_id,0 as rule_id,''''1'''' as enable,RG_CODE,set_year from sys_element GROUP BY RG_CODE, set_year'';
 sqlstr := sqlstr || '' union all select ''''002'''' as chr_id ,'''''''' as chr_code,''''追加范围'''' as chr_name,'''''''' as parent_id,0 as rule_id,''''1'''' as enable,RG_CODE,set_year from sys_element GROUP BY RG_CODE, set_year'';
 sqlstr := sqlstr || '' union all select ''''003'''' as chr_id ,'''''''' as chr_code,''''排除范围'''' as chr_name,'''''''' as parent_id,0 as rule_id,''''1'''' as enable,RG_CODE,set_year  from sys_element GROUP BY RG_CODE, set_year'';
 sqlstr := sqlstr || '' union all select distinct  ele_code || ''''001'''' as chr_id,'''''''' as chr_code,ele_name as chr_name,''''001'''' as parent_id,0 as rule_id,''''1'''' as enable,RG_CODE,set_year from sys_element se where se.is_rightfilter=''''1'''''';
 sqlstr := sqlstr || '' union all select distinct ele_code || ''''002'''' as chr_id,'''''''' as chr_code,ele_name as chr_name,''''002'''' as parent_id,0 as rule_id,''''1'''' as enable,RG_CODE,set_year from sys_element se where se.is_rightfilter=''''1'''''';
 sqlstr := sqlstr || '' union all select distinct ele_code || ''''003'''' as chr_id,'''''''' as chr_code,ele_name as chr_name,''''003'''' as parent_id,0 as rule_id,''''1'''' as enable,RG_CODE,set_year from sys_element se where se.is_rightfilter=''''1'''''';
 LOOP
 exit when eleType_cur%notfound;
 sqlstr := sqlstr || '' '' || ''union all select * from ('';
 sqlstr := sqlstr || '' '' ||
 ''select ''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || c.chr_id as chr_id,c.chr_code as chr_code,c.chr_name as chr_name,''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || parent_id as parent_id,k.rule_id as rule_id, case when c.chr_id1 = c.chr_id then ''''1'''' else ''''0'''' end as enable,c.RG_CODE ,c.set_year from ''|| eleType_rec.ele_source ||'' c,(select chr_id1 as chr_id,rule_id,b.RIGHT_GROUP_DESCRIBE as RIGHT_GROUP_DESCRIBE  from  ''|| eleType_rec.ele_source ||'' c,sys_right_group b,sys_right_group_detail a where b.right_group_id=a.right_group_id and c.RG_CODE=b.RG_CODE AND b.RG_CODE = a.RG_CODE AND a.set_year = b.set_year AND b.set_year = c.set_year  and a.ele_value=c.chr_code and a.ele_code=''''''|| eleType_rec.ele_code ||'''''') k where k.chr_id=c.chr_id'';
 sqlstr := sqlstr || '' '' ||
 ''union  select ''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || c.chr_id as chr_id,c.chr_code as chr_code,c.chr_name as chr_name,''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || parent_id as parent_id,k.rule_id as rule_id, case when c.chr_id2 = c.chr_id then ''''1'''' else ''''0'''' end as enable,c.RG_CODE ,c.set_year from  ''|| eleType_rec.ele_source || '' c,(select chr_id2 as chr_id,rule_id,b.RIGHT_GROUP_DESCRIBE as RIGHT_GROUP_DESCRIBE  from  ''|| eleType_rec.ele_source ||'' c,sys_right_group b,sys_right_group_detail a where b.right_group_id=a.right_group_id and c.RG_CODE=b.RG_CODE AND b.RG_CODE = a.RG_CODE AND a.set_year = b.set_year AND b.set_year = c.set_year  and a.ele_value=c.chr_code and a.ele_code=''''''|| eleType_rec.ele_code ||'''''') k where k.chr_id=c.chr_id'';
 sqlstr := sqlstr || '' '' ||
 ''union  select ''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || c.chr_id as chr_id,c.chr_code as chr_code,c.chr_name as chr_name,''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || parent_id as parent_id,k.rule_id as rule_id, case when c.chr_id3 = c.chr_id then ''''1'''' else ''''0'''' end as enable,c.RG_CODE ,c.set_year from ''|| eleType_rec.ele_source ||'' c,(select chr_id3 as chr_id,rule_id,b.RIGHT_GROUP_DESCRIBE as RIGHT_GROUP_DESCRIBE  from  ''|| eleType_rec.ele_source || ''  c,sys_right_group b,sys_right_group_detail a where b.right_group_id=a.right_group_id  and c.RG_CODE=b.RG_CODE AND b.RG_CODE = a.RG_CODE AND a.set_year = b.set_year AND b.set_year = c.set_year and a.ele_value=c.chr_code and a.ele_code=''''''|| eleType_rec.ele_code ||'''''') k where k.chr_id=c.chr_id'';
 sqlstr := sqlstr || '' '' ||
 ''union  select ''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || c.chr_id as chr_id,c.chr_code as chr_code,c.chr_name as chr_name,''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || parent_id as parent_id,k.rule_id as rule_id, case when c.chr_id4 = c.chr_id then ''''1'''' else ''''0'''' end as enable,c.RG_CODE ,c.set_year from ''|| eleType_rec.ele_source ||'' c,(select chr_id4 as chr_id,rule_id,b.RIGHT_GROUP_DESCRIBE as RIGHT_GROUP_DESCRIBE from ''|| eleType_rec.ele_source || '' c,sys_right_group b,sys_right_group_detail a where b.right_group_id=a.right_group_id and c.RG_CODE=b.RG_CODE AND b.RG_CODE = a.RG_CODE AND a.set_year = b.set_year AND b.set_year = c.set_year and a.ele_value=c.chr_code and a.ele_code=''''''|| eleType_rec.ele_code ||'''''') k where k.chr_id=c.chr_id'';
 sqlstr := sqlstr || '' '' ||
 ''union  select ''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE ||  c.chr_id as chr_id,c.chr_code as chr_code,c.chr_name as chr_name,''''''|| eleType_rec.ele_code ||'''''' || k.RIGHT_GROUP_DESCRIBE || parent_id as parent_id,k.rule_id as rule_id, case when c.chr_id5 = c.chr_id then ''''1'''' else ''''0'''' end as enable,c.RG_CODE ,c.set_year from ''|| eleType_rec.ele_source || '' c,(select chr_id5 as chr_id,rule_id,b.RIGHT_GROUP_DESCRIBE as RIGHT_GROUP_DESCRIBE  from ''|| eleType_rec.ele_source || '' c,sys_right_group b,sys_right_group_detail a where b.right_group_id=a.right_group_id and c.RG_CODE=b.RG_CODE AND b.RG_CODE = a.RG_CODE AND a.set_year = b.set_year AND b.set_year = c.set_year  and a.ele_value=c.chr_code and a.ele_code=''''''|| eleType_rec.ele_code ||'''''') k where k.chr_id=c.chr_id'';
 sqlstr := sqlstr || '' '' ||
 ''union  select a.ele_code || b.RIGHT_GROUP_DESCRIBE || ''''all'''' as chr_id,'''''''' as chr_code,case when b.RIGHT_GROUP_DESCRIBE=''''001'''' then ''''全部权限'''' else ''''无'''' end  as chr_name, a.ele_code || b.RIGHT_GROUP_DESCRIBE as parent_id,b.rule_id as rule_id,''''1'''' as enable,b.RG_CODE ,b.set_year from sys_right_group b,sys_right_group_detail a where a.ele_value=''''#'''' and b.right_group_id=a.right_group_id and b.RG_CODE=a.RG_CODE and b.set_year=a.set_year'';
 sqlstr := sqlstr || '' '' || '')'';
 fetch eleType_cur
 into eleType_rec;
 end loop;
 sqlstr := sqlstr || '' '' || '')'';
 execute immediate sqlstr;
 commit;
 close eleType_cur;
 end pf_build_vw_rightgroup;';

execute immediate 'create or replace procedure pf_changeRgCode(RG_CODE in VARCHAR2) is
 begin
 FOR T_CURSOR IN (SELECT DISTINCT T.TABLE_NAME
 FROM USER_TABLES T, USER_TAB_COLUMNS B
 WHERE T.table_name = B.table_name
 and B.COLUMN_NAME = ''RG_CODE'') LOOP
 IF (T_CURSOR.TABLE_NAME <> ''RG_CODE'') THEN
 EXECUTE IMMEDIATE ''UPDATE '' || T_CURSOR.TABLE_NAME || '' set RG_CODE='' ||
 RG_CODE;
 END IF;
 END LOOP;
 commit;
 end pf_changeRgCode;';

execute immediate 'CREATE OR REPLACE PROCEDURE PF_GENERATE_DAY(P_SET_YEAR number) IS
 V_SQL VARCHAR2(500);
 V_WEEK number(1);
 V_HOLIDAY number(1);
 V_TMP VARCHAR2(100);
 V_SETYEAR number(4);
 V_MONTH VARCHAR2(10);
 V_DAY VARCHAR2(10);
 BEGIN
 IF (LENGTH(P_SET_YEAR) = 4) THEN
 V_SQL := '' INSERT INTO SYS_WORK_DAY (SET_YEAR, DATE_MONTH, DATE_DAY,CHINA_DATE)'' ||
 '' SELECT '''''' || P_SET_YEAR ||
 '''''' AS SET_YEAR,SUBSTR(TO_CHAR(TO_DATE('''''' || P_SET_YEAR ||
 ''0101'''', ''''yyyymmdd'''') + ROWNUM - 1, ''''yyyy-mm-dd''''),6,2) AS DATE_MONTH,''||
 ''SUBSTR(TO_CHAR(TO_DATE('''''' || P_SET_YEAR ||
 ''0101'''', ''''yyyymmdd'''') + ROWNUM - 1, ''''yyyy-mm-dd''''),9,2) AS DATE_DAY,''||
 ''TO_CHAR(TO_DATE('''''' || P_SET_YEAR ||
 ''0101'''', ''''yyyymmdd'''') + ROWNUM - 1, ''''yyyy-mm-dd'''') AS CHINA_DATE''||
 '' FROM ALL_OBJECTS '' ||
 '' WHERE ROWNUM <= TO_CHAR(TO_DATE('''''' || P_SET_YEAR ||
 ''1231'''', ''''yyyymmdd''''), ''''ddd'''')'';
 EXECUTE IMMEDIATE V_SQL;
 EXECUTE IMMEDIATE '' COMMIT'';
 END IF;
 V_WEEK := 7;
 V_HOLIDAY := 1;
 FOR T_CURSOR IN(select set_year,DATE_MONTH,DATE_DAY from sys_work_day order by set_year,date_month,date_day) LOOP
 V_SETYEAR := T_CURSOR.set_year;
 V_MONTH := T_CURSOR.DATE_MONTH;
 V_DAY := T_CURSOR.DATE_DAY;
 V_TMP := ''UPDATE SYS_WORK_DAY SET WEEK=''||V_WEEK||'',DATE_TYPE=''||V_HOLIDAY||'' WHERE SET_YEAR=''||V_SETYEAR
 ||'' AND DATE_MONTH=''''''||V_MONTH||'''''' AND DATE_DAY=''''''||V_DAY||'''''''' ;
 EXECUTE IMMEDIATE V_TMP;
 EXECUTE IMMEDIATE ''COMMIT'';
 IF(V_WEEK = 7) THEN
 V_WEEK := 1;
 ELSE
 V_WEEK := V_WEEK + 1;
 END IF;
 IF(V_WEEK = 6 or V_WEEK = 7) THEN
 V_HOLIDAY := 1;
 ELSE
 V_HOLIDAY := 0;
 END IF;
 END LOOP;
 END PF_GENERATE_DAY;';

execute immediate 'create or replace procedure pf_changeSetYear(set_year in number) is
 pre_year number(4);
 begin
 pre_year := set_year - 1;
 FOR T_CURSOR IN (SELECT DISTINCT T.TABLE_NAME
 FROM USER_TABLES T, USER_TAB_COLUMNS B
 WHERE T.table_name = B.table_name
 and B.COLUMN_NAME = ''SET_YEAR'') LOOP
 IF (T_CURSOR.TABLE_NAME <> ''SYS_YEAR'' AND
 T_CURSOR.TABLE_NAME <> ''SYS_WORK_DAY'') THEN
 EXECUTE IMMEDIATE ''UPDATE ''|| T_CURSOR.TABLE_NAME ||'' SET SET_YEAR='''''' || set_year ||
 '''''' where set_year='''''' || pre_year || '''''''';
 END IF;
 END LOOP;
 EXECUTE IMMEDIATE ''DELETE FROM SYS_WORK_DAY WHERE SET_YEAR='' || set_year;
 pf_generate_day(set_year);
 commit;
 end pf_changeSetYear;';

 
execute immediate 'create or replace procedure PF_GLCCIDTRANSFER(P_SWITCH01 BOOLEAN DEFAULT FALSE) is
 V_ELE_CODE VARCHAR2(40);
 V_TABLENAME VARCHAR2(40);
 V_SQL VARCHAR2(8000);
 V_TMP VARCHAR2(8000);
 V_ORGTYPE_CODE  VARCHAR2(40);
 V_CZGB_CODE VARCHAR2(40);
 begin
 V_TMP := '''';
 FOR T_CURSOR IN(SELECT DISTINCT SUBSTR(COLUMN_NAME,0, LENGTH(COLUMN_NAME)-3) AS ELE_CODE from user_col_comments where table_name=''GL_CCIDS'' AND COLUMN_NAME LIKE ''%_ID''
 AND COLUMN_NAME != ''CCID'' AND COLUMN_NAME != ''COA_ID'') LOOP
 V_ELE_CODE := T_CURSOR.ELE_CODE;
 FOR TT_CURSOR IN(SELECT ELE_SOURCE FROM SYS_ELEMENT WHERE ELE_CODE = V_ELE_CODE) LOOP
 SELECT ELE_SOURCE INTO V_TABLENAME FROM SYS_ELEMENT WHERE ELE_CODE = V_ELE_CODE;
 SELECT CZGB_CODE INTO V_CZGB_CODE FROM SYS_ELEMENT WHERE ELE_CODE = V_ELE_CODE;
 IF(V_CZGB_CODE IS NULL) THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CHR_CODE FROM '' || V_TABLENAME ||
 '' WHERE CHR_ID = A.'' || V_ELE_CODE ||''_ID AND SET_YEAR = A.SET_YEAR) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT CHR_NAME FROM '' || V_TABLENAME ||
 '' WHERE CHR_ID = A.'' || V_ELE_CODE ||''_ID AND SET_YEAR = A.SET_YEAR) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSE
 IF(V_CZGB_CODE = ''ELEMENT02'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM T_DICENUMITEM T ''||
 '' WHERE T.ELEMENTCODE=''''INDSOURCE'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM T_DICENUMITEM T ''||
 ''  WHERE T.ELEMENTCODE=''''INDSOURCE'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF(V_CZGB_CODE = ''PREPAYTAG'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM T_DICENUMITEM T ''||
 '' WHERE T.ELEMENTCODE=''''APPTYPE2'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM T_DICENUMITEM T ''||
 ''  WHERE T.ELEMENTCODE=''''APPTYPE2'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''BDGVERSION'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM T_DICENUMITEM T ''||
 '' WHERE T.ELEMENTCODE=''''BDGVERSION'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM T_DICENUMITEM T ''||
 ''  WHERE T.ELEMENTCODE=''''BDGVERSION'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''PROGRAMTYPE'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM T_DICENUMITEM T ''||
 '' WHERE T.ELEMENTCODE=''''PROGRAMTYPE'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM T_DICENUMITEM T ''||
 ''  WHERE T.ELEMENTCODE=''''PROGRAMTYPE'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''SETTLEMODE'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM T_DICENUMITEM T ''||
 '' WHERE T.ELEMENTCODE=''''SETTLEMODE'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM T_DICENUMITEM T ''||
 ''  WHERE T.ELEMENTCODE=''''SETTLEMODE'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''MAINBODY'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM T_DICENUMITEM T ''||
 '' WHERE T.ELEMENTCODE=''''MAINBODY'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM T_DICENUMITEM T ''||
 ''  WHERE T.ELEMENTCODE=''''MAINBODY'''' AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''ACCOUNT'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT ACCTCODE FROM T_FMACCOUNT T ''||
 '' WHERE ACCTID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT ACCTNAME FROM T_FMACCOUNT T ''||
 ''  WHERE ACCTID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''ACCSYSTYPE'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT ACCSYSTYPECODE FROM T_FMACCOUNTSYSTEMTYPE T ''||
 '' WHERE ACCTSYSTYPEID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT ACCSYSTYPESPEC FROM T_FMACCOUNTSYSTEMTYPE T ''||
 ''  WHERE ACCTSYSTYPEID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''CURRENCY'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CYCODE FROM T_FMCURRENCY T ''||
 '' WHERE CYID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT CYNAME FROM T_FMCURRENCY T ''||
 ''  WHERE CYID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''PAYMENTBANKACCOUNT'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM T_PUBBANKACCOUNT T ''||
 '' WHERE T.TYPE IN(3, 6, 7) AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM T_PUBBANKACCOUNT T ''||
 ''  WHERE T.TYPE IN(3, 6, 7) AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''GATHERINGBANKACCOUNT'') THEN
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM T_PUBBANKACCOUNT T ''||
 '' WHERE T.TYPE=1 AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM T_PUBBANKACCOUNT T ''||
 ''  WHERE T.TYPE=1 AND ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''DEPARTMENTDIVISION'') THEN
 V_TABLENAME := ''T_PUBMOFDEPARTMENT'';
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM ''|| V_TABLENAME ||'' T ''||
 '' WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM ''|| V_TABLENAME ||'' T ''||
 ''  WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''BDGMANAGEDIVISION'') THEN
 V_TABLENAME := ''T_PUBMOFDEPARTMENT'';
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM ''|| V_TABLENAME ||'' T ''||
 '' WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM ''|| V_TABLENAME ||'' T ''||
 ''  WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''BDGAGENCY'') THEN
 V_TABLENAME := ''T_PUBAGENCY'';
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM ''|| V_TABLENAME ||'' T ''||
 '' WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM ''|| V_TABLENAME ||'' T ''||
 ''  WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''ADMCHARGE'') THEN
 V_TABLENAME := ''T_PUBCHARGINGPROGRAM'';
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM ''|| V_TABLENAME ||'' T ''||
 '' WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM ''|| V_TABLENAME ||'' T ''||
 ''  WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''AGENTBANK'') THEN
 V_TABLENAME := ''T_PUBBANK'';
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM ''|| V_TABLENAME ||'' T ''||
 '' WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM ''|| V_TABLENAME ||'' T ''||
 ''  WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSIF (V_CZGB_CODE = ''ELEMENT05'') THEN
 V_TABLENAME := ''T_PUBBANK'';
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM ''|| V_TABLENAME ||'' T ''||
 '' WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM ''|| V_TABLENAME ||'' T ''||
 ''  WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 ELSE
 V_TABLENAME := ''T_PUB'' || V_CZGB_CODE;
 V_TMP := V_TMP || '', A.'' || V_ELE_CODE || ''_ID,'' || ''(SELECT CODE FROM ''|| V_TABLENAME ||'' T ''||
 '' WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_CODE, (SELECT NAME FROM ''|| V_TABLENAME ||'' T ''||
 ''  WHERE ITEMID = A.'' || V_ELE_CODE ||''_ID ) AS '' ||
 V_ELE_CODE || ''_NAME'';
 END IF;
 END IF;
 END LOOP;
 END LOOP;
 V_SQL := ''CREATE OR REPLACE VIEW VW_GL_CCIDS AS SELECT A.SET_YEAR, A.CCID, A.COA_ID'' || V_TMP ||
 '' FROM GL_CCIDS A'';
 EXECUTE IMMEDIATE ''COMMIT'';
 EXECUTE IMMEDIATE V_SQL;
 EXECUTE IMMEDIATE ''COMMIT'';
 V_TMP := ''select chr_id,chr_code,chr_name,(select ORGTYPE_CODE from SYS_ORGTYPE where ELE_CODE=''''RG'''' and rownum=1 ) as ORG_TYPE,set_year,null as RG_CODE,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5 '' ||
 '' from ele_region'';
 FOR TT_CURSOR IN(SELECT E.ELE_CODE,E.ELE_SOURCE,OT.ORGTYPE_CODE FROM SYS_ELEMENT E  INNER JOIN SYS_ORGTYPE OT ON E.Ele_Code=OT.Ele_Code WHERE E.ELE_CODE IN (''AGENCY'',''PB'',''CB'',''IB'')) LOOP
 V_ELE_CODE:=TT_CURSOR.ELE_CODE;
 V_TABLENAME:=TT_CURSOR.ELE_SOURCE;
 V_ORGTYPE_CODE:=TT_CURSOR.ORGTYPE_CODE;
 V_TMP := V_TMP || '' UNION ALL select chr_id,chr_code,chr_name,'''''' || V_ORGTYPE_CODE ||'''''' as ORG_TYPE,set_year,null as RG_CODE,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5 '' ||
 '' from '' || V_TABLENAME ;
 END LOOP;
 V_SQL := ''CREATE OR REPLACE VIEW VW_SYS_ORGS AS '' || V_TMP ;
 EXECUTE IMMEDIATE V_SQL;
 EXECUTE IMMEDIATE ''COMMIT'';
 IF(P_SWITCH01) THEN
 V_TMP := ''select to_char(USERID) AS user_id, CODE as user_code, NAME as user_name, PASSWORD as password, ''''001'''' as  org_type, ''''001'''' as org_code, 1 as level_num, 1 as is_leaf, null as gender, null as telephone, null as mobile,1  as  enabled '' ||
 '' from   T_CAUSER '';
 ELSE
 V_TMP := ''select "USER_ID","USER_CODE","USER_NAME","PASSWORD","ORG_TYPE","ORG_CODE","LEVEL_NUM","IS_LEAF","GENDER","TELEPHONE","MOBILE","ENABLED" '' ||
 '' from sys_usermanage '';
 END IF;
 V_SQL := ''CREATE OR REPLACE VIEW SYS_USER AS '' || V_TMP ;
 EXECUTE IMMEDIATE V_SQL;
 EXECUTE IMMEDIATE ''COMMIT'';
 V_SQL :='' delete from sys_user_role where user_id=(select user_ID from sys_user  u where u.user_code=''''999001002'''') '' ;
 EXECUTE IMMEDIATE V_SQL;
 EXECUTE IMMEDIATE ''COMMIT'';
 V_SQL :=''insert into sys_user_role select (select user_ID from sys_user  u where u.user_code=''''999001002'''') as user_ID, '' ||
 '' R.role_ID,R.set_year,sysdate '' ||
 '' from sys_role R '' ||
 '' where R.role_name like ''''%超级%'''' or R.role_name like ''''%管理员%'''''' ;
 EXECUTE IMMEDIATE V_SQL;
 EXECUTE IMMEDIATE ''COMMIT'';
 end PF_GLCCIDTRANSFER;';

execute immediate 'create or replace procedure pf_import_ele is
 lsSql varchar(4000);
 type varchar_array is table of varchar2(60) index by binary_integer;
 type lcCursors is ref cursor;
 lsCursor lcCursors;--结果集游标
 ele_source_array varchar_array;
 i integer;
 ele_source_length integer;
 ele_source varchar2(60);
 begin
 i:=1;
 ele_source_length :=0;
 lsSql := ''select distinct lower(ele_source) from sys_element where is_local=0'';
 open lsCursor for lsSql;
 loop
 fetch lsCursor into ele_source;
 if(lsCursor%notfound)then
 exit;
 else
 ele_source_array(i) := ele_source;
 i := i+1;
 ele_source_length := ele_source_length + 1;
 end if;
 end loop;
 close lsCursor;
 lsSql := null;
 if(ele_source_length = 0)then
 return;
 end if;
 i:=1;
 while(i<=ele_source_length)
 loop
 begin
 if(ele_source_array(i) not in
 (''ele_accountant_subject'',''ele_account'',''ele_bank'',''ele_budget_item_summary'',''ele_region'',''ele_book_set''))then
 lsSql := ''insert into ''||ele_source_array(i)||''(set_year,chr_id,chr_code,disp_code,chr_name,level_num,is_leaf''
 ||'',enabled,create_date,create_user,latest_op_date,is_deleted,latest_op_user,''
 ||''last_ver,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5,RG_CODE,parent_id)''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''',null ''
 ||''from ''||substr(ele_source_array(i),5,length(ele_source_array(i)))||''@hbunion a ''
 ||''where not exists (select 1 from ''||ele_source_array(i)||'' b where ''
 ||''a.disp_code = b.chr_code and a.set_year = b.set_year)'';
 execute immediate lsSql;
 lsSql := ''update ''||ele_source_array(i)||'' a set a.chr_code1=substr(a.chr_code,1,3),''
 ||''a.chr_code2=(case when length(a.chr_code)>6 then substr(a.chr_code,1,6) else null end),''
 ||''a.chr_code3=(case when length(a.chr_code)>9 then substr(a.chr_code,1,9) else null end),''
 ||''a.chr_code4=(case when length(a.chr_code)>12 then substr(a.chr_code,1,12) else null end),''
 ||''a.chr_code5=(case when length(a.chr_code)>15 then substr(a.chr_code,1,15) else null end),''
 ||''a.parent_id=(select chr_id from ''||ele_source_array(i)||'' b where b.chr_code = substr(a.chr_code,1,length
 (a.chr_code)-3)''
 ||''and set_year = 2006 and rownum = 1) where level_num <> 1 and set_year = 2006 and parent_id is null '';
 execute immediate lsSql;
 commit;
 else if(ele_source_array(i) = ''ele_book_set'')then
 lsSql := ''insert into ''||ele_source_array(i)||''(set_year,chr_id,chr_code,disp_code,chr_name,level_num,is_leaf''
 ||'',enabled,create_date,create_user,latest_op_date,is_deleted,latest_op_user,start_date,set_type,''
 ||''last_ver,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5,RG_CODE,parent_id)''
 ||'' select set_year,newid(),set_code,set_code,''
 ||''set_name,decode(length(set_code),4,1,8,2,12,3,16,4,20,5),1,1,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),0,null,start_date,set_type,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''',null ''
 ||''from ''||substr(ele_source_array(i),5,length(ele_source_array(i)))||''@hbunion a ''
 ||''where not exists (select 1 from ''||ele_source_array(i)||'' b where ''
 ||''a.set_code = b.chr_code and a.set_year = b.set_year)'';
 execute immediate lsSql;
 lsSql := ''update ''||ele_source_array(i)||'' a set a.chr_code1=substr(a.chr_code,1,4),''
 ||''a.chr_code2=(case when length(a.chr_code)>8 then substr(a.chr_code,1,8) else null end),''
 ||''a.chr_code3=(case when length(a.chr_code)>12 then substr(a.chr_code,1,12) else null end),''
 ||''a.chr_code4=(case when length(a.chr_code)>16 then substr(a.chr_code,1,16) else null end),''
 ||''a.chr_code5=(case when length(a.chr_code)>20 then substr(a.chr_code,1,20) else null end),''
 ||''a.parent_id=(select chr_id from ''||ele_source_array(i)||'' b where b.chr_code = substr(a.chr_code,1,length
 (a.chr_code)-4)''
 ||''and set_year = 2006 and rownum = 1) where level_num <> 1 and set_year = 2006 and parent_id is null'';
 execute immediate lsSql;
 commit;
 else if(ele_source_array(i) = ''ele_account'')then
 lsSql := ''insert into ''||ele_source_array(i)||''(set_year,chr_id,chr_code,disp_code,chr_name,level_num,is_leaf''
 ||'',enabled,create_date,create_user,latest_op_date,is_deleted,latest_op_user,''
 ||''last_ver,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5,RG_CODE,account_no,''
 ||''account_name,bank_code,account_type,owner_code, parent_id)''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''', ''
 ||''account_no,account_name,bank_code,decode(account_type,3,11,4,12),owner_code,null ''
 ||''from bank_account_pay@hbunion a where not exists (select 1 from ''
 ||ele_source_array(i)||'' b where a.disp_code = b.chr_code and a.set_year = b.set_year) ''
 ||''union ''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''', ''
 ||''account_no,account_name,bank_code,decode(account_type,1,21,2,22),owner_code,null ''
 ||''from bank_account_clear@hbunion a where not exists (select 1 from ''
 ||ele_source_array(i)||'' b where a.disp_code = b.chr_code and a.set_year = b.set_year) ''
 ||''union ''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''', ''
 ||''account_no,account_name,bank_code,31,owner_code,null ''
 ||''from bank_account_agent@hbunion a where not exists (select 1 from ''
 ||ele_source_array(i)||'' b where a.disp_code = b.chr_code and a.set_year = b.set_year) ''
 ||''union ''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''', ''
 ||''account_no,account_name,bank_code,41,owner_code,null ''
 ||''from bank_account_income@hbunion a where not exists (select 1 from ''
 ||ele_source_array(i)||'' b where a.disp_code = b.chr_code and a.set_year = b.set_year) ''
 ||''union ''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''', ''
 ||''account_no,account_name,bank_code,51,owner_code,null ''
 ||''from bank_account_black@hbunion a where not exists (select 1 from ''
 ||ele_source_array(i)||'' b where a.disp_code = b.chr_code and a.set_year = b.set_year) '';
 execute immediate lsSql;
 lsSql := ''update ''||ele_source_array(i)||'' a set a.chr_code1=substr(a.chr_code,1,3),''
 ||''a.chr_code2=(case when length(a.chr_code)>6 then substr(a.chr_code,1,6) else null end),''
 ||''a.chr_code3=(case when length(a.chr_code)>9 then substr(a.chr_code,1,9) else null end),''
 ||''a.chr_code4=(case when length(a.chr_code)>12 then substr(a.chr_code,1,12) else null end),''
 ||''a.chr_code5=(case when length(a.chr_code)>15 then substr(a.chr_code,1,15) else null end),''
 ||''a.parent_id=(select chr_id from ''||ele_source_array(i)||'' b where b.chr_code = substr(a.chr_code,1,length
 (a.chr_code)-3)''
 ||''and set_year = 2006 and rownum = 1) where level_num <> 1 and set_year = 2006 and parent_id is null'';
 execute immediate lsSql;
 commit;
 else if(ele_source_array(i) = ''ele_bank'')then
 lsSql := ''insert into ''||ele_source_array(i)||''(set_year,chr_id,chr_code,disp_code,chr_name,level_num,is_leaf''
 ||'',enabled,create_date,create_user,latest_op_date,is_deleted,latest_op_user,''
 ||''last_ver,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5,RG_CODE,parent_id)''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''',null ''
 ||''from pay_bank@hbunion a ''
 ||''where not exists (select 1 from ''||ele_source_array(i)||'' b where ''
 ||''a.disp_code = b.chr_code and a.set_year = b.set_year) ''
 ||''union ''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''',null ''
 ||''from clear_bank@hbunion a ''
 ||''where not exists (select 1 from ''||ele_source_array(i)||'' b where ''
 ||''a.disp_code = b.chr_code and a.set_year = b.set_year) ''
 ||''union ''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''',null ''
 ||''from income_bank@hbunion a ''
 ||''where not exists (select 1 from ''||ele_source_array(i)||'' b where ''
 ||''a.disp_code = b.chr_code and a.set_year = b.set_year) '';
 execute immediate lsSql;
 lsSql := ''update ''||ele_source_array(i)||'' a set a.chr_code1=substr(a.chr_code,1,3),''
 ||''a.chr_code2=(case when length(a.chr_code)>6 then substr(a.chr_code,1,6) else null end),''
 ||''a.chr_code3=(case when length(a.chr_code)>9 then substr(a.chr_code,1,9) else null end),''
 ||''a.chr_code4=(case when length(a.chr_code)>12 then substr(a.chr_code,1,12) else null end),''
 ||''a.chr_code5=(case when length(a.chr_code)>15 then substr(a.chr_code,1,15) else null end),''
 ||''a.parent_id=(select chr_id from ''||ele_source_array(i)||'' b where b.chr_code = substr(a.chr_code,1,length
 (a.chr_code)-3)''
 ||''and set_year = 2006 and rownum = 1) where level_num <> 1 and set_year = 2006 and parent_id is null'';
 execute immediate lsSql;
 commit;
 else if(ele_source_array(i) = ''ele_budget_item_summary'')then
 lsSql := ''insert into ''||ele_source_array(i)||''(set_year,chr_id,chr_code,disp_code,chr_name,level_num,is_leaf''
 ||'',enabled,create_date,create_user,latest_op_date,is_deleted,latest_op_user,''
 ||''last_ver,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5,RG_CODE,owner_code,parent_id)''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,''''210000'''',AGENCY_CODE,null ''
 ||''from ''||substr(ele_source_array(i),5,length(ele_source_array(i)))||''@hbunion a ''
 ||''where not exists (select 1 from ''||ele_source_array(i)||'' b where ''
 ||''a.disp_code = b.chr_code and a.set_year = b.set_year)'';
 execute immediate lsSql;
 lsSql := ''update ''||ele_source_array(i)||'' a set a.chr_code1=substr(a.chr_code,1,3),''
 ||''a.chr_code2=(case when length(a.chr_code)>6 then substr(a.chr_code,1,6) else null end),''
 ||''a.chr_code3=(case when length(a.chr_code)>9 then substr(a.chr_code,1,9) else null end),''
 ||''a.chr_code4=(case when length(a.chr_code)>12 then substr(a.chr_code,1,12) else null end),''
 ||''a.chr_code5=(case when length(a.chr_code)>15 then substr(a.chr_code,1,15) else null end),''
 ||''a.parent_id=(select chr_id from ''||ele_source_array(i)||'' b where b.chr_code = substr(a.chr_code,1,length
 (a.chr_code)-3)''
 ||''and set_year = 2006 and rownum = 1) where level_num <> 1 and set_year = 2006 and parent_id is null'';
 execute immediate lsSql;
 commit;
 else if(ele_source_array(i) = ''ele_region'')then
 lsSql := ''insert into ''||ele_source_array(i)||''(set_year,chr_id,chr_code,disp_code,chr_name,level_num,is_leaf''
 ||'',enabled,create_date,create_user,latest_op_date,is_deleted,latest_op_user,''
 ||''last_ver,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5,parent_id)''
 ||'' select set_year,newid(),disp_code,disp_code,''
 ||''disp_name,nvl(level_num,1),nvl(is_leaf,1),enabled,to_char(sysdate,''''YYYY-MM-DD''''),''
 ||''null,to_char(sysdate,''''YYYY-MM-DD''''),is_delete,null,''
 ||''to_char(sysdate,''''YYYY-MM-DD HH:MM:SS''''),null,null,null,null,null,null ''
 ||''from ''||substr(ele_source_array(i),5,length(ele_source_array(i)))||''@hbunion a ''
 ||''where not exists (select 1 from ''||ele_source_array(i)||'' b where ''
 ||''a.disp_code = b.chr_code and a.set_year = b.set_year)'';
 execute immediate lsSql;
 lsSql := ''update ''||ele_source_array(i)||'' a set a.chr_code1=substr(a.chr_code,1,3),''
 ||''a.chr_code2=(case when length(a.chr_code)>6 then substr(a.chr_code,1,6) else null end),''
 ||''a.chr_code3=(case when length(a.chr_code)>9 then substr(a.chr_code,1,9) else null end),''
 ||''a.chr_code4=(case when length(a.chr_code)>12 then substr(a.chr_code,1,12) else null end),''
 ||''a.chr_code5=(case when length(a.chr_code)>15 then substr(a.chr_code,1,15) else null end),''
 ||''a.parent_id=(select chr_id from ''||ele_source_array(i)||'' b where b.chr_code = substr(a.chr_code,1,length
 (a.chr_code)-3)''
 ||''and set_year = 2006 and rownum = 1) where level_num <> 1 and set_year = 2006 and parent_id is null'';
 execute immediate lsSql;
 commit;
 end if;
 end if;
 end if;
 end if;
 end if;
 end if;
 exception when others then
 rollback;
 end;
 i := i+1;
 end loop;
 end pf_import_ele;';
execute immediate 'create or replace procedure rightGroupManage(srcRGCode   in varchar2,
 destRGCode  in varchar2,
 srcSetYear  in varchar2,
 destSetYear in varchar2) as
 begin
 begin
 declare
 cursor cur_srg is
 select srg.right_group_id, srg.rule_id
 from sys_right_group srg
 where srg.RG_CODE = srcRGCode
 and srg.set_year = srcSetYear;
 ser_old_rule_id    number;
 ser_new_rule_id    number;
 ser_right_group_id varchar2(38);
 new_right_group_id varchar2(38);
 excuteSQL          varchar2(200);
 count1              number;
 create_temp_sql_str varchar2(200);
 BEGIN
 execute immediate ''ALTER TABLE sys_right_group_detail DISABLE ALL TRIGGERS'';
 execute immediate ''delete from sys_right_group where RG_CODE='''''' ||
 destRGCode || '''''''' || '' AND SET_YEAR= '' ||
 destSetYear;
 execute immediate ''delete from SYS_RIGHT_GROUP_DETAIL where RG_CODE='''''' ||
 destRGCode || '''''''' || '' AND SET_YEAR= '' ||
 destSetYear;
 execute immediate ''delete from SYS_RIGHT_GROUP_TYPE where RG_CODE='''''' ||
 destRGCode || '''''''' || '' AND SET_YEAR= '' ||
 destSetYear;
 select count(*)
 into count1
 from user_tables
 where table_name = ''SYS_RULE_TEMP_TAB'';
 if count1 <= 0 then
 create_temp_sql_str := ''create table sys_rule_temp_tab(old_rule_id number,new_rule_id number)'';
 execute immediate create_temp_sql_str;
 end if;
 commit;
 open cur_srg;
 fetch cur_srg
 into ser_right_group_id, ser_old_rule_id;
 while cur_srg%found loop
 new_right_group_id := newid;
 begin
 excuteSQL := ''select new_rule_id  from sys_rule_temp_tab where old_rule_id='' ||
 ser_old_rule_id;
 EXECUTE IMMEDIATE excuteSQL
 INTO ser_new_rule_id;
 EXCEPTION
 when NO_DATA_FOUND THEN
 select seq_other_id.nextval into ser_new_rule_id from dual;
 insert into SYS_RULE
 (RULE_ID,
 RULE_CODE,
 RULE_NAME,
 REMARK,
 SET_YEAR,
 ENABLED,
 RULE_CLASSIFY,
 SYS_REMARK,
 RIGHT_TYPE,
 CREATE_USER,
 CREATE_DATE,
 LATEST_OP_USER,
 LATEST_OP_DATE,
 LAST_VER,
 RG_CODE)
 select ser_new_rule_id,
 RULE_CODE,
 RULE_NAME,
 REMARK,
 destSetYear,
 ENABLED,
 RULE_CLASSIFY,
 SYS_REMARK,
 RIGHT_TYPE,
 CREATE_USER,
 to_char(sysdate, ''yyyy-MM-dd hh24:mi:ss''),
 LATEST_OP_USER,
 to_char(sysdate, ''yyyy-MM-dd hh24:mi:ss''),
 to_char(sysdate, ''yyyy-MM-dd hh24:mi:ss''),
 destRGCode
 from SYS_RULE
 where rule_id = ser_old_rule_id;
 create_temp_sql_str := ''insert into sys_rule_temp_tab(old_rule_id,new_rule_id) values('' ||
 ser_old_rule_id || '','' ||
 ser_new_rule_id || '')'';
 EXECUTE IMMEDIATE create_temp_sql_str;
 when OTHERS THEN
 ser_new_rule_id := ser_new_rule_id;
 end;
 insert into SYS_RIGHT_GROUP
 (RIGHT_GROUP_ID,
 RIGHT_GROUP_DESCRIBE,
 RIGHT_TYPE,
 RULE_ID,
 LAST_VER,
 RG_CODE,
 SET_YEAR)
 select new_right_group_id,
 RIGHT_GROUP_DESCRIBE,
 RIGHT_TYPE,
 ser_new_rule_id,
 LAST_VER,
 destRGCode,
 destSetYear
 from SYS_RIGHT_GROUP
 where rule_id = ser_old_rule_id
 and right_group_id = ser_right_group_id;
 
 
 insert into SYS_RIGHT_GROUP_DETAIL
 (RIGHT_GROUP_ID,
 ELE_CODE,
 ELE_VALUE,
 SET_YEAR,
 LAST_VER,
 ELE_NAME,
 RG_CODE)
 select new_right_group_id,
 ELE_CODE,
 ELE_VALUE,
 destSetYear,
 LAST_VER,
 ELE_NAME,
 destRGCode
 from sys_right_group_detail
 where right_group_id = ser_right_group_id and RG_CODE=srcRGCode and set_year=srcSetYear;
 
 
 insert into SYS_RIGHT_GROUP_TYPE
 (RIGHT_GROUP_ID,
 ELE_CODE,
 RIGHT_TYPE,
 SET_YEAR,
 LAST_VER,
 RG_CODE)
 select new_right_group_id,
 ELE_CODE,
 RIGHT_TYPE,
 destSetYear,
 LAST_VER,
 destRGCode
 from sys_right_group_type
 where right_group_id = ser_right_group_id and RG_CODE=srcRGCode and set_year=srcSetYear;
 
 fetch cur_srg
 into ser_right_group_id, ser_old_rule_id;
 end loop;
 close cur_srg;
 excuteSQL := ''drop table sys_rule_temp_tab'';
 EXECUTE IMMEDIATE excuteSQL;
 commit;
 end;
 execute immediate ''ALTER TABLE sys_right_group_detail ENABLE ALL TRIGGERS'';
 end;
 end rightGroupManage;';

execute immediate 'create or replace procedure RIGHT_DETAIL_NAME_PROC is
 type lcCursors is ref cursor;
 lsCursor lcCursors;--结果集游标
 lsSql            varchar2(2000);--sql
 v_ELE_SOURCE     varchar2(100);   --数据字典表
 v_ELE_CODE       VARCHAR2(20);
 upSql        varchar2(2000);
 begin
 lsSql := ''SELECT ELE_CODE, ELE_SOURCE FROM SYS_ELEMENT WHERE IS_RIGHTFILTER=1'';
 open lsCursor for lsSql;
 loop
 fetch lsCursor into v_ELE_CODE, v_ELE_SOURCE;
 if(lsCursor%notfound)then
 exit;
 else
 upSql := ''update sys_right_group_detail m set m.ele_name = (select e.chr_name from ''|| v_ELE_SOURCE|| '' e where e.chr_code = m.ele_value) WHERE M.ELE_CODE = ''''''|| v_ELE_CODE||'''''''';
 execute immediate upSql;
 end if;
 end loop;
 close lsCursor;
 end RIGHT_DETAIL_NAME_PROC;';

execute immediate 'CREATE OR REPLACE PROCEDURE ROLEMANAGE (srcRGCode   in varchar2,
 destRGCode  in varchar2,
 srcSetYear  in varchar2,
 destSetYear in varchar2) as
 begin
 DECLARE
 temp_sql_str VARCHAR2(2000);
 count1              NUMBER;
 begin
 execute immediate ''delete from SYS_ROLE where RG_CODE='''''' ||
 destRGCode || '''''''' || '' AND SET_YEAR= '' || destSetYear;
 execute immediate ''delete from SYS_ROLE_MENU where RG_CODE='''''' ||
 destRGCode || '''''''' || '' AND SET_YEAR= '' ||  destSetYear;
 execute immediate ''delete from SYS_ROLE_MENU_BUTTON where RG_CODE='''''' ||
 destRGCode || '''''''' || '' AND SET_YEAR= '' ||  destSetYear;
 execute immediate ''delete from SYS_ROLE_MENU_MODULE where RG_CODE='''''' ||
 destRGCode || '''''''' || '' AND SET_YEAR= '' || destSetYear;
 execute immediate ''delete from SYS_ROLE_MODULE where RG_CODE='''''' ||
 destRGCode || '''''''' || '' AND SET_YEAR= '' ||  destSetYear;
 SELECT COUNT(*) INTO count1 FROM user_tables WHERE table_name = ''SYS_ROLE_TEMP_TAB'';
 IF count1 > 0 THEN
 EXECUTE IMMEDIATE ''Drop table SYS_ROLE_TEMP_TAB'';
 END IF;
 EXECUTE IMMEDIATE ''create table sys_role_temp_tab(old_role_id number,new_role_id number)'';
 temp_sql_str := ''insert into sys_role_temp_tab(old_role_id,new_role_id)
 select role_id,SEQ_SYS_FRAME_ID.nextval
 from sys_role
 where RG_CODE ='''''' || srcrgcode || ''''''
 and set_year ='' || srcSetYear;
 EXECUTE IMMEDIATE temp_sql_str;
 commit;
 temp_sql_str := ''insert into sys_role(role_id,role_code,role_name,user_sys_id,enabled,role_type,last_ver,RG_CODE,set_year)
 select (select new_role_id from sys_role_temp_tab t where t.old_role_id=s.role_id ) ROLE_ID,role_code,role_name,user_sys_id,
 enabled,role_type,last_ver,''''''||destRGCode||'''''',''||destSetYear||'' from sys_role s where s.RG_CODE=''''''||srcRGCode||'''''' and s.set_year=''||srcSetYear;
 EXECUTE IMMEDIATE temp_sql_str;
 commit;
 delete from SYS_ROLE_MENU t where t.role_id not in (select r.role_id from sys_role r);
 temp_sql_str := ''insert into SYS_ROLE_MENU
 (ROLE_ID, MENU_ID, SET_YEAR, LAST_VER, RG_CODE)
 select decode((select new_role_id from sys_role_temp_tab t where t.old_role_id=m.role_id ),null,0,(select new_role_id from sys_role_temp_tab t where t.old_role_id=m.role_id )) ROLE_ID, MENU_ID, ''||destSetYear||'', LAST_VER, ''''''||destRGCode ||''''''
 from SYS_ROLE_MENU m
 where RG_CODE = ''''''||srcRGCode||''''''
 AND SET_YEAR = ''|| srcSetYear;
 EXECUTE IMMEDIATE temp_sql_str;
 temp_sql_str := ''delete from SYS_ROLE_MENU where role_id=0'';
 EXECUTE IMMEDIATE temp_sql_str;
 commit;
 temp_sql_str := ''insert into SYS_ROLE_MENU_BUTTON
 (ROLE_ID, BUTTON_ID, SET_YEAR, MENU_ID, LAST_VER, RG_CODE)
 select decode((select new_role_id from sys_role_temp_tab t where t.old_role_id=FININTORG.role_id ),null,0,(select new_role_id from sys_role_temp_tab t where t.old_role_id=FININTORG.role_id )) ROLE_ID, BUTTON_ID, ''||destSetYear ||'', MENU_ID, LAST_VER, ''''''||destRGCode ||''''''
 from SYS_ROLE_MENU_BUTTON FININTORG
 where RG_CODE = ''''''||srcRGCode ||''''''
 AND SET_YEAR = ''||srcSetYear;
 EXECUTE IMMEDIATE temp_sql_str;
 temp_sql_str := ''delete from SYS_ROLE_MENU_BUTTON where role_id=0'';
 EXECUTE IMMEDIATE temp_sql_str;
 commit;
 delete from SYS_ROLE_MENU_MODULE t where t.role_id not in (select r.role_id from sys_role r);
 temp_sql_str := ''insert into SYS_ROLE_MENU_MODULE
 (ROLE_ID, MENU_ID, MODULE_ID, SET_YEAR, LAST_VER, RG_CODE)
 select decode((select new_role_id from sys_role_temp_tab t where t.old_role_id=mm.role_id ),null,0,(select new_role_id from sys_role_temp_tab t where t.old_role_id=mm.role_id )) ROLE_ID, MENU_ID, MODULE_ID, ''||destSetYear ||'', LAST_VER, ''''''||destRGCode ||''''''
 from SYS_ROLE_MENU_MODULE mm
 where RG_CODE = ''''''||srcRGCode ||''''''
 AND SET_YEAR = ''||srcSetYear;
 EXECUTE IMMEDIATE temp_sql_str;
 temp_sql_str := ''delete from SYS_ROLE_MENU_MODULE where role_id=0'';
 EXECUTE IMMEDIATE temp_sql_str;
 commit;
 temp_sql_str := ''insert into SYS_ROLE_MODULE
 (ROLE_ID, MODULE_ID, SET_YEAR, LAST_VER, RG_CODE)
 select decode((select new_role_id from sys_role_temp_tab t where t.old_role_id=rm.role_id ),null,0,(select new_role_id from sys_role_temp_tab t where t.old_role_id=rm.role_id )) ROLE_ID, MODULE_ID, ''||destSetYear ||'', LAST_VER, ''''''||destRGCode ||''''''
 from SYS_ROLE_MODULE rm
 where RG_CODE = ''''''||srcRGCode ||''''''
 AND SET_YEAR = ''||srcSetYear;
 EXECUTE IMMEDIATE temp_sql_str;
 temp_sql_str := ''delete from SYS_ROLE_MODULE where role_id=0'';
 EXECUTE IMMEDIATE temp_sql_str;
 commit;
 temp_sql_str := ''drop table sys_role_temp_tab'';
 EXECUTE IMMEDIATE temp_sql_str;
 end;
 end roleManage;';

execute immediate 'CREATE OR REPLACE PROCEDURE sys_ele_rule_detail_copy(srcrgcode   IN VARCHAR2,
 destrgcode  IN VARCHAR2,
 srcsetyear  IN VARCHAR2,
 destsetyear IN VARCHAR2) AS
 new_ele_value   VARCHAR2(50);
 new_ele_rule_id VARCHAR2(50);
 new_rule_id     VARCHAR2(50);
 FUNCTION get_ele_rule_id(old_ele_rule_id IN VARCHAR2) RETURN VARCHAR2 IS
 retvalue VARCHAR2(50);
 BEGIN
 SELECT nvl(ele_rule_id, '''')
 INTO retvalue
 FROM sys_ele_rule t
 WHERE RG_CODE = srcrgcode
 AND t.ele_rule_code =
 (SELECT ele_rule_code FROM sys_ele_rule WHERE ele_rule_id = old_ele_rule_id)
 AND t.set_year = destsetyear;
 RETURN retvalue;
 END;
 FUNCTION get_rule_id(old_rule_id IN VARCHAR2) RETURN VARCHAR2 IS
 retvalue VARCHAR2(50);
 BEGIN
 Dbms_Output.put_line(old_rule_id);
 SELECT nvl(rule_id, '''')
 INTO retvalue
 FROM sys_rule t
 WHERE RG_CODE = srcrgcode
 AND t.rule_code IN (SELECT rule_code FROM sys_rule WHERE rule_id = old_rule_id)
 AND t.set_year = destsetyear;
 RETURN retvalue;
 END;
 FUNCTION get_ele_value(old_ele_rule_id IN VARCHAR2,
 old_ele_value   IN VARCHAR2) RETURN VARCHAR2 IS
 retinfo        VARCHAR2(50);
 ele_table_name VARCHAR2(50);
 sqlstr         VARCHAR2(1500);
 BEGIN
 SELECT ele_source
 INTO ele_table_name
 FROM sys_element t
 WHERE t.RG_CODE = srcrgcode
 AND t.ele_code = (SELECT ele_code FROM sys_ele_rule WHERE ele_rule_id = old_ele_rule_id)
 AND t.set_year = srcsetyear;
 IF ELE_TABLE_NAME = ''VW_CLEARBANKACCOUNT'' OR ELE_TABLE_NAME = ''VW_PAYMENTACCOUNT'' THEN
 sqlstr := ''select chr_id  from  '' || ele_table_name ||
 '' where (chr_code,set_year,RG_CODE,account_type) = (select chr_code,'' || destsetyear ||
 '',RG_CODE ,account_type from '' || ele_table_name || '' where chr_id='''''' || old_ele_value || '''''')'';
 ELSE
 sqlstr := ''select chr_id  from  '' || ele_table_name ||
 '' where (chr_code,set_year,RG_CODE) = (select chr_code,'' || destsetyear ||
 '',RG_CODE from '' || ele_table_name || '' where chr_id='''''' || old_ele_value || '''''')'';
 END IF;
 EXECUTE IMMEDIATE sqlstr INTO retinfo;
 RETURN retinfo;
 EXCEPTION
 WHEN OTHERS THEN
 BEGIN
 RETURN '''';
 END;
 END;
 BEGIN
 IF (srcrgcode = destrgcode AND srcsetyear <> destsetyear) THEN
 DELETE FROM sys_ele_rule_detail WHERE RG_CODE = destrgcode AND set_year = destsetyear;
 INSERT INTO sys_ele_rule_detail
 (ele_rule_detail_id, ele_value, ele_rule_id, rule_id, create_date, create_user, latest_op_date, latest_op_user, last_ver, set_year, ele_code, ele_name, RG_CODE)
 SELECT newid, ele_value, ele_rule_id, rule_id, to_char(SYSDATE, ''yyyy-MM-dd hh24:mi:ss''), create_user, to_char(SYSDATE, ''yyyy-MM-dd hh24:mi:ss''), latest_op_user, to_char(SYSDATE, ''yyyy-MM-dd hh24:mi:ss''), destsetyear, ele_code, ele_name, RG_CODE
 FROM sys_ele_rule_detail t
 WHERE t.RG_CODE = srcrgcode
 AND t.set_year = srcsetyear;
 FOR var IN (SELECT ele_rule_detail_id, ele_value, ele_rule_id, rule_id
 FROM sys_ele_rule_detail t
 WHERE t.set_year = destsetyear
 AND t.RG_CODE = srcrgcode) LOOP
 new_ele_value   := get_ele_value(var.ele_rule_id, var.ele_value);
 new_ele_rule_id := get_ele_rule_id(var.ele_rule_id);
 new_rule_id     := get_rule_id(var.rule_id);
 IF new_ele_value IS NOT NULL AND new_ele_rule_id IS NOT NULL AND new_rule_id IS NOT NULL THEN
 UPDATE sys_ele_rule_detail t
 SET t.ele_value = new_ele_value, t.ele_rule_id = new_ele_rule_id, t.rule_id = new_rule_id
 WHERE t.ele_rule_detail_id = var.ele_rule_detail_id;
 ELSE DELETE sys_ele_rule_detail T WHERE t.ele_rule_detail_id = var.ele_rule_detail_id;
 END IF;
 END LOOP;
 COMMIT;
 END IF;
 END sys_ele_rule_detail_copy;';

execute immediate 'CREATE OR REPLACE PROCEDURE RULEMANAGE (srcrgcode   IN VARCHAR2,
 destrgcode  IN VARCHAR2,
 srcsetyear  IN VARCHAR2,
 destsetyear IN VARCHAR2) AS
 BEGIN
 DECLARE
 create_temp_sql_str VARCHAR2(2000);
 count1              NUMBER;
 BEGIN
 EXECUTE IMMEDIATE ''delete from sys_ele_rule where RG_CODE='''''' || destrgcode || '''''''' || '' AND SET_YEAR= '' || destsetyear;
 INSERT INTO sys_ele_rule
 (ele_rule_id, ele_rule_code, ele_rule_name, ele_code, create_date, create_user, latest_op_date, latest_op_user, last_ver, set_year, RG_CODE)
 SELECT newid, ele_rule_code, ele_rule_name, ele_code, to_char(SYSDATE, ''yyyy-MM-dd hh24:mi:ss''), create_user, to_char(SYSDATE, ''yyyy-MM-dd hh24:mi:ss''), latest_op_user, to_char(SYSDATE, ''yyyy-MM-dd hh24:mi:ss''), destsetyear, destrgcode
 FROM sys_ele_rule
 WHERE RG_CODE = srcrgcode
 AND set_year = srcsetyear;
 EXECUTE IMMEDIATE ''delete from sys_rule where RG_CODE='''''' || destrgcode || '''''''' || '' AND SET_YEAR= '' || destsetyear;
 SELECT COUNT(*) INTO count1 FROM user_tables WHERE table_name = ''SYS_RULE_TEMP_TAB'';
 IF count1 > 0 THEN
 create_temp_sql_str := ''Drop table SYS_RULE_TEMP_TAB'';
 EXECUTE IMMEDIATE create_temp_sql_str;
 END IF;
 create_temp_sql_str := ''create table sys_rule_temp_tab(old_rule_id number,new_rule_id number)'';
 EXECUTE IMMEDIATE create_temp_sql_str;
 create_temp_sql_str := ''insert into sys_rule_temp_tab(old_rule_id,new_rule_id)
 select rule_id,seq_other_id.nextval
 from sys_rule
 where RG_CODE ='''''' || srcrgcode || ''''''
 and set_year ='' || srcsetyear;
 EXECUTE IMMEDIATE create_temp_sql_str;
 create_temp_sql_str := ''insert into SYS_RULE
 (RULE_ID,
 RULE_CODE,
 RULE_NAME,
 REMARK,
 SET_YEAR,
 ENABLED,
 RULE_CLASSIFY,
 SYS_REMARK,
 RIGHT_TYPE,
 CREATE_USER,
 CREATE_DATE,
 LATEST_OP_USER,
 LATEST_OP_DATE,
 LAST_VER,
 RG_CODE)
 select (select new_rule_id from sys_rule_temp_tab srtb where srtb.old_rule_id=sr.rule_id ) rule_id,
 RULE_CODE,
 RULE_NAME,
 REMARK,
 '' || destsetyear || '',
 ENABLED,
 RULE_CLASSIFY,
 SYS_REMARK,
 RIGHT_TYPE,
 CREATE_USER,
 to_char(sysdate, ''''yyyy-MM-dd hh24:mi:ss''''),
 LATEST_OP_USER,
 to_char(sysdate, ''''yyyy-MM-dd hh24:mi:ss''''),
 to_char(sysdate, ''''yyyy-MM-dd hh24:mi:ss''''),
 '''''' || destrgcode || ''''''
 from SYS_RULE sr
 where RG_CODE ='''''' || srcrgcode || ''''''
 and set_year ='' || srcsetyear;
 EXECUTE IMMEDIATE create_temp_sql_str;
 sys_ele_rule_detail_copy(srcrgcode, destrgcode, srcsetyear, destsetyear);--新年度初始化，合库时执行
--copy_sys_rulecrossjoin(srcrgcode, destrgcode, srcsetyear, destsetyear);--新年度初始化，合库时执行
 END;
 COMMIT;
 END rulemanage;';

execute immediate 'create or replace procedure setTopRgCode(setRgCode in varchar2)
 as
 begin
 for c in (select distinct ''update '' || table_name ||
 '' set RG_CODE = '''''' || setRgCode ||'''''''' AS v_sql
 from user_tab_columns t
 where column_name = ''RG_CODE'' and
 exists (select 1 from user_tables u where u.TABLE_NAME = t.TABLE_NAME)) loop
 begin
 execute immediate c.v_sql;
 exception
 when others then
 dbms_output.put_line(sqlerrm);
 end;
 end loop;
 update sys_userpara set chr_value = setRgCode where chr_code = ''App_Curr_Region'';
 update sys_user_region set RG_CODE= setRgCode;
 update ele_region set is_top=1,is_valid=1 where chr_code= setRgCode;
 update ele_region set is_valid=0,is_top=0 where chr_code=''000000'';
 commit;
 end;';

execute immediate 'create or replace procedure SYS_CHECK_CHRID_PRO is
 v_sql_code  varchar2(2000);
 v_sql_type  varchar2(200);
 v_num number := 0;
 type t_cur is ref cursor;
 v_cur t_cur;
 type eleRecord is record(chr_code varchar2(42),chr_code1 varchar2(42),chr_code2 varchar2(42),chr_code3 varchar2(42),chr_code4 varchar2(42),chr_code5 varchar2(42),chr_id varchar2(38),chr_id1 varchar2(38),chr_id2 varchar2(38),chr_id3 varchar2(38),chr_id4 varchar2(38),chr_id5 varchar2(38),
 parent_id varchar2(38),chr_name varchar2(200),level_num number(2));
 tab2 eleRecord;
 begin
 select count(*) into v_num from user_tables where table_name=upper(''SYS_CHECK_CHRID'');
 if v_num > 0 then
 execute  immediate  ''TRUNCATE TABLE SYS_CHECK_CHRID '';
 commit;
 end if;
 for tab in (select ele_source,ele_code,ele_name from sys_element where ele_source not like ''VW_%'' and ele_source not like ''vw_%''  and ele_source not in(''SAL_DEPARTMENT'',''VM_ACC_BANK'',''ELE_CLEAR_INTERFACE_DATE'')) loop
 begin
 open v_cur for ''select chr_code,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5,chr_id,chr_id1,chr_id2,chr_id3,chr_id4,chr_id5, parent_id,chr_name,level_num from ''||tab.ele_source||'' where enabled =1 '';
 loop
 fetch v_cur into tab2;
 exit when v_cur%notfound;
 v_num := ele_is_valid(tab.ele_code,tab2.chr_id);
 if(v_num = 0) then
 v_sql_type := ''要素在基础要素中不存在'';
 end if;
 if(v_num = 1) then
 v_sql_type := ''未找到父节点'';
 end if;
 if(v_num = 2) then
 v_sql_type := ''父级CHR_ID 为空'';
 end if;
 if(v_num = 3) then
 v_sql_type := ''父级CHR_CODE 为空'';
 end if;
 if(v_num >= 0) then
 v_sql_code  := ''insert into SYS_CHECK_CHRID(ele_source,ele_code, ele_name,chr_code,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5, chr_id ,chr_id1 ,chr_id2 ,chr_id3 ,chr_id4,chr_id5, chr_name,parent_id,level_num,error_type)
 values (''''''||tab.ele_source||'''''',''''''||tab.ele_code||'''''',''''''||tab.ele_name||'''''',''''''||tab2.chr_code||'''''',''''''||tab2.chr_code1||'''''',''''''||tab2.chr_code2||'''''',''''''||tab2.chr_code3||'''''',''''''||tab2.chr_code4||'''''',''''''||tab2.chr_code5||'''''',''''''||tab2.chr_id||'''''',''''''||tab2.chr_id1||'''''',''''''||tab2.chr_id2||'''''',
 ''''''||tab2.chr_id3||'''''',''''''||tab2.chr_id4||'''''',''''''||tab2.chr_id5||'''''',''''''||tab2.chr_name||'''''',''''''||tab2.parent_id||'''''',''''''||tab2.level_num||'''''',''''''||v_sql_type||'''''')'';
 execute immediate v_sql_code;
 end if;
 commit;
 end loop;
 exception when others then
 insert into SYS_CHECK_CHRID(ele_source,ele_code,ele_name,error_type) values(tab.ele_source,tab.ele_code,tab.ele_name,''表或视图不存在'');
 commit;
 end;
 end loop;
 end SYS_CHECK_CHRID_PRO;';

execute immediate 'create or replace procedure SYS_CHECK_CODE_NAME_PRO is
 v_sql_code  varchar2(2000);
 v_sql_name  varchar2(2000);
 v_num number := 0;
 begin
 select count(*) into v_num from user_tables where table_name=upper(''SYS_CHECK_CODE_NAME'');
 if v_num > 0 then
 execute  immediate  ''TRUNCATE TABLE SYS_CHECK_CODE_NAME '';
 commit;
 else
 execute  immediate ''create table SYS_CHECK_CODE_NAME(ele_source VARCHAR2(64), ele_code VARCHAR2(64), ele_name VARCHAR2(64),chr_code VARCHAR2(64),chr_name VARCHAR2(64),repeat_feild VARCHAR2(64))'';
 end if;
 for tab in (select ele_source,ele_code,ele_name from sys_element) loop
 begin
 v_sql_code  := ''insert into SYS_CHECK_CODE_NAME(ele_source,ele_code,ele_name,chr_code,chr_name,repeat_feild) '' ||
 '' select distinct ''''''|| tab.ele_source ||'''''',''''''|| tab.ele_code||'''''',''''''||tab.ele_name||'''''',t.chr_code,'''''''',''''编码重复'''' from '' || tab.ele_source ||
 '' t where chr_code in (select chr_code from  '' || tab.ele_source ||
 '' group by chr_code,set_year having count(chr_code)>1) '';
 execute immediate v_sql_code;
 exception when others then
 insert into SYS_CHECK_CODE_NAME(ele_source,ele_code,ele_name,repeat_feild) values(tab.ele_source,tab.ele_code,tab.ele_name,''表或视图不存在'');
 commit;
 end;
 begin
 v_sql_name  := ''insert into SYS_CHECK_CODE_NAME(ele_source,ele_code,ele_name,chr_code,chr_name,repeat_feild) '' ||
 '' select distinct ''''''|| tab.ele_source ||'''''',''''''|| tab.ele_code||'''''',''''''||tab.ele_name||'''''','''''''',t.chr_name, ''''名称重复'''' from '' || tab.ele_source ||
 '' t where chr_name in (select chr_name from  '' || tab.ele_source ||
 '' group by chr_name,set_year having count(chr_name)>1) '';
 execute immediate v_sql_name;
 exception when others then
 commit;
 end;
 end loop;
 commit;
 exception when others then
 commit;
 end SYS_CHECK_CODE_NAME_PRO;';

execute immediate 'create or replace procedure SYS_CHECK_RELATIONS_PRO is
 v_sql1 varchar2(1000);
 v_sql2 varchar2(1000);
 v_num number := 0;
 begin
 select count(*) into v_num from user_tables where table_name=upper(''SYS_CHECK_RELATIONS'');
 if v_num > 0 then
 execute  immediate  ''TRUNCATE TABLE SYS_CHECK_RELATIONS'';
 commit;
 end if;
 for tab in (select a.relation_id, a.relation_code, b.ele_code, b.ele_name, b.ele_source,1 as is_pri
 from SYS_RELATION_MANAGER a, sys_element b
 where a.pri_ele_code = b.ele_code
 union all
 select a.relation_id, a.relation_code, b.ele_code, b.ele_name, b.ele_source,0 as is_pri
 from SYS_RELATION_MANAGER a, sys_element b
 where a.sec_ele_code = b.ele_code) loop
 if(tab.is_pri = 1) then
 v_sql1 := ''insert into SYS_CHECK_RELATIONS(relation_code ,ele_code ,ele_name ,ele_source ,pri_ele_value,invalid_feild ) '' ||
 '' select distinct ''''''||tab.relation_code ||'''''' as relation_code,''''''||tab.ele_code ||'''''' as ele_code,''''''||tab.ele_name ||'''''' as ele_name ,''''''||tab.ele_source||'''''' as ele_source, a.pri_ele_value,''''主控要素在基础要素中不存在'''' as invalid_feild from SYS_RELATIONS a '' ||
 '' where not exists(select 1 from '' || tab.ele_source || '' b where a.pri_ele_value=b.chr_code)and a.relation_id='''''' ||tab.relation_id ||'''''''';
 execute immediate v_sql1;
 else
 v_sql2 := ''insert into SYS_CHECK_RELATIONS(relation_code,ele_code ,ele_name ,ele_source ,sec_ele_value ,invalid_feild ) '' ||
 '' select distinct ''''''||tab.relation_code ||'''''' as relation_code,''''''||tab.ele_code ||'''''' as ele_code,''''''||tab.ele_name ||'''''' as ele_name ,''''''||tab.ele_source||'''''' as ele_source, a.sec_ele_value,''''被控要素在基础要素中不存在'''' as invalid_feild from SYS_RELATIONS a '' ||
 '' where not exists(select 1 from '' || tab.ele_source || '' b where a.sec_ele_value=b.chr_code)and a.relation_id='''''' ||tab.relation_id ||'''''''';
 execute immediate v_sql2;
 end if;
 end loop;
 commit;
 end SYS_CHECK_RELATIONS_PRO;';

execute immediate 'create or replace procedure SYS_CHECK_RULE_PRO is
 v_sql1 varchar2(1000);
 v_num number := 0;
 begin
 select count(*) into v_num from user_tables where table_name=upper(''SYS_CHECK_RULE'');
 if v_num > 0 then
 execute  immediate  ''TRUNCATE TABLE SYS_CHECK_RULE'';
 end if;
 for tab in (select distinct a.ele_rule_code,a.ele_rule_name,a.ele_rule_id,b.ele_code,b.ele_name,b.ele_source from sys_ele_rule a,sys_element b where a.ele_code=b.ele_code) loop
 v_sql1 := ''insert into SYS_CHECK_RULE(ele_rule_code,ele_rule_name,ele_code,ele_name,ele_source,ele_value)'' ||
 '' select distinct '''''' || tab.ele_rule_code  || '''''','''''' || tab.ele_rule_name  || '''''','''''' || tab.ele_code  || '''''','''''' || tab.ele_name || '''''','''''' || tab.ele_source || '''''',a.ele_code as ele_value from sys_ele_rule_detail a where not exists(select 1 from '' ||
 tab.ele_source || '' b where a.ele_code=b.chr_code and a.ele_value = b.chr_id) and a.ele_rule_id= '''''' || tab.ele_rule_id || '''''''';
 execute immediate v_sql1;
 end loop;
 commit;
 end SYS_CHECK_RULE_PRO;';
execute immediate 'CREATE OR REPLACE PROCEDURE userparaManage(srcrgcode   IN VARCHAR2,
 destrgcode  IN VARCHAR2,
 srcsetyear  IN VARCHAR2,
 destsetyear IN VARCHAR2) AS
 BEGIN
 DELETE FROM sys_userpara WHERE set_year = destsetyear AND RG_CODE = destrgcode;
 INSERT INTO sys_userpara
 (select
 newid chr_id,
 chr_code,
 chr_name,
 chr_value,
 chr_desc,
 sys_id,
 is_visible,
 is_edit,
 field_valueset,
 field_disptype,
 group_name,
 set_id,
 last_ver,
 destrgcode  RG_CODE,
 destsetyear  set_year
 from sys_userpara
 WHERE set_year = srcsetyear
 AND RG_CODE = srcrgcode);
 END userparaManage;';
execute immediate 'CREATE OR REPLACE PROCEDURE viewmanage(srcrgcode   IN VARCHAR2,
 destrgcode  IN VARCHAR2,
 srcsetyear  IN VARCHAR2,
 destsetyear IN VARCHAR2) AS
 BEGIN
 DECLARE
 count1  NUMBER;
 strtemp VARCHAR2(200);
 BEGIN
 SELECT COUNT(*)
 INTO count1
 FROM user_tables
 WHERE table_name = ''SYS_UIMANAGE_PROCEDURE_TEMP'';
 IF count1 > 0 THEN
 EXECUTE IMMEDIATE ''drop table sys_uimanage_procedure_temp'';
 END IF;
 SELECT COUNT(*) INTO count1 FROM user_tables WHERE table_name = ''SYS_UICONF_DETAIL_TEMP'';
 IF count1 > 0 THEN
 EXECUTE IMMEDIATE ''drop table SYS_UICONF_DETAIL_TEMP'';
 END IF;
 EXECUTE IMMEDIATE ''delete from sys_uimanager where RG_CODE='''''' || destrgcode || '''''''' ||
 '' AND SET_YEAR='' || destsetyear;
 EXECUTE IMMEDIATE ''delete from SYS_UIDETAIL where RG_CODE='''''' || destrgcode || '''''''' ||
 '' AND SET_YEAR='' || destsetyear;
 EXECUTE IMMEDIATE ''delete from sys_module_view where RG_CODE='''''' || destrgcode || '''''''' ||
 '' AND SET_YEAR='' || destsetyear;
 EXECUTE IMMEDIATE ''delete from SYS_ACTION_VIEW where RG_CODE='''''' || destrgcode || '''''''' ||
 '' AND SET_YEAR='' || destsetyear;
 COMMIT;
 EXECUTE IMMEDIATE ''create table sys_uimanage_procedure_temp (oldUUID   VARCHAR2(40) not null,newUUID VARCHAR2(40) not null)'';
 EXECUTE IMMEDIATE ''create table SYS_UICONF_DETAIL_TEMP(oldUUID   VARCHAR2(40) not null,newUUID VARCHAR2(40) not null)'';
 EXECUTE IMMEDIATE ''alter table SYS_UIMANAGER modify UI_ID VARCHAR2(40)'';
 EXECUTE IMMEDIATE ''alter table SYS_UIDETAIL modify UI_ID VARCHAR2(40)'';
 EXECUTE IMMEDIATE ''alter table SYS_UICONF_DETAIL modify UICONF_ID VARCHAR2(40)'';
 EXECUTE IMMEDIATE ''alter table SYS_UIDETAIL modify UI_DETAIL_ID VARCHAR2(40)'';
 EXECUTE IMMEDIATE ''alter table SYS_UIDETAIL modify PARENT_ID VARCHAR2(40)'';
 EXECUTE IMMEDIATE ''alter table SYS_MODULE_VIEW modify UI_ID VARCHAR2(40)'';
 EXECUTE IMMEDIATE ''alter table SYS_ACTION_VIEW modify UI_ID VARCHAR2(40)'';
 EXECUTE IMMEDIATE '' INSERT INTO sys_uimanage_procedure_temp VALUE(oldUUID, newUUID) select ui_id, NEWID from SYS_UIMANAGER where RG_CODE ='''''' ||
 srcrgcode || '''''''' || '' AND SET_YEAR='' || srcsetyear;
 strtemp := '' INSERT INTO SYS_UICONF_DETAIL_TEMP VALUE(oldUUID, newUUID) select DISTINCT ui_detail_id ||''''+'''', NEWID from sys_uidetail where RG_CODE ='''''' ||
 srcrgcode || '''''''' || '' AND SET_YEAR='' || srcsetyear;
 EXECUTE IMMEDIATE strtemp;
 COMMIT;
 INSERT INTO sys_uimanager VALUE
 (ui_id, ui_code, ui_name, ui_type, ui_source, remark, create_user, create_date, latest_op_user, latest_op_date, columns, id, title, last_ver, query_relation_sign, sys_id, RG_CODE,set_year)
 SELECT ui_id || ''+'', ui_code, ui_name, ui_type, ui_source, remark, create_user, create_date, latest_op_user, latest_op_date, columns, id, title, last_ver, query_relation_sign, sys_id, destrgcode,destsetyear
 FROM sys_uimanager
 WHERE RG_CODE = srcrgcode
 and set_year=srcsetyear;
 INSERT INTO sys_uidetail VALUE
 (ui_detail_id, ui_id, field_name, disp_mode, is_nessary, is_enabled, field_index, id, title, cols, visible, editable, VALUE, ref_model, SOURCE, width, is_must_input, last_ver, enabled, query_relation_sign, detail_type, parent_id, virtual_column_sql, is_virtual_column, RG_CODE,set_year)
 SELECT ui_detail_id || ''+'', ui_id || ''+'', field_name, disp_mode, is_nessary, is_enabled, field_index, id, title, cols, visible, editable, VALUE, ref_model, SOURCE, width, is_must_input, last_ver, enabled, query_relation_sign, detail_type,decode(parent_id,null,parent_id,parent_id||''+''), virtual_column_sql, is_virtual_column, destrgcode,destsetyear
 FROM sys_uidetail
 WHERE RG_CODE = srcrgcode
 and set_year=srcsetyear;
 INSERT INTO sys_uiconf_detail VALUE
 (uiconf_id, uiconf_field, uiconf_value, last_ver)
 SELECT uiconf_id || ''+'', uiconf_field, uiconf_value, last_ver
 FROM sys_uiconf_detail
 WHERE uiconf_id IN (SELECT ui_detail_id
 FROM sys_uidetail
 WHERE RG_CODE = srcrgcode
 and set_year=srcsetyear
 UNION ALL
 SELECT ui_id FROM sys_uidetail WHERE RG_CODE = srcrgcode and set_year=srcsetyear);
 INSERT INTO sys_module_view VALUE
 (module_id, ui_id, disp_order, remark, last_ver, key_value, RG_CODE,set_year)
 SELECT module_id, ui_id || ''+'', disp_order, remark, last_ver, key_value, destrgcode,destsetyear
 FROM sys_module_view
 WHERE RG_CODE = srcrgcode
 and set_year=srcsetyear;
 INSERT INTO sys_action_view VALUE
 (action_id, ui_id, disp_order, set_year, remark, last_ver, key_value, RG_CODE)
 SELECT action_id, ui_id || ''+'', disp_order, destsetyear, remark, last_ver, key_value, destrgcode
 FROM sys_action_view
 WHERE RG_CODE = srcrgcode
 AND set_year = srcsetyear;
 COMMIT;
 DECLARE
 CURSOR cursor_uimanage IS
 SELECT ui_id FROM sys_uimanager WHERE RG_CODE = destrgcode and set_year = destsetyear;
 varoldui_id  VARCHAR2(2000); -- 老的UI_ID,这个UI_ID是经过处理含有"+"的,
 varnewtempid VARCHAR2(2000); -- 新增UI_ID
 tempsql      VARCHAR2(2000);
 BEGIN
 OPEN cursor_uimanage;
 FETCH cursor_uimanage
 INTO varoldui_id;
 WHILE cursor_uimanage%FOUND LOOP
 EXECUTE IMMEDIATE ''select newUUID from sys_uimanage_procedure_temp where oldUUID='''''' || REPLACE(varoldui_id, ''+'', '''') || ''''''''
 INTO varnewtempid;
 UPDATE sys_uimanager
 SET ui_id = varnewtempid
 WHERE ui_id = varoldui_id
 AND RG_CODE = destrgcode and set_year = destsetyear;
 UPDATE sys_uidetail
 SET ui_id = varnewtempid
 WHERE ui_id = varoldui_id
 AND RG_CODE = destrgcode and set_year = destsetyear;
 UPDATE sys_uiconf_detail SET uiconf_id = varnewtempid WHERE uiconf_id = varoldui_id;
 UPDATE sys_module_view
 SET ui_id = varnewtempid
 WHERE ui_id = varoldui_id
 AND RG_CODE = destrgcode and set_year = destsetyear;
 UPDATE sys_action_view
 SET ui_id = varnewtempid
 WHERE ui_id = varoldui_id
 AND RG_CODE = destrgcode
 AND set_year = destsetyear;
 COMMIT;
 FETCH cursor_uimanage
 INTO varoldui_id;
 END LOOP;
 CLOSE cursor_uimanage;
 tempsql := ''update sys_uiconf_detail sud set sud.uiconf_id=(select sudt.newuuid from  SYS_UICONF_DETAIL_TEMP sudt where  sudt.oldUUID=SUD.UICONF_ID )where sud.uiconf_id in(select sudt.oldUUID from SYS_UICONF_DETAIL_TEMP sudt)'';
 EXECUTE IMMEDIATE tempsql;
 tempsql := ''update sys_uidetail su  set su.ui_detail_id =(select sudt.newuuid from  SYS_UICONF_DETAIL_TEMP sudt where  sudt.oldUUID=SU.ui_detail_id ) where su.ui_detail_id like ''''%+'''''';
 EXECUTE IMMEDIATE tempsql;
 tempsql := ''update sys_uidetail su  set su.parent_id =(select sudt.newuuid from  SYS_UICONF_DETAIL_TEMP sudt where  sudt.oldUUID=SU.parent_id ) where su.parent_id like ''''%+'''''';
 EXECUTE IMMEDIATE tempsql;
 EXECUTE IMMEDIATE ''delete from sys_uidetail where ui_id like ''''%+'''''';
 EXECUTE IMMEDIATE ''delete from sys_uidetail where parent_id like ''''%+'''''';
 EXECUTE IMMEDIATE ''delete from SYS_UICONF_DETAIL where uiconf_id like ''''%+'''''';
 EXECUTE IMMEDIATE ''drop table sys_uimanage_procedure_temp'';
 EXECUTE IMMEDIATE ''drop table SYS_UICONF_DETAIL_TEMP'';
 EXECUTE IMMEDIATE ''alter table SYS_UICONF_DETAIL modify UICONF_ID VARCHAR2(38)'';
 EXECUTE IMMEDIATE ''alter table SYS_UIMANAGER modify UI_ID VARCHAR2(38)'';
 EXECUTE IMMEDIATE ''alter table SYS_UIDETAIL modify UI_ID VARCHAR2(38)'';
 EXECUTE IMMEDIATE ''alter table SYS_UIDETAIL modify UI_DETAIL_ID VARCHAR2(38)'';
 EXECUTE IMMEDIATE ''alter table SYS_UIDETAIL modify PARENT_ID VARCHAR2(38)'';
 EXECUTE IMMEDIATE ''delete from SYS_MODULE_VIEW where UI_ID like ''''%+'''''';
 EXECUTE IMMEDIATE ''alter table SYS_MODULE_VIEW modify UI_ID VARCHAR2(38)'';
 EXECUTE IMMEDIATE ''delete from SYS_ACTION_VIEW where UI_ID like ''''%+'''''';
 EXECUTE IMMEDIATE ''alter table SYS_ACTION_VIEW modify UI_ID VARCHAR2(38)'';
 COMMIT;
 EXCEPTION
 WHEN OTHERS THEN
 EXECUTE IMMEDIATE ''drop table sys_uimanage_procedure_temp'';
 EXECUTE IMMEDIATE ''delete from SYS_UIMANAGER where RG_CODE='''''' || destrgcode || '''''''' ||
 '' AND SET_YEAR='' || destsetyear;
 EXECUTE IMMEDIATE ''delete from SYS_UIDETAIL where RG_CODE='''''' || destrgcode || '''''''' ||
 '' AND SET_YEAR='' || destsetyear;
 EXECUTE IMMEDIATE ''delete from SYS_MODULE_VIEW where RG_CODE='''''' || destrgcode || '''''''' ||
 '' AND SET_YEAR='' || destsetyear;
 EXECUTE IMMEDIATE ''alter table SYS_UIMANAGER modify UI_ID VARCHAR2(38)'';
 EXECUTE IMMEDIATE ''alter table SYS_MODULE_VIEW modify UI_ID VARCHAR2(38)'';
 EXECUTE IMMEDIATE ''alter table SYS_ACTION_VIEW modify UI_ID VARCHAR2(38)'';
 END;
 DECLARE
 CURSOR cursor_uidetail IS
 SELECT VALUE, upper(SOURCE)
 FROM sys_uidetail
 WHERE RG_CODE = destrgcode
 AND length(VALUE) > 30; --只处理value值为uuid的情况
 strvalue     VARCHAR2(200);
 strsource    VARCHAR2(200);
 strtablename VARCHAR2(200);
 strchr_code  VARCHAR2(200);
 strnewvalue  VARCHAR2(200);
 icount       NUMBER;
 BEGIN
 OPEN cursor_uidetail;
 FETCH cursor_uidetail
 INTO strvalue, strsource;
 WHILE cursor_uidetail%FOUND LOOP
 DECLARE
 BEGIN
 SELECT COUNT(*)
 INTO icount
 FROM sys_element
 WHERE ele_code = strsource
 AND RG_CODE = destrgcode;
 IF icount > 0 THEN
 EXECUTE IMMEDIATE ''SELECT ELE_SOURCE FROM  SYS_ELEMENT WHERE ELE_CODE='''''' || strsource || '''''' AND  RG_CODE='''''' || destrgcode || '''''' and set_year='''''' || destsetyear || ''''''''
 INTO strtablename;
 EXECUTE IMMEDIATE ''SELECT CHR_CODE FROM '' || strtablename || '' WHERE CHR_ID='''''' || strvalue || ''''''''
 INTO strchr_code;
 EXECUTE IMMEDIATE ''SELECT CHR_ID FROM '' || strtablename || '' WHERE CHR_CODE='''''' || strchr_code || '''''''' || '' AND RG_CODE='''''' || destrgcode || '''''' and set_year='''''' || destsetyear || ''''''''
 INTO strnewvalue;
 UPDATE sys_uidetail
 SET VALUE = strnewvalue
 WHERE VALUE = strvalue
 AND RG_CODE = destrgcode
 AND set_year = destsetyear;
 COMMIT;
 END IF;
 FETCH cursor_uidetail
 INTO strvalue, strsource;
 EXCEPTION
 WHEN OTHERS THEN
 FETCH cursor_uidetail
 INTO strvalue, strsource;
 END;
 END LOOP;
 CLOSE cursor_uidetail;
 END;
 COMMIT;
 END;
 END viewmanage;';

execute immediate 'create or replace procedure workflowManager(srcRgCode   in varchar2,
 destRgCode  in varchar2,
 srcSetYear  in varchar2,
 destSetYear in varchar2) as
 begin
 execute immediate ''delete from SYS_WF_CONDITIONS
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_FLOWS
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODES
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_MODULE_NODE
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_ACTION_INSPECT
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_TOLLY_ACTION_TYPE
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_ROLE_NODE
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_FLOWS_BYTE
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_RULE
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_CONDITION_LINES
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_CONDITIONS
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_RELATIONS
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_CONDITION_PARAS
 where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 declare
 varHasColumn number;
 begin
 select count(*)
 into varHasColumn
 from COLS
 where TABLE_NAME = ''SYS_WF_CONDITIONS''
 and COLUMN_NAME = ''OLD_CONDITION_ID'';
 if varHasColumn = 0 then
 execute immediate ''alter table SYS_WF_CONDITIONS add OLD_CONDITION_ID varchar2(38)'';
 end if;
 select count(*)
 into varHasColumn
 from COLS
 where TABLE_NAME = ''SYS_WF_FLOWS''
 and COLUMN_NAME = ''OLD_WF_ID'';
 if varHasColumn = 0 then
 execute immediate ''alter table SYS_WF_FLOWS add OLD_WF_ID number'';
 end if;
 select count(*)
 into varHasColumn
 from COLS
 where TABLE_NAME = ''SYS_WF_NODES''
 and COLUMN_NAME = ''OLD_NODE_ID'';
 if varHasColumn = 0 then
 execute immediate ''alter table SYS_WF_NODES add OLD_NODE_ID number'';
 end if;
 end;
 execute immediate  ''insert into SYS_WF_CONDITIONS value
 (CONDITION_ID, CONDITION_CODE, CONDITION_NAME, EXPRESSION, REMARK, CREATE_USER,
 CREATE_DATE, LATEST_OP_USER, LATEST_OP_DATE, LAST_VER, BSH_EXPRESSION, CONDITION_TYPE,
 SET_YEAR, RG_CODE, OLD_CONDITION_ID)
 select NEWID CONDITION_ID, CONDITION_CODE, CONDITION_NAME, EXPRESSION, REMARK,
 CREATE_USER, CREATE_DATE, LATEST_OP_USER, LATEST_OP_DATE, LAST_VER, BSH_EXPRESSION,
 CONDITION_TYPE, '' || destSetYear || '' SET_YEAR, '''''' ||
 destRgCode || '''''' RG_CODE,
 CONDITION_ID OLD_CONDITION_ID from SYS_WF_CONDITIONS
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_FLOWS value
 (WF_ID, WF_CODE, WF_NAME, WF_TABLE_NAME, ID_COLUMN_NAME, CONDITION_ID, REMARK,
 ENABLED, CREATE_USER, CREATE_DATE, LATEST_OP_USER, LATEST_OP_DATE, SET_YEAR,
 LAST_VER, COA_ID, RIGHT_CCID, RIGHT_RCID, RG_CODE, OLD_WF_ID)
 select SEQ_SYS_WF_ID.NEXTVAL WF_ID, WF_CODE, WF_NAME, WF_TABLE_NAME, ID_COLUMN_NAME,
 CONDITION_ID, REMARK, ENABLED, CREATE_USER, CREATE_DATE, LATEST_OP_USER,
 LATEST_OP_DATE, '' || destSetYear || '' SET_YEAR, LAST_VER, COA_ID, RIGHT_CCID,
 RIGHT_RCID,  '''''' || destRgCode ||  '''''' RG_CODE, WF_ID OLD_WF_ID from SYS_WF_FLOWS
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_NODES value
 (WF_ID, NODE_CODE, NODE_NAME, NODE_TYPE, REMARK, CREATE_USER, CREATE_DATE,
 LATEST_OP_USER, LATEST_OP_DATE, NODE_ID, WF_TABLE_NAME, GATHER_FLAG, ID_COLUMN_NAME,
 OUTER_TABLE_NAME, OUTER_COLUMN_NAME, RELATION_COLUMN_NAME, LAST_VER, SEND_MSG_FLAG,
 SEND_MSG_TIME, AUTO_AUDIT_FLAG, AUTO_AUDIT_TIME, SEND_MSG_TIME_UNIT,
 AUTO_AUDIT_TIME_UNIT, SET_YEAR, RG_CODE, OLD_NODE_ID)
 select WF_ID, NODE_CODE, NODE_NAME, NODE_TYPE, REMARK, CREATE_USER, CREATE_DATE,
 LATEST_OP_USER, LATEST_OP_DATE, SEQ_NODE.NEXTVAL NODE_ID, WF_TABLE_NAME, GATHER_FLAG,
 ID_COLUMN_NAME, OUTER_TABLE_NAME, OUTER_COLUMN_NAME, RELATION_COLUMN_NAME, LAST_VER,
 SEND_MSG_FLAG, SEND_MSG_TIME, AUTO_AUDIT_FLAG, AUTO_AUDIT_TIME, SEND_MSG_TIME_UNIT,
 AUTO_AUDIT_TIME_UNIT, '' || destSetYear || '' SET_YEAR,  '''''' ||
 destRgCode ||  '''''' RG_CODE,
 NODE_ID OLD_NODE_ID from SYS_WF_NODES
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_MODULE_NODE value
 (NODE_ID, MODULE_ID, LAST_VER, SET_YEAR, RG_CODE)
 select NODE_ID, MODULE_ID, LAST_VER, '' || destSetYear ||
 '' SET_YEAR,  '''''' || destRgCode ||  '''''' RG_CODE from SYS_WF_MODULE_NODE
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_NODE_ACTION_INSPECT value
 (NODE_ID, ACTION_TYPE_CODE, INSPECT_RULE_ID, LAST_VER, SET_YEAR, RG_CODE)
 select NODE_ID, ACTION_TYPE_CODE, INSPECT_RULE_ID, LAST_VER, '' ||
 destSetYear || '' SET_YEAR,  '''''' || destRgCode ||
 '''''' RG_CODE from SYS_WF_NODE_ACTION_INSPECT
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_NODE_TOLLY_ACTION_TYPE value
 (NODE_ID, ACTION_TYPE_CODE, TOLLY_FLAG, LAST_VER, SET_YEAR, RG_CODE)
 select NODE_ID, ACTION_TYPE_CODE, TOLLY_FLAG, LAST_VER, '' ||
 destSetYear || '' SET_YEAR,  '''''' || destRgCode ||
 '''''' RG_CODE from SYS_WF_NODE_TOLLY_ACTION_TYPE
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_ROLE_NODE value
 (NODE_ID, ROLE_ID, LAST_VER, SET_YEAR, RG_CODE)
 select NODE_ID, ROLE_ID, LAST_VER,  '''''' || destSetYear ||
 '''''' SET_YEAR,  '''''' || destRgCode ||  '''''' RG_CODE from SYS_WF_ROLE_NODE
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_FLOWS_BYTE value
 (WF_ID, WF_BYTE, LAST_VER, SET_YEAR, RG_CODE)
 select WF_ID, WF_BYTE, LAST_VER, '' || destSetYear ||
 '' SET_YEAR,  '''''' || destRgCode ||  '''''' RG_CODE from SYS_WF_FLOWS_BYTE
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_RULE value
 (WF_ID, RULE_ID, LAST_VER, SET_YEAR, RG_CODE)
 select WF_ID, RULE_ID, LAST_VER, '' || destSetYear ||
 '' SET_YEAR,  '''''' || destRgCode ||  '''''' RG_CODE from SYS_WF_RULE
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_CONDITION_LINES value
 (LINE_ID, CONDITION_ID, OPERATOR, LOGIC_OPERATOR, CREATE_USER, CREATE_DATE,
 LATEST_OP_USER, LATEST_OP_DATE, LEFT_PARE, RIGHT_PARE, LAST_VER, LEFT_PARAID,
 RIGHT_PARAID, LINE_SORT, SET_YEAR, RG_CODE)
 select NEWID LINE_ID, CONDITION_ID, OPERATOR, LOGIC_OPERATOR, CREATE_USER, CREATE_DATE,
 LATEST_OP_USER, LATEST_OP_DATE, LEFT_PARE, RIGHT_PARE, LAST_VER, LEFT_PARAID,
 RIGHT_PARAID, LINE_SORT, '' || destSetYear ||
 '' SET_YEAR,  '''''' || destRgCode ||  '''''' RG_CODE
 from SYS_WF_CONDITION_LINES
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_NODE_CONDITIONS value
 (WF_ID, NODE_ID, NEXT_NODE_ID, CONDITION_ID, ROUTING_TYPE, LAST_VER, SET_YEAR, RG_CODE)
 select WF_ID, NODE_ID, NEXT_NODE_ID, CONDITION_ID, ROUTING_TYPE, LAST_VER, '' ||
 destSetYear || '' SET_YEAR,  '''''' || destRgCode ||
 '''''' RG_CODE from SYS_WF_NODE_CONDITIONS
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_NODE_RELATIONS value
 (WF_ID, NODE_ID, NEXT_NODE_ID, RELATION_TYPE, LAST_VER, SET_YEAR, RG_CODE)
 select WF_ID, NODE_ID, NEXT_NODE_ID, RELATION_TYPE, LAST_VER, '' ||
 destSetYear || '' SET_YEAR,  '''''' || destRgCode ||
 '''''' RG_CODE from SYS_WF_NODE_RELATIONS
 where SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 execute immediate ''insert into SYS_WF_CONDITION_PARAS value
 (PARA_ID, CONDITION_ID, PARA_DESC, PARA_TYPE, PARA_NAME, PARA_REMARK, PARA_VALUETYPE,
 FUN_PARAS, IS_SHARED, FUN_ID, SET_YEAR, RG_CODE) select NEWID PARA_ID, CONDITION_ID,
 PARA_DESC, PARA_TYPE, PARA_NAME, PARA_REMARK, PARA_VALUETYPE, FUN_PARAS, IS_SHARED,
 FUN_ID, '' || destSetYear || '' SET_YEAR,  '''''' || destRgCode ||
 '''''' RG_CODE from SYS_WF_CONDITION_PARAS
 where  SET_YEAR = '' || srcSetYear || '' and RG_CODE = '''''' ||
 srcRgCode||'''''''';
 commit;
 execute immediate ''update SYS_WF_FLOWS set condition_id = ''''#''''
 where condition_id not in (select condition_id from sys_wf_conditions) and condition_id <> ''''#'''' and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_CONDITION_LINES
 where condition_id not in (select condition_id from sys_wf_conditions) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODES
 where wf_id not in (select wf_id from SYS_WF_FLOWS) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_FLOWS_BYTE
 where wf_id not in (select wf_id from SYS_WF_FLOWS) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_RULE
 where wf_id not in (select wf_id from SYS_WF_FLOWS) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_CONDITIONS
 where wf_id not in (select wf_id from SYS_WF_FLOWS) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_RELATIONS
 where wf_id not in (select wf_id from SYS_WF_FLOWS) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_MODULE_NODE
 where node_id not in (select node_id from SYS_WF_NODES) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_ACTION_INSPECT
 where node_id not in (select node_id from SYS_WF_NODES) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_TOLLY_ACTION_TYPE
 where node_id not in (select node_id from SYS_WF_NODES) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_ROLE_NODE
 where node_id not in (select node_id from SYS_WF_NODES) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_CONDITIONS
 where node_id not in (select node_id from SYS_WF_NODES) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_RELATIONS
 where node_id not in (select node_id from SYS_WF_NODES) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_CONDITIONS
 where next_node_id not in (select node_id from SYS_WF_NODES) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 execute immediate ''delete from SYS_WF_NODE_RELATIONS
 where next_node_id not in (select node_id from SYS_WF_NODES) and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 commit;
 execute immediate ''update SYS_WF_CONDITIONS set CONDITION_CODE = CONDITION_ID,
 CONDITION_NAME = CONDITION_ID where SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 commit;
 declare
 type varchar2Array is table of varchar2(42) index by binary_integer;
 array_tableName varchar2Array;
 varTableName    varchar2(42);
 begin
 array_tableName(1) := ''SYS_WF_FLOWS'';
 array_tableName(2) := ''SYS_WF_CONDITION_LINES'';
 array_tableName(3) := ''SYS_WF_NODE_CONDITIONS'';
 for i in 1 .. array_tableName.count loop
 varTableName := array_tableName(i);
 declare
 type refCursor is ref cursor;
 cursor_conditionId refCursor;
 varOldConditionId  varchar2(38);
 varNewConditionId  varchar2(38);
 begin
 open cursor_conditionId for ''select distinct CONDITION_ID from '' || varTableName || '' where CONDITION_ID <> ''''#'''' and SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 fetch cursor_conditionId
 into varOldConditionId;
 while cursor_conditionId% found loop
 declare
 begin
 execute immediate ''select CONDITION_ID from SYS_WF_CONDITIONS
 where OLD_CONDITION_ID = '''''' ||
 varOldConditionId || ''''''''
 into varNewConditionId;
 exception
 when others then
 varNewConditionId := null;
 end;
 if varNewConditionId is not null then
 execute immediate ''update '' || varTableName ||
 '' set CONDITION_ID = '''''' || varNewConditionId ||
 '''''' where CONDITION_ID = '''''' ||
 varOldConditionId || '''''' and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' ||
 destRgCode||'''''''';
 end if;
 fetch cursor_conditionId
 into varOldConditionId;
 end loop;
 close cursor_conditionId;
 commit;
 end;
 end loop;
 end;
 declare
 type varchar2Array is table of varchar2(42) index by binary_integer;
 array_tableName varchar2Array;
 varTableName    varchar2(42);
 begin
 array_tableName(1) := ''SYS_WF_NODES'';
 array_tableName(2) := ''SYS_WF_FLOWS_BYTE'';
 array_tableName(3) := ''SYS_WF_RULE'';
 array_tableName(4) := ''SYS_WF_NODE_CONDITIONS'';
 array_tableName(5) := ''SYS_WF_NODE_RELATIONS'';
 for i in 1 .. array_tableName.count loop
 varTableName := array_tableName(i);
 declare
 type refCursor is ref cursor;
 cursor_wfId refCursor;
 varOldWfId  varchar2(38);
 varNewWfId  varchar2(38);
 begin
 open cursor_wfId for ''select distinct WF_ID from '' || varTableName || '' where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 fetch cursor_wfId
 into varOldWfId;
 while cursor_wfId% found loop
 execute immediate ''select max(WF_ID) from SYS_WF_FLOWS
 where OLD_WF_ID = '''''' || varOldWfId || ''''''''
 into varNewWfId;
 execute immediate ''update '' || varTableName || '' set WF_ID = '''''' ||
 varNewWfId || '''''' where WF_ID = '''''' ||
 varOldWfId || '''''' and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 fetch cursor_wfId
 into varOldWfId;
 end loop;
 close cursor_wfId;
 commit;
 end;
 end loop;
 end;
 declare
 type varchar2Array is table of varchar2(42) index by binary_integer;
 array_tableName varchar2Array;
 varTableName    varchar2(42);
 begin
 array_tableName(1) := ''SYS_WF_MODULE_NODE'';
 array_tableName(2) := ''SYS_WF_NODE_ACTION_INSPECT'';
 array_tableName(3) := ''SYS_WF_NODE_TOLLY_ACTION_TYPE'';
 array_tableName(4) := ''SYS_WF_ROLE_NODE'';
 array_tableName(5) := ''SYS_WF_NODE_CONDITIONS'';
 array_tableName(6) := ''SYS_WF_NODE_RELATIONS'';
 for i in 1 .. array_tableName.count loop
 varTableName := array_tableName(i);
 declare
 type refCursor is ref cursor;
 cursor_nodeId refCursor;
 varOldNodeId  varchar2(38);
 varNewNodeId  varchar2(38);
 begin
 open cursor_nodeId for ''select distinct NODE_ID from '' || varTableName || '' where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 fetch cursor_nodeId
 into varOldNodeId;
 while cursor_nodeId% found loop
 execute immediate ''select nvl(max(NODE_ID),'' ||
 '''''''''') from SYS_WF_NODES
 where OLD_NODE_ID = '''''' || varOldNodeId || ''''''''
 into varNewNodeId;
 if varNewNodeId is not null then
 execute immediate ''update '' || varTableName ||
 '' set NODE_ID = '''''' || varNewNodeId ||
 '''''' where NODE_ID = '''''' || varOldNodeId ||
 '''''' and SET_YEAR = '' || destSetYear ||
 '' and RG_CODE = '''''' || destRgCode||'''''''';
 end if;
 fetch cursor_nodeId
 into varOldNodeId;
 end loop;
 close cursor_nodeId;
 commit;
 end;
 end loop;
 end;
 declare
 type varchar2Array is table of varchar2(42) index by binary_integer;
 array_tableName varchar2Array;
 varTableName    varchar2(42);
 begin
 array_tableName(1) := ''SYS_WF_NODE_CONDITIONS'';
 array_tableName(2) := ''SYS_WF_NODE_RELATIONS'';
 for i in 1 .. array_tableName.count loop
 varTableName := array_tableName(i);
 declare
 type refCursor is ref cursor;
 cursor_nextNodeId refCursor;
 varOldNextNodeId  varchar2(38);
 varNewNextNodeId  varchar2(38);
 begin
 open cursor_nextNodeId for ''select distinct NEXT_NODE_ID from '' || varTableName || '' where SET_YEAR = '' || destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 fetch cursor_nextNodeId
 into varOldNextNodeId;
 while cursor_nextNodeId% found loop
 execute immediate ''select max(NODE_ID) from SYS_WF_NODES
 where OLD_NODE_ID = '''''' || varOldNextNodeId || ''''''''
 into varNewNextNodeId;
 execute immediate ''update '' || varTableName ||
 '' set NEXT_NODE_ID = '''''' || varNewNextNodeId ||
 '''''' where NEXT_NODE_ID = '''''' ||
 varOldNextNodeId || '''''' and SET_YEAR = '' ||
 destSetYear || '' and RG_CODE = '''''' || destRgCode||'''''''';
 fetch cursor_nextNodeId
 into varOldNextNodeId;
 end loop;
 close cursor_nextNodeId;
 commit;
 end;
 end loop;
 declare
 roleIdCount number;
 tempId      varchar2(10);
 cursor roleIdCursor is
 select role_id
 from sys_wf_role_node
 where RG_CODE = destRgCode
 and set_year = destSetYear;
 begin
 open roleIdCursor;
 fetch roleIdCursor
 into tempId;
 while roleIdCursor%found loop
 select count(*)
 into roleIdCount
 from sys_role sr
 where sr.role_code =
 (select r.role_code
 from sys_role r
 where r.role_id = tempId
 and r.RG_CODE = srcRgCode
 and r.set_year = srcSetYear)
 and sr.RG_CODE = destRgCode
 and sr.set_year = destSetYear;
 if roleIdCount > 0 then
 execute immediate ''update sys_wf_role_node n set n.role_id=(select sr.role_id  from sys_role sr where sr.role_code=(select r.role_code from sys_role r where r.role_id='''''' ||
 tempId || '''''' and r.RG_CODE='''''' || srcRgCode ||
 '''''' and r.set_year='' || srcSetYear || '')
 and sr.RG_CODE='''''' || destRgCode ||
 '''''' and sr.set_year='' || destSetYear ||
 '') where n.role_id='''''' || tempId ||
 '''''' and n.RG_CODE='''''' || destRgCode ||
 '''''' and n.set_year='' || destSetYear;
 end if;
 fetch roleIdCursor
 into tempId;
 end loop;
 close roleIdCursor;
 commit;
 end;
 end;
 end workflowManager;';
execute immediate 'CREATE OR REPLACE PROCEDURE copy_sys_rulecrossjoin(srcrgcode  IN VARCHAR2,
 desrgcode  IN VARCHAR2,
 srcsetyear IN VARCHAR2,
 dessetyear IN VARCHAR2) AS
 BEGIN
 DECLARE
 sqlstr  VARCHAR2(2000);
 BEGIN
 IF (srcsetyear <> dessetyear AND srcrgcode = desrgcode) THEN
 DELETE sys_rule_cross_join_add t
 WHERE t.rule_id IN (SELECT rule_id
 FROM sys_rule
 WHERE set_year = dessetyear
 AND RG_CODE = srcrgcode);
 FOR var_rule IN (SELECT rule_id, (SELECT rule_id
 FROM sys_rule
 WHERE rule_code = r.rule_code
 AND RG_CODE = r.RG_CODE
 AND set_year = srcsetyear) old_rule_id
 FROM sys_rule r
 WHERE set_year = dessetyear
 AND RG_CODE = srcrgcode) LOOP
 IF var_rule.old_rule_id IS NOT NULL THEN
 INSERT INTO sys_rule_cross_join_add
 (rule_id, as_id, AGENCY_ID, FUNDTYPE_ID, EXPFUNC_ID, AGENCYEXP_ID, EXPECO_ID, PAYTYPE_ID, BGTTYPE_ID, PAYKIND_ID, MB_ID, file_id, SETMODE_ID, in_bs_id, in_bis_id, cb_id, pb_id, ib_id, bac_id, bap_id, bai_id, BGTDIR_ID, bp_id, BGTSOURCE_ID, hold1_id, hold2_id, hold3_id, hold4_id, hold5_id, hold6_id, hold7_id, hold8_id, hold9_id, hold10_id, hold11_id, hold12_id, hold13_id, hold14_id, hold15_id, hold16_id, hold17_id, hold18_id, hold19_id, hold20_id, hold21_id, hold22_id, hold23_id, hold24_id, hold25_id, hold26_id, hold27_id, hold28_id, hold29_id, hold30_id, bis_id, inpm_id, st_id, ct_id, sm_id, editor_id, ba_id, pa_id, last_ver, bc_id, ZFCGBS_ID, pub_id, GZBS_ID, ien_id, pf_id, payee_account_id, agent_account_id, tbv_id)
 SELECT var_rule.rule_id, as_id, AGENCY_ID, FUNDTYPE_ID, EXPFUNC_ID, AGENCYEXP_ID, EXPECO_ID, PAYTYPE_ID, BGTTYPE_ID, PAYKIND_ID, MB_ID, file_id, SETMODE_ID, in_bs_id, in_bis_id, cb_id, pb_id, ib_id, bac_id, bap_id, bai_id, BGTDIR_ID, bp_id, BGTSOURCE_ID, hold1_id, hold2_id, hold3_id, hold4_id, hold5_id, hold6_id, hold7_id, hold8_id, hold9_id, hold10_id, hold11_id, hold12_id, hold13_id, hold14_id, hold15_id, hold16_id, hold17_id, hold18_id, hold19_id, hold20_id, hold21_id, hold22_id, hold23_id, hold24_id, hold25_id, hold26_id, hold27_id, hold28_id, hold29_id, hold30_id, bis_id, inpm_id, st_id, ct_id, sm_id, editor_id, ba_id, pa_id, last_ver, bc_id, ZFCGBS_ID, pub_id, GZBS_ID, ien_id, pf_id, payee_account_id, agent_account_id, tbv_id
 FROM sys_rule_cross_join_add t
 WHERE t.rule_id = var_rule.old_rule_id;
 END IF;
 END LOOP;
 FOR name_source IN (SELECT ele_code || ''_ID'' AS ele_name, ele_source
 FROM sys_element e
 WHERE e.RG_CODE = desrgcode --desRgCode
 AND e.set_year = dessetyear --desSetYear
 AND ele_code || ''_ID'' IN
 (SELECT column_name
 FROM user_tab_cols t
 WHERE t.table_name = ''SYS_RULE_CROSS_JOIN_ADD'')) LOOP
 sqlstr := ''update sys_rule_cross_join_add ad
 set '' || name_source.ele_name ||
 '' = (select chr_id  from '' || name_source.ele_source ||
 '' t where t.chr_code in
 (select chr_code
 from '' || name_source.ele_source || '' tt
 where tt.chr_id = ad.'' || name_source.ele_name ||
 '')  and t.RG_CODE='''''' || desrgcode || '''''' and set_year='''''' || dessetyear ||
 '''''') where
 ad.rule_id in
 (select rule_id
 from sys_rule r
 where r.RG_CODE = '''''' || desrgcode ||
 '''''' and r.set_year = '''''' || dessetyear || '''''')'';
 EXECUTE IMMEDIATE sqlstr;
 END LOOP;
 COMMIT;
 END IF;
 END;
 END copy_sys_rulecrossjoin;';
 
 execute immediate 'create or replace procedure PF_SYSINIT is
 P_SWITCH01 VARCHAR2(3);
 begin
 SELECT NVL(CHR_VALUE,''0'') into P_SWITCH01 FROM SYS_USERPARA where UPPER(CHR_CODE)=''SWITCH01'' ;
 IF( P_SWITCH01 = ''1'') THEN
 pf_glccidtransfer(TRUE);
 ELSE
 pf_glccidtransfer(FALSE);
 END IF;
 end PF_SYSINIT;';