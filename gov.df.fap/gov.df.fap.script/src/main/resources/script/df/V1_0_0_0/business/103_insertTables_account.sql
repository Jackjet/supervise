begin
insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('001001', '21', '21', '国库清算账户', null, null, null, 'ele_account', 2, 1, '001', 'cz', 'cb', 'ele_account', 'BAC');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('001002', '22', '22', '国库实拨付款账户', null, null, null, 'ele_account', 2, 1, '001', 'cz', 'cb', 'ele_account', 'BAC');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('002', '002', '002', '支付账户', null, null, null, null, 1, 0, null, null, null, null, null);

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('002001', '11', '11', '直接支付财政零余额账户', null, null, null, 'ele_account', 2, 1, '002', 'cz', 'pb', 'ele_account', 'BAP');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('002002', '12', '12', '授权支付预算单位零余额账户', null, null, null, 'ele_account', 2, 1, '002', 'en', 'pb', 'ele_account', 'BAP');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('003', '003', '003', '收款账户', null, null, null, null, 1, 0, null, null, null, null, null);

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('003001', '61', '61', '预算单位工资统发账户', null, null, null, 'ele_payee_account', 2, 1, '003', 'cz', 'pb', 'ele_payee_account', 'PAYEE_ACCOUNT');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('003002', '62', '62', '预算单位实拨收款账户', null, null, null, 'ele_payee_account', 2, 1, '003', 'en', 'pb', 'ele_payee_account', 'PAYEE_ACCOUNT');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('003003', '63', '63', '预算单位普通收款账户', null, null, null, 'ele_payee_account', 2, 1, '003', 'en', 'pb', 'ele_payee_account', 'PAYEE_ACCOUNT');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('004', '004', '004', '代理银行账户', null, null, null, null, 1, 0, null, null, null, null, null);

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('004001', '31', '31', '代理银行清算账户', null, null, null, 'ele_account', 2, 1, '004', 'pb', 'cb', 'ele_account', 'AGENT_ACCOUNT');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('003004', '68', '68', '中央专项资金收款账户', null, null, null, 'ele_payee_account', 2, 1, '003', 'en', 'pb', 'ele_payee_account', 'PAYEE_ACCOUNT');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('003005', '64', '64', '公务卡汇总还款账户', null, null, null, 'ele_payee_account', 2, 1, '003', 'en', 'pb', 'ele_payee_account', 'PAYEE_ACCOUNT');

insert into ele_account_type (CHR_ID, CHR_CODE, DISP_CODE, CHR_NAME, OWNER_CODE, OWNER_FIELD, BANK_FIELD, ELE_SOURCE, LEVEL_NUM, IS_LEAF, PARENT_ID, OWNER_TYPE_ID, BANK_TYPE_ID, PHISCAL_SOURCE, ELE_CODE)
values ('003006', '82', '82', '预算单位网银收款账户', null, null, null, 'ele_payee_account', 2, 1, '003', 'en', 'pb', 'ele_payee_account', 'PAYEE_ACCOUNT');
