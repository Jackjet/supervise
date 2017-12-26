import org.apache.log4j.Logger;

import com.longtu.framework.daosupport.CommonJdbcDaoSupport;
import com.longtu.framework.start.StartInfo;
import com.longtu.framework.util.StringUtil;

public class CheckStartService {
  private static final Logger logger = Logger.getLogger(CheckStartService.class);

  private static final CheckStartService service = new CheckStartService();

  String sql1 = "create or replace procedure testcreate is i integer;begin      execute immediate 'create table TEST_TABLE(  A VARCHAR2(1))';      execute immediate 'create or replace view test_view as select * from TEST_TABLE';      execute immediate 'create or replace package TEST_PGK is  function a(v_parm varchar2) RETURN varchar2;end TEST_PGK;';      execute immediate 'create or replace package body TEST_PGK is  function a(v_parm  varchar2)   Return varchar2 is  begin    return ''1'';  end a;END TEST_PGK;';      execute immediate 'create sequence TEST_SEQ minvalue 1 maxvalue 100 start with 1';      execute immediate 'create or replace trigger TEST_TRI  before insert or update or delete ON TEST_TABLE  for each row begin if inserting then    :new.a  := ''1'';   end if; end TEST_TRI;';  end testcreate;";

  String sql2 = "create or replace procedure testdrop is i integer; begin   select count(*) into i from user_tables where table_name='TEST_TABLE';       if i>0 then            execute immediate 'drop table TEST_TABLE';       end if;              select count(*) into i from user_views where view_name='TEST_VIEW';       if i>0 then            execute immediate 'drop view TEST_VIEW';       end if;              select count(*) into i from user_objects where  object_name='TEST_PGK';       if i>0 then            execute immediate 'drop package TEST_PGK';       end if;              select count(*) into i from user_objects where object_name='TEST_SEQ';       if i>0 then            execute immediate 'drop sequence TEST_SEQ';       end if;              select count(*) into i from user_objects where object_name='TEST_TRI';       if i>0 then            execute immediate 'drop trigger TEST_TRI';       end if; end testdrop;";

  public static CheckStartService newInstance() {
    return service;
  }

  public void initStart() {
    CommonJdbcDaoSupport dao = CommonJdbcDaoSupport.instanceDao(DFInitClasspathXmlApplicationContext.getThis()
      .getDataSource());
    try {
      dao.execute("drop table tmp_install");
    } catch (Exception localException) {
    }
  }

  public void setDbGuid() {
    CommonJdbcDaoSupport dao = CommonJdbcDaoSupport.instanceDao(DFInitClasspathXmlApplicationContext.getThis()
      .getDataSource());
    try {
      dao.queryForList("select * from tmp_v_dbguid");
    } catch (Exception localException) {
      String guid = StringUtil.createUUID();
      dao.execute("create or replace view tmp_v_dbguid as select '" + guid + "' as guid  from dual");
    }
  }

  public void checkStart() {
    checkDbGrant();
  }

  private void checkDbGrant() {
    CommonJdbcDaoSupport dao = CommonJdbcDaoSupport.instanceDao(DFInitClasspathXmlApplicationContext.getThis()
      .getDataSource());
    try {
      dao.execute(this.sql1);
      dao.execute(this.sql2);
      dao.execute("call testdrop()");
      dao.execute("call testcreate()");
      dao.execute("call testdrop()");
      StartInfo.DB_USER_GRANT = true;
    } catch (Exception localException) {
      logger
        .error("用户权限不足，请检查权限！需要补充的权限包括：administer database trigger,alter any procedure,alter any table,comment any table,create any materialized view,create any procedure,create any sequence,create any synonym,create any table,create any trigger,create any view,create database link,create job,create materialized view,create procedure,create sequence,create session,create synonym,create table,create trigger,create type,create view,debug any procedure,debug connect session,delete any table,drop any materialized view,drop any synonym,drop any table,drop any view,insert any table,select any dictionary,select any table,unlimited tablespace,update any table");
      StartInfo.DB_USER_GRANT = false;
    }
  }

  private void checktablespace() {
  }
}
