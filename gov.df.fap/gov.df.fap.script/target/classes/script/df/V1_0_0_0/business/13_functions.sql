begin
execute immediate 'create or replace function create_rcid(ccid in string, set_year in string)
 return varchar2 is
 rcid varchar2(38);
 type lcCursors is ref cursor;
 lsSql             varchar2(20000);
 lsCursor          lcCursors; --结果集游标
 detailLsCursor    lcCursors; --结果集游标
 findRcidSql       varchar2(4000); --查询满足要求的RCID
 insertRoleRcidSql varchar2(4000); --插入ROLE与RCID的关联表
 insertRcidSql1    varchar2(4000); --插入RCID的表（字段）
 insertRcidSql2    varchar2(4000); --插入RCID的表（值）
 ele_code          varchar2(38); --要素简称
 ele_source        varchar2(38); --要素表
 ele_value         varchar2(38); --要旨值
 tmp_rcid          varchar2(38); --新生成的RCID
 begin
 insertRcidSql1 := ''insert into Sys_Right_Combination(set_year'';
 insertRcidSql2 := '' values ('' || set_year;
 lsSql := ''select ele_code,ele_source from Sys_Element where IS_RIGHTFILTER=1 and SET_YEAR='' ||set_year;
 open lsCursor for lsSql;
 loop
 fetch lsCursor
 into ele_code, ele_source;
 if (lsCursor%notfound) then
 exit;
 else
 lsSql := ''select '' || ele_code || ''_id from gl_ccids where  ccid='''''' || ccid ||
 '''''' and '' || ele_code || ''_id is not null'';
 open detailLsCursor for lsSql;
 fetch detailLsCursor
 into ele_value;
 if (detailLsCursor%notfound) then
 findRcidSql := findRcidSql || '' and com.'' || ele_code ||
 ''_ID is null '';
 insertRoleRcidSql := insertRoleRcidSql || '' and (a.'' || ele_code ||
 ''_id is null )'';
 else
 insertRcidSql1    := insertRcidSql1 || '','' || ele_code || ''_id'';
 insertRcidSql2    := insertRcidSql2 || '','''''' || ele_value || '''''''';
 findRcidSql       := findRcidSql || '' and '' || ele_code ||
 ''_ID ='''''' || ele_value || '''''''';
 insertRoleRcidSql := insertRoleRcidSql || '' and (a.'' || ele_code ||
''_id is null or a.'' || ele_code || ''_id='''''' ||
 ele_value ||'''''')'';
 end if;
 close detailLsCursor;
 end if;
 end loop;
 close lsCursor;
 lsSql := ''select rcid from Sys_Right_Combination com '' ||
 ''where com.SET_YEAR='' || set_year || findRcidSql;
 open lsCursor for lsSql;
 fetch lsCursor
 into tmp_rcid;
 if (lsCursor%notfound) then
 lsSql := ''select seq_rcid.nextval from dual'';
 open lsCursor for lsSql;
 fetch lsCursor
 into tmp_rcid;
 close lsCursor;
 lsSql := insertRcidSql1 || '',rcid) '' || insertRcidSql2 || '','''''' ||
 tmp_rcid || '''''') '';
 execute immediate lsSql;
 else
 rcid := tmp_rcid;
 return rcid;
 end if;
 lsSql := ''insert into sys_rule_rcid(set_year,rule_id,rcid,last_ver)'' ||
 '' select '' || set_year || '',b.rule_id,'''''' || tmp_rcid ||
 '''''',sysdate from sys_rule b where b.rule_classify=''''006'''' and exists(select 1 from sys_rule_cross_join_add a where a.rule_id=b.rule_id ''
 || insertRoleRcidSql || '' ) and not exists(select 1 from sys_rule_cross_join_del a where a.rule_id=b.rule_id ''
 || insertRoleRcidSql||'')'';
 execute immediate lsSql;
 rcid := tmp_rcid;
 return rcid;
 EXCEPTION
 WHEN OTHERS THEN
 rcid := tmp_rcid;
 return rcid;
 end create_rcid;';

