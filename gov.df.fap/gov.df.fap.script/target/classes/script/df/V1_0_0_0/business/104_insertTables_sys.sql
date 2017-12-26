begin
insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '3015', '�����Ŀ�����Ϣ', 'delete  fb_p_audit_opinion  where div_code = ''#div_code#'' and rg_code =''#rg_code#'' and set_year=#set_year# and data_type=#from_data_type# and batch_no=#batch_no# and audit_flag=1', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '6026', '���߷�����ļ��ύ������һ������', 'insert into fb_regular_info(file_id,file_code,file_name,file_kind_code,auth_level,div_code,div_name,dep_code,dep_name,duty_post,issu_time,valid_time,set_year,rg_code,file_summary,data_type,batch_no)select file_id,file_code,file_name,file_kind_code,auth_level,div_code,div_name,dep_code,dep_name,duty_post,issu_time,valid_time,set_year,rg_code,file_summary,#to_data_type#,batch_no from fb_regular_info where set_year=#set_year# and rg_code=#rg_code# and data_type=#from_data_type# and batch_no=#batch_no# and file_id=''#obj_code#''', '', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '6027', '�����˻�ʱɾ���û����ļ���Ȩ', 'delete fb_regular_usertofile where set_year=#set_year# and rg_code=#rg_code# and file_id=''#obj_code#''', '', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '1007', '������������', 'FB_U_PAYOUT_GOV_SERVERBUY', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '6025', '���߷�����ļ��ύɾ����һ������', 'delete fb_regular_info where set_year=#set_year# and rg_code=#rg_code# and data_type=#to_data_type# and batch_no=#batch_no# and file_id=''#obj_code#''', '', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '1004', '���������ɹ�Ԥ���', 'FB_U_PAYOUT_GOV_PURCHASE', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '1006', '����֧��Ԥ���', 'FB_U_ACCT_DIVIDE', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3006', '������Ŀ��ר������', 'FB_P_DLIB_BASE', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3007', '������Ŀ��ר����ϸ��', 'FB_P_DLIB_DETAIL_MX', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3008', '������Ŀ��ר���ʽ���Դ��', 'FB_P_DLIB_DETAIL_PFS', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 1, '3009', '�����걨ʱ��', 'update fb_p_dlib_base set declaration_data_type = #from_data_type#,declaration_time =''#last_var#'' where (declaration_data_type = '''' or declaration_data_type is null)   and div_code = ''#div_code#'' and prj_code=''#obj_code#'' and set_year=#budget_year#', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '3500', '��¼���ϱ����1', 'insert into fb_s_trandetail
  (set_year,
   batch_no,
   en_id,
   div_code,
   div_name,
   transmit_flag,
   incept_flag,
   row_id)
  select #set_year# set_year,
         #batch_no# batch_no,
         en_id,
         div_code,
         div_name,
         0 transmit_flag,
         1 incept_flag,
         newid()
    from vw_fb_division
   where div_code=''#div_code#''
     and not exists (select 1
            from fb_s_trandetail
           where transmit_flag = 0 and set_year=#set_year# and batch_no=#batch_no#
             and div_code = ''#div_code#'')', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '3510', '��¼���ϱ����2', 'update fb_s_trandetail
   set incept_flag = 1
 where set_year=#set_year# and batch_no=#batch_no#
   and div_code = ''#div_code#''', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '3550', 'ɾ�����ϱ����', 'delete from fb_s_trandetail where set_year=''#set_year#'' and div_code=''#div_code#'' and batch_no=#batch_no#', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2010', '��֧��������', 'FB_U_DIV_FUNDSOURCE_INC', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '1002', '��������Ԥ���', 'FB_U_DIV_INCOMING_BUDGET', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '1003', '����֧��Ԥ���', 'FB_U_PAYOUT_BUDGET', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '1001', '���ݷ�˰�ʽ����ռƻ���', 'FB_U_DIV_INCOMING', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '1005', '�����Զ���¼���', 'FB_U_IR_DATA', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2001', '��λ�ۺ���Ϣ�����', 'FB_B_DIVINFO', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2002', '��ְ�ڱ���Ա��Ϣ��', 'FB_B_INACTIVESERVICE', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2003', '��ְ�ڱ���Ա��Ϣ��_��ҵ', 'FB_B_INACTIVESERVICESY', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2004', '��������Ա��Ϣ��', 'FB_B_RETIRE', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2005', '��������Ա��Ϣ��_��ҵ', 'FB_B_RETIRESY', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2006', '��λ�ʲ������', 'FB_B_FIXEDASSETS', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2007', 'ѧ�����������', 'FB_B_STUDENT', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2008', '��У�����ƻ���', 'FB_B_STUDENTZS', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '2009', '�������칫�豸����Զ����', 'FB_B_INFO', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 1, '4001', '�ύ����Ч���۸ڵ�ʱ��ʹ��', 'update fb_p_dlib_base set is_needappraise = ''-1'' where prj_code = ''#obj_code#'' and set_year = #set_year# and rg_code = ''#rg_code#'' and data_type = #to_data_type# and batch_no = 1', 'PRJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6003', '���뵥��Ӧ�䶯��¼', 'FB_BAS_APPLICATION', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6004', 'ָ��䶯���뵥', 'FB_BAS_B_BUDGET_APPLY', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6005', '������', 'FB_BAS_B_AFFIX', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6006', '��λ��Ϣ��', 'FB_BAS_B_T_DIVINFO', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6007', '׼����Ա�����', 'FB_BAS_B_T_ENTRY', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6008', '�̶��ʲ���', 'FB_BAS_B_T_FIXEDASSETS', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6009', '��ְ������', 'FB_BAS_B_T_INACTIVESERVICE', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6010', '��ְ��ҵ��', 'FB_BAS_B_T_INACTIVESERVICESY', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6011', '�Զ���¼���', 'FB_BAS_B_T_INFO', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6012', '����������', 'FB_BAS_B_T_RETIRE', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6013', '������ҵ��', 'FB_BAS_B_T_RETIRESY', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6016', '������', 'FB_BAS_B_T_SURVIVOR', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6017', '�䶯������', 'FB_BAS_B_BUDGET_MONEY', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6019', '�䶯�ֶ�', 'FB_BAS_B_MODIFY_FIELD', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('103', 2, '6020', 'Ԥ�������', 'FB_U_PAYOUT_BUDGET_ADJ', 'FORM_ID', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3020', 'Ԥ������䶯����', 'FB_P_ADJUST_DETAIL', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3021', 'Ԥ�������Ŀ����', 'FB_P_BASE', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3022', 'Ԥ�������Ŀ�ʽ���Դ��', 'FB_P_DETAIL_MX', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3023', 'Ԥ�������Ŀ��ϸ��', 'FB_P_DETAIL_PFS', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3024', 'Ԥ����������ɹ���', 'FB_U_PAYOUT_GOV_PURCHASE', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '3011', 'ר��Ԥ���������������˱�־', 'update fb_p_audit_opinion set audit_flag = 0 where div_code = ''#div_code#'' and prj_code=''#obj_code#'' and set_year=#set_year# and data_type=#to_data_type# and batch_no=#batch_no# and audit_flag=1', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3012', '���������ɹ�Ԥ���', 'FB_U_DLIB_PAYOUT_GOV_PURCHASE', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 1, '3010', '������Ŀ��Ŀ���������������˱�־', 'update fb_p_dlib_audit_opinion set audit_flag = 0 where div_code = ''#div_code#'' and prj_code=''#obj_code#'' and set_year=#budget_year# and data_type=#to_data_type# and batch_no=#batch_no# and audit_flag=1', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '3560', 'ɾ������֧����������1', 'delete  fb_u_payout_budget where set_year = #set_year# and rg_code = #rg_code#
and div_code =  ''#div_code#'' and batch_no = #batch_no# and data_type=#to_data_type# and ver_no=#ver_no#', 'DIV_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '3562', '���빫��֧����������1', 'insert into fb_u_payout_budget(row_id, en_id, div_kind, div_code, div_name, payout_kind_code, payout_kind_name, bs_id, acct_code,acct_name, bsi_id, acct_code_jj, acct_name_jj, prj_code, prj_name, stock_flag, payment_type, f1, f2,
f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24,
f25, f26, f27, f28, f29, f30, f31, f32, f33, f34, f35, f36, f37, f38, f39, f40, n1, n2, n3, n4, n5, c1,
c2, c3, c4, c5, f41, f42, d1, d2, set_year, opt_no, ver_no, batch_no, data_type, data_src, remark, rg_code,
last_ver, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, c6, c7,
c8, c9, c10, f61, f62, f63,f73, f74, f75, f76, f77, f78, f79,
f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98, f99, f100,
is_needappraise, dp_id, is_splited, detail_key)select newid row_id, en_id, div_kind, div_code, div_name, payout_kind_code, payout_kind_name, bs_id, acct_code,acct_name, bsi_id, acct_code_jj, acct_name_jj, prj_code, prj_name, stock_flag, payment_type, f1, f2,f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24,f25, f26, f27, f28, f29, f30, f31, f32, f33, f34, f35, f36, f37, f38, f39, f40, n1, n2, n3, n4, n5, c1,c2, c3, c4, c5, f41, f42, d1, d2, set_year, opt_no, ver_no, batch_no,#to_data_type# data_type, data_src, remark, rg_code,last_ver, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, c6, c7,c8, c9, c10, f61, f62, f63, f64, f65, f66, f67, f68, f69, f70, f71, f72, f73, f74, f75, f76, f77, f78, f79,f80, f81, f82, f83, f84, f85, f86, f87, f88, f89, f90, f91, f92, f93, f94, f95, f96, f97, f98, f99, f100,
is_needappraise, dp_id, is_splited, detail_key from fb_u_payout_budget where set_year = #set_year# and div_code =''#div_code#'' and BATCH_NO = #batch_no# and DATA_TYPE =#from_data_type# and rg_code = #rg_code# and ver_no=#ver_no#', 'DIV_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 1, '4002', '�Ӽ�Ч���۸��ύ�����˻���������λʱʹ��', 'update fb_p_dlib_base set is_needappraise = ''#is_needappraise#'' where prj_code = ''#obj_code#'' and set_year = #set_year# and rg_code = ''#rg_code#'' and data_type = #to_data_type# and batch_no = 1', 'PRJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3001', 'ר������', 'FB_P_BASE', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3002', 'ר����ϸ��', 'FB_P_DETAIL_MX', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3003', 'ר���ʽ���Դ��', 'FB_P_DETAIL_PFS', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3004', '������Ϣ����Ԫ��ֵ��', 'FB_B_REFELEMENTVALUE', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3005', '���㹫ʽֵ��', 'FB_U_DIV_PAYOUT_FORMULA_VALUE', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3013', '����Ԥ����Ŀ��Ч���۱�', 'FB_P_PRJPERFORMANCE', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3014', '��Ŀ����Ŀ��Ч���۱�', 'FB_P_DLIB_PRJPERFORMANCE', 'PRJ_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '8997', '���ڹ滮���ݱ�', 'FB_PLG_U_IR_DATA', 'div_code', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '8001', '����Ԥ��ר���', 'FB_FD_P_BASE', 'FD_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '8002', '���ݻ����˰���ռƻ���', 'FB_FD_U_DIV_INCOMING', 'FD_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '8003', '��������Ԥ���', 'FB_FD_U_DIV_INCOMING_BUDGET', 'FD_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '8004', '���ݻ���ר����ϸ', 'FB_FD_P_DETAIL_PFS', 'FD_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '8996', '���ڹ滮��������', 'FB_PLG_PAYOUT_BUDGET_RAE', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '8998', '���ڹ滮������ϸ��', 'FB_PLG_U_IR_DATA_DETAIL', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '8999', '���ڹ滮һ����ϸ�ҽӱ�', 'FB_PLG_ATTACH_PRJ', 'DIV_CODE', 1, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '9001', '����Ԥ��', 'fb_u_ir_data_2', 'div_code', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3026', '��Ŀ��������', 'FB_P_ADJUST_BILL', 'BILL_NUM', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3027', 'Ԥ������䶯����2', 'FB_P_ADJUST_DETAIL', 'OBJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3028', 'Ԥ�������Ŀ����2', 'FB_P_BASE', 'OBJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3029', 'Ԥ�������Ŀ�ʽ���Դ��2', 'FB_P_DETAIL_MX', 'OBJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3030', 'Ԥ�������Ŀ��ϸ��2', 'FB_P_DETAIL_PFS', 'OBJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3031', '����Ԥ����Ŀ��Ч���۱�2', 'FB_P_PRJPERFORMANCE', 'OBJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3032', 'Ԥ����������ɹ���2', 'FB_U_PAYOUT_GOV_PURCHASE', 'OBJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3033', '��Ŀ������ϸ��', 'FB_P_ADJUST_BILL_DETAIL', 'BILL_NUM', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 1, '3034', '��λ�ύֱ������', 'update fb_wf_flow_current set is_completed=2,data_type=1 where obj_code=''#obj_code#'' and set_year=#set_year# and rg_code=''#rg_code#''', 'DIV_CODE', null, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3035', '����-���þ��ѱ�', 'FB_U_PAYOUT_BUDGET', 'PRJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('101', 2, '3036', '����-���þ��ѱ�2', 'FB_U_PAYOUT_BUDGET', 'OBJ_CODE', 0, 1, 2016, '3700');

