package gov.df.fap.service.util.exceptions.workflow;

public class WorkFlowExceptionConstants {

  public static final String WF_ERROR_SQL_OP = "数据库操作错误！";

  public static final String WF_ERROR_CONN = "数据库连接为空或已关闭！";

  public static final String NOT_FOUND_CURRENT_NODE = "数据未找到当前工作流节点！请检查工作流配置。";

  public static final String NOT_FOUND_PRE_NODE = "数据未找到当前工作流来源节点！请检查工作流配置。";

  public static final String NOT_FOUND_COMPLETE_NODE = "未找到可撤销流程节点信息";

  public static final String EXISTS_ = "未找到可撤销流程节点信息";

  public static final String NOT_FOUND_HIS_NODE = "工作流未找到历史节点数据";
}