execute immediate 'create or replace function create_rule_cross(rule_id in string)
 return varchar2 is
 return_str varchar2(38);
 type lcCursors is ref cursor;
 lsSql          varchar2(20000);
 lsCursor       lcCursors; --结果集游标
 detailLsCursor lcCursors; --结果集游标
 ele_code   varchar2(38); --要素简称
 ele_source varchar2(38); --要素表
 return_str           varchar2(38); --新生成的RCID
 right_group_id       varchar2(38); --新生成的RCID
 right_group_describe varchar2(38);
 right_type           varchar2(38);
 str                  varchar2(4000);
 str1                 varchar2(4000);
 str2                 varchar2(4000);
 str3                 varchar2(20000);
 num                  int(4);
 begin
 str := ''delete from sys_RULE_CROSS_JOIN_add where rule_id='''''' || rule_id || '''''''';
 execute immediate str;
 str := ''delete from sys_RULE_CROSS_JOIN_del where rule_id='''''' || rule_id || '''''''';
 execute immediate str;
 lsSql := ''select right_type from sys_rule b where  rule_id='''''' || rule_id || '''''''';
 open lsCursor for lsSql;
 fetch lsCursor
 into right_type;
 if (lsCursor%notfound) then
 return ''false'';
 else
 if (right_type = 0 or right_type = 2) then
 return ''true'';
 end if;
 end if;
 close lsCursor;
 lsSql := ''select right_group_id,right_group_describe from sys_right_group b where  rule_id='''''' ||
 rule_id || '''''''';
 open lsCursor for lsSql;
 loop
 fetch lsCursor
 into right_group_id, right_group_describe;
 if (lsCursor%notfound) then
 exit;
 else
 lsSql := '' select distinct a.ele_code,a.right_type,c.ele_source from sys_right_group_type a,sys_element c  '' ||
 '' where  a.ele_code=c.ele_code and a.right_type=1 and a.right_group_id='''''' ||
 right_group_id || '''''''';
 str  := '''';
 str1 := '' RULE_ID, '';
 str2 := '''''''' || rule_id || '''''','';
 str3 := '''';
 open detailLsCursor for lsSql;
 num := 0;
 loop
 fetch detailLsCursor
 into ele_code, right_type, ele_source;
 if (detailLsCursor%notfound) then
 exit;
 else
 str1 := str1 || lower(ele_code) || ''_id,'';
 str2 := str2 || ''a'' || to_char(num) || ''.chr_id,'';
 str3 := str3 || '' (select * from '' || '' sys_right_group_detail m,'' ||
 ele_source ||
 '' n  where m.RG_CODE=n.RG_CODE and m.ele_value=n.chr_code and m.right_group_id='''''' ||
 right_group_id || '''''' and m.ele_code='''''' || ele_code ||
 '''''') a'' || to_char(num) || '','';
 num  := num + 1;
 end if;
 end loop;
 close detailLsCursor;
 if (str3 is not null) then
 if (right_group_describe = 001 or right_group_describe = 002) then
 str := ''insert into sys_RULE_CROSS_JOIN_add ('' ||
 substr(str1, 0, length(str1) - 1) || '') SELECT '' ||
 substr(str2, 0, length(str2) - 1) || '' from '' ||
 substr(str3, 0, length(str3) - 1);
 execute immediate str;
 end if;
 end if;
 if (right_group_describe = 003) then
 str := ''insert into sys_RULE_CROSS_JOIN_del('' ||
 substr(str1, 0, length(str1) - 1) || '') SELECT '' ||
 substr(str2, 0, length(str2) - 1) || '' from '' ||
 substr(str3, 0, length(str3) - 1);
 execute immediate str;
 end if;
 end if;
 end loop;
 close lsCursor;
 return ''true'';
 end create_rule_cross;';
