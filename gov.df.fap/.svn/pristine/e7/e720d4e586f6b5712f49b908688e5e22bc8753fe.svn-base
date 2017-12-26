package gov.df.fap.service.util.signer;

import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 签名服务器远程签名相关操作接口类
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
public interface IRemoteSigner {

  /**
   * 调用签名服务器，对数据进行签名，返回签名结果
   * @param data			待签名数据
   * @param dn			证书DN
   * @param iAlgorithm	签名算法 0表示MD5, 1 表示SHA1
   * @return				签名结果
   * @throws Exception 	异常错误
   */
  public byte[] signMessage(byte[] data, String dn, int iAlgorithm) throws Exception;

  /**
   * 调用签名服务器，对文件进行签名，返回签名结果
   * @param signFile		待签名的文件路径
   * @param dn			证书DN
   * @param iAlgorithm	签名算法 0表示MD5, 1 表示SHA1
   * @return				签名结果
   * @throws Exception	异常错误
   */
  public byte[] signFile(String signFile, String dn, int iAlgorithm) throws Exception;

  /**
   * 调用签名服务器，对数据进行验签，返回验证结果
   * @param data		原文数据
   * @param signData	签名信息
   * @param dn		证书DN
   * @return			0:验证结果错误   1：验证结果正确
   * @throws Exception  异常错误
   */
  public int verifyMessage(byte[] data, byte[] signData, String dn) throws Exception;

  /**
   * 验签 并返回验签后的证书信息
   * @param data
   * @param signData
   * @param dn
   * @return 吉大正元证书信息
   * @throws Exception
   */
  public Map getVerifyMessageCert(byte[] data, byte[] signData, String dn) throws Exception;

  /**
   * 调用签名服务器，对文件进行验签，返回验证结果
   * @param signFile	待验证原文件
   * @param signData	签名信息
   * @param dn		证书DN
   * @return		0:验证结果错误   1：验证结果正确
   * @throws Exception	异常错误
   */
  public int verifyMessage(String signFile, byte[] signData, String dn) throws Exception;

  /**
   * 对数据进行签名，返回签名结果
   * @param data			待签名数据
   * @param cert			X509证书
   * @param iAlgorithm	签名算法 0表示MD5, 1 表示SHA1
   * @return				P7签名数据包
   * @throws Exception
   */
  public byte[] signPKCS7(byte[] data, X509Certificate cert, int iAlgorithm) throws Exception;

  /**
   * 对数据进行验证，返回验签结果
   * @param p7data	PKCS7数据
   * @return			验签结果
   * @throws Exception
   */
  public int verifyPKCS7(byte[] p7data) throws Exception;

}
