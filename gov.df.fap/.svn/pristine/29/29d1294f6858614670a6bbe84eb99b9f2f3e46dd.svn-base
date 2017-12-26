package gov.df.fap.service.util.ftp;

import gov.df.fap.service.util.sessionmanager.SessionUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 附件上传工具工厂
 * @author Administrator
 *
 */
public class AttachmentTransferFactory {

  public AttachmentTransferFactory() {
    super();
  }

  public static final String DEFAULT_UPLOAD = "0";

  public static final String FTP_UPLOAD = "1";

  public static AbstractAttachTransferHelper getUploadingHelper() {
    String uploadMode = (String) SessionUtil.getParaMap().get("UPLOAD_MODE");
    if (StringUtils.isEmpty(uploadMode) || DEFAULT_UPLOAD.equals(uploadMode))//改为不设置则使用默认上传模式
      return new DefaultAttachTransferHelper();
    else if (FTP_UPLOAD.equals(uploadMode))
      return new FtpAttachTransferHelper();
    else
      throw new RuntimeException("预算执行系统中附件上传模式参数配置有误!");
  }

}