execute immediate 'create or replace function ELE_IS_VALID(eleCode in VARCHAR2, chrId in VARCHAR2) return number is
 eleSource sys_element.ele_source%type;
 type eleRecord is record(chr_code varchar2(42),chr_code1 varchar2(42),chr_code2 varchar2(42),chr_code3 varchar2(42),chr_code4 varchar2(42),chr_code5 varchar2(42),chr_id varchar2(38),chr_id1 varchar2(38),chr_id2 varchar2(38),chr_id3 varchar2(38),chr_id4 varchar2(38),chr_id5 varchar2(38),
 parent_id varchar2(38),level_num number(2));
 ele_record eleRecord;
 levelNum number;
 level_count number := -1;
 v_sql varchar2(300);
 temp_id varchar2(38);
 temp_code varchar2(42);
 begin
 select ele_source into eleSource from sys_element where ele_code = upper(eleCode);
 v_sql := ''select chr_code,chr_code1,chr_code2,chr_code3,chr_code4,chr_code5,chr_id,chr_id1,chr_id2,chr_id3,chr_id4,chr_id5,parent_id,level_num  from ''||eleSource||'' where chr_id = ''''''||chrId||'''''''';
 execute immediate v_sql into ele_record;
 levelNum := ele_record.level_num;
 if(levelNum > 1 and ele_record.parent_id is null) then
 return(1);
 end if;
 for level_count in 1 .. levelNum-1 loop
 select decode(level_count,1,ele_record.chr_id1,2,ele_record.chr_id2,3,ele_record.chr_id3,4,ele_record.chr_id4,5,ele_record.chr_id5) into temp_id from dual;
 select decode(level_count,1,ele_record.chr_code1,2,ele_record.chr_code2,3,ele_record.chr_code3,4,ele_record.chr_code4,5,ele_record.chr_code5) into temp_code from dual;
 if(temp_id is null ) then
 return(2);
 end if;
 if(temp_code is null ) then
 return(3);
 end if;
 end loop;
 return(-1);
 exception
 when no_data_found then
 return(0);
 end ELE_IS_VALID;';
execute immediate ' create or replace function get_bizdata_searchsql(entityid  in string,
 tablename in string)
 return varchar2 is
 returnSql varchar2(2000);
 id        varchar2(20);
 tempTableName varchar2(50);
 begin
 id := ''id'';
 tempTableName :=tablename;
 if instr(upper(tablename), ''BUDGET'') > 0 then
 id := ''b_vou_id'';
 end if;
 returnSql := ''select t.*,g.* from VW_''||tempTableName||
 '' t,gl_ccids g where t.'' || id || ''='''''' || entityid ||
 '''''' and t.ccid = g.ccid'';
 return returnSql;
 end get_bizdata_searchsql;';

execute immediate 'CREATE OR REPLACE FUNCTION newid RETURN VARCHAR2 IS
 guid VARCHAR2(38);
 BEGIN
 guid  := SYS_guid();
 RETURN ''{''||substr(guid,1,8)||''-''|| substr(guid,9,4)||''-''|| substr(guid,13,4)||''-''|| substr(guid,17,4)||''-''||
 substr(guid,21,12)|| ''}'';
 END newid;';

execute immediate 'CREATE OR REPLACE FUNCTION NEWNUMID RETURN number IS
 numberid number(9);
 BEGIN
 select F_numberID.NEXTVAL INTO numberid FROM DUAL;
 return numberid;
 END NEWNUMID;';