insert into SYS_WF_EXTEND_RULE (SYS_TYPE, RULE_TYPE, RULE_ID, RULE_NAME, RULE_CONTENT, RULE_FIELD, RULE_EXTEND_FLAG, IS_ENABLED, SET_YEAR, RG_CODE)
values ('102', 2, '3037', '��Ŀ�����ϼ�����¼����ϸ��', 'FB_ADJUST_BILL_NZCL_DETAIL', 'BILL_NUM', 0, 1, 2016, '3700');


--*********�������֣���ҵ���sys_metadata��������һ����¼����ʾԤ��������͡�


insert into sys_metadata (chr_id,field_code,field_name,field_disptype,field_valueset,is_system,create_date,latest_op_date,is_deleted,last_ver) VALUES ('{E57FBB0D-7C1C-4AB1-8144-3229C026C1C4}','RULE_TYPE','Ԥ�����','2',
'1#����+2#ִ��SQL+3#��˹�ʽID+4#��������+5#�������+6#�༭����+7#�������','0','2017-06-20 21:24:20','2017-06-20 21:24:20','0','2017-06-20 21:24:20');


--*********���Ĳ��֣���ҵ���sys_app��������������¼����ʾԤ��ϵͳ��ʶ��


insert into sys_app (SYS_GUID, SYS_ID, SYS_NAME, SYS_DESC, ENABLED, VERSION, DEPLOY_IP, DEPLOY_PORT, DEPLOY_FOLDER, DOWNLOAD_SERVLET, IS_LOCAL, ORG_TYPE, WELCOME_IMG, IS_OFFLINE, APPLICATION_HANDLE, LAST_VER, IS_DSTORE, SYS_YEAR, JAR_NAMES)
values ('{16369DB0-3EB1-4448-9B38-194AC8373E6D}', '101', 'Ԥ�����', 'Ԥ�����', 1, 'V8.0', '0.0.0.0', '', 'df', '', 1, '', '', 0, '', '', 0, '', '');

