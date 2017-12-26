package gov.df.fap.service.util.signer;

import java.security.cert.X509Certificate;

/**
 * 证书远程验证相关操作接口类
 * <p>
 * <p>
 * All rights reserved.
 * <p>
 * <p>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>
 * 侵权者将受到法律追究。
 * <p>
 * DERIVED FROM: NONE
 * <p>
 * PURPOSE:
 * <p>
 * DESCRIPTION:
 * <p>
 * UPDATE: 
 * <p>
 * DATE: 2017-04-01
 * <p>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author lindx
 * @since java 1.6.0.34
 */
public interface ICertVerify {

  /**
   * 验证签名证书是否由可信任的CA机构颁发
   * @param cert
   * @return	0：证书错误  1：证书正确
   * @throws Exception
   */
  public int verifyCertRoot(X509Certificate cert) throws Exception;

  /**
   * 验证签名证书是否在CRL列表上
   * @param cert
   * @return		0：证书错误  1：证书正确
   * @throws Exception
   */
  public int verifyCertCRL(X509Certificate cert) throws Exception;

  /**
   * 验证签名证书是否在有效期限内
   * @param cert	CA证书
   * @return		0：证书不在有效期限内  1：证书有效
   * @throws Exception
   */
  public int verifyCertDate(X509Certificate cert) throws Exception;

}