execute immediate 'create or replace function RULE_IS_MATCH(ruleid in string, ccid in string)
 return boolean is
 type lcCursors is ref cursor;
 lsSql                varchar2(1000);
 lsCursor             lcCursors;
 detailLsCursor       lcCursors;
 right_type           varchar2(2);
 rule_result               varchar2(2);
 right_group_id       varchar2(38);
 right_group_describe varchar2(38);
 select_sql           varchar2(6000);
 union_select_sql     varchar2(4000);
 minus_select_sql     varchar2(4000);
 ele_code             varchar2(38);
 ele_source           varchar2(38);
 begin
 lsSql := ''select right_type from sys_rule where rule_id='''''' || ruleid || '''''''';
 open lsCursor for lsSql;
 fetch lsCursor
 into right_type;
 close lsCursor;
 if (right_type = 0) then
 return true;
 end if;
 if (right_type = 2) then
 return false;
 end if;
 if(ruleid<>0) then
 lsSql := ''select 1 from sys_rule r where not exists (select 1 from sys_right_group g where r.rule_id=g.rule_id) and r.rule_id='''''' || ruleid || '''''''';
 open lsCursor for lsSql;
 fetch lsCursor
 into rule_result;
 if (rule_result = 1) then
 return true;
 end if;
 end if;
 select_sql       := ''select 1 from dual where 1=1'';
 union_select_sql := '' and (1=0 '';
 lsSql            := ''select right_group_id,right_group_describe from sys_right_group where rule_id='''''' ||
 ruleid || '''''''';
 open lsCursor for lsSql;
 loop
 fetch lsCursor
 into right_group_id, right_group_describe;
 if (lsCursor%notfound) then
 exit;
 else
 if (right_group_describe = 001 or right_group_describe = 002) then
 union_select_sql := union_select_sql ||
 '' Or Exists (select 1 from dual where 1=1'';
 lsSql            := ''select distinct d.ele_code,e.ele_source from sys_right_group_type d, sys_element e where  d.ele_code=e.ele_code and d.right_group_id='''''' ||
 right_group_id || '''''''';
 open detailLsCursor for lsSql;
 loop
 fetch detailLsCursor
 into ele_code, ele_source;
 if (detailLsCursor%notfound) then
 exit;
 else
 union_select_sql := union_select_sql ||
 '' and exists (select 1 from sys_right_group_detail b where '' ||
 '' b.right_group_id='''''' || right_group_id || '''''''' ||
 '' and b.ele_code = '''''' || ele_code || '''''''' ||
 '' and (b.ele_value = (select chr_code from '' ||
 ele_source || '' where chr_id=(select '' ||
 ele_code ||
 ''_id from gl_ccids where ccid='''''' || ccid || '''''''' ||
 '')) or b.ele_value = ''''#''''))'';
 end if;
 end loop;
 close detailLsCursor;
 union_select_sql := union_select_sql || '')'';
 end if;
 if (right_group_describe = 003) then
 minus_select_sql := minus_select_sql ||
 '' And Not Exists (select 1 from dual where 1=1 '';
 lsSql            := ''select distinct d.ele_code,e.ele_source from sys_right_group_type d, sys_element e where  d.ele_code=e.ele_code and d.right_group_id='''''' ||
 right_group_id || '''''''';
 open detailLsCursor for lsSql;
 loop
 fetch detailLsCursor
 into ele_code, ele_source;
 if (detailLsCursor%notfound) then
 exit;
 else
 minus_select_sql := minus_select_sql ||
 '' and exists (select 1 from sys_right_group_detail b where '' ||
 '' b.right_group_id='''''' || right_group_id || '''''''' ||
 '' and b.ele_code = '''''' || ele_code || '''''''' ||
 '' and (b.ele_value = (select chr_code from '' ||
 ele_source || '' where chr_id=(select '' ||
 ele_code ||
 ''_id from gl_ccids where ccid='''''' || ccid || '''''''' ||
 '')) or b.ele_value = ''''#''''))'';
 end if;
 end loop;
 close detailLsCursor;
 minus_select_sql := minus_select_sql || '')'';
 end if;
 end if;
 end loop;
 close lsCursor;
 select_sql := select_sql || union_select_sql || '')'' || minus_select_sql;
 open lsCursor for select_sql;
 fetch lsCursor
 into ele_code;
 if (lsCursor%notfound) then
 return false;
 else
 return true;
 end if;
 end RULE_IS_MATCH;';

execute immediate 'create or replace function RULE_IS_MATCH_ADAPTER(ruleid in string, ccid in string)
 return integer
 is
 isMatch boolean;
 begin
 isMatch := RULE_IS_MATCH(ruleid, ccid);
 if isMatch then
 return 1;
 end if;
 return 0;
 end;';
