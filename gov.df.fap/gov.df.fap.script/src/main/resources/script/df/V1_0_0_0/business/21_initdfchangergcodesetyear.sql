begin
execute immediate 'create or replace procedure initdf_changeSetYear(set_year in number) is
 begin
 FOR T_CURSOR IN (SELECT DISTINCT T.TABLE_NAME
 FROM USER_TABLES T, USER_TAB_COLUMNS B
 WHERE T.table_name = B.table_name
 and B.COLUMN_NAME = ''SET_YEAR'') LOOP
 IF (T_CURSOR.TABLE_NAME <> ''SYS_YEAR'' AND
 T_CURSOR.TABLE_NAME <> ''SYS_WORK_DAY'') THEN
 EXECUTE IMMEDIATE ''UPDATE ''|| T_CURSOR.TABLE_NAME ||'' SET SET_YEAR='' || set_year;
 END IF;
 END LOOP;
 EXECUTE IMMEDIATE ''DELETE FROM SYS_WORK_DAY WHERE SET_YEAR='' || set_year;
 pf_generate_day(set_year);
 commit;
 end initdf_changeSetYear;';