begin
insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (51, '总待分指标明细COA', 30, 0, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (30, '处室待分指标明细COA', 51, 1, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (30, '处室待分指标明细COA', 114, 0, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (114, '部门待分指标明细COA', 30, 1, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (114, '部门待分指标明细COA', 59, 0, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (59, '单位预算明细COA', 114, 1, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (59, '单位预算明细COA', 22, 0, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (22, '单位可执行指标明细COA', 59, 1, 1, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (22, '单位可执行指标明细COA', 6, 0, 1, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (22, '单位可执行指标明细COA', 14, 0, 1, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (6, '用款计划额度COA', 22, 1, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (6, '用款计划额度COA', 73, 0, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (73, '计划明细COA', 6, 1, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (73, '计划明细COA', 43, 0, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (43, '用款申请COA', 73, 1, 0, '3700', 2016);

insert into gl_coa_cascade (COA_ID, COA_NAME, RELATION_COA_ID, IS_UP_STREAM, IS_BRANCH, RG_CODE, SET_YEAR)
values (14, '请款单COA', 22, 1, 0, '3700', 2016);