insert into sys_app (SYS_GUID, SYS_ID, SYS_NAME, SYS_DESC, ENABLED, VERSION, DEPLOY_IP, DEPLOY_PORT, DEPLOY_FOLDER, DOWNLOAD_SERVLET, IS_LOCAL, ORG_TYPE, WELCOME_IMG, IS_OFFLINE, APPLICATION_HANDLE, LAST_VER, IS_DSTORE, SYS_YEAR, JAR_NAMES)
values ('{B086C29E-7A0F-4C9E-818D-17C083CC7251}', '102', '��Ŀ��', '��Ŀ��', 1, 'V8.0', '0.0.0.0', '', 'df', '', 1, '', '', 0, '', '', 0, '', '');

insert into sys_app (SYS_GUID, SYS_ID, SYS_NAME, SYS_DESC, ENABLED, VERSION, DEPLOY_IP, DEPLOY_PORT, DEPLOY_FOLDER, DOWNLOAD_SERVLET, IS_LOCAL, ORG_TYPE, WELCOME_IMG, IS_OFFLINE, APPLICATION_HANDLE, LAST_VER, IS_DSTORE, SYS_YEAR, JAR_NAMES)
values ('{38DB88BD-A627-428E-BAD5-B34AB025F998}', '103', '��Աϵͳ', '��Աϵͳ', 1, 'V8.0', '0.0.0.0', '', 'df', '', 1, '', '', 0, '', '', 0, '', '');

